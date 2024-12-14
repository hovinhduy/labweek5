/*
 * @ (#) ExperienceController.java        1.0     12/13/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.fontend.controllers;

import iuh.fit.backend.models.Candidate;
import iuh.fit.backend.models.Experience;
import iuh.fit.backend.services.CandidateServices;
import iuh.fit.backend.services.ExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   12/13/2024
 */
@Controller
@RequestMapping("/experience")
public class ExperienceController {
    @Autowired
    private ExperienceService experienceService;

    @Autowired
    private CandidateServices candidateServices;

    @PostMapping("/add")
    public String addExperience(@ModelAttribute Experience experience,
                                Authentication authentication) {
        Candidate candidate = candidateServices.getCandidateByUsername(authentication.getName());
        experience.setCandidate(candidate);
        experienceService.addExperience(experience);
        return "redirect:/candidate/profile";
    }

    @GetMapping("/delete/{id}")
    public String deleteExperience(@PathVariable Long id) {
        experienceService.deleteExperience(id);
        return "redirect:/candidate/profile";
    }
}