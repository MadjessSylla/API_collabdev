package odk.groupe4.ApiCollabDev.controllers;

import odk.groupe4.ApiCollabDev.dto.ContributeurDto;
import odk.groupe4.ApiCollabDev.models.Contributeur;
import odk.groupe4.ApiCollabDev.service.ContributeurSercice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping ("/api/contributeurs")

public class ContributeurController {
    @Autowired
    private ContributeurSercice contributeurSercice;

    @PostMapping
    public Contributeur creerAbonner(@RequestBody ContributeurDto contributeur) {
       return contributeurSercice.ajouterContributeur(contributeur);

    }
}
