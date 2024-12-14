/*
 * @ (#) CandidateSkillRepository.java        1.0     11/16/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.backend.repositories;

import iuh.fit.backend.models.CanSkillId;
import iuh.fit.backend.models.CandidateSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/16/2024
 */
@Repository
public interface CandidateSkillRepository extends JpaRepository<CandidateSkill, CanSkillId> {
    List<CandidateSkill> findByCandidateId(Long candidateId);
    List<CandidateSkill> findBySkillId(Long skillId);
    CandidateSkill findByCandidateIdAndSkillId(Long candidateId, Long skillId);
    boolean existsByCandidateIdAndSkillId(Long candidateId, Long skillId);
}
