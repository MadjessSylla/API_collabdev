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
import odk.groupe4.ApiCollabDev.models.Participant;
import odk.groupe4.ApiCollabDev.models.Projet;
import odk.groupe4.ApiCollabDev.models.enums.*;
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

    public List<ProjetResponseDto> getAllProjets(ProjectStatus status) {
        List<Projet> projets;
        if (status != null) {
            projets = projetDao.findByStatus(status);
        } else {
            projets = projetDao.findAll();
        }
        return projets.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public List<ProjetResponseDto> getProjetsByContributeur(int idContributeur) {
        Contributeur contributeur = contributeurDao.findById(idContributeur)
                .orElseThrow(() -> new RuntimeException("Contributeur introuvable avec l'ID: " + idContributeur));
        List<Projet> projets = projetDao.findByCreateur(contributeur);
        return projets.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public List<ProjetResponseDto> getProjetsDebloquesByContributeur(int id) {
        Contributeur contributeur = contributeurDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Contributeur non trouvé avec l'ID: " + id));

        // Conversion en liste (optionnel, car c'est un Set)
        return contributeur.getProjetsDebloques().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

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

    public List<ProjetResponseDto> getProjetsByDomaine(ProjectDomain domaine) {
        List<Projet> projets = projetDao.findByDomaine(domaine);
        return projets.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public List<ProjetResponseDto> getProjetsBySecteur(ProjectSector secteur) {
        List<Projet> projets = projetDao.findBySecteur(secteur);
        return projets.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public ProjetResponseDto getProjetById(int id) {
        Projet projet = projetDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'ID: " + id));
        return mapToResponseDto(projet);
    }

    public ProjetResponseDto proposerProjet(ProjetDto projetDto, int idCreateurProjet) {
        Contributeur contributeur = contributeurDao.findById(idCreateurProjet)
                .orElseThrow(() -> new RuntimeException("Contributeur introuvable"));

        Projet projet = new Projet();
        projet.setTitre(projetDto.getTitre());
        projet.setDescription(projetDto.getDescription());
        projet.setDomaine(projetDto.getDomaine());
        projet.setSecteur(projetDto.getSecteur());
        projet.setUrlCahierDeCharge(projetDto.getUrlCahierDeCharge());
        projet.setStatus(ProjectStatus.EN_ATTENTE);
        projet.setCreateur(contributeur);
        projet.setDateCreation(LocalDate.now());
        if (projetDto.getDateEcheance() != null) {
            projet.setDateEcheance(projetDto.getDateEcheance());
        }

        Projet savedProjet = projetDao.save(projet);

        // Crée automatiquement un Participant pour le créateur (profil selon role)
        Participant participant = new Participant();
        participant.setStatut(ParticipantStatus.ACCEPTE);
        participant.setEstDebloque(false);
        participant.setContributeur(contributeur);
        participant.setProjet(savedProjet);
        participant.setDatePostulation(LocalDate.now());

        if (projetDto.getRole() == RolePorteurProjet.PORTEUR_DE_PROJET) {
            participant.setProfil(ParticipantProfil.PORTEUR_DE_PROJET);
        } else if (projetDto.getRole() == RolePorteurProjet.GESTIONNAIRE) {
            participant.setProfil(ParticipantProfil.GESTIONNAIRE);
        } else {
            // Par défaut, si non renseigné, on considère PORTEUR_DE_PROJET
            participant.setProfil(ParticipantProfil.PORTEUR_DE_PROJET);
        }
        participantDao.save(participant);

        // Notifier tous les administrateurs
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

        notificationService.createNotification(
                projet.getCreateur(),
                "Projet validé",
                "Votre projet '" + projet.getTitre() + "' a été validé par le service de validation."
        );

        Projet savedProjet = projetDao.save(projet);
        return mapToResponseDto(savedProjet);
    }

    public void rejeterProjet(int idProjet, int idUserValide) {
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet introuvable"));

        if (projet.getStatus() != ProjectStatus.EN_ATTENTE) {
            throw new RuntimeException("Le projet doit être en attente de validation.");
        }

        Administrateur admin = administrateurDao.findById(idUserValide)
                .orElseThrow(() -> new RuntimeException("Administrateur introuvable"));

        // On passe le statut à REJETE et on supprime le projet (logique actuelle)
        projet.setValidateur(admin);
        projet.setStatus(ProjectStatus.REJETE);

        notificationService.createNotification(
                projet.getCreateur(),
                "Projet rejeté",
                "Votre projet '" + projet.getTitre() + "' a été rejeté par le service de validation."
        );

        projetDao.delete(projet);
    }

    public ProjetResponseDto editerCahierDeCharge(ProjetCahierDto projetCahierDto, int idProjet) {
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet introuvable"));
        projet.setUrlCahierDeCharge(projetCahierDto.getUrlCahierDeCharge());
        Projet savedProjet = projetDao.save(projet);
        return mapToResponseDto(savedProjet);
    }

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

        Projet savedProjet = projetDao.save(projet);
        return mapToResponseDto(savedProjet);
    }

    public ProjetResponseDto demarrerProjet(int idProjet) {
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'ID: " + idProjet));

        if (projet.getStatus() != ProjectStatus.OUVERT) {
            throw new RuntimeException("Le projet doit être ouvert pour démarrer.");
        }

        projet.setStatus(ProjectStatus.EN_COURS);

        projet.getParticipants().forEach(participant ->
                notificationService.createNotification(
                        participant.getContributeur(),
                        "Projet démarré",
                        "Le projet '" + projet.getTitre() + "' a été démarré."
                )
        );

        Projet savedProjet = projetDao.save(projet);
        return mapToResponseDto(savedProjet);
    }

    public ProjetResponseDto terminerProjet(int idProjet) {
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'ID: " + idProjet));

        if (projet.getStatus() != ProjectStatus.EN_COURS) {
            throw new RuntimeException("Le projet doit être en cours pour le terminer.");
        }

        projet.setStatus(ProjectStatus.TERMINER);
        Projet savedProjet = projetDao.save(projet);
        return mapToResponseDto(savedProjet);
    }

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
                projet.getFonctionnalites() != null ? projet.getFonctionnalites().size() : 0
        );
    }
}
