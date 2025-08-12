package odk.groupe4.ApiCollabDev.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import odk.groupe4.ApiCollabDev.dto.*;
import odk.groupe4.ApiCollabDev.exception.GlobalExceptionHandler;
import odk.groupe4.ApiCollabDev.service.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/questionnaires")
@Tag(name = "Questionnaires", description = "API de gestion des questionnaires et quiz d'évaluation")
public class QuestionnaireController {

    private final QuestionnaireService questionnaireService;

    @Autowired
    public QuestionnaireController(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    @Operation(
            summary = "Créer un questionnaire pour un projet",
            description = "Permet au créateur ou gestionnaire d'un projet de créer un questionnaire d'évaluation"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Questionnaire créé avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = QuestionnaireDetailResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Projet ou créateur non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/projet/{idProjet}/createur/{idCreateur}")
    public ResponseEntity<QuestionnaireDetailResponseDto> creerQuestionnaireProjet(
            @Parameter(description = "ID du projet", required = true, example = "1")
            @PathVariable int idProjet,
            @Parameter(description = "ID du créateur", required = true, example = "1")
            @PathVariable int idCreateur,
            @Parameter(description = "Données du questionnaire", required = true)
            @Valid @RequestBody QuestionnaireDto dto) {
        QuestionnaireDetailResponseDto questionnaire = questionnaireService.creerQuestionnaireProjet(idProjet, idCreateur, dto);
        return new ResponseEntity<>(questionnaire, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Ajouter une question à un questionnaire",
            description = "Ajoute une nouvelle question avec ses options de réponse à un questionnaire existant"
    )
    @PostMapping("/{idQuestionnaire}/questions")
    public ResponseEntity<Void> ajouterQuestion(
            @Parameter(description = "ID du questionnaire", required = true, example = "1")
            @PathVariable int idQuestionnaire,
            @Parameter(description = "Données de la question", required = true)
            @Valid @RequestBody QuestionDto dto) {
        questionnaireService.ajouterQuestion(idQuestionnaire, dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Évaluer un quiz",
            description = "Évalue les réponses d'un participant à un questionnaire et retourne le score"
    )
    @PostMapping("/{idQuestionnaire}/evaluer")
    public ResponseEntity<ResultatQuizDto> evaluerQuiz(
            @Parameter(description = "ID du questionnaire", required = true, example = "1")
            @PathVariable int idQuestionnaire,
            @Parameter(description = "Réponses du participant", required = true)
            @Valid @RequestBody ReponseQuizDto reponses) {
        ResultatQuizDto resultat = questionnaireService.evaluerQuiz(idQuestionnaire, reponses);
        return ResponseEntity.ok(resultat);
    }

    @Operation(
            summary = "Récupérer un questionnaire par ID avec tous les détails",
            description = "Retourne les détails complets d'un questionnaire spécifique incluant toutes les questions, informations du créateur et du projet"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Questionnaire récupéré avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = QuestionnaireDetailResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Questionnaire non trouvé"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<QuestionnaireDetailResponseDto> getQuestionnaireById(
            @Parameter(description = "ID du questionnaire", required = true, example = "1")
            @PathVariable int id) {
        QuestionnaireDetailResponseDto questionnaire = questionnaireService.getQuestionnaireById(id);
        return ResponseEntity.ok(questionnaire);
    }

    @Operation(
            summary = "Récupérer les questionnaires d'un projet avec tous les détails",
            description = "Retourne tous les questionnaires associés à un projet spécifique avec leurs détails complets (questions, créateur, etc.)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des questionnaires récupérée avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = QuestionnaireDetailResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Projet non trouvé"
            )
    })
    @GetMapping("/projet/{idProjet}")
    public ResponseEntity<List<QuestionnaireDetailResponseDto>> getQuestionnairesByProjet(
            @Parameter(description = "ID du projet", required = true, example = "1")
            @PathVariable int idProjet) {
        List<QuestionnaireDetailResponseDto> questionnaires = questionnaireService.getQuestionnairesByProjet(idProjet);
        return ResponseEntity.ok(questionnaires);
    }

    @Operation(
            summary = "Modifier un questionnaire",
            description = "Met à jour les informations d'un questionnaire existant"
    )
    @PutMapping("/{id}")
    public ResponseEntity<QuestionnaireDetailResponseDto> modifierQuestionnaire(
            @Parameter(description = "ID du questionnaire", required = true, example = "1")
            @PathVariable int id,
            @Parameter(description = "Nouvelles données du questionnaire", required = true)
            @Valid @RequestBody QuestionnaireDto dto) {
        QuestionnaireDetailResponseDto questionnaire = questionnaireService.modifierQuestionnaire(id, dto);
        return ResponseEntity.ok(questionnaire);
    }

    @Operation(
            summary = "Supprimer un questionnaire",
            description = "Supprime définitivement un questionnaire et toutes ses questions"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerQuestionnaire(
            @Parameter(description = "ID du questionnaire", required = true, example = "1")
            @PathVariable int id) {
        questionnaireService.supprimerQuestionnaire(id);
        return ResponseEntity.noContent().build();
    }
}
