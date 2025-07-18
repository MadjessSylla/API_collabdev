package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.BadgeParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BadgeParticipantDao extends JpaRepository<BadgeParticipant, Integer> {

    // Méthode pour trouver un Badge_participant par participantId et badgeId
    Optional<BadgeParticipant> findByParticipantIdAndBadgeId(int idParticipant, int idBadge);
}
