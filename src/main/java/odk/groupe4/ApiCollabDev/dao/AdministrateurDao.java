package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.Administrateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministrateurDao extends JpaRepository<Administrateur, Integer> {
}
