package odk.groupe4.ApiCollabDev;

import jakarta.annotation.PostConstruct;
import odk.groupe4.ApiCollabDev.dao.AdministrateurDao;
import odk.groupe4.ApiCollabDev.dao.ParametreCoinDao;
import odk.groupe4.ApiCollabDev.models.Administrateur;
import odk.groupe4.ApiCollabDev.models.ParametreCoin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CoinConfigInitializer {

    private final ParametreCoinDao parametreCoinDao;
    private final AdministrateurDao administrateurDao;


    @Autowired
    public CoinConfigInitializer(ParametreCoinDao parametreCoinDao, AdministrateurDao administrateurDao) {
        this.parametreCoinDao = parametreCoinDao;
        this.administrateurDao = administrateurDao;
    }

    // Méthode pour initialiser les paramètres de coins par défaut
    @PostConstruct
    public void init() {
        // Récupérer le premier administrateur ou créer un administrateur système par défaut
        Administrateur adminSysteme = obtenirOuCreerAdminSysteme();

        // Initialiation des paramètres de coins par défaut
        creerParametreCoinParDefaut(
                "INSCRIPTION",
                "Coins attribuées lors de l'inscription de l'utilisateur",
                100,
                adminSysteme);

        creerParametreCoinParDefaut(
                "CONTRIBUTION_VALIDEE",
                "Coins attribuées pour une contribution validée",
                10,
                adminSysteme);

        creerParametreCoinParDefaut(
                "DEVERROUILLAGE_PROJET_INTERMEDIAIRE",
                "Coins réduit pour débloquer un projet intermédiaire",
                20,
                adminSysteme);

        creerParametreCoinParDefaut(
                "DEVERROUILLAGE_PROJET_DIFFICILE",
                "Coins réduit pour débloquer un projet difficile",
                50,
                adminSysteme);

        creerParametreCoinParDefaut("DEVERROUILLAGE_PROJET_EXPERT",
                "Coins réduit pour débloquer un projet expert",
                70,
                adminSysteme);
    }
    // Méthode pour créer un paramètre de coin par défaut si il n'existe pas déjà
    private void creerParametreCoinParDefaut(String type, String description, int valeur, Administrateur admin) {
        if (parametreCoinDao.findByTypeEvenementLien(type).isEmpty()) {
            ParametreCoin param = new ParametreCoin();
            param.setNom(type);
            param.setDescription(description);
            param.setTypeEvenementLien(type);
            param.setValeur(valeur);
            param.setAdministrateur(admin);

            parametreCoinDao.save(param);
            System.out.println("Coin Système pour " + type + " créé avec succès (Valeur: " + valeur + " coins)");

        }else {
            System.out.println("Coin Système pour " + type + " existe déjà (Valeur: " + valeur + " coins)");
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
