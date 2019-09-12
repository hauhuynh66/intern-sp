package com.intern.repository;

import com.intern.model.Candidate;
import com.intern.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import com.intern.model.Event;

public interface StatusRepository extends JpaRepository<Status,Integer> {
    Status findByEventAndCandidate(Event event, Candidate candidate);
    List<Status> findByEvent(Event event);
    List<Status> findByCandidate(Candidate candidate);
    List<Status> findByEventAndStatus(Event event,String status);
}
