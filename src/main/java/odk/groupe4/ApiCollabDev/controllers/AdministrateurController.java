package odk.groupe4.ApiCollabDev.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import odk.groupe4.ApiCollabDev.dto.AdministrateurRequestDto;
import odk.groupe4.ApiCollabDev.dto.AdministrateurResponseDto;
import odk.groupe4.ApiCollabDev.exception.GlobalExceptionHandler;
import odk.groupe4.ApiCollabDev.service.AdministrateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/administrateurs")
@Tag(name = "Administrateurs", description = "API de gestion des administrateurs du système")
public class AdministrateurController {
    
    private final AdministrateurService administrateurService;
    
    @Autowired
    public AdministrateurController(AdministrateurService administrateurService) {
        this.administrateurService = administrateurService;
    }

   @Operation(
        summary = "Récupérer tous les administrateurs",
        description = "Retourne la liste complète de tous les administrateurs du système"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Liste des administrateurs récupérée avec succès",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AdministrateurResponseDto.class)
            )
        )
    })
    // Affiche tous les administrateurs
    @GetMapping
    public ResponseEntity<List<AdministrateurResponseDto>> getAllAdministrateurs() {
        List<AdministrateurResponseDto> admins = administrateurService.getAll();
        return ResponseEntity.ok(admins);
    }

    @Operation(
        summary = "Récupérer un administrateur par ID",
        description = "Retourne les détails d'un administrateur spécifique"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Administrateur trouvé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AdministrateurResponseDto.class)
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
    // Affiche un administrateur par son ID
    @GetMapping("/{id}")
    public ResponseEntity<AdministrateurResponseDto> getAdministrateurById(
            @Parameter(description = "ID unique de l'administrateur", required = true, example = "1")
            @PathVariable Integer id) {
        AdministrateurResponseDto admin = administrateurService.getById(id);
        return ResponseEntity.ok(admin);
    }

    @Operation(
        summary = "Créer un nouvel administrateur",
        description = "Crée un nouveau compte administrateur dans le système"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Administrateur créé avec succès",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AdministrateurResponseDto.class)
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
            responseCode = "409", 
            description = "Email déjà utilisé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        )
    })
    // Crée un nouvel administrateur
    @PostMapping
    public ResponseEntity<AdministrateurResponseDto> createAdministrateur(
            @Parameter(description = "Données de l'administrateur à créer", required = true)
            @Valid @RequestBody AdministrateurRequestDto adminDto) {
        AdministrateurResponseDto admin = administrateurService.create(adminDto);
        return new ResponseEntity<>(admin, HttpStatus.CREATED);
    }

    @Operation(
        summary = "Mettre à jour un administrateur",
        description = "Met à jour les informations d'un administrateur existant"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Administrateur mis à jour avec succès",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AdministrateurResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Administrateur non trouvé",
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
    // Met à jour un administrateur existant
    @PutMapping("/{id}")
    public ResponseEntity<AdministrateurResponseDto> updateAdministrateur(
            @Parameter(description = "ID de l'administrateur à modifier", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "Nouvelles données de l'administrateur", required = true)
            @Valid @RequestBody AdministrateurRequestDto dto) {
        AdministrateurResponseDto admin = administrateurService.update(id, dto);
        return ResponseEntity.ok(admin);
    }

    @Operation(
        summary = "Supprimer un administrateur",
        description = "Supprime définitivement un administrateur du système"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204", 
            description = "Administrateur supprimé avec succès"
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
    // Supprime un administrateur par son ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdministrateur(
            @Parameter(description = "ID de l'administrateur à supprimer", required = true, example = "1")
            @PathVariable Integer id) {
        administrateurService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Bloquer un administrateur",
        description = "Désactive le compte d'un administrateur (le rend inactif)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Administrateur bloqué avec succès",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AdministrateurResponseDto.class)
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
    // Bloque un administrateur par son ID
    @PatchMapping("/{id}/block")
    public ResponseEntity<AdministrateurResponseDto> blockAdministrateur(
            @Parameter(description = "ID de l'administrateur à bloquer", required = true, example = "1")
            @PathVariable Integer id) {
        AdministrateurResponseDto admin = administrateurService.block(id);
        return ResponseEntity.ok(admin);
    }

    @Operation(
        summary = "Débloquer un administrateur",
        description = "Réactive le compte d'un administrateur (le rend actif)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Administrateur débloqué avec succès",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AdministrateurResponseDto.class)
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
    // Débloque un administrateur par son ID
    @PatchMapping("/{id}/unblock")
    public ResponseEntity<AdministrateurResponseDto> unblockAdministrateur(
            @Parameter(description = "ID de l'administrateur à débloquer", required = true, example = "1")
            @PathVariable Integer id) {
        AdministrateurResponseDto admin = administrateurService.unblock(id);
        return ResponseEntity.ok(admin);
    }
}
