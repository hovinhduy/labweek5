/*
 * @ (#) CandidateSkill.java        1.0     11/3/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.backend.models;

import iuh.fit.backend.enums.SkillLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/3/2024
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CandidateSkill  {
    @EmbeddedId
    private CanSkillId id;
    private String moreInfo;
    @Enumerated(EnumType.ORDINAL)
    private SkillLevel skillLevel;

    @ManyToOne
    @MapsId("candidateId")
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @OneToOne
    @MapsId("skillId")
    @JoinColumn(name = "skill_id")
    private Skill skill;

    public CandidateSkill(Candidate candidate, Skill skill, String moreInfo, SkillLevel skillLevel) {
        this.candidate = candidate;
        this.skill = skill;
        this.moreInfo = moreInfo;
        this.skillLevel = skillLevel;
        this.id = new CanSkillId(candidate.getId(), skill.getId());
    }

    public CandidateSkill(Skill skill, SkillLevel skillLevel) {
        this.skill = skill;
        this.skillLevel = skillLevel;
    }
}
