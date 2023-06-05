package com.intern.service;

import com.intern.model.Event;
import com.intern.model.Faculty;
import com.intern.model.Skill;
import com.intern.model.University;
import com.intern.repository.SkillRepository;
import com.intern.utils.Converter;
import com.intern.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class CustomValidationService {
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private Utils utils;
    @Autowired
    private Converter converter;
    public void validName(String name) throws Exception {
        if(name.isEmpty()){
            throw new Exception("Name can't be empty");
        }
        if(name.length()<8){
            throw new Exception("Name too short");
        }
        if(name.length()>100){
            throw new Exception("Name too long");
        }
    }
    public void validEmail(String email) throws Exception{
        if(email==null){
            throw new NullPointerException("Mail is null");
        }
        if(!email.contains("@")){
            throw new Exception("Email should have a \'@\' character");
        }
        if(!email.contains(".")){
            throw new Exception("Email should contain a dot");
        }
        if(email.length()<8){
            throw new Exception("Email should have more than 8 characters");
        }
    }
    public void validPhoneNumber(String phone)throws Exception{
        if(phone.isEmpty()){
            throw new Exception("Phone number should not be empty");
        }
        if(phone.length()<8){
            throw new Exception("Phone number should have more than 8 numbers");
        }
        if(phone.length()>14){
            throw new Exception("Phone number cannot have more than 14 numbers");
        }
    }
    public void validSkill(String skillName) throws Exception{
        if(skillName==null){
            throw new Exception("Skill name cannot be null");
        }else {
            boolean t = false;
            List<Skill> skills = skillRepository.findAll();
            for(Skill skill:skills){
                if(skill.getName().equals(skillName)){
                    t = true;
                    break;
                }
            }
            if(!t){
                throw new Exception("Skill name not found in database");
            }
        }
    }
    public void validFaculty(University university, Faculty faculty) throws Exception{
        Set<Faculty> faculties = university.getFaculties();
        boolean isContains = false;
        for(Faculty f:faculties){
            if(f.getCode().equals(faculty.getCode())&&f.getName().equals(faculty.getName())){
                isContains = true;
            }
        }
        if(!isContains){
            throw new Exception(university.getName()+" doesn't have faculty named " +faculty.getName());
        }
    }
    public void validGrade(@Nullable Double grade)throws Exception{
        if(grade==null){
            return;
        }else{
            if(grade<0){
                throw new Exception("??????? Score");
            }
            if(grade>10){
                throw new Exception("He can't be that good");
            }
        }
    }
    public void validDeleteEvent(Event event)throws Exception{
        Date date = new Date();
        if(date.after(event.getActualEndDate())){
            throw new Exception("Can't delete event that already finish!");
        }
        if(date.after(event.getActualStartDate())&&date.before(event.getActualEndDate())){
            throw new Exception("Can't delete event that is in-progress either!");
        }
    }
    public void validBirthDay(Date d)throws Exception{
        Date date = new Date();

        long mldiff = date.getTime()-d.getTime();
        if(mldiff<0){
            throw new Exception("Date of birth not valid");
        }else{
            long diff = TimeUnit.DAYS.convert(mldiff,TimeUnit.MILLISECONDS);
            if(diff<=5475){
                throw new Exception("Not even 15 years old????");
            }
        }
    }
}
