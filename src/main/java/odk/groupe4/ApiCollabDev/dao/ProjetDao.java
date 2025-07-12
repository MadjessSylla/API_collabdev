package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.Contributeur;
import odk.groupe4.ApiCollabDev.models.Projet;
import odk.groupe4.ApiCollabDev.models.enums.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjetDao extends JpaRepository<Projet, Integer> {
    List<Projet> findByStatus(ProjectStatus status);

    List<Projet> findByCreateur(Contributeur contributeur);
}
