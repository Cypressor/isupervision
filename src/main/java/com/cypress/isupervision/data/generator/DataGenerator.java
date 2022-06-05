/*
 * iSupervision
 * DataGenerator
 * generates sample data for testing
 * Author: Martin Lunelli
 * Last Change: 04.06.2022
 */

package com.cypress.isupervision.data.generator;

import com.cypress.isupervision.data.Role;
import com.cypress.isupervision.data.entity.project.BachelorsThesis;
import com.cypress.isupervision.data.entity.project.MastersThesis;
import com.cypress.isupervision.data.entity.user.Administrator;
import com.cypress.isupervision.data.entity.user.Assistant;
import com.cypress.isupervision.data.entity.project.Project;
import com.cypress.isupervision.data.entity.user.Student;
import com.cypress.isupervision.data.service.*;
import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import java.time.LocalDateTime;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringComponent
public class DataGenerator
{
    private Student student = new Student();
    private List<Student> students;
    private Assistant assistant = new Assistant();
    private List<Assistant> assistants;
    private Administrator admin = new Administrator();
    private List<Student> attendants=new ArrayList<>();
    private List<Student> finishers=new ArrayList<>();
    private PasswordEncoder passwordEncoder;
    private StudentRepository studentRepository;
    private AssistantRepository assistantRepository;
    private AdministratorRepository administratorRepository;
    private ProjectRepository projectRepository;
    private BachelorsThesisRepository bachelorsThesisRepository;
    private MastersThesisRepository mastersThesisRepository;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private Random rng = new Random();
    private int randomNumber;
    private int seed;

    @Bean
    public CommandLineRunner loadData(PasswordEncoder passwordEncoder, UserRepository userRepository,
                                      ProjectRepository projectRepository, BachelorsThesisRepository bachelorsThesisRepository,
                                      MastersThesisRepository mastersThesisRepository, StudentRepository studentRepository,
                                      AssistantRepository assistantRepository, AdministratorRepository administratorRepository)
    {
        this.passwordEncoder=passwordEncoder;
        this.studentRepository=studentRepository;
        this.assistantRepository=assistantRepository;
        this.administratorRepository=administratorRepository;
        this.projectRepository=projectRepository;
        this.bachelorsThesisRepository=bachelorsThesisRepository;
        this.mastersThesisRepository=mastersThesisRepository;

        return args ->
        {
            if (userRepository.count() != 0L)
            {
                logger.info("Using existing database");
                return;
            }

            logger.info("Generating demo data");

            createDefaultUsers();
            generateStudents();
            generateAssistants();
            generateProjects();
            generateBachelorsTheses();
            generateMastersTheses();

            logger.info("Generated demo data");
        };
    }

    private void generateMastersTheses()
    {
        logger.info("... generating 25 MastersThesis entities...");
        seed = 567;
        List<MastersThesis> mastersTheses;
        MastersThesis mastersThesis;

        ExampleDataGenerator<MastersThesis> masterarbeitRepositoryGenerator = new ExampleDataGenerator<>(
                MastersThesis.class, LocalDateTime.of(2022, 5, 24, 0, 0, 0));
        masterarbeitRepositoryGenerator.setData(MastersThesis::setTitle, DataType.SENTENCE);
        masterarbeitRepositoryGenerator.setData(MastersThesis::setDeadline, DataType.DATE_NEXT_1_YEAR);
        mastersTheses = masterarbeitRepositoryGenerator.create(25, seed);
        attendants.clear();
        attendants.addAll(finishers);
        finishers.clear();
        for (int i = 0; i < mastersTheses.size(); i++)
        {
            mastersThesis = mastersTheses.get(i);
            randomNumber = rng.nextInt(assistants.size());
            mastersThesis.setAssistant(assistants.get(randomNumber));
            if(attendants.size()>0)
            {
                randomNumber = rng.nextInt(attendants.size());
                mastersThesis.setStudent(attendants.get(randomNumber).getFirstname()+" " + attendants.get(randomNumber).getLastname());
                if((randomNumber%3)>0)
                {
                    mastersThesis.setFinished(true);
                }
                attendants.remove(attendants.get(randomNumber));
            }
            mastersTheses.set(i, mastersThesis);
        }
        mastersThesisRepository.saveAll(mastersTheses);
    }

