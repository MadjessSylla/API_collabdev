package odk.groupe4.ApiCollabDev.service;

import jakarta.transaction.Transactional;
import odk.groupe4.ApiCollabDev.dao.NotificationDao;
import odk.groupe4.ApiCollabDev.dao.UtilisateurDao;
import odk.groupe4.ApiCollabDev.dto.NotificationResponseDto;
import odk.groupe4.ApiCollabDev.models.Contributeur;
import odk.groupe4.ApiCollabDev.models.Notification;
import odk.groupe4.ApiCollabDev.models.Utilisateur;
import odk.groupe4.ApiCollabDev.service.utility.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationDao notificationDao;
    private final UtilisateurDao utilisateurDao;
    private final EmailService emailService;

    @Autowired
    public NotificationService(NotificationDao notificationDao,
                               UtilisateurDao utilisateurDao,
                               EmailService emailService) {
        this.notificationDao = notificationDao;
        this.utilisateurDao = utilisateurDao;
        this.emailService = emailService;
    }

    /**
     * Crée une notification pour un utilisateur et envoie un email.
     *
     * @param utilisateur L'utilisateur à notifier.
     * @param sujet       Le sujet de la notification.
     * @param message     Le message de la notification.
     */
    public NotificationResponseDto createNotification(Utilisateur utilisateur, String sujet, String message) {
        // Vérification de l'existence de l'utilisateur
        Notification notification = new Notification();
        // Remplissage des champs de la notification
        notification.setUtilisateur(utilisateur);
        notification.setSujet(sujet);
        notification.setMessage(message);
        notification.setDateCreation(LocalDateTime.now());
        notification.setLu(false);

        // Enregistrement de la notification dans la base de données
        Notification savedNotification = notificationDao.save(notification);

        // Envoie de la notification par email :
        System.out.println(
                emailService.envoyerEmail(utilisateur.getEmail(), sujet, message)
        );

        return mapToResponseDto(savedNotification);
    }

    /**
     * Récupère toutes les notifications d'un utilisateur
     */
    public List<NotificationResponseDto> getNotificationsByUtilisateur(int utilisateurId) {
        Utilisateur utilisateur = utilisateurDao.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + utilisateurId));

        List<Notification> notifications = notificationDao.findByUtilisateurOrderByDateCreationDesc(utilisateur);

        return notifications.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Récupère les notifications d'un utilisateur avec pagination
     */
    public Page<NotificationResponseDto> getNotificationsByUtilisateurWithPagination(int utilisateurId, int page, int size) {
        Utilisateur utilisateur = utilisateurDao.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + utilisateurId));

        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notifications = notificationDao.findByUtilisateurOrderByDateCreationDesc(utilisateur, pageable);

        return notifications.map(this::mapToResponseDto);
    }

    /**
     * Récupère les notifications non lues d'un utilisateur
     */
    public List<NotificationResponseDto> getUnreadNotifications(int utilisateurId) {
        Utilisateur utilisateur = utilisateurDao.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + utilisateurId));

        List<Notification> notifications = notificationDao.findByUtilisateurAndLuFalseOrderByDateCreationDesc(utilisateur);

        return notifications.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Compte le nombre de notifications non lues d'un utilisateur
     */
    public long countUnreadNotifications(int utilisateurId) {
        Utilisateur utilisateur = utilisateurDao.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + utilisateurId));

        return notificationDao.countByUtilisateurAndLuFalse(utilisateur);
    }

    /**
     * Marque une notification comme lue
     */
    public NotificationResponseDto markAsRead(int notificationId) {
        Notification notification = notificationDao.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée avec l'ID: " + notificationId));

        notification.setLu(true);
        Notification savedNotification = notificationDao.save(notification);

        return mapToResponseDto(savedNotification);
    }

    /**
     * Marque toutes les notifications d'un utilisateur comme lues
     */
    @Transactional
    public void markAllAsRead(int utilisateurId) {
        if (!utilisateurDao.existsById(utilisateurId)) {
            throw new RuntimeException("Utilisateur non trouvé avec l'ID: " + utilisateurId);
        }

        notificationDao.markAllAsReadByUtilisateurId(utilisateurId);
    }

    /**
     * Supprime une notification spécifique
     */
    public void deleteNotification(int notificationId) {
        if (!notificationDao.existsById(notificationId)) {
            throw new RuntimeException("Notification non trouvée avec l'ID: " + notificationId);
        }

        notificationDao.deleteById(notificationId);
    }

    /**
     * Supprime toutes les notifications d'un utilisateur
     */
    @Transactional
    public void deleteAllNotifications(int utilisateurId) {
        if (!utilisateurDao.existsById(utilisateurId)) {
            throw new RuntimeException("Utilisateur non trouvé avec l'ID: " + utilisateurId);
        }

        notificationDao.deleteAllByUtilisateurId(utilisateurId);
    }

    /**
     * Mappe une entité Notification vers un DTO de réponse
     */
    private NotificationResponseDto mapToResponseDto(Notification notification) {
        String nom, prenom;
        if (notification.getUtilisateur() instanceof Contributeur){
            nom =  ((Contributeur) notification.getUtilisateur()).getNom();
            prenom = ((Contributeur) notification.getUtilisateur()).getPrenom();
        } else {
            nom = null;
            prenom = null;
        }
        return new NotificationResponseDto(
                notification.getId(),
                notification.getSujet(),
                notification.getMessage(),
                notification.getDateCreation(),
                notification.isLu(),
                nom,
                prenom,
                notification.getUtilisateur().getEmail()
        );
    }
}
