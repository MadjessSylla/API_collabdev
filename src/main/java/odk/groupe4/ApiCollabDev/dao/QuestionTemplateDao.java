package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.QuestionTemplate;
import odk.groupe4.ApiCollabDev.models.QuestionnaireTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionTemplateDao extends JpaRepository<QuestionTemplate, Integer> {
    List<QuestionTemplate> findByQuestionnaireTemplateOrderByOrdre(QuestionnaireTemplate template);

    void deleteByQuestionnaireTemplate(QuestionnaireTemplate template);
}
