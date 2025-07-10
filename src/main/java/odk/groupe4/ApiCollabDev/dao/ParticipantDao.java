package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.Contributeur;
import odk.groupe4.ApiCollabDev.models.Participant;
import odk.groupe4.ApiCollabDev.models.Projet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantDao extends JpaRepository<Participant, Integer>  {
    //Equivalent SQL : SELECT * FROM participant WHERE projet_id = ? AND contributeur_id = ?;
    boolean existsByProjetAndContributeur(Projet projet, Contributeur contributeur);
}
