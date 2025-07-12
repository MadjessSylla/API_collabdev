package odk.groupe4.ApiCollabDev;

import jakarta.annotation.PostConstruct;
import odk.groupe4.ApiCollabDev.dao.AdministrateurDao;
import odk.groupe4.ApiCollabDev.dao.BadgeDao;
import odk.groupe4.ApiCollabDev.models.Administrateur;
import odk.groupe4.ApiCollabDev.models.Badge;
import odk.groupe4.ApiCollabDev.models.enums.TypeBadge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BadgeConfigInitializer {

    private final BadgeDao badgeDao;
    private final AdministrateurDao administrateurDao;

    @Autowired
    public BadgeConfigInitializer(BadgeDao badgeDao, AdministrateurDao administrateurDao) {
        this.badgeDao = badgeDao;
        this.administrateurDao = administrateurDao;
    }

    // Méthode pour initialiser les badges par défaut
    @PostConstruct
    public void init() {
        // Récupérer le premier administrateur ou créer un administrateur système par défaut
        Administrateur adminSysteme = obtenirOuCreerAdminSysteme();

        // Initialisation des badges par défaut avec leurs seuils de contributions
        creerBadgeParDefaut(
                TypeBadge.DEBUTANT,
                "Badge attribué pour la première contribution validée",
                1,
                10,
                adminSysteme
        );

        creerBadgeParDefaut(
                TypeBadge.BRONZE,
                "Badge Bronze attribué après 5 contributions validées",
                5,
                25,
                adminSysteme
        );

        creerBadgeParDefaut(
                TypeBadge.ARGENT,
                "Badge Argent attribué après 10 contributions validées",
                10,
                50,
                adminSysteme
        );

        creerBadgeParDefaut(
                TypeBadge.OR,
                "Badge Or attribué après 20 contributions validées",
                20,
                100,
                adminSysteme
        );

        creerBadgeParDefaut(
                TypeBadge.PLATINE,
                "Badge Platine attribué après 50 contributions validées",
                50,
                200,
                adminSysteme
        );
    }

    // Méthode pour créer un badge par défaut si il n'existe pas déjà
    private void creerBadgeParDefaut(TypeBadge type, String description, int nombreContribution, int coinRecompense, Administrateur createur) {
        // Vérifier si un badge de ce type avec ce nombre de contributions existe déjà
        Optional<Badge> badgeExistant = badgeDao.findByTypeAndNombreContribution(type, nombreContribution);

        if (badgeExistant.isEmpty()) {
            Badge badge = new Badge();
            badge.setType(type);
            badge.setDescription(description);
            badge.setNombreContribution(nombreContribution);
            badge.setCoin_recompense(coinRecompense);
            badge.setCreateur(createur);

            badgeDao.save(badge);
            System.out.println("Badge " + type + " créé avec succès (seuil: " + nombreContribution + " contributions)");
        } else {
            System.out.println("Badge " + type + " existe déjà (seuil: " + nombreContribution + " contributions)");
        }
    }

    // Méthode pour obtenir ou créer un administrateur système par défaut
    private Administrateur obtenirOuCreerAdminSysteme() {
        // Essayer de récupérer le premier administrateur
        Optional<Administrateur> adminOpt = administrateurDao.findAll().stream().findFirst();

        if (adminOpt.isPresent()) {
            return adminOpt.get();
        } else {
            // Créer un administrateur système par défaut si aucun n'existe
            Administrateur adminSysteme = new Administrateur();
            adminSysteme.setEmail("system@collabdev.com");
            adminSysteme.setPassword("system123");
            adminSysteme.setActif(true);

            Administrateur savedAdmin = administrateurDao.save(adminSysteme);
            System.out.println("Administrateur système créé par défaut pour l'initialisation des badges");
            return savedAdmin;
        }
    }
}
