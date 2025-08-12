package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.ContributeurDao;
import odk.groupe4.ApiCollabDev.dao.ParametreCoinDao;
import odk.groupe4.ApiCollabDev.dao.UtilisateurDao;
import odk.groupe4.ApiCollabDev.dto.*;
import odk.groupe4.ApiCollabDev.models.Contributeur;
import odk.groupe4.ApiCollabDev.models.ParametreCoin;
import odk.groupe4.ApiCollabDev.models.Projet;
import odk.groupe4.ApiCollabDev.models.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContributeurService {
    private final ContributeurDao contributeurDao;
    private final ParametreCoinDao parametreCoinDao;
    private final UtilisateurDao utilisateurDao;

    @Autowired
    public ContributeurService(ContributeurDao contributeurDao,
                               ParametreCoinDao parametreCoinDao,
                               UtilisateurDao utilisateurDao) {
        this.contributeurDao = contributeurDao;
        this.parametreCoinDao = parametreCoinDao;
        this.utilisateurDao = utilisateurDao;
    }

    // Affiche tous les details des contributeurs
    public List<ContributeurResponseDto> getAllContributeurs() {
        return contributeurDao.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    // Affiche un contributeur par son id
    public ContributeurResponseDto getContributeurById(int id) {
        Contributeur contributeur = contributeurDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Contributeur non trouvé avec l'ID: " + id));
        return mapToResponseDto(contributeur);
    }

    public ContributeurSoldeDto afficherSoldeContributeur(int id) {
        if (!contributeurDao.existsById(id)) {
            throw new RuntimeException("Contributeur non trouvé avec l'ID: " + id);
        }
        return contributeurDao.totalCoinContributeur(id);
    }

    public ContributeurResponseDto ajouterContributeur(ContributeurDto dto) {
        // Vérifier l'unicité de l'email
        Optional<Utilisateur> existingUser = utilisateurDao.findByEmail(dto.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Cet email est déjà utilisé.");
        }

        // Vérifier l'unicité du téléphone
        if (contributeurDao.findByTelephone(dto.getTelephone()).isPresent()) {
            throw new IllegalArgumentException("Ce numéro de téléphone est déjà utilisé.");
        }

        // Récupérer le paramètre de coin pour l'inscription
        ParametreCoin soldeCoin = parametreCoinDao.findByTypeEvenementLien("INSCRIPTION")
                .orElseThrow(() -> new RuntimeException("Paramètre de coin non trouvé pour l'inscription."));

        Contributeur contributeur = new Contributeur();
        contributeur.setNom(dto.getNom());
        contributeur.setPrenom(dto.getPrenom());
        contributeur.setTelephone(dto.getTelephone());
        contributeur.setEmail(dto.getEmail());
        contributeur.setPassword(dto.getPassword());
        contributeur.setBiographie(dto.getBiographie()); // Nouveau champ pris en compte
        contributeur.setTotalCoin(soldeCoin.getValeur());
        contributeur.setPointExp(10);
        contributeur.setActif(true);

        Contributeur savedContributeur = contributeurDao.save(contributeur);
        return mapToResponseDto(savedContributeur);
    }

    public ContributeurResponseDto updateContributeur(int id, ContributeurDto dto) {
        Contributeur contributeur = contributeurDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Contributeur non trouvé avec l'ID: " + id));

        contributeur.setNom(dto.getNom());
        contributeur.setPrenom(dto.getPrenom());
        contributeur.setTelephone(dto.getTelephone());
        contributeur.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            contributeur.setPassword(dto.getPassword());
        }
        // Mise à jour de la bibliographie si fournie
        contributeur.setBiographie(dto.getBiographie());

        Contributeur updatedContributeur = contributeurDao.save(contributeur);
        return mapToResponseDto(updatedContributeur);
    }

    public ContributeurResponseDto updateContributeurStatus(int id, boolean actif) {
        Contributeur contributeur = contributeurDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Contributeur non trouvé avec l'ID: " + id));

        contributeur.setActif(actif);
        Contributeur savedContributeur = contributeurDao.save(contributeur);
        return mapToResponseDto(savedContributeur);
    }

    private ContributeurResponseDto mapToResponseDto(Contributeur contributeur) {
        return new ContributeurResponseDto(
                contributeur.getId(),
                contributeur.getNom(),
                contributeur.getPrenom(),
                contributeur.getTelephone(),
                contributeur.getEmail(),
                contributeur.getPointExp(),
                contributeur.getTotalCoin(),
                contributeur.getBiographie(),
                contributeur.getPhotoProfil(),
                contributeur.isActif()
        );
    }

    }
