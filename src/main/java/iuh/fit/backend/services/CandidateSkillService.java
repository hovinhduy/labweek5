/*
 * @ (#) CandidateSkillService.java        1.0     11/16/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.backend.services;

import iuh.fit.backend.models.CanSkillId;
import iuh.fit.backend.models.Candidate;
import iuh.fit.backend.models.CandidateSkill;
import iuh.fit.backend.models.Skill;
import iuh.fit.backend.repositories.CandidateRepository;
import iuh.fit.backend.repositories.CandidateSkillRepository;
import iuh.fit.backend.repositories.SkillRepository;
import iuh.fit.fontend.dto.CandidateSkillDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/16/2024
 */
@Service
public class CandidateSkillService {
    @Autowired
    private CandidateSkillRepository candidateSkillRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private SkillRepository skillRepository;

    public List<CandidateSkill> findByCandidateId(Long candidateId) {
        return candidateSkillRepository.findByCandidateId(candidateId);
    }
    public void saveCandidateSkill(CandidateSkill candidateSkill) {
        System.out.println(candidateSkill+"test");
        candidateSkillRepository.save(candidateSkill);
    }
    @Transactional
    public void deleteSkill(Long candidateId, Long skillId) {
        // Tạo composite key
        CanSkillId canSkillId = new CanSkillId(candidateId, skillId);

        // Xóa trực tiếp bằng composite key
        candidateSkillRepository.deleteById(canSkillId);
    }


}
