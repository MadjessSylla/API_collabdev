package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.ContributionDao;
import odk.groupe4.ApiCollabDev.dao.FonctionnaliteDao;
import odk.groupe4.ApiCollabDev.dao.Participant_projetDao;
import odk.groupe4.ApiCollabDev.dao.ProjetDao;
import odk.groupe4.ApiCollabDev.dto.ContributionDto;
import odk.groupe4.ApiCollabDev.dto.Participant_projetDto;
import odk.groupe4.ApiCollabDev.models.*;
import odk.groupe4.ApiCollabDev.models.enums.StatusContribution;
import odk.groupe4.ApiCollabDev.models.enums.StatusFeatures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class Participant_projetService {
    @Autowired
    private Participant_projetDao participantProjetDao;
    @Autowired
    private FonctionnaliteDao fonctionnaliteDao;
    @Autowired
    private ContributionDao contributionDao;
    @Autowired
    private ProjetDao projetDao;

    //Méthode pour transformer un Participant_projet en Participant_projetDto
     private Participant_projetDto Participant_projetToParticipant_projetDto(Participant participant) {
        Participant_projetDto participantProjetDto = new Participant_projetDto();
        participantProjetDto.setProfil(participant.getProfil());
        participantProjetDto.setContributions(participant.getContributions());

        participantProjetDto.setFonctionnalite(participant.getFonctionnalite());
        return participantProjetDto;
     }
     // Méthode contrionDAOtoDTO
    private ContributionDto ContributionDaoToContributionDto(Contribution contribution) {
        ContributionDto contributionDto = new ContributionDto();
        contributionDto.setLienUrl(contribution.getLienUrl());
        contributionDto.setFileUrl(contribution.getFileUrl());
        contributionDto.setStatus(contribution.getStatus());
        contributionDto.setDateCreation(contribution.getDateCreation());
        contributionDto.setParticipant(contribution.getParticipant());
        contributionDto.setGestionnaire(contribution.getGestionnaire());
        contributionDto.setFonctionnalite(contribution.getFonctionnalite());
        return contributionDto;
    }

    // Methode pour soumettre une contribution
    public ContributionDto SoumettreUneContribution(String dateHeader, int idParticipant, ContributionDto contributiondto){
        Contribution contribution = new Contribution();
        StatusContribution status = StatusContribution.ENVOYE;
        Participant participant = participantProjetDao.findById(idParticipant)
                .orElseThrow(() -> new RuntimeException("Participant non trouvé"));
        LocalDate dateCreation = LocalDate.now(ZoneOffset.UTC);;
        if(dateHeader != null) {
            try {
                // Parser l'en-tête Date au format RFC 1123
                DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
                ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateHeader, formatter);
                // Convertir ZonedDateTime en java.util.Date
                dateCreation = zonedDateTime.toLocalDate();
            } catch (DateTimeParseException e) {
                System.out.println("Erreur de parsing de l'en-tête Date, utilisation de la date actuelle : " + dateCreation);
            }
        }else {
            // Fallback à la date actuelle si l'en-tête Date est absent
            System.out.println("En-tête Date absent, utilisation de la date actuelle : " + dateCreation);
        }
        contribution.setLienUrl(contribution.getLienUrl());
        contribution.setFileUrl(contributiondto.getFileUrl());
        contribution.setStatus(status); // pour mettre le status par défaut à "En attente de validation"
        contribution.setDateCreation(dateCreation);
        contribution.setParticipant(participant);
        contribution.setFonctionnalite(contribution.getFonctionnalite());

        // Enregistrement de la contribution dans la base de données
        Contribution contributionSaved = contributionDao.save(contribution);
        return ContributionDaoToContributionDto(contributionSaved);
    }


    public List<Participant> afficherParticipantProjet() {
        return participantProjetDao.findAll();
    }
    public Participant ajouterParticipant(Participant_projetDto participantProjet){

        Participant participant = new Participant();
        participant.setProfil(participantProjet.getProfil());
        return participantProjetDao.save(participant);
    }

    //Méthode pour reserver une fonctionnalité à un participant
    public Participant_projetDto reserverFonctionnalite(int idParticipant, int idFonctionnalite) {
        Participant participant = participantProjetDao.findById(idParticipant).orElseThrow(() -> new RuntimeException("Participant non trouvé"));
        Fonctionnalite fonctionnalite = fonctionnaliteDao.findById(idFonctionnalite).orElseThrow(() -> new RuntimeException("Fonctionnalité non trouvée"));
        // Vérifier si la fonctionnalité est déjà réservée
        if (fonctionnalite.getStatusFeatures() == StatusFeatures.EN_COURS || fonctionnalite.getStatusFeatures() == StatusFeatures.TERMINEE) {
            throw new RuntimeException("La fonctionnalité est déjà réservée");
        }
        // Réserver la fonctionnalité
        else {
            fonctionnalite.setStatusFeatures(StatusFeatures.EN_COURS);
            participant.setFonctionnalite(fonctionnalite);
            return Participant_projetToParticipant_projetDto( participantProjetDao.save(participant));
        }



    }
}