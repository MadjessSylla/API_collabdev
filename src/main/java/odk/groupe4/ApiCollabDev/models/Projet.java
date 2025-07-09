package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.enums.DomaineProjet;
import odk.groupe4.ApiCollabDev.models.enums.NiveauProjet;
import odk.groupe4.ApiCollabDev.models.enums.SecteurProjet;
import odk.groupe4.ApiCollabDev.models.enums.StatusProject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Projet {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 50)
    private String titre;
    private String description;
    private String urlCahierDeCharge;

    @Enumerated(EnumType.STRING)
    private StatusProject status;

    @Enumerated(EnumType.STRING)
    private DomaineProjet domaine;

    @Enumerated(EnumType.STRING)
    private SecteurProjet secteur;

    @Enumerated(EnumType.STRING)
    private NiveauProjet niveauProjet;


    // Liste des fonctionnalités de la classe fonctionnalités
    // OprhanRemoval = true permet d'indiquer que les objects fonctionnalités seront supprimés avec l'objet projet concerné
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Fonctionnalite> fonctionnalites = new ArrayList<>();

    // Clé de réference pour l'association plusieurs à plusieurs vers la table Participants
    @OneToMany(mappedBy = "projet")
    private Set<Participant> participants = new HashSet<>();

    //clé étrangère de la table administrateur
    @ManyToOne @JoinColumn(name="id_validateur")
  private Administrateur administrateur;

    @ManyToOne @JoinColumn(name="id_contributeur")
    private Contributeur contributeur;
}

