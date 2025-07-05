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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Projet {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 50)
    private String titre; // Titre du projet
    private String description; // Description du projet
    private DomaineProjet domaine; // Domaine du projet (ex: Web, Mobile, IA, etc.)
    private SecteurProjet secteur; // Secteur du projet (ex: Santé, Éducation, Finance, etc.)
    private String urlCahierDeCharge; // URL du cahier des charges du projet au format PDF
    private NiveauProjet niveau; // Niveau de difficulté du projet (ex: Débutant, Intermédiaire, Avancé, Expert)
    @Enumerated(EnumType.STRING)
    private StatusProject status; // Statut du projet (ex: En cours, Terminé, En attente, etc.)
    private LocalDate dateCreation; // Date de création du projet

    // Clé étrangère vers l'entité Contributeur (Créateur du projet)
    @ManyToOne
    @JoinColumn(name = "id_createur")
    private Contributeur createur;

    // Liste des fonctionnalités du projet lié à l'entité Fonctionnalité
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Fonctionnalite> fonctionnalites = new ArrayList<>();

    // Association avec l'entité Contributeur pour les contributeurs qui débloquent l'accès au projet
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "projet_debloque",
            joinColumns = @JoinColumn(name = "id_projet"),
            inverseJoinColumns = @JoinColumn(name = "id_contributeur"))
    private Set<Contributeur> contributeurs = new HashSet<>();

    // Clé étrangère vers l'entité Questionnaire (Questionnaires associés au projet)
    @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Questionnaire> questionnaires = new HashSet<>(); // Liste des questionnaires associés au projet

    // Clé étrangère vers l'entité Participant (Contributeurs qui participent au projet)
    @OneToMany(mappedBy = "projet")
    private Set<Participant> participants = new HashSet<>();

    // Clé étrangère vers l'entité Administrateur (Celui qui valide le projet)
    @ManyToOne
    @JoinColumn(name = "id_validateur")
    private Administrateur validateur;
}