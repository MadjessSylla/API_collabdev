package odk.groupe4.ApiCollabDev.controllers;

import odk.groupe4.ApiCollabDev.dao.Participant_projetDao;
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

    @PostMapping
    public Participant creerParticipant(@RequestBody Participant_projetDto participant){
        return participantProjetService.ajouterParticipant(participant);
    }
}
