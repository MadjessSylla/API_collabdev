package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.BadgeDao;
import odk.groupe4.ApiCollabDev.dto.BadgeCoinDescDto;
import odk.groupe4.ApiCollabDev.dto.BadgeDto;
import odk.groupe4.ApiCollabDev.models.Badge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BadgeService {

    private BadgeDao badgeDao;

    @Autowired
    public BadgeService(BadgeDao badgeDao) {
        this.badgeDao = badgeDao;
    }

    // POST
    public Badge creerBadge(BadgeDto dto){
       // Initialisation d'un nouvel objet Badge à partir du DTO
        Badge badge = new Badge();

        badge.setType(dto.getType());
        badge.setDescription(dto.getDescription());
        badge.setCoin_recompense(dto.getCoin_recompense());

        return badgeDao.save(badge);
    }

    // GET By ID
    public Badge obtenirBadgeParId(int idBadge) {
        /*Optional<Badge> badgeOpt = badgeDao.findById(idBadge);
        if (badgeOpt.isPresent()){
            return badgeOpt.get();
        } else {
            throw new RuntimeException("Badge not found with id: " + idBadge);
        }*/

        return badgeDao.findById(idBadge)
                .orElseThrow( () -> new RuntimeException("Badge not found with id: " + idBadge));
    }

    // GET All
    public List<Badge> obtenirTousLesBadges() {
        return badgeDao.findAll();
    }
    // PUT
    public Badge mettreAJourBagde(int idBadge, BadgeDto dto) {
        // Récupération du badge existant
        Badge badge = obtenirBadgeParId(idBadge);

        // Mise à jour des champs du badge avec les valeurs du DTO
        badge.setType(dto.getType());
        badge.setDescription(dto.getDescription());
        badge.setCoin_recompense(dto.getCoin_recompense());

        // Enregistrement des modifications
        return badgeDao.save(badge);
    }

    // PATCH
    public Badge mettreAJourCoinEtDescription(int idBadge, BadgeCoinDescDto dto) {
        // Récupération du badge existant
        Badge badge = obtenirBadgeParId(idBadge);

        // Mise à jour des champs coin_recompense et description
        badge.setCoin_recompense(dto.getCoin_recompense());
        badge.setDescription(dto.getDescription());

        // Enregistrement des modifications
        return badgeDao.save(badge);
    }
    // DELETE
    public String supprimerBadge(int idBadge) {
        // Vérification de l'existence du badge avant la suppression
        if (!badgeDao.existsById(idBadge)) {
            throw new RuntimeException("Badge not found with id: " + idBadge);
        }

        // Suppression du badge
        badgeDao.deleteById(idBadge);
        System.out.println("Badge avec l'id " + idBadge + " a été supprimé avec succès.");
        return "Badge avec l'id " + idBadge + " a été supprimé avec succès.";
    }
}
