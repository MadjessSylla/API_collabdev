package odk.groupe4.ApiCollabDev.controllers;

import odk.groupe4.ApiCollabDev.dto.FonctionnaliteDto;
import odk.groupe4.ApiCollabDev.dto.HistAcquisitionDto;
import odk.groupe4.ApiCollabDev.models.Participant;
import odk.groupe4.ApiCollabDev.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/participant_projet")
public class ParticipantController {
    private final ParticipantService participantProjetService;

    @Autowired
    public ParticipantController(ParticipantService participantProjetService) {
        this.participantProjetService = participantProjetService;
    }

    @GetMapping("/{participantId}/hist-acquisition")
    public ResponseEntity<HistAcquisitionDto> getContributorHistory(@PathVariable int participantId) {
        try {
            HistAcquisitionDto historique = participantProjetService.getHistAcquisition(participantId);
            return ResponseEntity.ok(historique);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

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

        //Creer un participant
        @PostMapping("creerParticipant")
        public Participant creerParticipant(@RequestBody Participant_projetDto participant){
            return participantProjetService.ajouterParticipant(participant);
        }

        // Soumettre une contribution
        @PostMapping("{idParticipant}/SoumettreUneContribution")
        public ContributionDto SoumettreContribution(@RequestHeader(value = "Date", required = false) String dateHeader,
        @PathVariable int idParticipant,
        @RequestBody ContributionDto contributiondto) {
            return participantProjetService.SoumettreUneContribution(dateHeader, idParticipant, contributiondto);
        }

        //Méthode pour reserver une fonctionnalité à un participant
        @PatchMapping("{idParticipant}/{idFonctionnalite}/reserverFonctionnalite")
        public Participant_projetDto reserverFonctionnalite(@PathVariable int idParticipant, @PathVariable int idFonctionnalite) {
            return participantProjetService.reserverFonctionnalite(idParticipant, idFonctionnalite);
        }
    }

}