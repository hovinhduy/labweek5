/*
 * @ (#) JobService.java        1.0     11/6/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.backend.services;

import iuh.fit.backend.enums.JobType;
import iuh.fit.backend.enums.WorkMode;
import iuh.fit.backend.models.Company;
import iuh.fit.backend.models.Job;
import iuh.fit.backend.models.JobSkill;
import iuh.fit.backend.repositories.CompanyRepository;
import iuh.fit.backend.repositories.JobRepository;
import iuh.fit.backend.repositories.JobSkillRepository;
import iuh.fit.backend.repositories.SkillRepository;
import iuh.fit.fontend.dto.JobPostingDTO;
import iuh.fit.fontend.dto.JobSkillDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/6/2024
 */
@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private JobSkillRepository jobSkillRepository;

    public List<Job> getJobsByCOmpanyId(Long companyId) {
        return jobRepository.findByCompanyId(companyId);
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Page<Job> findAllJobs(int pageNo, int pageSize, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return jobRepository.findAll(pageable);
    }

    public Job saveJob(Job job) {
        return jobRepository.save(job);
    }

    public List<Job> getJobsByCompany(Company company) {
        return jobRepository.findByCompany(company);
    }

    public void saveAllJobSkills(List<JobSkill> jobSkills) {
        jobSkillRepository.saveAll(jobSkills);
    }

    public int getTotalJobCount() {
        return jobRepository.findAll().size();
    }

    public Page<Job> searchJobs(String title, Double minSalary, Double maxSalary,
                                JobType type, WorkMode workMode,
                                int pageNo, int pageSize) {


        Pageable pageable = PageRequest.of(pageNo, pageSize,
                Sort.by(Sort.Direction.ASC, "id"));

        return jobRepository.searchJobs(title, minSalary, maxSalary, type, workMode, pageable);
    }
    // Add helper methods for job counts
    public Map<JobType, Long> getJobTypeCounts() {
        Map<JobType, Long> counts = new HashMap<>();
        for (JobType type : JobType.values()) {
            counts.put(type, jobRepository.countByType(type));
        }
        return counts;
    }

    public Map<WorkMode, Long> getWorkModeCounts() {
        Map<WorkMode, Long> counts = new HashMap<>();
        for (WorkMode mode : WorkMode.values()) {
            counts.put(mode, jobRepository.countByWorkMode(mode));
        }
        return counts;
    }

    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }
    //getJobPostingById
    public JobPostingDTO getJobPostingById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        JobPostingDTO jobPostingDTO = new JobPostingDTO();
        jobPostingDTO.setName(job.getName());
        jobPostingDTO.setDescription(job.getDescription());
        jobPostingDTO.setStartDate(job.getStartDate());
        jobPostingDTO.setEndDate(job.getEndDate());
        jobPostingDTO.setMinSalary(job.getMinSalary());
        jobPostingDTO.setMaxSalary(job.getMaxSalary());
        jobPostingDTO.setTypeJob(job.getType());
        jobPostingDTO.setWorkMode(job.getWorkMode());
        jobPostingDTO.setMaxApplicants(job.getNumberOfPeople());
        //
        jobPostingDTO.setRequiredSkills(job.getJobSkills().stream()
                .map(jobSkill -> {
                    JobSkillDTO skillDTO = new JobSkillDTO();
                    skillDTO.setSkillId(jobSkill.getSkill().getId());
                    skillDTO.setSkillLevel(jobSkill.getSkillLevel());
                    skillDTO.setMoreInfo(jobSkill.getMoreInfo());
                    return skillDTO;
                })
                .collect(Collectors.toList()));

        return jobPostingDTO;
    }


}
