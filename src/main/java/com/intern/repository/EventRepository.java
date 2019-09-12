package com.intern.repository;

import com.intern.model.Event;
import com.intern.model.Faculty;
import com.intern.model.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface EventRepository extends JpaRepository<Event,Integer> {
    Event findByCourseCode(String courseCode);
    Event findById(int id);
    List<Event> findByUniversityAndFaculty(University university, Faculty faculty);
    List<Event> findByPlannedStartDateAndPlannedEndDate(Date plannedStartDate, Date plannedEndDate);
    @Query("SELECT e from Event e WHERE e.plannedStartDate >= :plannedStartDate "
            + "AND e.plannedEndDate <= :plannedEndDate" )
    List<Event> findByStartDateAndEndDate(@Param("plannedStartDate") Date plannedStartDate,
                                          @Param("plannedEndDate") Date plannedEndDate);
}
