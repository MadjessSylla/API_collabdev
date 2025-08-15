package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.*;
import odk.groupe4.ApiCollabDev.dto.*;
import odk.groupe4.ApiCollabDev.models.*;
import odk.groupe4.ApiCollabDev.models.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParticipantService {
    private final ParticipantDao participantDao;
    private final ProjetDao projetDao;
    private final ContributionDao contributionDao;
    private final NotificationService notificationService;
    private final ContributeurDao contributeurDao;
    private final ParametreCoinDao parametreCoinDao;
    private final FonctionnaliteDao fonctionnaliteDao;
    private final BadgeDao badgeDao;
    private final BadgeContributeurDao badgeContributeurDao;

    @Autowired
    public ParticipantService(ParticipantDao participantDao,
                              ProjetDao projetDao,
                              ContributionDao contributionDao,
                              NotificationService notificationService,
                              ContributeurDao contributeurDao,
                              ParametreCoinDao parametreCoinDao,
                              FonctionnaliteDao fonctionnaliteDao,
                              BadgeDao badgeDao,
                              BadgeContributeurDao badgeContributeurDao) {
        this.participantDao = participantDao;
        this.projetDao = projetDao;
        this.contributionDao = contributionDao;
        this.notificationService = notificationService;
        this.contributeurDao = contributeurDao;
        this.parametreCoinDao = parametreCoinDao;
        this.fonctionnaliteDao = fonctionnaliteDao;
        this.badgeDao = badgeDao;
        this.badgeContributeurDao = badgeContributeurDao;
    }

    /**
     * Vérifie si un contributeur a déjà envoyé une demande de candidature à un projet
     */
    public CandidatureStatusDto verifierCandidature(int idProjet, int idContributeur) {
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet introuvable avec l'ID: " + idProjet));

        Contributeur contributeur = contributeurDao.findById(idContributeur)
                .orElseThrow(() -> new RuntimeException("Contributeur introuvable avec l'ID: " + idContributeur));

        // Vérifier si le contributeur est le créateur du projet
        if (projet.getCreateur().getId() == (contributeur.getId())) {
            return new CandidatureStatusDto(false, "Vous êtes le créateur de ce projet");
        }

        // Chercher une participation existante
        Optional<Participant> participantExistant = participantDao.findByContributeurAndProjet(contributeur, projet);

        if (participantExistant.isPresent()) {
            Participant participant = participantExistant.get();
            String message = getMessageStatut(participant.getStatut());

            return new CandidatureStatusDto(
                    true,
                    participant.getStatut(),
                    message,
                    participant.getId()
            );
        } else {
            return new CandidatureStatusDto(false, "Aucune candidature trouvée pour ce projet");
        }
    }

    /**
     * Génère un message approprié selon le statut de la candidature
     */
    private String getMessageStatut(ParticipantStatus statut) {
        switch (statut) {
            case EN_ATTENTE:
                return "Votre candidature est en cours d'examen";
            case ACCEPTE:
                return "Votre candidature a été acceptée";
            case REFUSE:
                return "Votre candidature a été refusée";
            default:
                return "Statut de candidature inconnu";
        }
    }

    public ParticipantResponseDto envoyerDemande(int idProjet, int idContributeur, ParticipantDto demandeDTO) {
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet introuvable"));

        Contributeur contributeur = contributeurDao.findById(idContributeur)
                .orElseThrow(() -> new RuntimeException("Contributeur introuvable"));

        // Vérifier si le contributeur est le créateur du projet
        if (projet.getCreateur().getId() == (contributeur.getId())) {
            throw new RuntimeException("Vous ne pouvez pas postuler à votre propre projet");
        }

        if (participantDao.existsByProjetAndContributeur(projet, contributeur)) {
            throw new RuntimeException("Le contributeur a déjà envoyé une demande pour ce projet.");
        }

        Participant participant = new Participant();
        participant.setProjet(projet);
        participant.setContributeur(contributeur);
        participant.setProfil(demandeDTO.getProfil());
        participant.setStatut(ParticipantStatus.EN_ATTENTE);
        participant.setScoreQuiz(demandeDTO.getScoreQuiz());
        participant.setEstDebloque(false);
        participant.setDatePostulation(LocalDate.now());
        participant.setCommentaireMotivation(demandeDTO.getCommentaireMotivation());
        participant.setCommentaireExperience(demandeDTO.getCommentaireExperience());

        Participant savedParticipant = participantDao.save(participant);

        // Notifier le créateur du projet
        notificationService.createNotification(
                projet.getCreateur(),
                "Nouvelle candidature",
                "Un contributeur a postulé pour le profil " + demandeDTO.getProfil() +
                        " sur votre projet '" + projet.getTitre() + "'."
        );

        return mapToResponseDto(savedParticipant);
    }

    public ParticipantResponseDto accepterDemande(int participantId) {
        Participant participant = participantDao.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Participant non trouvé"));

        if (participant.getStatut() == ParticipantStatus.ACCEPTE) {
            throw new IllegalArgumentException("La demande de participation a déjà été acceptée");
        }

        participant.setStatut(ParticipantStatus.ACCEPTE);
        Participant savedParticipant = participantDao.save(participant);

        notificationService.createNotification(
                participant.getContributeur(),
                "Demande de participation acceptée",
                "Votre demande de participation au projet '" + participant.getProjet().getTitre() + "' a été acceptée."
        );

        return mapToResponseDto(savedParticipant);
    }

    public ParticipantResponseDto refuserDemande(int participantId) {
        Participant participant = participantDao.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Participant non trouvé"));

        if (participant.getStatut() == ParticipantStatus.REFUSE) {
            throw new IllegalArgumentException("La demande de participation a déjà été refusée");
        }

        participant.setStatut(ParticipantStatus.REFUSE);
        Participant savedParticipant = participantDao.save(participant);

        notificationService.createNotification(
                participant.getContributeur(),
                "Demande de participation refusée",
                "Votre demande de participation au projet '" + participant.getProjet().getTitre() + "' a été refusée."
        );

        return mapToResponseDto(savedParticipant);
    }

    public ParticipantResponseDto debloquerAcces(int idParticipant, int idProjet) {
        Participant participant = participantDao.findById(idParticipant)
                .orElseThrow(() -> new RuntimeException("Participant introuvable"));

        // Vérifier que le participant appartient bien au projet spécifié
        if (participant.getProjet().getId() != idProjet) {
            throw new RuntimeException("Le participant ne fait pas partie de ce projet");
        }

        if (participant.getStatut() != ParticipantStatus.ACCEPTE) {
            throw new RuntimeException("La demande n'a pas été acceptée.");
        }

        if (participant.isEstDebloque()) {
            throw new RuntimeException("L'accès est déjà débloqué.");
        }

        int soldeParticipant = participant.getContributeur().getTotalCoin();
        ParametreCoin coinSystem;

        switch (participant.getProjet().getNiveau()) {
            case INTERMEDIAIRE:
                coinSystem = parametreCoinDao.findByTypeEvenementLien("DEVERROUILLAGE_PROJET_INTERMEDIAIRE")
                        .orElseThrow(() -> new RuntimeException("Paramètre coin non trouvé"));
                break;
            case AVANCE:
                coinSystem = parametreCoinDao.findByTypeEvenementLien("DEVERROUILLAGE_PROJET_DIFFICILE")
                        .orElseThrow(() -> new RuntimeException("Paramètre coin non trouvé"));
                break;
            case EXPERT:
                coinSystem = parametreCoinDao.findByTypeEvenementLien("DEVERROUILLAGE_PROJET_EXPERT")
                        .orElseThrow(() -> new RuntimeException("Paramètre coin non trouvé"));
                break;
            default:
                throw new RuntimeException("Niveau de projet non reconnu pour le déverrouillage.");
        }

        int valeur = coinSystem.getValeur();

        if (soldeParticipant >= valeur) {
            participant.getContributeur().setTotalCoin(soldeParticipant - valeur);
            participant.setEstDebloque(true);
            contributeurDao.save(participant.getContributeur());
        } else {
            throw new RuntimeException("Solde insuffisant pour débloquer le projet");
        }

        Participant savedParticipant = participantDao.save(participant);

        // Notifier le participant du déverrouillage
        notificationService.createNotification(
                participant.getContributeur(),
                "Accès débloqué",
                "Vous avez débloqué l'accès au projet '" + participant.getProjet().getTitre() +
                        "' pour " + valeur + " coins."
        );

        return mapToResponseDto(savedParticipant);
    }

    public FonctionnaliteDto reserverFonctionnalite(int idParticipant, int idFonctionnalite) {
        Participant participant = participantDao.findById(idParticipant)
                .orElseThrow(() -> new RuntimeException("Participant non trouvé"));

        Fonctionnalite fonctionnalite = fonctionnaliteDao.findById(idFonctionnalite)
                .orElseThrow(() -> new RuntimeException("Fonctionnalité non trouvée"));

        if (fonctionnalite.getStatusFeatures() != FeaturesStatus.A_FAIRE) {
            throw new RuntimeException("La fonctionnalité est déjà réservée ou terminée");
        }

        fonctionnalite.setParticipant(participant);
        fonctionnalite.setStatusFeatures(FeaturesStatus.EN_COURS);
        fonctionnaliteDao.save(fonctionnalite);

        return fonctionnaliteToDto(fonctionnalite, participant);
    }

    public FonctionnaliteDto attribuerTache(int idParticipant, int idFonctionnalite) {
        Fonctionnalite fonctionnalite = fonctionnaliteDao.findById(idFonctionnalite)
                .orElseThrow(() -> new RuntimeException("Fonctionnalité introuvable"));

        Participant participant = participantDao.findById(idParticipant)
                .orElseThrow(() -> new RuntimeException("Participant introuvable"));

        fonctionnalite.setParticipant(participant);
        fonctionnalite.setStatusFeatures(FeaturesStatus.EN_COURS);
        fonctionnaliteDao.save(fonctionnalite);

        return fonctionnaliteToDto(fonctionnalite, participant);
    }

    public HistAcquisitionDto getHistAcquisition(int idParticipant) {
        Participant participant = participantDao.findById(idParticipant)
                .orElseThrow(() -> new IllegalArgumentException("Participant avec l'ID " + idParticipant + " n'existe pas."));

        // Récupérer les contributions validées du participant
        List<Contribution> contributions = contributionDao.findByParticipantIdAndStatus(idParticipant, ContributionStatus.VALIDE);
        List<ContributionDto> contributionDTOs = contributions.stream()
                .map(this::mapToContributionDTO)
                .collect(Collectors.toList());

        // Récupérer les badges du contributeur (pas du participant)
        Contributeur contributeur = participant.getContributeur();
        List<BadgeContributeur> badgeContributeurs = badgeContributeurDao.findByContributeur(contributeur);

        List<BadgeRewardDto> badgeDTOs = badgeContributeurs.stream()
                .map(this::mapToBadgeDTO)
                .collect(Collectors.toList());

        return new HistAcquisitionDto(idParticipant, contributionDTOs, badgeDTOs);
    }

    public List<BadgeRewardDto> getBadgesGagnes(int idParticipant, Integer idProjet) {
        Participant participant = participantDao.findById(idParticipant)
                .orElseThrow(() -> new RuntimeException("Participant non trouvé à l'ID " + idParticipant));

        // Récupérer tous les badges du contributeur
        Contributeur contributeur = participant.getContributeur();
        List<BadgeContributeur> badgeContributeurs = badgeContributeurDao.findByContributeur(contributeur);

        return badgeContributeurs.stream()
                .map(this::mapToBadgeDTO)
                .collect(Collectors.toList());
    }

    public List<ContributionDto> afficherContributionsParticipant(int idParticipant) {
        Participant participant = participantDao.findById(idParticipant)
                .orElseThrow(() -> new RuntimeException("Participant non trouvé"));

        List<Contribution> contributions = participant.getContributions();
        return contributions.stream()
                .map(this::ContributionDaoToContributionDto)
                .collect(Collectors.toList());
    }

    public List<ParticipantResponseDto> getParticipantsByProjet(int idProjet) {
        if (!projetDao.existsById(idProjet)) {
            throw new RuntimeException("Projet non trouvé avec l'ID: " + idProjet);
        }

        List<Participant> participants = participantDao.findByProjetId(idProjet);
        return participants.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public List<BadgeSeuilDto> getProgressionBadges(int idParticipant) {
        Participant participant = participantDao.findById(idParticipant)
                .orElseThrow(() -> new RuntimeException("Participant non trouvé"));

        // Compter toutes les contributions validées du contributeur (pas seulement du participant)
        Contributeur contributeur = participant.getContributeur();
        int nombreContributions = contributionDao.countValidatedContributionsByContributeur(contributeur.getId());

        List<Badge> tousLesBadges = badgeDao.findAllOrderByNombreContributionAsc();

        return tousLesBadges.stream()
                .map(badge -> {
                    // Vérifier si le contributeur possède déjà ce badge
                    boolean possedeBadge = badgeContributeurDao.findByContributeurAndBadge(contributeur, badge).isPresent();

                    return new BadgeSeuilDto(
                            badge.getType(),
                            badge.getNombreContribution(),
                            badge.getCoin_recompense(),
                            badge.getDescription(),
                            possedeBadge || nombreContributions >= badge.getNombreContribution()
                    );
                })
                .collect(Collectors.toList());
    }

    private ParticipantResponseDto mapToResponseDto(Participant participant) {
        return new ParticipantResponseDto(
                participant.getId(),
                participant.getProfil(),
                participant.getStatut(),
                participant.getScoreQuiz(),
                participant.isEstDebloque(),
                participant.getContributeur().getNom(),
                participant.getContributeur().getPrenom(),
                participant.getContributeur().getEmail(),
                participant.getProjet().getTitre()
        );
    }

    public FonctionnaliteDto fonctionnaliteToDto(Fonctionnalite f, Participant p) {
        FonctionnaliteDto dto = new FonctionnaliteDto();
        dto.setId(f.getId());
        dto.setIdProjet(f.getProjet().getId());
        dto.setTitre(f.getTitre());
        dto.setContenu(f.getContenu());
        dto.setNom(p.getContributeur().getNom());
        dto.setPrenom(p.getContributeur().getPrenom());
        dto.setEmail(p.getContributeur().getEmail());
        return dto;
    }

    private ContributionDto mapToContributionDTO(Contribution contribution) {
        ContributionDto contributionDto = new ContributionDto();
        contributionDto.setId(contribution.getId());
        contributionDto.setLienUrl(contribution.getLienUrl());
        contributionDto.setFileUrl(contribution.getFileUrl());
        contributionDto.setStatus(contribution.getStatus());
        contributionDto.setDateSoumission(contribution.getDateSoumission());
        contributionDto.setParticipantId(contribution.getParticipant().getId());
        contributionDto.setGestionnaireId(contribution.getGestionnaire() != null ? contribution.getGestionnaire().getId() : 0);
        contributionDto.setFonctionnaliteId(contribution.getFonctionnalite().getId());
        return contributionDto;
    }

    private BadgeRewardDto mapToBadgeDTO(BadgeContributeur badgeContributeur) {
        BadgeRewardDto dto = new BadgeRewardDto();
        dto.setIdBadge(badgeContributeur.getBadge().getId());
        dto.setTypeBadge(badgeContributeur.getBadge().getType());
        dto.setDescription(badgeContributeur.getBadge().getDescription());
        dto.setNombreContribution(badgeContributeur.getBadge().getNombreContribution());
        dto.setCoinRecompense(badgeContributeur.getBadge().getCoin_recompense());
        dto.setDateAcquisition(badgeContributeur.getDateAcquisition());
        return dto;
    }

    private ContributionDto ContributionDaoToContributionDto(Contribution contribution) {
        ContributionDto contributionDto = new ContributionDto();
        contributionDto.setId(contribution.getId());
        contributionDto.setLienUrl(contribution.getLienUrl());
        contributionDto.setFileUrl(contribution.getFileUrl());
        contributionDto.setStatus(contribution.getStatus());
        contributionDto.setDateSoumission(contribution.getDateSoumission());
        contributionDto.setParticipantId(contribution.getParticipant().getId());
        contributionDto.setGestionnaireId(contribution.getGestionnaire() != null ? contribution.getGestionnaire().getId() : 0);
        contributionDto.setFonctionnaliteId(contribution.getFonctionnalite().getId());
        return contributionDto;
    }

    @Transactional
    public ProjetResponseDto definirGestionnaire(int idProjet, int idParticipant) {
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));

        Participant participant = participantDao.findById(idParticipant)
                .orElseThrow(() -> new RuntimeException("Participant non trouvé"));

        // Vérifier que le participant fait partie du projet
        if (!(participant.getProjet().getId() == (projet.getId()))) {
            throw new RuntimeException("Le participant ne fait pas partie de ce projet");
        }

        // Vérifier que le participant est accepté
        if (participant.getStatut() != ParticipantStatus.ACCEPTE) {
            throw new RuntimeException("Le participant doit être accepté pour devenir gestionnaire");
        }

        // Définir le participant comme gestionnaire
        projet.setGestionnaire(participant);
        participant.setProfil(ParticipantProfil.GESTIONNAIRE);

        participantDao.save(participant);
        Projet savedProjet = projetDao.save(projet);

        // Notifier le participant
        notificationService.createNotification(
                participant.getContributeur(),
                "Nomination comme gestionnaire",
                "Vous avez été désigné comme gestionnaire du projet '" + projet.getTitre() + "'."
        );

        return mapProjetToResponseDto(savedProjet);
    }

    private ProjetResponseDto mapProjetToResponseDto(Projet projet) {
        String gestionnaireNom = null;
        String gestionnairePrenom = null;

        if (projet.getGestionnaire() != null) {
            gestionnaireNom = projet.getGestionnaire().getContributeur().getNom();
            gestionnairePrenom = projet.getGestionnaire().getContributeur().getPrenom();
        }

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
                projet.getDateEcheance(),
                gestionnaireNom,
                gestionnairePrenom
        );
    }
}
