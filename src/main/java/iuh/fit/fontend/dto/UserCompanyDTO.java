/*
 * @ (#) UserCompanyDTO.java        1.0     11/14/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.fontend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/14/2024
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCompanyDTO {
    @NotBlank(message = "Username is required")
    private String username;

    private String password;
    private String companyName;
    private String about;
    private String phone;
    private String webURL;
    private String email;

}
