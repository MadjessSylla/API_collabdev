package odk.groupe4.ApiCollabDev.service.interfaces;

import odk.groupe4.ApiCollabDev.models.Commentaire;
import odk.groupe4.ApiCollabDev.models.Participant;
import odk.groupe4.ApiCollabDev.models.interfaces.NotificationObserver;

public interface NotificationDiffuseur {
    void sabonner(NotificationObserver observer);
    void seDesabonner(NotificationObserver observer);
    void notifierParticipants(NotificationObserver exp, String message);
}
