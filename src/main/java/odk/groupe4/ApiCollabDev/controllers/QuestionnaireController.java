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
                            schema = @Schema(implementation = QuestionnaireResponseDto.class)
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
    public ResponseEntity<QuestionnaireResponseDto> creerQuestionnaireProjet(
            @Parameter(description = "ID du projet", required = true, example = "1")
            @PathVariable int idProjet,
            @Parameter(description = "ID du créateur", required = true, example = "1")
            @PathVariable int idCreateur,
            @Parameter(description = "Données du questionnaire", required = true)
            @Valid @RequestBody QuestionnaireDto dto) {
        QuestionnaireResponseDto questionnaire = questionnaireService.creerQuestionnaireProjet(idProjet, idCreateur, dto);
        return new ResponseEntity<>(questionnaire, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Créer un questionnaire pour un template",
            description = "Permet à un administrateur de créer un questionnaire pour un template de projet"
    )
    @PostMapping("/template/{idTemplate}/admin/{idAdmin}")
    public ResponseEntity<QuestionnaireResponseDto> creerQuestionnaireTemplate(
            @Parameter(description = "ID du template", required = true, example = "1")
            @PathVariable int idTemplate,
            @Parameter(description = "ID de l'administrateur", required = true, example = "1")
            @PathVariable int idAdmin,
            @Parameter(description = "Données du questionnaire", required = true)
            @Valid @RequestBody QuestionnaireDto dto) {
        QuestionnaireResponseDto questionnaire = questionnaireService.creerQuestionnaireTemplate(idTemplate, idAdmin, dto);
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
            summary = "Récupérer un questionnaire par ID",
            description = "Retourne les détails d'un questionnaire spécifique"
    )
    @GetMapping("/{id}")
    public ResponseEntity<QuestionnaireResponseDto> getQuestionnaireById(
            @Parameter(description = "ID du questionnaire", required = true, example = "1")
            @PathVariable int id) {
        QuestionnaireResponseDto questionnaire = questionnaireService.getQuestionnaireById(id);
        return ResponseEntity.ok(questionnaire);
    }

    @Operation(
            summary = "Récupérer les questionnaires d'un projet",
            description = "Retourne tous les questionnaires associés à un projet spécifique"
    )
    @GetMapping("/projet/{idProjet}")
    public ResponseEntity<List<QuestionnaireResponseDto>> getQuestionnairesByProjet(
            @Parameter(description = "ID du projet", required = true, example = "1")
            @PathVariable int idProjet) {
        List<QuestionnaireResponseDto> questionnaires = questionnaireService.getQuestionnairesByProjet(idProjet);
        return ResponseEntity.ok(questionnaires);
    }

    @Operation(
            summary = "Récupérer les questionnaires d'un template",
            description = "Retourne tous les questionnaires associés à un template de projet"
    )
    @GetMapping("/template/{idTemplate}")
    public ResponseEntity<List<QuestionnaireResponseDto>> getQuestionnairesByTemplate(
            @Parameter(description = "ID du template", required = true, example = "1")
            @PathVariable int idTemplate) {
        List<QuestionnaireResponseDto> questionnaires = questionnaireService.getQuestionnairesByTemplate(idTemplate);
        return ResponseEntity.ok(questionnaires);
    }
}
