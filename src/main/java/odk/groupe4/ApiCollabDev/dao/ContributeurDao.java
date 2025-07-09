package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.Contributeur;
import odk.groupe4.ApiCollabDev.models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContributeurDao extends JpaRepository<Contributeur, Integer> {
    Optional<Contributeur> findByTelephone(String telephone);
}
