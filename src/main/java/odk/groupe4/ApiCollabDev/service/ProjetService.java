package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.ProjetDao;
import odk.groupe4.ApiCollabDev.dto.ProjetDto;
import odk.groupe4.ApiCollabDev.models.Projet;
import odk.groupe4.ApiCollabDev.models.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjetService {
    @Autowired
    private ProjetDao projetDao;


    public List<Projet> afficherProjetService() {
        return projetDao.findAll();
    }

    public Projet ajouterProjet(ProjetDto projet){
        Projet projet1 = new Projet();
        //
        projet1.setTitre(projet.getTitre());
        projet1.setDescription(projet.getDescription());
        projet1.setDomaine(projet.getDomaine());
        projet1.setUrlCahierDeCharge(projet.getUrlCahierDeCharge());
        projet1.setStatus(projet.getStatus());
        projet1.setNiveauProfil(projet.getNiveauProfil());
        projet1.setDateCreation(projet.getDate());
        projet1.setCreateur(projet.getCreateur());
        projet1.setAdministrateur(projet.getAdministrateur());
        //
        return projetDao.save(projet1);
    }

}
