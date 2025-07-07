package odk.groupe4.ApiCollabDev.controllers;

import odk.groupe4.ApiCollabDev.models.Contribution;
import odk.groupe4.ApiCollabDev.models.enums.StatusContribution;
import odk.groupe4.ApiCollabDev.service.ContributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contributions")
public class ContributionController {
    private final ContributionService contributionService;

    @Autowired
    public ContributionController(ContributionService contributionService) {
        this.contributionService = contributionService;
    }
    @PutMapping("/{id}/validate")
    public ResponseEntity<Contribution> updateContributionStatus(
            @PathVariable("id") int contributionId, // Identifiant de la contribution
            @RequestParam StatusContribution status,  // Nouveau statut de la contribution
            @RequestParam int gestionnaireId) // Identifiant du gestionnaire qui met à jour le statut
    {
        // Vérification que le statut est valide
        try {
            Contribution updatedContribution = contributionService.MiseAJourStatutContribution(contributionId, status, gestionnaireId);
            return ResponseEntity.ok(updatedContribution);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}