package com.intern.repository;

import com.intern.model.Candidate;
import com.intern.model.University;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate,Integer> {
    Candidate findByNameAndEmail(String name,String email);
    Candidate findById(int id);
    List<Candidate> findByNameContainingIgnoreCase(String name);
    Candidate findByNameAndUniversity(String name, University university);
}
