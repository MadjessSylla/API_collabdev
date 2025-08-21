package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.enums.TypeQuiz;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Questionnaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_questionnaire")
    private int id;

    private String titre; // Titre du questionnaire

    @Column(columnDefinition = "TEXT")
    private String description; // Description du questionnaire

    @Enumerated(EnumType.STRING)
    private TypeQuiz type; // Type de questionnaire (par exemple, "Quiz Gestionnaire", "Quiz Développeur","Quiz Design", etc.)

    private int dureeEstimee; // Durée estimée pour remplir le questionnaire en minutes

    private LocalDate dateCreation; // Date de création du questionnaire

    // Un questionnaire peut être associé à plusieurs questions.
    @OneToMany(mappedBy = "questionnaire", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<QuestionsQuestionnaire> questions = new HashSet<>();

    // Un questionnaire peut être rempli par plusieurs contributeurs ou Administrateur.
    @ManyToOne
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;

    // Un questionnaire peut être affecté à plusieurs projets.
    @ManyToOne
    @JoinColumn(name = "id_projet")
    private Projet projet;
}
