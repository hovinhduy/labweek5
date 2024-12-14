package iuh.fit.fontend.dto;

import java.math.BigDecimal;
import java.util.List;

import iuh.fit.backend.enums.JobType;
import iuh.fit.backend.enums.WorkMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobSearchResponseDTO {
    private Long id;
    private String name;
    private String description;
    private JobType type;
    private WorkMode workMode;
    private double salaryMin;
    private double salaryMax;
    private CompanyDTO company;
    private List<JobSkillDTO> jobSkills;
    private int applicationCount;
}

//@Data
//class CompanyDTO {
//    private Long id;
//    private String name;
//    private String logo;
//    private String address;
//}
//
//@Data
//class JobSkillDTO {
//    private Long id;
//    private String skillName;
//    private String skillLevel;
//}
