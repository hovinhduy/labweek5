/*
 * @ (#) JobPostingDTO.java        1.0     11/15/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.fontend.dto;

import iuh.fit.backend.enums.JobType;
import iuh.fit.backend.enums.WorkMode;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/15/2024
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobPostingDTO {
    private String name;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private double minSalary;
    private double maxSalary;
    private JobType typeJob;
    private WorkMode workMode;
    private String maxApplicants;
    private List<JobSkillDTO> requiredSkills;

}