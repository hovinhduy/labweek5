/*
 * @ (#) SkillController.java        1.0     11/6/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.fontend.controllers;

import iuh.fit.backend.models.Job;
import iuh.fit.backend.models.Skill;
import iuh.fit.backend.services.JobService;
import iuh.fit.backend.services.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/6/2024
 */
@Controller
public class SkillController {
    @Autowired
    private SkillService skillService;
//    @GetMapping("/skills")
    @GetMapping("/skills/paging")
    public String showJobListPaging(Model model,
                                    @RequestParam("page") Optional<Integer> page,
                                    @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);
        Page<Skill> skillPage= skillService.findAllSkills(
                currentPage - 1,pageSize,"id","asc");


        model.addAttribute("skillPage", skillPage);
        System.out.println("skillPage: " + skillPage.getContent());

        int totalPages = skillPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "skills/skills-list-paging";
    }
    @PostMapping("/skills")
    public String createSkill(@ModelAttribute("skill") Skill skill) {
        skillService.createSkill(skill);
        return "redirect:/skills";
    }
    @GetMapping("/skill/add")
    public String showSkillForm(Model model) {
        model.addAttribute("skill", new Skill());
        return "skills/create";  // tên file HTML của form
    }

    @PostMapping("/save-skill")
    public String saveSkill(@ModelAttribute Skill skill) {
        skillService.createSkill(skill);
        return "redirect:skills/paging";
    }


}
