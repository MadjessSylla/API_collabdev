// PushNotificationService.java
/*package odk.groupe4.ApiCollabDev.service;

import org.springframework.stereotype.Service;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

@Service
public class PushNotificationService {

    public void sendPushNotification(String deviceToken, String title, String body) {
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .setToken(deviceToken)
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            // Gérer les erreurs d'envoi
        }
    }
}*/