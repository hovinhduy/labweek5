/*
 * @ (#) JobSkill.java        1.0     11/3/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.backend.models;

import iuh.fit.backend.enums.SkillLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/3/2024
 */
@Entity
@Table(name = "job_skill")
@Getter
@Setter
@NoArgsConstructor
public class JobSkill {
    @EmbeddedId
    private JobSkillId id;
    private String moreInfo;
    @Enumerated(EnumType.ORDINAL)
    private SkillLevel skillLevel;

    @ManyToOne
    @MapsId("jobId")
    @JoinColumn(name = "job_id")
    private Job job;

    public JobSkill(Skill skill, SkillLevel skillLevel) {
        this.skill = skill;
        this.skillLevel = skillLevel;
    }

    @ManyToOne
    @MapsId("skillId")
    @JoinColumn(name = "skill_id")
    private Skill skill;

    public JobSkill(Job job, Skill skill, SkillLevel skillLevel, String moreInfo) {
        this.id = new JobSkillId(job.getId(), skill.getId());
        this.job = job;
        this.skill = skill;
        this.skillLevel = skillLevel;
        this.moreInfo = moreInfo;
    }


}
