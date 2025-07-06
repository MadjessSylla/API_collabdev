package odk.groupe4.ApiCollabDev.service.interfaces;

import odk.groupe4.ApiCollabDev.models.Commentaire;
import odk.groupe4.ApiCollabDev.models.Participant;

public interface NotificationDiffuseur {
    void sabonner(Participant participant);
    void seDesabonner(Participant participant);
    void notifierParticipants(Commentaire commentaire, Participant auteur);
}
