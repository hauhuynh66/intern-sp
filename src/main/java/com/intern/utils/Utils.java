package com.intern.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.intern.model.Status;
import com.intern.model.University;
import com.intern.repository.AdminRepository;
import com.intern.repository.UniversityRepository;
import com.intern.security.CustomUserDetails;
import com.intern.serializer.CandidateSerializer;
import com.intern.serializer.EventSerializer;
import com.intern.serializer.StatusSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class Utils {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private UniversityRepository repository;
    public String mapper(Object object){
        String result = "";
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            result = objectMapper.writeValueAsString(object);
        }catch (JsonProcessingException jpe){
            System.out.println("Error : " + jpe.getMessage());
        }
        return result;
    }
    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder(5);
    }
    public String getCurrentPrincipal(){
        Object admin = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = "";
        if(admin instanceof CustomUserDetails){
            email = ((CustomUserDetails)admin).getUsername();
        }else {
            email = admin.toString();
        }
        return email;
    }
    public String getCurrentAdminName(){
        String email = getCurrentPrincipal();
        return adminRepository.findByMail(email).getName();
    }
    public Map<Integer, List<String>> mapData(List<University> universities){
        Map<Integer,List<String>> data = new HashMap<>();
        for(int i=0;i<universities.size();i++){
            String temp = universities.get(i).toString();
            temp = temp.substring(1,temp.length()-1);
            String []list = temp.split(",");
            data.put(i,new ArrayList<>());
            for(int j=0;j<list.length;j++){
                data.get(i).add(list[j]);
            }
        }
        return data;
    }
    public String getYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String year = calendar.get(Calendar.YEAR)+"";
        return year.substring(2);
    }
    public Map<Integer,List<String>> getInformation(List<Status> statuses){
        Map<Integer,List<String>> data = new HashMap<>();
        for(int i = 0; i< statuses.size(); i++){
            List<String> information = new ArrayList<>();
            information.add(statuses.get(i).getCandidate().getId()+"");
            information.add(statuses.get(i).getCandidate().getName());
            if(statuses.get(i).getCandidate().getGender()!=null){
                information.add(statuses.get(i).getCandidate().getGender());
            }else{
                information.add("-");
            }
            information.add(statuses.get(i).getCandidate().getDateOfBirth()+"");
            information.add(statuses.get(i).getCandidate().getEmail());
            information.add(statuses.get(i).getCandidate().getUniversity().getName());
            information.add(statuses.get(i).getCandidate().getFaculty().getCode());
            information.add(statuses.get(i).getStatus());
            data.put(i,information);
        }
        return data;
    }
    public String candidateMapper(Object candidate){
        String json = "";
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(new CandidateSerializer());
        mapper.registerModule(module);
        try{
            json = mapper.writeValueAsString(candidate);
        }catch (JsonProcessingException jpe){
            System.out.println("Error : CandidateSerializer " +jpe.getMessage());
        }
        return json;
    }
    public String eventMapper(Object events){
        String json = "";
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(new EventSerializer());
        mapper.registerModule(module);
        try {
            json = mapper.writeValueAsString(events);
        }catch (JsonProcessingException jpe){
            System.out.println("Error : Event Serializer "+ jpe.getMessage());
        }
        return json;
    }
    public String statusMapper(Object status){
        String json = "";
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(new StatusSerializer());
        mapper.registerModule(module);
        try {
            json = mapper.writeValueAsString(status);
        }catch (JsonProcessingException jpe){
            System.out.println("Error : "+ jpe.getMessage());
        }
        return json;
    }
    public boolean isAvailable(String str,List<String> list){
        for(int i=0;i<list.size();i++){
            if(str.equals(list.get(i))){
                return true;
            }
        }
        return false;
    }
    public List<String> facultyResolve(String faculty){
        List<String> codeName = new ArrayList<>();
        codeName.add(faculty.substring(0,faculty.indexOf("(")));
        codeName.add(faculty.substring(faculty.indexOf("(")+1,faculty.length()-1));
        return codeName;
    }
}
