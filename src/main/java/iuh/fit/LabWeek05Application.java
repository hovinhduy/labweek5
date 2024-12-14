package iuh.fit;

import com.neovisionaries.i18n.CountryCode;
import iuh.fit.backend.enums.SkillType;
import iuh.fit.backend.enums.UserRole;
import iuh.fit.backend.models.*;
import iuh.fit.backend.repositories.*;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Random;

@SpringBootApplication
public class LabWeek05Application {

    public static void main(String[] args) {
        SpringApplication.run(LabWeek05Application.class, args);
    }
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Bean
    CommandLineRunner initData(){
        return args -> {
            Random rnd  = new Random();
            Faker faker = new Faker();
            for (int i = 0; i < 100; i++) {
                Address add = new Address("HCM",CountryCode.VN ,"HCM",rnd.nextInt(70000,80000)+"", rnd.nextInt(1,1000)+"");
//                addressRepository.save(add);
//                Candidate can = new Candidate(rnd.nextInt(1000000,9999999)+"", LocalDate.now(),"email_"+i+"@gmail.com" ,"Fullname_"+i);
//                can.setAddress(add);
//                add.setCandidate(can);
//                candidateRepository.save(can);
                Company com = new Company("Company_"+faker.company().name(), "About_"+i, faker.phoneNumber().phoneNumber(), "www.company_"+i+".com", faker.internet().emailAddress(),add);
//                companyRepository.save(com);
//                Job job = new Job("Job_"+faker.job().position(), "Description_"+i,com,null);
//                jobRepository.save(job);
                //radom skilltype
                Skill skill = new Skill(faker.job().keySkills(),"Description_"+i,SkillType.values()[rnd.nextInt(0,2)]);
                skillRepository.save(skill);
                //add sắn user cho company
                User user = new User("user_"+i,passwordEncoder.encode("pass_"+i), UserRole.COMPANY);
                user.setCompany(com);
//                user.setCandidate(can);
//                can.setUser(user);
                com.setUser(user);
                userRepository.save(user);



            }
        };
    }



}
