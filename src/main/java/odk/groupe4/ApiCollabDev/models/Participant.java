package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.enums.Profil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity @Getter  @Setter @NoArgsConstructor @AllArgsConstructor
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "id_participant")
    private int id;

    @Enumerated(EnumType.STRING)
    private Profil profil;

    // Clé étrangère de la table Projet
    @ManyToOne
    @JoinColumn(name = "id_projet")
    private Projet projet;

    // Clé étrangère de la table Contributeur
    @ManyToOne
    @JoinColumn(name = "id_contributeur")
    private Contributeur contributeur;

    // On spécifie tjrs le type de la classe d'association
    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Commentaire> commentaires = new ArrayList<>();

    // Clé de reference vers la classe association Badge_Participation
    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Badge_participant> badgeParticipants = new HashSet<>();
    // Clé de référence vers la classe association Fonctionnalite_Participant
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_fonctionnalite", referencedColumnName = "id_contributeur")
    private Fonctionnalite fonctionnalite;
}
