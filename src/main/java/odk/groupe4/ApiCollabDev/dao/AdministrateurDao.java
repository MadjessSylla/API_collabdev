package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.Administrateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Indique à Spring que c’est un composant qui accède à la base
public interface AdministrateurDao extends JpaRepository<Administrateur, Integer> {

    // Recherche d’un admin par email (utile pour vérifier s’il est actif)
    Optional<Administrateur> findByEmail(String email);
}
