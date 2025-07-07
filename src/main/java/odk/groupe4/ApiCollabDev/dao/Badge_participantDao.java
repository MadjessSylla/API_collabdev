package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.Badge_participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Badge_participantDao extends JpaRepository<Badge_participant, Integer> {
    // Méthode pour trouver un Badge_participant par participantId et badgeId
    Optional<Badge_participant> findByParticipantIdAndBadgeId(int idParticipant, int idBadge);
}
