package odk.groupe4.ApiCollabDev.controllers;

import odk.groupe4.ApiCollabDev.dto.HistAcquisitionDto;
import odk.groupe4.ApiCollabDev.dto.ParticipantDto;
import odk.groupe4.ApiCollabDev.models.Participant;
import odk.groupe4.ApiCollabDev.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    // Methode pour obtenir l'historique d'acquisition d'un participant(GET)
    @GetMapping("/{participantId}/hist-acquisition")
    public ResponseEntity<HistAcquisitionDto> getContributorHistory(@PathVariable int participantId) {
        try {
            HistAcquisitionDto historique = participantProjetService.getHistAcquisition(participantId);
            return ResponseEntity.ok(historique);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Methode permettant de soumettre une demande de participation
    @PostMapping("/send-request/projets/{idProjet}/contributeur/{idContributeur}")
    public ResponseEntity<Participant> envoyerDemande(
            @PathVariable("idProjet") int idProjet,
            @PathVariable("idContributeur") int idContributeur,
            @RequestBody ParticipantDto demandeDTO ){
        Participant p = participantProjetService.envoyerDemande(idProjet,idContributeur, demandeDTO);
        return new ResponseEntity<>(p, HttpStatus.CREATED);
    }

    // Methode pour debloquer l'accès à un projet
    @PutMapping("/{idParticipant}/debloquer")
    public  ResponseEntity<String>debloquerProjet(@PathVariable("idParticipant") int idParticipant){
        try{
            participantProjetService.debloquerAcces(idParticipant);
            return ResponseEntity.ok("Accès au projet debloqué avec succès");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_MODIFIED);
        }
    }



    //Creer un participant
    @PostMapping("creerParticipant")
    public Participant creerParticipant(@RequestBody Participant_projetDtoParticipant){
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