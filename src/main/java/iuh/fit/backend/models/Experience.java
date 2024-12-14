/*
 * @ (#) Experience.java        1.0     11/3/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/3/2024
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate toDate;
    private LocalDate fromDate;
    private String companyName;
    private String role;
    private String workDescription;

    @ManyToOne
    private Candidate candidate;
}
