package odk.groupe4.ApiCollabDev.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import odk.groupe4.ApiCollabDev.dto.BadgeCoinDescDto;
import odk.groupe4.ApiCollabDev.dto.BadgeRequestDto;
import odk.groupe4.ApiCollabDev.dto.BadgeResponseDto;
import odk.groupe4.ApiCollabDev.exception.GlobalExceptionHandler;
import odk.groupe4.ApiCollabDev.service.BadgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/badges")
@Tag(name = "Badges", description = "API de gestion des badges de récompense")
public class BadgeController {

    private final BadgeService badgeService;

    @Autowired
    public BadgeController(BadgeService badgeService) {
        this.badgeService = badgeService;
    }

   @Operation(
        summary = "Récupérer tous les badges",
        description = "Retourne la liste complète de tous les badges disponibles dans le système"
    )
    @ApiResponse(
        responseCode = "200", 
        description = "Liste des badges récupérée avec succès",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = BadgeResponseDto.class)
        )
    )
    // Affiche tous les badges
    @GetMapping
    public ResponseEntity<List<BadgeResponseDto>> getAllBadges() {
        List<BadgeResponseDto> badges = badgeService.obtenirTousLesBadges();
        return ResponseEntity.ok(badges);
    }

    @Operation(
        summary = "Récupérer un badge par ID",
        description = "Retourne les détails d'un badge spécifique"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Badge trouvé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BadgeResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Badge non trouvé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        )
    })
    // Récupère un badge par son ID
    @GetMapping("/{id}")
    public ResponseEntity<BadgeResponseDto> getBadgeById(
            @Parameter(description = "ID unique du badge", required = true, example = "1")
            @PathVariable int id) {
        BadgeResponseDto badge = badgeService.obtenirBadgeParId(id);
        return ResponseEntity.ok(badge);
    }

    @Operation(
        summary = "Créer un nouveau badge",
        description = "Crée un nouveau badge de récompense. Seuls les administrateurs peuvent créer des badges."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Badge créé avec succès",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BadgeResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Données de création invalides",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Administrateur non trouvé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        )
    })
    // Crée un nouveau badge
    @PostMapping("/admin/{idAdmin}")
    public ResponseEntity<BadgeResponseDto> createBadge(
            @Parameter(description = "ID de l'administrateur créateur", required = true, example = "1")
            @PathVariable int idAdmin,
            @Parameter(description = "Données du badge à créer", required = true)
            @Valid @RequestBody BadgeRequestDto badgeDto) {
        BadgeResponseDto createdBadge = badgeService.creerBadge(badgeDto, idAdmin);
        return new ResponseEntity<>(createdBadge, HttpStatus.CREATED);
    }

    @Operation(
        summary = "Mettre à jour un badge",
        description = "Met à jour toutes les informations d'un badge existant"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Badge mis à jour avec succès",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BadgeResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Badge non trouvé",
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
    // Met à jour un badge existant
    @PutMapping("/{id}")
    public ResponseEntity<BadgeResponseDto> updateBadge(
            @Parameter(description = "ID du badge à modifier", required = true, example = "1")
            @PathVariable int id,
            @Parameter(description = "Nouvelles données du badge", required = true)
            @Valid @RequestBody BadgeRequestDto badgeDto) {
        BadgeResponseDto badge = badgeService.mettreAJourBagde(id, badgeDto);
        return ResponseEntity.ok(badge);
    }

    @Operation(
        summary = "Mettre à jour partiellement un badge",
        description = "Met à jour uniquement la description et les coins de récompense d'un badge"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Badge mis à jour avec succès",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BadgeResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Badge non trouvé",
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
    // Met à jour partiellement un badge (description et coins)
    @PatchMapping("/{id}")
    public ResponseEntity<BadgeResponseDto> updateBadgePartial(
            @Parameter(description = "ID du badge à modifier", required = true, example = "1")
            @PathVariable int id,
            @Parameter(description = "Données partielles du badge (description et coins)", required = true)
            @Valid @RequestBody BadgeCoinDescDto dto) {
        BadgeResponseDto badge = badgeService.mettreAJourCoinEtDescription(id, dto);
        return ResponseEntity.ok(badge);
    }

    @Operation(
        summary = "Supprimer un badge",
        description = "Supprime définitivement un badge du système"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204", 
            description = "Badge supprimé avec succès"
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Badge non trouvé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        )
    })
    // Supprime un badge par son ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBadge(
            @Parameter(description = "ID du badge à supprimer", required = true, example = "1")
            @PathVariable int id) {
        badgeService.supprimerBadge(id);
        return ResponseEntity.noContent().build();
    }
}
