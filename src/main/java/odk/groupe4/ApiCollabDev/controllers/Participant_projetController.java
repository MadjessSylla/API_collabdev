package odk.groupe4.ApiCollabDev.controllers;


import odk.groupe4.ApiCollabDev.dto.FonctionnaliteDto;
import odk.groupe4.ApiCollabDev.dto.Participant_projetDto;
import odk.groupe4.ApiCollabDev.models.Participant;
import odk.groupe4.ApiCollabDev.service.Participant_projetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/participants")
public class Participant_projetController {
    @Autowired
    private Participant_projetService participant_projetService;

    // Methode pour participer à un projet
    @PostMapping("/projets/{idProjet}/contributeur/{idContributeur}")
    public ResponseEntity<String>
    envoyerDemande(@RequestBody Participant_projetDto demandeDTO, @PathVariable ("idProjet") int idProjet, @PathVariable ("idContributeur") int idContributeur ){
        participant_projetService.envoyerDemande(idProjet, demandeDTO, idContributeur);
       return ResponseEntity.ok("Demande envoyée avec succès");
        //Participant p = participant_projetService.envoyerDemande(idProjet, demandeDTO, idContributeur);
        //return new ResponseEntity(p, HttpStatus.CREATED);

    }
    // Methode pour debloquer l'accès à un projet
    @PutMapping("/{idParticipant}/debloquer")
    public  ResponseEntity<String>debloquerAcces(@PathVariable ("idParticipant") int idParticipant){
         participant_projetService.debloquerAcces(idParticipant);
         return ResponseEntity.ok("Accès au projet debloqué avec succès");
    }

    //Methode pour attribuer une tâche à un participant
     @PutMapping("/{idParticipant}/attribuer-tache/projets/{idProjet}/fonctionnalites/{idFonctionnalite}")
    public ResponseEntity<FonctionnaliteDto> attribuerTache(int idParticipant, int idProjet, int idFonctionnalite){
    FonctionnaliteDto fonctionnalite = participant_projetService.attribuerTache(idParticipant, idProjet, idFonctionnalite);
    return ResponseEntity.ok(fonctionnalite);
    }

}
