package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.dto.ContributeurSoldeDto;
import odk.groupe4.ApiCollabDev.models.Contributeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContributeurDao extends JpaRepository<Contributeur, Integer> {
    @Query("SELECT new odk.groupe4.ApiCollabDev.dto.ContributeurSoldeDto(c.totalCoin) " +
           "FROM Contributeur c WHERE c.id = :id")
    ContributeurSoldeDto totalCoinContributeur(int id);

    Optional<Contributeur> findByTelephone(String telephone);
    
    Optional<Contributeur> findByEmail(String email);
}
