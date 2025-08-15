package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.QuestionnaireTemplate;
import odk.groupe4.ApiCollabDev.models.enums.ProjectDomain;
import odk.groupe4.ApiCollabDev.models.enums.ProjectSector;
import odk.groupe4.ApiCollabDev.models.enums.TypeQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionnaireTemplateDao extends JpaRepository<QuestionnaireTemplate, Integer> {

    List<QuestionnaireTemplate> findByEstActifTrue();

    List<QuestionnaireTemplate> findByTypeAndEstActifTrue(TypeQuiz type);

    List<QuestionnaireTemplate> findByDomaineAndEstActifTrue(ProjectDomain domaine);

    List<QuestionnaireTemplate> findBySecteurAndEstActifTrue(ProjectSector secteur);

    List<QuestionnaireTemplate> findByTypeAndDomaineAndEstActifTrue(TypeQuiz type, ProjectDomain domaine);

    List<QuestionnaireTemplate> findByTypeAndSecteurAndEstActifTrue(TypeQuiz type, ProjectSector secteur);

    List<QuestionnaireTemplate> findByDomaineAndSecteurAndEstActifTrue(ProjectDomain domaine, ProjectSector secteur);

    List<QuestionnaireTemplate> findByTypeAndDomaineAndSecteurAndEstActifTrue(
            TypeQuiz type, ProjectDomain domaine, ProjectSector secteur);

    @Query("SELECT qt FROM QuestionnaireTemplate qt WHERE qt.estActif = true " +
            "AND (LOWER(qt.titre) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(qt.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(qt.tags) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<QuestionnaireTemplate> findByKeywordAndEstActifTrue(@Param("keyword") String keyword);

    List<QuestionnaireTemplate> findByEstActifTrueOrderByNombreUtilisationsDesc();

    List<QuestionnaireTemplate> findByEstActifTrueOrderByDateCreationDesc();
}
