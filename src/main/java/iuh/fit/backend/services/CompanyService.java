/*
 * @ (#) CompanyService.java        1.0     11/7/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.backend.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.neovisionaries.i18n.CountryCode;

import iuh.fit.backend.models.Address;
import iuh.fit.backend.models.Company;
import iuh.fit.backend.models.Job;
import iuh.fit.backend.models.User;
import iuh.fit.backend.repositories.CompanyRepository;
import iuh.fit.fontend.dto.CompanyDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/7/2024
 */
@Service
@Slf4j
public class CompanyService {
    private final CompanyRepository companyRepository;

    @Autowired
    private Cloudinary cloudinary;

    // Constructor injection
    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    public Optional<Company> findByEmail(String email) {
        return companyRepository.findByEmail(email);
    }

    public Company getCompanyByUsername(String username) {
        return companyRepository.findByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Company not found"));
    }
    public Company getCompanyBy_Username(String username) {
        return companyRepository.findByUserUsername(username)
                .orElse(null);
    }


    public String uploadLogo(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "company-logos",
                            "unique_filename", true,
                            "overwrite", true));
            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            log.error("Error uploading file to Cloudinary", e);
            throw new RuntimeException("Could not upload file", e);
        }
    }

    public Company saveCompany(CompanyDTO companyDTO) {
        Company company = new Company();
        Address address = new Address();
        address.setNumber(companyDTO.getStreetNumber());
        address.setCity(companyDTO.getCity());
        address.setStreet(companyDTO.getStreet());
        address.setZipCode(companyDTO.getZipCode());
        address.setCountryCode(CountryCode.valueOf(companyDTO.getCountry()));
        company.setName(companyDTO.getCompanyName());
        company.setEmail(companyDTO.getEmail());
        company.setPhone(companyDTO.getPhone());
        company.setAddress(address);
        company.setName(companyDTO.getCompanyName());
        company.setPhone(companyDTO.getPhone());
        company.setEmail(companyDTO.getEmail());
        company.setLogoURL(companyDTO.getLogoURL());
        company.setAbout(companyDTO.getAboutUs());
        company.setWebURL(companyDTO.getWebsite());
        company.setUser(companyDTO.getUser());
        return companyRepository.save(company);
    }
    public boolean isCompanySetup(Long userId) {
        // Implement logic để kiểm tra xem company đã setup chưa
        // Ví dụ: kiểm tra các thông tin bắt buộc đã được điền đầy đủ chưa
        Company company = companyRepository.findByUserId(userId);
        return company != null && isSetupComplete(company); // Giả sử có field isSetupComplete
    }
    private boolean isSetupComplete(Company company) {
        // Implement logic kiểm tra thông tin company đã đầy đủ chưa
        return company.getName() != null &&
                company.getAddress() != null &&
                company.getPhone() != null;
    }


}
