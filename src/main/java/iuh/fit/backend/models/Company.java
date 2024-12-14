/*
 * @ (#) Company.java        1.0     11/3/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class Company  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String about;
    private String phone;
    private String webURL;
    private String email;
    private String logoURL;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    @OneToMany(mappedBy = "company")
    private List<Job> jobs;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Company(String name, String about, String phone, String webURL, String email) {
        this.name = name;
        this.about = about;
        this.phone = phone;
        this.webURL = webURL;
        this.email = email;
    }

    public Company(String name, String about, String phone, String webURL, String email, Address address) {
        this.name = name;
        this.about = about;
        this.phone = phone;
        this.webURL = webURL;
        this.email = email;
        this.address = address;
    }


}
