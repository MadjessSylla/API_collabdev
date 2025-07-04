package odk.groupe4.ApiCollabDev.service;


import odk.groupe4.ApiCollabDev.dao.ContributionDao;
import odk.groupe4.ApiCollabDev.dto.ContributionDto;
import odk.groupe4.ApiCollabDev.models.Contribution;
import odk.groupe4.ApiCollabDev.models.enums.StatusContribution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        contributionDto.setId(contribution.getId());
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
    public ContributionDto SoumettreUneContribution(ContributionDto contributiondto){

        Contribution contribution = new Contribution();
        contribution.setId(contributiondto.getId());
        contribution.setLienUrl(contribution.getLienUrl());
        contribution.setFileUrl(contributiondto.getFileUrl());
        contribution.setStatus(contributiondto.getStatus());
        contribution.setDateCreation(contributiondto.getDateCreation());
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
