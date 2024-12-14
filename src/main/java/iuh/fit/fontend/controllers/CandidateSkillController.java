/*
 * @ (#) CandidateSkillController.java        1.0     11/16/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.fontend.controllers;

import iuh.fit.backend.models.Candidate;
import iuh.fit.backend.models.CandidateSkill;
import iuh.fit.backend.models.Skill;
import iuh.fit.backend.services.CandidateServices;
import iuh.fit.backend.services.CandidateSkillService;
import iuh.fit.backend.services.SkillService;
import iuh.fit.fontend.dto.CandidateSkillDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/16/2024
 */
@Controller
@RequestMapping("/candidate")
public class CandidateSkillController {
    @Autowired
    private CandidateSkillService candidateSkillService;

    @Autowired
    private CandidateServices candidateService;
    @Autowired
    private SkillService skillService;



    @PostMapping("/skills/add")
    public String addSkill(@ModelAttribute @Valid CandidateSkillDTO candidateSkillDTO,
                           BindingResult bindingResult,
                           Model model, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return "redirect:/candidate/profile";
        }
        try {
            String username = authentication.getName();
            Candidate candidate = candidateService.getCandidateByUsername(username);
            if (candidate == null) {
                model.addAttribute("error", "Candidate not found");
                return "candidates/profile1";
            }

            Skill skill = skillService.getSkillById(candidateSkillDTO.getSkillId());
            if (skill == null) {
                model.addAttribute("error", "Skill not found");
                return "candidates/profile1";
            }

            CandidateSkill candidateSkill = new CandidateSkill(candidate, skill, candidateSkillDTO.getMoreInfo(), candidateSkillDTO.getSkillLevel());
            //tạo list candidateSkill
            List<CandidateSkill> candidateSkills1 = List.of(candidateSkill);
            candidateService.saveAll(candidateSkills1);
            return "redirect:/candidate/profile";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "candidates/profile1";
        }
    }
    @PostMapping("/{candidateId}/skills/{skillId}")
    public String deleteSkill(@PathVariable Long candidateId, @PathVariable Long skillId) {
        candidateSkillService.deleteSkill(candidateId, skillId);
        return "redirect:/candidate/profile";
    }

}