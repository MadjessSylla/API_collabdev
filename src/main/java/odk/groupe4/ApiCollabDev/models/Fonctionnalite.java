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

    private String titre; // Titre de la fonctionnalité
    private String contenu; // Contenu de la fonctionnalité (description détaillée)
    @Enumerated(EnumType.STRING)
    private StatusFeatures statusFeatures; // Statut de la fonctionnalité (ex: En attente, En cours, Terminé, etc.)

    // Clé étrangère vers l'entité Projet (Projet auquel la fonctionnalité est associée)
    @ManyToOne
    @JoinColumn(name = "id_projet")
    private Projet projet;
}
