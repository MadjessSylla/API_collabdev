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
    @Autowired
    private CommentaireDao commentaireDao ;


    public List<Commentaire> afficherCommentaire() {
        return commentaireDao.findAll();
    }

    public Commentaire ajouterCommentaire(CommentaireDto commentaire){

        Commentaire commentaire1 = new Commentaire();
        //
        commentaire1.setContenu(commentaire.getContenu());
        commentaire1.setDateCreation(commentaire.getDate());
        commentaire1.setParticipant(commentaire.getParticipant());

        return commentaireDao.save(commentaire1);
    }


}
