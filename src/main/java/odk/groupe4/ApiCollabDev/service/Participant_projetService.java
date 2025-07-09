package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.*;
import odk.groupe4.ApiCollabDev.dto.FonctionnaliteDto;
import odk.groupe4.ApiCollabDev.dto.Participant_projetDto;
import odk.groupe4.ApiCollabDev.models.*;
import odk.groupe4.ApiCollabDev.models.enums.DemandeParticipation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static odk.groupe4.ApiCollabDev.models.enums.DemandeParticipation.ACCEPTER;
import static odk.groupe4.ApiCollabDev.models.enums.DemandeParticipation.EN_ATTENTE;


@Service
public class Participant_projetService {
  @Autowired
  private Participant_projetDao participantDao;
  @Autowired
  private ContributeurDao contributeurDao;
  @Autowired
  ProjetDao projetDao;
  @Autowired
  private ParametreCoinDao parametreCoinDao;
  @Autowired
  private FonctionnaliteDao fonctionnaliteDao;

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
