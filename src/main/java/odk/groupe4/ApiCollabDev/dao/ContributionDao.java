package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.Contribution;
import odk.groupe4.ApiCollabDev.models.enums.StatusContribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContributionDao extends JpaRepository<Contribution, Integer> {
    // Méthode pour trouver les contributions par participant et statut
    // Equivalent en SQL : SELECT * FROM contribution WHERE participant_id = ? AND status = ?
    List<Contribution> findByParticipantIdAndStatus(int participantId, StatusContribution status);
    @Query("SELECT c FROM Contribution c WHERE c.participant.contributeur.id = :idUtilisateur")
    List<Contribution> findByUserId(@Param("idUtilisateur") Integer idUtilisateur);
}
