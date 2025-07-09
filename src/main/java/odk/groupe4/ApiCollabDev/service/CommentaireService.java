package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.CommentaireDao;
import odk.groupe4.ApiCollabDev.dao.ParticipantDao;
import odk.groupe4.ApiCollabDev.dto.CommentaireDto;
import odk.groupe4.ApiCollabDev.models.Commentaire;
import odk.groupe4.ApiCollabDev.models.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CommentaireService {

    private final  CommentaireDao commentaireDao ;
    private final ParticipantDao participantDao;

    // Ensemble pour stocker les observateurs (participants abonnés aux notifications)
    private final Set<Participant> observers = new HashSet<>();


    @Autowired
    public CommentaireService(CommentaireDao commentaireDao, ParticipantDao participantDao) {
        this.commentaireDao = commentaireDao;
        this.participantDao = participantDao;
    }

    // SIMPO POST
    public Commentaire creerCommentaire(int id, CommentaireDto dto){

        // Vérification de l'existence du participant
        Participant participant = participantDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Participant non trouvé avec l'id : " + id));

        // Initialisation d'un nouvel objet commentaire à partir du DTO
        Commentaire commentaire = new Commentaire();

        commentaire.setContenu(dto.getContenu());
        commentaire.setDate(dto.getDate());
        commentaire.setAuteur(participant);

        return commentaireDao.save(commentaire);
    }

    //SIMPO GET ALL
    public List<Commentaire> afficherCommentaire(){

        return commentaireDao.findAll();
    }
    //SIMPO DELETE
    public String supprimerCommentaire(int id_commentaire){
        // Vérification de l'existence du commentaire avant la suppression
        if (!commentaireDao.existsById(id_commentaire)){
            throw new RuntimeException("Commentaire non trouvé avec l'id : " + id_commentaire);
        }
        //suppression du commentaire
        commentaireDao.deleteById(id_commentaire);
        return "commentaire avec l'id " + id_commentaire + "a ete supprimer avec succes.";
    }

}
