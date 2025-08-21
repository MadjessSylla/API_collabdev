package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.Commentaire;
import odk.groupe4.ApiCollabDev.models.Participant;
import odk.groupe4.ApiCollabDev.models.Projet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CommentaireDao extends JpaRepository<Commentaire, Integer> {
    List<Commentaire> findByAuteurAndCommentaireParentIsNull(Participant auteur);
    List<Commentaire> findByProjetAndCommentaireParentIsNull(Projet projet);
}
