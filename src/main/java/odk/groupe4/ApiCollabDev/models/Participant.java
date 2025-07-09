package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.enums.DemandeParticipation;
import odk.groupe4.ApiCollabDev.models.enums.Profil;
import odk.groupe4.ApiCollabDev.models.enums.StatusParticipant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity @Getter  @Setter @NoArgsConstructor @AllArgsConstructor
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_participant")
    private int id;

    @Enumerated(EnumType.STRING)
    private Profil profil; // Profil du participant (Porteur de projet, Développeur, Designer, Gestionnaire, Testeur, etc.)

    @Enumerated(EnumType.STRING)
    private StatusParticipant statut; // EN_ATTENTE, ACCEPTE, REFUSE

    private DemandeParticipation demande;

    //les reponse des Quiz seront stockées dans cette variable
    private String reponseQuiz;

    //par defaut L'accès débloquer est false
    private boolean accesDebloquer=false;

    @ManyToOne
    @JoinColumn(name = "id_projet")
    private Projet projet;

    // Clé étrangère de la table Contributeur
    @ManyToOne
    @JoinColumn(name = "id_contributeur")
    private Contributeur contributeur;

    // Liste des commentaires associés à ce participant
    @OneToMany(mappedBy = "auteur", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Commentaire> commentaires = new HashSet<>();

    // Les badges reçus par le participant
    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Badge_participant> badgeParticipants = new HashSet<>();

    // Clé de référence vers la classe association Fonctionnalite_Participant
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_fonctionnalite")
    // Clé de reférence vers la classe association contribution
    private Fonctionnalite fonctionnalite;
    @OneToMany(mappedBy = "participants", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contribution> contributions = new ArrayList<>();
}
