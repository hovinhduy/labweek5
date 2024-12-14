/*
 * @ (#) Skill.java        1.0     11/3/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.backend.models;

import iuh.fit.backend.enums.SkillType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/3/2024
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id")
    private Long id;
    @Column(name = "skill_name" ,length = 150)
    private String skillName;
    @Column(name = "skill_desc",length =300)
    private String skillDescription;
    @Enumerated(EnumType.ORDINAL)
    private SkillType type;

    @OneToMany(mappedBy = "skill")
    private List<JobSkill> jobSkills;

    public Skill(String skillName, String skillDescription, SkillType type) {
        this.skillName = skillName;
        this.skillDescription = skillDescription;
        this.type = type;
    }

    public Skill(String skillName, String skillDescription, SkillType type, List<JobSkill> jobSkills) {
        this.skillName = skillName;
        this.skillDescription = skillDescription;
        this.type = type;
        this.jobSkills = jobSkills;
    }

    public Skill(Long id, String skillName) {
        this.id = id;
        this.skillName = skillName;
    }
}
