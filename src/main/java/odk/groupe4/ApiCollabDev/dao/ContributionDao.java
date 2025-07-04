package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.Contribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContributionDao extends JpaRepository<Contribution, Integer> {
    @Query("SELECT c FROM Contribution c WHERE c.participant.contributeur.id = :idUtilisateur")
    List<Contribution> findByUserId(@Param("idUtilisateur") Integer idUtilisateur);
}
