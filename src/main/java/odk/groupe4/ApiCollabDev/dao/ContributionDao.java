package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.Contribution;
import odk.groupe4.ApiCollabDev.models.Participant;
import odk.groupe4.ApiCollabDev.models.enums.ContributionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContributionDao extends JpaRepository<Contribution, Integer> {
    List<Contribution> findByParticipantIdAndStatus(int participantId, ContributionStatus status);
    List<Contribution> findByParticipant(Participant participant);
    List<Contribution> findByStatus(ContributionStatus status);
    List<Contribution> findByFonctionnaliteId(int fonctionnaliteId);
    List<Contribution> findByParticipantIn(List<Participant> participants);
    List<Contribution> findByParticipantInAndStatus(List<Participant> participants, ContributionStatus status);
    @Query("SELECT COUNT(c) FROM Contribution c JOIN c.participant p WHERE p.contributeur.id = :contributeurId AND c.status = 'VALIDE'")
    int countValidatedContributionsByContributeur(@Param("contributeurId") int contributeurId);

}
