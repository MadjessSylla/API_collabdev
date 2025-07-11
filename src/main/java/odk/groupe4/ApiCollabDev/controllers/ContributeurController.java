package odk.groupe4.ApiCollabDev.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import odk.groupe4.ApiCollabDev.dto.ContributeurDto;
import odk.groupe4.ApiCollabDev.dto.ContributeurResponseDto;
import odk.groupe4.ApiCollabDev.dto.ContributeurSoldeDto;
import odk.groupe4.ApiCollabDev.exception.GlobalExceptionHandler;
import odk.groupe4.ApiCollabDev.service.ContributeurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contributeurs")
@Tag(name = "Contributeurs", description = "API de gestion des contributeurs de la plateforme")
public class ContributeurController {
    
    private final ContributeurService contributeurService;

    @Autowired
    public ContributeurController(ContributeurService contributeurService) {
        this.contributeurService = contributeurService;
    }

    /*@Operation(
        summary = "Récupérer tous les contributeurs",
        description = "Retourne la liste complète de tous les contributeurs inscrits sur la plateforme"
    )
    @ApiResponse(
        responseCode = "200", 
        description = "Liste des contributeurs récupérée avec succès",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ContributeurResponseDto.class)
        )
    )*/
    @GetMapping
    public ResponseEntity<List<ContributeurResponseDto>> getAllContributeurs() {
        List<ContributeurResponseDto> contributeurs = contributeurService.getAllContributeurs();
        return ResponseEntity.ok(contributeurs);
    }

    /*@Operation(
        summary = "Récupérer un contributeur par ID",
        description = "Retourne les détails d'un contributeur spécifique"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Contributeur trouvé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ContributeurResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Contributeur non trouvé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        )
    })*/
    @GetMapping("/{id}")
    public ResponseEntity<ContributeurResponseDto> getContributeurById(
            @Parameter(description = "ID unique du contributeur", required = true, example = "1")
            @PathVariable int id) {
        ContributeurResponseDto contributeur = contributeurService.getContributeurById(id);
        return ResponseEntity.ok(contributeur);
    }

    /*@Operation(
        summary = "Créer un nouveau contributeur",
        description = "Inscrit un nouveau contributeur sur la plateforme avec attribution automatique de coins de bienvenue"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Contributeur créé avec succès",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ContributeurResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Données d'inscription invalides",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "409", 
            description = "Email ou téléphone déjà utilisé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        )
    })*/
    @PostMapping
    public ResponseEntity<ContributeurResponseDto> createContributeur(
            @Parameter(description = "Données du contributeur à inscrire", required = true)
            @Valid @RequestBody ContributeurDto contributeurDto) {
        ContributeurResponseDto createdContributeur = contributeurService.ajouterContributeur(contributeurDto);
        return new ResponseEntity<>(createdContributeur, HttpStatus.CREATED);
    }

    /*@Operation(
        summary = "Mettre à jour un contributeur",
        description = "Met à jour les informations personnelles d'un contributeur"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Contributeur mis à jour avec succès",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ContributeurResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Contributeur non trouvé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Données de mise à jour invalides",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        )
    })*/
    @PutMapping("/{id}")
    public ResponseEntity<ContributeurResponseDto> updateContributeur(
            @Parameter(description = "ID du contributeur à modifier", required = true, example = "1")
            @PathVariable int id,
            @Parameter(description = "Nouvelles données du contributeur", required = true)
            @Valid @RequestBody ContributeurDto contributeurDto) {
        ContributeurResponseDto contributeur = contributeurService.updateContributeur(id, contributeurDto);
        return ResponseEntity.ok(contributeur);
    }

    /*@Operation(
        summary = "Afficher le solde d'un contributeur",
        description = "Retourne le solde actuel en coins d'un contributeur spécifique"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Solde récupéré avec succès",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ContributeurSoldeDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Contributeur non trouvé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        )
    })*/
    @GetMapping("/{id}/solde")
    public ResponseEntity<ContributeurSoldeDto> getSoldeContributeur(
            @Parameter(description = "ID du contributeur", required = true, example = "1")
            @PathVariable int id) {
        ContributeurSoldeDto solde = contributeurService.afficherSoldeContributeur(id);
        return ResponseEntity.ok(solde);
    }

    /*@Operation(
        summary = "Désactiver un contributeur",
        description = "Désactive le compte d'un contributeur (le rend inactif)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Contributeur désactivé avec succès",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ContributeurResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Contributeur non trouvé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        )
    })*/
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ContributeurResponseDto> deactivateContributeur(
            @Parameter(description = "ID du contributeur à désactiver", required = true, example = "1")
            @PathVariable int id) {
        ContributeurResponseDto contributeur = contributeurService.deactivateContributeur(id);
        return ResponseEntity.ok(contributeur);
    }

    /*@Operation(
        summary = "Réactiver un contributeur",
        description = "Réactive le compte d'un contributeur (le rend actif)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Contributeur réactivé avec succès",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ContributeurResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Contributeur non trouvé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        )
    })*/
    @PatchMapping("/{id}/activate")
    public ResponseEntity<ContributeurResponseDto> activateContributeur(
            @Parameter(description = "ID du contributeur à réactiver", required = true, example = "1")
            @PathVariable int id) {
        ContributeurResponseDto contributeur = contributeurService.activateContributeur(id);
        return ResponseEntity.ok(contributeur);
    }
}
