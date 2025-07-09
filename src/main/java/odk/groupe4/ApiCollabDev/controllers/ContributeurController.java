package odk.groupe4.ApiCollabDev.controllers;

import odk.groupe4.ApiCollabDev.dto.ContributeurDto;
import odk.groupe4.ApiCollabDev.dto.ProjetDto;
import odk.groupe4.ApiCollabDev.models.Contributeur;
import odk.groupe4.ApiCollabDev.models.enums.StatusProject;
import odk.groupe4.ApiCollabDev.service.ContributeurSercice;
import odk.groupe4.ApiCollabDev.service.ProjetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping ("/api/contributeurs")

public class ContributeurController {
    @Autowired
    private ProjetService projetService;
    @Autowired
    private ContributeurSercice contributeurSercice;

    @PostMapping("creerAbonner")
    public Contributeur creerAbonner(@RequestBody ContributeurDto contributeur) {
       return contributeurSercice.ajouterContributeur(contributeur);

    }
    //Méthode pour proposer un projet
    @PostMapping("/proposerProjets")
    public ProjetDto proposerProjet(@RequestBody ProjetDto projetDto) {
        return projetService.proposerProjet(projetDto);
    }
    // Méthode pour recuperer l'avancement d'un projet
    @GetMapping("/projets/{id}/avancement")
    public StatusProject recupererAvancementProjet(@PathVariable int id) {
        return projetService.suivreAvancementProjet(id);
    }
    //Méthode pour choisir un gestionnaire de projet
    @PatchMapping("{idProjet},{idGestionnaire}/selectionnerGestionnaire")
    public void selectionnerGestionnaire(int idProjet, int idGesionnaire){
        projetService.selectGestionnaire(idProjet,idGesionnaire);
    }
}
