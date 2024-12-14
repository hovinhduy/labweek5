/*
 * @ (#) CandidateSkillDTO.java        1.0     11/16/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.fontend.dto;

import iuh.fit.backend.enums.SkillLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/16/2024
 */
@Data
@NoArgsConstructor
public class CandidateSkillDTO {
    private Long skillId;
    private SkillLevel skillLevel;
    private String moreInfo;
}