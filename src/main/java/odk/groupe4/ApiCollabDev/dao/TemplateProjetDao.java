package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.TemplateProjet;
import odk.groupe4.ApiCollabDev.models.enums.ProjectDomain;
import odk.groupe4.ApiCollabDev.models.enums.ProjectSector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateProjetDao extends JpaRepository<TemplateProjet, Integer> {
    List<TemplateProjet> findByActifTrue();
    List<TemplateProjet> findByDomaineAndActifTrue(ProjectDomain domaine);
    List<TemplateProjet> findBySecteurAndActifTrue(ProjectSector secteur);
    List<TemplateProjet> findByCreateurId(int createurId);
}
