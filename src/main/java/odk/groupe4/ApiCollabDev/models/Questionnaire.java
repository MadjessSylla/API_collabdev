package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Questionnaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_questionnaire")
    private int id; // Identifiant du questionnaire

    private String titre; // Titre du questionnaire

    private String description; // Description du questionnaire

    private LocalDate dateCreation; // Date de création du questionnaire

    @OneToMany(mappedBy = "questionnaire", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<QuestionsQuestionnaire> questions = new HashSet<>(); // Liste des questions du questionnaire

    @ManyToOne
    @JoinColumn(name = "id_contributeur")
    private Contributeur contributeur; // Identifiant de l'utilisateur qui a créé le questionnaire

    @ManyToOne
    @JoinColumn(name = "id_projet")
    private Projet projet; // Identifiant du projet auquel le questionnaire est associé

    @ManyToOne
    @JoinColumn(name = "id_template")
    private TemplateProjet templateProjet; // Template auquel le questionnaire est associé
}
