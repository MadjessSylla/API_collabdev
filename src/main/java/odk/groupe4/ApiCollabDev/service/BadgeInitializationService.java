package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.AdministrateurDao;
import odk.groupe4.ApiCollabDev.dao.BadgeDao;
import odk.groupe4.ApiCollabDev.models.Administrateur;
import odk.groupe4.ApiCollabDev.models.Badge;
import odk.groupe4.ApiCollabDev.models.enums.TypeBadge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BadgeInitializationService {

    private final BadgeDao badgeDao;
    private final AdministrateurDao administrateurDao;

    @Autowired
    public BadgeInitializationService(BadgeDao badgeDao, AdministrateurDao administrateurDao) {
        this.badgeDao = badgeDao;
        this.administrateurDao = administrateurDao;
    }

    /**
     * Méthode pour réinitialiser ou mettre à jour les badges par défaut
     */
    public void reinitialiserBadges() {
        Administrateur adminSysteme = obtenirOuCreerAdminSysteme();

        // Supprimer les anciens badges système (optionnel)
        // badgeDao.deleteByCreateur(adminSysteme);

        // Recréer les badges avec les nouveaux paramètres
        creerOuMettreAJourBadge(TypeBadge.DEBUTANT, "Badge attribué pour la première contribution validée", 1, 10, adminSysteme);
        creerOuMettreAJourBadge(TypeBadge.BRONZE, "Badge Bronze attribué après 5 contributions validées", 5, 25, adminSysteme);
        creerOuMettreAJourBadge(TypeBadge.ARGENT, "Badge Argent attribué après 10 contributions validées", 10, 50, adminSysteme);
        creerOuMettreAJourBadge(TypeBadge.OR, "Badge Or attribué après 20 contributions validées", 20, 100, adminSysteme);
        creerOuMettreAJourBadge(TypeBadge.PLATINE, "Badge Platine attribué après 50 contributions validées", 50, 200, adminSysteme);
    }

    /**
     * Récupère tous les seuils de badges configurés dans la base de données
     */
    public List<Integer> getSeuilsBadges() {
        return badgeDao.findAllOrderByNombreContributionAsc()
                .stream()
                .map(Badge::getNombreContribution)
                .distinct()
                .sorted()
                .toList();
    }

    /**
     * Récupère les badges éligibles pour un nombre de contributions donné
     */
    public List<Badge> getBadgesEligibles(int nombreContributions) {
        return badgeDao.findByNombreContributionLessThanEqualOrderByNombreContributionDesc(nombreContributions);
    }

    private void creerOuMettreAJourBadge(TypeBadge type, String description, int nombreContribution, int coinRecompense, Administrateur createur) {
        Optional<Badge> badgeExistant = badgeDao.findByTypeAndNombreContribution(type, nombreContribution);

        if (badgeExistant.isEmpty()) {
            Badge badge = new Badge();
            badge.setType(type);
            badge.setDescription(description);
            badge.setNombreContribution(nombreContribution);
            badge.setCoin_recompense(coinRecompense);
            badge.setCreateur(createur);
            badgeDao.save(badge);
        } else {
            // Mettre à jour le badge existant si nécessaire
            Badge badge = badgeExistant.get();
            badge.setDescription(description);
            badge.setCoin_recompense(coinRecompense);
            badgeDao.save(badge);
        }
    }

    private Administrateur obtenirOuCreerAdminSysteme() {
        Optional<Administrateur> adminOpt = administrateurDao.findByEmail("system@collabdev.com");

        if (adminOpt.isPresent()) {
            return adminOpt.get();
        } else {
            return administrateurDao.findAll().stream().findFirst()
                    .orElseGet(() -> {
                        Administrateur adminSysteme = new Administrateur();
                        adminSysteme.setEmail("system@collabdev.com");
                        adminSysteme.setPassword("system123");
                        adminSysteme.setActif(true);
                        return administrateurDao.save(adminSysteme);
                    });
        }
    }
}
