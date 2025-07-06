package odk.groupe4.ApiCollabDev.controllers;

import odk.groupe4.ApiCollabDev.dto.HistAcquisitionDto;
import odk.groupe4.ApiCollabDev.service.Participant_projetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/participant_projet")
public class Participant_projetController {

    private final Participant_projetService participantProjetService;

    @Autowired
    public Participant_projetController(Participant_projetService participantProjetService) {
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
}
