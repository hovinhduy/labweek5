/*
 * @ (#) SavedJobController.java        1.0     12/13/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.fontend.controllers;

import iuh.fit.backend.models.SavedJob;
import iuh.fit.backend.services.CandidateServices;
import iuh.fit.backend.services.SavedJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   12/13/2024
 */
@Controller
public class SavedJobController {
    @Autowired
    private SavedJobService savedJobService;
    @Autowired
    private CandidateServices candidateServices;

    @PostMapping("/candidate/save-job/{jobId}")
    public String saveJob(@PathVariable Long jobId,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        Long candidateId = candidateServices.getCandidateByUsername(username).getId();
        savedJobService.saveJob(candidateId, jobId);
        redirectAttributes.addFlashAttribute("message", "Job saved successfully");
        return "redirect:/candidate/home";
    }

    @PostMapping("/candidate/unsave-job/{jobId}")
    public String unsaveJob(@PathVariable Long jobId,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        Long candidateId = candidateServices.getCandidateByUsername(username).getId();
        savedJobService.unsaveJob(candidateId, jobId);
        redirectAttributes.addFlashAttribute("message", "Job unsaved successfully");
        return "redirect:/candidate/home";
    }

    @GetMapping("/candidate/saved-jobs")
    public String getSavedJobs(Model model, Authentication authentication) {
        String username = authentication.getName();
        Long candidateId = candidateServices.getCandidateByUsername(username).getId();
        List<SavedJob> savedJobs = savedJobService.getSavedJobs(candidateId);
        model.addAttribute("savedJobs", savedJobs);
        model.addAttribute("activeTab", "saved");
        return "candidates/saved-jobs";
    }
}