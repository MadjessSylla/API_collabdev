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
import odk.groupe4.ApiCollabDev.service.CommentaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/commentaires")
@Tag(name = "Commentaires", description = "API de gestion des commentaires et réponses (threads)")
public class CommentaireController {

    private final CommentaireService commentaireService;

    @Autowired
    public CommentaireController(CommentaireService commentaireService) {
        this.commentaireService = commentaireService;
    }

    @Operation(
            summary = "Créer un commentaire ou une réponse dans un projet",
            description = "Permet à un participant de créer un commentaire racine ou une réponse à un commentaire existant dans un projet donné."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Commentaire créé avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentaireResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Participant ou projet non trouvé",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/participant/{participantId}/projet/{projetId}")
    public ResponseEntity<CommentaireResponseDto> creerCommentaire(
            @Parameter(description = "ID du participant auteur du commentaire", required = true, example = "1")
            @PathVariable int participantId,
            @Parameter(description = "ID du projet associé au commentaire", required = true, example = "5")
            @PathVariable int projetId,
            @Parameter(description = "Données du commentaire à créer", required = true)
            @Valid @RequestBody CommentaireRequestDto body
    ) {
        CommentaireResponseDto created = commentaireService.creerCommentaire(participantId, projetId, body);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Lister les commentaires racines d'un participant",
            description = "Retourne tous les commentaires racines écrits par un participant, avec leurs réponses imbriquées."
    )
    @ApiResponse(responseCode = "200", description = "Commentaires récupérés avec succès",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommentaireResponseDto.class)))
    @GetMapping("/participant/{id}")
    public ResponseEntity<List<CommentaireResponseDto>> getCommentairesByParticipant(
            @Parameter(description = "ID du participant", required = true, example = "1")
            @PathVariable int id
    ) {
        return ResponseEntity.ok(commentaireService.afficherCommentairesRacinesParParticipant(id));
    }

    @Operation(
            summary = "Lister les commentaires racines d'un projet",
            description = "Retourne tous les commentaires racines associés à un projet, avec leurs réponses imbriquées."
    )
    @ApiResponse(responseCode = "200", description = "Commentaires récupérés avec succès",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommentaireResponseDto.class)))
    @GetMapping("/projet/{id}")
    public ResponseEntity<List<CommentaireResponseDto>> getCommentairesByProjet(
            @Parameter(description = "ID du projet", required = true, example = "5")
            @PathVariable int id
    ) {
        return ResponseEntity.ok(commentaireService.afficherCommentairesRacinesParProjet(id));
    }

    @Operation(
            summary = "Supprimer un commentaire (et ses réponses)",
            description = "Supprime un commentaire et toutes ses réponses associées."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Commentaire supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Commentaire non trouvé",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> supprimerCommentaire(
            @Parameter(description = "ID du commentaire à supprimer", required = true, example = "10")
            @PathVariable int id
    ) {
        return ResponseEntity.ok(commentaireService.supprimerCommentaire(id));
    }
}
