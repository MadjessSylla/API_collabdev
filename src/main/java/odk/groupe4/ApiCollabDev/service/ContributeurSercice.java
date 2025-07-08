package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.AdministrateurDao;
import odk.groupe4.ApiCollabDev.dao.ContributeurDao;
import odk.groupe4.ApiCollabDev.dao.ProjetDao;
import odk.groupe4.ApiCollabDev.dto.AdministrateurDto;
import odk.groupe4.ApiCollabDev.dto.ContributeurDto;
import odk.groupe4.ApiCollabDev.dto.ProjetDto;
import odk.groupe4.ApiCollabDev.models.Administrateur;
import odk.groupe4.ApiCollabDev.models.Contributeur;
import odk.groupe4.ApiCollabDev.models.Projet;
import odk.groupe4.ApiCollabDev.models.enums.StatusProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContributeurSercice {
    @Autowired
    private ContributeurDao contributeurDao;
    @Autowired
    private ProjetDao projetDao;
    @Autowired
    private AdministrateurDao administrateurDao;





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

    // Méthode pour Sélectionner un gestionnaire de projet



}
