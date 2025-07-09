package odk.groupe4.ApiCollabDev.controllers;

import odk.groupe4.ApiCollabDev.dto.ContributeurDto;
import odk.groupe4.ApiCollabDev.dto.ContributeurSoldeDto;
import odk.groupe4.ApiCollabDev.models.Contributeur;
import odk.groupe4.ApiCollabDev.models.enums.ProjectStatus;
import odk.groupe4.ApiCollabDev.service.ContributeurSercice;
import odk.groupe4.ApiCollabDev.service.ProjetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping ("/api/contributeurs")

public class ContributeurController {
    @Autowired
    private ProjetService projetService;
    private ContributeurSercice contributeurSercice;

    @PostMapping
    public Contributeur creerAbonner(@RequestBody ContributeurDto contributeur) {
       return contributeurSercice.ajouterContributeur(contributeur);

    }
    // Afficher le solde d'un contributeur - GET
    @GetMapping("/{id}/solde")
    public ContributeurSoldeDto afficherSoldeContributeur(@PathVariable int id) {
        return contributeurSercice.afficherSoldeContributeur(id);
    }

}
