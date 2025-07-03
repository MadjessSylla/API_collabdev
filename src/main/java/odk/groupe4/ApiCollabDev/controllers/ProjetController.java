package odk.groupe4.ApiCollabDev.controllers;

import odk.groupe4.ApiCollabDev.dto.ProjetDto;
import odk.groupe4.ApiCollabDev.models.Projet;
import odk.groupe4.ApiCollabDev.service.BadgeService;
import odk.groupe4.ApiCollabDev.service.ProjetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/projet")
public class ProjetController {
    @Autowired
    private ProjetService projetService;
    private BadgeService badgeService;

    @PostMapping
    public Projet creerProjet(@RequestBody ProjetDto projet){

        return projetService.ajouterProjet(projet);
    }



}
