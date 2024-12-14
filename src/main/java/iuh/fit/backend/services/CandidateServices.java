package iuh.fit.backend.services;


import iuh.fit.backend.models.*;
import iuh.fit.backend.repositories.CandidateRepository;
import iuh.fit.backend.repositories.CandidateSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.List;

@Service
public class CandidateServices {
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private CandidateSkillRepository candidateSkillRepository;

    public Page<Candidate> findAll(int pageNo, int pageSize, String sortBy,
                                   String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return candidateRepository.findAll(pageable);//findFirst.../findTop...
    }

    public Candidate getCandidateByUsername(String username) {
        System.out.println("username: " + username);
        return candidateRepository.findByUser_Username(username).orElseThrow(() -> new RuntimeException("Candidate not found"));
    }
    public Candidate updateCandidate(Long id, Candidate candidate) {
        Candidate existingCandidate = candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        existingCandidate.setFullName(candidate.getFullName());
        existingCandidate.setPhone(candidate.getPhone());
        existingCandidate.setDob(candidate.getDob());
        existingCandidate.setEmail(candidate.getEmail());
        if (existingCandidate.getAddress() == null) {
            existingCandidate.setAddress(new Address());
        }
        existingCandidate.getAddress().setNumber(candidate.getAddress().getNumber());
        existingCandidate.getAddress().setStreet(candidate.getAddress().getStreet());
        existingCandidate.getAddress().setCity(candidate.getAddress().getCity());
        existingCandidate.getAddress().setZipCode(candidate.getAddress().getZipCode());
        existingCandidate.getAddress().setCountryCode(candidate.getAddress().getCountryCode());

        return candidateRepository.save(existingCandidate);
    }
    public void save(Candidate candidate) {
        candidateRepository.save(candidate);
    }
    public void saveAll(List<CandidateSkill> candidateSkills) {
        candidateSkillRepository.saveAll(candidateSkills);
    }
    public Candidate getCandidateByUser(User user) {
        return candidateRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
    }
    public Candidate getCandidateById(Long id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
    }

}