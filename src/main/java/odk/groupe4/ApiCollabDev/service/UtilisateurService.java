package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.ContributeurDao;
import odk.groupe4.ApiCollabDev.dao.ParametreCoinDao;
import odk.groupe4.ApiCollabDev.dao.UtilisateurDao;
import odk.groupe4.ApiCollabDev.dto.ContributeurDto;
import odk.groupe4.ApiCollabDev.models.Contributeur;
import odk.groupe4.ApiCollabDev.models.ParametreCoin;
import odk.groupe4.ApiCollabDev.models.Utilisateur;
import odk.groupe4.ApiCollabDev.dto.UtilisateurDto;
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
     * Inscrit un nouveau contributeur.
     *
     * @param dto les informations du contributeur à inscrire
     * @return l'utilisateur inscrit
     * @throws ResponseStatusException si l'email ou le téléphone est déjà utilisé
     */
    public Utilisateur inscrire(ContributeurDto dto) {
        // Récupération des informations du contributeur
        Optional<Utilisateur> existingUser = utilisateurDao.findByEmail(dto.getEmail());
        // Vérification de l'unicité de l'email
        if (existingUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cet email est déjà utilisé.");
        }
        // Récupération des informations du contributeur par téléphone
        Optional<Contributeur> existTelephone = contributeurDao.findByTelephone(dto.getTelephone());
        // Vérification de l'unicité du téléphone
        if (existTelephone.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cet numéro est déjà utilisé.");
        }
        // Récupération du paramètre de coin pour l'inscription
        ParametreCoin soldeCoin = parametreCoinDao.findByTypeEvenementLien("INSCRIPTION")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paramètre de coin non trouvé pour l'inscription."));
        // Création du contributeur
        Contributeur contributeur = new Contributeur();
        contributeur.setNom(dto.getNom()); // Nom du contributeur
        contributeur.setPrenom(dto.getPrenom()); // Prénom du contributeur
        contributeur.setTelephone(dto.getTelephone()); // Téléphone du contributeur
        contributeur.setEmail(dto.getEmail()); // Email du contributeur
        contributeur.setPassword(dto.getPassword()); // Mot de passe du contributeur
        contributeur.setTotalCoin(soldeCoin.getValeur()); // Solde de Coin initial du contributeur
        contributeur.setPointExp(10); // Points d'expérience initial du contributeur
        // Sauvegarde du contributeur dans la base de données
        return utilisateurDao.save(contributeur);
    }
    /**
     * Connecte un utilisateur en vérifiant ses identifiants.
     *
     * @param utilisateurDto les informations de connexion de l'utilisateur
     * @return l'utilisateur connecté
     * @throws ResponseStatusException si l'utilisateur n'est pas trouvé ou si le mot de passe est incorrect
     */
    public Utilisateur connecter(UtilisateurDto utilisateurDto) {
        // Récupération de l'utilisateur par email
        Optional<Utilisateur> utilisateurOpt = utilisateurDao.findByEmail(utilisateurDto.getEmail());
        // Vérification si l'utilisateur existe
        if (utilisateurOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utilisateur non trouvé.");
        }
        // Récupération de l'utilisateur
        Utilisateur utilisateur = utilisateurOpt.get();
        // Vérification du mot de passe
        if (!utilisateurDto.getPassword().equals(utilisateur.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Mot de passe incorrect.");
        }
        // Renvoi de l'utilisateur connecté
        return utilisateur;
    }
}