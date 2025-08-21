package odk.groupe4.ApiCollabDev.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import odk.groupe4.ApiCollabDev.dao.ContributeurDao;
import odk.groupe4.ApiCollabDev.dao.ContributionDao;
import odk.groupe4.ApiCollabDev.dao.ProjetDao;
import odk.groupe4.ApiCollabDev.dto.FileUploadResponseDto;
import odk.groupe4.ApiCollabDev.models.Contributeur;
import odk.groupe4.ApiCollabDev.models.Contribution;
import odk.groupe4.ApiCollabDev.models.Projet;
import odk.groupe4.ApiCollabDev.service.utility.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/uploads")
@RequiredArgsConstructor
@Tag(name = "Uploads", description = "Endpoints de gestion des uploads de fichiers")
public class UploadController {

    private final FileStorageService storage;
    private final ContributeurDao contributeurDao;
    private final ProjetDao projetDao;
    private final ContributionDao contributionDao;

    /* ---------------------- UPLOAD UNIQUE ---------------------- */

    @Operation(summary = "Uploader la photo de profil d'un contributeur")
    @ApiResponse(responseCode = "201", description = "Photo uploadée avec succès",
            content = @Content(schema = @Schema(implementation = FileUploadResponseDto.class)))
    @PostMapping("/contributeurs/{id}/photo")
    public ResponseEntity<FileUploadResponseDto> uploadContributeurPhoto(
            @PathVariable int id,
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        Contributeur c = contributeurDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contributeur non trouvé: " + id));

        var stored = storage.store(file, "photos");
        c.setPhotoProfil(stored.url());
        contributeurDao.save(c);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new FileUploadResponseDto(stored.fileName(), stored.url(), stored.size(), stored.contentType()));
    }

    @Operation(summary = "Uploader le cahier des charges d'un projet")
    @ApiResponse(responseCode = "201", description = "Fichier uploadé avec succès")
    @PostMapping("/projets/{id}/cahier")
    public ResponseEntity<FileUploadResponseDto> uploadProjetCahier(
            @PathVariable int id,
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        Projet p = projetDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Projet non trouvé: " + id));

        var stored = storage.store(file, "cahiers");
        p.setUrlCahierDeCharge(stored.url());
        projetDao.save(p);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new FileUploadResponseDto(stored.fileName(), stored.url(), stored.size(), stored.contentType()));
    }

    @Operation(summary = "Uploader un fichier unique pour une contribution")
    @PostMapping("/contributions/{id}/fichier")
    public ResponseEntity<FileUploadResponseDto> uploadContributionFile(
            @PathVariable int id,
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        Contribution c = contributionDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contribution non trouvée: " + id));

        var stored = storage.store(file, "contributions");
        c.setFileUrl(stored.url());
        contributionDao.save(c);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new FileUploadResponseDto(stored.fileName(), stored.url(), stored.size(), stored.contentType()));
    }

    /* ---------------------- UPLOAD MULTIPLE ---------------------- */

    @Operation(summary = "Uploader plusieurs fichiers pour une contribution")
    @PostMapping("/contributions/{id}/fichiers")
    public ResponseEntity<List<FileUploadResponseDto>> uploadContributionFiles(
            @PathVariable int id,
            @RequestParam("files") MultipartFile[] files
    ) throws Exception {
        Contribution c = contributionDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contribution non trouvée: " + id));

        List<FileUploadResponseDto> responses = storeMultiple(files, "contributions");
        contributionDao.save(c);

        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    /* ---------------------- Modification ---------------------- */

    @Operation(summary = "Modifier la photo de profil d'un contributeur")
    @ApiResponse(responseCode = "200", description = "Photo modifiée avec succès",
            content = @Content(schema = @Schema(implementation = FileUploadResponseDto.class)))
    @PutMapping("/contributeurs/{id}/photo")
    public ResponseEntity<FileUploadResponseDto> updateContributeurPhoto(
            @PathVariable int id,
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        Contributeur c = contributeurDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contributeur non trouvé: " + id));

        // Supprimer l'ancienne photo si elle existe
        if (c.getPhotoProfil() != null && !c.getPhotoProfil().isEmpty()) {
            storage.delete(c.getPhotoProfil());
        }

        // Uploader la nouvelle photo
        var stored = storage.store(file, "photos");
        c.setPhotoProfil(stored.url());
        contributeurDao.save(c);

        return ResponseEntity.ok()
                .body(new FileUploadResponseDto(stored.fileName(), stored.url(), stored.size(), stored.contentType()));
    }

    @Operation(summary = "Modifier le cahier des charges d'un projet")
    @ApiResponse(responseCode = "200", description = "Cahier des charges modifié avec succès")
    @PutMapping("/projets/{id}/cahier")
    public ResponseEntity<FileUploadResponseDto> updateProjetCahier(
            @PathVariable int id,
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        Projet p = projetDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Projet non trouvé: " + id));

        // Supprimer l'ancien fichier s'il existe
        if (p.getUrlCahierDeCharge() != null && !p.getUrlCahierDeCharge().isEmpty()) {
            storage.delete(p.getUrlCahierDeCharge());
        }

        var stored = storage.store(file, "cahiers");
        p.setUrlCahierDeCharge(stored.url());
        projetDao.save(p);

        return ResponseEntity.ok()
                .body(new FileUploadResponseDto(stored.fileName(), stored.url(), stored.size(), stored.contentType()));
    }

    @Operation(summary = "Modifier le fichier d'une contribution")
    @PutMapping("/contributions/{id}/fichier")
    public ResponseEntity<FileUploadResponseDto> updateContributionFile(
            @PathVariable int id,
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        Contribution c = contributionDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contribution non trouvée: " + id));

        // Supprimer l'ancien fichier s'il existe
        if (c.getFileUrl() != null && !c.getFileUrl().isEmpty()) {
            storage.delete(c.getFileUrl());
        }

        var stored = storage.store(file, "contributions");
        c.setFileUrl(stored.url());
        contributionDao.save(c);

        return ResponseEntity.ok()
                .body(new FileUploadResponseDto(stored.fileName(), stored.url(), stored.size(), stored.contentType()));
    }

    /* ---------------------- SUPPRESSION ---------------------- */

    @Operation(summary = "Supprimer un fichier par son chemin")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam String path) {
        try {
            storage.delete(path);
            return ResponseEntity.ok("Fichier supprimé : " + path);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @Operation(summary = "Supprimer plusieurs fichiers")
    @DeleteMapping("/delete-multiple")
    public ResponseEntity<List<String>> deleteMultipleFiles(@RequestParam List<String> paths) {
        List<String> results = new ArrayList<>();
        for (String path : paths) {
            try {
                storage.delete(path);
                results.add("Supprimé : " + path);
            } catch (IOException e) {
                results.add("Erreur : " + path + " -> " + e.getMessage());
            }
        }
        return ResponseEntity.ok(results);
    }

    /* ---------------------- MÉTHODE UTILITAIRE ---------------------- */
    private List<FileUploadResponseDto> storeMultiple(MultipartFile[] files, String folder) throws Exception {
        List<FileUploadResponseDto> responses = new ArrayList<>();
        for (MultipartFile file : files) {
            var stored = storage.store(file, folder);
            responses.add(new FileUploadResponseDto(stored.fileName(), stored.url(), stored.size(), stored.contentType()));
        }
        return responses;
    }
}
