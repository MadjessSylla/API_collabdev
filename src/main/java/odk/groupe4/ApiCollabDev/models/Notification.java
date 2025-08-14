package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notification")
    private int id;

    @Column(length = 50)
    private String sujet;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @Column(name = "lu")
    private boolean lu = false; // Par défaut, une notification n'est pas lue

    @ManyToOne
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur; // L'utilisateur qui reçoit la notification
}
