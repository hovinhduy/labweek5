/*
 * @ (#) Candidate.java        1.0     11/3/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.backend.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

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
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "candidate_id")
    private Long id;
    private String phone;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;
    private String email;
    private String fullName;

    @OneToMany(mappedBy = "candidate")
    @ToString.Exclude
    private List<Experience> experiences;

    @OneToMany(mappedBy = "candidate")
    @ToString.Exclude
    private List<CandidateSkill> candidateSkills;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "candidate")
    @ToString.Exclude
    private List<SavedJob> savedJobs;

    public Candidate(String phone, LocalDate dob, String email, String fullName) {
        this.phone = phone;
        this.dob = dob;
        this.email = email;
        this.fullName = fullName;
    }

    public Candidate(String phone, LocalDate dob, String email, String fullName, Address address) {
        this.phone = phone;
        this.dob = dob;
        this.email = email;
        this.fullName = fullName;
        this.address = address;
    }

    public Candidate(Long id, String fullName, List<CandidateSkill> candidateSkills) {
        this.id = id;
        this.fullName = fullName;
        this.candidateSkills = candidateSkills;
    }
}
