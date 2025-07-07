package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.FonctionnaliteDao;
import odk.groupe4.ApiCollabDev.dao.Participant_projetDao;
import odk.groupe4.ApiCollabDev.dto.Participant_projetDto;
import odk.groupe4.ApiCollabDev.models.Fonctionnalite;
import odk.groupe4.ApiCollabDev.models.Participant;
import odk.groupe4.ApiCollabDev.models.Projet;
import odk.groupe4.ApiCollabDev.models.Utilisateur;
import odk.groupe4.ApiCollabDev.models.enums.StatusFeatures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Participant_projetService {
    @Autowired
    private Participant_projetDao participantProjetDao;
    @Autowired
    private FonctionnaliteDao fonctionnaliteDao;

    //Méthode pour transformer un Participant_projet en Participant_projetDto
     private Participant_projetDto Participant_projetToParticipant_projetDto(Participant participant) {
        Participant_projetDto participantProjetDto = new Participant_projetDto();
        participantProjetDto.setProfil(participant.getProfil());
        participantProjetDto.setProjet(participant.getProjet());
        participantProjetDto.setContributeur(participant.getContributeur());
        participantProjetDto.setFonctionnalite(participant.getFonctionnalite());
        return participantProjetDto;
     }


    public List<Participant> afficherParticipantProjet() {
        return participantProjetDao.findAll();
    }
    public Participant ajouterParticipant(Participant_projetDto participantProjet){

        Participant participant = new Participant();
        //
        participant.setProfil(participantProjet.getProfil());
        participant.setProjet(participantProjet.getProjet());
        participant.setContributeur(participantProjet.getContributeur());
        //
        return participantProjetDao.save(participant);
    }
    //Méthode pour reserver une fonctionnalité à un participant
    public Participant_projetDto reserverFonctionnalite(int idParticipant, int idFonctionnalite) {
        Participant participant = participantProjetDao.findById(idParticipant).orElseThrow(() -> new RuntimeException("Participant non trouvé"));
        Fonctionnalite fonctionnalite = fonctionnaliteDao.findById(idFonctionnalite).orElseThrow(() -> new RuntimeException("Fonctionnalite non trouvée"));
        if(fonctionnalite.getStatusFeatures() != StatusFeatures.A_FAIRE) {
            throw new RuntimeException("La fonctionnalité est déjà réservée");
        }
        participant.setFonctionnalite(fonctionnalite);
        participantProjetDao.save(participant);



        return Participant_projetToParticipant_projetDto(participant);
    }

}
