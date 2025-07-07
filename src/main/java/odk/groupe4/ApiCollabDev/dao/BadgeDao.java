package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BadgeDao extends JpaRepository<Badge, Integer> {
    List<Badge> findByNombreContribution(int nombreContribution);
}
