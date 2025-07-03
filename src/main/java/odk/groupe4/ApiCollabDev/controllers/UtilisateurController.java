package odk.groupe4.ApiCollabDev.controllers;

import odk.groupe4.ApiCollabDev.dao.UtilisateurDao;
import odk.groupe4.ApiCollabDev.dto.UtilisateurDto;
import odk.groupe4.ApiCollabDev.models.Utilisateur;
import odk.groupe4.ApiCollabDev.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/utilisateur")
public class UtilisateurController {
    @Autowired
    private UtilisateurService utilisateurService;
    private UtilisateurDao utilisateurDao;

    @GetMapping
    public List<Utilisateur> afficherUtilisateur() {
        return utilisateurService.afficherUtilisateurs();
    }

    @PostMapping
    public Utilisateur creerUtilisateur(@RequestBody UtilisateurDto utilisateur){
        return utilisateurService.ajouterUtilisateur(utilisateur);
    }

}
