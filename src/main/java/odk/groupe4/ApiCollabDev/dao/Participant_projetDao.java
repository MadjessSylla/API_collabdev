package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Participant_projetDao extends JpaRepository<Participant, Integer>  {
}
