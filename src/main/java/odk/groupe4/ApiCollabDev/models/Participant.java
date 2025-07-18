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

    // Le score du participant pour le quiz
    private String scoreQuiz;

    // Indique si le participant a débloqué le projet
    private boolean estDebloque;

    // Le projet auquel le participant est associé
    @ManyToOne
    @JoinColumn(name = "id_projet")
    private Projet projet;

    // Le contributeur associé à ce participant
    @ManyToOne
    @JoinColumn(name = "id_contributeur")
    private Contributeur contributeur;

    // Un participant peut être l'auteur de plusieurs commentaires
    @OneToMany(mappedBy = "auteur", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Commentaire> commentaires = new HashSet<>();

    // Un participant peut recevoir plusieurs badges
    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BadgeParticipant> badgeParticipants = new HashSet<>();

    // Un participant peut être assigné à plusieurs fonctionnalités
    @OneToMany(mappedBy = "participant")
    private Set<Fonctionnalite> fonctionnalite;

    // Un participant peut avoir plusieurs contributions
    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contribution> contributions = new ArrayList<>();
}
