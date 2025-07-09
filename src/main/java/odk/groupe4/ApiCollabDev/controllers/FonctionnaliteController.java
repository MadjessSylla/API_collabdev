package odk.groupe4.ApiCollabDev.controllers;


import odk.groupe4.ApiCollabDev.dao.FonctionnaliteDao;
import odk.groupe4.ApiCollabDev.dto.FonctionnaliteDto;
import odk.groupe4.ApiCollabDev.models.Fonctionnalite;
import odk.groupe4.ApiCollabDev.service.FonctionnaliteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fonctionnalite")
public class FonctionnaliteController {
    @Autowired
    private FonctionnaliteService fonctionnaliteService;
    @Autowired
    private FonctionnaliteDao fonctionnaliteDao;


    @PostMapping("ajouterFonctionnalite")
    public Fonctionnalite creerFonctionnalite(@RequestBody FonctionnaliteDto fonctionnalite){
        return fonctionnaliteService.ajouterFonctionnalite(fonctionnalite);
    }
}
