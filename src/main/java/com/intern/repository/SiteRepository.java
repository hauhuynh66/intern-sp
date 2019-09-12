package com.intern.repository;

import com.intern.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteRepository extends JpaRepository<Site,Integer> {
    Site findBySite(String site);
}
