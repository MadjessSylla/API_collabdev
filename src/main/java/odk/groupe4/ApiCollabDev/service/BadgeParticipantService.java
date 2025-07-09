package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.BadgeParticipantDao;
import odk.groupe4.ApiCollabDev.dto.BadgeParticipantDto;
import odk.groupe4.ApiCollabDev.models.BadgeParticipant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BadgeParticipantService {
    @Autowired
    private BadgeParticipantDao badgeParticipantDao;


    public List<BadgeParticipant> afficherBadgeParticipant() {
        return badgeParticipantDao.findAll();
    }

    public BadgeParticipant ajouterBadgeParticipant(BadgeParticipantDto badgeParticipant){
       BadgeParticipant badge_participant = new BadgeParticipant();

       //
        badge_participant.setDateAcquisition(badgeParticipant.getDateAcquisition());
        badge_participant.setBadge(badgeParticipant.getBadge());
        badge_participant.setParticipant(badgeParticipant.getParticipant());

        //
        return badgeParticipantDao.save(badge_participant);
    }
}
