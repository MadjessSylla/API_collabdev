package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.Badge_participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Badge_participantDao extends JpaRepository<Badge_participant, Integer> {
}
