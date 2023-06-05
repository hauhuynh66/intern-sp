package com.intern.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "event")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "sq")
    @SequenceGenerator(name = "sq",initialValue = 1,allocationSize = 1)
    private int id;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "site",referencedColumnName = "id")
    private Site site;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "university_id",referencedColumnName = "id")
    private University university;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "courseName",referencedColumnName = "id")
    private Program program;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "faculty_id",referencedColumnName = "id")
    private Faculty faculty;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "skill",referencedColumnName = "id")
    private Skill skill;
    @Column(nullable = false)
    private String courseCode;
    private String subjectType;
    private String format;
    @Temporal(TemporalType.DATE)
    private Date plannedStartDate;
    @Temporal(TemporalType.DATE)
    private Date plannedEndDate;
    private double plannedExpense;
    private String budgetCode;
    @Temporal(TemporalType.DATE)
    private Date actualStartDate;
    @Temporal(TemporalType.DATE)
    private Date actualEndDate;
    private int actualLearningTime;
    private double actualExpense;
    private String trainingFeedback;
    private String feedbackContent;
    private String feedbackTeacher;
    private String feedbackOrganization;
    private Date updateDate;
    @OneToMany(mappedBy = "event")
    private Set<Status> statuses;

    public Event() {
    }

    public Event(int id, String courseCode) {
        this.id = id;
        this.courseCode = courseCode;
    }

    public String getYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String year = calendar.get(Calendar.YEAR)+"";
        return year.substring(2);
    }
    public String formatInt(int i){
        String format = String.format("%03d",i);
        return format;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode() {
        this.courseCode = this.university.getCode()+"_"+
                            this.program.getCode()+"_"+
                            this.skill.getName()+"_"+
                            this.site.getSite()+getYear(this.plannedStartDate)+"_"+
                            formatInt(this.id);
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Date getPlannedStartDate() {
        return plannedStartDate;
    }

    public void setPlannedStartDate(Date plannedStartDate) {
        this.plannedStartDate = plannedStartDate;
    }

    public Date getPlannedEndDate() {
        return plannedEndDate;
    }

    public void setPlannedEndDate(Date plannedEndDate) {
        this.plannedEndDate = plannedEndDate;
    }

    public double getPlannedExpense() {
        return plannedExpense;
    }

    public void setPlannedExpense(double plannedExpense) {
        this.plannedExpense = plannedExpense;
    }

    public String getBudgetCode() {
        return budgetCode;
    }

    public void setBudgetCode(String budgetCode) {
        this.budgetCode = budgetCode;
    }

    public Date getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(Date actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public Date getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(Date actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public int getActualLearningTime() {
        return actualLearningTime;
    }

    public void setActualLearningTime(int actualLearningTime) {
        this.actualLearningTime = actualLearningTime;
    }

    public double getActualExpense() {
        return actualExpense;
    }

    public void setActualExpense(double actualExpense) {
        this.actualExpense = actualExpense;
    }

    public String getTrainingFeedback() {
        return trainingFeedback;
    }

    public void setTrainingFeedback(String trainingFeedback) {
        this.trainingFeedback = trainingFeedback;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }

    public String getFeedbackTeacher() {
        return feedbackTeacher;
    }

    public void setFeedbackTeacher(String feedbackTeacher) {
        this.feedbackTeacher = feedbackTeacher;
    }

    public String getFeedbackOrganization() {
        return feedbackOrganization;
    }

    public void setFeedbackOrganization(String feedbackOrganization) {
        this.feedbackOrganization = feedbackOrganization;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Set<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(Set<Status> statuses) {
        this.statuses = statuses;
    }
}
