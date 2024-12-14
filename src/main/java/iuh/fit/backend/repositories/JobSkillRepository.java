/*
 * @ (#) JobSkillRepository.java        1.0     11/6/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.backend.repositories;

import iuh.fit.backend.models.JobSkill;
import iuh.fit.backend.models.JobSkillId;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/6/2024
 */
public interface JobSkillRepository extends JpaRepository<JobSkill,JobSkillId> {
}
