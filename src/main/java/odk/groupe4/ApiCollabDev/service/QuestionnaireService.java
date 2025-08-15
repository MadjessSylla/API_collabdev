package odk.groupe4.ApiCollabDev.service;

import jdk.jshell.execution.Util;
import odk.groupe4.ApiCollabDev.dao.*;
import odk.groupe4.ApiCollabDev.dto.*;
import odk.groupe4.ApiCollabDev.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class QuestionnaireService {
    private final QuestionnaireDao questionnaireDao;
    private final QuestionsQuestionnaireDao questionDao;
    private final UtilisateurDao utilisateurDao;
    private final ProjetDao projetDao;

    @Autowired
    public QuestionnaireService(QuestionnaireDao questionnaireDao,
                                QuestionsQuestionnaireDao questionDao,
                                UtilisateurDao utilisateurDao,
                                ProjetDao projetDao) {
        this.questionnaireDao = questionnaireDao;
        this.questionDao = questionDao;
        this.utilisateurDao = utilisateurDao;
        this.projetDao = projetDao;
    }

    public QuestionnaireDetailResponseDto creerQuestionnaireProjet(int idProjet, int idCreateur, QuestionnaireDto dto) {
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'ID: " + idProjet));

        Utilisateur createur = utilisateurDao.findById(idCreateur)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + idCreateur));

        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setTitre(dto.getTitre());
        questionnaire.setDescription(dto.getDescription());
        questionnaire.setType(dto.getType());
        questionnaire.setDureeEstimee(dto.getDureeEstimee());
        questionnaire.setDateCreation(LocalDate.now());
        questionnaire.setUtilisateur(createur);
        questionnaire.setProjet(projet);

        Questionnaire savedQuestionnaire = questionnaireDao.save(questionnaire);
        return mapToDetailResponseDto(savedQuestionnaire);
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
            List<Integer> reponsesParticipant = reponses.getReponses().get(question.getId());
            if (reponsesParticipant != null && reponsesParticipant.equals(question.getIndexReponse())) {
                score++;
            }
        }

        double pourcentage = totalQuestions > 0 ? (double) score / totalQuestions * 100 : 0;
        String niveau = determinerNiveau(pourcentage);
        String commentaire = genererCommentaire(pourcentage);

        return new ResultatQuizDto(score, totalQuestions, pourcentage, niveau, commentaire);
    }

    public List<QuestionnaireDetailResponseDto> getQuestionnairesByProjet(int idProjet) {
        if (!projetDao.existsById(idProjet)) {
            throw new RuntimeException("Projet non trouvé avec l'ID: " + idProjet);
        }

        return questionnaireDao.findByProjetId(idProjet).stream()
                .map(this::mapToDetailResponseDto)
                .collect(Collectors.toList());
    }

    public QuestionnaireDetailResponseDto getQuestionnaireById(int id) {
        Questionnaire questionnaire = questionnaireDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Questionnaire non trouvé avec l'ID: " + id));
        return mapToDetailResponseDto(questionnaire);
    }

    public void supprimerQuestionnaire(int id) {
        Questionnaire questionnaire = questionnaireDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Questionnaire non trouvé avec l'ID: " + id));
        questionnaireDao.delete(questionnaire);
    }

    public QuestionnaireDetailResponseDto modifierQuestionnaire(int id, QuestionnaireDto dto) {
        Questionnaire questionnaire = questionnaireDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Questionnaire non trouvé avec l'ID: " + id));

        questionnaire.setTitre(dto.getTitre());
        questionnaire.setDescription(dto.getDescription());
        questionnaire.setType(dto.getType());
        questionnaire.setDureeEstimee(dto.getDureeEstimee());

        Questionnaire savedQuestionnaire = questionnaireDao.save(questionnaire);
        return mapToDetailResponseDto(savedQuestionnaire);
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

    private QuestionnaireDetailResponseDto mapToDetailResponseDto(Questionnaire questionnaire) {
        QuestionnaireDetailResponseDto dto = new QuestionnaireDetailResponseDto();

        // Informations de base du questionnaire
        dto.setId(questionnaire.getId());
        dto.setTitre(questionnaire.getTitre());
        dto.setDescription(questionnaire.getDescription());
        dto.setType(questionnaire.getType());
        dto.setDureeEstimee(questionnaire.getDureeEstimee());
        dto.setDateCreation(questionnaire.getDateCreation());
        dto.setNombreQuestions(questionnaire.getQuestions().size());

        // Informations sur le créateur
        Utilisateur createur = questionnaire.getUtilisateur();
        if (createur != null) {
            dto.setCreateurId(createur.getId());
            dto.setCreateurEmail(createur.getEmail());

            if (createur instanceof Contributeur) {
                Contributeur contributeur = (Contributeur) createur;
                dto.setCreateurNom(contributeur.getNom());
                dto.setCreateurPrenom(contributeur.getPrenom());
                dto.setCreateurType("CONTRIBUTEUR");
            } else if (createur instanceof Administrateur) {
                Administrateur admin = (Administrateur) createur;
                dto.setCreateurNom(null);
                dto.setCreateurPrenom(null);
                dto.setCreateurType("ADMINISTRATEUR");
            }
        }

        // Informations sur le projet
        Projet projet = questionnaire.getProjet();
        if (projet != null) {
            dto.setProjetId(projet.getId());
            dto.setProjetTitre(projet.getTitre());
            dto.setProjetDescription(projet.getDescription());
        }

        // Mapping des questions avec ordre
        AtomicInteger ordre = new AtomicInteger(1);
        List<QuestionDetailDto> questionsDto = questionnaire.getQuestions().stream()
                .map(question -> {
                    QuestionDetailDto questionDto = new QuestionDetailDto();
                    questionDto.setId(question.getId());
                    questionDto.setQuestion(question.getQuestion());
                    questionDto.setOptions(question.getOptions());
                    questionDto.setIndexReponse(question.getIndexReponse());
                    questionDto.setOrdre(ordre.getAndIncrement());
                    return questionDto;
                })
                .collect(Collectors.toList());

        dto.setQuestions(questionsDto);

        return dto;
    }
}
