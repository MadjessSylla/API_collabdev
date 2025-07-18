package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.NotificationDao;
import odk.groupe4.ApiCollabDev.dto.NotificationDto;
import odk.groupe4.ApiCollabDev.models.Notification;
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
     * @param utilisateur L'utilisateur à notifier.
     * @param sujet       Le sujet de la notification.
     * @param message     Le message de la notification.
     */
    public void createNotification(Utilisateur utilisateur, String sujet, String message) {
        // Création d'une nouvelle notification
        Notification notification = new Notification();
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

    /**
     * Ajoute une notification à partir d'un DTO et envoie un email.
     *
     * @param notificationDto Le DTO contenant les informations de la notification.
     * @return La notification créée et enregistrée.
     */
    public Notification ajouterNotification(NotificationDto notificationDto) {
        // Création d'une nouvelle notification à partir du DTO
        Notification notification = new Notification();
        // Remplissage des champs de la notification
        notification.setSujet(notificationDto.getSujet());
        notification.setMessage(notificationDto.getContenu());
        notification.setUtilisateur(notificationDto.getContributeur());
        // Sauvegarde de la notification dans la base de données
        Notification savedNotification = notificationRepository.save(notification);
        // Envoi de l'email de notification
        emailService.envoyerEmail(
                notificationDto.getContributeur().getEmail(),
                notificationDto.getSujet(),
                notificationDto.getContenu()
        );
        // Retourne la notification enregistrée
        return savedNotification;
    }
}
