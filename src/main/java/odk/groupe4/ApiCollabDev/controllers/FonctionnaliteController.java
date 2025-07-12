package odk.groupe4.ApiCollabDev.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import odk.groupe4.ApiCollabDev.dto.FonctionnaliteDto;
import odk.groupe4.ApiCollabDev.dto.FonctionnaliteNewDto;
import odk.groupe4.ApiCollabDev.dto.FonctionnaliteResponseDto;
import odk.groupe4.ApiCollabDev.exception.GlobalExceptionHandler;
import odk.groupe4.ApiCollabDev.models.enums.FeaturesStatus;
import odk.groupe4.ApiCollabDev.service.FonctionnaliteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fonctionnalites")
@Tag(name = "Fonctionnalités", description = "API de gestion des fonctionnalités des projets")
public class FonctionnaliteController {
    
    private final FonctionnaliteService fonctionnaliteService;

    @Autowired
    public FonctionnaliteController(FonctionnaliteService fonctionnaliteService) {
        this.fonctionnaliteService = fonctionnaliteService;
    }

    @Operation(
        summary = "Créer une nouvelle fonctionnalité",
        description = "Ajoute une nouvelle fonctionnalité à un projet existant"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Fonctionnalité créée avec succès",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FonctionnaliteResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Données de fonctionnalité invalides",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
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
    // Crée une nouvelle fonctionnalité pour un projet spécifique
    @PostMapping("/projet/{idProjet}")
    public ResponseEntity<FonctionnaliteResponseDto> createFonctionnalite(
            @Parameter(description = "ID du projet", required = true, example = "1")
            @PathVariable int idProjet,
            @Parameter(description = "Données de la fonctionnalité à créer", required = true)
            @Valid @RequestBody FonctionnaliteNewDto fonctionnaliteDto) {
        FonctionnaliteResponseDto fonctionnalite = fonctionnaliteService.ajouterFonctionnalite(idProjet, fonctionnaliteDto);
        return new ResponseEntity<>(fonctionnalite, HttpStatus.CREATED);
    }

    @Operation(
        summary = "Mettre à jour une fonctionnalité",
        description = "Met à jour les informations d'une fonctionnalité existante"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Fonctionnalité mise à jour avec succès",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FonctionnaliteResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Fonctionnalité non trouvée",
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
    })
    // Met à jour une fonctionnalité existante
    @PutMapping("/{id}")
    public ResponseEntity<FonctionnaliteResponseDto> updateFonctionnalite(
            @Parameter(description = "ID de la fonctionnalité", required = true, example = "1")
            @PathVariable int id,
            @Parameter(description = "Nouvelles données de la fonctionnalité", required = true)
            @Valid @RequestBody FonctionnaliteNewDto fonctionnaliteDto) {
        FonctionnaliteResponseDto fonctionnalite = fonctionnaliteService.updateFonctionnalite(id, fonctionnaliteDto);
        return ResponseEntity.ok(fonctionnalite);
    }

    @Operation(
        summary = "Récupérer les fonctionnalités d'un projet",
        description = "Retourne toutes les fonctionnalités associées à un projet spécifique"
    )
    @ApiResponse(
        responseCode = "200", 
        description = "Fonctionnalités du projet récupérées avec succès",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = FonctionnaliteResponseDto.class)
        )
    )
    // Récupère toutes les fonctionnalités d'un projet spécifique
    @GetMapping("/projet/{idProjet}")
    public ResponseEntity<List<FonctionnaliteResponseDto>> getFonctionnalitesByProjet(
            @Parameter(description = "ID du projet", required = true, example = "1")
            @PathVariable int idProjet) {
        List<FonctionnaliteResponseDto> fonctionnalites = fonctionnaliteService.getFonctionnalitesByProjet(idProjet);
        return ResponseEntity.ok(fonctionnalites);
    }

    @Operation(
        summary = "Supprimer une fonctionnalité",
        description = "Supprime définitivement une fonctionnalité du système"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204", 
            description = "Fonctionnalité supprimée avec succès"
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Fonctionnalité non trouvée",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        )
    })
    // Supprime une fonctionnalité par son ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFonctionnalite(
            @Parameter(description = "ID de la fonctionnalité à supprimer", required = true, example = "1")
            @PathVariable int id) {
        fonctionnaliteService.deleteFonctionnalite(id);
        return ResponseEntity.noContent().build();
    }

   /*@Operation(
        summary = "Récupérer toutes les fonctionnalités",
        description = "Retourne la liste complète de toutes les fonctionnalités avec possibilité de filtrage par statut"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Liste des fonctionnalités récupérée avec succès",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FonctionnaliteResponseDto.class)
            )
        )
    })
    @GetMapping
    public ResponseEntity<List<FonctionnaliteResponseDto>> getAllFonctionnalites(
            @Parameter(description = "Filtrer par statut de fonctionnalité", required = false)
            @RequestParam(required = false) FeaturesStatus status) {
        List<FonctionnaliteResponseDto> fonctionnalites = fonctionnaliteService.getAllFonctionnalites(status);
        return ResponseEntity.ok(fonctionnalites);
    }

    @Operation(
        summary = "Récupérer une fonctionnalité par ID",
        description = "Retourne les détails d'une fonctionnalité spécifique"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Fonctionnalité trouvée",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FonctionnaliteResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Fonctionnalité non trouvée",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<FonctionnaliteResponseDto> getFonctionnaliteById(
            @Parameter(description = "ID unique de la fonctionnalité", required = true, example = "1")
            @PathVariable int id) {
        FonctionnaliteResponseDto fonctionnalite = fonctionnaliteService.getFonctionnaliteById(id);
        return ResponseEntity.ok(fonctionnalite);
    }
    @Operation(
        summary = "Changer le statut d'une fonctionnalité",
        description = "Met à jour le statut d'une fonctionnalité (A_FAIRE, EN_COURS, TERMINE)"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Statut mis à jour avec succès",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = FonctionnaliteResponseDto.class)
        )
    )
    // Change le statut d'une fonctionnalité
    @PatchMapping("/{id}/status")
    public ResponseEntity<FonctionnaliteResponseDto> updateStatusFonctionnalite(
            @Parameter(description = "ID de la fonctionnalité", required = true, example = "1")
            @PathVariable int id,
            @Parameter(description = "Nouveau statut", required = true)
            @RequestParam FeaturesStatus status) {
        FonctionnaliteResponseDto fonctionnalite = fonctionnaliteService.updateStatus(id, status);
        return ResponseEntity.ok(fonctionnalite);
    }*/
}
