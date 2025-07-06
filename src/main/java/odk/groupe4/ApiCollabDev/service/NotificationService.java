// NotificationService.java
/*package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.models.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.
import org.springframework.stereotype.Service;
import odk.groupe4.ApiCollabDev.push.PushNotificationService;

@Service
public class NotificationService {

    private final JavaMailSender emailSender;
    private final PushNotificationService pushService;

    @Autowired
    public NotificationService(
            JavaMailSender emailSender,
            PushNotificationService pushService
    ) {
        this.emailSender = emailSender;
        this.pushService = pushService;
    }

    public void sendNotification(Participant participant, String subject, String content) {
        // Notification Email
        sendEmailNotification(participant, subject, content);

        // Notification Push
        sendPushNotification(participant, subject, content);
    }

    private void sendEmailNotification(Participant participant, String subject, String content) {
        String email = participant.getContributeur().getEmail();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(content);

        emailSender.send(message);
    }

    private void sendPushNotification(Participant participant, String title, String body) {
        String deviceToken = participant.getContributeur().getDeviceToken();
        if (deviceToken != null && !deviceToken.isEmpty()) {
            pushService.sendPushNotification(deviceToken, title, body);
        }
    }
}*/