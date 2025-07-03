package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.Participant_projetDao;
import odk.groupe4.ApiCollabDev.dto.Participant_projetDto;
import odk.groupe4.ApiCollabDev.models.Participant;
import odk.groupe4.ApiCollabDev.models.Projet;
import odk.groupe4.ApiCollabDev.models.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Participant_projetService {
    @Autowired
    private Participant_projetDao participantProjetDao;


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
}
