package odk.groupe4.ApiCollabDev.models.interfaces;

import odk.groupe4.ApiCollabDev.models.Commentaire;
import odk.groupe4.ApiCollabDev.models.Participant;

public interface NotificationObserver {
    void recevoir(Commentaire commentaire, Participant auteur);
}
