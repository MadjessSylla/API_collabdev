package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.CommentaireDao;
import odk.groupe4.ApiCollabDev.dao.ParticipantDao;
import odk.groupe4.ApiCollabDev.dto.CommentaireRequestDto;
import odk.groupe4.ApiCollabDev.dto.CommentaireResponseDto;
import odk.groupe4.ApiCollabDev.models.Commentaire;
import odk.groupe4.ApiCollabDev.models.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

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
    public CommentaireResponseDto creerCommentaire(int id, CommentaireRequestDto dto){
        // Vérification de l'existence du participant
        Participant participant = participantDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Participant non trouvé avec l'id : " + id));

        // Initialisation d'un nouvel objet commentaire à partir du DTO
        Commentaire commentaire = new Commentaire();

        // Remplissage des champs du commentaire
        commentaire.setContenu(dto.getContenu());
        commentaire.setDate(dto.getDate());
        commentaire.setAuteur(participant);
        // Enregistrement du commentaire dans la base de données
        commentaireDao.save(commentaire);

        return mapToResponseDto(commentaire);
    }

    /**
     * Affiche les commentaires d'un participant spécifique.
     *
     * @param id l'identifiant du participant
     * @return un ensemble de commentaires associés au participant
     */
    public Set<CommentaireResponseDto> afficherCommentaireParParticipant(int id){
        // Vérification de l'existence du participant
        Participant participant = participantDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Participant non trouvé avec l'id : " + id));
        // Récupération des commentaires associés au participant
        Set<Commentaire> commentaires = commentaireDao.findByAuteur(participant);
        if (commentaires.isEmpty()) {
            throw new RuntimeException("Aucun commentaire trouvé pour le participant avec l'id : " + id);
        }
        // Conversion des commentaires en DTOs
        return commentaires.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toSet());
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

    /**
     * Mappe un objet Commentaire en un objet CommentaireResponseDto.
     *
     * @param commentaire l'objet Commentaire à mapper
     * @return un objet CommentaireResponseDto contenant les informations du commentaire
     */
    public CommentaireResponseDto mapToResponseDto(Commentaire commentaire) {
        // Création d'un objet CommentaireResponseDto avec les informations du commentaire
        return new CommentaireResponseDto(
                commentaire.getAuteur().getContributeur().getPrenom() + " " + commentaire.getAuteur().getContributeur().getNom(),
                commentaire.getContenu(),
                commentaire.getDate().toString()
        );
    }

}
