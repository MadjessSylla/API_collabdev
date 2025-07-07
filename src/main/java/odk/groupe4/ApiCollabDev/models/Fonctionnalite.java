package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.enums.StatusFeatures;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name = "fonctionnalites")
public class Fonctionnalite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fonctionnalite")
    private int id;

    @Column(length = 50)
    private String titre;

    @Column(length = 500)
    private String contenu;

    @Enumerated(EnumType.STRING)
    private StatusFeatures statusFeatures;

    @ManyToOne
    @JoinColumn(name = "id_projet")
    private Projet projet;

    @OneToOne(mappedBy = "fonctionnalite", cascade = CascadeType.ALL, orphanRemoval = true)
    private Participant participant;
}
