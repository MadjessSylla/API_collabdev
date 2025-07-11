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
import odk.groupe4.ApiCollabDev.models.enums.ProjectLevel;
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

        return mapToResponseDto(savedProjet);
    }

    public ProjetResponseDto validerProjet(int idProjet, int idUserValide) {
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet introuvable"));

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

        Administrateur admin = administrateurDao.findById(idUserValide)
                .orElseThrow(() -> new RuntimeException("Administrateur introuvable"));

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

        projet.getParticipants().forEach(participant -> {
            notificationService.createNotification(
                    participant.getContributeur(),
                    "Projet démarré",
                    "Le projet '" + projet.getTitre() + "' a été démarré."
            );
        });

        Projet savedProjet = projetDao.save(projet);
        return mapToResponseDto(savedProjet);
    }

    public ProjetResponseDto terminerProjet(int idProjet) {
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'ID: " + idProjet));

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
                projet.getCreateur().getNom(),
                projet.getCreateur().getPrenom(),
                projet.getValidateur() != null ? projet.getValidateur().getEmail() : null,
                projet.getParticipants().size(),
                projet.getFonctionnalites().size()
        );
    }
}
