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
import java.util.Collections;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(PasswordEncoder passwordEncoder, UserRepository userRepository,
                                      ProjectRepository projectRepository, BachelorsThesisRepository bachelorsThesisRepository,
                                      MastersThesisRepository mastersThesisRepository, StudentRepository studentRepository,
                                      AssistantRepository assistantRepository, AdministratorRepository administratorRepository) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (userRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;
            Random rng = new Random();

            logger.info("Generating demo data");

            logger.info("... generating 3 User entities...");
            Student student = new Student();
            student.setFirstname("stu");
            student.setLastname("dent");
            student.setUsername("student");
            student.setEmail("student@edu.at");
            student.setLevel(0);
            student.setPassword("teststudentpw");
            student.setHashedPassword(passwordEncoder.encode(student.getPassword()));
            student.setProfilePictureUrl(
                    "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=128&h=128&q=80");
            student.setRoles(Collections.singleton(Role.STUDENT));
            studentRepository.save(student);

            Assistant assistant = new Assistant();
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
                    "https://t3.ftcdn.net/jpg/02/05/76/62/240_F_205766203_bGiVmoVpbipIAAM3CWL84FWbROgSIcik.jpg");
            student.setRoles(Collections.singleton(Role.ASSISTENT));
            assistantRepository.save(assistant);

           Administrator admin = new Administrator();
            admin.setFirstname("ad");
            admin.setLastname("min");
            admin.setUsername("testadminpw");
            admin.setHashedPassword(admin.getPassword());
            admin.setProfilePictureUrl(
                    "https://images.unsplash.com/photo-1607746882042-944635dfe10e?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=128&h=128&q=80");
            administratorRepository.save(admin);


            logger.info("... generating 100 Projekt entities...");
            ExampleDataGenerator<Project> projectRepositoryGenerator = new ExampleDataGenerator<>(Project.class,
                    LocalDateTime.of(2022, 5, 24, 0, 0, 0));
            projectRepositoryGenerator.setData(Project::setTitle, DataType.SENTENCE);
            projectRepositoryGenerator.setData(Project::setAssistant, DataType.FULL_NAME);
            projectRepositoryGenerator.setData(Project::setStudent, DataType.FULL_NAME);
            projectRepositoryGenerator.setData(Project::setDeadline, DataType.DATE_OF_BIRTH);
            projectRepository.saveAll(projectRepositoryGenerator.create(100, seed));

            seed = 234;

            logger.info("... generating 100 Bachelorarbeit entities...");
            ExampleDataGenerator<BachelorsThesis> bachelorarbeitRepositoryGenerator = new ExampleDataGenerator<>(
                    BachelorsThesis.class, LocalDateTime.of(2022, 5, 24, 0, 0, 0));
            bachelorarbeitRepositoryGenerator.setData(BachelorsThesis::setTitle, DataType.SENTENCE);
            bachelorarbeitRepositoryGenerator.setData(BachelorsThesis::setAssistant, DataType.FULL_NAME);
            bachelorarbeitRepositoryGenerator.setData(BachelorsThesis::setStudent, DataType.FULL_NAME);
            bachelorarbeitRepositoryGenerator.setData(BachelorsThesis::setDeadline, DataType.DATE_OF_BIRTH);
            bachelorsThesisRepository.saveAll(bachelorarbeitRepositoryGenerator.create(100, seed));

            seed = 345;

            logger.info("... generating 100 Masterarbeit entities...");
            ExampleDataGenerator<MastersThesis> masterarbeitRepositoryGenerator = new ExampleDataGenerator<>(
                    MastersThesis.class, LocalDateTime.of(2022, 5, 24, 0, 0, 0));
            masterarbeitRepositoryGenerator.setData(MastersThesis::setTitle, DataType.SENTENCE);
            masterarbeitRepositoryGenerator.setData(MastersThesis::setAssistant, DataType.FULL_NAME);
            masterarbeitRepositoryGenerator.setData(MastersThesis::setStudent, DataType.FULL_NAME);
            masterarbeitRepositoryGenerator.setData(MastersThesis::setDeadline, DataType.DATE_OF_BIRTH);
            masterarbeitRepositoryGenerator.setData(MastersThesis::setExamDate, DataType.DATE_OF_BIRTH);
            mastersThesisRepository.saveAll(masterarbeitRepositoryGenerator.create(100, seed));

            seed = 456;


            logger.info("... generating 100 Student entities...");
            ExampleDataGenerator<Student> studentRepositoryGenerator = new ExampleDataGenerator<>(Student.class,
                    LocalDateTime.of(2022, 5, 24, 0, 0, 0));
            studentRepositoryGenerator.setData(Student::setUsername, (DataType.FIRST_NAME));
            studentRepositoryGenerator.setData(Student::setFirstname, DataType.FIRST_NAME);
            studentRepositoryGenerator.setData(Student::setLastname, DataType.LAST_NAME);
            studentRepositoryGenerator.setData(Student::setEmail, DataType.EMAIL);
            studentRepositoryGenerator.setData(Student::setPassword, DataType.TWO_WORDS);
            studentRepositoryGenerator.setData(Student::setLevel, DataType.NUMBER_UP_TO_10);
            studentRepository.saveAll(studentRepositoryGenerator.create(100, seed));

            seed = 567;

            logger.info("... generating 100 Assistent entities...");
            ExampleDataGenerator<Assistant> assistantRepositoryGenerator = new ExampleDataGenerator<>(Assistant.class,
                    LocalDateTime.of(2022, 5, 24, 0, 0, 0));
            assistantRepositoryGenerator.setData(Assistant::setUsername, DataType.FIRST_NAME);
            assistantRepositoryGenerator.setData(Assistant::setFirstname, DataType.FIRST_NAME);
            assistantRepositoryGenerator.setData(Assistant::setLastname, DataType.LAST_NAME);
            assistantRepositoryGenerator.setData(Assistant::setEmail, DataType.EMAIL);
            assistantRepositoryGenerator.setData(Assistant::setPassword, DataType.TWO_WORDS);
            assistantRepositoryGenerator.setData(Assistant::setProjLimit, DataType.NUMBER_UP_TO_100);
            assistantRepositoryGenerator.setData(Assistant::setBaLimit, DataType.NUMBER_UP_TO_100);
            assistantRepositoryGenerator.setData(Assistant::setMaLimit, DataType.NUMBER_UP_TO_100);
            assistantRepository.saveAll(assistantRepositoryGenerator.create(100, seed));

            logger.info("Generated demo data");
        };
    }

}