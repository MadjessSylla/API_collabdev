package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.Badge_participantDao;
import odk.groupe4.ApiCollabDev.dto.Badge_participantDto;
import odk.groupe4.ApiCollabDev.models.Badge_participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Badge_participantService {
    @Autowired
    private Badge_participantDao badgeParticipantDao;

    public Badge_participant ajouterBadgeParticipant(Badge_participantDto badgeParticipant){
       Badge_participant badge_participant = new Badge_participant();

       //
        badge_participant.setDateAcquisition(badgeParticipant.getDateAcquisition());
        badge_participant.setBadge(badgeParticipant.getBadge());
        badge_participant.setParticipant(badgeParticipant.getParticipant());

        //
        return badgeParticipantDao.save(badge_participant);
    }
}
