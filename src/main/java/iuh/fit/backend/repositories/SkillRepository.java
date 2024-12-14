/*

 * @ (#) SkillRepository.java        1.0     11/6/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.backend.repositories;

import iuh.fit.backend.models.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/6/2024
 */
@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
   Skill findBySkillName(String skillName);
}
