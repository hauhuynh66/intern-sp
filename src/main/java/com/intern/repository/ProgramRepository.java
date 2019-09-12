package com.intern.repository;

import com.intern.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramRepository extends JpaRepository<Program,Integer> {
    Program findByName(String name);
}
