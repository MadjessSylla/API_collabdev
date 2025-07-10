package odk.groupe4.ApiCollabDev.controllers;


import odk.groupe4.ApiCollabDev.dao.FonctionnaliteDao;
import odk.groupe4.ApiCollabDev.dto.FonctionnaliteDto;
import odk.groupe4.ApiCollabDev.models.Fonctionnalite;
import odk.groupe4.ApiCollabDev.service.FonctionnaliteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    //Methode pour attribuer une tâche à un participant
    @PutMapping("/{idParticipant}/attribuer-tache/projets/{idProjet}/fonctionnalites/{idFonctionnalite}")
    public ResponseEntity<FonctionnaliteDto> attribuerTache(int idParticipant, int idProjet, int idFonctionnalite){
        FonctionnaliteDto fonctionnalite = participantProjetService.attribuerTache(idParticipant, idProjet, idFonctionnalite);
        return ResponseEntity.ok(fonctionnalite);
    }
}
