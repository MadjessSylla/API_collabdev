package odk.groupe4.ApiCollabDev.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import odk.groupe4.ApiCollabDev.dto.ProjetCahierDto;
import odk.groupe4.ApiCollabDev.dto.ProjetDto;
import odk.groupe4.ApiCollabDev.dto.ProjetResponseDto;
import odk.groupe4.ApiCollabDev.exception.GlobalExceptionHandler;
import odk.groupe4.ApiCollabDev.models.Projet;
import odk.groupe4.ApiCollabDev.models.enums.ProjectDomain;
import odk.groupe4.ApiCollabDev.models.enums.ProjectLevel;
import odk.groupe4.ApiCollabDev.models.enums.ProjectSector;
import odk.groupe4.ApiCollabDev.models.enums.ProjectStatus;
import odk.groupe4.ApiCollabDev.service.ProjetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/v1/projets")
@Tag(name = "Projets", description = "API de gestion des projets collaboratifs")
public class ProjetController {

    private final ProjetService projetService;

    @Autowired
    public ProjetController(ProjetService projetService) {
        this.projetService = projetService;
    }

    @Operation(
            summary = "Récupérer tous les projets",
            description = "Retourne la liste complète de tous les projets avec possibilité de filtrage par statut"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des projets récupérée avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjetResponseDto.class)
                    )
            )
    })
    @GetMapping
    // Récupérer tous les projets avec option de filtrage par statut
    public ResponseEntity<List<ProjetResponseDto>> getAllProjets(
            @Parameter(description = "Filtrer par statut du projet", required = false)
            @RequestParam(required = false) ProjectStatus status) {
        List<ProjetResponseDto> projets = projetService.getAllProjets(status);
        return ResponseEntity.ok(projets);
    }

    @Operation(
            summary = "Récupérer tous les projets ouverts",
            description = "Retourne la liste de tous les projets avec le statut OUVERT, avec possibilité de filtrage par domaine et secteur"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des projets ouverts récupérée avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjetResponseDto.class)
                    )
            )
    })
    @GetMapping("/ouverts")
    public ResponseEntity<List<ProjetResponseDto>> getProjetsOuverts(
            @Parameter(description = "Filtrer par domaine du projet", required = false)
            @RequestParam(required = false) ProjectDomain domaine,
            @Parameter(description = "Filtrer par secteur du projet", required = false)
            @RequestParam(required = false) ProjectSector secteur) {
        List<ProjetResponseDto> projets = projetService.getProjetsOuverts(domaine, secteur);
        return ResponseEntity.ok(projets);
    }

    @Operation(
            summary = "Filtrer les projets par domaine",
            description = "Retourne tous les projets d'un domaine spécifique"
    )
    @GetMapping("/domaine/{domaine}")
    public ResponseEntity<List<ProjetResponseDto>> getProjetsByDomaine(
            @Parameter(description = "Domaine du projet", required = true)
            @PathVariable ProjectDomain domaine) {
        List<ProjetResponseDto> projets = projetService.getProjetsByDomaine(domaine);
        return ResponseEntity.ok(projets);
    }

    @Operation(
            summary = "Filtrer les projets par secteur",
            description = "Retourne tous les projets d'un secteur spécifique"
    )
    @GetMapping("/secteur/{secteur}")
    public ResponseEntity<List<ProjetResponseDto>> getProjetsBySecteur(
            @Parameter(description = "Secteur du projet", required = true)
            @PathVariable ProjectSector secteur) {
        List<ProjetResponseDto> projets = projetService.getProjetsBySecteur(secteur);
        return ResponseEntity.ok(projets);
    }

    @Operation(
            summary = "Récupérer un projet par ID",
            description = "Retourne les détails complets d'un projet spécifique"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Projet trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjetResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Projet non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    // Récupérer un projet par son ID
    @GetMapping("/{id}")
    public ResponseEntity<ProjetResponseDto> getProjetById(
            @Parameter(description = "ID unique du projet", required = true, example = "1")
            @PathVariable int id) {
        ProjetResponseDto projet = projetService.getProjetById(id);
        return ResponseEntity.ok(projet);
    }

    @Operation(
            summary = "Afficher les projets par contributeur",
            description = "Retourne la liste des projets créés par un contributeur spécifique"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des projets récupérée avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjetResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Aucun projet trouvé pour ce contributeur",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    // Afficher la liste des projets créés par un contributeur
    @GetMapping("/contributeur/{idContributeur}")
    public ResponseEntity<List<ProjetResponseDto>> getProjetsByContributeur(
            @Parameter(description = "ID du contributeur", required = true, example = "1")
            @PathVariable int idContributeur) {
        List<ProjetResponseDto> projets = projetService.getProjetsByContributeur(idContributeur);
        return ResponseEntity.ok(projets);
    }

    @Operation(
            summary = "Lister les projets débloqués par un contributeur",
            description = "Retourne la liste de tous les projets qu'un contributeur a débloqués"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des projets débloqués récupérée avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Projet.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Contributeur non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/{id}/projets-debloques")
    public ResponseEntity<List<ProjetResponseDto>> getProjetsDebloquesByContributeur(
            @Parameter(description = "ID du contributeur", required = true, example = "1")
            @PathVariable int id) {
        List<ProjetResponseDto> projets = projetService.getProjetsDebloquesByContributeur(id);
        return ResponseEntity.ok(projets);
    }

    @Operation(
            summary = "Proposer un nouveau projet",
            description = "Permet à un contributeur de proposer un nouveau projet collaboratif avec un cahier des charges optionnel"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Projet proposé avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjetResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données du projet invalides ou erreur de téléversement",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Contributeur porteur de projet non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    @PostMapping(value = "/contributeur/{idPorteurProjet}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProjetResponseDto> proposerProjet(
            @Parameter(description = "ID du contributeur porteur du projet", required = true, example = "1")
            @PathVariable int idPorteurProjet,
            @Parameter(description = "Données du projet à proposer", required = true)
            @RequestPart("projet") @Valid ProjetDto projetDto,
            @Parameter(description = "Fichier du cahier des charges (optionnel)", required = false)
            @RequestPart(value = "cahierDesCharges", required = false) MultipartFile cahierDesCharges) {

        try {
            // Gérer l'upload du fichier si présent
            if (cahierDesCharges != null && !cahierDesCharges.isEmpty()) {
                String fileUrl = uploadFile(cahierDesCharges);
                projetDto.setUrlCahierDeCharge(fileUrl);
            }

            ProjetResponseDto projet = projetService.proposerProjet(projetDto, idPorteurProjet);
            return new ResponseEntity<>(projet, HttpStatus.CREATED);

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du téléversement du fichier: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Mettre à jour un projet",
            description = "Permet au créateur d'un projet de modifier ses détails. Si le projet était validé, il repasse en attente de validation."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Projet mis à jour avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjetResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données invalides ou projet non modifiable",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Seul le créateur peut modifier le projet",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
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
    @PutMapping("/{idProjet}/createur/{idCreateur}")
    public ResponseEntity<ProjetResponseDto> mettreAJourProjet(
            @Parameter(description = "ID du projet à modifier", required = true, example = "1")
            @PathVariable int idProjet,
            @Parameter(description = "ID du créateur du projet", required = true, example = "1")
            @PathVariable int idCreateur,
            @Parameter(description = "Nouvelles données du projet", required = true)
            @Valid @RequestBody ProjetDto projetDto) {
        ProjetResponseDto projet = projetService.mettreAJourProjet(idProjet, idCreateur, projetDto);
        return ResponseEntity.ok(projet);
    }

    @Operation(
            summary = "Annuler un projet",
            description = "Permet au créateur d'un projet de l'annuler (supprimer). Impossible si le projet est en cours ou terminé."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Projet annulé avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Projet non annulable dans son état actuel",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Seul le créateur peut annuler le projet",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
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
    @DeleteMapping("/{idProjet}/createur/{idCreateur}")
    public ResponseEntity<Void> annulerProjet(
            @Parameter(description = "ID du projet à annuler", required = true, example = "1")
            @PathVariable int idProjet,
            @Parameter(description = "ID du créateur du projet", required = true, example = "1")
            @PathVariable int idCreateur) {
        projetService.annulerProjet(idProjet, idCreateur);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Valider un projet",
            description = "Permet à un administrateur de valider un projet proposé (change le statut à OUVERT)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Projet validé avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjetResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Projet ou administrateur non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Le projet ne peut pas être validé dans son état actuel",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    @PatchMapping("/{id}/validate/admin/{idAdmin}")
    public ResponseEntity<ProjetResponseDto> validerProjet(
            @Parameter(description = "ID du projet à valider", required = true, example = "1")
            @PathVariable int id,
            @Parameter(description = "ID de l'administrateur validateur", required = true, example = "1")
            @PathVariable int idAdmin) {
        ProjetResponseDto projet = projetService.validerProjet(id, idAdmin);
        return ResponseEntity.ok(projet);
    }

    @Operation(
            summary = "Rejeter un projet",
            description = "Permet à un administrateur de rejeter un projet proposé (supprime le projet)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Projet rejeté et supprimé avec succès"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Projet ou administrateur non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    @DeleteMapping("/{id}/reject/admin/{idAdmin}")
    public ResponseEntity<Void> rejeterProjet(
            @Parameter(description = "ID du projet à rejeter", required = true, example = "1")
            @PathVariable int id,
            @Parameter(description = "ID de l'administrateur", required = true, example = "1")
            @PathVariable int idAdmin) {
        projetService.rejeterProjet(id, idAdmin);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Éditer le cahier des charges",
            description = "Met à jour le cahier des charges d'un projet en téléversant un nouveau fichier ou en fournissant une URL"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cahier des charges mis à jour avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjetResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Projet non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erreur de téléversement ou données invalides",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    @PatchMapping(value = "/{id}/cahier-charges", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProjetResponseDto> editerCahierDeCharge(
            @Parameter(description = "ID du projet", required = true, example = "1")
            @PathVariable int id,
            @Parameter(description = "URL du cahier des charges (optionnel si fichier fourni)", required = false)
            @RequestPart(value = "cahierDto", required = false) ProjetCahierDto projetCahierDto,
            @Parameter(description = "Fichier du cahier des charges (optionnel si URL fournie)", required = false)
            @RequestPart(value = "cahierDesCharges", required = false) MultipartFile cahierDesCharges) {

        try {
            String urlCahier = null;

            // Priorité au fichier téléversé
            if (cahierDesCharges != null && !cahierDesCharges.isEmpty()) {
                urlCahier = uploadFile(cahierDesCharges);
            } else if (projetCahierDto != null && projetCahierDto.getUrlCahierDeCharge() != null) {
                urlCahier = projetCahierDto.getUrlCahierDeCharge();
            } else {
                throw new RuntimeException("Aucun fichier ou URL fourni pour le cahier des charges");
            }

            // Créer le DTO avec l'URL finale
            ProjetCahierDto cahierDto = new ProjetCahierDto();
            cahierDto.setUrlCahierDeCharge(urlCahier);

            ProjetResponseDto projet = projetService.editerCahierDeCharge(cahierDto, id);
            return ResponseEntity.ok(projet);

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du téléversement du fichier: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Attribuer un niveau de complexité",
            description = "Permet à un administrateur d'attribuer un niveau de complexité à un projet"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Niveau attribué avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjetResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Projet ou administrateur non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Niveau invalide ou projet déjà nivelé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    @PatchMapping("/{id}/niveau/admin/{idAdmin}")
    public ResponseEntity<ProjetResponseDto> attribuerNiveau(
            @Parameter(description = "ID du projet", required = true, example = "1")
            @PathVariable int id,
            @Parameter(description = "ID de l'administrateur", required = true, example = "1")
            @PathVariable int idAdmin,
            @Parameter(description = "Niveau de complexité à attribuer", required = true)
            @RequestParam ProjectLevel niveau) {
        ProjetResponseDto projet = projetService.attribuerNiveau(id, idAdmin, niveau);
        return ResponseEntity.ok(projet);
    }

    @Operation(
            summary = "Démarrer un projet",
            description = "Change le statut d'un projet validé vers EN_COURS"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Projet démarré avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjetResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Projet non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Le projet ne peut pas être démarré dans son état actuel",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    @PatchMapping("/{id}/start")
    public ResponseEntity<ProjetResponseDto> demarrerProjet(
            @Parameter(description = "ID du projet à démarrer", required = true, example = "1")
            @PathVariable int id) {
        ProjetResponseDto projet = projetService.demarrerProjet(id);
        return ResponseEntity.ok(projet);
    }

    @Operation(
            summary = "Terminer un projet",
            description = "Marque un projet comme terminé"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Projet terminé avec succès",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProjetResponseDto.class)
            )
    )
    @PatchMapping("/{id}/complete")
    public ResponseEntity<ProjetResponseDto> terminerProjet(
            @Parameter(description = "ID du projet à terminer", required = true, example = "1")
            @PathVariable int id) {
        ProjetResponseDto projet = projetService.terminerProjet(id);
        return ResponseEntity.ok(projet);
    }

    /**
     * Méthode privée pour gérer l'upload des fichiers
     */
    private String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("Le fichier est vide");
        }

        // Validation du type de fichier (optionnel)
        String contentType = file.getContentType();
        if (contentType != null && !isValidFileType(contentType)) {
            throw new RuntimeException("Type de fichier non autorisé: " + contentType);
        }

        // Créer le dossier d'upload s'il n'existe pas
        Path uploadDir = Paths.get("uploads/cahiers-charges");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        // Générer un nom de fichier unique
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String uniqueFilename = System.currentTimeMillis() + "_" +
                (originalFilename != null ? originalFilename.replaceAll("[^a-zA-Z0-9.-]", "_") : "file" + fileExtension);

        Path filePath = uploadDir.resolve(uniqueFilename);

        // Sauvegarder le fichier
        Files.write(filePath, file.getBytes());

        // Retourner l'URL relative du fichier
        return "/uploads/cahiers-charges/" + uniqueFilename;
    }

    /**
     * Valide le type de fichier autorisé
     */
    private boolean isValidFileType(String contentType) {
        return contentType.equals("application/pdf") ||
                contentType.equals("application/msword") ||
                contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document") ||
                contentType.equals("text/plain") ||
                contentType.startsWith("image/");
    }
}
