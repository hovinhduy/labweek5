/*
 * @ (#) CloudinaryService.java        1.0     11/29/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.backend.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/29/2024
 */
@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadFile(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("url").toString();
    }
    public String uploadPdf(MultipartFile file) {
        try {
            // Kiểm tra kích thước file
            if (file.getSize() > 5 * 1024 * 1024) { // 5MB
                throw new IllegalArgumentException("File size must be less than 5MB");
            }

            // Kiểm tra loại file
            if (!file.getContentType().equals("application/pdf")) {
                throw new IllegalArgumentException("Only PDF files are allowed");
            }

            Map<String, String> params = new HashMap<>();
            params.put("folder", "cvs");
            params.put("resource_type", "raw");

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), params);
            return uploadResult.get("url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Could not upload file", e);
        }
    }
}