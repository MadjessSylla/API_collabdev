package odk.groupe4.ApiCollabDev.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private NotificationDao notificationDao;

    @Autowired
    public NotificationService(NotificationDao notificationDao){
        this.notificationDao = notificationDao;
    }

    // SIMPO POST

    public Notification creerNotification(NotificationDto dto){
        //Initialisation d'un notification a partir du DTO
        Notification notification = new Notification();

        notification.setSujet(dto.getSujet());
        notification.setContenu(dto.getContenu());
        notification.setDestinataireEmail(dto.getDestinateurEmail());
        notification.setContribution(dto.getContribution());

        return notificationDao.save(notification);
    }

    //SIMPO GET BY ID
    public Notification afficherUneNotification(int id_notification){
        return notificationDao.findById(id_notification)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: \" + id_notification"));
    }


    // SIMPO GET ALL
    public List<Notification> afficherNotification(){

        return notificationDao.findAll();
    }


    //SIMPO DELETE
    public String supprimerNotification(int id_notification){
        // Vérification de l'existence du notification avant la suppression
        if (!notificationDao.existsById(id_notification)){
            throw new RuntimeException("Notification not found with id : " + id_notification);
        }
        //suppression du commentaire
        notificationDao.deleteById(id_notification);
        return "notification avec l'id " + id_notification + "a ete supprimer avec succes.";
    }

}
