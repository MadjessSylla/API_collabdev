package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.UtilisateurDao;
import odk.groupe4.ApiCollabDev.models.Utilisateur;
import odk.groupe4.ApiCollabDev.dto.UtilisateurDto;
import odk.groupe4.ApiCollabDev.models.enums.NiveauProfil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurDao utilisateurDao;

    public Utilisateur inscrire(UtilisateurDto utilisateurDto) {
        Optional<Utilisateur> existingUser = utilisateurDao.findByEmail(utilisateurDto.getEmail());
        if (existingUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cet email est déjà utilisé.");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(utilisateurDto.getEmail());
        utilisateur.setPassword(utilisateurDto.getPassword());
        utilisateur.setNiveauProfil(NiveauProfil.DEBUTANT);
        return utilisateurDao.save(utilisateur);
    }

    public Utilisateur connecter(UtilisateurDto utilisateurDto) {
        Optional<Utilisateur> utilisateurOpt = utilisateurDao.findByEmail(utilisateurDto.getEmail());
        if (utilisateurOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utilisateur non trouvé.");
        }

        Utilisateur utilisateur = utilisateurOpt.get();

        if (!utilisateurDto.getPassword().equals(utilisateur.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Mot de passe incorrect.");
        }

        return utilisateur;
    }
}
