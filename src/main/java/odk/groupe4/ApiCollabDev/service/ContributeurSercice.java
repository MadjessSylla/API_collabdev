package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.AdministrateurDao;
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
    private final ContributeurDao contributeurDao;

    @Autowired
    public ContributeurSercice (ContributeurDao contributeurDao) {
        this.contributeurDao = contributeurDao;
    }

    /** Afficher le solde d'un contributeur
     * @param id l'ID du contributeur
     * @return un objet ContributeurSoldeDto contenant le solde du contributeur
     * @throws RuntimeException si le contributeur n'existe pas
     */
    public ContributeurSoldeDto afficherSoldeContributeur(int id) {
        // Vérification de l'existence du contributeur
        if (!contributeurDao.existsById(id)) {
            throw new RuntimeException("Contributeur non trouvé avec l'ID: " + id);
        }
        // Récupération du contributeur
        return contributeurDao.totalCoinContributeur(id);
    }
}
