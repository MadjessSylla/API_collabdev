package odk.groupe4.ApiCollabDev.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import odk.groupe4.ApiCollabDev.dto.TemplateProjetDto;
import odk.groupe4.ApiCollabDev.dto.TemplateProjetResponseDto;
import odk.groupe4.ApiCollabDev.exception.GlobalExceptionHandler;
import odk.groupe4.ApiCollabDev.models.enums.ProjectDomain;
import odk.groupe4.ApiCollabDev.models.enums.ProjectSector;
import odk.groupe4.ApiCollabDev.service.TemplateProjetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/templates-projets")
@Tag(name = "Templates de Projets", description = "API de gestion des modèles/templates de projets")
public class TemplateProjetController {

    private final TemplateProjetService templateService;

    @Autowired
    public TemplateProjetController(TemplateProjetService templateService) {
        this.templateService = templateService;
    }

    @Operation(
            summary = "Créer un nouveau template de projet",
            description = "Permet à un administrateur de créer un nouveau modèle de projet réutilisable"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Template créé avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TemplateProjetResponseDto.class)
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
    @PostMapping("/admin/{idAdmin}")
    public ResponseEntity<TemplateProjetResponseDto> creerTemplate(
            @Parameter(description = "ID de l'administrateur créateur", required = true, example = "1")
            @PathVariable int idAdmin,
            @Parameter(description = "Données du template à créer", required = true)
            @Valid @RequestBody TemplateProjetDto dto) {
        TemplateProjetResponseDto template = templateService.creerTemplate(dto, idAdmin);
        return new ResponseEntity<>(template, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Récupérer tous les templates actifs",
            description = "Retourne la liste de tous les templates de projets actifs"
    )
    @GetMapping
    public ResponseEntity<List<TemplateProjetResponseDto>> getAllTemplatesActifs() {
        List<TemplateProjetResponseDto> templates = templateService.getAllTemplatesActifs();
        return ResponseEntity.ok(templates);
    }

    @Operation(
            summary = "Récupérer un template par ID",
            description = "Retourne les détails d'un template spécifique"
    )
    @GetMapping("/{id}")
    public ResponseEntity<TemplateProjetResponseDto> getTemplateById(
            @Parameter(description = "ID du template", required = true, example = "1")
            @PathVariable int id) {
        TemplateProjetResponseDto template = templateService.getTemplateById(id);
        return ResponseEntity.ok(template);
    }

    @Operation(
            summary = "Filtrer les templates par domaine",
            description = "Retourne tous les templates actifs d'un domaine spécifique"
    )
    @GetMapping("/domaine/{domaine}")
    public ResponseEntity<List<TemplateProjetResponseDto>> getTemplatesByDomaine(
            @Parameter(description = "Domaine du template", required = true)
            @PathVariable ProjectDomain domaine) {
        List<TemplateProjetResponseDto> templates = templateService.getTemplatesByDomaine(domaine);
        return ResponseEntity.ok(templates);
    }

    @Operation(
            summary = "Filtrer les templates par secteur",
            description = "Retourne tous les templates actifs d'un secteur spécifique"
    )
    @GetMapping("/secteur/{secteur}")
    public ResponseEntity<List<TemplateProjetResponseDto>> getTemplatesBySecteur(
            @Parameter(description = "Secteur du template", required = true)
            @PathVariable ProjectSector secteur) {
        List<TemplateProjetResponseDto> templates = templateService.getTemplatesBySecteur(secteur);
        return ResponseEntity.ok(templates);
    }

    @Operation(
            summary = "Mettre à jour un template",
            description = "Met à jour les informations d'un template existant"
    )
    @PutMapping("/{id}")
    public ResponseEntity<TemplateProjetResponseDto> updateTemplate(
            @Parameter(description = "ID du template", required = true, example = "1")
            @PathVariable int id,
            @Parameter(description = "Nouvelles données du template", required = true)
            @Valid @RequestBody TemplateProjetDto dto) {
        TemplateProjetResponseDto template = templateService.updateTemplate(id, dto);
        return ResponseEntity.ok(template);
    }

    @Operation(
            summary = "Désactiver un template",
            description = "Désactive un template (le rend indisponible)"
    )
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> desactiverTemplate(
            @Parameter(description = "ID du template", required = true, example = "1")
            @PathVariable int id) {
        templateService.desactiverTemplate(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Activer un template",
            description = "Active un template (le rend disponible)"
    )
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activerTemplate(
            @Parameter(description = "ID du template", required = true, example = "1")
            @PathVariable int id) {
        templateService.activerTemplate(id);
        return ResponseEntity.noContent().build();
    }
}
