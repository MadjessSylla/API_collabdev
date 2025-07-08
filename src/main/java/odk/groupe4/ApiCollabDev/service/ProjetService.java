package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.Participant_projetDao;
import odk.groupe4.ApiCollabDev.dao.ProjetDao;
import odk.groupe4.ApiCollabDev.dto.ContributeurDto;
import odk.groupe4.ApiCollabDev.dto.ProjetDto;
import odk.groupe4.ApiCollabDev.models.Contributeur;
import odk.groupe4.ApiCollabDev.models.Participant;
import odk.groupe4.ApiCollabDev.models.Projet;
import odk.groupe4.ApiCollabDev.models.enums.Profil;
import odk.groupe4.ApiCollabDev.models.enums.StatusProject;
import org.springframework.beans.factory.annotation.Autowired;

public class ProjetService {

    private final ProjetDao projetDao;
    private final Participant_projetDao participantDao;

    @Autowired
    public ProjetService(ProjetDao projetDao, Participant_projetDao participantDao) {
        this.projetDao = projetDao;
        this.participantDao = participantDao;
    }
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
        // Initialise un objet projet
        Projet projet = new Projet();
        // Affecter les données de projetDto à projet
        projet.setTitre(projetDto.getTitre());
        projet.setDescription(projetDto.getDescription());
        projet.setDomaine(projetDto.getDomaine());
        projet.setUrlCahierDeCharge(projetDto.getUrlCahierDeCharge());
        projet.setStatus(StatusProject.EN_ATTENTE); // Initialisation du statut du projet à EN_ATTENTE

        // Sauvegarde projet dans la base de données
        return ProjetDaoToDto(projetDao.save(projet));
    }
    // suivre l'avancement d'un projet
   /* public StatusProject suivreAvancementProjet(int idProjet) {
        ProjetDto projetDto = new ProjetDto();
        Projet projet = projetDao.findById(idProjet).orElse(null);
        if (projet != null) {
            projetDto.setStatus(projet.getStatus());
            return projetDto.getStatus();
        }
        return null;
    }*/

    // M&thode permettant de selectionner un participant de type Gestionnaire
    public void selectGestionnaire(int idProjet, int idContributeur) {
        // Récupération du projet dans la base de données par son ID
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'ID: " + idProjet));

        // Vérification qu'il existe au moins un participant de type Ideateur de ce projet
        boolean hasIdeateur = projet.getParticipants().stream()
                .anyMatch(participant -> participant.getProfil().equals(Profil.PORTEUR_DE_PROJET));

        if (!hasIdeateur) {
            throw new RuntimeException("Aucun participant de type Porteur de Projet trouvé pour le projet ID: " + idProjet);
        }

        // Vérifier si le projet a déjà un gestionnaire
        boolean hasGestionnaire = projet.getParticipants().stream()
                .anyMatch(participant -> participant.getProfil().equals(Profil.GESTIONNAIRE));

        if (hasGestionnaire) {
            throw new RuntimeException("Le projet ID: " + idProjet + " a déjà un gestionnaire.");
        }

        // S'assurer que le participant existe et qu'il a un profil de type Gestionnaire
        Participant gestionnaire = participantDao.findById(idContributeur)
                .orElseThrow(() -> new RuntimeException("Participant non trouvé avec l'ID: " + idContributeur));
        if (!gestionnaire.getProfil().equals(Profil.GESTIONNAIRE)) {
            throw new RuntimeException("Le participant ID: " + idContributeur + " n'est pas un gestionnaire.");
        }

        // Ajouter le participant gestionnaire au projet
        projet.getParticipants().add(gestionnaire);

        // Enregistrer les modifications du projet
        projetDao.save(projet);
    }
}
