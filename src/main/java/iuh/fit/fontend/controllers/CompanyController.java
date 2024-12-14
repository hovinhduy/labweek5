/*
 * @ (#) CompanyController.java        1.0     11/7/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.fontend.controllers;

import iuh.fit.backend.models.User;
import iuh.fit.backend.services.CloudinaryService;
import iuh.fit.backend.services.CompanyService;
import iuh.fit.backend.services.UserService;
import iuh.fit.fontend.dto.CompanyDTO;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import com.neovisionaries.i18n.CountryCode;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/7/2024
 */
@Controller
@SessionAttributes("company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private UserService userService;

    @ModelAttribute("company")
    public CompanyDTO companyDTO() {
        return new CompanyDTO();
    }

    @GetMapping("/company/setup")
    public String showSetupForm(Model model) {
        model.addAttribute("currentStep", 1);
        model.addAttribute("progress", 33);
        return "companys/company-setup";
    }

    @PostMapping("/company-setup/company-info")
    public String saveCompanyInfo(@ModelAttribute("company") CompanyDTO company,
            @RequestParam("logo") MultipartFile logo) throws IOException {
        if (!logo.isEmpty()) {
            String logoUrl = cloudinaryService.uploadFile(logo);
            company.setLogoURL(logoUrl);
        }
        return "redirect:/company-setup/contact";
    }

    @GetMapping("/company-setup/contact")
    public String showContactForm(Model model) {
        model.addAttribute("currentStep", 2);
        model.addAttribute("progress", 66);
        return "companys/company-setup";
    }

    @PostMapping("/company-setup/contact")
    public String saveContact(@ModelAttribute("company") CompanyDTO company) {
        return "redirect:/company-setup/address";
    }

    @GetMapping("/company-setup/address")
    public String showAddressForm(Model model) {
        model.addAttribute("currentStep", 3);
        model.addAttribute("progress", 100);
        // Add list of countries if needed
        model.addAttribute("countries", CountryCode.values());
        return "companys/company-setup";
    }

    @PostMapping("/company-setup/address")
    public String saveAddress(@ModelAttribute("company") CompanyDTO company,
            SessionStatus sessionStatus, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        company.setUser(user);
        // Save complete company data
        companyService.saveCompany(company);
        // End session
        sessionStatus.setComplete();
        return "redirect:/company/job/post";
    }
    @PostMapping("/company-setup/upload-logo")
    @ResponseBody
    public Map<String, Object> uploadLogo(@RequestParam("logo") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            String logoUrl = cloudinaryService.uploadFile(file);
            response.put("success", true);
            response.put("url", logoUrl);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }

}
