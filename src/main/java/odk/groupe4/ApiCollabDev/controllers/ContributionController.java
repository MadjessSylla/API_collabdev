package odk.groupe4.ApiCollabDev.controllers;

import odk.groupe4.ApiCollabDev.dao.ContributionDao;
import odk.groupe4.ApiCollabDev.dto.ContributionDto;
import odk.groupe4.ApiCollabDev.models.Contribution;
import odk.groupe4.ApiCollabDev.models.enums.StatusContribution;
import odk.groupe4.ApiCollabDev.service.ContributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/contributions")
public class ContributionController {

    @Autowired
    private ContributionDao contributionDao;
    private ContributionService contributionService;
    // Afficher la liste des contributions
    @GetMapping("/listesDesContributions")
    public List<ContributionDto> afficherContributions() {
        return contributionService.afficherLaListeDesContribution();
    }
    // Soumettre une contribution
    @PostMapping("/SoumettreUneContribution")
    public ContributionDto SoumettreContribution(@RequestHeader(value = "Date", required = false) String dateHeader,
                                                 @RequestBody ContributionDto contributiondto){
        return contributionService.SoumettreUneContribution(dateHeader,contributiondto);
    }
    // Afficher la liste de ses contributions
    @GetMapping("/user/{id}")
    public List<ContributionDto> afficherLaListeDesContributionParId(@PathVariable int id) {
        return contributionService.afficherContributionsParUtilisateur(id);
    }
    // Methode pour valider ou refuser une contribution
    @PutMapping("/validerOuRefuser/{id}")
    public ContributionDto validerOuRefuserContribution(@PathVariable int id, @RequestParam StatusContribution status) {
        return contributionService.validerOuRefuserContribution(id, status);
    }

}
