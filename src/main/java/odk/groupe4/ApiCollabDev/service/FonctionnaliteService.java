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

    /**
     * Ajoute une nouvelle fonctionnalité à un projet.
     *
     * @param idProjet l'ID du projet auquel la fonctionnalité est ajoutée
     * @param dto      les données de la nouvelle fonctionnalité
     * @return un DTO de la fonctionnalité ajoutée
     */
    public FonctionnaliteResponseDto ajouterFonctionnalite(int idProjet, FonctionnaliteNewDto dto) {
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'ID: " + idProjet));

        Fonctionnalite fonctionnalite = new Fonctionnalite();
        fonctionnalite.setTitre(dto.getTitre());
        fonctionnalite.setContenu(dto.getContenu());
        fonctionnalite.setStatusFeatures(FeaturesStatus.A_FAIRE);
        fonctionnalite.setDateEcheance(dto.getDateEcheance());
        fonctionnalite.setExigences(dto.getExigences());
        fonctionnalite.setCriteresAcceptation(dto.getCriteresAcceptation());
        fonctionnalite.setImportance(dto.getImportance());
        fonctionnalite.setMotsCles(dto.getMotsCles());
        fonctionnalite.setProjet(projet);

        Fonctionnalite savedFonctionnalite = fonctionnaliteDao.save(fonctionnalite);
        return mapToResponseDto(savedFonctionnalite);
    }

    /**
     * Met à jour une fonctionnalité existante.
     *
     * @param id  l'ID de la fonctionnalité à mettre à jour
     * @param dto les nouvelles données de la fonctionnalité
     * @return un DTO de la fonctionnalité mise à jourw
     */
    public FonctionnaliteResponseDto updateFonctionnalite(int id, FonctionnaliteNewDto dto) {
        Fonctionnalite fonctionnalite = fonctionnaliteDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Fonctionnalité non trouvée avec l'ID: " + id));

        fonctionnalite.setTitre(dto.getTitre());
        fonctionnalite.setContenu(dto.getContenu());
        fonctionnalite.setDateEcheance(dto.getDateEcheance());
        fonctionnalite.setExigences(dto.getExigences());
        fonctionnalite.setCriteresAcceptation(dto.getCriteresAcceptation());
        fonctionnalite.setImportance(dto.getImportance());
        fonctionnalite.setMotsCles(dto.getMotsCles());

        Fonctionnalite updatedFonctionnalite = fonctionnaliteDao.save(fonctionnalite);
        return mapToResponseDto(updatedFonctionnalite);
    }

    /**
     * Récupère toutes les fonctionnalités d'un projet spécifique.
     *
     * @param idProjet l'ID du projet dont on veut récupérer les fonctionnalités
     * @return une liste de DTO de fonctionnalités
     */
    public List<FonctionnaliteResponseDto> getFonctionnalitesByProjet(int idProjet) {
        // Vérifie si le projet existe
        if (!projetDao.existsById(idProjet)) {
            throw new RuntimeException("Projet non trouvé avec l'ID: " + idProjet);
        }

        // Récupère les fonctionnalités associées au projet
        List<Fonctionnalite> fonctionnalites = fonctionnaliteDao.findByProjetId(idProjet);
        // Retourne les fonctionnalités sous forme de DTOs
        return fonctionnalites.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Supprime une fonctionnalité par son ID.
     *
     * @param id l'ID de la fonctionnalité à supprimer
     */
    public void deleteFonctionnalite(int id) {
        // Vérifie si la fonctionnalité existe avant de la supprimer
        if (!fonctionnaliteDao.existsById(id)) {
            throw new RuntimeException("Fonctionnalité non trouvée avec l'ID: " + id);
        }
        // Supprime la fonctionnalité de la base de données
        fonctionnaliteDao.deleteById(id);
    }

    /**
     * Mappe une entité Fonctionnalite à un DTO FonctionnaliteResponseDto.
     *
     * @param fonctionnalite l'entité à mapper
     * @return le DTO correspondant
     */
    private FonctionnaliteResponseDto mapToResponseDto(Fonctionnalite fonctionnalite) {
        return new FonctionnaliteResponseDto(
                fonctionnalite.getId(),
                fonctionnalite.getTitre(),
                fonctionnalite.getContenu(),
                fonctionnalite.getStatusFeatures(),
                fonctionnalite.getDateEcheance(),
                fonctionnalite.getExigences(),
                fonctionnalite.getCriteresAcceptation(),
                fonctionnalite.getImportance(),
                fonctionnalite.getMotsCles(),
                fonctionnalite.getProjet().getTitre(),
                fonctionnalite.getParticipant() != null ?
                        fonctionnalite.getParticipant().getContributeur().getPrenom() + " " +
                                fonctionnalite.getParticipant().getContributeur().getNom() : null,
                fonctionnalite.getParticipant() != null ?
                        fonctionnalite.getParticipant().getContributeur().getEmail() : null
        );
    }
}
