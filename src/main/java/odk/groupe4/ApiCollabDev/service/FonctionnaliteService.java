package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.FonctionnaliteDao;
import odk.groupe4.ApiCollabDev.dao.ProjetDao;
import odk.groupe4.ApiCollabDev.dto.FonctionnaliteNewDto;
import odk.groupe4.ApiCollabDev.dto.FonctionnaliteResponseDto;
import odk.groupe4.ApiCollabDev.models.Fonctionnalite;
import odk.groupe4.ApiCollabDev.models.Projet;
import odk.groupe4.ApiCollabDev.models.enums.FeaturesStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FonctionnaliteService {
    private final FonctionnaliteDao fonctionnaliteDao;
    private final ProjetDao projetDao;

    @Autowired
    public FonctionnaliteService(FonctionnaliteDao fonctionnaliteDao, ProjetDao projetDao) {
        this.fonctionnaliteDao = fonctionnaliteDao;
        this.projetDao = projetDao;
    }

    public List<FonctionnaliteResponseDto> getAllFonctionnalites(FeaturesStatus status) {
        List<Fonctionnalite> fonctionnalites;
        if (status != null) {
            fonctionnalites = fonctionnaliteDao.findByStatusFeatures(status);
        } else {
            fonctionnalites = fonctionnaliteDao.findAll();
        }
        return fonctionnalites.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public FonctionnaliteResponseDto getFonctionnaliteById(int id) {
        Fonctionnalite fonctionnalite = fonctionnaliteDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Fonctionnalité non trouvée avec l'ID: " + id));
        return mapToResponseDto(fonctionnalite);
    }

    public FonctionnaliteResponseDto ajouterFonctionnalite(int idProjet, FonctionnaliteNewDto dto) {
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'ID: " + idProjet));

        Fonctionnalite fonctionnalite = new Fonctionnalite();
        fonctionnalite.setTitre(dto.getTitre());
        fonctionnalite.setContenu(dto.getContenu());
        fonctionnalite.setStatusFeatures(FeaturesStatus.A_FAIRE);
        fonctionnalite.setProjet(projet);

        Fonctionnalite savedFonctionnalite = fonctionnaliteDao.save(fonctionnalite);
        return mapToResponseDto(savedFonctionnalite);
    }

    public FonctionnaliteResponseDto updateFonctionnalite(int id, FonctionnaliteNewDto dto) {
        Fonctionnalite fonctionnalite = fonctionnaliteDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Fonctionnalité non trouvée avec l'ID: " + id));

        fonctionnalite.setTitre(dto.getTitre());
        fonctionnalite.setContenu(dto.getContenu());

        Fonctionnalite updatedFonctionnalite = fonctionnaliteDao.save(fonctionnalite);
        return mapToResponseDto(updatedFonctionnalite);
    }

    public FonctionnaliteResponseDto updateStatus(int id, FeaturesStatus status) {
        Fonctionnalite fonctionnalite = fonctionnaliteDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Fonctionnalité non trouvée avec l'ID: " + id));

        fonctionnalite.setStatusFeatures(status);
        Fonctionnalite updatedFonctionnalite = fonctionnaliteDao.save(fonctionnalite);
        return mapToResponseDto(updatedFonctionnalite);
    }

    public List<FonctionnaliteResponseDto> getFonctionnalitesByProjet(int idProjet) {
        if (!projetDao.existsById(idProjet)) {
            throw new RuntimeException("Projet non trouvé avec l'ID: " + idProjet);
        }
        
        List<Fonctionnalite> fonctionnalites = fonctionnaliteDao.findByProjetId(idProjet);
        return fonctionnalites.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public void deleteFonctionnalite(int id) {
        if (!fonctionnaliteDao.existsById(id)) {
            throw new RuntimeException("Fonctionnalité non trouvée avec l'ID: " + id);
        }
        fonctionnaliteDao.deleteById(id);
    }

    private FonctionnaliteResponseDto mapToResponseDto(Fonctionnalite fonctionnalite) {
        return new FonctionnaliteResponseDto(
                fonctionnalite.getId(),
                fonctionnalite.getTitre(),
                fonctionnalite.getContenu(),
                fonctionnalite.getStatusFeatures(),
                fonctionnalite.getProjet().getTitre(),
                fonctionnalite.getParticipant() != null ? 
                    fonctionnalite.getParticipant().getContributeur().getNom() + " " + 
                    fonctionnalite.getParticipant().getContributeur().getPrenom() : null,
                fonctionnalite.getParticipant() != null ? 
                    fonctionnalite.getParticipant().getContributeur().getEmail() : null
        );
    }
}
