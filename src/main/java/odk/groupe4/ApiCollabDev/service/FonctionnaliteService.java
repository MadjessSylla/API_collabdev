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
        // Vérifie si le projet existe
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'ID: " + idProjet));

        // Crée une nouvelle fonctionnalité à partir du DTO
        Fonctionnalite fonctionnalite = new Fonctionnalite();
        // Remplit les champs de la fonctionnalité
        fonctionnalite.setTitre(dto.getTitre());
        fonctionnalite.setContenu(dto.getContenu());
        fonctionnalite.setStatusFeatures(FeaturesStatus.A_FAIRE);
        fonctionnalite.setProjet(projet);

        // Enregistre la fonctionnalité dans la base de données
        Fonctionnalite savedFonctionnalite = fonctionnaliteDao.save(fonctionnalite);
        // Retourne le DTO de la fonctionnalité ajoutée
        return mapToResponseDto(savedFonctionnalite);
    }

    /**
     * Met à jour une fonctionnalité existante.
     *
     * @param id  l'ID de la fonctionnalité à mettre à jour
     * @param dto les nouvelles données de la fonctionnalité
     * @return un DTO de la fonctionnalité mise à jour
     */
    public FonctionnaliteResponseDto updateFonctionnalite(int id, FonctionnaliteNewDto dto) {
        // Vérifie si la fonctionnalité existe
        Fonctionnalite fonctionnalite = fonctionnaliteDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Fonctionnalité non trouvée avec l'ID: " + id));

        // Met à jour les champs de la fonctionnalité
        fonctionnalite.setTitre(dto.getTitre());
        fonctionnalite.setContenu(dto.getContenu());

        // Enregistre la fonctionnalité mise à jour dans la base de données
        Fonctionnalite updatedFonctionnalite = fonctionnaliteDao.save(fonctionnalite);
        // Retourne le DTO de la fonctionnalité mise à jour
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
        // Retourne un DTO de fonctionnalité avec les informations nécessaires
        return new FonctionnaliteResponseDto(
                fonctionnalite.getId(), // ID de la fonctionnalité
                fonctionnalite.getTitre(), // Titre de la fonctionnalité
                fonctionnalite.getContenu(), // Contenu de la fonctionnalité
                fonctionnalite.getStatusFeatures(), // Statut de la fonctionnalité
                fonctionnalite.getProjet().getTitre(), // Titre du projet auquel la fonctionnalité est associée
                fonctionnalite.getParticipant() != null ? 
                    fonctionnalite.getParticipant().getContributeur().getPrenom() + " " +
                    fonctionnalite.getParticipant().getContributeur().getNom() : null, // Nom complet du participant
                fonctionnalite.getParticipant() != null ? 
                    fonctionnalite.getParticipant().getContributeur().getEmail() : null // Email du participant
        );
    }


    /**
     * Récupère toutes les fonctionnalités, filtrées par statut si spécifié.
     *
     * @param status le statut des fonctionnalités à récupérer, peut être null pour toutes
     * @return une liste de DTO de fonctionnalités
     */
   /* public List<FonctionnaliteResponseDto> getAllFonctionnalites(FeaturesStatus status) {
        // Si un statut est spécifié, on filtre les fonctionnalités par ce statut
        List<Fonctionnalite> fonctionnalites;
        if (status != null) {
            fonctionnalites = fonctionnaliteDao.findByStatusFeatures(status);
        } else {
            fonctionnalites = fonctionnaliteDao.findAll();
        }
        return fonctionnalites.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }*/

    /**
     * Récupère une fonctionnalité par son ID.
     *
     * @param id l'ID de la fonctionnalité à récupérer
     * @return un DTO de la fonctionnalité
     */
   /* public FonctionnaliteResponseDto getFonctionnaliteById(int id) {
        Fonctionnalite fonctionnalite = fonctionnaliteDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Fonctionnalité non trouvée avec l'ID: " + id));
        return mapToResponseDto(fonctionnalite);
    }*/

    /**
     * Met à jour le statut d'une fonctionnalité.
     *
     * @param id     l'ID de la fonctionnalité à mettre à jour
     * @param status le nouveau statut de la fonctionnalité
     * @return un DTO de la fonctionnalité mise à jour
     */
    /*public FonctionnaliteResponseDto updateStatus(int id, FeaturesStatus status) {
        // Vérifie si la fonctionnalité existe
        Fonctionnalite fonctionnalite = fonctionnaliteDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Fonctionnalité non trouvée avec l'ID: " + id));

        // Met à jour le statut de la fonctionnalité
        fonctionnalite.setStatusFeatures(status);
        Fonctionnalite updatedFonctionnalite = fonctionnaliteDao.save(fonctionnalite);
        return mapToResponseDto(updatedFonctionnalite);
    }*/

}
