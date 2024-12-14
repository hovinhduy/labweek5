/*
 * @ (#) Job.java        1.0     11/3/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.backend.models;

import iuh.fit.backend.enums.JobType;
import iuh.fit.backend.enums.WorkMode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/3/2024
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Long id;
    private String name;
    private String description;
    @Column(name = "job_start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Column(name = "job_end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;


    @Enumerated(EnumType.ORDINAL)
    private JobType type;

    @Enumerated(EnumType.ORDINAL)
    private WorkMode workMode;

    @Column(name = "min_salary")
    private double minSalary;

    @Column(name = "max_salary")
    private double maxSalary;

    @Column(name = "number_of_people")
    private String numberOfPeople;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "job")
    private List<JobSkill> jobSkills;




    public Job(String name, String description, Company company, List<JobSkill> jobSkills) {
        this.name = name;
        this.description = description;
        this.company = company;
        this.jobSkills = jobSkills;
    }

    public Job(Long id, String name, List<JobSkill> jobSkills) {
        this.id = id;
        this.name = name;
        this.jobSkills = jobSkills;
    }
}
