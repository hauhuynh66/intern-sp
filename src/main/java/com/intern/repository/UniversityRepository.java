package com.intern.repository;

import com.intern.model.University;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniversityRepository extends JpaRepository<University,Integer> {
    University findByName(String name);
    University findByCode(String code);
}
