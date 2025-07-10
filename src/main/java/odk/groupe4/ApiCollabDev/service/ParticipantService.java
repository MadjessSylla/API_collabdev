package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.*;
import odk.groupe4.ApiCollabDev.dto.*;
import odk.groupe4.ApiCollabDev.models.*;
import odk.groupe4.ApiCollabDev.models.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParticipantService {
    private final ParticipantDao participantProjetDao;
    private final ProjetDao projetDao;
    private final ContributionDao contributionDao;
    private final NotificationService notificationService;
    private final ContributeurDao contributeurDao;
    private final ParametreCoinDao parametreCoinDao;
    private final FonctionnaliteDao fonctionnaliteDao;

    @Autowired
    public ParticipantService(ParticipantDao participantProjetDao,
                              ProjetDao projetDao,
                              ContributionDao contributionDao,
                              NotificationService notificationService,
                              ContributeurDao contributeurDao,
                              ParametreCoinDao parametreCoinDao,
                              FonctionnaliteDao fonctionnaliteDao) {
        this.participantProjetDao = participantProjetDao;
        this.projetDao = projetDao;
        this.contributionDao = contributionDao;
        this.notificationService = notificationService;
        this.contributeurDao = contributeurDao;
        this.parametreCoinDao = parametreCoinDao;
        this.fonctionnaliteDao = fonctionnaliteDao;
    }

    /**
     * Méthode pour envoyer une demande de participation à un projet.
     *
     * @param idProjet      L'ID du projet auquel le contributeur souhaite participer.
     * @param idContributeur L'ID du contributeur qui envoie la demande.
     * @param demandeDTO    Les données de la demande de participation.
     * @return Le participant créé avec les détails de la demande.
     */
    public Participant envoyerDemande(int idProjet, int idContributeur, ParticipantDto demandeDTO){
        // on va recuperer l'objet projet dans le Dao
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(()-> new RuntimeException("Projet introuvable"));

        // on va recuperer l'objet contributeur dans le Dao
        Contributeur contributeur=contributeurDao.findById(idContributeur)
                .orElseThrow(()-> new RuntimeException("Contributeur introuvable"));

        // on va verifier si le contributeur a deja envoyé une demande de participation
        if(participantProjetDao.existsByProjetAndContributeur(projet, contributeur)){
            throw new RuntimeException("Le contributeur a déjà envoyé une demande pour ce projet.");
        }

        Participant participant = new Participant();
        // On va récupèrer les données qui se trouvent demandeDTO et les affecter à participant
        participant.setProjet(projet);
        participant.setContributeur(contributeur);
        participant.setProfil(demandeDTO.getProfil());
        participant.setStatut(ParticipantStatus.EN_ATTENTE);
        participant.setScoreQuiz(demandeDTO.getScoreQuiz());
        participant.setEstDebloque(false);

        return participantProjetDao.save(participant);

    }

    /**
     * Méthode pour accepter une demande de participation d'un contributeur à un projet.
     *
     * @param participantId L'ID du participant dont la demande doit être acceptée.
     */
    public void accepterDemande(int participantId) {
        Participant participant = participantProjetDao.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Participant non trouvé"));

        // Vérifier si le participant est déjà accepté
        if (participant.getStatut() == ParticipantStatus.ACCEPTE) {
            throw new IllegalArgumentException("La demande de participation a déjà été acceptée");
        }
        // Mettre à jour le statut du participant
        participant.setStatut(ParticipantStatus.ACCEPTE);
        // Enregistrer le participant avec le nouveau statut
        participantProjetDao.save(participant);
        // Envoyer une notification au contributeur
        notificationService.createNotification(
                participant.getContributeur(),
                "Demande de participation acceptée",
                "Votre demande de participation au projet '" + participant.getProjet().getTitre() + "' a été acceptée."
        );
    }

    /** Méthode pour refuser une demande de participation d'un contributeur à un projet.
     *
     * @param participantId
     */
     public void refuserDemande(int participantId) {
        Participant participant = participantProjetDao.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Participant non trouvé"));

        // Vérifier si le participant est déjà refusé
        if (participant.getStatut() == ParticipantStatus.REFUSE) {
            throw new IllegalArgumentException("La demande de participation a déjà été refusée");
        }
        // Mettre à jour le statut du participant
        participant.setStatut(ParticipantStatus.REFUSE);
        // Enregistrer le participant avec le nouveau statut
        participantProjetDao.save(participant);
        // Envoyer une notification au contributeur
        notificationService.createNotification(
                participant.getContributeur(),
                "Demande de participation refusée",
                "Votre demande de participation au projet '" + participant.getProjet().getTitre() + "' a été refusée."
        );
    }

    /** Méthode pour débloquer l'accès d'un participant à un projet.
     * Cette méthode permet à un participant d'accéder à un projet en utilisant ses coins. Le montant requis pour débloquer l'accès dépend du niveau de complexité du projet.
     * @throws RuntimeException si le participant n'est pas accepté, s'il a déjà débloqué l'accès ou si le solde du participant est insuffisant.
     * @param idParticipant
     * @return Un message de succès indiquant que le projet a été débloqué avec succès.
     */
     public String debloquerAcces(int idParticipant){
         // On va recuperer l'objet participant dans le Dao
         Participant participant= participantProjetDao.findById(idParticipant)
                .orElseThrow(()->new RuntimeException("Participant introuvable"));

         // On va verifier que le participant a bien été accepté
         if(participant.getStatut() != ParticipantStatus.ACCEPTE){
             throw new RuntimeException("La demande n'a pas été acceptée.");
         }

         // On va vérifier si le participant a déjà débloqué l'accès au projet
         if(participant.isEstDebloque()){
             throw new RuntimeException("L'accès est déjà débloqué.");
         }

         // Récupération le solde du participant en passant par (l'objet contributeur)
         int solde_Participant = participant.getContributeur().getTotalCoin();

         ParametreCoin coinSystem;
         // On vérifie le niveau de complexité du projet pour récupérer dans la base de données le paramètre Coin appropriés
         if(participant.getProjet().getNiveau() == ProjectLevel.INTERMEDIAIRE){
             coinSystem = parametreCoinDao.
                     findByTypeEvenementLien("DEVERROUILLAGE_PROJET_INTERMEDIAIRE").get();
         } else if (participant.getProjet().getNiveau() == ProjectLevel.AVANCE) {
             coinSystem = parametreCoinDao.
                     findByTypeEvenementLien("DEVERROUILLAGE_PROJET_AVANCE").get();
         } else if (participant.getProjet().getNiveau() == ProjectLevel.EXPERT) {
             coinSystem = parametreCoinDao.
                     findByTypeEvenementLien("DEVERROUILLAGE_PROJET_EXPERT").get();
         } else {
             throw new RuntimeException("Niveau de projet non reconnu pour le déverrouillage.");
         }

         // On récupère la valeur du paramètre Coin
         int valeur = coinSystem.getValeur();

         // Vérifie que le participant ait le montant suffisant pour débloquer le projet
         if( solde_Participant >= valeur){
             //on soustrait et on actualise le solde du participant
             participant.getContributeur().setTotalCoin(solde_Participant - valeur);
             participant.setEstDebloque(true);
         } else {
             throw new RuntimeException("Solde insuffisant pour débloquer le projet");
         }
         // Sauvegarde du participant
         participantProjetDao.save(participant);
         return "Projet débloqué avec succès";
     }

    /** Méthode pour attribuer une tâche à un participant.
     *
     * @param idParticipant L'ID du participant auquel la tâche doit être attribuée.
     * @param idFonctionnalite L'ID de la fonctionnalité à laquelle la tâche est associée.
     * @return Un objet FonctionnaliteDto représentant la fonctionnalité mise à jour.
     */
    public FonctionnaliteDto attribuerTache(int idParticipant, int idFonctionnalite){
         // On récupère l'objet Fonctionnalité dans le Dao
        Fonctionnalite fonctionnalite = fonctionnaliteDao.findById(idFonctionnalite)
                .orElseThrow(() -> new RuntimeException("Fonctionnalité introuvable"));

        // On va récupère l'objet participant dans le Dao
        Participant participant = participantProjetDao.findById(idParticipant)
                .orElseThrow(()->new RuntimeException("participant introuvable"));

        //on affecte l'objet participant à la fonctionnalité
        fonctionnalite.setParticipant(participant);
        // On met à jour le statut de la fonctionnalité
        fonctionnalite.setStatusFeatures(FeaturesStatus.EN_COURS);
        // On enregistre la fonctionnalité dans la base de données
        fonctionnaliteDao.save(fonctionnalite);
        // On retourne la fonctionnalité convertie en DTO
        return fonctionnaliteToDto(fonctionnalite,participant);
    }

    /**
     * Méthode pour réserver une fonctionnalité à un participant.
     *
     * @param idParticipant L'ID du participant qui souhaite réserver la fonctionnalité.
     * @param idFonctionnalite L'ID de la fonctionnalité à réserver.
     * @return Un objet FonctionnaliteDto représentant la fonctionnalité réservée.
     */
    public FonctionnaliteDto reserverFonctionnalite(int idParticipant, int idFonctionnalite) {

        // Récupérer le participant et la fonctionnalité à partir de leurs IDs
        Participant participant = participantProjetDao.findById(idParticipant)
                .orElseThrow(() -> new RuntimeException("Participant non trouvé"));
        // Vérifier si le participant a déjà une fonctionnalité réservée
        if (participant.getFonctionnalite() != null) {
            throw new RuntimeException("Le participant a déjà une fonctionnalité réservée");
        }
        // Récupérer la fonctionnalité à partir de son ID
        Fonctionnalite fonctionnalite = fonctionnaliteDao.findById(idFonctionnalite)
                .orElseThrow(() -> new RuntimeException("Fonctionnalité non trouvée"));


        // Vérifier si la fonctionnalité est déjà réservée
        if (fonctionnalite.getStatusFeatures() != FeaturesStatus.A_FAIRE) {
            throw new RuntimeException("La fonctionnalité est déjà réservée ou terminée");
        }
        // Si la fonctionnalité est disponible, on la réserve pour le participant
        else {
            // Associer la fonctionnalité au participant
            fonctionnalite.setParticipant(participant);
            // Mettre à jour le statut de la fonctionnalité
            fonctionnalite.setStatusFeatures(FeaturesStatus.EN_COURS);
            // Enregistrer la fonctionnalité mise à jour dans la base de données
            fonctionnaliteDao.save(fonctionnalite);
            // On retourne le participant converti en DTO
            return fonctionnaliteToDto(fonctionnalite, participant);
        }
    }

    /**
     * Méthode pour afficher tous les participants d'un projet.
     *
     * @return Une liste de participants associés au projet.
     */
    public List<Participant> afficherParticipantProjet() {
        return participantProjetDao.findAll();
    }

    /**
     * Méthode pour récupérer l'historique d'acquisition des badges et contributions d'un participant.
     *
     * @param idParticipant L'ID du participant dont on veut récupérer l'historique.
     * @return Un objet HistAcquisitionDto contenant les contributions et badges acquis par le participant.
     */
    public HistAcquisitionDto getHistAcquisition(int idParticipant) {
        // Vérification de l'existence du participant
        if (!participantProjetDao.existsById(idParticipant)) {
            throw new IllegalArgumentException("Participant avec l'ID " + idParticipant + " n'existe pas.");
        }

        // Récupération des contributions validées du participant
        List<Contribution> contributions = contributionDao.findByParticipantIdAndStatus(idParticipant, ContributionStatus.VALIDE);

        // Conversion des contributions vers des DTOs
        List<ContributionDto> contributionDTOs = contributions.stream()
                .map(this::mapToContributionDTO)
                .collect(Collectors.toList());

        // Récupération des badges acquis par le participant
        List<BadgeParticipant> badgeParticipants = participantProjetDao.findById(idParticipant)
                .orElseThrow(() -> new IllegalArgumentException("Participant avec l'ID " + idParticipant + " n'existe pas."))
                .getBadgeParticipants().stream().toList();

        // Conversion des badges vers des DTOs
        List<BadgeRewardDto> badgeDTOs = badgeParticipants.stream()
                .map(this::mapToBadgeDTO)
                .collect(Collectors.toList());
        // Création et retour de l'objet HistAcquisitionDto
        return new HistAcquisitionDto(
                idParticipant,
                contributionDTOs,
                badgeDTOs
        );
    }

    /**
     * Méthode pour afficher les contributions d'un participant spécifique.
     *
     * @param idParticipant L'ID du participant dont on veut afficher les contributions.
     * @return Une liste de ContributionDto représentant les contributions du participant.
     */
    public List<ContributionDto> afficherContributionsParticipant(int idParticipant) {
        // Vérification de l'existence du participant
        Participant participant = participantProjetDao.findById(idParticipant)
                .orElseThrow(() -> new RuntimeException("Participant non trouvé"));
        // Récupération des contributions du participant
        List<Contribution> contributions = participant.getContributions();
        return contributions.stream()
                .map(this::ContributionDaoToContributionDto)
                .toList();
    }

    // Methode pour convertir une Fonctionnalité en DTO
    public FonctionnaliteDto fonctionnaliteToDto(Fonctionnalite f, Participant p){
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

    // Méthode pour mapper une Contribution vers un ContributionDto
    private ContributionDto mapToContributionDTO(Contribution contribution) {
        // Création d'un nouvel objet ContributionDto et remplissage des champs à partir de l'objet Contribution
        ContributionDto contributionDto = new ContributionDto();
        // Remplissage des champs de ContributionDto avec les valeurs de Contribution
        contributionDto.setIdContribution(contribution.getId());
        contributionDto.setLienUrl(contribution.getLienUrl());
        contributionDto.setFileUrl(contribution.getFileUrl());
        contributionDto.setStatus(contribution.getStatus());
        contributionDto.setDateSoumission(contribution.getDateSoumission());
        contributionDto.setParticipantId(contribution.getParticipant().getId());
        contributionDto.setGestionnaireId(contribution.getGestionnaire().getId());
        contributionDto.setFonctionnaliteId(contribution.getFonctionnalite().getId());
        return contributionDto;
    }

    // Méthode pour mapper un BadgeParticipant vers un BadgeRewardDto
    private BadgeRewardDto mapToBadgeDTO(BadgeParticipant badgeParticipant) {
        BadgeRewardDto dto = new  BadgeRewardDto();
        // Remplissage des champs de BadgeRewardDto avec les valeurs de BadgeParticipant
        dto.setIdBadge(badgeParticipant.getBadge().getId());
        dto.setTypeBadge(badgeParticipant.getBadge().getType());
        dto.setDescription(badgeParticipant.getBadge().getDescription());
        dto.setNombreContribution(badgeParticipant.getBadge().getNombreContribution());
        dto.setCoinRecompense(badgeParticipant.getBadge().getCoin_recompense());
        dto.setDateAcquisition(badgeParticipant.getDateAcquisition());
        return dto;
    }

    // Méthode pour convertir un objet Contribution en ContributionDto
    private ContributionDto ContributionDaoToContributionDto(Contribution contribution) {
        ContributionDto contributionDto = new ContributionDto();
        contributionDto.setIdContribution(contribution.getId()); // Identifiant de la contribution
        contributionDto.setLienUrl(contribution.getLienUrl()); // Lien vers la contribution
        contributionDto.setFileUrl(contribution.getFileUrl()); // Lien vers un fichier de contribution (par exemple, un fichier de code, une image, un document, etc.)
        contributionDto.setStatus(contribution.getStatus()); // Statut de la contribution (En attente, Acceptée, Rejetée)
        contributionDto.setDateSoumission(contribution.getDateSoumission()); // Date de soumission de la contribution
        contributionDto.setParticipantId(contribution.getParticipant().getId()); // Identifiant du participant qui a soumis la contribution
        contributionDto.setGestionnaireId(contribution.getGestionnaire().getId()); // Identifiant du participant Gestionnaire qui a validé la contribution
        contributionDto.setFonctionnaliteId(contribution.getFonctionnalite().getId()); // Identifiant de la fonctionnalité à laquelle la contribution est associée
        return contributionDto;
    }
}
