package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.Projet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjetDao extends JpaRepository<Projet, Integer> {
}
