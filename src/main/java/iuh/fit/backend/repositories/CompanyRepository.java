/*

 * @ (#) CopanyRepository.java        1.0     11/6/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.backend.repositories;

import iuh.fit.backend.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/6/2024
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByEmail(String email);

    Optional<Company> findByUser_Username(String username);

    Company findByUserId(Long userId);


    Optional<Company> findByUserUsername(String username);

}
