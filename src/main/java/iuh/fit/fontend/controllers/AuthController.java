/*
 * @ (#) a.java        1.0     11/14/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.fontend.controllers;

import iuh.fit.backend.enums.UserRole;
import iuh.fit.backend.models.User;
import iuh.fit.backend.services.CompanyService;
import iuh.fit.backend.services.UserService;
import iuh.fit.fontend.dto.UserCompanyDTO;
import iuh.fit.fontend.dto.UserRegistrationDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/14/2024
 */
// Controller classes
@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @GetMapping("/registerCandidate")
    public String showRegistrationFormCandidate(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "auth/register";
    }

    @PostMapping("/registerCandidate")
    public String registerCandidate(@ModelAttribute("user") @Valid UserRegistrationDto userDto,
                               BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "auth/register";
        }

        try {
            userDto.setRole(UserRole.CANDIDATE);
            userService.registerCandidate(userDto);
            return "redirect:/login";
        } catch (DataIntegrityViolationException ex) {
            // Thêm thông báo lỗi vào RedirectAttributes
            redirectAttributes.addFlashAttribute("error", ex.getMessage()); // Hoặc một message tùy chỉnh
            // Chuyển hướng lại về trang đăng ký
            return "redirect:/registerCandidate"; // Đảm bảo bạn có route /register
        }
    }


    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }

    @GetMapping("/registerCompany")
    public String showRegistrationFormCompany(Model model) {
        model.addAttribute("userCompany", new UserCompanyDTO());
        return "auth/test";
    }

    @PostMapping("/registerCompany")
    public String registerCompayny(@ModelAttribute("userCompany") @Valid User user,
                                   BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "auth/test";
        }

        try {
            user.setRole(UserRole.COMPANY);
            userService.registerCompany(user);
            return "redirect:/login";
        } catch (DataIntegrityViolationException ex) {
            // Thêm thông báo lỗi vào RedirectAttributes
            redirectAttributes.addFlashAttribute("error", ex.getMessage()); // Hoặc một message tùy chỉnh
            // Chuyển hướng lại về trang đăng ký
            return "redirect:/registerCompany"; // Đảm bảo bạn có route /register
        }
    }




}