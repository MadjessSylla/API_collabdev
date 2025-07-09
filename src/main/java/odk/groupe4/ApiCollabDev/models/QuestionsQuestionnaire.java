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

    private String question; // Titre de la question

    private List<String> options; // Liste des options de réponse

    private int indexReponse; // Indice de la bonne réponse dans la liste des options

    @ManyToOne
    @JoinColumn(name = "id_questionnaire")
    private Questionnaire questionnaire; // Référence au questionnaire auquel cette question appartient

}
