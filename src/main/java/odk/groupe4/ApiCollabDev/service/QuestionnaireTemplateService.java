package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.*;
import odk.groupe4.ApiCollabDev.dto.*;
import odk.groupe4.ApiCollabDev.models.*;
import odk.groupe4.ApiCollabDev.models.enums.ProjectDomain;
import odk.groupe4.ApiCollabDev.models.enums.ProjectSector;
import odk.groupe4.ApiCollabDev.models.enums.TypeQuiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionnaireTemplateService {

    private final QuestionnaireTemplateDao templateDao;
    private final QuestionTemplateDao questionTemplateDao;
    private final QuestionnaireDao questionnaireDao;
    private final QuestionsQuestionnaireDao questionDao;
    private final UtilisateurDao utilisateurDao;
    private final ProjetDao projetDao;

    @Autowired
    public QuestionnaireTemplateService(QuestionnaireTemplateDao templateDao,
                                        QuestionTemplateDao questionTemplateDao,
                                        QuestionnaireDao questionnaireDao,
                                        QuestionsQuestionnaireDao questionDao,
                                        UtilisateurDao utilisateurDao,
                                        ProjetDao projetDao) {
        this.templateDao = templateDao;
        this.questionTemplateDao = questionTemplateDao;
        this.questionnaireDao = questionnaireDao;
        this.questionDao = questionDao;
        this.utilisateurDao = utilisateurDao;
        this.projetDao = projetDao;
    }

    // ===== MARKETPLACE - CONSULTATION =====

    public List<QuestionnaireTemplateResponseDto> getAllTemplatesActifs() {
        return templateDao.findByEstActifTrue().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public List<QuestionnaireTemplateResponseDto> getTemplatesPopulaires() {
        return templateDao.findByEstActifTrueOrderByNombreUtilisationsDesc().stream()
                .limit(10)
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public List<QuestionnaireTemplateResponseDto> getTemplatesRecents() {
        return templateDao.findByEstActifTrueOrderByDateCreationDesc().stream()
                .limit(10)
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public List<QuestionnaireTemplateResponseDto> rechercherTemplates(MarketplaceFilterDto filter) {
        List<QuestionnaireTemplate> templates;

        // Recherche par mot-clé
        if (filter.getKeyword() != null && !filter.getKeyword().trim().isEmpty()) {
            templates = templateDao.findByKeywordAndEstActifTrue(filter.getKeyword().trim());
        } else {
            // Filtrage par critères
            TypeQuiz type = filter.getType();
            ProjectDomain domaine = filter.getDomaine();
            ProjectSector secteur = filter.getSecteur();

            if (type != null && domaine != null && secteur != null) {
                templates = templateDao.findByTypeAndDomaineAndSecteurAndEstActifTrue(type, domaine, secteur);
            } else if (type != null && domaine != null) {
                templates = templateDao.findByTypeAndDomaineAndEstActifTrue(type, domaine);
            } else if (type != null && secteur != null) {
                templates = templateDao.findByTypeAndSecteurAndEstActifTrue(type, secteur);
            } else if (domaine != null && secteur != null) {
                templates = templateDao.findByDomaineAndSecteurAndEstActifTrue(domaine, secteur);
            } else if (type != null) {
                templates = templateDao.findByTypeAndEstActifTrue(type);
            } else if (domaine != null) {
                templates = templateDao.findByDomaineAndEstActifTrue(domaine);
            } else if (secteur != null) {
                templates = templateDao.findBySecteurAndEstActifTrue(secteur);
            } else {
                templates = templateDao.findByEstActifTrue();
            }
        }

        return templates.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public QuestionnaireTemplateResponseDto getTemplateAvecQuestions(int id) {
        QuestionnaireTemplate template = templateDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Template non trouvé avec l'ID: " + id));

        return mapToDetailResponseDto(template);
    }

    // ===== GESTION DES TEMPLATES (ADMIN) =====

    @Transactional
    public QuestionnaireTemplateResponseDto creerTemplate(QuestionnaireTemplateDto dto) {
        Utilisateur createur = utilisateurDao.findById(dto.getCreateurId())
                .orElseThrow(() -> new RuntimeException("Créateur non trouvé"));

        QuestionnaireTemplate template = new QuestionnaireTemplate();
        template.setTitre(dto.getTitre());
        template.setDescription(dto.getDescription());
        template.setType(dto.getType());
        template.setDomaine(dto.getDomaine());
        template.setSecteur(dto.getSecteur());
        template.setDureeEstimee(dto.getDureeEstimee());
        template.setDateCreation(LocalDate.now());
        template.setEstActif(dto.isEstActif());
        template.setCreateur(createur);
        template.setTags(dto.getTags());
        template.setObjectifsPedagogiques(dto.getObjectifsPedagogiques());

        QuestionnaireTemplate savedTemplate = templateDao.save(template);

        // Ajouter les questions si présentes
        if (dto.getQuestions() != null && !dto.getQuestions().isEmpty()) {
            for (QuestionTemplateDto questionDto : dto.getQuestions()) {
                ajouterQuestionTemplate(savedTemplate.getId(), questionDto);
            }
        }

        return mapToResponseDto(savedTemplate);
    }

    public void ajouterQuestionTemplate(int templateId, QuestionTemplateDto dto) {
        QuestionnaireTemplate template = templateDao.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Template non trouvé"));

        QuestionTemplate question = new QuestionTemplate();
        question.setQuestion(dto.getQuestion());
        question.setOptions(dto.getOptions());
        question.setIndexReponse(dto.getIndexReponse());
        question.setOrdre(dto.getOrdre());
        question.setExplication(dto.getExplication());
        question.setQuestionnaireTemplate(template);

        questionTemplateDao.save(question);
    }

    @Transactional
    public QuestionnaireTemplateResponseDto modifierTemplate(int id, QuestionnaireTemplateDto dto) {
        QuestionnaireTemplate template = templateDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Template non trouvé"));

        template.setTitre(dto.getTitre());
        template.setDescription(dto.getDescription());
        template.setType(dto.getType());
        template.setDomaine(dto.getDomaine());
        template.setSecteur(dto.getSecteur());
        template.setDureeEstimee(dto.getDureeEstimee());
        template.setEstActif(dto.isEstActif());
        template.setTags(dto.getTags());
        template.setObjectifsPedagogiques(dto.getObjectifsPedagogiques());

        return mapToResponseDto(templateDao.save(template));
    }

    public void desactiverTemplate(int id) {
        QuestionnaireTemplate template = templateDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Template non trouvé"));

        template.setEstActif(false);
        templateDao.save(template);
    }

    @Transactional
    public void supprimerTemplate(int id) {
        QuestionnaireTemplate template = templateDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Template non trouvé"));

        templateDao.delete(template);
    }

    // ===== UTILISATION DES TEMPLATES =====

    @Transactional
    public QuestionnaireDetailResponseDto utiliserTemplate(int templateId, int projetId, int createurId) {
        QuestionnaireTemplate template = templateDao.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Template non trouvé"));

        if (!template.isEstActif()) {
            throw new RuntimeException("Ce template n'est plus disponible");
        }

        Projet projet = projetDao.findById(projetId)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));

        Utilisateur createur = utilisateurDao.findById(createurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Créer un nouveau questionnaire basé sur le template
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setTitre(template.getTitre() + " - " + projet.getTitre());
        questionnaire.setDescription(template.getDescription());
        questionnaire.setType(template.getType());
        questionnaire.setDureeEstimee(template.getDureeEstimee());
        questionnaire.setDateCreation(LocalDate.now());
        questionnaire.setUtilisateur(createur);
        questionnaire.setProjet(projet);

        Questionnaire savedQuestionnaire = questionnaireDao.save(questionnaire);

        // Copier toutes les questions du template
        List<QuestionTemplate> questionsTemplate = questionTemplateDao
                .findByQuestionnaireTemplateOrderByOrdre(template);

        for (QuestionTemplate questionTemplate : questionsTemplate) {
            QuestionsQuestionnaire question = new QuestionsQuestionnaire();
            question.setQuestion(questionTemplate.getQuestion());
            question.setOptions(questionTemplate.getOptions());
            question.setIndexReponse(questionTemplate.getIndexReponse());
            question.setQuestionnaire(savedQuestionnaire);

            questionDao.save(question);
        }

        // Incrémenter le compteur d'utilisation
        template.setNombreUtilisations(template.getNombreUtilisations() + 1);
        templateDao.save(template);

        return mapQuestionnaireToDetailResponseDto(savedQuestionnaire);
    }

    // ===== MAPPING METHODS =====

    private QuestionnaireTemplateResponseDto mapToResponseDto(QuestionnaireTemplate template) {
        QuestionnaireTemplateResponseDto dto = new QuestionnaireTemplateResponseDto();

        dto.setId(template.getId());
        dto.setTitre(template.getTitre());
        dto.setDescription(template.getDescription());
        dto.setType(template.getType());
        dto.setDomaine(template.getDomaine());
        dto.setSecteur(template.getSecteur());
        dto.setDureeEstimee(template.getDureeEstimee());
        dto.setDateCreation(template.getDateCreation());
        dto.setNombreQuestions(template.getQuestions().size());
        dto.setNombreUtilisations(template.getNombreUtilisations());
        dto.setEstActif(template.isEstActif());
        dto.setTags(template.getTags());
        dto.setObjectifsPedagogiques(template.getObjectifsPedagogiques());

        // Informations créateur
        if (template.getCreateur() != null) {
            Utilisateur createur = template.getCreateur();
            dto.setCreateurId(createur.getId());

            if (createur instanceof Contributeur) {
                Contributeur contrib = (Contributeur) createur;
                dto.setCreateurNom(contrib.getNom());
                dto.setCreateurPrenom(contrib.getPrenom());
                dto.setCreateurType("CONTRIBUTEUR");
            } else if (createur instanceof Administrateur) {
                Administrateur admin = (Administrateur) createur;
                dto.setCreateurNom(null);
                dto.setCreateurPrenom(null);
                dto.setCreateurType("ADMINISTRATEUR");
            }
        }

        return dto;
    }

    private QuestionnaireTemplateResponseDto mapToDetailResponseDto(QuestionnaireTemplate template) {
        QuestionnaireTemplateResponseDto dto = mapToResponseDto(template);

        // Ajouter les questions
        List<QuestionTemplateDto> questionsDto = questionTemplateDao
                .findByQuestionnaireTemplateOrderByOrdre(template)
                .stream()
                .map(this::mapQuestionToDto)
                .collect(Collectors.toList());

        dto.setQuestions(questionsDto);
        return dto;
    }

    private QuestionTemplateDto mapQuestionToDto(QuestionTemplate question) {
        QuestionTemplateDto dto = new QuestionTemplateDto();
        dto.setId(question.getId());
        dto.setQuestion(question.getQuestion());
        dto.setOptions(question.getOptions());
        dto.setIndexReponse(question.getIndexReponse());
        dto.setOrdre(question.getOrdre());
        dto.setExplication(question.getExplication());
        dto.setQuestionnaireTemplateId(question.getQuestionnaireTemplate().getId());
        return dto;
    }

    private QuestionnaireDetailResponseDto mapQuestionnaireToDetailResponseDto(Questionnaire questionnaire) {
        QuestionnaireDetailResponseDto dto = new QuestionnaireDetailResponseDto();

        dto.setId(questionnaire.getId());
        dto.setTitre(questionnaire.getTitre());
        dto.setDescription(questionnaire.getDescription());
        dto.setType(questionnaire.getType());
        dto.setDureeEstimee(questionnaire.getDureeEstimee());
        dto.setDateCreation(questionnaire.getDateCreation());
        dto.setNombreQuestions(questionnaire.getQuestions().size());

        // Informations créateur
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

        // Informations projet
        Projet projet = questionnaire.getProjet();
        if (projet != null) {
            dto.setProjetId(projet.getId());
            dto.setProjetTitre(projet.getTitre());
            dto.setProjetDescription(projet.getDescription());
        }

        return dto;
    }
}
