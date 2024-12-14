/*
 * @ (#) JobRecommendation.java        1.0     12/13/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.fontend.dto;

import iuh.fit.backend.models.Job;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   12/13/2024
 */
@Getter
@Setter
@NoArgsConstructor
public class JobRecommendation {
    private Job job;
    private double score;
    private boolean isSaved;

    public String getFormattedScore() {
        return String.format("%.1f%%", score);
    }


    public JobRecommendation(Job job, double score) {
        this.job = job;
        this.score = score;
    }

}