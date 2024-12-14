/*
 * @ (#) MailConTroller.java        1.0     12/14/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.fontend.controllers;

import iuh.fit.backend.models.Candidate;
import iuh.fit.backend.services.CandidateServices;
import iuh.fit.backend.services.EmailService;
import iuh.fit.backend.services.JobService;
import iuh.fit.fontend.dto.JobPostingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   12/14/2024
 */
@Controller
public class EmailController {
    @Autowired
    private EmailService emailService;

    @Autowired
    private JobService jobService;

    @Autowired
    private CandidateServices candidateService;

    @PostMapping("/jobs/{jobId}/invite/{candidateId}")
    public String inviteCandidate(
            @PathVariable Long jobId,
            @PathVariable Long candidateId,
            RedirectAttributes redirectAttributes) {
        try {
            // Lấy thông tin job và candidate
            JobPostingDTO job = jobService.getJobPostingById(jobId);
            Candidate candidate = candidateService.getCandidateById(candidateId);

            // Gửi email
            emailService.sendInvitationEmail(
                    candidate.getEmail(),
                    job,
                    candidate.getFullName()
            );

            // Thông báo thành công
            redirectAttributes.addFlashAttribute("successMessage",
                    "Invitation sent successfully to " + candidate.getFullName());

        } catch (Exception e) {
            // Thông báo lỗi
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Failed to send invitation: " + e.getMessage());
        }

        // Redirect về trang recommended candidates
        return "redirect:/jobs/" + jobId + "/recommended-candidates";
    }
}