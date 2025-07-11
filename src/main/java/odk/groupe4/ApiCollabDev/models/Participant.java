package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.enums.ParticipantProfil;
import odk.groupe4.ApiCollabDev.models.enums.ParticipantStatus;

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
    private ParticipantProfil profil; // Profil du participant (Porteur de projet, Développeur, Designer, Gestionnaire, Testeur, etc.)

    @Enumerated(EnumType.STRING)
    private ParticipantStatus statut; // EN_ATTENTE, ACCEPTE, REFUSE

    //les reponse des Quiz seront stockées dans cette variable
    private String scoreQuiz;

    //par defaut L'accès débloquer est false
    private boolean estDebloque;

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
    private Set<BadgeParticipant> badgeParticipants = new HashSet<>();

    // Clé de référence vers la classe association Fonctionnalite_Participant
    @OneToMany(mappedBy = "participant")
    private Set<Fonctionnalite> fonctionnalite;

    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contribution> contributions = new ArrayList<>();
}
