package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.enums.FeaturesStatus;
import odk.groupe4.ApiCollabDev.models.enums.ProjectPriority;

import java.time.LocalDate;
import java.util.List;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name = "fonctionnalites")
public class Fonctionnalite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fonctionnalite")
    private int id;

    @Column(length = 100, nullable = false)
    private String titre; // Titre de la fonctionnalité

    @Column(length = 500)
    private String contenu; // Contenu ou description de la fonctionnalité

    @Enumerated(EnumType.STRING)
    private FeaturesStatus statusFeatures; // Statut de la fonctionnalité (par exemple, "En cours", "Terminée", "En attente")

    private LocalDate dateEcheance; // Date d'échéance de la fonctionnalité

    @ElementCollection
    @CollectionTable(name = "fonctionnalite_exigences", joinColumns = @JoinColumn(name = "id_fonctionnalite"))
    @Column(name = "exigence")
    private List<String> exigences; // Exigences ou spécifications de la fonctionnalité

    @ElementCollection
    @CollectionTable(name = "fonctionnalite_criteres", joinColumns = @JoinColumn(name = "id_fonctionnalite"))
    @Column(name = "critere")
    private List<String> criteresAcceptation; // Critères d'acceptation de la fonctionnalité

    @Enumerated(EnumType.STRING)
    private ProjectPriority importance; // Priorité de la fonctionnalité (par exemple, "Haute", "Moyenne", "Faible")

    @ElementCollection
    @CollectionTable(name = "fonctionnalite_mots_cles", joinColumns = @JoinColumn(name = "id_fonctionnalite"))
    @Column(name = "mot_cle")
    private List<String> motsCles; // Mots-clés associés à la fonctionnalité pour faciliter la recherche

    // Une fonctionnalité est associée à un projet.
    @ManyToOne
    @JoinColumn(name = "id_projet")
    private Projet projet;

    // Une fonctionnalité est assignée ou réservée par un contributeur.
    @ManyToOne
    @JoinColumn(name = "id_participant")
    private Participant participant;
}
