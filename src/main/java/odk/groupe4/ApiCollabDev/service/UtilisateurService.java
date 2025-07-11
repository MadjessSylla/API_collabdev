package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.ContributeurDao;
import odk.groupe4.ApiCollabDev.dao.ParametreCoinDao;
import odk.groupe4.ApiCollabDev.dao.UtilisateurDao;
import odk.groupe4.ApiCollabDev.dto.ContributeurDto;
import odk.groupe4.ApiCollabDev.dto.LoginResponseDto;
import odk.groupe4.ApiCollabDev.dto.UtilisateurDto;
import odk.groupe4.ApiCollabDev.dto.UtilisateurResponseDto;
import odk.groupe4.ApiCollabDev.models.Administrateur;
import odk.groupe4.ApiCollabDev.models.Contributeur;
import odk.groupe4.ApiCollabDev.models.ParametreCoin;
import odk.groupe4.ApiCollabDev.models.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UtilisateurService {

    private final UtilisateurDao utilisateurDao;
    private final ContributeurDao contributeurDao;
    private final ParametreCoinDao parametreCoinDao;

    @Autowired
    public UtilisateurService(UtilisateurDao utilisateurDao, ContributeurDao contributeurDao, ParametreCoinDao parametreCoinDao) {
        this.utilisateurDao = utilisateurDao;
        this.contributeurDao = contributeurDao;
        this.parametreCoinDao = parametreCoinDao;
    }

    public UtilisateurResponseDto inscrire(ContributeurDto dto) {
        Optional<Utilisateur> existingUser = utilisateurDao.findByEmail(dto.getEmail());
        if (existingUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cet email est déjà utilisé.");
        }

        Optional<Contributeur> existTelephone = contributeurDao.findByTelephone(dto.getTelephone());
        if (existTelephone.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ce numéro est déjà utilisé.");
        }

        ParametreCoin soldeCoin = parametreCoinDao.findByTypeEvenementLien("INSCRIPTION")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paramètre de coin non trouvé pour l'inscription."));

        Contributeur contributeur = new Contributeur();
        contributeur.setNom(dto.getNom());
        contributeur.setPrenom(dto.getPrenom());
        contributeur.setTelephone(dto.getTelephone());
        contributeur.setEmail(dto.getEmail());
        contributeur.setPassword(dto.getPassword());
        contributeur.setTotalCoin(soldeCoin.getValeur());
        contributeur.setPointExp(10);
        contributeur.setActif(true);

        Utilisateur savedUser = utilisateurDao.save(contributeur);
        return mapToUtilisateurResponseDto(savedUser);
    }

    public LoginResponseDto connecter(UtilisateurDto utilisateurDto) {
        Optional<Utilisateur> utilisateurOpt = utilisateurDao.findByEmail(utilisateurDto.getEmail());
        if (utilisateurOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utilisateur non trouvé.");
        }

        Utilisateur utilisateur = utilisateurOpt.get();
        if (!utilisateurDto.getPassword().equals(utilisateur.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Mot de passe incorrect.");
        }

        if (!utilisateur.isActif()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Compte désactivé.");
        }

        return mapToLoginResponseDto(utilisateur);
    }

    public UtilisateurResponseDto getProfile(int id) {
        Utilisateur utilisateur = utilisateurDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + id));
        return mapToUtilisateurResponseDto(utilisateur);
    }

    public void changerMotDePasse(int id, String ancienMotDePasse, String nouveauMotDePasse) {
        Utilisateur utilisateur = utilisateurDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + id));

        if (!utilisateur.getPassword().equals(ancienMotDePasse)) {
            throw new IllegalArgumentException("Ancien mot de passe incorrect");
        }

        if (nouveauMotDePasse == null || nouveauMotDePasse.length() < 6) {
            throw new IllegalArgumentException("Le nouveau mot de passe doit contenir au moins 6 caractères");
        }

        utilisateur.setPassword(nouveauMotDePasse);
        utilisateurDao.save(utilisateur);
    }

    private UtilisateurResponseDto mapToUtilisateurResponseDto(Utilisateur utilisateur) {
        String type = utilisateur instanceof Contributeur ? "CONTRIBUTEUR" : "ADMINISTRATEUR";
        
        if (utilisateur instanceof Contributeur contributeur) {
            return new UtilisateurResponseDto(
                    utilisateur.getId(),
                    utilisateur.getEmail(),
                    type,
                    utilisateur.isActif(),
                    contributeur.getNom(),
                    contributeur.getPrenom(),
                    contributeur.getTelephone(),
                    contributeur.getPointExp(),
                    contributeur.getTotalCoin()
            );
        } else {
            return new UtilisateurResponseDto(
                    utilisateur.getId(),
                    utilisateur.getEmail(),
                    type,
                    utilisateur.isActif(),
                    null,
                    null,
                    null,
                    null,
                    null
            );
        }
    }

    private LoginResponseDto mapToLoginResponseDto(Utilisateur utilisateur) {
        String type = utilisateur instanceof Contributeur ? "CONTRIBUTEUR" : "ADMINISTRATEUR";
        String nom = null;
        String prenom = null;
        
        if (utilisateur instanceof Contributeur contributeur) {
            nom = contributeur.getNom();
            prenom = contributeur.getPrenom();
        }

        return new LoginResponseDto(
                utilisateur.getId(),
                utilisateur.getEmail(),
                type,
                nom,
                prenom,
                utilisateur.isActif(),
                "Connexion réussie"
        );
    }
}
