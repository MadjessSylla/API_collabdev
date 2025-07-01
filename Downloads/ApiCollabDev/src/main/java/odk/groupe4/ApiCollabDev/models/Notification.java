package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "id_notification")
    private int id;
    private String sujet;
    private String contenu;
    private String destinataireEmail;

    // Clé étranègre de la table contribution
    @ManyToOne
    @JoinColumn(name = "id_contributeur")
    private Contribution contribution;
}
