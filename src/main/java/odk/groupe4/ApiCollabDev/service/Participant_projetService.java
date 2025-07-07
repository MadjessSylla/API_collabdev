package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.ContributeurDao;
import odk.groupe4.ApiCollabDev.dao.Participant_projetDao;
import odk.groupe4.ApiCollabDev.dao.ProjetDao;
import odk.groupe4.ApiCollabDev.dto.Participant_projetDto;
import odk.groupe4.ApiCollabDev.models.Contributeur;
import odk.groupe4.ApiCollabDev.models.Participant;
import odk.groupe4.ApiCollabDev.models.Projet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static odk.groupe4.ApiCollabDev.models.enums.DemandeParticipation.EN_ATTENTE;


@Service
public class Participant_projetService {
  @Autowired
  private Participant_projetDao participantDao;
  @Autowired
  private ContributeurDao contributeurDao;
  @Autowired
  ProjetDao projetDao;

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

}
