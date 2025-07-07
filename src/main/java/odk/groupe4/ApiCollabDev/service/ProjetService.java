package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.AdministrateurDao;
import odk.groupe4.ApiCollabDev.dao.ProjetDao;
import odk.groupe4.ApiCollabDev.models.Projet;
import odk.groupe4.ApiCollabDev.models.enums.StatusProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjetService {

    private final ProjetDao projetDao;
    private final NotificationService notificationService;
    private final AdministrateurDao administrateurDao;

    @Autowired
    private ProjetService (ProjetDao projetDao,
                                 NotificationService notificationService,
                                 AdministrateurDao administrateurDao) {
        this.projetDao = projetDao;
        this.notificationService = notificationService;
        this.administrateurDao = administrateurDao;
    }

    public Projet createProjet(Projet projet) {
        // Sauvegarde du projet
        Projet savedProjet = projetDao.save(projet);

        // Vérifier si le projet soumis est en attente
        if (savedProjet.getStatus() == StatusProject.EN_ATTENTE) {
            // Récupérer tous les administrateurs
            administrateurDao.findAll().forEach(administrateur -> {
                notificationService.createNotification(
                        administrateur, // Administrateur hérite d'Utilisateur
                        "Nouvelle idée de projet soumise",
                        "Un nouveau projet '" + savedProjet.getTitre() + "' a été soumis par " +
                                savedProjet.getCreateur().getNom() + " pour validation."
                );
            });
        }

        return savedProjet;
    }
}
