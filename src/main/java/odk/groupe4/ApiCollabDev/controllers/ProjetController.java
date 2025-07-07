package odk.groupe4.ApiCollabDev.controllers;

import odk.groupe4.ApiCollabDev.dto.ProjetDto;
import odk.groupe4.ApiCollabDev.models.Projet;
import odk.groupe4.ApiCollabDev.service.ProjetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/projets")
public class ProjetController {
    @Autowired
    private ProjetService projetService;

    @PatchMapping("/{id}/admin/{idadmin}")
// cette methode permet de modifier le statut du projet
    public Projet validerProjet(@PathVariable("id")int idprojet, @PathVariable("idadmin") int idadmin){
        return projetService.validerProjet(idprojet, idadmin);

    }

    @PostMapping
// Methode pour proposer un projet
   public  Projet proposerProjet(@RequestBody ProjetDto projet, @RequestParam int idPorteurProjet){
        return projetService.proposerProjet(projet, idPorteurProjet);
    }

    @DeleteMapping("/{id}/admin/{idadmin}")
//Methode pour supprimer un projet qui va retourner une reponse http
   public ResponseEntity<String> rejeterProjet(@PathVariable("id")int idprojet, @PathVariable("admin") int idadmin) {
        projetService.rejeterProjet(idprojet, idadmin);
        return ResponseEntity.ok("Projet supprimé avec succès");
    }


}
