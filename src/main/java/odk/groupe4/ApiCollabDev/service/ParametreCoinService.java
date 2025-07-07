package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.AdministrateurDao;
import odk.groupe4.ApiCollabDev.dao.ParametreCoinDao;
import odk.groupe4.ApiCollabDev.dto.ParametreCoinDto;
import odk.groupe4.ApiCollabDev.models.Administrateur;
import odk.groupe4.ApiCollabDev.models.ParametreCoin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParametreCoinService {
    private final ParametreCoinDao parametreCoinDao;
    private final AdministrateurDao administrateurDao;

    @Autowired
    public ParametreCoinService(ParametreCoinDao parametreCoinDao, AdministrateurDao administrateurDao) {
        this.parametreCoinDao = parametreCoinDao;
        this.administrateurDao = administrateurDao;
    }

    //Méthode pour créer un nouveau paramètre de coin
    public ParametreCoin creerParametreCoin(int idAdmin, ParametreCoinDto dto) {
        // Vérifier si l'administrateur existe (vous pouvez ajouter une vérification ici si nécessaire)
        Administrateur admin = administrateurDao.findById(idAdmin)
                .orElseThrow(() -> new IllegalArgumentException("Administrateur non trouvé avec l'ID : " + idAdmin));
        // Convertir le DTO en modèle ParametreCoin
        ParametreCoin parametreCoin = dtoToModel(dto);
        // Assigner l'administrateur au paramètre de coin
        parametreCoin.setAdministrateur(admin);
        // Enregistrer le paramètre de coin dans la base de données
        return parametreCoinDao.save(dtoToModel(dto));
    }

    // Méthode pour obtenir tous les paramètres de coins
    public List<ParametreCoin> obtenirTousLesParametresCoins() {
        // Récupérer la liste de ParametreCoinDto depuis le DAO
        List<ParametreCoinDto> parametreCoinDtos = parametreCoinDao.findAllByOrderByIdAsc();

        // Convertir la liste de ParametreCoinDto en liste de ParametreCoin
        return parametreCoinDtos.stream()
                .map(this::dtoToModel)
                .toList();
    }
    // Méthode pour modifier un paramètre de coin
    public ParametreCoin modifierParametreCoin(int id, ParametreCoinDto dto) {
        // Vérifier si le paramètre de coin existe
        if (!parametreCoinDao.existsById(id)) {
            throw new IllegalArgumentException("Paramètre de coin non trouvé avec l'ID : " + id);
        }
        // Convertir le DTO en modèle
        ParametreCoin parametreCoin = dtoToModel(dto);
        parametreCoin.setId(id); // Assigner l'ID pour la mise à jour
        return parametreCoinDao.save(parametreCoin);
    }
    // Méthode pour supprimer un paramètre de coin
    public String supprimerParametreCoin(int id) {
        // Vérifier si le paramètre de coin existe
        if (!parametreCoinDao.existsById(id)) {
            return "Paramètre de coin non trouvé avec l'ID : " + id;
        }
        // Supprimer le paramètre de coin
        parametreCoinDao.deleteById(id);
        return "Paramètre de coin supprimé avec succès.";
    }

    // Convertir ParametreCoinDto en ParametreCoin
    private ParametreCoin dtoToModel(ParametreCoinDto dto) {
        ParametreCoin parametreCoin = new ParametreCoin();
        parametreCoin.setNom(dto.getNom());
        parametreCoin.setDescription(dto.getDescription());
        parametreCoin.setTypeEvenementLien(dto.getTypeEvenementLien());
        parametreCoin.setValeur(dto.getValeur());
        return parametreCoin;
    }

    // Convertir ParametreCoin en ParametreCoinDto
    private ParametreCoinDto modelToDto(ParametreCoin parametreCoin) {
        ParametreCoinDto dto = new ParametreCoinDto();
        dto.setNom(parametreCoin.getNom());
        dto.setDescription(parametreCoin.getDescription());
        dto.setTypeEvenementLien(parametreCoin.getTypeEvenementLien());
        dto.setValeur(parametreCoin.getValeur());
        return dto;
    }
}
