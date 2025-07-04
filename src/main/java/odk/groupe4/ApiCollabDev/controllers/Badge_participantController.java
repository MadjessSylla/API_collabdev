package odk.groupe4.ApiCollabDev.controllers;

import lombok.AllArgsConstructor;
import odk.groupe4.ApiCollabDev.dao.Badge_participantDao;
import odk.groupe4.ApiCollabDev.dto.Badge_participantDto;
import odk.groupe4.ApiCollabDev.models.Badge_participant;
import odk.groupe4.ApiCollabDev.service.Badge_participantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("api/Badge_participant")
public class Badge_participantController {
    @Autowired
    private Badge_participantService badgeParticipantService;
    private Badge_participantDao badgeParticipantDao;

    @PostMapping
    public Badge_participant creerBadgeParticipant(@RequestBody Badge_participantDto badgeParticipant ){
        return badgeParticipantService.ajouterBadgeParticipant(badgeParticipant);
    }
}
