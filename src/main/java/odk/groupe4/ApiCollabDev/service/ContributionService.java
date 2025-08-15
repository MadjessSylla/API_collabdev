package odk.groupe4.ApiCollabDev.service;

import jakarta.transaction.Transactional;
import odk.groupe4.ApiCollabDev.dao.*;
import odk.groupe4.ApiCollabDev.dto.*;
import odk.groupe4.ApiCollabDev.models.*;
import odk.groupe4.ApiCollabDev.models.enums.ContributionStatus;
import odk.groupe4.ApiCollabDev.models.enums.FeaturesStatus;
import odk.groupe4.ApiCollabDev.models.enums.ParticipantProfil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContributionService {

    // Déclaration des DAO et services nécessaires pour gérer les contributions, participants, fonctionnalités, badges et notifications
    private final ContributionDao contributionDao;
    private final ParticipantDao participantDao;
    private final FonctionnaliteDao fonctionnaliteDao;
    private final ContributeurDao contributeurDao;
    private final ParametreCoinDao parametreCoinDao;
    private final BadgeDao badgeDao;
    private final BadgeContributeurDao badgeContributeurDao;
    private final NotificationService notificationService;
    private final ProjetDao projetDao;

    // Injection des dépendances via constructeur
    @Autowired
    public ContributionService(ContributionDao contributionDao,
                               ParticipantDao participantDao,
                               FonctionnaliteDao fonctionnaliteDao,
                               ContributeurDao contributeurDao,
                               ParametreCoinDao parametreCoinDao,
                               BadgeDao badgeDao,
                               BadgeContributeurDao badgeContributeurDao,
                               NotificationService notificationService,
                               ProjetDao projetDao) {
        this.contributionDao = contributionDao;
        this.participantDao = participantDao;
        this.fonctionnaliteDao = fonctionnaliteDao;
        this.contributeurDao = contributeurDao;
        this.parametreCoinDao = parametreCoinDao;
        this.badgeDao = badgeDao;
        this.badgeContributeurDao = badgeContributeurDao;
        this.notificationService = notificationService;
        this.projetDao = projetDao;
    }

    /**
     * Récupère la liste des contributions filtrées par statut.
     * Si le statut est null, retourne toutes les contributions.
     *
     * @param status Le statut des contributions à filtrer (optionnel)
     * @return Liste de DTO des contributions correspondantes
     */
    public List<ContributionDto> afficherLaListeDesContribution(ContributionStatus status) {
        List<Contribution> contributions;
        if (status != null) {
            contributions = contributionDao.findByStatus(status);
        } else {
            contributions = contributionDao.findAll();
        }
        return contributions.stream()
                .map(this::ContributionDaoToContributionDto)
                .collect(Collectors.toList());
    }

    public List<ContributionDto> afficherContributionsParProjetEtStatus(int projetId, ContributionStatus status) {
        // Récupérer la liste des participants associés au projet donné
        List<Participant> participantsDuProjet = participantDao.findByProjetId(projetId);

        // Récupérer toutes les contributions dont le participant fait partie de ce projet
        List<Contribution> contributions;

        if (status != null) {
            // Filtrer par statut en plus
            contributions = contributionDao.findByParticipantInAndStatus(participantsDuProjet, status);
        } else {
            // Sans filtre de statut
            contributions = contributionDao.findByParticipantIn(participantsDuProjet);
        }

        // Transformer en DTO
        return contributions.stream()
                .map(this::ContributionDaoToContributionDto)
                .collect(Collectors.toList());
    }

    /**
     * Récupère toutes les contributions validées des participants d'un projet donné.
     *
     * @param projetId ID du projet
     * @return Liste des contributions validées avec leurs URLs
     * @throws IllegalArgumentException si le projet n'existe pas
     */
    public List<ContributionValideeDto> getContributionsValideesParProjet(int projetId) {
        // Vérifier que le projet existe
        if (!projetDao.existsById(projetId)) {
            throw new IllegalArgumentException("Projet avec ID " + projetId + " non trouvé");
        }

        // Récupérer tous les participants du projet
        List<Participant> participantsDuProjet = participantDao.findByProjetId(projetId);

        // Récupérer toutes les contributions validées de ces participants
        List<Contribution> contributionsValidees = contributionDao.findByParticipantInAndStatus(
                participantsDuProjet,
                ContributionStatus.VALIDE
        );

        // Mapper vers le DTO spécialisé
        return contributionsValidees.stream()
                .map(this::mapToContributionValideeDto)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une Contribution en ContributionValideeDto.
     */
    private ContributionValideeDto mapToContributionValideeDto(Contribution contribution) {
        return ContributionValideeDto.builder()
                .id(contribution.getId())
                .titre(contribution.getTitre())
                .description(contribution.getDescription())
                .lienUrl(contribution.getLienUrl())
                .fileUrl(contribution.getFileUrl())
                .dateSoumission(contribution.getDateSoumission())
                .fonctionnaliteTitre(contribution.getFonctionnalite().getTitre())
                .participantNom(contribution.getParticipant().getContributeur().getNom())
                .participantPrenom(contribution.getParticipant().getContributeur().getPrenom())
                .build();
    }

    /**
     * Récupère une contribution par son ID.
     *
     * @param id ID de la contribution
     * @return DTO de la contribution correspondante
     * @throws RuntimeException si la contribution n'existe pas
     */
    public ContributionResponseDto getContributionById(int id) {
        Contribution contribution = contributionDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Contribution non trouvée avec l'ID: " + id));
        return mapToResponseDto(contribution);
    }

    /**
     * Soumet une nouvelle contribution associée à une fonctionnalité et un participant.
     * Gère l'upload du fichier si fourni.
     * Initialise la contribution avec le statut ENVOYE et la date de soumission actuelle.
     *
     * @param idFonctionnalite ID de la fonctionnalité concernée
     * @param idParticipant ID du participant soumettant la contribution
     * @param contribution Données de la contribution soumise
     * @param fichier Fichier optionnel à uploader
     * @return DTO de la contribution enregistrée
     * @throws IllegalArgumentException si le participant ou la fonctionnalité n'existe pas
     */
    public ContributionResponseDto soumettreContribution(int idFonctionnalite, int idParticipant,
                                                         ContributionSoumiseDto contribution,
                                                         MultipartFile fichier) {
        Participant participant = participantDao.findById(idParticipant)
                .orElseThrow(() -> new IllegalArgumentException("Participant non trouvé"));

        Fonctionnalite fonctionnalite = fonctionnaliteDao.findById(idFonctionnalite)
                .orElseThrow(() -> new IllegalArgumentException("Fonctionnalité non trouvée"));

        Contribution newContribution = new Contribution();
        newContribution.setTitre(contribution.getTitre());
        newContribution.setDescription(contribution.getDescription());
        newContribution.setLienUrl(contribution.getLienUrl());

        // Gérer l'upload du fichier si fourni
        if (fichier != null && !fichier.isEmpty()) {
            try {
                String fileUrl = uploadContributionFile(fichier);
                newContribution.setFileUrl(fileUrl);
            } catch (IOException e) {
                throw new RuntimeException("Erreur lors du téléversement du fichier: " + e.getMessage());
            }
        }

        newContribution.setStatus(ContributionStatus.ENVOYE);
        newContribution.setDateSoumission(LocalDate.now());
        newContribution.setFonctionnalite(fonctionnalite);
        newContribution.setParticipant(participant);

        Contribution savedContribution = contributionDao.save(newContribution);
        return mapToResponseDto(savedContribution);
    }

    /**
     * Méthode pour uploader un fichier de contribution
     */
    private String uploadContributionFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("Le fichier est vide");
        }

        // Validation du type de fichier
        String contentType = file.getContentType();
        if (contentType != null && !isValidContributionFileType(contentType)) {
            throw new RuntimeException("Type de fichier non autorisé: " + contentType);
        }

        // Créer le dossier d'upload s'il n'existe pas
        Path uploadDir = Paths.get("uploads/contributions");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        // Générer un nom de fichier unique
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String uniqueFilename = System.currentTimeMillis() + "_" +
                (originalFilename != null ? originalFilename.replaceAll("[^a-zA-Z0-9.-]", "_") : "file" + fileExtension);

        Path filePath = uploadDir.resolve(uniqueFilename);

        // Sauvegarder le fichier
        Files.write(filePath, file.getBytes());

        // Retourner l'URL relative du fichier
        return "/uploads/contributions/" + uniqueFilename;
    }

    /**
     * Valide le type de fichier autorisé pour les contributions
     */
    private boolean isValidContributionFileType(String contentType) {
        return contentType.equals("application/pdf") ||
                contentType.equals("application/msword") ||
                contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document") ||
                contentType.equals("text/plain") ||
                contentType.equals("application/zip") ||
                contentType.equals("application/x-zip-compressed") ||
                contentType.startsWith("image/") ||
                contentType.startsWith("video/") ||
                contentType.startsWith("audio/");
    }

    /**
     * Récupère toutes les contributions d'un participant donné.
     *
     * @param participantId ID du participant
     * @return Liste des contributions sous forme de DTO
     * @throws IllegalArgumentException si le participant n'existe pas
     */
    public List<ContributionDto> getContributionsByParticipant(int participantId) {
        Participant participant = participantDao.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Participant avec ID " + participantId + " non trouvé"));

        List<Contribution> contributions = contributionDao.findByParticipant(participant);
        return contributions.stream()
                .map(this::ContributionDaoToContributionDto)
                .collect(Collectors.toList());
    }

    /**
     * Récupère toutes les contributions associées à une fonctionnalité spécifique.
     *
     * @param fonctionnaliteId ID de la fonctionnalité
     * @return Liste des contributions sous forme de DTO
     * @throws RuntimeException si la fonctionnalité n'existe pas
     */
    public List<ContributionDto> getContributionsByFonctionnalite(int fonctionnaliteId) {
        if (!fonctionnaliteDao.existsById(fonctionnaliteId)) {
            throw new RuntimeException("Fonctionnalité non trouvée avec l'ID: " + fonctionnaliteId);
        }

        List<Contribution> contributions = contributionDao.findByFonctionnaliteId(fonctionnaliteId);
        return contributions.stream()
                .map(this::ContributionDaoToContributionDto)
                .collect(Collectors.toList());
    }

    /**
     * Valide ou rejette une contribution selon la décision d'un gestionnaire.
     * Seuls les participants ayant le profil GESTIONNAIRE peuvent effectuer cette opération.
     * En cas de validation, récompense le participant avec des coins, met à jour le statut de la fonctionnalité,
     * et assigne des badges si les critères sont remplis.
     * En cas de rejet, supprime le fichier associé s'il existe.
     * Envoie également une notification au participant concernant la décision.
     *
     * Cette méthode est transactionnelle pour garantir la cohérence des données.
     *
     * @param contributionId ID de la contribution à valider ou rejeter
     * @param newStatus Nouveau statut (VALIDE ou REJETE)
     * @param gestionnaireId ID du gestionnaire effectuant la validation
     * @return DTO de la contribution mise à jour
     * @throws IllegalArgumentException si la contribution, gestionnaire n'existent pas ou si le profil est invalide
     */
    @Transactional
    public ContributionResponseDto MiseAJourStatutContribution(int contributionId, ContributionStatus newStatus, int gestionnaireId) {
        Contribution contribution = contributionDao.findById(contributionId)
                .orElseThrow(() -> new IllegalArgumentException("Contribution avec ID " + contributionId + " non trouvée"));

        Participant gestionnaire = participantDao.findById(gestionnaireId)
                .orElseThrow(() -> new IllegalArgumentException("Gestionnaire avec ID " + gestionnaireId + " non trouvé"));

        if (!gestionnaire.getProfil().equals(ParticipantProfil.GESTIONNAIRE)) {
            throw new IllegalArgumentException("Seul un gestionnaire peut mettre à jour le statut d'une contribution");
        }

        contribution.setStatus(newStatus);
        contribution.setGestionnaire(gestionnaire);

        if (newStatus == ContributionStatus.VALIDE) {
            recompenseCoins(contribution.getParticipant());  // Attribution des coins au participant
            MiseAJourStatutFonctionnalite(contribution.getFonctionnalite()); // Passage de la fonctionnalité au statut TERMINÉ
            assignerBadges(contribution.getParticipant()); // Attribution éventuelle de badges
        } else if (newStatus == ContributionStatus.REJETE) {
            // Supprimer le fichier associé lors du rejet
            supprimerFichierContribution(contribution.getFileUrl());
        }

        // Envoi de notification au participant selon la décision prise
        if (newStatus == ContributionStatus.VALIDE || newStatus == ContributionStatus.REJETE) {
            Participant participant = contribution.getParticipant();
            String sujet = newStatus == ContributionStatus.VALIDE
                    ? "Contribution validée"
                    : "Contribution rejetée";
            String message = newStatus == ContributionStatus.VALIDE
                    ? "Votre contribution pour la fonctionnalité '" + contribution.getFonctionnalite().getTitre() + "' a été validée."
                    : "Votre contribution pour la fonctionnalité '" + contribution.getFonctionnalite().getTitre() + "' a été rejetée.";

            notificationService.createNotification(
                    participant.getContributeur(),
                    sujet,
                    message
            );
        }

        Contribution savedContribution = contributionDao.save(contribution);
        return mapToResponseDto(savedContribution);
    }

    /**
     * Supprime le fichier physique d'une contribution rejetée
     */
    private void supprimerFichierContribution(String fileUrl) {
        if (fileUrl != null && !fileUrl.isEmpty()) {
            try {
                // Extraire le chemin du fichier à partir de l'URL
                // Supposons que l'URL soit de la forme "/uploads/contributions/filename"
                if (fileUrl.startsWith("/uploads/contributions/")) {
                    String filename = fileUrl.substring("/uploads/contributions/".length());
                    Path filePath = Paths.get("uploads/contributions", filename);

                    if (Files.exists(filePath)) {
                        Files.delete(filePath);
                        System.out.println("Fichier supprimé: " + filePath);
                    }
                }
            } catch (IOException e) {
                System.err.println("Erreur lors de la suppression du fichier " + fileUrl + ": " + e.getMessage());
                // Ne pas faire échouer la transaction pour une erreur de suppression de fichier
            }
        }
    }

    /**
     * Ajoute des coins de récompense au contributeur associé à un participant donné,
     * selon la configuration de coins pour l'événement "CONTRIBUTION_VALIDEE".
     *
     * @param participant Participant qui reçoit les coins
     */
    private void recompenseCoins(Participant participant) {
        ParametreCoin coinConfig = parametreCoinDao.findByTypeEvenementLien("CONTRIBUTION_VALIDEE")
                .orElseThrow(() -> new IllegalStateException("Coin configuration pour CONTRIBUTION_VALIDEE non trouvée"));

        Contributeur contributeur = participant.getContributeur();
        contributeur.setTotalCoin(contributeur.getTotalCoin() + coinConfig.getValeur());
        contributeurDao.save(contributeur);
    }

    /**
     * Met à jour le statut d'une fonctionnalité en le passant à TERMINÉ.
     *
     * @param fonctionnalite La fonctionnalité à mettre à jour
     */
    private void MiseAJourStatutFonctionnalite(Fonctionnalite fonctionnalite) {
        if (fonctionnalite != null) {
            fonctionnalite.setStatusFeatures(FeaturesStatus.TERMINE);
            fonctionnaliteDao.save(fonctionnalite);
        }
    }

    /**
     * Attribue des badges à un contributeur en fonction du nombre total de contributions validées.
     * Compte toutes les contributions validées du contributeur à travers toutes ses participations.
     * Si le contributeur atteint le seuil pour un badge non encore attribué,
     * le badge est attribué, les coins de récompense associés sont ajoutés,
     * et une notification est envoyée.
     *
     * @param participant Participant dont le contributeur recevra les badges
     */
    private void assignerBadges(Participant participant) {
        Contributeur contributeur = participant.getContributeur();

        // Compter toutes les contributions validées du contributeur à travers toutes ses participations
        int nombreValidationTotal = 0;
        for (Participant p : contributeur.getParticipations()) {
            nombreValidationTotal += contributionDao.findByParticipantIdAndStatus(p.getId(), ContributionStatus.VALIDE).size();
        }

        // Liste des badges disponibles triée par nombre de contributions requises
        List<Badge> badgesDisponibles = badgeDao.findAllOrderByNombreContributionAsc();

        for (Badge badge : badgesDisponibles) {
            if (nombreValidationTotal >= badge.getNombreContribution()) {
                // Vérifie si le contributeur possède déjà ce badge
                boolean hasBadge = badgeContributeurDao.findByContributeurIdAndBadgeId(contributeur.getId(), badge.getId()).isPresent();

                if (!hasBadge) {
                    // Attribution du badge au contributeur
                    BadgeContributeur badgeContributeur = new BadgeContributeur();
                    badgeContributeur.setBadge(badge);
                    badgeContributeur.setContributeur(contributeur);
                    badgeContributeur.setDateAcquisition(LocalDate.now());
                    badgeContributeurDao.save(badgeContributeur);

                    // Attribution des coins de récompense au contributeur
                    contributeur.setTotalCoin(contributeur.getTotalCoin() + badge.getCoin_recompense());
                    contributeurDao.save(contributeur);

                    // Notification au contributeur
                    notificationService.createNotification(
                            contributeur,
                            "Nouveau badge obtenu !",
                            "Félicitations ! Vous avez obtenu le badge " + badge.getType() +
                                    " pour avoir atteint " + badge.getNombreContribution() + " contributions validées. " +
                                    "Vous recevez " + badge.getCoin_recompense() + " coins en récompense !"
                    );

                    System.out.println("Badge " + badge.getType() + " attribué au contributeur " + contributeur.getId());
                }
            }
        }
    }

    /**
     * Convertit une entité Contribution en DTO simple.
     *
     * @param contribution Entité Contribution à convertir
     * @return DTO correspondant
     */
    private ContributionDto ContributionDaoToContributionDto(Contribution contribution) {
        ContributionDto contributionDto = new ContributionDto();
        contributionDto.setId(contribution.getId());
        contributionDto.setTitre(contribution.getTitre());
        contributionDto.setDescription(contribution.getDescription());
        contributionDto.setLienUrl(contribution.getLienUrl());
        contributionDto.setFileUrl(contribution.getFileUrl());
        contributionDto.setStatus(contribution.getStatus());
        contributionDto.setDateSoumission(contribution.getDateSoumission());
        contributionDto.setFonctionnaliteId(contribution.getFonctionnalite().getId());
        contributionDto.setParticipantId(contribution.getParticipant().getId());
        if (contribution.getGestionnaire() != null) {
            contributionDto.setGestionnaireId(contribution.getGestionnaire().getId());
        }
        return contributionDto;
    }

    /**
     * Convertit une entité Contribution en DTO détaillé pour la réponse API.
     *
     * @param contribution Entité Contribution à convertir
     * @return DTO détaillé avec informations sur la fonctionnalité, participant et gestionnaire
     */
    private ContributionResponseDto mapToResponseDto(Contribution contribution) {
        return new ContributionResponseDto(
                contribution.getId(),
                contribution.getTitre(),
                contribution.getDescription(),
                contribution.getLienUrl(),
                contribution.getFileUrl(),
                contribution.getStatus(),
                contribution.getDateSoumission(),
                contribution.getFonctionnalite().getTitre(),
                contribution.getParticipant().getContributeur().getNom() + " " + contribution.getParticipant().getContributeur().getPrenom(),
                contribution.getGestionnaire() != null ?
                        contribution.getGestionnaire().getContributeur().getNom() + " " + contribution.getGestionnaire().getContributeur().getPrenom() :
                        null
        );
    }
}
