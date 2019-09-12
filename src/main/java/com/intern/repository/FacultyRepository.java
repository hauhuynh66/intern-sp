package com.intern.repository;

import com.intern.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty,Integer> {
    List<Faculty> findByCode(String code);
    Faculty findByCodeAndName(String code,String name);
}
