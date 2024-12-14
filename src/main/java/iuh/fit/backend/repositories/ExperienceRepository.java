/*
 * @ (#) ExperienceRepository.java        1.0     12/13/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.backend.repositories;

import iuh.fit.backend.models.Candidate;
import iuh.fit.backend.models.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   12/13/2024
 */
@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    List<Experience> findByCandidateOrderByFromDateDesc(Candidate candidate);
}
