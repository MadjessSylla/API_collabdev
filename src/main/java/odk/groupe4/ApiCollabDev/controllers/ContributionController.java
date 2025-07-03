package odk.groupe4.ApiCollabDev.controllers;

import odk.groupe4.ApiCollabDev.dao.ContributionDao;
import odk.groupe4.ApiCollabDev.dto.ContributionDto;
import odk.groupe4.ApiCollabDev.models.Contribution;
import odk.groupe4.ApiCollabDev.service.ContributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("api/contribution")
public class ContributionController {

    @Autowired
    private ContributionDao contributionDao;
    private ContributionService contributionService;

    @GetMapping
    public Contribution creerContribution(@RequestBody ContributionDto contribution){
        return contributionService.ajouterContribution(contribution);
    }
}
