/*
 * @ (#) JobSkillId.java        1.0     11/6/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.backend.models;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/6/2024
 */
@Embeddable
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JobSkillId implements Serializable {
    private Long jobId;
    private Long skillId;


}
