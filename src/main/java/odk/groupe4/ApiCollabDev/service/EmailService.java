package odk.groupe4.ApiCollabDev.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String expediteur;

    public String envoyerEmail(String to, String sujet, String contenu){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(expediteur);
            message.setTo(to);
            message.setSubject(sujet);
            message.setText(contenu);
            javaMailSender.send(message);
            return "Message envoyé avec succès !";
        } catch (Exception e) {
            return "Erreur lors de l'envoie de l'email !";
        }
    }
}
