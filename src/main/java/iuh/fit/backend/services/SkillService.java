/*
 * @ (#) SkillService.java        1.0     11/6/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.backend.services;

import iuh.fit.backend.models.Skill;
import iuh.fit.backend.repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/6/2024
 */
@Service
public class SkillService {
    @Autowired
    private SkillRepository skillRepository;

    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }
    public Skill createSkill(Skill skill) {
        return skillRepository.save(skill);
    }
    public Skill updateSkill(Skill skill) {
        return skillRepository.save(skill);
    }
    public void deleteSkill(Long id) {
        skillRepository.deleteById(id);
    }
    public Skill getSkillById(Long id) {
        return skillRepository.findById(id).orElseThrow(()->
                new RuntimeException("Skill not found with id: " + id));
    }
    public Page<Skill> findAllSkills(int pageNo, int pageSize, String sortBy, String sortDirection) {
        return skillRepository.findAll(PageRequest.of(pageNo, pageSize));
    }
    public Skill getSkillByName(String skillName) {
        return skillRepository.findBySkillName(skillName);
    }





}
