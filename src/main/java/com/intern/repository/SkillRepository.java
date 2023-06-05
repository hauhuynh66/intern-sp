package com.intern.repository;

import com.intern.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill,Integer> {
    Skill findByName(String skillName);
}
