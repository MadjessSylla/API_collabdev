package odk.groupe4.ApiCollabDev.controllers;


import odk.groupe4.ApiCollabDev.dto.NotificationDto;
import odk.groupe4.ApiCollabDev.models.Notification;
import odk.groupe4.ApiCollabDev.service.CommentaireService;
import odk.groupe4.ApiCollabDev.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping
public class NotificationController {

    private NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService){
        this.notificationService = notificationService;
    }


    // SIMPO POST
    public Notification creerNotification(@RequestBody NotificationDto notificationDto){
        return notificationService.creerNotification(notificationDto);
    }

}
