/*
 * @ (#) Experience.java        1.0     12/13/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.backend.services;

import iuh.fit.backend.models.Candidate;
import iuh.fit.backend.models.Experience;
import iuh.fit.backend.repositories.ExperienceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   12/13/2024
 */
@Service
@Slf4j
public class ExperienceService {
    @Autowired
    private ExperienceRepository experienceRepository;

    public Experience addExperience(Experience experience) {
        log.info("Adding new experience: {}", experience);
        return experienceRepository.save(experience);
    }

    public List<Experience> getExperiencesByCandidate(Candidate candidate) {
        return experienceRepository.findByCandidateOrderByFromDateDesc(candidate);
    }
    public void deleteExperience(Long id) {
        experienceRepository.deleteById(id);
    }
}