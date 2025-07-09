package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
//L'interface permettent d'interagir avec la table user dans la DB
@Repository

public interface UtilisateurDao extends JpaRepository<Utilisateur, Integer> {
    Optional<Utilisateur> findByEmail(String email);
}