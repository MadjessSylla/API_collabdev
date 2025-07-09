package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.*;
import odk.groupe4.ApiCollabDev.dto.*;
import odk.groupe4.ApiCollabDev.models.*;
import odk.groupe4.ApiCollabDev.models.enums.DemandeParticipation;
import odk.groupe4.ApiCollabDev.models.enums.Profil;
import odk.groupe4.ApiCollabDev.models.enums.StatusContribution;
import odk.groupe4.ApiCollabDev.models.enums.StatusParticipant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class Participant_projetService {
    private final Participant_projetDao participantProjetDao;
    private final ProjetDao projetDao;
    private final ContributionDao contributionDao;
    private final NotificationService notificationService;
    private final ContributeurDao contributeurDao;
    private final ParametreCoinDao parametreCoinDao;
    private final FonctionnaliteDao fonctionnaliteDao;


    public Participant_projetService(Participant_projetDao participantProjetDao,
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

    @Autowired


    // Méthode pour récupérer l'historique d'acquisition d'un participant
    public HistAcquisitionDto getHistAcquisition(int idParticipant) {
        // Vérification de l'existence du participant
        if (!participantProjetDao.existsById(idParticipant)) {
            throw new IllegalArgumentException("Participant avec l'ID " + idParticipant + " n'existe pas.");
        }
        // Récupération des contributions validées du participant
        List<Contribution> contributions = contributionDao.findByParticipantIdAndStatus(idParticipant, StatusContribution.VALIDER);
        // Conversion des contributions vers des DTOs
        List<ContributionDto> contributionDTOs = contributions.stream()
                .map(this::mapToContributionDTO)
                .collect(Collectors.toList());

        // Récupération des badges acquis par le participant
        List<Badge_participant> badgeParticipants = participantProjetDao.findById(idParticipant)
                .get().getBadgeParticipants().stream().toList();
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
    // Méthode pour inscrire un participant dans un projet
    public Participant creerParticipant(Participant participant) {
        // Sauvegarde du participant
        Participant savedParticipant = participantProjetDao.save(participant);

        // Récupérer le projet associé
        Projet projet = savedParticipant.getProjet();

        // Récupérer tous les participants du projet avec le profil GESTIONNAIRE
        projet.getParticipants().stream()
                .filter(p -> p.getProfil() == Profil.GESTIONNAIRE)
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
    // Méthode pour refuser une demande de participation
    public void updateParticipantStatus(int participantId, StatusParticipant newStatus) {
        Participant participant = participantProjetDao.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Participant non trouvé"));
        participant.setStatut(newStatus);
        participantProjetDao.save(participant);

        if (newStatus == StatusParticipant.REFUSE) {
            notificationService.createNotification(
                    participant.getContributeur(),
                    "Participation refusée",
                    "Votre demande de participation au projet '" + participant.getProjet().getTitre() + "' a été refusée."
            );
        }
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
    private BadgeRewardDto mapToBadgeDTO(Badge_participant badgeParticipant) {
        return new BadgeRewardDto(
                badgeParticipant.getBadge().getId(),
                badgeParticipant.getBadge().getType(),
                badgeParticipant.getBadge().getDescription(),
                badgeParticipant.getBadge().getNombreContribution(),
                badgeParticipant.getBadge().getCoin_recompense(),
                badgeParticipant.getDateAcquisition()
        );
    }

  //Methode pour participer à un projet
    public Participant envoyerDemande(int idProjet, Participant_projetDto demandeDTO, int idContributeur){
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(()-> new RuntimeException("Projet introuvable"));
   //on recuperer le contributeur
        Contributeur contributeur=contributeurDao.findById(idContributeur)
                .orElseThrow(()-> new RuntimeException("Contributeur introuvable"));
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

    //Methode pour attribuer une tache à un participant
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

}