    private void generateBachelorsTheses()
    {
        logger.info("... generating 25 BachelorsThesis entities...");
        seed = 456;
        List<BachelorsThesis> bachelorsTheses;
        BachelorsThesis bachelorsThesis;
        ExampleDataGenerator<BachelorsThesis> bachelorarbeitRepositoryGenerator = new ExampleDataGenerator<>(
                BachelorsThesis.class, LocalDateTime.of(2022, 5, 24, 0, 0, 0));
        bachelorarbeitRepositoryGenerator.setData(BachelorsThesis::setTitle, DataType.SENTENCE);
        bachelorarbeitRepositoryGenerator.setData(BachelorsThesis::setDeadline, DataType.DATE_NEXT_1_YEAR);
        bachelorsTheses = bachelorarbeitRepositoryGenerator.create(25, seed);
        attendants.clear();
        attendants.addAll(finishers);
        finishers.clear();
        for (int i = 0; i < bachelorsTheses.size(); i++)
        {
            bachelorsThesis = bachelorsTheses.get(i);
            randomNumber = rng.nextInt(assistants.size());
            bachelorsThesis.setAssistant(assistants.get(randomNumber));
            if(attendants.size()>0)
            {
                randomNumber = rng.nextInt(attendants.size());
                bachelorsThesis.setStudent(attendants.get(randomNumber).getFirstname()+" " + attendants.get(randomNumber).getLastname());
                if((randomNumber%3)>0)
                {
                    bachelorsThesis.setFinished(true);
                    finishers.add(attendants.get(randomNumber));
                }
                attendants.remove(attendants.get(randomNumber));
            }
            bachelorsTheses.set(i, bachelorsThesis);
        }
        bachelorsThesisRepository.saveAll(bachelorsTheses);
    }

    private void generateProjects()
    {
        logger.info("... generating 25 Project entities...");
        seed = 345;
        Project project;
        List<Project> projects;
        ExampleDataGenerator<Project> projectRepositoryGenerator = new ExampleDataGenerator<>(Project.class,
                LocalDateTime.of(2022, 5, 24, 0, 0, 0));
        projectRepositoryGenerator.setData(Project::setTitle, DataType.SENTENCE);
        projectRepositoryGenerator.setData(Project::setDeadline, DataType.DATE_NEXT_1_YEAR);
        projects = projectRepositoryGenerator.create(25, seed);
        for (int i = 0; i < projects.size(); i++)
        {
            project = projects.get(i);
            randomNumber = rng.nextInt(assistants.size());
            project.setAssistant(assistants.get(randomNumber));
            randomNumber = rng.nextInt(students.size());
            if(!attendants.contains(students.get(randomNumber)))
            {
                attendants.add(students.get(randomNumber));
                project.setStudent(students.get(randomNumber).getFirstname()+" "+students.get(randomNumber).getLastname());
                if((randomNumber%4)>0)
                {
                    finishers.add(students.get(randomNumber));
                    project.setFinished(true);
                }
            }
            projects.set(i, project);
        }

        projectRepository.saveAll(projects);
    }

    private void generateStudents()
    {
        logger.info("... generating 20 Student entities...");
        seed=123;
        ExampleDataGenerator<Student> studentRepositoryGenerator = new ExampleDataGenerator<>(Student.class,
                LocalDateTime.of(2022, 5, 24, 0, 0, 0));
        studentRepositoryGenerator.setData(Student::setFirstname, DataType.FIRST_NAME);
        studentRepositoryGenerator.setData(Student::setLastname, DataType.LAST_NAME);
        studentRepositoryGenerator.setData(Student::setEmail, DataType.EMAIL);
        studentRepositoryGenerator.setData(Student::setPassword, DataType.TWO_WORDS);
        students = studentRepositoryGenerator.create(20, seed);
        for (int i = 0; i < students.size(); i++)
        {
            student = students.get(i);
            student.setUsername("teststudent" + (i + 1));
            student.setHashedPassword(passwordEncoder.encode(student.getPassword()));
            student.setLevel(rng.nextInt(4));
            students.set(i, student);
        }
        studentRepository.saveAll(students);
    }

