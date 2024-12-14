/*
 * @ (#) CandidateRecommendationService.java        1.0     12/14/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.backend.services;

import iuh.fit.backend.enums.SkillLevel;
import iuh.fit.backend.models.Candidate;
import iuh.fit.backend.models.CandidateSkill;
import iuh.fit.backend.models.Job;
import iuh.fit.backend.models.JobSkill;
import iuh.fit.backend.repositories.CandidateRepository;
import iuh.fit.backend.repositories.CandidateSkillRepository;
import iuh.fit.fontend.dto.CandidateRecommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   12/14/2024
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CandidateRecommendationService {
    private final CandidateRepository candidateRepository;
    private final CandidateSkillRepository candidateSkillRepository;

    public List<CandidateRecommendation> recommendCandidatesForJob(Job job) {
        // Lấy tất cả ứng viên
        List<Candidate> allCandidates = candidateRepository.findAll();
        List<CandidateRecommendation> recommendations = new ArrayList<>();

        // Lấy danh sách kỹ năng yêu cầu của công việc
        List<JobSkill> requiredSkills = job.getJobSkills();

        for (Candidate candidate : allCandidates) {
            // Lấy kỹ năng của ứng viên
            List<CandidateSkill> candidateSkills = candidateSkillRepository.findByCandidateId(candidate.getId());

            // Tính điểm matching
            double matchScore = calculateMatchScore(candidateSkills, requiredSkills);

            if (matchScore > 0.3) { // Chỉ lấy những ứng viên có điểm matching > 30%
                recommendations.add(new CandidateRecommendation(candidate, matchScore));
            }
        }

        // Sắp xếp theo điểm matching giảm dần
        recommendations.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));

        return recommendations;
    }

    private double calculateMatchScore(List<CandidateSkill> candidateSkills, List<JobSkill> requiredSkills) {
        if (requiredSkills == null || requiredSkills.isEmpty()) {
            return 0.0;
        }

        double totalScore = 0;
        int matchedSkills = 0;

        for (JobSkill required : requiredSkills) {
            for (CandidateSkill candidateSkill : candidateSkills) {
                if (candidateSkill.getSkill().getId().equals(required.getSkill().getId())) {
                    totalScore += calculateSkillLevelScore(
                            candidateSkill.getSkillLevel(),
                            required.getSkillLevel()
                    );
                    matchedSkills++;
                    break;
                }
            }
        }

        // Tính điểm cuối cùng
        double averageScore = matchedSkills > 0 ? totalScore / requiredSkills.size() : 0;
        double coverageScore = (double) matchedSkills / requiredSkills.size();

        // 60% cho mức độ phù hợp kỹ năng, 40% cho độ phủ kỹ năng
        return (averageScore * 0.6 + coverageScore * 0.4);
    }

    private double calculateSkillLevelScore(SkillLevel candidateLevel, SkillLevel requiredLevel) {
        if (candidateLevel.getValue() >= requiredLevel.getValue()) {
            return 1.0;
        } else {
            return (double) candidateLevel.getValue() / requiredLevel.getValue();
        }
    }
}