/*
 * @ (#) CompanyDTO.java        1.0     11/30/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.fontend.dto;

import iuh.fit.backend.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/30/2024
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {
    private String companyName;
    private String website;
    private String aboutUs;
    private String email;
    private String phone;
    private String streetNumber;
    private String street;
    private String city;
    private String zipCode;
    private String country;
    private String logoURL;
    private User user;
}
