package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.*;
import odk.groupe4.ApiCollabDev.dto.*;
import odk.groupe4.ApiCollabDev.models.*;
import odk.groupe4.ApiCollabDev.models.enums.ParticipantProfil;
import odk.groupe4.ApiCollabDev.models.enums.ContributionStatus;
import odk.groupe4.ApiCollabDev.models.enums.ParticipantStatus;
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

    // Methode pour participer à un projet
    public Participant envoyerDemande(int idProjet, int idContributeur){
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
        // on va verifier si le contributeur est le gestionnaire du projet
        if(projet.getGestionnaire().getId() == idContributeur){
            throw new RuntimeException("Le gestionnaire du projet ne peut pas envoyer une demande de participation.");
        }
        Participant participant = new Participant();
     // on va recuperer les données qui se trouve demandeDTO et les affecter à participant
        participant.setProjet(projet);
        participant.setContributeur(contributeur);
        participant.setProfil(demandeDTO.getProfil());
        participant.setReponseQuiz(demandeDTO.getReponseQuiz());

     // par defaut la demande est en attente
        participant.setDemande(EN_ATTENTE);

     return participantDao.save(participant);

    }

    // Méthode pour refuser une demande de participation
    public void updateParticipantStatus(int participantId, ParticipantStatus newStatus) {
        Participant participant = participantProjetDao.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Participant non trouvé"));
        participant.setStatut(newStatus);
        participantProjetDao.save(participant);

        if (newStatus == ParticipantStatus.REFUSE) {
            notificationService.createNotification(
                    participant.getContributeur(),
                    "Participation refusée",
                    "Votre demande de participation au projet '" + participant.getProjet().getTitre() + "' a été refusée."
            );
        }
    }

    // Methode pour debloquer l'accés à un projet
     public String debloquerAcces(int idParticipant){
        Participant participant= participantDao.findById(idParticipant)
                .orElseThrow(()->new RuntimeException("Participant introuvable"));

     // on va verifier si la demande a été acceptée
     if(participant.getDemande() != DemandeParticipation.ACCEPTER){

         throw new RuntimeException("La demande n'a pas été acceptée.");
     }
     // on va verifier que l'accés n'a pas été deja débloqué
         if(participant.isAccesDebloquer()){
             throw new RuntimeException("L'accès est déjà débloqué.");
         }
     //on recupère le solde du participant en passant par (l'objet contributeur)
      int solde_Participant = participant.getContributeur().getTotalCoin();

     // on recupère dans la bd le coin definit pour le debloquage à l'accès d'un projet
     ParametreCoin coinSystem= parametreCoinDao.findByTypeEvenementLien("DEVERROUILLAGE_PROJET_INTERMEDIAIRE").get();

     // on recupere la valeur à depenser pour debloquer le projet

     int valeur = coinSystem.getValeur();

     // Vérifie que le participant ait le montant suffisant pour débloquer le projet
         if(solde_Participant >= valeur){

             //on soustrait et on actualise le solde du participant
             participant.getContributeur().setTotalCoin(solde_Participant - valeur);
             participant.setAccesDebloquer(true);
         }else {

             throw new RuntimeException("Solde insuffisant pour débloquer le projet");
         }
      //on récupérer le contributeur associé à un participant.
         Contributeur contributeur=participant.getContributeur();
             contributeurDao.save(contributeur);

      //sauvegarde l'état du participant dans la base de données
         participantDao.save(participant);

        return "Projet débloqué avec succès";
     }

    // Méthode pour inscrire un participant dans un projet
    public Participant creerParticipant(Participant participant) {
        // Sauvegarde du participant
        Participant savedParticipant = participantProjetDao.save(participant);

        // Récupérer le projet associé
        Projet projet = savedParticipant.getProjet();

        // Récupérer tous les participants du projet avec le profil GESTIONNAIRE
        projet.getParticipants().stream()
                .filter(p -> p.getProfil() == ParticipantProfil.GESTIONNAIRE)
                .forEach(gestionnaire -> {
                    // Envoyer une notification à l'utilisateur du contributeur du gestionnaire
                    notificationService.createNotification(
                            gestionnaire.getContributeur(),
                            "Nouvelle demande de participation",
                            "Un contributeur a demandé à participer au projet '" + projet.getTitre() + "'."
                    );
                });

        return savedParticipant;
    }

    // Méthode pour ajouter un participant à un projet
    public Participant ajouterParticipant(ParticipantDto participantProjet){
        Participant participant = new Participant();
        participant.setProfil(participantProjet.getProfil());
        return participantProjetDao.save(participant);
    }


    // Methode pour attribuer une tache à un participant
    public FonctionnaliteDto attribuerTache(int idParticipant, int idProjet, int idFonctionnalite){
        Projet projet= projetDao.findById(idProjet)
                .orElseThrow(()->new RuntimeException("Projet introuvable"));

        Fonctionnalite f = projet.getFonctionnalites().stream().
                filter(p -> p.getId() == idFonctionnalite).
                findFirst().get();

        //on va recuper l'objet participant dans le Dao
        Participant participant = participantDao.findById(idParticipant)
                .orElseThrow(()->new RuntimeException("participant introuvable"));

        //on affecte l'objet participant à la fonctionnalité
        f.setParticipant(participant);
        return fonctionnaliteToDto(f,participant);
    }

    //Méthode pour reserver une fonctionnalité à un participant
    public ParticipantDto reserverFonctionnalite(int idParticipant, int idFonctionnalite) {
        Participant participant = participantProjetDao.findById(idParticipant).orElseThrow(() -> new RuntimeException("Participant non trouvé"));
        Fonctionnalite fonctionnalite = fonctionnaliteDao.findById(idFonctionnalite).orElseThrow(() -> new RuntimeException("Fonctionnalité non trouvée"));
        // Vérifier si la fonctionnalité est déjà réservée
        if (fonctionnalite.getStatusFeatures() == StatusFeatures.EN_COURS || fonctionnalite.getStatusFeatures() == StatusFeatures.TERMINEE) {
            throw new RuntimeException("La fonctionnalité est déjà réservée");
        }
        // Réserver la fonctionnalité
        else {
            fonctionnalite.setStatusFeatures(StatusFeatures.EN_COURS);
            participant.setFonctionnalite(fonctionnalite);
            return Participant_projetToParticipant_projetDto( participantProjetDao.save(participant));
        }
    }

    // Methode pour soumettre une contribution
    public ContributionDto SoumettreUneContribution(String dateHeader, int idParticipant, ContributionDto contributiondto){
        Contribution contribution = new Contribution();
        ContributionStatus status = ContributionStatus.ENVOYE;
        Participant participant = participantProjetDao.findById(idParticipant)
                .orElseThrow(() -> new RuntimeException("Participant non trouvé"));
        LocalDate dateCreation = LocalDate.now(ZoneOffset.UTC);;
        if(dateHeader != null) {
            try {
                // Parser l'en-tête Date au format RFC 1123
                DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
                ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateHeader, formatter);
                // Convertir ZonedDateTime en java.util.Date
                dateCreation = zonedDateTime.toLocalDate();
            } catch (DateTimeParseException e) {
                System.out.println("Erreur de parsing de l'en-tête Date, utilisation de la date actuelle : " + dateCreation);
            }
        }else {
            // Fallback à la date actuelle si l'en-tête Date est absent
            System.out.println("En-tête Date absent, utilisation de la date actuelle : " + dateCreation);
        }
        contribution.setLienUrl(contributiondto.getLienUrl());
        contribution.setFileUrl(contributiondto.getFileUrl());
        contribution.setStatus(status); // pour mettre le status par défaut à "En attente de validation"
        contribution.setDateCreation(dateCreation);
        contribution.setParticipant(participant);
        contribution.setFonctionnalite(contributiondto.getFonctionnalite());

        // Enregistrement du participant lié à la contribution
        participant.getContributions().add(contribution);
        participantProjetDao.save(participant);
        // Enregistrement de la contribution dans la base de données
        Contribution contributionSaved = contributionDao.save(contribution);
        return ContributionDaoToContributionDto(contributionSaved);
    }

    // Méthode pour afficher la liste des participants d'un projet
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
        List<Contribution> contributions = contributionDao.findByParticipantIdAndStatus(idParticipant, ContributionStatus.VALIDER);

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

    // Méthode pour afficher les contributions d'un participant
    public List<ContributionDto> afficherContributionsParticipant(int idParticipant) {
        Participant participant = participantProjetDao.findById(idParticipant)
                .orElseThrow(() -> new RuntimeException("Participant non trouvé"));
        List<Contribution> contributions = participant.getContributions();
        return contributions.stream()
                .map(this::ContributionDaoToContributionDto)
                .toList();
    }

    // Methode permettant de convertir Fonctionnalite en fonctionnalite DTO
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
        return new ContributionDto(
                contribution.getId(),
                contribution.getLienUrl(),
                contribution.getFileUrl(),
                contribution.getStatus(),
                contribution.getDateSoumission(),
                contribution.getFonctionnalite() != null ? contribution.getFonctionnalite().getId() : 0
        );
    }

    // Méthode pour mapper un Badge_participant vers un BadgeRewardDto
    private BadgeRewardDto mapToBadgeDTO(BadgeParticipant badgeParticipant) {
        return new BadgeRewardDto(
                badgeParticipant.getBadge().getId(),
                badgeParticipant.getBadge().getType(),
                badgeParticipant.getBadge().getDescription(),
                badgeParticipant.getBadge().getNombreContribution(),
                badgeParticipant.getBadge().getCoin_recompense(),
                badgeParticipant.getDateAcquisition()
        );
    }

    //Méthode pour transformer un Participant_projet en Participant_projetDto
    private ParticipantDto Participant_projetToParticipant_projetDto(Participant participant) {
        ParticipantDto participantProjetDto = new ParticipantDto();
        participantProjetDto.setProfil(participant.getProfil());
        participantProjetDto.setContributions(participant.getContributions());
        participantProjetDto.setFonctionnalite(participant.getFonctionnalite());
        return participantProjetDto;
    }

    // Méthode contrionDAOtoDTO
    private ContributionDto ContributionDaoToContributionDto(Contribution contribution) {
        ContributionDto contributionDto = new ContributionDto();
        contributionDto.setLienUrl(contribution.getLienUrl());
        contributionDto.setFileUrl(contribution.getFileUrl());
        contributionDto.setStatus(contribution.getStatus());
        contributionDto.setDateCreation(contribution.getDateCreation());
        contributionDto.setParticipant(contribution.getParticipant());
        contributionDto.setGestionnaire(contribution.getGestionnaire());
        contributionDto.setFonctionnalite(contribution.getFonctionnalite());
        return contributionDto;
    }
}
