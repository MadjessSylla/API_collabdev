package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.ContributeurDao;
import odk.groupe4.ApiCollabDev.dao.UtilisateurDao;
import odk.groupe4.ApiCollabDev.dto.ContributeurDto;
import odk.groupe4.ApiCollabDev.models.Contributeur;
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
    @Autowired
    private ContributeurDao contributeurDao;

    public Utilisateur inscrire(ContributeurDto dto) {
        Optional<Utilisateur> existingUser = utilisateurDao.findByEmail(dto.getEmail());
        if (existingUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cet email est déjà utilisé.");
        }
        Optional<Contributeur> existtelephone = contributeurDao.findByTelephone(dto.getTelephone());
        if (existtelephone.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cet numéro est déjà utilisé.");
        }

        Contributeur contributeur = new Contributeur();
        contributeur.setEmail(dto.getEmail());
        contributeur.setPassword(dto.getPassword());
        contributeur.setNiveauProfil(NiveauProfil.DEBUTANT);

        contributeur.setNom(dto.getNom());
        contributeur.setPrenom(dto.getPrenom());
        contributeur.setTelephone(dto.getTelephone());
        contributeur.setTotalCoin(100);
        contributeur.setPointExp(10);
        return utilisateurDao.save(contributeur);
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