package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.NotificationDao;
import odk.groupe4.ApiCollabDev.models.Notifications;
import odk.groupe4.ApiCollabDev.models.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private NotificationDao notificationRepository;
    private EmailService emailService;

    @Autowired
    public NotificationService(NotificationDao notificationRepository, EmailService emailService) {
        this.notificationRepository = notificationRepository;
        this.emailService = emailService;
    }

    public void createNotification(Utilisateur utilisateur, String sujet, String message) {
        Notifications notification = new Notifications();
        notification.setUtilisateur(utilisateur);
        notification.setSujet(sujet);
        notification.setMessage(message);
        notificationRepository.save(notification);

        // Envoie de la notification par email :
        System.out.println(
                emailService.envoyerEmail(utilisateur.getEmail(), sujet, message)
        );
    }
}
