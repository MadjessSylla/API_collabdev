package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.AdministrateurDao;
import odk.groupe4.ApiCollabDev.dao.ContributeurDao;
import odk.groupe4.ApiCollabDev.dao.ParticipantDao;
import odk.groupe4.ApiCollabDev.dao.ProjetDao;
import odk.groupe4.ApiCollabDev.dto.ProjetCahierDto;
import odk.groupe4.ApiCollabDev.dto.ProjetDto;
import odk.groupe4.ApiCollabDev.dto.ProjetResponseDto;
import odk.groupe4.ApiCollabDev.models.Administrateur;
import odk.groupe4.ApiCollabDev.models.Contributeur;
import odk.groupe4.ApiCollabDev.models.Projet;
import odk.groupe4.ApiCollabDev.models.enums.ProjectDomain;
import odk.groupe4.ApiCollabDev.models.enums.ProjectLevel;
import odk.groupe4.ApiCollabDev.models.enums.ProjectSector;
import odk.groupe4.ApiCollabDev.models.enums.ProjectStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjetService {
    private final ProjetDao projetDao;
    private final AdministrateurDao administrateurDao;
    private final ContributeurDao contributeurDao;
    private final ParticipantDao participantDao;
    private final NotificationService notificationService;

    @Autowired
    public ProjetService(ProjetDao projetDao,
                         AdministrateurDao administrateurDao,
                         ContributeurDao contributeurDao,
                         ParticipantDao participantDao,
                         NotificationService notificationService) {
        this.projetDao = projetDao;
        this.administrateurDao = administrateurDao;
        this.contributeurDao = contributeurDao;
        this.participantDao = participantDao;
        this.notificationService = notificationService;
    }

    /**
     * Récupère tous les projets, filtrés par statut si spécifié.
     *
     * @param status Le statut des projets à récupérer, ou null pour tous les projets.
     * @return Une liste de ProjetResponseDto contenant les informations des projets.
     */
    public List<ProjetResponseDto> getAllProjets(ProjectStatus status) {
        // Si le statut est spécifié, on récupère les projets avec ce statut, sinon on récupère tous les projets.
        List<Projet> projets;
        if (status != null) {
            projets = projetDao.findByStatus(status);
        } else {
            projets = projetDao.findAll();
        }
        // On mappe chaque projet en ProjetResponseDto pour la réponse.
        return projets.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Récupère tous les projets créés par un contributeur spécifique.
     *
     * @param idContributeur L'ID du contributeur dont on veut récupérer les projets.
     * @return Une liste de ProjetResponseDto contenant les informations des projets du contributeur.
     */
    public List<ProjetResponseDto> getProjetsByContributeur(int idContributeur) {
        // On récupère le contributeur par son ID, ou on lance une exception si le contributeur n'existe pas.
        Contributeur contributeur = contributeurDao.findById(idContributeur)
                .orElseThrow(() -> new RuntimeException("Contributeur introuvable avec l'ID: " + idContributeur));
        // On récupère tous les projets créés par ce contributeur.
        List<Projet> projets = projetDao.findByCreateur(contributeur);
        // On mappe chaque projet en ProjetResponseDto pour la réponse.
        return projets.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    // Affiche tous les projets par filtres (Secteur et Domaine)
    public List<ProjetResponseDto> getProjetsOuverts(ProjectDomain domaine, ProjectSector secteur) {
        List<Projet> projets;

        if (domaine != null && secteur != null) {
            projets = projetDao.findByStatusAndDomaineAndSecteur(ProjectStatus.OUVERT, domaine, secteur);
        } else if (domaine != null) {
            projets = projetDao.findByStatusAndDomaine(ProjectStatus.OUVERT, domaine);
        } else if (secteur != null) {
            projets = projetDao.findByStatusAndSecteur(ProjectStatus.OUVERT, secteur);
        } else {
            projets = projetDao.findByStatus(ProjectStatus.OUVERT);
        }

        return projets.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    // Affiche tous les projets par domaine
    public List<ProjetResponseDto> getProjetsByDomaine(ProjectDomain domaine) {
        List<Projet> projets = projetDao.findByDomaine(domaine);
        return projets.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    // Affiche tous les projets par Secteur
    public List<ProjetResponseDto> getProjetsBySecteur(ProjectSector secteur) {
        List<Projet> projets = projetDao.findBySecteur(secteur);
        return projets.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Récupère un projet par son ID.
     *
     * @param id L'ID du projet à récupérer.
     * @return Un objet ProjetResponseDto contenant les informations du projet.
     */
    public ProjetResponseDto getProjetById(int id) {
        // On récupère le projet par son ID, ou on lance une exception si le projet n'existe pas.
        Projet projet = projetDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'ID: " + id));
        // On mappe le projet en ProjetResponseDto pour la réponse.
        return mapToResponseDto(projet);
    }

    /**
     * Propose un nouveau projet.
     *
     * @param projetDto Les détails du projet à proposer.
     * @param idCreateurProjet L'ID du contributeur qui crée le projet.
     * @return Un objet ProjetResponseDto contenant les informations du projet créé.
     */
    public ProjetResponseDto proposerProjet(ProjetDto projetDto, int idCreateurProjet) {
        // On récupère le contributeur par son ID, ou on lance une exception si le contributeur n'existe pas.
        Contributeur contributeur = contributeurDao.findById(idCreateurProjet)
                .orElseThrow(() -> new RuntimeException("Contributeur introuvable"));

        // On vérifie que le contributeur a suffisamment de points d'expérience pour proposer un projet.
        Projet projet = new Projet();
        // On initialise les propriétés du projet à partir du DTO.
        projet.setTitre(projetDto.getTitre());
        projet.setDescription(projetDto.getDescription());
        projet.setDomaine(projetDto.getDomaine());
        projet.setSecteur(projetDto.getSecteur());
        projet.setUrlCahierDeCharge(projetDto.getUrlCahierDeCharge());
        projet.setStatus(ProjectStatus.EN_ATTENTE);
        projet.setCreateur(contributeur);
        projet.setDateCreation(LocalDate.now());

        // On sauvegarde le projet dans la base de données.
        Projet savedProjet = projetDao.save(projet);

        // Notifier tous les administrateurs
        administrateurDao.findAll().forEach(administrateur -> {
            notificationService.createNotification(
                    administrateur,
                    "Nouvelle idée de projet soumise",
                    "Un nouveau projet '" + projet.getTitre() + "' a été soumis par " +
                            projet.getCreateur().getNom() + " pour validation."
            );
        });

        // On mappe le projet sauvegardé en ProjetResponseDto pour la réponse.
        return mapToResponseDto(savedProjet);
    }

    /**
     * Valide un projet proposé par un contributeur.
     *
     * @param idProjet L'ID du projet à valider.
     * @param idUserValide L'ID de l'administrateur qui valide le projet.
     * @return Un objet ProjetResponseDto contenant les informations du projet validé.
     */
    public ProjetResponseDto validerProjet(int idProjet, int idUserValide) {
        // On récupère le projet par son ID, ou on lance une exception si le projet n'existe pas.
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet introuvable"));

        // On vérifie que le projet est en attente de validation.
        if (projet.getStatus() != ProjectStatus.EN_ATTENTE) {
            throw new RuntimeException("Le projet doit être en attente de validation.");
        }

        // On récupère l'administrateur par son ID, ou on lance une exception si l'administrateur n'existe pas.
        Administrateur admin = administrateurDao.findById(idUserValide)
                .orElseThrow(() -> new RuntimeException("Administrateur introuvable"));

        // On met à jour le projet avec le validateur et le statut validé.
        projet.setValidateur(admin);
        projet.setStatus(ProjectStatus.OUVERT);

        // On notifie le créateur du projet que son projet a été validé.
        notificationService.createNotification(
                projet.getCreateur(),
                "Projet validé",
                "Votre projet '" + projet.getTitre() + "' a été validé par le service de validation."
        );

        // On sauvegarde le projet mis à jour dans la base de données.
        Projet savedProjet = projetDao.save(projet);
        // On retourne le projet validé en ProjetResponseDto.
        return mapToResponseDto(savedProjet);
    }

    /**
     * Rejette un projet proposé par un contributeur.
     *
     * @param idProjet L'ID du projet à rejeter.
     * @param idUserValide L'ID de l'administrateur qui rejette le projet.
     */
    public void rejeterProjet(int idProjet, int idUserValide) {
        // On récupère le projet par son ID, ou on lance une exception si le projet n'existe pas.
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet introuvable"));

        // On vérifie que le projet est en attente de validation.
        if (projet.getStatus() != ProjectStatus.EN_ATTENTE) {
            throw new RuntimeException("Le projet doit être en attente de validation.");
        }
        // On récupère l'administrateur par son ID, ou on lance une exception si l'administrateur n'existe pas.
        Administrateur admin = administrateurDao.findById(idUserValide)
                .orElseThrow(() -> new RuntimeException("Administrateur introuvable"));

        // On met à jour le projet avec le validateur et le statut rejeté.
        projet.setValidateur(admin);
        projet.setStatus(ProjectStatus.REJETE);

        // On notifie le créateur du projet que son projet a été rejeté.
        notificationService.createNotification(
                projet.getCreateur(),
                "Projet rejeté",
                "Votre projet '" + projet.getTitre() + "' a été rejeté par le service de validation."
        );

        // On sauvegarde le projet mis à jour dans la base de données.
        projetDao.delete(projet);
    }

    /**
     * Met à jour le cahier des charges d'un projet.
     *
     * @param projetCahierDto Les détails du cahier des charges à mettre à jour.
     * @param idProjet L'ID du projet dont le cahier des charges doit être mis à jour.
     * @return Un objet ProjetResponseDto contenant les informations du projet mis à jour.
     */
    public ProjetResponseDto editerCahierDeCharge(ProjetCahierDto projetCahierDto, int idProjet) {
        // On récupère le projet par son ID, ou on lance une exception si le projet n'existe pas.
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet introuvable"));

        // On vérifie que le projet est ouvert.
        projet.setUrlCahierDeCharge(projetCahierDto.getUrlCahierDeCharge());
        // On met à jour la date de création du projet.
        Projet savedProjet = projetDao.save(projet);
        // On retourne le projet mis à jour en ProjetResponseDto.
        return mapToResponseDto(savedProjet);
    }

    /**
     * Attribue un niveau de complexité à un projet.
     *
     * @param idProjet L'ID du projet auquel le niveau doit être attribué.
     * @param idAdministrateur L'ID de l'administrateur qui attribue le niveau.
     * @param niveau Le niveau de complexité à attribuer.
     * @return Un objet ProjetResponseDto contenant les informations du projet mis à jour.
     */
    public ProjetResponseDto attribuerNiveau(int idProjet, int idAdministrateur, ProjectLevel niveau) {
        // On récupère le projet par son ID, ou on lance une exception si le projet n'existe pas.
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet introuvable"));

        // On récupère l'administrateur par son ID, ou on lance une exception si l'administrateur n'existe pas.
        Administrateur admin = administrateurDao.findById(idAdministrateur)
                .orElseThrow(() -> new RuntimeException("Administrateur introuvable"));

        // On vérifie que le niveau de complexité est valide.
        if (niveau == null) {
            throw new IllegalArgumentException("Niveau de complexité invalide");
        }

        // On vérifie que le projet n'a pas déjà un niveau attribué.
        if (projet.getNiveau() != null) {
            throw new RuntimeException("Le projet a déjà un niveau attribué.");
        }

        // On attribue le niveau de complexité au projet.
        projet.setNiveau(niveau);
        projet.setValidateur(admin);

        // On notifie le créateur du projet que le niveau de complexité a été attribué.
        notificationService.createNotification(
                projet.getCreateur(),
                "Niveau de complexité attribué",
                "Le niveau de complexité '" + niveau + "' a été attribué à votre projet '" + projet.getTitre() + "'."
        );

        // On sauvegarde le projet mis à jour dans la base de données.
        Projet savedProjet = projetDao.save(projet);
        // On retourne le projet mis à jour en ProjetResponseDto.
        return mapToResponseDto(savedProjet);
    }

    /**
     * Démarre un projet.
     *
     * @param idProjet L'ID du projet à démarrer.
     * @return Un objet ProjetResponseDto contenant les informations du projet démarré.
     */
    public ProjetResponseDto demarrerProjet(int idProjet) {
        // On récupère le projet par son ID, ou on lance une exception si le projet n'existe pas.
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'ID: " + idProjet));

        // On vérifie que le projet est ouvert avant de le démarrer.
        if (projet.getStatus() != ProjectStatus.OUVERT) {
            throw new RuntimeException("Le projet doit être ouvert pour démarrer.");
        }

        // On met à jour le statut du projet à "En cours".
        projet.setStatus(ProjectStatus.EN_COURS);

        // On notifie tous les participants du projet que le projet a été démarré.
        projet.getParticipants().forEach(participant -> {
            notificationService.createNotification(
                    participant.getContributeur(),
                    "Projet démarré",
                    "Le projet '" + projet.getTitre() + "' a été démarré."
            );
        });

        // On sauvegarde le projet mis à jour dans la base de données.
        Projet savedProjet = projetDao.save(projet);
        // On retourne le projet démarré en ProjetResponseDto.
        return mapToResponseDto(savedProjet);
    }

    /**
     * Termine un projet.
     *
     * @param idProjet L'ID du projet à terminer.
     * @return Un objet ProjetResponseDto contenant les informations du projet terminé.
     */
    public ProjetResponseDto terminerProjet(int idProjet) {
        // On récupère le projet par son ID, ou on lance une exception si le projet n'existe pas.
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'ID: " + idProjet));

        // On vérifie que le projet est en cours avant de le terminer.
        if (projet.getStatus() != ProjectStatus.EN_COURS) {
            throw new RuntimeException("Le projet doit être en cours pour le terminer.");
        }
        // On met à jour le statut du projet à "Terminé".
        projet.setStatus(ProjectStatus.TERMINER);
        // On notifie tous les participants du projet que le projet a été terminé.
        projet.getParticipants().forEach(participant -> {
            notificationService.createNotification(
                    participant.getContributeur(),
                    "Projet terminé",
                    "Le projet '" + projet.getTitre() + "' a été terminé."
            );
        });
        // On sauvegarde le projet mis à jour dans la base de données.
        Projet savedProjet = projetDao.save(projet);
        // On retourne le projet terminé en ProjetResponseDto.
        return mapToResponseDto(savedProjet);
    }

    /**
     * Mappe un objet Projet en ProjetResponseDto.
     *
     * @param projet L'objet Projet à mapper.
     * @return Un objet ProjetResponseDto contenant les informations du projet.
     */
    private ProjetResponseDto mapToResponseDto(Projet projet) {
        return new ProjetResponseDto(
                projet.getId(),
                projet.getTitre(),
                projet.getDescription(),
                projet.getDomaine(),
                projet.getSecteur(),
                projet.getUrlCahierDeCharge(),
                projet.getStatus(),
                projet.getNiveau(),
                projet.getDateCreation(),
                projet.getCreateur().getNom(),
                projet.getCreateur().getPrenom(),
                projet.getValidateur() != null ? projet.getValidateur().getEmail() : null,
                projet.getParticipants().size(),
                projet.getFonctionnalites().size()
        );
    }
}
