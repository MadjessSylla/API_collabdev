package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.UtilisateurDao;
import odk.groupe4.ApiCollabDev.dto.UtilisateurDto;
import odk.groupe4.ApiCollabDev.models.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilisateurService {
    @Autowired
    private UtilisateurDao utilisateurDao;


    public List<Utilisateur> afficherUtilisateurs() {
        return utilisateurDao.findAll();
    }

    public Utilisateur ajouterUtilisateur(UtilisateurDto utilisateur){
        Utilisateur utilisateur1 = new Utilisateur();
        //
        utilisateur1.setEmail(utilisateur.getEmail());
        utilisateur1.setPassword(utilisateur.getPassword());
        //
        return utilisateurDao.save(utilisateur1);
    }

}
