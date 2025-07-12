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
    private final BadgeDao badgeDao;

    @Autowired
    public ParticipantService(ParticipantDao participantDao,
                              ProjetDao projetDao,
                              ContributionDao contributionDao,
                              NotificationService notificationService,
                              ContributeurDao contributeurDao,
                              ParametreCoinDao parametreCoinDao,
                              FonctionnaliteDao fonctionnaliteDao,
                              BadgeDao badgeDao) {
        this.participantDao = participantDao;
        this.projetDao = projetDao;
        this.contributionDao = contributionDao;
        this.notificationService = notificationService;
        this.contributeurDao = contributeurDao;
        this.parametreCoinDao = parametreCoinDao;
        this.fonctionnaliteDao = fonctionnaliteDao;
        this.badgeDao = badgeDao;
    }

    public ParticipantResponseDto envoyerDemande(int idProjet, int idContributeur, ParticipantDto demandeDTO) {
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet introuvable"));

        Contributeur contributeur = contributeurDao.findById(idContributeur)
                .orElseThrow(() -> new RuntimeException("Contributeur introuvable"));

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

        Participant savedParticipant = participantDao.save(participant);
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

    public ParticipantResponseDto debloquerAcces(int idParticipant) {
        Participant participant = participantDao.findById(idParticipant)
                .orElseThrow(() -> new RuntimeException("Participant introuvable"));

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
        if (!participantDao.existsById(idParticipant)) {
            throw new IllegalArgumentException("Participant avec l'ID " + idParticipant + " n'existe pas.");
        }

        List<Contribution> contributions = contributionDao.findByParticipantIdAndStatus(idParticipant, ContributionStatus.VALIDE);
        List<ContributionDto> contributionDTOs = contributions.stream()
                .map(this::mapToContributionDTO)
                .collect(Collectors.toList());

        List<BadgeParticipant> badgeParticipants = participantDao.findById(idParticipant)
                .orElseThrow(() -> new IllegalArgumentException("Participant avec l'ID " + idParticipant + " n'existe pas."))
                .getBadgeParticipants().stream().toList();

        List<BadgeRewardDto> badgeDTOs = badgeParticipants.stream()
                .map(this::mapToBadgeDTO)
                .collect(Collectors.toList());

        return new HistAcquisitionDto(idParticipant, contributionDTOs, badgeDTOs);
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

        int nombreContributions = contributionDao.findByParticipantIdAndStatus(idParticipant, ContributionStatus.VALIDE).size();

        List<Badge> tousLesBadges = badgeDao.findAllOrderByNombreContributionAsc();

        return tousLesBadges.stream()
                .map(badge -> new BadgeSeuilDto(
                        badge.getType(),
                        badge.getNombreContribution(),
                        badge.getCoin_recompense(),
                        badge.getDescription(),
                        nombreContributions >= badge.getNombreContribution()
                ))
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
