package odk.groupe4.ApiCollabDev.controllers;


import odk.groupe4.ApiCollabDev.dao.FonctionnaliteDao;
import odk.groupe4.ApiCollabDev.dto.FonctionnaliteDto;
import odk.groupe4.ApiCollabDev.models.Fonctionnalite;
import odk.groupe4.ApiCollabDev.service.FonctionnaliteService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller @RequestMapping("/api/fonctionnalite")
public class FonctionnaliteController {
    private FonctionnaliteService fonctionnaliteService;
    private FonctionnaliteDao fonctionnaliteDao;


    @PostMapping
    public Fonctionnalite creerFonctionnalite(@RequestBody FonctionnaliteDto fonctionnalite){
        return fonctionnaliteService.ajouterFonctionnalite(fonctionnalite);
    }
}
