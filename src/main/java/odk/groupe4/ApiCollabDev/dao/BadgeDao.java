package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.Badge;
import odk.groupe4.ApiCollabDev.models.enums.TypeBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BadgeDao extends JpaRepository<Badge, Integer> {
    List<Badge> findByNombreContribution(int nombreContribution);

    Optional<Badge> findByTypeAndNombreContribution(TypeBadge type, int nombreContribution);

    @Query("SELECT b FROM Badge b ORDER BY b.nombreContribution ASC")
    List<Badge> findAllOrderByNombreContributionAsc();

    List<Badge> findByNombreContributionLessThanEqualOrderByNombreContributionDesc(int nombreContribution);
}
