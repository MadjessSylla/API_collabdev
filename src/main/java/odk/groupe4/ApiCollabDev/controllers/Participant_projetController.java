package odk.groupe4.ApiCollabDev.controllers;

import odk.groupe4.ApiCollabDev.dao.Participant_projetDao;
import odk.groupe4.ApiCollabDev.dto.ContributionDto;
import odk.groupe4.ApiCollabDev.dto.Participant_projetDto;
import odk.groupe4.ApiCollabDev.models.Participant;
import odk.groupe4.ApiCollabDev.service.Participant_projetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/participant")
public class Participant_projetController {
    @Autowired
    private Participant_projetService participantProjetService;
    private Participant_projetDao participantProjetDao;

    @PostMapping
    public Participant creerParticipant(@RequestBody Participant_projetDto participant){
        return participantProjetService.ajouterParticipant(participant);
    }
    // Soumettre une contribution
    @PostMapping("/{idParticipant}/SoumettreUneContribution")
    public ContributionDto SoumettreContribution(@RequestHeader(value = "Date", required = false) String dateHeader,
                                                 @PathVariable int idParticipant,
                                                 @RequestBody ContributionDto contributiondto) {
        return participantProjetService.SoumettreUneContribution(dateHeader, idParticipant, contributiondto);
    }
    //Méthode pour reserver une fonctionnalité à un participant
    @PutMapping("/{idParticipant}/reserverFonctionnalite/{idFonctionnalite}")
    public Participant_projetDto reserverFonctionnalite(@PathVariable int idParticipant, @PathVariable int idFonctionnalite) {
        return participantProjetService.reserverFonctionnalite(idParticipant, idFonctionnalite);
    }
    // Méthode pour afficher les contributions d'un participant
    @GetMapping("/{idParticipant}/contributions")
    public List<ContributionDto> afficherContributions(@PathVariable int idParticipant) {
        return participantProjetService.afficherContributionsParticipant(idParticipant);
    }
}
