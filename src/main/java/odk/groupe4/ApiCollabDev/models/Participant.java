package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.enums.Profil;
import odk.groupe4.ApiCollabDev.models.enums.StatusParticipant;

import java.util.HashSet;
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

    // Clé étrangère de la table Projet
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

    @OneToMany(mappedBy = "participant")
    private Set<Fonctionnalite> fonctionnalitesTraite = new HashSet<>();

}
