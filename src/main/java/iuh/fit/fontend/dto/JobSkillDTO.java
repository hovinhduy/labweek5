/*
 * @ (#) JobSkillDTO.java        1.0     11/15/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.fontend.dto;

import iuh.fit.backend.enums.SkillLevel;
import lombok.*;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/15/2024
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobSkillDTO {
    private Long skillId;
    private SkillLevel skillLevel;
    private String moreInfo;
}