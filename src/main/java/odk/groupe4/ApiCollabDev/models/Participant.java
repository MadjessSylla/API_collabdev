package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.enums.ParticipantProfil;
import odk.groupe4.ApiCollabDev.models.enums.ParticipantStatus;

import java.time.LocalDate;
import java.util.*;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_participant")
    private int id;

    @Enumerated(EnumType.STRING)
    private ParticipantProfil profil; // Profil du participant : Gestionnaire, Développeur, Designer

    @Enumerated(EnumType.STRING)
    private ParticipantStatus statut; // Statut de la participation EN_ATTENTE, ACCEPTE, REFUSE

    private String scoreQuiz; // Score eu lors du quiz pour participer

    private boolean estDebloque; // boolean vérifiant si le projet a été débloqué

    private LocalDate datePostulation; // Date à laquelle le participant a postulé

    @Column(columnDefinition = "TEXT")
    private String commentaireMotivation; // Motivation du participant au projet

    @Column(columnDefinition = "TEXT")
    private String commentaireExperience; // Commentaire sur l'exp

    // Un participant contribue à un projet.
    @ManyToOne
    @JoinColumn(name = "id_projet")
    private Projet projet;

    // Un participant est un contributeur
    @ManyToOne
    @JoinColumn(name = "id_contributeur")
    private Contributeur contributeur;

    // Un participant peut écrire plusieurs commentaires dans le projet.
    @OneToMany(mappedBy = "auteur", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Commentaire> commentaires = new HashSet<>();

    /*// Un participant peur recevoir des badges de récompenses
    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BadgeParticipant> badgeParticipants = new HashSet<>();*/

    // Un participant peut travailler sur plusieurs fonctionnalités d'un projet.
    @OneToMany(mappedBy = "participant")
    private Set<Fonctionnalite> fonctionnalite;

    // Un participant peut soumettre plusieurs contributions.
    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contribution> contributions = new ArrayList<>();
}
