package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.enums.NiveauProfil;
import odk.groupe4.ApiCollabDev.models.enums.StatusProject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Projet {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_projet")
    private int id;

    @Column(length = 50)
    private String titre;
    private String description;
    @Column(length = 30)
    private String domaine;
    private String urlCahierDeCharge;

    @Enumerated(EnumType.STRING)
    private StatusProject status;

    @Enumerated(EnumType.STRING)
    private NiveauProfil niveauProfil;
    private LocalDate dateCreation;

    // Association un à plusieurs vers la table Fonctionnalité
    @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Fonctionnalite> Listefonctionnalites = new ArrayList<>();

    // Association plusieurs à un vers la table Contributeur
    @ManyToOne
    @JoinColumn(name = "id_createur")
    private Contributeur createur;

    // Clé de réference pour l'association plusieurs à plusieurs vers la table Participants
    @OneToMany(mappedBy = "projet")
    private Set<Participant> participants = new HashSet<>();

    // Association plusieurs à un vers la table Administrateur
    @ManyToOne
    @JoinColumn(name = "id_admin")
    private Administrateur administrateur;
}

