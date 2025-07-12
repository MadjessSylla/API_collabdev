package odk.groupe4.ApiCollabDev.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import odk.groupe4.ApiCollabDev.dto.ParametreCoinDto;
import odk.groupe4.ApiCollabDev.dto.ParametreCoinResponseDto;
import odk.groupe4.ApiCollabDev.exception.GlobalExceptionHandler;
import odk.groupe4.ApiCollabDev.service.ParametreCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parametres-coins")
@Tag(name = "Paramètres Coins", description = "API de gestion des paramètres de système de coins")
public class ParametreCoinController {

    private final ParametreCoinService parametreCoinService;

    @Autowired
    public ParametreCoinController(ParametreCoinService parametreCoinService) {
        this.parametreCoinService = parametreCoinService;
    }

    @Operation(
        summary = "Récupérer tous les paramètres de coins",
        description = "Retourne la liste complète de tous les paramètres de coins configurés dans le système"
    )
    @ApiResponse(
        responseCode = "200", 
        description = "Liste des paramètres récupérée avec succès",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ParametreCoinResponseDto.class)
        )
    )
    // Affiche tous les paramètres de coins
    @GetMapping
    public ResponseEntity<List<ParametreCoinResponseDto>> getAllParametresCoins() {
        List<ParametreCoinResponseDto> parametres = parametreCoinService.obtenirTousLesParametresCoins();
        return ResponseEntity.ok(parametres);
    }

    @Operation(
        summary = "Récupérer un paramètre de coin par ID",
        description = "Retourne les détails d'un paramètre de coin spécifique"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Paramètre trouvé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ParametreCoinResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Paramètre non trouvé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        )
    })
    // Récupère un paramètre de coin par son ID
    @GetMapping("/{id}")
    public ResponseEntity<ParametreCoinResponseDto> getParametreCoinById(
            @Parameter(description = "ID unique du paramètre", required = true, example = "1")
            @PathVariable int id) {
        ParametreCoinResponseDto parametre = parametreCoinService.getParametreCoinById(id);
        return ResponseEntity.ok(parametre);
    }

    @Operation(
        summary = "Créer un nouveau paramètre de coin",
        description = "Crée un nouveau paramètre de coin dans le système. Seuls les administrateurs peuvent créer des paramètres."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Paramètre créé avec succès",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ParametreCoinResponseDto.class)
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
    // Crée un nouveau paramètre de coin
    @PostMapping("/admin/{idAdmin}")
    public ResponseEntity<ParametreCoinResponseDto> createParametreCoin(
            @Parameter(description = "ID de l'administrateur créateur", required = true, example = "1")
            @PathVariable int idAdmin,
            @Parameter(description = "Données du paramètre à créer", required = true)
            @Valid @RequestBody ParametreCoinDto dto) {
        ParametreCoinResponseDto parametre = parametreCoinService.creerParametreCoin(idAdmin, dto);
        return new ResponseEntity<>(parametre, HttpStatus.CREATED);
    }

    @Operation(
        summary = "Mettre à jour un paramètre de coin",
        description = "Met à jour les informations d'un paramètre de coin existant"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Paramètre mis à jour avec succès",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ParametreCoinResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Paramètre non trouvé",
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
    // Met à jour un paramètre de coin existant
    @PutMapping("/{id}")
    public ResponseEntity<ParametreCoinResponseDto> updateParametreCoin(
            @Parameter(description = "ID du paramètre à modifier", required = true, example = "1")
            @PathVariable int id,
            @Parameter(description = "Nouvelles données du paramètre", required = true)
            @Valid @RequestBody ParametreCoinDto dto) {
        ParametreCoinResponseDto parametre = parametreCoinService.modifierParametreCoin(id, dto);
        return ResponseEntity.ok(parametre);
    }

    @Operation(
        summary = "Supprimer un paramètre de coin",
        description = "Supprime définitivement un paramètre de coin du système"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204", 
            description = "Paramètre supprimé avec succès"
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Paramètre non trouvé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        )
    })
    // Supprime un paramètre de coin par son ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParametreCoin(
            @Parameter(description = "ID du paramètre à supprimer", required = true, example = "1")
            @PathVariable int id) {
        parametreCoinService.supprimerParametreCoin(id);
        return ResponseEntity.noContent().build();
    }
}
