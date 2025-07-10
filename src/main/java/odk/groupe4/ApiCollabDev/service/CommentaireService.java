package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.CommentaireDao;
import odk.groupe4.ApiCollabDev.dao.ParticipantDao;
import odk.groupe4.ApiCollabDev.dto.CommentaireDto;
import odk.groupe4.ApiCollabDev.models.Commentaire;
import odk.groupe4.ApiCollabDev.models.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CommentaireService {

    private final CommentaireDao commentaireDao ;
    private final ParticipantDao participantDao;

    @Autowired
    public CommentaireService(CommentaireDao commentaireDao, ParticipantDao participantDao) {
        this.commentaireDao = commentaireDao;
        this.participantDao = participantDao;
    }

    /**
     * Crée un commentaire pour un participant donné.
     *
     * @param id  l'identifiant du participant
     * @param dto les données du commentaire à créer
     * @return le commentaire créé
     */
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

    /**
     * Affiche les commentaires d'un participant spécifique.
     *
     * @param id l'identifiant du participant
     * @return un ensemble de commentaires associés au participant
     */
    public Set<Commentaire> afficherCommentaireParParticipant(int id){
        // Vérification de l'existence du participant
        Participant participant = participantDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Participant non trouvé avec l'id : " + id));
        // Récupération des commentaires associés au participant
        Set<Commentaire> commentaires = commentaireDao.findByAuteur(participant);
        if (commentaires.isEmpty()) {
            throw new RuntimeException("Aucun commentaire trouvé pour le participant avec l'id : " + id);
        }
        return commentaires;
    }

    /** Supprime un commentaire par son identifiant.
     *
     * @param id_commentaire l'identifiant du commentaire à supprimer
     * @return un message de confirmation de la suppression
     */
    public String supprimerCommentaire(int id_commentaire){
        // Vérification de l'existence du commentaire
        Commentaire commentaire = commentaireDao.findById(id_commentaire)
                .orElseThrow(() -> new RuntimeException("Commentaire non trouvé avec l'id : " + id_commentaire));
        // Suppression du commentaire
        commentaireDao.delete(commentaire);
        return "Commentaire supprimé avec succès.";
    }

}
