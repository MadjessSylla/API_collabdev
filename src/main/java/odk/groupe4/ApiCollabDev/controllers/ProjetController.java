package odk.groupe4.ApiCollabDev.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import odk.groupe4.ApiCollabDev.dto.ProjetCahierDto;
import odk.groupe4.ApiCollabDev.dto.ProjetDto;
import odk.groupe4.ApiCollabDev.dto.ProjetResponseDto;
import odk.groupe4.ApiCollabDev.exception.GlobalExceptionHandler;
import odk.groupe4.ApiCollabDev.models.enums.ProjectDomain;
import odk.groupe4.ApiCollabDev.models.enums.ProjectLevel;
import odk.groupe4.ApiCollabDev.models.enums.ProjectSector;
import odk.groupe4.ApiCollabDev.models.enums.ProjectStatus;
import odk.groupe4.ApiCollabDev.service.ProjetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projets")
@Tag(name = "Projets", description = "API de gestion des projets collaboratifs")
public class ProjetController {
    
    private final ProjetService projetService;

    @Autowired
    public ProjetController(ProjetService projetService) {
        this.projetService = projetService;
    }

   @Operation(
        summary = "Récupérer tous les projets",
        description = "Retourne la liste complète de tous les projets avec possibilité de filtrage par statut"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Liste des projets récupérée avec succès",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProjetResponseDto.class)
            )
        )
    })
    @GetMapping
    // Récupérer tous les projets avec option de filtrage par statut
    public ResponseEntity<List<ProjetResponseDto>> getAllProjets(
            @Parameter(description = "Filtrer par statut du projet", required = false)
            @RequestParam(required = false) ProjectStatus status) {
        List<ProjetResponseDto> projets = projetService.getAllProjets(status);
        return ResponseEntity.ok(projets);
    }

    @Operation(
            summary = "Récupérer tous les projets ouverts",
            description = "Retourne la liste de tous les projets avec le statut OUVERT, avec possibilité de filtrage par domaine et secteur"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des projets ouverts récupérée avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjetResponseDto.class)
                    )
            )
    })
    @GetMapping("/ouverts")
    public ResponseEntity<List<ProjetResponseDto>> getProjetsOuverts(
            @Parameter(description = "Filtrer par domaine du projet", required = false)
            @RequestParam(required = false) ProjectDomain domaine,
            @Parameter(description = "Filtrer par secteur du projet", required = false)
            @RequestParam(required = false) ProjectSector secteur) {
        List<ProjetResponseDto> projets = projetService.getProjetsOuverts(domaine, secteur);
        return ResponseEntity.ok(projets);
    }

    @Operation(
            summary = "Filtrer les projets par domaine",
            description = "Retourne tous les projets d'un domaine spécifique"
    )
    @GetMapping("/domaine/{domaine}")
    public ResponseEntity<List<ProjetResponseDto>> getProjetsByDomaine(
            @Parameter(description = "Domaine du projet", required = true)
            @PathVariable ProjectDomain domaine) {
        List<ProjetResponseDto> projets = projetService.getProjetsByDomaine(domaine);
        return ResponseEntity.ok(projets);
    }

    @Operation(
            summary = "Filtrer les projets par secteur",
            description = "Retourne tous les projets d'un secteur spécifique"
    )
    @GetMapping("/secteur/{secteur}")
    public ResponseEntity<List<ProjetResponseDto>> getProjetsBySecteur(
            @Parameter(description = "Secteur du projet", required = true)
            @PathVariable ProjectSector secteur) {
        List<ProjetResponseDto> projets = projetService.getProjetsBySecteur(secteur);
        return ResponseEntity.ok(projets);
    }

    @Operation(
        summary = "Récupérer un projet par ID",
        description = "Retourne les détails complets d'un projet spécifique"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Projet trouvé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProjetResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Projet non trouvé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        )
    })
    // Récupérer un projet par son ID
    @GetMapping("/{id}")
    public ResponseEntity<ProjetResponseDto> getProjetById(
            @Parameter(description = "ID unique du projet", required = true, example = "1")
            @PathVariable int id) {
        ProjetResponseDto projet = projetService.getProjetById(id);
        return ResponseEntity.ok(projet);
    }

    @Operation(
            summary = "Afficher les projets par contributeur",
            description = "Retourne la liste des projets créés par un contributeur spécifique"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des projets récupérée avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjetResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Aucun projet trouvé pour ce contributeur",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    // Afficher la liste des projets créés par un contributeur
    @GetMapping("/contributeur/{idContributeur}")
    public ResponseEntity<List<ProjetResponseDto>> getProjetsByContributeur(
            @Parameter(description = "ID du contributeur", required = true, example = "1")
            @PathVariable int idContributeur) {
        List<ProjetResponseDto> projets = projetService.getProjetsByContributeur(idContributeur);
        return ResponseEntity.ok(projets);
    }

    @Operation(
        summary = "Proposer un nouveau projet",
        description = "Permet à un contributeur de proposer un nouveau projet collaboratif"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Projet proposé avec succès",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProjetResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Données du projet invalides",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Contributeur porteur de projet non trouvé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        )
    })
    // Proposer un nouveau projet par un contributeur
    @PostMapping("/contributeur/{idPorteurProjet}")
    public ResponseEntity<ProjetResponseDto> proposerProjet(
            @Parameter(description = "ID du contributeur porteur du projet", required = true, example = "1")
            @PathVariable int idPorteurProjet,
            @Parameter(description = "Données du projet à proposer", required = true)
            @Valid @RequestBody ProjetDto projetDto) {
        ProjetResponseDto projet = projetService.proposerProjet(projetDto, idPorteurProjet);
        return new ResponseEntity<>(projet, HttpStatus.CREATED);
    }

    @Operation(
        summary = "Valider un projet",
        description = "Permet à un administrateur de valider un projet proposé (change le statut à OUVERT)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Projet validé avec succès",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProjetResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Projet ou administrateur non trouvé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Le projet ne peut pas être validé dans son état actuel",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        )
    })
    // Valider un projet proposé par un contributeur
    @PatchMapping("/{id}/validate/admin/{idAdmin}")
    public ResponseEntity<ProjetResponseDto> validerProjet(
            @Parameter(description = "ID du projet à valider", required = true, example = "1")
            @PathVariable int id,
            @Parameter(description = "ID de l'administrateur validateur", required = true, example = "1")
            @PathVariable int idAdmin) {
        ProjetResponseDto projet = projetService.validerProjet(id, idAdmin);
        return ResponseEntity.ok(projet);
    }

    @Operation(
        summary = "Rejeter un projet",
        description = "Permet à un administrateur de rejeter un projet proposé (supprime le projet)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204", 
            description = "Projet rejeté et supprimé avec succès"
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Projet ou administrateur non trouvé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        )
    })
    // Rejeter un projet proposé par un contributeur
    @DeleteMapping("/{id}/reject/admin/{idAdmin}")
    public ResponseEntity<Void> rejeterProjet(
            @Parameter(description = "ID du projet à rejeter", required = true, example = "1")
            @PathVariable int id,
            @Parameter(description = "ID de l'administrateur", required = true, example = "1")
            @PathVariable int idAdmin) {
        projetService.rejeterProjet(id, idAdmin);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Éditer le cahier des charges",
        description = "Met à jour l'URL du cahier des charges d'un projet"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Cahier des charges mis à jour avec succès",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProjetResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Projet non trouvé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "URL du cahier des charges invalide",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        )
    })
    // Éditer le cahier des charges d'un projet
    @PatchMapping("/{id}/cahier-charges")
    public ResponseEntity<ProjetResponseDto> editerCahierDeCharge(
            @Parameter(description = "ID du projet", required = true, example = "1")
            @PathVariable int id,
            @Parameter(description = "Nouvelle URL du cahier des charges", required = true)
            @Valid @RequestBody ProjetCahierDto projetCahierDto) {
        ProjetResponseDto projet = projetService.editerCahierDeCharge(projetCahierDto, id);
        return ResponseEntity.ok(projet);
    }

    @Operation(
        summary = "Attribuer un niveau de complexité",
        description = "Permet à un administrateur d'attribuer un niveau de complexité à un projet"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Niveau attribué avec succès",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProjetResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Projet ou administrateur non trouvé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Niveau invalide ou projet déjà nivelé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        )
    })
    // Attribuer un niveau de complexité à un projet
    @PatchMapping("/{id}/niveau/admin/{idAdmin}")
    public ResponseEntity<ProjetResponseDto> attribuerNiveau(
            @Parameter(description = "ID du projet", required = true, example = "1")
            @PathVariable int id,
            @Parameter(description = "ID de l'administrateur", required = true, example = "1")
            @PathVariable int idAdmin,
            @Parameter(description = "Niveau de complexité à attribuer", required = true)
            @RequestParam ProjectLevel niveau) {
        ProjetResponseDto projet = projetService.attribuerNiveau(id, idAdmin, niveau);
        return ResponseEntity.ok(projet);
    }

    @Operation(
        summary = "Démarrer un projet",
        description = "Change le statut d'un projet validé vers EN_COURS"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Projet démarré avec succès",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProjetResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Projet non trouvé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Le projet ne peut pas être démarré dans son état actuel",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        )
    })
    // Démarrer un projet en changeant son statut à EN_COURS
    @PatchMapping("/{id}/start")
    public ResponseEntity<ProjetResponseDto> demarrerProjet(
            @Parameter(description = "ID du projet à démarrer", required = true, example = "1")
            @PathVariable int id) {
        ProjetResponseDto projet = projetService.demarrerProjet(id);
        return ResponseEntity.ok(projet);
    }

    @Operation(
        summary = "Terminer un projet",
        description = "Marque un projet comme terminé"
    )
    @ApiResponse(
        responseCode = "200", 
        description = "Projet terminé avec succès",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ProjetResponseDto.class)
        )
    )
    // Terminer un projet en changeant son statut à TERMINÉ
    @PatchMapping("/{id}/complete")
    public ResponseEntity<ProjetResponseDto> terminerProjet(
            @Parameter(description = "ID du projet à terminer", required = true, example = "1")
            @PathVariable int id) {
        ProjetResponseDto projet = projetService.terminerProjet(id);
        return ResponseEntity.ok(projet);
    }
}
