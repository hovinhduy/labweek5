package iuh.fit.backend.repositories;

import iuh.fit.backend.models.Address;
import iuh.fit.backend.models.Candidate;
import iuh.fit.backend.models.Company;
import iuh.fit.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate,Long> {
    Optional<Candidate> findByUser_Username(String username);
    Optional<Candidate> findByUser (User user);
}
