package odk.groupe4.ApiCollabDev.controllers;


import jakarta.validation.Valid;
import odk.groupe4.ApiCollabDev.dao.NotificationDao;
import odk.groupe4.ApiCollabDev.dto.NotificationDto;
import odk.groupe4.ApiCollabDev.models.Notification;
import odk.groupe4.ApiCollabDev.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    private NotificationDao notificationDao;


    @PostMapping
    public Notification creerNotifcation(@Valid @RequestBody NotificationDto notification) {
        return notificationService.ajouterNotification(notification);
    }
}
