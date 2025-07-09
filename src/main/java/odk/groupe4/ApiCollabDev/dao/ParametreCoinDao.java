package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.ParametreCoin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParametreCoinDao extends JpaRepository<ParametreCoin, Integer> {
      Optional<ParametreCoin> findByTypeEvenementLien(String typeEvent);
}
