package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.Contributeur;
import odk.groupe4.ApiCollabDev.models.Participant;
import odk.groupe4.ApiCollabDev.models.Projet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantDao extends JpaRepository<Participant, Integer>  {
    boolean existsByProjetAndContributeur(Projet projet, Contributeur contributeur);

    List<Participant> findByProjetId(int projetId);
}
