package odk.groupe4.ApiCollabDev.controllers;

import odk.groupe4.ApiCollabDev.dto.ProjetCahierDto;
import odk.groupe4.ApiCollabDev.dto.ProjetDto;
import odk.groupe4.ApiCollabDev.models.Projet;
import odk.groupe4.ApiCollabDev.models.enums.NiveauProjet;
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

//Methode pour editer un cahier de charge
   @PatchMapping("/{idProjet}/editer-cahier")
   public ResponseEntity<String> editerCahierDeCharge(@PathVariable("idProjet") int idprojet, ProjetCahierDto projetCahierDto){
        projetService.editerCahierDeCharge(projetCahierDto,idprojet);
        return ResponseEntity.ok("cahier de charge edité avec succès");

   }

   // Attribuer un niveau au projet
    @PatchMapping("/{idProjet}/admin/{idadmin}")
    public ResponseEntity<Projet> attribuerNiveau(@PathVariable("idProjet") int idProjet, @PathVariable("idAdmin") int idadministrateur,@RequestParam NiveauProjet niveau){
        Projet p = projetService.attribuerNiveau(idProjet, idadministrateur, niveau);
        return  ResponseEntity.ok(p);
    }

}
