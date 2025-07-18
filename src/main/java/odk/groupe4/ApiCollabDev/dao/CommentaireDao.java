package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.Commentaire;
import odk.groupe4.ApiCollabDev.models.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CommentaireDao extends JpaRepository<Commentaire, Integer> {
    // SQL: SELECT * FROM commentaire WHERE auteur_id = ?1
    Set<Commentaire> findByAuteur(Participant participant);
}
