/*
 * @ (#) SearchCriteria.java        1.0     12/14/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.fontend.dto;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   12/14/2024
 */

import iuh.fit.backend.enums.JobType;
import iuh.fit.backend.enums.WorkMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {
    private String title;
    private Double minSalary;
    private Double maxSalary;
    private JobType jobType;
    private WorkMode workMode;
}