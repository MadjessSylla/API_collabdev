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

    /**
     * Crée une notification pour un utilisateur et envoie un email.
     *
     * @param utilisateur L'utilisateur à notifier.
     * @param sujet       Le sujet de la notification.
     * @param message     Le message de la notification.
     */
    public void createNotification(Utilisateur utilisateur, String sujet, String message) {
        // Vérification de l'existence de l'utilisateur
        Notifications notification = new Notifications();
        // Remplissage des champs de la notification
        notification.setUtilisateur(utilisateur);
        notification.setSujet(sujet);
        notification.setMessage(message);
        // Enregistrement de la notification dans la base de données
        notificationRepository.save(notification);

        // Envoie de la notification par email :
        System.out.println(
                emailService.envoyerEmail(utilisateur.getEmail(), sujet, message)
        );
    }
}
