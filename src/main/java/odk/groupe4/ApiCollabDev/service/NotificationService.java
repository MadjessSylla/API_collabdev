package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.NotificationDao;
import odk.groupe4.ApiCollabDev.dto.NotificationDto;
import odk.groupe4.ApiCollabDev.models.Notification;
import odk.groupe4.ApiCollabDev.models.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationDao notificationDao;


    public List<Notification> afficherNotification() {
        return notificationDao.findAll();
    }

    public Notification ajouterNotification(NotificationDto notification){
        Notification notif = new Notification();
        //
        notif.setSujet(notification.getSujet());
        notif.setContenu(notification.getContenu());
        notif.setContributeur(notification.getContributeur());
        notif.setContributeur(notification.getContributeur());

        return notificationDao.save(notif);
    }

}
