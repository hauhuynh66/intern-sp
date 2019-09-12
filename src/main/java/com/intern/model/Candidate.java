package com.intern.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "candidate")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = true)
    private int id;
    private int nationalId;
    @Column(nullable = false)
    private String account;
    @Column(nullable = false)
    private String name;
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    private String gender;
    @Column(nullable = false)
    private String email;
    private String phone;
    private String facebook;
    @Temporal(TemporalType.DATE)
    private Date graduationDate;
    @Temporal(TemporalType.DATE)
    private Date fulltimeDate;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name= "university_id",referencedColumnName = "id")
    private University university;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "faculty_id",referencedColumnName = "id")
    private Faculty faculty;
    private double gpa;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "skill",referencedColumnName = "id")
    private Skill skill;
    @OneToMany(mappedBy = "candidate")
    private Set<Status> statuses = new HashSet<>();

    public Candidate() {
    }

    public Candidate(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNationalId() {
        return nationalId;
    }

    public void setNationalId(int nationalId) {
        this.nationalId = nationalId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(@Nullable Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public Date getGraduationDate() {
        return graduationDate;
    }

    public void setGraduationDate(@Nullable Date graduationDate) {
        this.graduationDate = graduationDate;
    }

    public Date getFulltimeDate() {
        return fulltimeDate;
    }

    public void setFulltimeDate(@Nullable Date fulltimeDate) {
        this.fulltimeDate = fulltimeDate;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(@Nullable double gpa) {
        this.gpa = gpa;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public Set<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(Set<Status> statuses) {
        this.statuses = statuses;
    }
}
