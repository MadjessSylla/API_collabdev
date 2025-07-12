package odk.groupe4.ApiCollabDev.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import odk.groupe4.ApiCollabDev.dto.CommentaireRequestDto;
import odk.groupe4.ApiCollabDev.dto.CommentaireResponseDto;
import odk.groupe4.ApiCollabDev.exception.GlobalExceptionHandler;
import odk.groupe4.ApiCollabDev.models.Commentaire;
import odk.groupe4.ApiCollabDev.service.CommentaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/commentaires")
@Tag(name = "Commentaires", description = "API de gestion des commentaires")
public class CommentaireController {

    private final CommentaireService commentaireService;

    @Autowired
    public CommentaireController(CommentaireService commentaireService) {
        this.commentaireService = commentaireService;
    }

   @Operation(
            summary = "Créer un commentaire",
            description = "Permet à un participant de créer un nouveau commentaire"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Commentaire créé avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Commentaire.class)
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
    @PostMapping("/participant/{id}")
   // Création d'un commentaire pour un participant spécifique
    public ResponseEntity<CommentaireResponseDto> creerCommentaire(
            @Parameter(description = "ID du participant", required = true, example = "1")
            @PathVariable int id,
            @Parameter(description = "Données du commentaire", required = true)
            @Valid @RequestBody CommentaireRequestDto commentaire) {
        CommentaireResponseDto nouveauCommentaire = commentaireService.creerCommentaire(id, commentaire);
        return new ResponseEntity<>(nouveauCommentaire, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Récupérer les commentaires d'un participant",
            description = "Retourne tous les commentaires créés par un participant spécifique"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Commentaires récupérés avec succès",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Commentaire.class)
            )
    )
    @GetMapping("/participant/{id}")
    // Récupération des commentaires d'un participant spécifique
    public ResponseEntity<Set<CommentaireResponseDto>> getCommentairesByParticipant(
            @Parameter(description = "ID du participant", required = true, example = "1")
            @PathVariable int id) {
        Set<CommentaireResponseDto> commentaires = commentaireService.afficherCommentaireParParticipant(id);
        return ResponseEntity.ok(commentaires);
    }

    @Operation(
            summary = "Supprimer un commentaire",
            description = "Supprime définitivement un commentaire du système"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Commentaire supprimé avec succès"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Commentaire non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    @DeleteMapping("/{id}")
    // Suppression d'un commentaire par son ID
    public ResponseEntity<String> supprimerCommentaire(
            @Parameter(description = "ID du commentaire à supprimer", required = true, example = "1")
            @PathVariable int id) {
        String message = commentaireService.supprimerCommentaire(id);
        return ResponseEntity.ok(message);
    }
}
