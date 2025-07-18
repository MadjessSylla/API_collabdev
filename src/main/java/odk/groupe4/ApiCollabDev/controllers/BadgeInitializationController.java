package odk.groupe4.ApiCollabDev.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import odk.groupe4.ApiCollabDev.service.BadgeInitializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/badges")
@Tag(name = "Administration des Badges", description = "API d'administration pour la gestion des badges système")
public class BadgeInitializationController {

    private final BadgeInitializationService badgeInitService;

    @Autowired
    public BadgeInitializationController(BadgeInitializationService badgeInitService) {
        this.badgeInitService = badgeInitService;
    }

    @Operation(
            summary = "Réinitialiser les badges par défaut",
            description = "Recrée ou met à jour tous les badges système par défaut"
    )
    @ApiResponse(responseCode = "200", description = "Badges réinitialisés avec succès")
    @PostMapping("/reinitialiser")
    public ResponseEntity<String> reinitialiserBadges() {
        badgeInitService.reinitialiserBadges();
        return ResponseEntity.ok("Badges par défaut réinitialisés avec succès");
    }

    @Operation(
            summary = "Récupérer les seuils de badges",
            description = "Retourne tous les seuils de contributions configurés pour les badges"
    )
    @GetMapping("/seuils")
    public ResponseEntity<List<Integer>> getSeuilsBadges() {
        List<Integer> seuils = badgeInitService.getSeuilsBadges();
        return ResponseEntity.ok(seuils);
    }
}
