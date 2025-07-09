package odk.groupe4.ApiCollabDev.controllers;

import odk.groupe4.ApiCollabDev.dao.Participant_projetDao;
import odk.groupe4.ApiCollabDev.dto.ContributionDto;
import odk.groupe4.ApiCollabDev.dto.Participant_projetDto;
import odk.groupe4.ApiCollabDev.models.Participant;
import odk.groupe4.ApiCollabDev.service.Participant_projetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/participant")
public class Participant_projetController {
    @Autowired
    private Participant_projetService participantProjetService;
    private Participant_projetDao participantProjetDao;
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
