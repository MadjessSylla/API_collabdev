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






}
