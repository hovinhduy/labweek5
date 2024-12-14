/*
 * @ (#) SavedJob.java        1.0     12/13/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   12/13/2024
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
public class SavedJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    public SavedJob(Candidate candidate, Job job) {
        this.candidate = candidate;
        this.job = job;
    }
}
