/*
 * @ (#) CandidateController.java        1.0     11/3/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.fontend.controllers;

import com.neovisionaries.i18n.CountryCode;
import iuh.fit.backend.models.*;
import iuh.fit.backend.repositories.CandidateRepository;
import iuh.fit.backend.services.*;
import iuh.fit.fontend.dto.CandidateSkillDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/3/2024
 */
@Controller
public class CandidateController {
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private CandidateServices candidateServices;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private ExperienceService experienceService;
    @Autowired
    private CandidateSkillService candidateSkillService;
    @Autowired
    private SkillService skillService;

    @GetMapping("/list")
    public String showCandidateList(Model model) {
        model.addAttribute("candidates", candidateRepository.findAll());
        return "candidates/candidates";
    }
    @GetMapping("/candidates")
    public String showCandidateListPaging(Model model,
                                          @RequestParam("page") Optional<Integer> page,
                                          @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);
        Page<Candidate> candidatePage= candidateServices.findAll(
                currentPage - 1,pageSize,"id","asc");


        model.addAttribute("candidatePage", candidatePage);

        int totalPages = candidatePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "candidates/candidates-paging";
    }
    @GetMapping("/candidate")
    public String showCandidateDetail(Model model, Authentication authentication) {
        String username = authentication.getName();
        Candidate candidate = candidateServices.getCandidateByUsername(username);
        List<CountryCode> countryCodes = List.of(CountryCode.values());
        System.out.println(candidate);
        model.addAttribute("candidate", candidate);
        model.addAttribute("countryCodes", countryCodes);
        return "candidates/profile";
    }
    @PostMapping("/candidate/update")
    public String updateCandidate(@ModelAttribute Candidate updatedCandidate,
                                  Authentication authentication) {
        // Lấy candidate hiện tại
        Candidate currentCandidate = candidateServices.getCandidateByUsername(
                authentication.getName());

        // Copy các thông tin cần update
        currentCandidate.setFullName(updatedCandidate.getFullName());
        currentCandidate.setPhone(updatedCandidate.getPhone());
        currentCandidate.setEmail(updatedCandidate.getEmail());
        currentCandidate.setDob(updatedCandidate.getDob());
        System.out.println(updatedCandidate.getDob()+"1");
        // Copy địa chỉ
        if (updatedCandidate.getAddress() != null) {
            currentCandidate.getAddress().setNumber(
                    updatedCandidate.getAddress().getNumber());
            currentCandidate.getAddress().setStreet(
                    updatedCandidate.getAddress().getStreet());
            currentCandidate.getAddress().setCity(
                    updatedCandidate.getAddress().getCity());
            currentCandidate.getAddress().setZipCode(
                    updatedCandidate.getAddress().getZipCode());
            currentCandidate.getAddress().setCountryCode(
                    updatedCandidate.getAddress().getCountryCode());
        }

        // Lưu vào database
        candidateServices.save(currentCandidate);

        return "redirect:/candidate";
    }
    @GetMapping("/candidate/profile")
    public String showCandidateProfile(Model model, Authentication authentication) {
        String username = authentication.getName();
        Candidate candidate = candidateServices.getCandidateByUsername(username);
        List<CountryCode> countryCodes = List.of(CountryCode.values());
        List<Experience> experiences = experienceService.getExperiencesByCandidate(candidate);
        List<CandidateSkill> candidateSkills = candidateSkillService.findByCandidateId(candidate.getId());
        List<Skill> skills = skillService.getAllSkills();

        model.addAttribute("candidate", candidate);
        model.addAttribute("countryCodes", countryCodes);
        model.addAttribute("experiences", experiences);
        model.addAttribute("newExperience", new Experience());
        model.addAttribute("candidateSkills", candidateSkills);
        model.addAttribute("skills", skills);
        model.addAttribute("candidateSkillDTO", new CandidateSkillDTO());
        return "candidates/profile"; // Changed to profile1
    }

    @PostMapping("/candidate/update-profile")
    public String updateProfile(@ModelAttribute Candidate updatedCandidate,
                                Authentication authentication) {
        String username = authentication.getName();
        Candidate currentCandidate = candidateServices.getCandidateByUsername(username);

        // Update basic info
        currentCandidate.setFullName(updatedCandidate.getFullName());
        currentCandidate.setPhone(updatedCandidate.getPhone());
        currentCandidate.setEmail(updatedCandidate.getEmail());

        // Update address
        if (currentCandidate.getAddress() == null) {
            currentCandidate.setAddress(new Address());
        }
        Address address = currentCandidate.getAddress();
        address.setNumber(updatedCandidate.getAddress().getNumber());
        address.setStreet(updatedCandidate.getAddress().getStreet());
        address.setCity(updatedCandidate.getAddress().getCity());
        address.setZipCode(updatedCandidate.getAddress().getZipCode());
        address.setCountryCode(updatedCandidate.getAddress().getCountryCode());

        candidateServices.save(currentCandidate);
        return "redirect:/candidate/profile";
    }
//    @PostMapping("/candidate/upload-cv")
//    @ResponseBody
//    public ResponseEntity<?> uploadCV(@RequestParam("file") MultipartFile file,
//                                      Authentication authentication) {
//        try {
//            String username = authentication.getName();
//            Candidate candidate = candidateServices.getCandidateByUsername(username);
//
////            String cvUrl = cloudinaryService.uploadPdf(file);
////            candidate.setCvUrl(cvUrl); // Thêm trường cvUrl vào Candidate entity
//            candidateServices.save(candidate);
//
//            return ResponseEntity.ok(Map.of(
//                    "message", "CV uploaded successfully",
//                    "url", cvUrl
//            ));
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(Map.of(
//                    "error", e.getMessage()
//            ));
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(Map.of(
//                    "error", "Failed to upload CV"
//            ));
//        }
//    }

}
