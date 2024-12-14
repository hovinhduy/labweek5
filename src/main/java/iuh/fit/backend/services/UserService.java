/*
 * @ (#) UserServices.java        1.0     11/12/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.backend.services;

import iuh.fit.backend.enums.UserRole;
import iuh.fit.backend.models.Address;
import iuh.fit.backend.models.Candidate;
import iuh.fit.backend.models.Company;
import iuh.fit.backend.models.User;
import iuh.fit.backend.repositories.CandidateRepository;
import iuh.fit.backend.repositories.CompanyRepository;
import iuh.fit.backend.repositories.UserRepository;
import iuh.fit.fontend.dto.UserCompanyDTO;
import iuh.fit.fontend.dto.UserRegistrationDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
//
import java.time.LocalDate;
import java.util.Collections;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/12/2024
 */
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CompanyRepository companyRepository;

    public void registerCandidate(UserRegistrationDto registrationDto) {
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new DataIntegrityViolationException("Tên người dùng đã tồn tại."); // Hoặc một exception tùy chỉnh
        }

        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

        Candidate candidate = new Candidate();
        candidate.setPhone(registrationDto.getPhone());
        candidate.setDob(registrationDto.getDateOfBirth());
        candidate.setEmail(registrationDto.getEmail());
        candidate.setFullName(registrationDto.getFullName());
        user.setRole(registrationDto.getRole());
        user.setCandidate(candidate); // Chỉ cần set quan hệ, không cần save candidate riêng
        candidate.setUser(user);


        userRepository.save(user); // Chỉ lưu user
    }
    public void registerCompany(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DataIntegrityViolationException("Tên người dùng đã tồn tại."); // Hoặc một exception tùy chỉnh
        }

//        User user = new User();
        user.setUsername(user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole());



        userRepository.save(user); // Chỉ lưu user
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
    //get user by username
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}