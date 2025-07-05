package odk.groupe4.ApiCollabDev.controllers;

import odk.groupe4.ApiCollabDev.dto.CommentaireDto;
import odk.groupe4.ApiCollabDev.models.Commentaire;
import odk.groupe4.ApiCollabDev.service.CommentaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commentaire")
public class CommentaireController {
    private final CommentaireService commentaireService;

    @Autowired
    public CommentaireController(CommentaireService commentaireService){
        this.commentaireService = commentaireService;
    }

    // SIMPO POST
    @PostMapping("/participant/{id}")
    public Commentaire creerCommentaires(@PathVariable("id") int id, @RequestBody CommentaireDto commentaire){
        return commentaireService.creerCommentaire(id, commentaire);
    }

    //SIMPO GET ALL
    @GetMapping
    public List<Commentaire> afficherCommentaire(CommentaireDto commentaire){
        return commentaireService.afficherCommentaire();
    }

    // SIMPO DELETE
   @DeleteMapping("/{id}")
    public String supprimerCommentaire(@PathVariable("id") int id_commentaire) {
        return commentaireService.supprimerCommentaire(id_commentaire);
    }

}
