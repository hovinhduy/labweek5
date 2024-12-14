/*

 * @ (#) Job.java        1.0     11/6/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.backend.repositories;

import iuh.fit.backend.enums.JobType;
import iuh.fit.backend.enums.WorkMode;
import iuh.fit.backend.models.Company;
import iuh.fit.backend.models.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/6/2024
 */
@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {
    List<Job> findByCompanyId(Long companyId);

    List<Job> findByCompany(Company company);

    long countByType(JobType type);

    long countByWorkMode(WorkMode workMode);

    @Query("SELECT j FROM Job j WHERE " +
            "(:title IS NULL OR LOWER(j.name) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:minSalary IS NULL OR j.minSalary >= :minSalary) AND " +
            "(:maxSalary IS NULL OR j.maxSalary <= :maxSalary) AND " +
            "(:type IS NULL OR j.type = :type) AND " +
            "(:workMode IS NULL OR j.workMode = :workMode)")
    Page<Job> searchJobs(
            @Param("title") String title,
            @Param("minSalary") Double minSalary,
            @Param("maxSalary") Double maxSalary,
            @Param("type") JobType type,
            @Param("workMode") WorkMode workMode,
            Pageable pageable
    );

}
