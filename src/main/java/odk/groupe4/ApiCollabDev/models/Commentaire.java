package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Commentaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_commentaire")
    private int id;
    private String contenu; // Contenu du commentaire
    private LocalDate date; // Date de création du commentaire

    // Auteur du commentaire
    // Clé étrangère vers la table Contributeur
    @ManyToOne
    @JoinColumn(name = "id_auteur")
    private Participant auteur;

}
