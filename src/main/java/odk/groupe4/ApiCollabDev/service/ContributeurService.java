package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.ContributeurDao;
import odk.groupe4.ApiCollabDev.dao.ParametreCoinDao;
import odk.groupe4.ApiCollabDev.dao.UtilisateurDao;
import odk.groupe4.ApiCollabDev.dto.ContributeurRequestDto;
import odk.groupe4.ApiCollabDev.dto.ContributeurResponseDto;
import odk.groupe4.ApiCollabDev.dto.ContributeurSoldeDto;
import odk.groupe4.ApiCollabDev.models.Contributeur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContributeurService {
    private final ContributeurDao contributeurDao;
    private final ParametreCoinDao parametreCoinDao;
    private final UtilisateurDao utilisateurDao;

    @Autowired
    public ContributeurService(ContributeurDao contributeurDao, ParametreCoinDao parametreCoinDao, UtilisateurDao utilisateurDao) {
        this.contributeurDao = contributeurDao;
        this.parametreCoinDao = parametreCoinDao;
        this.utilisateurDao = utilisateurDao;
    }

    /** * Récupère tous les contributeurs.
     * @return Une liste de DTO de réponse contenant les informations des contributeurs.
     */
    public List<ContributeurResponseDto> getAllContributeurs() {
        // Mapper tous les contributeurs à des DTO de réponse
        return contributeurDao.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Récupère un contributeur par son ID.
     * @param id L'ID du contributeur à récupérer.
     * @return Un DTO de réponse contenant les informations du contributeur.
     */
    public ContributeurResponseDto getContributeurById(int id) {
        // Vérifier si le contributeur existe
        Contributeur contributeur = contributeurDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Contributeur non trouvé avec l'ID: " + id));
        // Mapper le contributeur à un DTO de réponse
        return mapToResponseDto(contributeur);
    }

    /**
     * Mettre à jour un contributeur existant.
     * @param id L'ID du contributeur à mettre à jour.
     * @param dto Le DTO contenant les nouvelles informations du contributeur.
     * @return Un DTO de réponse contenant les informations du contributeur mis à jour.
     */
    public ContributeurResponseDto updateContributeur(int id, ContributeurRequestDto dto) {
        // Vérifier si le contributeur existe
        Contributeur contributeur = contributeurDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Contributeur non trouvé avec l'ID: " + id));

        // Renseigner les informations du contributeur à partir du DTO
        contributeur.setNom(dto.getNom());
        contributeur.setPrenom(dto.getPrenom());
        contributeur.setTelephone(dto.getTelephone());
        contributeur.setEmail(dto.getEmail());

        // Sauvegarder le contributeur mis à jour
        Contributeur updatedContributeur = contributeurDao.save(contributeur);
        // Mapper le contributeur mis à jour à un DTO de réponse
        return mapToResponseDto(updatedContributeur);
    }

    /**
     * Affiche le solde d'un contributeur.
     * @param id L'ID du contributeur dont on veut afficher le solde.
     * @return Un DTO contenant le solde du contributeur.
     */
    public ContributeurSoldeDto afficherSoldeContributeur(int id) {
        // Vérifier si le contributeur existe
        if (!contributeurDao.existsById(id)) {
            throw new RuntimeException("Contributeur non trouvé avec l'ID: " + id);
        }
        // Récupérer le solde total du contributeur
        return contributeurDao.totalCoinContributeur(id);
    }

    /**
     * Désactive un contributeur.
     * @param id L'ID du contributeur à désactiver.
     * @return Un DTO de réponse contenant les informations du contributeur désactivé.
     */
    public ContributeurResponseDto deactivateContributeur(int id) {
        // Vérifier si le contributeur existe
        Contributeur contributeur = contributeurDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Contributeur non trouvé avec l'ID: " + id));

        // Désactiver le contributeur
        contributeur.setActif(false);
        // Sauvegarder le contributeur désactivé
        Contributeur savedContributeur = contributeurDao.save(contributeur);
        // Mapper le contributeur désactivé à un DTO de réponse
        return mapToResponseDto(savedContributeur);
    }

    /**
     * Réactive un contributeur.
     * @param id L'ID du contributeur à réactiver.
     * @return Un DTO de réponse contenant les informations du contributeur réactivé.
     */
    public ContributeurResponseDto activateContributeur(int id) {
        // Vérifier si le contributeur existe
        Contributeur contributeur = contributeurDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Contributeur non trouvé avec l'ID: " + id));

        // Réactiver le contributeur
        contributeur.setActif(true);
        // Sauvegarder le contributeur réactivé
        Contributeur savedContributeur = contributeurDao.save(contributeur);
        // Mapper le contributeur réactivé à un DTO de réponse
        return mapToResponseDto(savedContributeur);
    }

    /**
     * Mappe un contributeur à un DTO de réponse.
     * @param contributeur Le contributeur à mapper.
     * @return Un DTO de réponse contenant les informations du contributeur.
     */
    private ContributeurResponseDto mapToResponseDto(Contributeur contributeur) {
        return new ContributeurResponseDto(
                contributeur.getId(),
                contributeur.getNom(),
                contributeur.getPrenom(),
                contributeur.getTelephone(),
                contributeur.getEmail(),
                contributeur.getPointExp(),
                contributeur.getTotalCoin(),
                contributeur.isActif()
        );
    }
}
