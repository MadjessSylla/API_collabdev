package odk.groupe4.ApiCollabDev.service;

import jakarta.transaction.Transactional;
import odk.groupe4.ApiCollabDev.dao.CommentaireDao;
import odk.groupe4.ApiCollabDev.dao.ParticipantDao;
import odk.groupe4.ApiCollabDev.dao.ProjetDao;
import odk.groupe4.ApiCollabDev.dto.CommentaireRequestDto;
import odk.groupe4.ApiCollabDev.dto.CommentaireResponseDto;
import odk.groupe4.ApiCollabDev.models.Commentaire;
import odk.groupe4.ApiCollabDev.models.Contributeur;
import odk.groupe4.ApiCollabDev.models.Participant;
import odk.groupe4.ApiCollabDev.models.Projet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentaireService {

    private final CommentaireDao commentaireDao;
    private final ParticipantDao participantDao;
    private final ProjetDao projetDao;

    @Autowired
    public CommentaireService(CommentaireDao commentaireDao, ParticipantDao participantDao, ProjetDao projetDao) {
        this.commentaireDao = commentaireDao;
        this.participantDao = participantDao;
        this.projetDao = projetDao;
    }

    @Transactional
    public CommentaireResponseDto creerCommentaire(int participantId, int projetId, CommentaireRequestDto dto) {
        Participant participant = participantDao.findById(participantId)
                .orElseThrow(() -> new RuntimeException("Participant non trouvé avec l'id : " + participantId));

        Projet projet = projetDao.findById(projetId)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'id : " + projetId));

        Commentaire commentaire = new Commentaire();
        commentaire.setContenu(dto.getContenu());
        commentaire.setCreationDate(LocalDate.now());
        commentaire.setAuteur(participant);
        commentaire.setProjet(projet);

        if (dto.getParentId() != null) {
            Commentaire parent = commentaireDao.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Commentaire parent non trouvé avec l'id : " + dto.getParentId()));
            commentaire.setCommentaireParent(parent);
        }

        Commentaire saved = commentaireDao.save(commentaire);
        return mapToResponseDto(saved, true);
    }

    @Transactional
    public List<CommentaireResponseDto> afficherCommentairesRacinesParParticipant(int participantId) {
        Participant participant = participantDao.findById(participantId)
                .orElseThrow(() -> new RuntimeException("Participant non trouvé avec l'id : " + participantId));

        List<Commentaire> racines = commentaireDao.findByAuteurAndCommentaireParentIsNull(participant);
        racines.sort(Comparator.comparing(Commentaire::getCreationDate));

        return racines.stream()
                .map(c -> mapToResponseDto(c, true))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<CommentaireResponseDto> afficherCommentairesRacinesParProjet(int projetId) {
        Projet projet = projetDao.findById(projetId)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'id : " + projetId));

        List<Commentaire> racines = commentaireDao.findByProjetAndCommentaireParentIsNull(projet);
        racines.sort(Comparator.comparing(Commentaire::getCreationDate));

        return racines.stream()
                .map(c -> mapToResponseDto(c, true))
                .collect(Collectors.toList());
    }

    @Transactional
    public String supprimerCommentaire(int idCommentaire) {
        Commentaire commentaire = commentaireDao.findById(idCommentaire)
                .orElseThrow(() -> new RuntimeException("Commentaire non trouvé avec l'id : " + idCommentaire));
        commentaireDao.delete(commentaire);
        return "Commentaire supprimé avec succès.";
    }

    private CommentaireResponseDto mapToResponseDto(Commentaire commentaire, boolean includeReplies) {
        CommentaireResponseDto dto = new CommentaireResponseDto();
        dto.setId(commentaire.getId());
        dto.setContenu(commentaire.getContenu());
        dto.setCreationDate(commentaire.getCreationDate() != null ? commentaire.getCreationDate().toString() : null);

        if (commentaire.getAuteur() != null) {
            dto.setAuteurId(commentaire.getAuteur().getId());
            Contributeur contrib = commentaire.getAuteur().getContributeur();
            if (contrib != null) {
                dto.setAuteurNomComplet((contrib.getPrenom() + " " + contrib.getNom()).trim());
                dto.setAuteurPhotoProfilUrl(contrib.getPhotoProfil());
            }
        }

        dto.setParentId(commentaire.getCommentaireParent() != null ? commentaire.getCommentaireParent().getId() : null);

        if (includeReplies) {
            dto.setReponses(
                    commentaire.getReponses().stream()
                            .filter(rep -> rep.getId() != commentaire.getId())
                            .sorted(Comparator.comparing(Commentaire::getCreationDate))
                            .map(child -> mapToResponseDto(child, true))
                            .collect(Collectors.toList())
            );
        }
        return dto;
    }
}

