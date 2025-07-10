package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.AdministrateurDao;
import odk.groupe4.ApiCollabDev.dao.BadgeDao;
import odk.groupe4.ApiCollabDev.dto.BadgeCoinDescDto;
import odk.groupe4.ApiCollabDev.dto.BadgeDto;
import odk.groupe4.ApiCollabDev.models.Administrateur;
import odk.groupe4.ApiCollabDev.models.Badge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BadgeService {
    private final BadgeDao badgeDao;
    private final AdministrateurDao administrateurDao;

    @Autowired
    public BadgeService(BadgeDao badgeDao, AdministrateurDao administrateurDao) {
        this.badgeDao = badgeDao;
        this.administrateurDao = administrateurDao;
    }

    /**
     * Crée un nouveau badge à partir des données fournies dans le DTO.
     *
     * @param dto Le DTO contenant les informations du badge à créer.
     * @return Le badge créé.
     */
    public Badge creerBadge(BadgeDto dto, int idAdmin){
        // Vérification de l'existence de l'administrateur
        Administrateur admin = administrateurDao.findById(idAdmin)
                .orElseThrow(() -> new RuntimeException("Administrateur non trouvé avec l'id: " + idAdmin));
        // Création d'un nouvel objet Badge à partir du DTO
        Badge badge = new Badge();
        // Remplissage des champs du badge avec les valeurs du DTO
        badge.setType(dto.getType());
        badge.setDescription(dto.getDescription());
        badge.setNombreContribution(dto.getNombreContribution());
        badge.setCoin_recompense(dto.getCoin_recompense());
        badge.setCreateur(admin); // Association du badge à l'administrateur
        // Enregistrement du badge dans la base de données
        return badgeDao.save(badge);
    }

    /**
     * Récupère un badge par son identifiant.
     *
     * @param idBadge L'identifiant du badge à récupérer.
     * @return Le badge correspondant à l'identifiant fourni.
     * @throws RuntimeException Si le badge n'est pas trouvé.
     */
    public Badge obtenirBadgeParId(int idBadge) {
        // Vérification de l'existence du badge avant de le récupérer
        return badgeDao.findById(idBadge)
                .orElseThrow( () -> new RuntimeException("Badge non trouvé avec l'id: " + idBadge));
    }

    /**
     * Récupère tous les badges existants.
     *
     * @return Une liste de tous les badges.
     */
    public List<Badge> obtenirTousLesBadges() {
        return badgeDao.findAll();
    }
    /**
     * Met à jour un badge existant avec les données fournies dans le DTO.
     *
     * @param idBadge L'identifiant du badge à mettre à jour.
     * @param dto Le DTO contenant les nouvelles informations du badge.
     * @return Le badge mis à jour.
     */
    public Badge mettreAJourBagde(int idBadge, BadgeDto dto) {
        // Récupération du badge existant
        Badge badge = obtenirBadgeParId(idBadge);
        // Mise à jour des champs du badge avec les valeurs du DTO
        badge.setType(dto.getType());
        badge.setDescription(dto.getDescription());
        badge.setNombreContribution(dto.getNombreContribution());
        badge.setCoin_recompense(dto.getCoin_recompense());
        // Enregistrement des modifications
        return badgeDao.save(badge);
    }

    /**
     * Met à jour les champs coin_recompense et description d'un badge existant.
     *
     * @param idBadge L'identifiant du badge à mettre à jour.
     * @param dto Le DTO contenant les nouvelles valeurs pour coin_recompense et description.
     * @return Le badge mis à jour.
     */
    public Badge mettreAJourCoinEtDescription(int idBadge, BadgeCoinDescDto dto) {
        // Récupération du badge existant
        Badge badge = obtenirBadgeParId(idBadge);
        // Mise à jour des champs coin_recompense et description
        badge.setCoin_recompense(dto.getCoin_recompense());
        badge.setDescription(dto.getDescription());
        // Enregistrement des modifications
        return badgeDao.save(badge);
    }

    /**
     * Supprime un badge par son identifiant.
     *
     * @param idBadge L'identifiant du badge à supprimer.
     * @return Un message de confirmation de la suppression.
     * @throws RuntimeException Si le badge n'est pas trouvé.
     */
    public String supprimerBadge(int idBadge) {
        // Vérification de l'existence du badge avant la suppression
        if (!badgeDao.existsById(idBadge)) {
            throw new RuntimeException("Badge non trouvé avec l'id: " + idBadge);
        }
        // Suppression du badge
        badgeDao.deleteById(idBadge);
        System.out.println("Badge avec l'id " + idBadge + " a été supprimé avec succès.");
        return "Badge avec l'id " + idBadge + " a été supprimé avec succès.";
    }
}
