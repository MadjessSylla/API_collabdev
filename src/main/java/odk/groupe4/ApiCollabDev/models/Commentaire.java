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

    @Column(length = 500)
    private String contenu;

    @Column(name = "date_creation")
    private LocalDate date;

    // Un commentaire est écrit par un auteur, qui est un participant
    @ManyToOne
    @JoinColumn(name = "id_auteur")
    private Participant auteur;

}
