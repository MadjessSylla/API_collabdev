package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.ContributeurDao;
import odk.groupe4.ApiCollabDev.dao.ParametreCoinDao;
import odk.groupe4.ApiCollabDev.dao.UtilisateurDao;
import odk.groupe4.ApiCollabDev.dto.ContributeurRequestDto;
import odk.groupe4.ApiCollabDev.dto.LoginResponseDto;
import odk.groupe4.ApiCollabDev.dto.UtilisateurDto;
import odk.groupe4.ApiCollabDev.dto.UtilisateurResponseDto;
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

    /**
     * Inscrit un nouvel utilisateur en tant que contributeur.
     *
     * @param dto les informations du contributeur à inscrire
     * @return les détails de l'utilisateur inscrit
     * @throws ResponseStatusException si l'email ou le téléphone est déjà utilisé
     */
    public UtilisateurResponseDto inscrire(ContributeurRequestDto dto) {
        // Vérifier si l'email ou le téléphone existe déjà
        Optional<Utilisateur> existingUser = utilisateurDao.findByEmail(dto.getEmail());
        if (existingUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cet email est déjà utilisé.");
        }
        // Vérifier si le téléphone existe déjà
        Optional<Contributeur> existTelephone = contributeurDao.findByTelephone(dto.getTelephone());
        if (existTelephone.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ce numéro est déjà utilisé.");
        }

        // Récupérer le solde de coin pour l'inscription
        ParametreCoin soldeCoin = parametreCoinDao.findByTypeEvenementLien("INSCRIPTION")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paramètre de coin non trouvé pour l'inscription."));

        // Créer un nouveau contributeur avec les informations fournies
        Contributeur contributeur = new Contributeur();
        contributeur.setNom(dto.getNom());
        contributeur.setPrenom(dto.getPrenom());
        contributeur.setTelephone(dto.getTelephone());
        contributeur.setEmail(dto.getEmail());
        contributeur.setPassword(dto.getPassword());
        contributeur.setTotalCoin(soldeCoin.getValeur());
        contributeur.setPointExp(10);
        contributeur.setActif(true);

        // Enregistrer le contributeur dans la base de données
        Utilisateur savedUser = utilisateurDao.save(contributeur);
        // Retourner les détails de l'utilisateur inscrit
        return mapToUtilisateurResponseDto(savedUser);
    }

    /**
     * Connecte un utilisateur en vérifiant ses informations d'identification.
     *
     * @param utilisateurDto les informations de connexion de l'utilisateur
     * @return les détails de l'utilisateur connecté
     * @throws ResponseStatusException si l'utilisateur n'est pas trouvé, le mot de passe est incorrect ou le compte est désactivé
     */
    public LoginResponseDto connecter(UtilisateurDto utilisateurDto) {
        // Vérifier si l'utilisateur existe avec l'email fourni
        Optional<Utilisateur> utilisateurOpt = utilisateurDao.findByEmail(utilisateurDto.getEmail());
        if (utilisateurOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utilisateur non trouvé.");
        }

        // Récupérer l'utilisateur
        Utilisateur utilisateur = utilisateurOpt.get();
        // Vérifier si le mot de passe correspond
        if (!utilisateurDto.getPassword().equals(utilisateur.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Mot de passe incorrect.");
        }

        // Vérifier si l'utilisateur est actif
        if (!utilisateur.isActif()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Compte désactivé.");
        }

        // Retourner les détails de l'utilisateur connecté
        return mapToLoginResponseDto(utilisateur);
    }

    /**
     * Récupère le profil d'un utilisateur par son ID.
     *
     * @param id l'ID de l'utilisateur
     * @return les détails du profil de l'utilisateur
     * @throws RuntimeException si l'utilisateur n'est pas trouvé
     */
    public UtilisateurResponseDto getProfile(int id) {
        // Vérifier si l'utilisateur existe avec l'ID fourni
        Utilisateur utilisateur = utilisateurDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + id));
        // Retourner les détails du profil de l'utilisateur
        return mapToUtilisateurResponseDto(utilisateur);
    }

    /**
     * Change le mot de passe d'un utilisateur.
     *
     * @param id l'ID de l'utilisateur
     * @param ancienMotDePasse l'ancien mot de passe de l'utilisateur
     * @param nouveauMotDePasse le nouveau mot de passe à définir
     * @throws RuntimeException si l'utilisateur n'est pas trouvé ou si l'ancien mot de passe est incorrect
     */
    public void changerMotDePasse(int id, String ancienMotDePasse, String nouveauMotDePasse) {
        // Vérifier si l'utilisateur existe avec l'ID fourni
        Utilisateur utilisateur = utilisateurDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + id));

        // Vérifier si l'ancien mot de passe correspond
        if (!utilisateur.getPassword().equals(ancienMotDePasse)) {
            throw new IllegalArgumentException("Ancien mot de passe incorrect");
        }

        // Vérifier si le nouveau mot de passe est valide
        if (nouveauMotDePasse == null || nouveauMotDePasse.length() < 6) {
            throw new IllegalArgumentException("Le nouveau mot de passe doit contenir au moins 6 caractères");
        }

        // Mettre à jour le mot de passe de l'utilisateur
        utilisateur.setPassword(nouveauMotDePasse);
        // Enregistrer les modifications de l'utilisateur
        utilisateurDao.save(utilisateur);
    }

    /**
     * Mappe un utilisateur à une réponse DTO.
     *
     * @param utilisateur l'utilisateur à mapper
     * @return la réponse DTO de l'utilisateur
     */
    private UtilisateurResponseDto mapToUtilisateurResponseDto(Utilisateur utilisateur) {
        // Déterminer le type d'utilisateur (CONTRIBUTEUR ou ADMINISTRATEUR)
        String type = utilisateur instanceof Contributeur ? "CONTRIBUTEUR" : "ADMINISTRATEUR";

        // Si l'utilisateur est un contributeur, récupérer ses détails
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
            // Si l'utilisateur est un administrateur, retourner les détails de l'administrateur
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
    /**
     * Mappe un utilisateur à une réponse de connexion.
     *
     * @param utilisateur l'utilisateur à mapper
     * @return la réponse de connexion
     */
    private LoginResponseDto mapToLoginResponseDto(Utilisateur utilisateur) {
        // Déterminer le type d'utilisateur (CONTRIBUTEUR ou ADMINISTRATEUR)
        String type = utilisateur instanceof Contributeur ? "CONTRIBUTEUR" : "ADMINISTRATEUR";
        // Initialiser nom et prénom à null
        String nom = null;
        String prenom = null;
        // Si l'utilisateur est un contributeur, récupérer son nom et prénom
        if (utilisateur instanceof Contributeur contributeur) {
            nom = contributeur.getNom();
            prenom = contributeur.getPrenom();
        }
        // Retourner la réponse de connexion
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
