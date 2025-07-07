package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.ContributeurDao;
import odk.groupe4.ApiCollabDev.dao.ParametreCoinDao;
import odk.groupe4.ApiCollabDev.dto.ContributeurDto;
import odk.groupe4.ApiCollabDev.dto.ContributeurSoldeDto;
import odk.groupe4.ApiCollabDev.models.Contributeur;
import odk.groupe4.ApiCollabDev.models.ParametreCoin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContributeurSercice {
    private ContributeurDao contributeurDao;
    private ParametreCoinDao parametreCoinDao;

    @Autowired
    public ContributeurSercice (ContributeurDao contributeurDao, ParametreCoinDao parametreCoinDao) {
        this.contributeurDao = contributeurDao;
        this.parametreCoinDao = parametreCoinDao;
    }

    public Contributeur ajouterContributeur(ContributeurDto contributeur) {

        Contributeur contrib = new Contributeur();
        // Conversion de l'objet ContributeurDAO en Contributeur
        contrib.setNom(contributeur.getNom());
        contrib.setPrenom(contributeur.getPrenom());
        contrib.setTelephone(contributeur.getTelephone());
        contrib.setEmail(contributeur.getEmail());
        contrib.setPassword(contributeur.getPassword());
        contrib.setPointExp(10); // Initialisation du point d'expérience à 10
        ParametreCoin coin = parametreCoinDao.findByTypeEvenementLien("INSCRIPTION")
                .orElseThrow(() -> new RuntimeException("Paramètre de coin pour l'inscription non trouvé"));
        contrib.setTotalCoin(contrib.getTotalCoin() + coin.getValeur()); // Ajout des coins d'inscription
        // Enregistrement de l'objet Contributeur
        return contributeurDao.save(contrib);
    }

    // Afficher le solde d'un contributeur
    public ContributeurSoldeDto afficherSoldeContributeur(int id) {
        // Vérification si le contributeur existe
        if (!contributeurDao.existsById(id)) {
            throw new RuntimeException("Contributeur non trouvé avec l'ID: " + id);
        }
        return contributeurDao.totalCoinContributeur(id);
    }
}
