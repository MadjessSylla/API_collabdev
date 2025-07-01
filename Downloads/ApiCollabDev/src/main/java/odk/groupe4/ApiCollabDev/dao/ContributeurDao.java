package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.Contributeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContributeurDao extends JpaRepository<Contributeur, Integer> {
}
