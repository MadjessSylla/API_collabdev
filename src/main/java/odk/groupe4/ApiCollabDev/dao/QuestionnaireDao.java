package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionnaireDao extends JpaRepository<Questionnaire, Integer> {
}
