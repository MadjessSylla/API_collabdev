package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.enums.ProjectDomain;
import odk.groupe4.ApiCollabDev.models.enums.ProjectLevel;
import odk.groupe4.ApiCollabDev.models.enums.ProjectSector;
import odk.groupe4.ApiCollabDev.models.enums.ProjectStatus;

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
    private String titre; // Titre du projet

    private String description; // Description du projet

    @Enumerated(EnumType.STRING)
    private ProjectDomain domaine; // Domaine du projet (ex: Web, Mobile, IA, etc.)

    @Enumerated(EnumType.STRING)
    private ProjectSector secteur; // Secteur du projet (ex: Santé, Éducation, Finance, etc.)

    private String urlCahierDeCharge; // URL du cahier des charges du projet au format PDF

    @Enumerated(EnumType.STRING)
    private ProjectStatus status; // Statut du projet (ex: En attente, En cours, Terminé, etc.)

    @Enumerated(EnumType.STRING)
    private ProjectLevel niveau; // Niveau du projet (ex: Débutant, Intermédiaire, Avancé)

    private LocalDate dateCreation; // Date de création du projet

    // Un projet est crée par un contributeur
    @ManyToOne @JoinColumn(name = "id_createur")
    private Contributeur createur;

    // Un projet est validé par un administrateur
    @ManyToOne @JoinColumn(name="id_validateur")
    private Administrateur validateur;

    // Un projet comprend plusieurs fonctionnalités
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Fonctionnalite> fonctionnalites = new ArrayList<>();

    // Un projet peut être débloqué par plusieurs contributeurs
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "projet_debloque",
            joinColumns = @JoinColumn(name = "id_projet"),
            inverseJoinColumns = @JoinColumn(name = "id_contributeur"))
    private Set<Contributeur> contributeurs = new HashSet<>();

    // Un projet peut avoir plusieurs QCM (Questionnaires de collecte de données)
    @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Questionnaire> questionnaires = new HashSet<>();

    // Un projet peut avoir plusieurs participants
    @OneToMany(mappedBy = "projet")
    private Set<Participant> participants = new HashSet<>();

}