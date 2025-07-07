package odk.groupe4.ApiCollabDev.service;


import jakarta.persistence.Entity;
import odk.groupe4.ApiCollabDev.dao.AdministrateurDao;
import odk.groupe4.ApiCollabDev.dao.ContributeurDao;
import odk.groupe4.ApiCollabDev.dao.ProjetDao;
import odk.groupe4.ApiCollabDev.dto.ProjetDto;
import odk.groupe4.ApiCollabDev.models.Administrateur;
import odk.groupe4.ApiCollabDev.models.Contributeur;
import odk.groupe4.ApiCollabDev.models.Projet;
import odk.groupe4.ApiCollabDev.models.enums.StatusProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static odk.groupe4.ApiCollabDev.models.enums.StatusProject.En_attente;
import static odk.groupe4.ApiCollabDev.models.enums.StatusProject.OUVERT;


@Service
public class ProjetService {
  @Autowired
  private ProjetDao projetDao;
  @Autowired
  private AdministrateurDao administrateurDao;
  @Autowired
  private ContributeurDao contributeurDao;

  //Methode Valider un projet
  public Projet validerProjet(int idProjet, int idUserValide) {

      Projet p = projetDao.findById(idProjet)
              .orElseThrow(() -> new RuntimeException("projet introuvable"));
   // on recupere l'objet admin dans la bd a partir de idadmin
      Administrateur admin= administrateurDao.findById(idUserValide)
              .orElseThrow(()-> new RuntimeException("admin introuvable"));

      //on affecte l'objet admin à l'objet projet
       p.setAdministrateur(admin);

       /*on change le statut du projet (ouvert), on le stocke
      dans la bd et return un objet de type projet
        */
          p.setStatus(OUVERT);
         return projetDao.save(p);
    }
    // Methode creer un projet
    public Projet proposerProjet(ProjetDto projetDto, int idPorteurProjet){
      Projet projet=new Projet();
    // on va recuperer les données qui se trouve projetDTO et les affecter à projet
       projet.setTitre( projetDto.getTitre());
       projet.setDescription(projetDto.getDescription());
       projet.setDomaine(projetDto.getDomaine());
       projet.setSecteur(projetDto.getSecteur());
       projet.setUrlCahierDeCharge(projetDto.getUrlCahierDeCharge());

       projet.setStatus(En_attente);
       // Récupération du porteur de projet dans la db

        Contributeur contributeur= contributeurDao.findById(idPorteurProjet)
                .orElseThrow(()-> new RuntimeException("contributeur introuvable"));

        projet.setContributeur(contributeur);
      return projetDao.save(projet);

    }
    //Methode pour rejeter un projet
    public void rejeterProjet(int idProjet, int idUserValide){

      Projet p = projetDao.findById(idProjet)
              .orElseThrow(()-> new RuntimeException("Projet introuvable"));
    //on va recuperer l'admin qui a rejeter le projet dans la bd a partir de idadmin
      Administrateur admin=administrateurDao.findById(idUserValide)
              .orElseThrow(()-> new RuntimeException("Admin introuvable"));

    //on affecte l'admin à un projet
        p.setAdministrateur(admin);

    // on supprime le projet de la bd
         projetDao.delete(p);
    }

}
