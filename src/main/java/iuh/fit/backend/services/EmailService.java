/*
 * @ (#) test.java        1.0     12/14/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.backend.services;

import iuh.fit.backend.models.Skill;
import iuh.fit.fontend.dto.JobPostingDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   12/14/2024
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;
    @Autowired
    private SkillService skillService;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
    public void sendInvitationEmail(String to, JobPostingDTO jobPosting, String candidateName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            Context context = new Context();
            context.setVariable("candidateName", candidateName);
            context.setVariable("jobTitle", jobPosting.getName());
            context.setVariable("workMode", jobPosting.getWorkMode().getValue());
            context.setVariable("jobType", jobPosting.getTypeJob().getValue());
            context.setVariable("minSalary", jobPosting.getMinSalary());
            context.setVariable("maxSalary", jobPosting.getMaxSalary());
            context.setVariable("startDate",
                    jobPosting.getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            List<String> skillNames = jobPosting.getRequiredSkills().stream()
                    .map(jobSkill -> {
                        return skillService.getSkillById(jobSkill.getSkillId()).getSkillName() + " (Level: " + jobSkill.getSkillLevel() + ")";
                    })
                    .collect(Collectors.toList());

            context.setVariable("requiredSkills", skillNames);


            String emailContent = templateEngine.process("mail/mail", context);

            helper.setTo(to);
            helper.setSubject("Job Invitation: " + jobPosting.getName());
            helper.setText(emailContent, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}