    private void generateAssistants()
    {
        logger.info("... generating 5 Assistant entities...");

        seed = 234;
        ExampleDataGenerator<Assistant> assistantRepositoryGenerator = new ExampleDataGenerator<>(Assistant.class,
                LocalDateTime.of(2022, 5, 24, 0, 0, 0));
        assistantRepositoryGenerator.setData(Assistant::setFirstname, DataType.FIRST_NAME);
        assistantRepositoryGenerator.setData(Assistant::setLastname, DataType.LAST_NAME);
        assistantRepositoryGenerator.setData(Assistant::setEmail, DataType.EMAIL);
        assistantRepositoryGenerator.setData(Assistant::setPassword, DataType.TWO_WORDS);
        assistants = assistantRepositoryGenerator.create(5, seed);
        for (int i = 0; i < assistants.size(); i++)
        {
            assistant = assistants.get(i);
            assistant.setUsername("testassistant" + (i + 1));
            assistant.setHashedPassword(passwordEncoder.encode(assistant.getPassword()));
            assistant.setProjLimit(rng.nextInt(20));
            assistant.setBaLimit(rng.nextInt(20));
            assistant.setMaLimit(rng.nextInt(20));
            assistants.set(i, assistant);
        }
        assistantRepository.saveAll(assistants);

    }

    private void createDefaultUsers()
    {
        logger.info("Generating 3 main test users");
        createDefaultStudent();
        createDefaultAssistant();
        createDefaultAdmin();
    }

    private void createDefaultAdmin()
    {
        admin.setFirstname("ad");
        admin.setLastname("min");
        admin.setUsername("admin");
        assistant.setProjLimit(20);
        assistant.setBaLimit(10);
        assistant.setMaLimit(5);
        admin.setPassword("verysecureadminpw");
        admin.setHashedPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setProfilePictureUrl(
                "https://breakingmuscle.com/wp-content/uploads/2015/01/biceplevisquare.png");
        administratorRepository.save(admin);
    }

    private void createDefaultAssistant()
    {
        assistant.setFirstname("assi");
        assistant.setLastname("stant");
        assistant.setUsername("assistant");
        assistant.setEmail("assistent@edu.at");
        assistant.setProjLimit(20);
        assistant.setBaLimit(10);
        assistant.setMaLimit(5);
        assistant.setPassword("testassistantpw");
        assistant.setHashedPassword(passwordEncoder.encode(assistant.getPassword()));
        assistant.setProfilePictureUrl(
                "https://upload.wikimedia.org/wikipedia/commons/f/f8/Burt_Ward_Robin.jpg");
        student.setRoles(Collections.singleton(Role.ASSISTANT));
        assistantRepository.save(assistant);
    }

    private void createDefaultStudent()
    {
        student.setFirstname("stu");
        student.setLastname("dent");
        student.setUsername("student");
        student.setEmail("student@edu.at");
        student.setLevel(0);
        student.setPassword("teststudentpw");
        student.setHashedPassword(passwordEncoder.encode(student.getPassword()));
        student.setProfilePictureUrl(
                "https://imagesvc.meredithcorp.io/v3/mm/image?url=https%3A%2F%2Fstatic.onecms.io%2Fwp-content%2Fuploads%2Fsites%2F13%2F2016%2F06%2F03%2Fron.jpg&q=60");
        student.setRoles(Collections.singleton(Role.STUDENT));
        studentRepository.save(student);
    }

}