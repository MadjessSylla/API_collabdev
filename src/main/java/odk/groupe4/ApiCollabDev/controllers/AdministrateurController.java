package odk.groupe4.ApiCollabDev.controllers;

import odk.groupe4.ApiCollabDev.dto.ContributionDto;
import odk.groupe4.ApiCollabDev.models.enums.StatusContribution;
import odk.groupe4.ApiCollabDev.service.AdministrateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/administrateur")
public class AdministrateurController {
    @Autowired
    private AdministrateurService administrateurService;
    // Methode pour valider ou refuser une contribution
    @PutMapping("/validerOuRefuser/{idContribution}")
    public ContributionDto validerOuRefuserContribution(@PathVariable int idContribution, @RequestParam StatusContribution status) {
        return administrateurService.validerOuRefuserContribution(idContribution, status);
    }
}
