package odk.groupe4.ApiCollabDev.controllers;

import odk.groupe4.ApiCollabDev.dao.Badge_participantDao;
import odk.groupe4.ApiCollabDev.dto.Badge_participantDto;
import odk.groupe4.ApiCollabDev.models.BadgeParticipant;
import odk.groupe4.ApiCollabDev.service.Badge_participantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/Badge_participant")
public class Badge_participantController {
    @Autowired
    private Badge_participantService badgeParticipantService;
    private Badge_participantDao badgeParticipantDao;

    @PostMapping
    public BadgeParticipant creerBadgeParticipant(@RequestBody Badge_participantDto badgeParticipant ){
        return badgeParticipantService.ajouterBadgeParticipant(badgeParticipant);
    }
}
