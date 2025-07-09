package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.enums.DemandeParticipation;
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

    @Enumerated(EnumType.STRING)
    private DemandeParticipation demande;

    //les reponse des Quiz seront stockées dans cette variable
    private String reponseQuiz;

    //par defaut L'accès débloquer est false
    private boolean accesDebloquer=false;

    // Clé étrnagère de la table Projet
    @ManyToOne
    @JoinColumn(name = "id_projet")
    private Projet projet;

    // Clé étrnagère de la table Contributeur
    @ManyToOne
    @JoinColumn(name = "id_contributeur")
    private Contributeur contributeur;

    // On spécifie tjrs le type de la classe d'association
    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Commentaire> commentaires = new HashSet<>();

    // Clé de reference vers la classe association Badge_Participation
    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Badge_participant> badgeParticipants = new HashSet<>();

    //on creer une listes des fonctionalitées traitées par le participant
    private List<Fonctionnalite> fonctionnaliteList= new ArrayList<>();
}
