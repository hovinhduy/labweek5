/*

 * @ (#) SaveJobRepository.java        1.0     12/13/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.backend.repositories;

import iuh.fit.backend.models.SavedJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   12/13/2024
 */
@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {
    List<SavedJob> findByCandidateId(Long candidateId);
    Optional<SavedJob> findByCandidateIdAndJobId(Long candidateId, Long jobId);
    boolean existsByCandidateIdAndJobId(Long candidateId, Long jobId);
}