package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.Fonctionnalite;
import odk.groupe4.ApiCollabDev.models.Participant;
import odk.groupe4.ApiCollabDev.models.enums.FeaturesStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FonctionnaliteDao extends JpaRepository<Fonctionnalite, Integer> {
    List<Fonctionnalite> findByStatusFeatures(FeaturesStatus status);
    
    List<Fonctionnalite> findByProjetId(int projetId);

    boolean existsByParticipantAndStatusFeatures(Participant participant, FeaturesStatus featuresStatus);
}
