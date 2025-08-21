package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.Badge;
import odk.groupe4.ApiCollabDev.models.BadgeContributeur;
import odk.groupe4.ApiCollabDev.models.Contributeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BadgeContributeurDao extends JpaRepository<BadgeContributeur, Integer> {

    // Méthode pour trouver un BadgeContributeur par contributeur et badge
    Optional<BadgeContributeur> findByContributeurAndBadge(Contributeur contributeur, Badge badge);

    // Méthode pour trouver un BadgeContributeur par contributeurId et badgeId
    Optional<BadgeContributeur> findByContributeurIdAndBadgeId(int idContributeur, int idBadge);

    // Méthode pour trouver tous les badges d'un contributeur
    List<BadgeContributeur> findByContributeur(Contributeur contributeur);

    // Méthode pour trouver tous les badges d'un contributeur par son ID
    List<BadgeContributeur> findByContributeurId(int contributeurId);

    // Compter le nombre de badges d'un contributeur
    @Query("SELECT COUNT(bc) FROM BadgeContributeur bc WHERE bc.contributeur.id = :contributeurId")
    long countByContributeurId(@Param("contributeurId") int contributeurId);
}
