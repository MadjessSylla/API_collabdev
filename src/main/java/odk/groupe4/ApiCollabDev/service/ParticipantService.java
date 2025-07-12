package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.*;
import odk.groupe4.ApiCollabDev.dto.*;
import odk.groupe4.ApiCollabDev.models.*;
import odk.groupe4.ApiCollabDev.models.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Autowired
    public ParticipantService(ParticipantDao participantDao,
                              ProjetDao projetDao,
                              ContributionDao contributionDao,
                              NotificationService notificationService,
                              ContributeurDao contributeurDao,
                              ParametreCoinDao parametreCoinDao,
                              FonctionnaliteDao fonctionnaliteDao) {
        this.participantDao = participantDao;
        this.projetDao = projetDao;
        this.contributionDao = contributionDao;
        this.notificationService = notificationService;
        this.contributeurDao = contributeurDao;
        this.parametreCoinDao = parametreCoinDao;
        this.fonctionnaliteDao = fonctionnaliteDao;
    }

    /**
     * Envoie une demande de participation à un projet.
     * @param idProjet L'ID du projet.
     * @param idContributeur L'ID du contributeur.
     * @param demandeDTO Les détails de la demande de participation.
     * @return Un ParticipantResponseDto représentant la demande envoyée.
     */
    public ParticipantResponseDto envoyerDemande(int idProjet, int idContributeur, ParticipantDto demandeDTO) {
        // Vérification de l'existence du projet et du contributeur
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet introuvable"));

        // Vérification de l'existence du contributeur
        Contributeur contributeur = contributeurDao.findById(idContributeur)
                .orElseThrow(() -> new RuntimeException("Contributeur introuvable"));

        // Vérification si le contributeur a déjà envoyé une demande pour ce projet
        if (participantDao.existsByProjetAndContributeur(projet, contributeur)) {
            throw new RuntimeException("Le contributeur a déjà envoyé une demande pour ce projet.");
        }

        // Création de la demande de participation
        Participant participant = new Participant();
        participant.setProjet(projet);
        participant.setContributeur(contributeur);
        participant.setProfil(demandeDTO.getProfil()); // Profil du participant (Porteur de projet, Développeur, Designer, etc.)
        participant.setStatut(ParticipantStatus.EN_ATTENTE); // Statut initial de la demande
        participant.setScoreQuiz(demandeDTO.getScoreQuiz()); // Score du quiz, si applicable
        participant.setEstDebloque(false); // Indique si le participant a débloqué le projet

        // Enregistrement de la demande de participation
        Participant savedParticipant = participantDao.save(participant);
        // Envoi de la notification au contributeur
        return mapToResponseDto(savedParticipant);
    }

    /**
     * Accepte une demande de participation.
     * @param participantId L'ID du participant dont la demande est acceptée.
     * @return Un ParticipantResponseDto représentant le participant mis à jour.
     */
    public ParticipantResponseDto accepterDemande(int participantId) {
        // Vérification de l'existence du participant
        Participant participant = participantDao.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Participant non trouvé"));

        // Vérification du statut de la demande
        if (participant.getStatut() == ParticipantStatus.ACCEPTE) {
            throw new IllegalArgumentException("La demande de participation a déjà été acceptée");
        }

        // Mise à jour du statut du participant
        participant.setStatut(ParticipantStatus.ACCEPTE);
        // Enregistrement du participant mis à jour
        Participant savedParticipant = participantDao.save(participant);

        // Envoi de la notification au contributeur
        notificationService.createNotification(
                participant.getContributeur(),
                "Demande de participation acceptée",
                "Votre demande de participation au projet '" + participant.getProjet().getTitre() + "' a été acceptée."
        );

        // Retourne le ParticipantResponseDto représentant le participant mis à jour
        return mapToResponseDto(savedParticipant);
    }

    /**
     * Refuse une demande de participation.
     * @param participantId L'ID du participant dont la demande est refusée.
     * @return Un ParticipantResponseDto représentant le participant mis à jour.
     */
    public ParticipantResponseDto refuserDemande(int participantId) {
        // Vérification de l'existence du participant
        Participant participant = participantDao.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Participant non trouvé"));

        // Vérification du statut de la demande
        if (participant.getStatut() == ParticipantStatus.REFUSE) {
            throw new IllegalArgumentException("La demande de participation a déjà été refusée");
        }

        // Mise à jour du statut du participant
        participant.setStatut(ParticipantStatus.REFUSE);
        // Enregistrement du participant mis à jour
        Participant savedParticipant = participantDao.save(participant);

        // Envoi de la notification au contributeur
        notificationService.createNotification(
                participant.getContributeur(),
                "Demande de participation refusée",
                "Votre demande de participation au projet '" + participant.getProjet().getTitre() + "' a été refusée."
        );

        // Retourne le ParticipantResponseDto représentant le participant mis à jour
        return mapToResponseDto(savedParticipant);
    }

    /**
     * Débloque l'accès d'un participant à un projet.
     * @param idParticipant L'ID du participant dont l'accès est débloqué.
     * @return Un ParticipantResponseDto représentant le participant mis à jour.
     */
    public ParticipantResponseDto debloquerAcces(int idParticipant) {
        // Vérification de l'existence du participant
        Participant participant = participantDao.findById(idParticipant)
                .orElseThrow(() -> new RuntimeException("Participant introuvable"));

        // Vérification du statut du participant
        if (participant.getStatut() != ParticipantStatus.ACCEPTE) {
            throw new RuntimeException("La demande n'a pas été acceptée.");
        }

        // Vérification si l'accès est déjà débloqué
        if (participant.isEstDebloque()) {
            throw new RuntimeException("L'accès est déjà débloqué.");
        }

        // Vérification du solde du contributeur
        int soldeParticipant = participant.getContributeur().getTotalCoin();

        // Récupération du paramètre coin pour le déverrouillage du projet
        ParametreCoin coinSystem;
        // Détermination du paramètre coin en fonction du niveau du projet
        switch (participant.getProjet().getNiveau()) {
            // Niveau du projet
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

        // Vérification du solde du participant
        int valeur = coinSystem.getValeur();

        // Vérification si le solde du participant est suffisant pour débloquer le projet
        if (soldeParticipant >= valeur) {
            // Débloquer le projet
            participant.getContributeur().setTotalCoin(soldeParticipant - valeur);
            participant.setEstDebloque(true);
            // Enregistrement du participant mis à jour
            contributeurDao.save(participant.getContributeur());
        } else {
            // Si le solde est insuffisant, lancer une exception
            throw new RuntimeException("Solde insuffisant pour débloquer le projet");
        }

        // Enregistrement du participant mis à jour
        Participant savedParticipant = participantDao.save(participant);
        // Retourne le ParticipantResponseDto représentant le participant mis à jour
        return mapToResponseDto(savedParticipant);
    }

    /**
     * Réserve une fonctionnalité pour un participant.
     * @param idParticipant L'ID du participant.
     * @param idFonctionnalite L'ID de la fonctionnalité à réserver.
     * @return Un FonctionnaliteDto représentant la fonctionnalité réservée.
     */
    public FonctionnaliteDto reserverFonctionnalite(int idParticipant, int idFonctionnalite) {
        // Vérification de l'existence du participant et de la fonctionnalité
        Participant participant = participantDao.findById(idParticipant)
                .orElseThrow(() -> new RuntimeException("Participant non trouvé"));

        // Vérification de l'existence de la fonctionnalité
        Fonctionnalite fonctionnalite = fonctionnaliteDao.findById(idFonctionnalite)
                .orElseThrow(() -> new RuntimeException("Fonctionnalité non trouvée"));

        // Vérification du statut de la fonctionnalité
        if (fonctionnalite.getStatusFeatures() != FeaturesStatus.A_FAIRE) {
            throw new RuntimeException("La fonctionnalité est déjà réservée ou terminée");
        }

        // Vérification si le participant a déjà une fonctionnalité réservée
        if (fonctionnaliteDao.existsByParticipantAndStatusFeatures(participant, FeaturesStatus.EN_COURS)) {
            throw new RuntimeException("Le participant a déjà une fonctionnalité en cours");
        }
        // Mise à jour de la fonctionnalité
        fonctionnalite.setParticipant(participant);
        // Changement du statut de la fonctionnalité à EN_COURS
        fonctionnalite.setStatusFeatures(FeaturesStatus.EN_COURS);
        // Enregistrement de la fonctionnalité mise à jour
        fonctionnaliteDao.save(fonctionnalite);

        // Retourne le DTO de la fonctionnalité réservée
        return fonctionnaliteToDto(fonctionnalite, participant);
    }

    /**
     * Attribue une tâche à un participant.
     * @param idParticipant L'ID du participant.
     * @param idFonctionnalite L'ID de la fonctionnalité à attribuer.
     * @return Un FonctionnaliteDto représentant la fonctionnalité attribuée.
     */
    public FonctionnaliteDto attribuerTache(int idParticipant, int idFonctionnalite) {
        // Vérification de l'existence de la fonctionnalité
        Fonctionnalite fonctionnalite = fonctionnaliteDao.findById(idFonctionnalite)
                .orElseThrow(() -> new RuntimeException("Fonctionnalité introuvable"));

        // Vérification du statut de la fonctionnalité
        Participant participant = participantDao.findById(idParticipant)
                .orElseThrow(() -> new RuntimeException("Participant introuvable"));

        // Vérification si la fonctionnalité est déjà en cours ou terminée
        if (fonctionnalite.getStatusFeatures() != FeaturesStatus.A_FAIRE) {
            throw new RuntimeException("La fonctionnalité est déjà en cours ou terminée");
        }
        // Vérification si le participant a déjà une fonctionnalité en cours
        if (fonctionnaliteDao.existsByParticipantAndStatusFeatures(participant, FeaturesStatus.EN_COURS)) {
            throw new RuntimeException("Le participant a déjà une fonctionnalité en cours");
        }
        // Mise à jour de la fonctionnalité
        fonctionnalite.setParticipant(participant);
        fonctionnalite.setStatusFeatures(FeaturesStatus.EN_COURS);
        // Enregistrement de la fonctionnalité mise à jour
        fonctionnaliteDao.save(fonctionnalite);

        // Retourne le DTO de la fonctionnalité attribuée
        return fonctionnaliteToDto(fonctionnalite, participant);
    }

    /**
     * Récupère l'historique d'acquisition des badges et contributions d'un participant.
     * @param idParticipant L'ID du participant.
     * @return Un HistAcquisitionDto contenant les contributions et badges du participant.
     */
    public HistAcquisitionDto getHistAcquisition(int idParticipant) {
        // Vérification de l'existence du participant
        if (!participantDao.existsById(idParticipant)) {
            throw new IllegalArgumentException("Participant avec l'ID " + idParticipant + " n'existe pas.");
        }

        // Récupération des contributions validées et des badges du participant
        List<Contribution> contributions = contributionDao.findByParticipantIdAndStatus(idParticipant, ContributionStatus.VALIDE);
        // Mapping des contributions en ContributionDto
        List<ContributionDto> contributionDTOs = contributions.stream()
                .map(this::mapToContributionDTO)
                .collect(Collectors.toList());

        List<BadgeParticipant> badgeParticipants = participantDao.findById(idParticipant)
                .orElseThrow(() -> new IllegalArgumentException("Participant avec l'ID " + idParticipant + " n'existe pas."))
                .getBadgeParticipants().stream().toList();

        // Mapping des badges en BadgeRewardDto
        List<BadgeRewardDto> badgeDTOs = badgeParticipants.stream()
                .map(this::mapToBadgeDTO)
                .collect(Collectors.toList());

        // Création et retour de l'objet HistAcquisitionDto
        return new HistAcquisitionDto(idParticipant, contributionDTOs, badgeDTOs);
    }

    /**
     * Affiche les contributions d'un participant.
     * @param idParticipant L'ID du participant.
     * @return Une liste de ContributionDto représentant les contributions du participant.
     */
    public List<ContributionDto> afficherContributionsParticipant(int idParticipant) {
        // Vérification de l'existence du participant
        Participant participant = participantDao.findById(idParticipant)
                .orElseThrow(() -> new RuntimeException("Participant non trouvé"));

        // Récupération des contributions du participant
        List<Contribution> contributions = participant.getContributions();
        return contributions.stream()
                .map(this::ContributionDaoToContributionDto)
                .collect(Collectors.toList());
    }

    /**
     * Récupère la liste des participants d'un projet.
     * @param idProjet L'ID du projet.
     * @return Une liste de ParticipantResponseDto représentant les participants du projet.
     */
    public List<ParticipantResponseDto> getParticipantsByProjet(int idProjet) {
        // Vérification de l'existence du projet
        if (!projetDao.existsById(idProjet)) {
            throw new RuntimeException("Projet non trouvé avec l'ID: " + idProjet);
        }

        // Récupération des participants du projet
        List<Participant> participants = participantDao.findByProjetId(idProjet);
        return participants.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Mapppe un participant à un ParticipantResponseDto.
     * @param participant
     * @return
     */
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

    /**
     * Convertit une fonctionnalité en DTO.
     * @param f La fonctionnalité à convertir.
     * @param p Le participant associé à la fonctionnalité.
     * @return Un DTO représentant la fonctionnalité.
     */
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

    /**
     * Convertit une contribution en DTO.
     * @param contribution La contribution à convertir.
     * @return Un DTO représentant la contribution.
     */
    private ContributionDto mapToContributionDTO(Contribution contribution) {
        ContributionDto contributionDto = new ContributionDto();
        contributionDto.setIdContribution(contribution.getId());
        contributionDto.setLienUrl(contribution.getLienUrl());
        contributionDto.setFileUrl(contribution.getFileUrl());
        contributionDto.setStatus(contribution.getStatus());
        contributionDto.setDateSoumission(contribution.getDateSoumission());
        contributionDto.setParticipantId(contribution.getParticipant().getId());
        contributionDto.setGestionnaireId(contribution.getGestionnaire() != null ? contribution.getGestionnaire().getId() : 0);
        contributionDto.setFonctionnaliteId(contribution.getFonctionnalite().getId());
        return contributionDto;
    }

    /**
     * Convertit un BadgeParticipant en BadgeRewardDto.
     * @param badgeParticipant Le BadgeParticipant à convertir.
     * @return Un DTO représentant le badge.
     */
    private BadgeRewardDto mapToBadgeDTO(BadgeParticipant badgeParticipant) {
        BadgeRewardDto dto = new BadgeRewardDto();
        dto.setIdBadge(badgeParticipant.getBadge().getId());
        dto.setTypeBadge(badgeParticipant.getBadge().getType());
        dto.setDescription(badgeParticipant.getBadge().getDescription());
        dto.setNombreContribution(badgeParticipant.getBadge().getNombreContribution());
        dto.setCoinRecompense(badgeParticipant.getBadge().getCoin_recompense());
        dto.setDateAcquisition(badgeParticipant.getDateAcquisition());
        return dto;
    }

    /**
     * Convertit une contribution en DTO.
     * @param contribution La contribution à convertir.
     * @return Un DTO représentant la contribution.
     */
    private ContributionDto ContributionDaoToContributionDto(Contribution contribution) {
        ContributionDto contributionDto = new ContributionDto();
        contributionDto.setIdContribution(contribution.getId());
        contributionDto.setLienUrl(contribution.getLienUrl());
        contributionDto.setFileUrl(contribution.getFileUrl());
        contributionDto.setStatus(contribution.getStatus());
        contributionDto.setDateSoumission(contribution.getDateSoumission());
        contributionDto.setParticipantId(contribution.getParticipant().getId());
        contributionDto.setGestionnaireId(contribution.getGestionnaire() != null ? contribution.getGestionnaire().getId() : 0);
        contributionDto.setFonctionnaliteId(contribution.getFonctionnalite().getId());
        return contributionDto;
    }
}
