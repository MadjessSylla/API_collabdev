package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.Notification;
import odk.groupe4.ApiCollabDev.models.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationDao extends JpaRepository<Notification, Integer> {

    /**
     * Trouve toutes les notifications d'un utilisateur, triées par date de création décroissante
     */
    List<Notification> findByUtilisateurOrderByDateCreationDesc(Utilisateur utilisateur);

    /**
     * Trouve toutes les notifications d'un utilisateur avec pagination
     */
    Page<Notification> findByUtilisateurOrderByDateCreationDesc(Utilisateur utilisateur, Pageable pageable);

    /**
     * Trouve les notifications non lues d'un utilisateur
     */
    List<Notification> findByUtilisateurAndLuFalseOrderByDateCreationDesc(Utilisateur utilisateur);

    /**
     * Compte le nombre de notifications non lues d'un utilisateur
     */
    long countByUtilisateurAndLuFalse(Utilisateur utilisateur);

    /**
     * Supprime toutes les notifications d'un utilisateur
     */
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.utilisateur.id = :utilisateurId")
    void deleteAllByUtilisateurId(@Param("utilisateurId") int utilisateurId);

    /**
     * Marque toutes les notifications d'un utilisateur comme lues
     */
    @Modifying
    @Query("UPDATE Notification n SET n.lu = true WHERE n.utilisateur.id = :utilisateurId AND n.lu = false")
    void markAllAsReadByUtilisateurId(@Param("utilisateurId") int utilisateurId);
}
