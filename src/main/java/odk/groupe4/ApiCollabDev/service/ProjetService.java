package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.AdministrateurDao;
import odk.groupe4.ApiCollabDev.dao.ParticipantDao;
import odk.groupe4.ApiCollabDev.dao.ProjetDao;
import odk.groupe4.ApiCollabDev.dto.ContributeurDto;
import odk.groupe4.ApiCollabDev.dto.ProjetDto;
import odk.groupe4.ApiCollabDev.models.Contributeur;
import odk.groupe4.ApiCollabDev.models.Participant;
import odk.groupe4.ApiCollabDev.models.Projet;
import odk.groupe4.ApiCollabDev.models.enums.ParticipantProfil;
import odk.groupe4.ApiCollabDev.dao.ContributeurDao;
import odk.groupe4.ApiCollabDev.dto.ProjetCahierDto;
import odk.groupe4.ApiCollabDev.models.Administrateur;
import odk.groupe4.ApiCollabDev.models.enums.ProjectLevel;
import odk.groupe4.ApiCollabDev.models.enums.ProjectStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjetService {

    private final AdministrateurDao administrateurDao;
    private final ContributeurDao contributeurDao;
    private final NotificationService notificationService;
    private final ParticipantDao participantDao;
    private final ProjetDao projetDao;

    // Constructeur pour initialiser les dépendances
    @Autowired
    private ProjetService (ProjetDao projetDao,
                           NotificationService notificationService,
                           AdministrateurDao administrateurDao,
                           ParticipantDao participantDao,
                           ContributeurDao contributeurDao) {
        this.projetDao = projetDao;
        this.notificationService = notificationService;
        this.administrateurDao = administrateurDao;
        this.participantDao = participantDao;
        this.contributeurDao = contributeurDao;
    }


    /**Méthode pour proposer un projet
     * Cette méthode permet à un contributeur de proposer un projet en remplissant un formulaire.
     * Elle enregistre le projet dans la base de données et notifie les administrateurs.
     * @param projetDto : DTO contenant les informations du projet
     * @param idCreateurProjet : ID du contributeur qui crée le projet
     * @return Projet : l'objet Projet créé et enregistré dans la base de données
     */
    public Projet proposerProjet(ProjetDto projetDto, int idCreateurProjet){
        // Récupération du contributeur dans la base de données par son ID
        Contributeur contributeur= contributeurDao.findById(idCreateurProjet)
                .orElseThrow(()-> new RuntimeException("contributeur introuvable"));
        // Initialisation d'un nouvel objet Projet
        Projet projet = new Projet();
        // On récupère les données du projet à partir de l'objet ProjetDto
        projet.setTitre( projetDto.getTitre()); // Titre du projet
        projet.setDescription(projetDto.getDescription()); // Description du projet
        projet.setDomaine(projetDto.getDomaine()); // Domaine informatique du projet
        projet.setSecteur(projetDto.getSecteur()); // Secteur d'activité du projet
        projet.setUrlCahierDeCharge(projetDto.getUrlCahierDeCharge()); // Lien vers le cahier de charge
        projet.setStatus(ProjectStatus.EN_ATTENTE); // Le projet est initialement en attente
        projet.setContributeur(contributeur); // Créateur du projet

        // Vérifier si le projet soumis est en attente
        if (projet.getStatus() == ProjectStatus.EN_ATTENTE) {
            // Récupérer tous les administrateurs
            administrateurDao.findAll().forEach(administrateur -> {
                notificationService.createNotification(
                        administrateur, // Administrateur hérite d'Utilisateur
                        "Nouvelle idée de projet soumise",
                        "Un nouveau projet '" + projet.getTitre() + "' a été soumis par " +
                                projet.getCreateur().getNom() + " pour validation."
                );
            });
        }
        return projetDao.save(projet);
    }
    // M&thode permettant de selectionner un participant de type Gestionnaire

    public void selectGestionnaire(int idProjet, int idContributeur) {
        // Récupération du projet dans la base de données par son ID
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'ID: " + idProjet));

        // Vérification qu'il existe au moins un participant de type Ideateur de ce projet
        boolean hasIdeateur = projet.getParticipants().stream()
                .anyMatch(participant -> participant.getProfil().equals(ParticipantProfil.PORTEUR_DE_PROJET));

        if (!hasIdeateur) {
            throw new RuntimeException("Aucun participant de type Porteur de Projet trouvé pour le projet ID: " + idProjet);
        }

        // Vérifier si le projet a déjà un gestionnaire
        boolean hasGestionnaire = projet.getParticipants().stream()
                .anyMatch(participant -> participant.getProfil().equals(ParticipantProfil.GESTIONNAIRE));

        if (hasGestionnaire) {
            throw new RuntimeException("Le projet ID: " + idProjet + " a déjà un gestionnaire.");
        }

        // S'assurer que le participant existe et qu'il a un profil de type Gestionnaire
        Participant gestionnaire = participantDao.findById(idContributeur)
                .orElseThrow(() -> new RuntimeException("Participant non trouvé avec l'ID: " + idContributeur));
        if (!gestionnaire.getProfil().equals(ParticipantProfil.GESTIONNAIRE)) {
            throw new RuntimeException("Le participant ID: " + idContributeur + " n'est pas un gestionnaire.");
        }

        // Ajouter le participant gestionnaire au projet
        projet.getParticipants().add(gestionnaire);

        // Enregistrer les modifications du projet
        projetDao.save(projet);
    }


    /** Méthode pour valider un projet
     * Cette méthode permet à un administrateur de valider un projet proposé par un contributeur.
     * Elle met à jour le statut du projet et notifie le créateur du projet.
     * @param idProjet : ID du projet à valider
     * @param idUserValide : ID de l'administrateur qui valide le projet
     * @return Projet : l'objet Projet mis à jour et enregistré dans la base de données
     */
    public Projet validerProjet(int idProjet, int idUserValide) {
        // On va récuperer l'objet projet dans la base de données a partir de idProjet
        Projet p = projetDao.findById(idProjet)
              .orElseThrow(() -> new RuntimeException("projet introuvable"));

        // On va recuperer l'admin qui a validé le projet dans la bd à partir de idUserValide
        Administrateur admin= administrateurDao.findById(idUserValide)
              .orElseThrow(()-> new RuntimeException("admin introuvable"));

        // On affecte l'admin à un projet qui a été validé
        p.setValidateur(admin);

       // On va mettre à jour le statut du projet à OUVERT
          p.setStatus(ProjectStatus.OUVERT);
          // On va notifier le porteur du projet que le projet a été validé
            notificationService.createNotification(
                p.getCreateur(),
                "Projet validé",
                "Votre projet '" + p.getTitre() + "' a été validé par le service de validation."
            );
        // On va enregistrer le projet dans la base de données
        return projetDao.save(p);
    }

    /** Méthode pour rejeter un projet
     * Cette méthode permet à un administrateur de rejeter un projet proposé par un contributeur.
     * Elle met à jour le statut du projet et notifie le créateur du projet.
     * @param idProjet : ID du projet à rejeter
     * @param idUserValide : ID de l'administrateur qui rejette le projet
     */
    public void rejeterProjet(int idProjet, int idUserValide){
        // On va récupérer l'objet projet dans la base de données à partir de idProjet
        Projet p = projetDao.findById(idProjet)
              .orElseThrow(()-> new RuntimeException("Projet introuvable"));

        // On va récupérer l'admin qui va rejeter le projet dans la bd à partir de idUserValide
        Administrateur admin=administrateurDao.findById(idUserValide)
              .orElseThrow(()-> new RuntimeException("Admin introuvable"));

        // On va affecter l'admin qui a rejeté le projet
        p.setValidateur(admin);
        // On va mettre à jour le statut du projet à REJETÉ
        p.setStatus(ProjectStatus.REJETE);
        // On va notifier le porteur du projet que le projet a été rejeté
        notificationService.createNotification(
                p.getCreateur(),
                "Projet rejeté",
                "Votre projet '" + p.getTitre() + "' a été rejeté par le service de validation."
        );
        // On va enregistrer le projet dans la base de données
        projetDao.delete(p);
    }

    /** Méthode pour éditer le cahier de charge d'un projet
     * Cette méthode permet de mettre à jour l'URL du cahier de charge d'un projet.
     * @param projetCahierDto : DTO contenant l'URL du cahier de charge
     * @param idProjet : ID du projet à mettre à jour
     * @return Projet : l'objet Projet mis à jour et enregistré dans la base de données
     */
    public Projet editerCahierDeCharge(ProjetCahierDto projetCahierDto, int idProjet){
        // On va récupérer l'objet projet dans la base de données à partir de son id
        Projet projet= projetDao.findById(idProjet)
              .orElseThrow(()->new RuntimeException("Projet introuvable"));
        // On va mettre à jour le cahier de charge du projet
        projet.setUrlCahierDeCharge(projetCahierDto.getUrlCahierDeCharge());
        // On sauvegarde le projet mis à jour dans la base de données
        return projetDao.save(projet);
    }

    /** Méthode pour attribuer un niveau de complexité à un projet
     * Cette méthode permet à un administrateur d'attribuer un niveau de complexité à un projet.
     * Elle met à jour le niveau du projet et notifie le créateur du projet.
     * @param idProjet : ID du projet auquel le niveau est attribué
     * @param idadministrateur : ID de l'administrateur qui attribue le niveau
     * @param niveau : Niveau de complexité à attribuer au projet
     * @return Projet : l'objet Projet mis à jour et enregistré dans la base de données
     */
    public Projet attribuerNiveau (int idProjet, int idadministrateur, ProjectLevel niveau){
        // On récupère l'objet projet dans la base de données à partir de son id
        Projet projet= projetDao.findById(idProjet)
                .orElseThrow(()->new RuntimeException("Projet introuvable"));

        // On récupère l'objet admin dans la base de données à partir de son id
        Administrateur admin = administrateurDao.findById(idadministrateur)
                .orElseThrow(()->new RuntimeException("admin introuvable"));

        // On vérifie si le niveau de complexité est valide
        if (niveau == null) {
            throw new IllegalArgumentException("Niveau de complexité invalide");
        }

        // On vérifie si le projet a déjà un niveau attribué
        if (projet.getNiveau() != null) {
            throw new RuntimeException("Le projet a déjà un niveau attribué.");
        }

        // On affecte le niveau de complexité au projet
        projet.setNiveau(niveau);

        // On affecte l'admin qui a attribué le niveau de complexité au projet
        projet.setValidateur(admin);

        // On notifie le créateur du projet que le niveau de complexité a été attribué
        notificationService.createNotification(
                projet.getCreateur(),
                "Niveau de complexité attribué",
                "Le niveau de complexité '" + niveau + "' a été attribué à votre projet '" + projet.getTitre() + "'."
        );

        // On enregistre le projet mis à jour dans la base de données
        return projetDao.save(projet);
    }

    /** Méthode pour démarrer un projet
     * Cette méthode permet de démarrer un projet en le passant du statut EN_ATTENTE à OUVERT.
     * Elle notifie les participants que le projet a été démarré.
     * @param idProjet : ID du projet à démarrer
     * @return Projet : l'objet Projet mis à jour et enregistré dans la base de données
     */
    public Projet demarrerProjet(int idProjet) {
        // Récupération du projet dans la base de données par son ID
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'ID: " + idProjet));

        // Vérification que le projet est en attente
        if (projet.getStatus() != ProjectStatus.EN_ATTENTE) {
            throw new RuntimeException("Le projet doit être en attente pour démarrer.");
        }

        // Mise à jour du statut du projet à OUVERT
        projet.setStatus(ProjectStatus.OUVERT);

        // Notification aux participants que le projet a été démarré
        projet.getParticipants().forEach(participant -> {
            notificationService.createNotification(
                    participant.getContributeur(),
                    "Projet démarré",
                    "Le projet '" + projet.getTitre() + "' a été démarré."
            );
        });

        // Enregistrement des modifications du projet dans la base de données
        return projetDao.save(projet);
    }
}
