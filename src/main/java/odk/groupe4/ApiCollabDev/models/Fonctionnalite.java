package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.enums.FeaturesStatus;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name = "fonctionnalites")
public class Fonctionnalite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fonctionnalite")
    private int id;

    @Column(length = 100, nullable = false)
    private String titre;

    @Column(length = 500)
    // Contenu de la fonctionnalité (description détaillée)
    private String contenu;

    @Enumerated(EnumType.STRING)
    // Statut de la fonctionnalité (ex: En attente, En cours, Terminé, etc.)
    private FeaturesStatus statusFeatures;

    // Une fonctionnalité est associée à un projet spécifique
    @ManyToOne
    @JoinColumn(name = "id_projet")
    private Projet projet;

    // Une fonctionnalité peut être assignée à un participant spécifique
    @ManyToOne
    @JoinColumn(name = "id_participant")
    private Participant participant;
}
