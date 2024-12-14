/*

 * @ (#) UserRepository.java        1.0     11/12/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.backend.repositories;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/12/2024
 */

import iuh.fit.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

}