package odk.groupe4.ApiCollabDev.service;


import odk.groupe4.ApiCollabDev.dao.FonctionnaliteDao;
import odk.groupe4.ApiCollabDev.dto.ContributionDto;
import odk.groupe4.ApiCollabDev.dto.FonctionnaliteDto;
import odk.groupe4.ApiCollabDev.models.Fonctionnalite;
import odk.groupe4.ApiCollabDev.models.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FonctionnaliteService {
    @Autowired
    private FonctionnaliteDao fonctionnaliteDao;


    public List<Fonctionnalite> afficherFonctionnalite() {
        return fonctionnaliteDao.findAll();
    }

    public Fonctionnalite ajouterFonctionnalite(FonctionnaliteDto fonctionnalite){

        Fonctionnalite fonctionnalite1 = new Fonctionnalite();
        fonctionnalite1.setTitre(fonctionnalite.getTitre());
        fonctionnalite1.setContenu(fonctionnalite.getContenu());
        fonctionnalite1.setStatusFeatures(fonctionnalite.getStatusFeatures());
        fonctionnalite1.setProjet(fonctionnalite.getProjet());
        return fonctionnaliteDao.save(fonctionnalite1);
    }
}
