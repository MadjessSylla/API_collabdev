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

    // Méthode pour changer un ContributeurDao en ContributeurDto
    private ContributeurDto ContributeurDaoToDto(Contributeur contributeur) {
        ContributeurDto contributeurDto = new ContributeurDto();
        // Conversion de l'objet Contributeur en ContributeurDto
        contributeurDto.setNom(contributeur.getNom());
        contributeurDto.setPrenom(contributeur.getPrenom());
        contributeurDto.setTelephone(contributeur.getTelephone());
        contributeurDto.setEmail(contributeur.getEmail());
        contributeurDto.setPassword(contributeur.getPassword());
        contributeurDto.setTotalCoin(contributeur.getTotalCoin());
        return contributeurDto;
    }
    // Méthode pour changer un ProjetDao en ProjetDto
    private ProjetDto ProjetDaoToDto(Projet projet) {
        ProjetDto projetDto = new ProjetDto();
        // Conversion de l'objet Projet en ProjetDto
        projetDto.setTitre(projet.getTitre());
        projetDto.setDescription(projet.getDescription());
        projetDto.setDomaine(projet.getDomaine());
        projetDto.setUrlCahierDeCharge(projet.getUrlCahierDeCharge());
        projetDto.setStatus(projet.getStatus());
        return projetDto;
    }

    // Méthode pour proposer un projet
    public ProjetDto proposerProjet (ProjetDto projetDto) {
        Projet projet = new Projet();
        StatusProject status = StatusProject.EN_ATTENTE; // Initialisation du statut du projet à EN_ATTENTE
        // Conversion de l'objet ProjetDto en Projet
        projet.setTitre(projetDto.getTitre());
        projet.setDescription(projetDto.getDescription());
        projet.setDomaine(projetDto.getDomaine());
        projet.setUrlCahierDeCharge(projetDto.getUrlCahierDeCharge());
        projet.setStatus(status); // Initialisation du statut du projet à EN_ATTENTE
        // Enregistrement de l'objet Projet
        return ProjetDaoToDto(projetDao.save(projet));
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
    // suivre l'avancement d'un projet
    public StatusProject suivreAvancementProjet(int idProjet) {
        ProjetDto projetDto = new ProjetDto();
        Projet projet = projetDao.findById(idProjet).orElse(null);
        if (projet != null) {
            projetDto.setStatus(projet.getStatus());
            return projetDto.getStatus();
        }
        return null;
    }
    // Méthode pour Sélectionner un gestionnaire de projet



}
