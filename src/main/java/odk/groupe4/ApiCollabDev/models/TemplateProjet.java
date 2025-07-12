package odk.groupe4.ApiCollabDev.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.enums.ProjectDomain;
import odk.groupe4.ApiCollabDev.models.enums.ProjectLevel;
import odk.groupe4.ApiCollabDev.models.enums.ProjectSector;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TemplateProjet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_template")
    private int id;

    @Column(length = 50)
    private String nom; // Nom du template

    private String description; // Description du template

    @Enumerated(EnumType.STRING)
    private ProjectDomain domaine; // Domaine du template

    @Enumerated(EnumType.STRING)
    private ProjectSector secteur; // Secteur du template

    @Enumerated(EnumType.STRING)
    private ProjectLevel niveauRecommande; // Niveau recommandé

    private String structureProjet; // Structure JSON du projet template

    private LocalDate dateCreation; // Date de création du template

    private boolean actif = true; // Template actif ou non

    // Administrateur qui a créé le template
    @ManyToOne
    @JoinColumn(name = "id_administrateur")
    @JsonIgnore
    private Administrateur createur;

    // Questionnaires associés au template
    @OneToMany(mappedBy = "templateProjet", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Questionnaire> questionnaires = new HashSet<>();
}
