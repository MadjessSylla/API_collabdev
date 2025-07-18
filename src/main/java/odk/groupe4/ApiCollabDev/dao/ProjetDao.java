package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.Contributeur;
import odk.groupe4.ApiCollabDev.models.Projet;
import odk.groupe4.ApiCollabDev.models.enums.ProjectDomain;
import odk.groupe4.ApiCollabDev.models.enums.ProjectSector;
import odk.groupe4.ApiCollabDev.models.enums.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjetDao extends JpaRepository<Projet, Integer> {
    List<Projet> findByStatus(ProjectStatus status);
    List<Projet> findByDomaine(ProjectDomain domaine);
    List<Projet> findBySecteur(ProjectSector secteur);
    List<Projet> findByStatusAndDomaine(ProjectStatus status, ProjectDomain domaine);
    List<Projet> findByStatusAndSecteur(ProjectStatus status, ProjectSector secteur);
    List<Projet> findByStatusAndDomaineAndSecteur(ProjectStatus status, ProjectDomain domaine, ProjectSector secteur);
    List<Projet> findByCreateur(Contributeur contributeur);
}
