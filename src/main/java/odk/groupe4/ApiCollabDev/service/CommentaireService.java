package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.CommentaireDao;
import odk.groupe4.ApiCollabDev.dto.CommentaireDto;
import odk.groupe4.ApiCollabDev.models.Commentaire;
import odk.groupe4.ApiCollabDev.models.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentaireService {

    private CommentaireDao commentaireDao ;

    @Autowired
    public CommentaireService(CommentaireDao commentaireDao){
        this.commentaireDao = commentaireDao;
    }

    // SIMPO POST
    public Commentaire creerCommentaire(CommentaireDto dto){

        // Initialisation d'un nouvel objet commentaire à partir du DTO
        Commentaire commentaire = new Commentaire();

        commentaire.setContenu(dto.getContenu());
        commentaire.setContribution(dto.getContribution());
        commentaire.setParticipant(dto.getParticipant());

        return commentaireDao.save(commentaire);
    }

    //SIMPO GET BY ID
    public Commentaire aficherUnCommentaire(int id_commentaire){
        return commentaireDao.findById(id_commentaire)
                .orElseThrow(() -> new RuntimeException("Commentaire not found with id: \" + idCommentaire"));
    }

    //SIMPO GET ALL
    public List<Commentaire> afficherCommentaire(){

        return commentaireDao.findAll();
    }
    //SIMPO PUT

    //SIMPO DELETE
    public String supprimerCommentaire(int id_commentaire){
        // Vérification de l'existence du commentaire avant la suppression
        if (!commentaireDao.existsById(id_commentaire)){
            throw new RuntimeException("Commentaire not found with id : " + id_commentaire);
        }
        //suppression du commentaire
        commentaireDao.deleteById(id_commentaire);
        return "commentaire avec l'id " + id_commentaire + "a ete supprimer avec succes.";
    }
}
