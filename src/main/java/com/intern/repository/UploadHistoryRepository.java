package com.intern.repository;

import com.intern.model.Admin;
import com.intern.model.UploadHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UploadHistoryRepository extends JpaRepository<UploadHistory,Integer> {
    List<UploadHistory> findByAdmin_Mail(String mail);
}
