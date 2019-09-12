package com.intern.testdb;

import com.intern.model.Admin;
import com.intern.model.Program;
import com.intern.model.Site;
import com.intern.model.Skill;
import com.intern.repository.*;
import com.intern.service.CandidateService;
import com.intern.service.MainService;
import com.intern.utils.Converter;
import com.intern.utils.Excel;
import com.intern.utils.ExcelService;
import com.intern.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLine implements CommandLineRunner {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private UniversityRepository universityRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private MainService mainService;
    @Autowired
    private CandidateService candidateService;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private Utils utils;
    @Autowired
    private Excel excel;
    @Autowired
    private Converter converter;
    @Autowired
    private ExcelService data;
    @Override
    public void run(String... args) throws Exception {
        Admin admin = new Admin();
        admin = new Admin();
        admin.setName("Hau");
        admin.setPassword(utils.encoder().encode("Hauhuynh"));
        admin.setMail("hauhuynh66@gmail.com");
        admin.setRole("ADMIN");
        admin.setPermission("ACCESS_MANAGE");
        adminRepository.save(admin);
        Admin owner = new Admin();
        owner = new Admin();
        owner.setName("HauHP");
        owner.setPassword(utils.encoder().encode("phuochau"));
        owner.setMail("phuochau@gmail.com");
        owner.setRole("OWNER");
        owner.setPermission("ACCESS_MANAGE");
        adminRepository.save(owner);
        loadSkill();
        loadProgram();
        loadSites();
    }
    private void loadSkill(){
        Skill []skills = new Skill[9];
        for (int i=0;i<skills.length;i++){
            skills[i] = new Skill();
        }
        skills[0].setSkillName("NET");
        skills[1].setSkillName("TEST");
        skills[2].setSkillName("JAVA");
        skills[3].setSkillName("IOS");
        skills[4].setSkillName("ANDROID");
        skills[5].setSkillName("Đề tài tốt nghiệp");
        skills[6].setSkillName("AI&ML");
        skills[7].setSkillName("EMBED");
        skills[8].setSkillName("ALL");
        for(int i =0;i<skills.length;i++){
            skillRepository.save(skills[i]);
        }
    }
    private void loadProgram(){
        Program []programs = new Program[10];
        for(int i =0;i<programs.length;i++){
            programs[i] = new Program();
        }
        programs[0].setName("Global Software Talent");
        programs[0].setCode("GST");
        programs[0].setTime(360);
        programs[1].setName("THESIS");
        programs[1].setCode("THESIS");
        programs[1].setTime(0);
        programs[2].setName("GST LITE");
        programs[2].setCode("LITE");
        programs[2].setTime(320);
        programs[3].setName("INTERNSHIP");
        programs[3].setCode("INTE");
        programs[3].setTime(320);
        programs[4].setName("SEMINAR");
        programs[4].setCode("SEMI");
        programs[4].setTime(4);
        programs[5].setName("JOB FAIR");
        programs[5].setCode("JOB");
        programs[5].setTime(0);
        programs[6].setName("FSOFT TOUR");
        programs[6].setCode("FTOUR");
        programs[6].setTime(3);
        programs[7].setName("Contest Sponsor");
        programs[7].setCode("CSR");
        programs[7].setTime(0);
        programs[8].setName("REC");
        programs[8].setCode("REC");
        programs[8].setTime(0);
        programs[9].setName("ONLINE");
        programs[9].setCode("ONLINE");
        programs[9].setTime(0);
        for(int i=0;i<programs.length;i++){
            programRepository.save(programs[i]);
        }
    }
    private void loadSites(){
        Site []sites = new Site[3];
        for(int i=0;i<3;i++){
            sites[i] = new Site();
        }
        sites[0].setSite("HCM");
        sites[0].setDescription("Hồ Chí Minh");
        sites[1].setSite("HN");
        sites[1].setDescription("Hà Nội");
        sites[2].setSite("DN");
        sites[2].setDescription("Đà Nẵng");
        for(Site site:sites){
            siteRepository.save(site);
        }
    }
}
