package odk.groupe4.ApiCollabDev;

import jakarta.annotation.PostConstruct;
import odk.groupe4.ApiCollabDev.dao.ParametreCoinDao;
import odk.groupe4.ApiCollabDev.models.ParametreCoin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CoinConfigInitializer {

    private ParametreCoinDao parametreCoinDao;

    @Autowired
    public CoinConfigInitializer(ParametreCoinDao parametreCoinDao) {
        this.parametreCoinDao = parametreCoinDao;
    }

    // Méthode pour initialiser les paramètres de coins par défaut
    @PostConstruct
    public void init() {
        // Initialiation des paramètres de coins par défaut
        creerParametreCoinParDefaut("INSCRIPTION", "Coins attribuées lors de l'inscription de l'utilisateur", 100);
        creerParametreCoinParDefaut("CONTRIBUTION_VALIDEE", "Coins attribuées pour une contribution validée", 10);
        creerParametreCoinParDefaut("DEVERROUILLAGE_PROJET_INTERMEDIAIRE", "Coins réduit pour débloquer un projet intermédiaire", 20);
        creerParametreCoinParDefaut("DEVERROUILLAGE_PROJET_DIFFICILE", "Coins réduit pour débloquer un projet difficile", 50);
        creerParametreCoinParDefaut("DEVERROUILLAGE_PROJET_EXPERT", "Coins réduit pour débloquer un projet expert", 70);
    }
    // Méthode pour créer un paramètre de coin par défaut si il n'existe pas déjà
    private void creerParametreCoinParDefaut(String type, String description, int valeur) {
        if (parametreCoinDao.findByTypeEvenementLien(type).isEmpty()) {
            ParametreCoin param = new ParametreCoin();
            param.setNom(type);
            param.setDescription(description);
            param.setTypeEvenementLien(type);
            param.setValeur(valeur);
            // Note : administrateur peut être défini ultérieurement par le gestionnaire.
            parametreCoinDao.save(param);
        }
    }
}
