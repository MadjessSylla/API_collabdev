package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionnaireDao extends JpaRepository<Questionnaire, Integer> {
    List<Questionnaire> findByProjetId(int projetId);
    List<Questionnaire> findByTemplateProjetId(int templateId);
    List<Questionnaire> findByContributeurId(int contributeurId);
}
