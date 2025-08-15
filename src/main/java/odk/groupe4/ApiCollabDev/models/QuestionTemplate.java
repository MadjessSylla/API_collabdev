package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class QuestionTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_question_template")
    private int id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @ElementCollection
    @CollectionTable(name = "question_template_options", joinColumns = @JoinColumn(name = "question_template_id"))
    @Column(name = "option_text")
    private List<String> options;

    @ElementCollection
    @CollectionTable(name = "question_template_reponses", joinColumns = @JoinColumn(name = "question_template_id"))
    @Column(name = "index_reponse")
    private List<Integer> indexReponse;

    @Column(nullable = false)
    private int ordre;

    @Column(columnDefinition = "TEXT")
    private String explication;

    @ManyToOne
    @JoinColumn(name = "id_questionnaire_template", nullable = false)
    private QuestionnaireTemplate questionnaireTemplate;
}
