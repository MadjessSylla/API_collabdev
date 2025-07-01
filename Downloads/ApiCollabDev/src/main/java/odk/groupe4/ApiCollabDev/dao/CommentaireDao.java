package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.Commentaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentaireDao extends JpaRepository<Commentaire, Integer> {
}
