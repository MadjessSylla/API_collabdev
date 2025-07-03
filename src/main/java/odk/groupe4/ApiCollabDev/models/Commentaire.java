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
    @JoinColumn(name = "id_commentaire")
    private int id;
    @Column(length = 500)
    private String contenu;
    private LocalDate dateCreation;

    // Clé étrangère de la table Participant
    @ManyToOne
    @JoinColumn(name = "id_participant")
    private Participant participant;
}
