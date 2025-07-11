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

    public ParametreCoinResponseDto creerParametreCoin(int idAdmin, ParametreCoinDto dto) {
        Administrateur admin = administrateurDao.findById(idAdmin)
                .orElseThrow(() -> new IllegalArgumentException("Administrateur non trouvé avec l'ID : " + idAdmin));
        
        ParametreCoin parametreCoin = new ParametreCoin();
        parametreCoin.setNom(dto.getNom());
        parametreCoin.setDescription(dto.getDescription());
        parametreCoin.setTypeEvenementLien(dto.getTypeEvenementLien());
        parametreCoin.setValeur(dto.getValeur());
        parametreCoin.setAdministrateur(admin);
        
        ParametreCoin savedParametre = parametreCoinDao.save(parametreCoin);
        return mapToResponseDto(savedParametre);
    }

    public ParametreCoinResponseDto getParametreCoinById(int id) {
        ParametreCoin parametreCoin = parametreCoinDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paramètre de coin non trouvé avec l'ID : " + id));
        return mapToResponseDto(parametreCoin);
    }

    public List<ParametreCoinResponseDto> obtenirTousLesParametresCoins() {
        return parametreCoinDao.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public ParametreCoinResponseDto modifierParametreCoin(int id, ParametreCoinDto dto) {
        ParametreCoin parametreCoin = parametreCoinDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paramètre de coin non trouvé avec l'ID : " + id));
        
        parametreCoin.setNom(dto.getNom());
        parametreCoin.setDescription(dto.getDescription());
        parametreCoin.setTypeEvenementLien(dto.getTypeEvenementLien());
        parametreCoin.setValeur(dto.getValeur());
        
        ParametreCoin updatedParametre = parametreCoinDao.save(parametreCoin);
        return mapToResponseDto(updatedParametre);
    }

    public void supprimerParametreCoin(int id) {
        if (!parametreCoinDao.existsById(id)) {
            throw new IllegalArgumentException("Paramètre de coin non trouvé avec l'ID : " + id);
        }
        parametreCoinDao.deleteById(id);
    }

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
