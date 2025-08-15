package odk.groupe4.ApiCollabDev.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import odk.groupe4.ApiCollabDev.dto.ContributionDto;
import odk.groupe4.ApiCollabDev.dto.ContributionResponseDto;
import odk.groupe4.ApiCollabDev.dto.ContributionSoumiseDto;
import odk.groupe4.ApiCollabDev.exception.GlobalExceptionHandler;
import odk.groupe4.ApiCollabDev.models.enums.ContributionStatus;
import odk.groupe4.ApiCollabDev.service.ContributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contributions")
@Tag(name = "Contributions", description = "API de gestion des contributions aux projets")
public class ContributionController {

    private final ContributionService contributionService;

    @Autowired
    public ContributionController(ContributionService contributionService) {
        this.contributionService = contributionService;
    }

    @Operation(
            summary = "Récupérer toutes les contributions",
            description = "Retourne la liste complète de toutes les contributions avec possibilité de filtrage par statut"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des contributions récupérée avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ContributionDto.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<ContributionDto>> getAllContributions(
            @Parameter(description = "Filtrer par statut de contribution", required = false)
            @RequestParam(required = false) ContributionStatus status) {
        List<ContributionDto> contributions = contributionService.afficherLaListeDesContribution(status);
        return ResponseEntity.ok(contributions);
    }

    @Operation(
            summary = "Récupérer une contribution par ID",
            description = "Retourne les détails d'une contribution spécifique"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Contribution trouvée",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ContributionResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Contribution non trouvée",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ContributionResponseDto> getContributionById(
            @Parameter(description = "ID unique de la contribution", required = true, example = "1")
            @PathVariable int id) {
        ContributionResponseDto contribution = contributionService.getContributionById(id);
        return ResponseEntity.ok(contribution);
    }

    @Operation(
            summary = "Soumettre une nouvelle contribution",
            description = "Permet à un participant de soumettre une contribution pour une fonctionnalité spécifique avec un fichier optionnel"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Contribution soumise avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ContributionResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données de contribution invalides ou erreur de téléversement",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Participant ou fonctionnalité non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    @PostMapping(value = "/fonctionnalite/{idFonctionnalite}/participant/{idParticipant}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ContributionResponseDto> soumettreContribution(
            @Parameter(description = "ID de la fonctionnalité", required = true, example = "1")
            @PathVariable int idFonctionnalite,
            @Parameter(description = "ID du participant", required = true, example = "1")
            @PathVariable int idParticipant,
            @Parameter(description = "Données de la contribution", required = true)
            @RequestPart("contribution") @Valid ContributionSoumiseDto contributionDto,
            @Parameter(description = "Fichier de la contribution (optionnel)", required = false)
            @RequestPart(value = "fichier", required = false) MultipartFile fichier) {

        ContributionResponseDto contribution = contributionService.soumettreContribution(
                idFonctionnalite, idParticipant, contributionDto, fichier);
        return new ResponseEntity<>(contribution, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Valider ou rejeter une contribution",
            description = "Permet à un gestionnaire de valider ou rejeter une contribution soumise"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Statut de contribution mis à jour avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ContributionResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Statut invalide ou gestionnaire non autorisé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Contribution ou gestionnaire non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    @PutMapping("/{id}/status")
    public ResponseEntity<ContributionResponseDto> updateContributionStatus(
            @Parameter(description = "ID de la contribution", required = true, example = "1")
            @PathVariable int id,
            @Parameter(description = "Nouveau statut de la contribution", required = true)
            @RequestParam ContributionStatus status,
            @Parameter(description = "ID du gestionnaire", required = true, example = "1")
            @RequestParam int gestionnaireId) {
        ContributionResponseDto contribution = contributionService.MiseAJourStatutContribution(id, status, gestionnaireId);
        return ResponseEntity.ok(contribution);
    }

    @Operation(
            summary = "Récupérer les contributions d'un participant",
            description = "Retourne toutes les contributions soumises par un participant spécifique"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Contributions du participant récupérées avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ContributionDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Participant non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/participant/{participantId}")
    public ResponseEntity<List<ContributionDto>> getContributionsByParticipant(
            @Parameter(description = "ID du participant", required = true, example = "1")
            @PathVariable int participantId) {
        List<ContributionDto> contributions = contributionService.getContributionsByParticipant(participantId);
        return ResponseEntity.ok(contributions);
    }

    @Operation(
            summary = "Récupérer les contributions d'une fonctionnalité",
            description = "Retourne toutes les contributions liées à une fonctionnalité spécifique"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Contributions de la fonctionnalité récupérées avec succès",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ContributionDto.class)
            )
    )
    @GetMapping("/fonctionnalite/{fonctionnaliteId}")
    public ResponseEntity<List<ContributionDto>> getContributionsByFonctionnalite(
            @Parameter(description = "ID de la fonctionnalité", required = true, example = "1")
            @PathVariable int fonctionnaliteId) {
        List<ContributionDto> contributions = contributionService.getContributionsByFonctionnalite(fonctionnaliteId);
        return ResponseEntity.ok(contributions);
    }
}
