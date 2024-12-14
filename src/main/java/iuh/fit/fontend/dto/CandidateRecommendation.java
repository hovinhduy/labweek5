/*
 * @ (#) CandidateRecommendation.java        1.0     12/14/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.fontend.dto;

import iuh.fit.backend.models.Candidate;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   12/14/2024
 */
@Getter
@AllArgsConstructor
public class CandidateRecommendation {
    private Candidate candidate;
    private double score;

    public String getFormattedScore() {
        return String.format("%.1f%%", score * 100);
    }

    public String getMatchLevel() {
        if (score >= 0.8) return "Excellent Match";
        if (score >= 0.6) return "Good Match";
        if (score >= 0.4) return "Fair Match";
        return "Basic Match";
    }

    public String getMatchLevelClass() {
        if (score >= 0.8) return "text-success";
        if (score >= 0.6) return "text-primary";
        if (score >= 0.4) return "text-warning";
        return "text-muted";
    }
}