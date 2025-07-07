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

}
