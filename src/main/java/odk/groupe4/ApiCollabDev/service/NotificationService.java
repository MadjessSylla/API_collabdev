package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.NotificationDao;
import odk.groupe4.ApiCollabDev.models.Notifications;
import odk.groupe4.ApiCollabDev.models.Utilisateur;

public class NotificationService {
    private NotificationDao notificationRepository;

    public void createNotification(Utilisateur utilisateur, String sujet, String message) {
        Notifications notification = new Notifications();
        notification.setUtilisateur(utilisateur);
        notification.setSujet(sujet);
        notification.setMessage(message);
        notificationRepository.save(notification);
    }
}
