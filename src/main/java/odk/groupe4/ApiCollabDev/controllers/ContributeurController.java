package odk.groupe4.ApiCollabDev.controllers;

import odk.groupe4.ApiCollabDev.dto.ContributeurDto;
import odk.groupe4.ApiCollabDev.dto.ProjetDto;
import odk.groupe4.ApiCollabDev.models.Contributeur;
import odk.groupe4.ApiCollabDev.models.enums.StatusProject;
import odk.groupe4.ApiCollabDev.service.ContributeurSercice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping ("/api/contributeurs")

public class ContributeurController {
    @Autowired
    private ContributeurSercice contributeurSercice;

    @PostMapping
    public Contributeur creerAbonner(@RequestBody ContributeurDto contributeur) {
       return contributeurSercice.ajouterContributeur(contributeur);

    }
    //Méthode pour proposer un projet
    @PostMapping("/proposerProjets")
    public ProjetDto proposerProjet(@RequestBody ProjetDto projetDto) {
        return contributeurSercice.proposerProjet(projetDto);
    }
    // Méthode pour recuperer l'avancement d'un projet
    @GetMapping("/projets/{id}/avancement")
    public StatusProject recupererAvancementProjet(@PathVariable int id) {
        return contributeurSercice.suivreAvancementProjet(id);
    }
}
