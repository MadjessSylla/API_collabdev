package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.QuestionsQuestionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionsQuestionnaireDao extends JpaRepository<QuestionsQuestionnaire, Integer> {
}
