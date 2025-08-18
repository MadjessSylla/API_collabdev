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
import java.util.*;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Projet {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_projet")
    private int id;

    @Column(length = 50)
    private String titre;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    private ProjectDomain domaine;

    @Enumerated(EnumType.STRING)
    private ProjectSector secteur;

    private String urlCahierDeCharge;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @Enumerated(EnumType.STRING)
    private ProjectLevel niveau;

    private LocalDate dateCreation;

    private LocalDate dateEcheance;

    // Un projet est créé par un contributeur.
    @ManyToOne @JoinColumn(name = "id_createur")
    private Contributeur createur;

    // Un projet peut être validé par un administrateur.
    @ManyToOne @JoinColumn(name="id_validateur")
    private Administrateur validateur;

    // Un projet peut avoir plusieurs fonctionnalités.
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Fonctionnalite> fonctionnalites = new ArrayList<>();

    // Un projet peut être débloqué par plusieurs contributeurs.
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "projet_debloque",
            joinColumns = @JoinColumn(name = "id_projet"),
            inverseJoinColumns = @JoinColumn(name = "id_contributeur"))
    private Set<Contributeur> contributeurs = new HashSet<>();

    // Un projet peut avoir plusieurs questionnaires.
    @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Questionnaire> questionnaires = new HashSet<>();

    // Un projet peut avoir plusieurs participants.
    @OneToMany(mappedBy = "projet", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Participant> participants = new HashSet<>();
}
