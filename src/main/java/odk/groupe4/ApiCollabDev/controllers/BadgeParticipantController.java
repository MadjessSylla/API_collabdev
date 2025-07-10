package odk.groupe4.ApiCollabDev.controllers;

import odk.groupe4.ApiCollabDev.dao.BadgeParticipantDao;
import odk.groupe4.ApiCollabDev.dto.BadgeParticipantDto;
import odk.groupe4.ApiCollabDev.models.BadgeParticipant;
import odk.groupe4.ApiCollabDev.service.BadgeParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/Badge_participant")
public class BadgeParticipantController {
    @Autowired
    private BadgeParticipantService badgeParticipantService;
    private BadgeParticipantDao badgeParticipantDao;

    @PostMapping
    public BadgeParticipant creerBadgeParticipant(@RequestBody BadgeParticipantDto badgeParticipant ){
        return badgeParticipantService.attribuerBadge(badgeParticipant);
    }
}
