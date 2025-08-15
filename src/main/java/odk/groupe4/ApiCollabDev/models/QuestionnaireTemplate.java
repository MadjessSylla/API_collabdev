package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.enums.ProjectDomain;
import odk.groupe4.ApiCollabDev.models.enums.ProjectSector;
import odk.groupe4.ApiCollabDev.models.enums.TypeQuiz;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class QuestionnaireTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_questionnaire_template")
    private int id;

    @Column(nullable = false, length = 200)
    private String titre;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeQuiz type;

    @Enumerated(EnumType.STRING)
    private ProjectDomain domaine;

    @Enumerated(EnumType.STRING)
    private ProjectSector secteur;

    @Column(nullable = false)
    private int dureeEstimee;

    private LocalDate dateCreation;

    @Column(nullable = false)
    private boolean estActif = true;

    @Column(nullable = false)
    private int nombreUtilisations = 0;

    // Créateur du template (administrateur généralement)
    @ManyToOne
    @JoinColumn(name = "id_createur")
    private Utilisateur createur;

    @OneToMany(mappedBy = "questionnaireTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<QuestionTemplate> questions = new HashSet<>();

    // Tags pour faciliter la recherche
    @Column(length = 500)
    private String tags;

    @Column(columnDefinition = "TEXT")
    private String objectifsPedagogiques;
}
