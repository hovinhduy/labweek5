/*
 * @ (#) SavedJobService.java        1.0     12/13/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.backend.services;

import iuh.fit.backend.models.SavedJob;
import iuh.fit.backend.repositories.CandidateRepository;
import iuh.fit.backend.repositories.JobRepository;
import iuh.fit.backend.repositories.SavedJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   12/13/2024
 */
@Service
public class SavedJobService {
    @Autowired
    private SavedJobRepository savedJobRepository;
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private JobRepository jobRepository;

    public void saveJob(Long candidateId, Long jobId) {
        if (!savedJobRepository.existsByCandidateIdAndJobId(candidateId, jobId)) {
            SavedJob savedJob = new SavedJob();
            savedJob.setCandidate(candidateRepository.findById(candidateId).orElseThrow());
            savedJob.setJob(jobRepository.findById(jobId).orElseThrow());
            savedJobRepository.save(savedJob);
        }
    }

    public void unsaveJob(Long candidateId, Long jobId) {
        savedJobRepository.findByCandidateIdAndJobId(candidateId, jobId)
                .ifPresent(savedJob -> savedJobRepository.delete(savedJob));
    }

    public List<SavedJob> getSavedJobs(Long candidateId) {
        return savedJobRepository.findByCandidateId(candidateId);
    }

    public boolean isJobSaved(Long candidateId, Long jobId) {
        return savedJobRepository.existsByCandidateIdAndJobId(candidateId, jobId);
    }
}
