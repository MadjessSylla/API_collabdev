package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class QuestionsQuestionnaire  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_questions")
    private int id;

    @Column(length = 500)
    private String question; // Titre de la question

    @ElementCollection
    @CollectionTable(name = "question_options", joinColumns = @JoinColumn(name = "id_question"))
    @Column(name = "option_text")
    private List<String> options; // Réponses possibles

    @ElementCollection
    @CollectionTable(name = "question_index_reponse", joinColumns = @JoinColumn(name = "id_question"))
    @Column(name = "index_reponse")
    private List<Integer> indexReponse; // Num d'indice des bonnes réponses

    // Une Question est assigné à un quiz.
    @ManyToOne
    @JoinColumn(name = "id_questionnaire")
    private Questionnaire questionnaire;
}
