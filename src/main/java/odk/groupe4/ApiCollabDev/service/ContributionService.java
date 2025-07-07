package odk.groupe4.ApiCollabDev.service;


import odk.groupe4.ApiCollabDev.dao.ContributionDao;
import odk.groupe4.ApiCollabDev.dto.ContributionDto;
import odk.groupe4.ApiCollabDev.models.Contribution;
import odk.groupe4.ApiCollabDev.models.enums.StatusContribution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContributionService {
    @Autowired
    private ContributionDao contributionDao;
    // methode contributionDAO à contributionDTO
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
    // methode pour afficher la liste des contributions
    public List<ContributionDto> afficherLaListeDesContribution(){
        return contributionDao.findAll()
                .stream().map(this :: ContributionDaoToContributionDto)
                .collect(Collectors.toList());
    }
    // Methode pour soumettre une contribution
    public ContributionDto SoumettreUneContribution( String dateHeader, ContributionDto contributiondto){

        Contribution contribution = new Contribution();
        StatusContribution status = StatusContribution.ENVOYE;
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
        contribution.setParticipant(contributiondto.getParticipant());
        contribution.setGestionnaire(contributiondto.getGestionnaire());
        contribution.setFonctionnalite(contribution.getFonctionnalite());

        // Enregistrement de la contribution dans la base de données
        Contribution contributionSaved = contributionDao.save(contribution);
        return ContributionDaoToContributionDto(contributionSaved);
    }
    // Méthode pour afficher la liste des contributions d'un utilisateur
    public List<ContributionDto> afficherContributionsParUtilisateur(int id) {
        List<Contribution> contributions = contributionDao.findByUserId(id);
        return contributions.stream()
                .map(this::ContributionDaoToContributionDto)
                .collect(Collectors.toList());
    }
    // Méthode pour valider ou refuser une contribution
    public ContributionDto validerOuRefuserContribution(int id, StatusContribution status) {
        Optional<Contribution> optionalContribution = contributionDao.findById(id);
        if (optionalContribution.isPresent()) {
            Contribution contribution = optionalContribution.get();
            contribution.setStatus(status);
            Contribution updatedContribution = contributionDao.save(contribution);
            return ContributionDaoToContributionDto(updatedContribution);
        } else {
            return null; // ou lancer une exception si la contribution n'existe pas
        }
    }



}
