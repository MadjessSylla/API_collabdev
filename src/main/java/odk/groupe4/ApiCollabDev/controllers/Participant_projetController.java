package odk.groupe4.ApiCollabDev.controllers;


import odk.groupe4.ApiCollabDev.dto.Participant_projetDto;
import odk.groupe4.ApiCollabDev.service.Participant_projetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/participants")
public class Participant_projetController {
    @Autowired
    private Participant_projetService participant_projetService;

    // Methode pour participer à un projet
    @PostMapping("/projets/{idProjet}/")
    public ResponseEntity<String>
    envoyerDemande(@RequestBody Participant_projetDto demandeDTO, @PathVariable int idProjet, @PathVariable int idContributeur ){
        participant_projetService.envoyerDemande(idProjet, demandeDTO, idContributeur);
        return ResponseEntity.ok("Demande envoyée");

    }

}
