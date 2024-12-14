/*
 * @ (#) FileUploadController.java        1.0     11/29/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.fontend.controllers;

import iuh.fit.backend.services.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/29/2024
 */
@Controller
public class FileUploadController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping("/upload")
    public String uploadPage() {
        return "auth/upload";
    }

    @PostMapping("/upload")
    public String uploadFile(MultipartFile file, Model model) {
        try {
            String url = cloudinaryService.uploadFile(file);
            model.addAttribute("message", "File uploaded successfully!");
            model.addAttribute("url", url);
        } catch (Exception e) {
            model.addAttribute("message", "Upload failed: " + e.getMessage());
        }
        return "auth/upload";
    }
}
