package odk.groupe4.ApiCollabDev.controllers;

import odk.groupe4.ApiCollabDev.dao.CommentaireDao;
import odk.groupe4.ApiCollabDev.dto.CommentaireDto;
import odk.groupe4.ApiCollabDev.models.Commentaire;
import odk.groupe4.ApiCollabDev.service.CommentaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/api/commentaire")
public class CommentaireController {
    @Autowired
    private CommentaireService commentaireService;
    private CommentaireDao commentaireDao;

    @PostMapping
    public Commentaire creerCommentaire(@RequestBody CommentaireDto commentaire){
        return commentaireService.ajouterCommentaire(commentaire);
    }

}
