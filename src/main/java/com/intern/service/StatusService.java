package com.intern.service;

import com.intern.model.Status;
import com.intern.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusService {
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private CustomValidationService validationService;
    public boolean saveStatus(Status status) throws Exception{
        validationService.validGrade(status.getFinalGrade());
        return statusRepository.save(status)!=null;
    }
}
