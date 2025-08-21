package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Commentaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_commentaire")
    private int id;

    @Column(length = 500)
    private String contenu;

    @Column(name = "date_creation")
    private LocalDate creationDate;

    @ManyToOne
    @JoinColumn(name = "id_auteur")
    private Participant auteur;

    @ManyToOne
    private Commentaire commentaireParent;

    @OneToMany(mappedBy = "commentaireParent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Commentaire> reponses = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "id_projet")
    private Projet projet;
}
