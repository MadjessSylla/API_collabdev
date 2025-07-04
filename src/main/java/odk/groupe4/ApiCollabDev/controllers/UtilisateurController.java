package odk.groupe4.ApiCollabDev.controllers;

import odk.groupe4.ApiCollabDev.dto.UtilisateurDto;
import odk.groupe4.ApiCollabDev.models.Utilisateur;
import odk.groupe4.ApiCollabDev.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/utilisateur")
public class UtilisateurController {
    @Autowired
    private UtilisateurService utilisateurService;
    @PostMapping ("/registre")
    public Utilisateur inscrire(@RequestBody UtilisateurDto utilisateurDto) {
        return utilisateurService.inscrire(utilisateurDto);
    }
    @PostMapping("/login")
    public Utilisateur connecter(@RequestBody UtilisateurDto utilisateurDto) {
        return utilisateurService.connecter(utilisateurDto);
    }

}
