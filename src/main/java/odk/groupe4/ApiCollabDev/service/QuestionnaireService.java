package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.*;
import odk.groupe4.ApiCollabDev.dto.*;
import odk.groupe4.ApiCollabDev.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QuestionnaireService {
    private final QuestionnaireDao questionnaireDao;
    private final QuestionsQuestionnaireDao questionDao;
    private final ContributeurDao contributeurDao;
    private final ProjetDao projetDao;
    private final TemplateProjetDao templateDao;

    @Autowired
    public QuestionnaireService(QuestionnaireDao questionnaireDao,
                                QuestionsQuestionnaireDao questionDao,
                                ContributeurDao contributeurDao,
                                ProjetDao projetDao,
                                TemplateProjetDao templateDao) {
        this.questionnaireDao = questionnaireDao;
        this.questionDao = questionDao;
        this.contributeurDao = contributeurDao;
        this.projetDao = projetDao;
        this.templateDao = templateDao;
    }

    public QuestionnaireResponseDto creerQuestionnaireProjet(int idProjet, int idCreateur, QuestionnaireDto dto) {
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'ID: " + idProjet));

        Contributeur createur = contributeurDao.findById(idCreateur)
                .orElseThrow(() -> new RuntimeException("Contributeur non trouvé avec l'ID: " + idCreateur));

        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setTitre(dto.getTitre());
        questionnaire.setDescription(dto.getDescription());
        questionnaire.setDateCreation(LocalDate.now());
        questionnaire.setContributeur(createur);
        questionnaire.setProjet(projet);

        Questionnaire savedQuestionnaire = questionnaireDao.save(questionnaire);
        return mapToResponseDto(savedQuestionnaire);
    }

    public QuestionnaireResponseDto creerQuestionnaireTemplate(int idTemplate, int idAdmin, QuestionnaireDto dto) {
        TemplateProjet template = templateDao.findById(idTemplate)
                .orElseThrow(() -> new RuntimeException("Template non trouvé avec l'ID: " + idTemplate));

        Contributeur createur = contributeurDao.findById(idAdmin)
                .orElseThrow(() -> new RuntimeException("Créateur non trouvé avec l'ID: " + idAdmin));

        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setTitre(dto.getTitre());
        questionnaire.setDescription(dto.getDescription());
        questionnaire.setDateCreation(LocalDate.now());
        questionnaire.setContributeur(createur);
        questionnaire.setTemplateProjet(template);

        Questionnaire savedQuestionnaire = questionnaireDao.save(questionnaire);
        return mapToResponseDto(savedQuestionnaire);
    }

    public void ajouterQuestion(int idQuestionnaire, QuestionDto dto) {
        Questionnaire questionnaire = questionnaireDao.findById(idQuestionnaire)
                .orElseThrow(() -> new RuntimeException("Questionnaire non trouvé avec l'ID: " + idQuestionnaire));

        QuestionsQuestionnaire question = new QuestionsQuestionnaire();
        question.setQuestion(dto.getQuestion());
        question.setOptions(dto.getOptions());
        question.setIndexReponse(dto.getIndexReponse());
        question.setQuestionnaire(questionnaire);

        questionDao.save(question);
    }

    public ResultatQuizDto evaluerQuiz(int idQuestionnaire, ReponseQuizDto reponses) {
        Questionnaire questionnaire = questionnaireDao.findById(idQuestionnaire)
                .orElseThrow(() -> new RuntimeException("Questionnaire non trouvé avec l'ID: " + idQuestionnaire));

        List<QuestionsQuestionnaire> questions = questionnaire.getQuestions().stream().toList();
        int score = 0;
        int totalQuestions = questions.size();

        for (QuestionsQuestionnaire question : questions) {
            Integer reponseParticipant = reponses.getReponses().get(question.getId());
            if (reponseParticipant != null && reponseParticipant.equals(question.getIndexReponse())) {
                score++;
            }
        }

        double pourcentage = totalQuestions > 0 ? (double) score / totalQuestions * 100 : 0;
        String niveau = determinerNiveau(pourcentage);
        String commentaire = genererCommentaire(pourcentage);

        return new ResultatQuizDto(score, totalQuestions, pourcentage, niveau, commentaire);
    }

    public List<QuestionnaireResponseDto> getQuestionnairesByProjet(int idProjet) {
        if (!projetDao.existsById(idProjet)) {
            throw new RuntimeException("Projet non trouvé avec l'ID: " + idProjet);
        }

        return questionnaireDao.findByProjetId(idProjet).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public List<QuestionnaireResponseDto> getQuestionnairesByTemplate(int idTemplate) {
        if (!templateDao.existsById(idTemplate)) {
            throw new RuntimeException("Template non trouvé avec l'ID: " + idTemplate);
        }

        return questionnaireDao.findByTemplateProjetId(idTemplate).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public QuestionnaireResponseDto getQuestionnaireById(int id) {
        Questionnaire questionnaire = questionnaireDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Questionnaire non trouvé avec l'ID: " + id));
        return mapToResponseDto(questionnaire);
    }

    private String determinerNiveau(double pourcentage) {
        if (pourcentage >= 90) return "EXPERT";
        if (pourcentage >= 75) return "AVANCE";
        if (pourcentage >= 60) return "INTERMEDIAIRE";
        return "DEBUTANT";
    }

    private String genererCommentaire(double pourcentage) {
        if (pourcentage >= 90) return "Excellent ! Vous maîtrisez parfaitement le sujet.";
        if (pourcentage >= 75) return "Très bien ! Vous avez un bon niveau de compétence.";
        if (pourcentage >= 60) return "Bien ! Vous avez des bases solides à développer.";
        if (pourcentage >= 40) return "Passable. Il serait bénéfique de réviser certains concepts.";
        return "Insuffisant. Une formation supplémentaire est recommandée.";
    }

    private QuestionnaireResponseDto mapToResponseDto(Questionnaire questionnaire) {
        return new QuestionnaireResponseDto(
                questionnaire.getId(),
                questionnaire.getTitre(),
                questionnaire.getDescription(),
                questionnaire.getDateCreation(),
                questionnaire.getContributeur().getNom() + " " + questionnaire.getContributeur().getPrenom(),
                questionnaire.getContributeur().getEmail(),
                questionnaire.getProjet() != null ? questionnaire.getProjet().getTitre() : null,
                questionnaire.getTemplateProjet() != null ? questionnaire.getTemplateProjet().getNom() : null,
                questionnaire.getQuestions().size()
        );
    }
}
