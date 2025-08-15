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
import odk.groupe4.ApiCollabDev.models.enums.ProjectDomain;
import odk.groupe4.ApiCollabDev.models.enums.ProjectSector;
import odk.groupe4.ApiCollabDev.models.enums.TypeQuiz;
import odk.groupe4.ApiCollabDev.service.QuestionnaireTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/questionnaire-templates")
@Tag(name = "Marketplace Questionnaires", description = "API du marketplace de questionnaires prédéfinis")
public class QuestionnaireTemplateController {

    private final QuestionnaireTemplateService templateService;

    @Autowired
    public QuestionnaireTemplateController(QuestionnaireTemplateService templateService) {
        this.templateService = templateService;
    }

    // ===== MARKETPLACE - CONSULTATION =====

    @Operation(
            summary = "Récupérer tous les templates actifs du marketplace",
            description = "Retourne la liste de tous les questionnaires templates disponibles dans le marketplace"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Liste des templates récupérée avec succès",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = QuestionnaireTemplateResponseDto.class)
            )
    )
    @GetMapping("/marketplace")
    public ResponseEntity<List<QuestionnaireTemplateResponseDto>> getMarketplaceTemplates() {
        List<QuestionnaireTemplateResponseDto> templates = templateService.getAllTemplatesActifs();
        return ResponseEntity.ok(templates);
    }

    @Operation(
            summary = "Récupérer les templates les plus populaires",
            description = "Retourne les 10 questionnaires templates les plus utilisés"
    )
    @GetMapping("/marketplace/populaires")
    public ResponseEntity<List<QuestionnaireTemplateResponseDto>> getTemplatesPopulaires() {
        List<QuestionnaireTemplateResponseDto> templates = templateService.getTemplatesPopulaires();
        return ResponseEntity.ok(templates);
    }

    @Operation(
            summary = "Récupérer les templates les plus récents",
            description = "Retourne les 10 questionnaires templates les plus récemment créés"
    )
    @GetMapping("/marketplace/recents")
    public ResponseEntity<List<QuestionnaireTemplateResponseDto>> getTemplatesRecents() {
        List<QuestionnaireTemplateResponseDto> templates = templateService.getTemplatesRecents();
        return ResponseEntity.ok(templates);
    }

    @Operation(
            summary = "Rechercher des templates avec filtres",
            description = "Permet de rechercher des questionnaires templates par type, domaine, secteur ou mot-clé"
    )
    @GetMapping("/marketplace/recherche")
    public ResponseEntity<List<QuestionnaireTemplateResponseDto>> rechercherTemplates(
            @Parameter(description = "Type de quiz (GESTIONNAIRE, DEVELOPPEUR, DESIGNER)", required = false)
            @RequestParam(required = false) TypeQuiz type,
            @Parameter(description = "Domaine du projet", required = false)
            @RequestParam(required = false) ProjectDomain domaine,
            @Parameter(description = "Secteur du projet", required = false)
            @RequestParam(required = false) ProjectSector secteur,
            @Parameter(description = "Mot-clé pour recherche textuelle", required = false)
            @RequestParam(required = false) String keyword,
            @Parameter(description = "Tri (popularite, recent, titre)", required = false)
            @RequestParam(required = false, defaultValue = "popularite") String sortBy) {

        MarketplaceFilterDto filter = new MarketplaceFilterDto(type, domaine, secteur, keyword, sortBy);
        List<QuestionnaireTemplateResponseDto> templates = templateService.rechercherTemplates(filter);
        return ResponseEntity.ok(templates);
    }

    @Operation(
            summary = "Récupérer un template avec toutes ses questions",
            description = "Retourne les détails complets d'un template incluant toutes ses questions"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Template avec questions récupéré avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = QuestionnaireTemplateResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Template non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/{id}/details")
    public ResponseEntity<QuestionnaireTemplateResponseDto> getTemplateAvecQuestions(
            @Parameter(description = "ID du template", required = true, example = "1")
            @PathVariable int id) {
        QuestionnaireTemplateResponseDto template = templateService.getTemplateAvecQuestions(id);
        return ResponseEntity.ok(template);
    }

    // ===== UTILISATION DES TEMPLATES =====

    @Operation(
            summary = "Utiliser un template pour créer un questionnaire personnalisé",
            description = "Copie un template du marketplace pour créer un questionnaire personnalisé lié à un projet spécifique"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Questionnaire créé à partir du template avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = QuestionnaireDetailResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Template, projet ou utilisateur non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Template non disponible",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/{templateId}/utiliser/projet/{projetId}/createur/{createurId}")
    public ResponseEntity<QuestionnaireDetailResponseDto> utiliserTemplate(
            @Parameter(description = "ID du template à utiliser", required = true, example = "1")
            @PathVariable int templateId,
            @Parameter(description = "ID du projet de destination", required = true, example = "1")
            @PathVariable int projetId,
            @Parameter(description = "ID du créateur du questionnaire", required = true, example = "1")
            @PathVariable int createurId) {
        QuestionnaireDetailResponseDto questionnaire = templateService.utiliserTemplate(templateId, projetId, createurId);
        return new ResponseEntity<>(questionnaire, HttpStatus.CREATED);
    }

    // ===== GESTION ADMIN DES TEMPLATES =====

    @Operation(
            summary = "Créer un nouveau template (Admin)",
            description = "Permet aux administrateurs de créer de nouveaux questionnaires templates pour le marketplace"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Template créé avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = QuestionnaireTemplateResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données invalides",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Créateur non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/admin")
    public ResponseEntity<QuestionnaireTemplateResponseDto> creerTemplate(
            @Parameter(description = "Données du template à créer", required = true)
            @Valid @RequestBody QuestionnaireTemplateDto dto) {
        QuestionnaireTemplateResponseDto template = templateService.creerTemplate(dto);
        return new ResponseEntity<>(template, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Modifier un template (Admin)",
            description = "Permet aux administrateurs de modifier un questionnaire template existant"
    )
    @PutMapping("/admin/{id}")
    public ResponseEntity<QuestionnaireTemplateResponseDto> modifierTemplate(
            @Parameter(description = "ID du template à modifier", required = true, example = "1")
            @PathVariable int id,
            @Parameter(description = "Nouvelles données du template", required = true)
            @Valid @RequestBody QuestionnaireTemplateDto dto) {
        QuestionnaireTemplateResponseDto template = templateService.modifierTemplate(id, dto);
        return ResponseEntity.ok(template);
    }

    @Operation(
            summary = "Ajouter une question à un template (Admin)",
            description = "Permet d'ajouter une nouvelle question à un template existant"
    )
    @PostMapping("/admin/{templateId}/questions")
    public ResponseEntity<Void> ajouterQuestionTemplate(
            @Parameter(description = "ID du template", required = true, example = "1")
            @PathVariable int templateId,
            @Parameter(description = "Données de la question à ajouter", required = true)
            @Valid @RequestBody QuestionTemplateDto dto) {
        templateService.ajouterQuestionTemplate(templateId, dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Désactiver un template (Admin)",
            description = "Désactive un template pour qu'il ne soit plus visible dans le marketplace"
    )
    @PatchMapping("/admin/{id}/desactiver")
    public ResponseEntity<Void> desactiverTemplate(
            @Parameter(description = "ID du template à désactiver", required = true, example = "1")
            @PathVariable int id) {
        templateService.desactiverTemplate(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Supprimer un template (Admin)",
            description = "Supprime définitivement un template du système"
    )
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> supprimerTemplate(
            @Parameter(description = "ID du template à supprimer", required = true, example = "1")
            @PathVariable int id) {
        templateService.supprimerTemplate(id);
        return ResponseEntity.noContent().build();
    }
}
