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
    @Column(length = 500)
    private String contenu;

    // Clé étranègre de la table contribution.
    @ManyToOne
    @JoinColumn(name = "id_contribution")
    private Contribution contribution;

    // Clé étrangère de la table contributeur.
    @ManyToOne
    @JoinColumn(name = "id_contributeur")
    private Contributeur contributeur;
}
