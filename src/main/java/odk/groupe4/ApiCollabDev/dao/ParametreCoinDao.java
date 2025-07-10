package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.dto.ParametreCoinDto;
import odk.groupe4.ApiCollabDev.models.ParametreCoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParametreCoinDao extends JpaRepository<ParametreCoin, Integer> {

    // Méthode pour obtenir tous les paramètres de coins triés par ID ascendant
    @Query("SELECT new odk.groupe4.ApiCollabDev.dto.ParametreCoinDto" +
            "(p.nom, p.description, p.typeEvenementLien, p.valeur) " +
            "FROM ParametreCoin p ORDER BY p.id ASC")
    List<ParametreCoinDto> findAllByOrderByIdAsc();

    // Méthode pour trouver un ParametreCoin par son typeEvenementLien
    // SQL: SELECT * FROM parametre_coin WHERE type_evenement_lien = ?1
    Optional<ParametreCoin> findByTypeEvenementLien(String typeEvenementLien);

}
