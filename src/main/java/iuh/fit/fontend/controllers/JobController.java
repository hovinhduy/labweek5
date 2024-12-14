/*
 * @ (#) JobController.java        1.0     11/6/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.fontend.controllers;

import iuh.fit.backend.enums.JobType;
import iuh.fit.backend.enums.SkillLevel;
import iuh.fit.backend.enums.WorkMode;
import iuh.fit.backend.models.*;
import iuh.fit.backend.services.*;
import iuh.fit.fontend.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/6/2024
 */
@Controller
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;
    private final SkillService skillService;
    private final CompanyService companyService;
    @Autowired
    private CandidateServices candidateService;
    @Autowired
    private SavedJobService savedJobService;
    @Autowired
    private JobRecommendationService jobRecommendationService;
    @Autowired
    private CandidateRecommendationService candidateRecommendationService;

    @GetMapping("/jobs")
    public String listJobs(Model model) {
        model.addAttribute("jobs", jobService.getAllJobs());
        return "jobs/list";
    }

    @GetMapping("/company/{companyId}/jobs")
    public String listCompanyJobs(@PathVariable Long companyId, Model model) {
        model.addAttribute("jobs", jobService.getJobsByCOmpanyId(companyId));
        return "jobs/list";
    }

    @GetMapping("/jobs/paging")
    public String showJobListPaging(Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);
        Page<Job> jobPage = jobService.findAllJobs(
                currentPage - 1, pageSize, "id", "asc");

        model.addAttribute("jobPage", jobPage);

        int totalPages = jobPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "jobs/jobpaging";
    }

    @GetMapping("company/job/post")
    public String showJobPostingForm(Model model, Authentication authentication) {
        // Get current logged in company
        String username = authentication.getName();
        Company company = companyService.getCompanyByUsername(username);

        // Add necessary data to model
        JobPostingDTO jobPostingDTO = new JobPostingDTO();
        // Set ngày hiện tại cho startDate trong DTO
        jobPostingDTO.setStartDate(LocalDate.now());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = LocalDate.now().format(formatter);

        model.addAttribute("currentDate", today);
        model.addAttribute("jobPostingDTO", jobPostingDTO);
        model.addAttribute("skills", skillService.getAllSkills());
        model.addAttribute("skillLevels", SkillLevel.values());
        model.addAttribute("jobTypes", JobType.values());
        model.addAttribute("workModes", WorkMode.values());

        return "jobs/job-posting-form";
    }

    @PostMapping("company/job/post")
    public String createJobPosting(@ModelAttribute JobPostingDTO jobPostingDTO,
            Authentication authentication) {
        // Get current logged in company
        String username = authentication.getName();
        Company company = companyService.getCompanyByUsername(username);

        Job job = new Job();
        job.setName(jobPostingDTO.getName());
        job.setDescription(jobPostingDTO.getDescription());
        job.setStartDate(jobPostingDTO.getStartDate());
        job.setEndDate(jobPostingDTO.getEndDate());
        job.setMinSalary(jobPostingDTO.getMinSalary());
        job.setMaxSalary(jobPostingDTO.getMaxSalary());
        job.setType(jobPostingDTO.getTypeJob());
        job.setWorkMode(jobPostingDTO.getWorkMode());
        job.setNumberOfPeople(jobPostingDTO.getMaxApplicants());
        job.setCompany(company);

        // Save job
        Job savedJob = jobService.saveJob(job);

        // Create and save job skills
        List<JobSkill> jobSkills = jobPostingDTO.getRequiredSkills().stream()
                .map(skillDTO -> {
                    Skill skill = skillService.getSkillById(skillDTO.getSkillId());
                    return new JobSkill(job, skill, skillDTO.getSkillLevel(), skillDTO.getMoreInfo());
                })
                .collect(Collectors.toList());
        jobService.saveAllJobSkills(jobSkills);

        return "redirect:/company/jobs";
    }

    @GetMapping("/company/jobs")
    public String listCompanyJobs(Model model, Authentication authentication) {
        // Get current logged in company
        String username = authentication.getName();
        Company company = companyService.getCompanyByUsername(username);

        // Get all jobs posted by company
        List<Job> jobs = jobService.getJobsByCompany(company);
        model.addAttribute("jobs", jobs);

        return "jobs/company-jobs";
    }


    @GetMapping("/candidate/home")
    public String showRecommendedJobs(Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "6") int size,
            Authentication authentication) {
        String username = authentication.getName();
        Candidate candidate = candidateService.getCandidateByUsername(username);

        // Lấy tất cả jobs
        List<Job> allJobs = jobService.getAllJobs();

//        // Lấy recommended jobs
//        JobRecommendationSystem recommendationSystem = new JobRecommendationSystem();
//        recommendationSystem.initializeModel(skillService.getAllSkills());
//        recommendationSystem.trainModel(Arrays.asList(candidate), allJobs);


        // Lấy toàn bộ recommendations và sắp xếp theo score
//        List<JobRecommendation> allRecommendations = recommendationSystem.recommendJobs(candidate, allJobs,
//                allJobs.size());
        List<JobRecommendation> allRecommendations = jobRecommendationService.recommendJobsForCandidate(candidate.getId());
        // Tính toán phân trang
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, allRecommendations.size());
        List<JobRecommendation> pageContent = allRecommendations.subList(fromIndex, toIndex);

        // Tạo đối tượng phân trang
        RecommendedJobPageDTO pageDTO = new RecommendedJobPageDTO(
                pageContent,
                page,
                (int) Math.ceil((double) allRecommendations.size() / size),
                allRecommendations.size(),
                size);

        model.addAttribute("recommendedJobs", pageDTO);
        model.addAttribute("savedJobService", savedJobService);
        return "candidates/home";
    }

    @GetMapping("/candidate/jobs/search")
    public String searchJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Double minSalary,
            @RequestParam(required = false) Double maxSalary,
            @RequestParam(required = false) JobType jobType,
            @RequestParam(required = false) WorkMode workMode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,

            Model model) {

        Page<Job> jobs = jobService.searchJobs(
                title, minSalary, maxSalary, jobType, workMode,
                page, size);

        model.addAttribute("jobs", jobs);
        model.addAttribute("currentSearch", new SearchCriteria(title, minSalary, maxSalary, jobType, workMode));

        return "candidates/findjob";
    }
    @GetMapping("/jobs/{jobId}/recommended-candidates")
    public String showRecommendedCandidates(@PathVariable Long jobId, Model model) {
        Job job = jobService.getJobById(jobId);
        List<CandidateRecommendation> recommendations = candidateRecommendationService.recommendCandidatesForJob(job);

        model.addAttribute("job", job);
        model.addAttribute("recommendations", recommendations);

        return "jobs/recommended-candidates";
    }
}
