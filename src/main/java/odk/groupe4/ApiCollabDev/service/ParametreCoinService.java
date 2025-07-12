package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.AdministrateurDao;
import odk.groupe4.ApiCollabDev.dao.ParametreCoinDao;
import odk.groupe4.ApiCollabDev.dto.ParametreCoinDto;
import odk.groupe4.ApiCollabDev.dto.ParametreCoinResponseDto;
import odk.groupe4.ApiCollabDev.models.Administrateur;
import odk.groupe4.ApiCollabDev.models.ParametreCoin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParametreCoinService {
    private final ParametreCoinDao parametreCoinDao;
    private final AdministrateurDao administrateurDao;

    @Autowired
    public ParametreCoinService(ParametreCoinDao parametreCoinDao, AdministrateurDao administrateurDao) {
        this.parametreCoinDao = parametreCoinDao;
        this.administrateurDao = administrateurDao;
    }

    /**
     * Crée un nouveau paramètre de coin.
     *
     * @param idAdmin l'ID de l'administrateur créateur
     * @param dto les informations du paramètre de coin à créer
     * @return les détails du paramètre de coin créé
     */
    public ParametreCoinResponseDto creerParametreCoin(int idAdmin, ParametreCoinDto dto) {
        // Vérification de l'existence de l'administrateur
        Administrateur admin = administrateurDao.findById(idAdmin)
                .orElseThrow(() -> new IllegalArgumentException("Administrateur non trouvé avec l'ID : " + idAdmin));

        // Création du paramètre de coin
        ParametreCoin parametreCoin = new ParametreCoin();
        parametreCoin.setNom(dto.getNom());
        parametreCoin.setDescription(dto.getDescription());
        parametreCoin.setTypeEvenementLien(dto.getTypeEvenementLien());
        parametreCoin.setValeur(dto.getValeur());
        parametreCoin.setAdministrateur(admin);

        // Enregistrement du paramètre de coin
        ParametreCoin savedParametre = parametreCoinDao.save(parametreCoin);
        // Retourne les détails du paramètre de coin créé
        return mapToResponseDto(savedParametre);
    }

    /**
     * Récupère un paramètre de coin par son ID.
     *
     * @param id l'ID du paramètre de coin à récupérer
     * @return les détails du paramètre de coin trouvé
     */
    public ParametreCoinResponseDto getParametreCoinById(int id) {
        // Recherche du paramètre de coin par ID
        ParametreCoin parametreCoin = parametreCoinDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paramètre de coin non trouvé avec l'ID : " + id));
        // Retourne les détails du paramètre de coin trouvé
        return mapToResponseDto(parametreCoin);
    }

    /**
     * Récupère tous les paramètres de coins.
     *
     * @return la liste des paramètres de coins
     */
    public List<ParametreCoinResponseDto> obtenirTousLesParametresCoins() {
        // Récupération de tous les paramètres de coins
        return parametreCoinDao.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Modifie un paramètre de coin existant.
     *
     * @param id l'ID du paramètre de coin à modifier
     * @param dto les nouvelles informations du paramètre de coin
     * @return les détails du paramètre de coin modifié
     */
    public ParametreCoinResponseDto modifierParametreCoin(int id, ParametreCoinDto dto) {
        // Vérification de l'existence du paramètre de coin
        ParametreCoin parametreCoin = parametreCoinDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paramètre de coin non trouvé avec l'ID : " + id));

        // Mise à jour des informations du paramètre de coin
        parametreCoin.setNom(dto.getNom());
        parametreCoin.setDescription(dto.getDescription());
        parametreCoin.setTypeEvenementLien(dto.getTypeEvenementLien());
        parametreCoin.setValeur(dto.getValeur());

        // Enregistrement du paramètre de coin mis à jour
        ParametreCoin updatedParametre = parametreCoinDao.save(parametreCoin);
        // Retourne les détails du paramètre de coin mis à jour
        return mapToResponseDto(updatedParametre);
    }

    /**
     * Supprime un paramètre de coin par son ID.
     *
     * @param id l'ID du paramètre de coin à supprimer
     */
    public void supprimerParametreCoin(int id) {
        // Vérification de l'existence du paramètre de coin
        if (!parametreCoinDao.existsById(id)) {
            throw new IllegalArgumentException("Paramètre de coin non trouvé avec l'ID : " + id);
        }
        // Suppression du paramètre de coin
        parametreCoinDao.deleteById(id);
    }

    /**
     * Mappe un objet ParametreCoin en ParametreCoinResponseDto.
     *
     * @param parametreCoin l'objet ParametreCoin à mapper
     * @return l'objet ParametreCoinResponseDto correspondant
     */
    private ParametreCoinResponseDto mapToResponseDto(ParametreCoin parametreCoin) {
        return new ParametreCoinResponseDto(
                parametreCoin.getId(),
                parametreCoin.getNom(),
                parametreCoin.getDescription(),
                parametreCoin.getTypeEvenementLien(),
                parametreCoin.getValeur(),
                parametreCoin.getAdministrateur() != null ? 
                    parametreCoin.getAdministrateur().getEmail() : null
        );
    }
}
