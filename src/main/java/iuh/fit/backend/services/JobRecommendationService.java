package iuh.fit.backend.services;

import iuh.fit.backend.enums.SkillLevel;
import iuh.fit.backend.models.CandidateSkill;
import iuh.fit.backend.models.Job;
import iuh.fit.backend.models.JobSkill;
import iuh.fit.backend.repositories.CandidateSkillRepository;
import iuh.fit.backend.repositories.JobRepository;
import iuh.fit.fontend.dto.JobRecommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobRecommendationService {
    
    private final CandidateSkillRepository candidateSkillRepository;
    private final JobRepository jobRepository;
    private final SkillService skillService;

    public List<JobRecommendation> recommendJobsForCandidate(Long candidateId) {
        // Lấy kỹ năng của candidate
        List<CandidateSkill> candidateSkills = candidateSkillRepository.findByCandidateId(candidateId);
        Map<Long, SkillLevel> candidateSkillMap = candidateSkills.stream()
            .collect(Collectors.toMap(
                skill -> skill.getSkill().getId(),
                CandidateSkill::getSkillLevel
            ));
        
        // Lấy tất cả công việc
        List<Job> allJobs = jobRepository.findAll();
        
        // Tính điểm phù hợp cho mỗi công việc
        List<JobRecommendation> recommendations = new ArrayList<>();
        
        for (Job job : allJobs) {
            double matchScore = calculateMatchScore(candidateSkillMap, job.getJobSkills());
            if (matchScore > 0.3) { // Chỉ recommend những công việc có điểm phù hợp > 30%
                recommendations.add(new JobRecommendation(job, matchScore));
            }
        }
        
        // Sắp xếp theo điểm phù hợp giảm dần
        recommendations.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));
        
        return recommendations;
    }

    
    private double calculateMatchScore(Map<Long, SkillLevel> candidateSkills, List<JobSkill> jobSkills) {
        if (jobSkills == null || jobSkills.isEmpty()) {
            return 0.0;
        }

        double totalScore = 0;
        int matchedSkills = 0;
        
        for (JobSkill jobSkill : jobSkills) {
            Long skillId = jobSkill.getSkill().getId();
            SkillLevel requiredLevel = jobSkill.getSkillLevel();
            
            if (candidateSkills.containsKey(skillId)) {
                SkillLevel candidateLevel = candidateSkills.get(skillId);
                totalScore += calculateSkillLevelScore(candidateLevel, requiredLevel);
                matchedSkills++;
            }
        }
        
        // Tính điểm cuối cùng dựa trên cả số lượng kỹ năng khớp và mức độ phù hợp
        double averageScore = matchedSkills > 0 ? totalScore / jobSkills.size() : 0;
        double coverageScore = (double) matchedSkills / jobSkills.size();
        
        // Trọng số: 60% cho mức độ phù hợp kỹ năng, 40% cho độ phủ kỹ năng
        return (averageScore * 0.6 + coverageScore * 0.4) ;
    }
    
    private double calculateSkillLevelScore(SkillLevel candidateLevel, SkillLevel requiredLevel) {
        if (candidateLevel.getValue() >= requiredLevel.getValue()) {
            return 1.0; // Điểm tối đa nếu ứng viên có kỹ năng bằng hoặc cao hơn yêu cầu
        } else {
            // Điểm một phần nếu kỹ năng thấp hơn yêu cầu
            return (double) candidateLevel.getValue() / requiredLevel.getValue();
        }
    }
} 