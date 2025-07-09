package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.AdministrateurDao;
import odk.groupe4.ApiCollabDev.dao.Participant_projetDao;
import odk.groupe4.ApiCollabDev.dao.ProjetDao;
import odk.groupe4.ApiCollabDev.dto.ContributeurDto;
import odk.groupe4.ApiCollabDev.dto.ProjetDto;
import odk.groupe4.ApiCollabDev.models.Contributeur;
import odk.groupe4.ApiCollabDev.models.Participant;
import odk.groupe4.ApiCollabDev.models.Projet;
import odk.groupe4.ApiCollabDev.models.enums.Profil;
import odk.groupe4.ApiCollabDev.dao.ContributeurDao;
import odk.groupe4.ApiCollabDev.dto.ProjetCahierDto;
import odk.groupe4.ApiCollabDev.models.Administrateur;
import odk.groupe4.ApiCollabDev.models.enums.NiveauProjet;
import odk.groupe4.ApiCollabDev.models.enums.StatusProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjetService {

    private final ProjetDao projetDao;
    private final NotificationService notificationService;
    private final AdministrateurDao administrateurDao;
    private final Participant_projetDao participantDao;
    private final ContributeurDao contributeurDao;



    @Autowired
    private ProjetService (ProjetDao projetDao,
                           NotificationService notificationService,
                           AdministrateurDao administrateurDao,
                           Participant_projetDao participantDao,
                           ContributeurDao contributeurDao) {
        this.projetDao = projetDao;
        this.notificationService = notificationService;
        this.administrateurDao = administrateurDao;
        this.participantDao = participantDao;
        this.contributeurDao = contributeurDao;
    }

    public Projet createProjet(Projet projet) {
        // Sauvegarde du projet
        Projet savedProjet = projetDao.save(projet);

        // Vérifier si le projet soumis est en attente
        if (savedProjet.getStatus() == StatusProject.EN_ATTENTE) {
            // Récupérer tous les administrateurs
            administrateurDao.findAll().forEach(administrateur -> {
                notificationService.createNotification(
                        administrateur, // Administrateur hérite d'Utilisateur
                        "Nouvelle idée de projet soumise",
                        "Un nouveau projet '" + savedProjet.getTitre() + "' a été soumis par " +
                                savedProjet.getCreateur().getNom() + " pour validation."
                );
            });
        }

        return savedProjet;


    }
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

    //Methode pour mettre à jour le cahier de charge
    public Projet editerCahierDeCharge(ProjetCahierDto projetCahierDto, int idProjet){
      Projet projet= projetDao.findById(idProjet)
              .orElseThrow(()->new RuntimeException("Projet introuvable"));

      //on va mettre à jour l'URL du cahier de charge
        projet.setUrlCahierDeCharge(projetCahierDto.getUrlCahierDeCharge());

      return projetDao.save(projet);
    }

    // Methode permettant d'attribuer un niveau de complexité au projet

    public Projet attribuerNiveau (int idProjet, int idadministrateur, NiveauProjet niveau){
        // On récupère l'objet projet dans la base de donnnées à partir de son id
        Projet projet= projetDao.findById(idProjet)
                .orElseThrow(()->new RuntimeException("Projet introuvable"));

        // On récupère l'objet admin dans la base de donnnées à partir de son id
        Administrateur admin = administrateurDao.findById(idadministrateur)
                .orElseThrow(()->new RuntimeException("admin introuvable"));

        projet.setNiveauProjet(niveau); // affectation du niveau a l'objet projet

        projet.setAdministrateur(admin); // affectation de l'admin qui valide a l'objet projet

        return projetDao.save(projet); // On a enregistré dans la base de données
    }
}
