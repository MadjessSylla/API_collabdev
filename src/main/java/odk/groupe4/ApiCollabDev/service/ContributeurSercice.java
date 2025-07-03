package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.ContributeurDao;
import odk.groupe4.ApiCollabDev.dto.ContributeurDto;
import odk.groupe4.ApiCollabDev.models.Contributeur;
import odk.groupe4.ApiCollabDev.models.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContributeurSercice {
    @Autowired
    private ContributeurDao contributeurDao;


    public List<Contributeur> afficherContributeur() {
        return contributeurDao.findAll();
    }

    public Contributeur ajouterContributeur(ContributeurDto contributeur) {

        Contributeur contrib =new Contributeur();
        // Conversion de l'objet ContributeurDAO en Contributeur
        contrib.setNom(contributeur.getNom());
        contrib.setPrenom(contributeur.getPrenom());
        contrib.setTelephone(contributeur.getTelephone());
        contrib.setEmail(contributeur.getEmail());
        contrib.setPassword(contributeur.getPassword());
        contrib.setTotalCoin(contributeur.getTotalCoin());
        // Enregistrement de l'objet Contributeur
        return contributeurDao.save(contrib);
    }
}
