package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.*;
import odk.groupe4.ApiCollabDev.dto.*;
import odk.groupe4.ApiCollabDev.models.*;
import odk.groupe4.ApiCollabDev.models.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service qui gère la logique métier pour les projets :
 * - Création, validation, rejet
 * - Filtrage par domaine, secteur, contributeur, statut
 * - Notifications associées
 */
@Service
public class ProjetService {

    private final ProjetDao projetDao;
    private final AdministrateurDao administrateurDao;
    private final ContributeurDao contributeurDao;
    private final ParticipantDao participantDao;
    private final NotificationService notificationService;

    /**
     * Injection des dépendances via constructeur
     */
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
     * Récupère tous les projets ou uniquement ceux d'un statut donné.
     */
    public List<ProjetResponseDto> getAllProjets(ProjectStatus status) {
        List<Projet> projets = (status != null)
                ? projetDao.findByStatus(status)
                : projetDao.findAll();

        return projets.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Retourne les projets créés par un contributeur spécifique.
     */
    public List<ProjetResponseDto> getProjetsByContributeur(int idContributeur) {
        Contributeur contributeur = contributeurDao.findById(idContributeur)
                .orElseThrow(() -> new RuntimeException("Contributeur introuvable avec l'ID: " + idContributeur));

        return projetDao.findByCreateur(contributeur).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Récupère les projets débloqués pour un contributeur donné.
     */
    public List<ProjetResponseDto> getProjetsDebloquesByContributeur(int id) {
        Contributeur contributeur = contributeurDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Contributeur non trouvé avec l'ID: " + id));

        // Ici, on transforme le Set de projets en liste de DTO
        return contributeur.getProjetsDebloques().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Récupère les projets ouverts selon le domaine et/ou secteur.
     */
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

    /**
     * Filtre les projets par domaine.
     */
    public List<ProjetResponseDto> getProjetsByDomaine(ProjectDomain domaine) {
        return projetDao.findByDomaine(domaine).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Filtre les projets par secteur.
     */
    public List<ProjetResponseDto> getProjetsBySecteur(ProjectSector secteur) {
        return projetDao.findBySecteur(secteur).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Récupère un projet par son identifiant.
     */
    public ProjetResponseDto getProjetById(int id) {
        Projet projet = projetDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'ID: " + id));
        return mapToResponseDto(projet);
    }

    /**
     * Permet à un contributeur de proposer un projet.
     * Crée également un participant lié au projet.
     * Notifie tous les administrateurs de la soumission.
     */
    public ProjetResponseDto proposerProjet(ProjetDto projetDto, int idCreateurProjet) {
        Contributeur contributeur = contributeurDao.findById(idCreateurProjet)
                .orElseThrow(() -> new RuntimeException("Contributeur introuvable"));

        // Création du projet
        Projet projet = new Projet();
        projet.setTitre(projetDto.getTitre());
        projet.setDescription(projetDto.getDescription());
        projet.setDomaine(projetDto.getDomaine());
        projet.setSecteur(projetDto.getSecteur());
        // L'URL du cahier de charge sera géré par uploadProjetCahier() de UploadController
        projet.setStatus(ProjectStatus.EN_ATTENTE);
        projet.setCreateur(contributeur);
        projet.setDateCreation(LocalDate.now());
        if (projetDto.getDateEcheance() != null) {
            projet.setDateEcheance(projetDto.getDateEcheance());
        }

        Projet savedProjet = projetDao.save(projet);

        // Création du participant associé au créateur
        Participant participant = new Participant();
        participant.setStatut(ParticipantStatus.ACCEPTE);
        participant.setEstDebloque(false);
        participant.setContributeur(contributeur);
        participant.setProjet(savedProjet);
        participant.setDatePostulation(LocalDate.now());

        // Détermination du profil en fonction du rôle choisi
        if (projetDto.getRole() == RolePorteurProjet.PORTEUR_DE_PROJET) {
            participant.setProfil(ParticipantProfil.PORTEUR_DE_PROJET);
        } else if (projetDto.getRole() == RolePorteurProjet.GESTIONNAIRE) {
            participant.setProfil(ParticipantProfil.GESTIONNAIRE);
        } else {
            participant.setProfil(ParticipantProfil.PORTEUR_DE_PROJET); // Par défaut
        }
        participantDao.save(participant);

        // Notification des administrateurs
        administrateurDao.findAll().forEach(administrateur ->
                notificationService.createNotification(
                        administrateur,
                        "Nouvelle idée de projet soumise",
                        "Un nouveau projet '" + projet.getTitre() + "' a été soumis par " +
                                (projet.getCreateur() != null ? projet.getCreateur().getNom() : "Un contributeur") + " pour validation."
                )
        );

        return mapToResponseDto(savedProjet);
    }

    /**
     * Valide un projet en attente.
     */
    public ProjetResponseDto validerProjet(int idProjet, int idUserValide) {
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet introuvable"));

        if (projet.getStatus() != ProjectStatus.EN_ATTENTE) {
            throw new RuntimeException("Le projet doit être en attente de validation.");
        }

        Administrateur admin = administrateurDao.findById(idUserValide)
                .orElseThrow(() -> new RuntimeException("Administrateur introuvable"));

        projet.setValidateur(admin);
        projet.setStatus(ProjectStatus.OUVERT);

        // Notifier le créateur
        notificationService.createNotification(
                projet.getCreateur(),
                "Projet validé",
                "Votre projet '" + projet.getTitre() + "' a été validé par le service de validation."
        );

        return mapToResponseDto(projetDao.save(projet));
    }

    /**
     * Rejette un projet en attente et le supprime de la base.
     */
    public void rejeterProjet(int idProjet, int idUserValide) {
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet introuvable"));

        if (projet.getStatus() != ProjectStatus.EN_ATTENTE) {
            throw new RuntimeException("Le projet doit être en attente de validation.");
        }

        Administrateur admin = administrateurDao.findById(idUserValide)
                .orElseThrow(() -> new RuntimeException("Administrateur introuvable"));

        projet.setValidateur(admin);
        projet.setStatus(ProjectStatus.REJETE);

        // Notifier le créateur
        notificationService.createNotification(
                projet.getCreateur(),
                "Projet rejeté",
                "Votre projet '" + projet.getTitre() + "' a été rejeté par le service de validation."
        );

        // Suppression du projet
        projetDao.delete(projet);
    }

    /**
     * Édite l'URL du cahier des charges d'un projet.
     */
    public ProjetResponseDto editerCahierDeCharge(ProjetCahierDto projetCahierDto, int idProjet) {
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet introuvable"));

        projet.setUrlCahierDeCharge(projetCahierDto.getUrlCahierDeCharge());
        return mapToResponseDto(projetDao.save(projet));
    }

    /**
     * Attribue un niveau de complexité à un projet.
     */
    public ProjetResponseDto attribuerNiveau(int idProjet, int idAdministrateur, ProjectLevel niveau) {
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet introuvable"));

        Administrateur admin = administrateurDao.findById(idAdministrateur)
                .orElseThrow(() -> new RuntimeException("Administrateur introuvable"));

        if (niveau == null) {
            throw new IllegalArgumentException("Niveau de complexité invalide");
        }

        if (projet.getNiveau() != null) {
            throw new RuntimeException("Le projet a déjà un niveau attribué.");
        }

        projet.setNiveau(niveau);
        projet.setValidateur(admin);

        notificationService.createNotification(
                projet.getCreateur(),
                "Niveau de complexité attribué",
                "Le niveau de complexité '" + niveau + "' a été attribué à votre projet '" + projet.getTitre() + "'."
        );

        return mapToResponseDto(projetDao.save(projet));
    }

    /**
     * Passe le projet au statut "EN_COURS".
     */
    public ProjetResponseDto demarrerProjet(int idProjet) {
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'ID: " + idProjet));

        if (projet.getStatus() != ProjectStatus.OUVERT) {
            throw new RuntimeException("Le projet doit être ouvert pour démarrer.");
        }

        projet.setStatus(ProjectStatus.EN_COURS);

        // Notifier tous les participants
        projet.getParticipants().forEach(participant ->
                notificationService.createNotification(
                        participant.getContributeur(),
                        "Projet démarré",
                        "Le projet '" + projet.getTitre() + "' a été démarré."
                )
        );

        return mapToResponseDto(projetDao.save(projet));
    }

    /**
     * Passe le projet au statut "TERMINER".
     */
    public ProjetResponseDto terminerProjet(int idProjet) {
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'ID: " + idProjet));

        if (projet.getStatus() != ProjectStatus.EN_COURS) {
            throw new RuntimeException("Le projet doit être en cours pour le terminer.");
        }

        projet.setStatus(ProjectStatus.TERMINER);
        return mapToResponseDto(projetDao.save(projet));
    }

    /**
     * Convertit un objet Projet en ProjetResponseDto pour l'API.
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
                projet.getCreateur() != null ? projet.getCreateur().getNom() : null,
                projet.getCreateur() != null ? projet.getCreateur().getPrenom() : null,
                projet.getValidateur() != null ? projet.getValidateur().getEmail() : null,
                projet.getParticipants() != null ? projet.getParticipants().size() : 0,
                projet.getFonctionnalites() != null ? projet.getFonctionnalites().size() : 0,
                projet.getDateEcheance()
        );
    }
}
