/*
 * @ (#) Address.java        1.0     11/3/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.backend.models;

import com.neovisionaries.i18n.CountryCode;
import jakarta.persistence.*;
import lombok.*;

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
public class Address  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String number;
    private String street;
    private String city;
    private String zipCode;
    private CountryCode countryCode;

    @OneToOne(mappedBy = "address" ,cascade = CascadeType.ALL)
    private Candidate candidate;
    @OneToOne(mappedBy = "address" ,cascade = CascadeType.ALL)
    private Company company;

    public Address(String city, CountryCode countryCode, String street, String zipCode, String number) {
        this.city = city;
        this.countryCode = countryCode;
        this.street = street;
        this.zipCode = zipCode;
        this.number = number;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s %s, %s",
                number,
                street,
                city,
                zipCode,
                countryCode);
    }

}
