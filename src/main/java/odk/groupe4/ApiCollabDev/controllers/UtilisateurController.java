package odk.groupe4.ApiCollabDev.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import odk.groupe4.ApiCollabDev.dto.ContributeurRequestDto;
import odk.groupe4.ApiCollabDev.dto.LoginResponseDto;
import odk.groupe4.ApiCollabDev.dto.UtilisateurDto;
import odk.groupe4.ApiCollabDev.dto.UtilisateurResponseDto;
import odk.groupe4.ApiCollabDev.exception.GlobalExceptionHandler;
import odk.groupe4.ApiCollabDev.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentification", description = "API d'authentification et de gestion des comptes utilisateurs")
public class UtilisateurController {
    
    private final UtilisateurService utilisateurService;

    @Autowired
    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @Operation(
        summary = "Inscription d'un nouveau contributeur",
        description = "Permet à un nouvel utilisateur de s'inscrire en tant que contributeur sur la plateforme"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Inscription réussie",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UtilisateurResponseDto.class)
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
    })
    @PostMapping("/register")
    // Inscription d'un utilisateur
    public ResponseEntity<UtilisateurResponseDto> inscrire(
            @Parameter(description = "Données d'inscription du contributeur", required = true)
            @Valid @RequestBody ContributeurRequestDto dto) {
        UtilisateurResponseDto utilisateur = utilisateurService.inscrire(dto);
        return new ResponseEntity<>(utilisateur, HttpStatus.CREATED);
    }

    @Operation(
        summary = "Connexion d'un utilisateur",
        description = "Permet à un utilisateur existant de se connecter à la plateforme"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Connexion réussie",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoginResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Identifiants invalides",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Données de connexion invalides",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        )
    })
    @PostMapping("/login")
    // Connexion d'un utilisateur
    public ResponseEntity<LoginResponseDto> connecter(
            @Parameter(description = "Identifiants de connexion", required = true)
            @Valid @RequestBody UtilisateurDto utilisateurDto) {
        LoginResponseDto response = utilisateurService.connecter(utilisateurDto);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Déconnexion d'un utilisateur",
        description = "Permet à un utilisateur connecté de se déconnecter de la plateforme"
    )
    @ApiResponse(
        responseCode = "200", 
        description = "Déconnexion réussie"
    )
    @PostMapping("/logout")
    // Déconnexion d'un utilisateur
    public ResponseEntity<String> deconnecter() {
        // Logique de déconnexion (invalidation de token, etc.)
        return ResponseEntity.ok("Déconnexion réussie");
    }

    @Operation(
        summary = "Récupérer le profil utilisateur",
        description = "Retourne les informations du profil de l'utilisateur connecté"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Profil récupéré avec succès",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UtilisateurResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Utilisateur non trouvé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        )
    })
    // Récupérer le profil de l'utilisateur connecté
    @GetMapping("/profile/{id}")
    // Récupérer le profil d'un utilisateur par son ID
    public ResponseEntity<UtilisateurResponseDto> getProfile(
            @Parameter(description = "ID de l'utilisateur", required = true, example = "1")
            @PathVariable int id) {
        UtilisateurResponseDto utilisateur = utilisateurService.getProfile(id);
        return ResponseEntity.ok(utilisateur);
    }

    @Operation(
        summary = "Changer le mot de passe",
        description = "Permet à un utilisateur de changer son mot de passe"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Mot de passe changé avec succès"
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Ancien mot de passe incorrect ou nouveau mot de passe invalide",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Utilisateur non trouvé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
            )
        )
    })
    // Changer le mot de passe de l'utilisateur
    @PatchMapping("/{id}/change-password")
    // Changer le mot de passe d'un utilisateur par son ID
    public ResponseEntity<String> changerMotDePasse(
            @Parameter(description = "ID de l'utilisateur", required = true, example = "1")
            @PathVariable int id,
            @Parameter(description = "Ancien mot de passe", required = true)
            @RequestParam String ancienMotDePasse,
            @Parameter(description = "Nouveau mot de passe", required = true)
            @RequestParam String nouveauMotDePasse) {
        utilisateurService.changerMotDePasse(id, ancienMotDePasse, nouveauMotDePasse);
        return ResponseEntity.ok("Mot de passe changé avec succès");
    }
}
