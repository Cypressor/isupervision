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
import java.util.List;
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

            logger.info("Generating demo data");
            Random rng = new Random();

            logger.info("Generating 3 main test users");

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
            student.setRoles(Collections.singleton(Role.ASSISTANT));
            assistantRepository.save(assistant);

            Administrator admin = new Administrator();
            admin.setFirstname("ad");
            admin.setLastname("min");
            admin.setUsername("admin");
            assistant.setProjLimit(20);
            assistant.setBaLimit(10);
            assistant.setMaLimit(5);
            admin.setPassword("verysecureadminpw");
            admin.setHashedPassword(passwordEncoder.encode(admin.getPassword()));
            admin.setProfilePictureUrl(
                    "https://images.unsplash.com/photo-1607746882042-944635dfe10e?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=128&h=128&q=80");
            administratorRepository.save(admin);


            logger.info("... generating 20 Student entities...");
            List<Student> students;
            int seed = 123;
            ExampleDataGenerator<Student> studentRepositoryGenerator = new ExampleDataGenerator<>(Student.class,
                    LocalDateTime.of(2022, 5, 24, 0, 0, 0));
            studentRepositoryGenerator.setData(Student::setFirstname, DataType.FIRST_NAME);
            studentRepositoryGenerator.setData(Student::setLastname, DataType.LAST_NAME);
            studentRepositoryGenerator.setData(Student::setEmail, DataType.EMAIL);
            studentRepositoryGenerator.setData(Student::setPassword, DataType.TWO_WORDS);
            studentRepositoryGenerator.setData(Student::setProfilePictureUrl, DataType.PROFILE_PICTURE_URL);
            students=studentRepositoryGenerator.create(20, seed);
            for (int i=0; i<students.size();i++)
            {
                student = students.get(i);
                student.setUsername("teststudent"+(i+1));
                student.setHashedPassword(passwordEncoder.encode(student.getPassword()));
                student.setLevel(rng.nextInt(3));
                students.set(i,student);
            }
            studentRepository.saveAll(students);


            logger.info("... generating 5 Assistant entities...");
            List<Assistant> assistants;
            seed = 234;
            ExampleDataGenerator<Assistant> assistantRepositoryGenerator = new ExampleDataGenerator<>(Assistant.class,
                    LocalDateTime.of(2022, 5, 24, 0, 0, 0));

            assistantRepositoryGenerator.setData(Assistant::setFirstname, DataType.FIRST_NAME);
            assistantRepositoryGenerator.setData(Assistant::setLastname, DataType.LAST_NAME);
            assistantRepositoryGenerator.setData(Assistant::setEmail, DataType.EMAIL);
            assistantRepositoryGenerator.setData(Assistant::setPassword, DataType.TWO_WORDS);
            assistants=assistantRepositoryGenerator.create(5, seed);
            for (int i=0; i<assistants.size();i++)
            {
                assistant = assistants.get(i);
                assistant.setUsername("testassistant"+(i+1));
                assistant.setHashedPassword(passwordEncoder.encode(assistant.getPassword()));
                assistant.setProjLimit(rng.nextInt(20));
                assistant.setBaLimit(rng.nextInt(20));
                assistant.setMaLimit(rng.nextInt(20));
                assistants.set(i,assistant);
            }
            assistantRepository.saveAll(assistants);

            logger.info("... generating 25 Project entities...");
            seed = 345;
            List<Project> projects;
            Project project;
            int randomNumber;
            ExampleDataGenerator<Project> projectRepositoryGenerator = new ExampleDataGenerator<>(Project.class,
                    LocalDateTime.of(2022, 5, 24, 0, 0, 0));
            projectRepositoryGenerator.setData(Project::setTitle, DataType.SENTENCE);
            projectRepositoryGenerator.setData(Project::setDeadline, DataType.DATE_NEXT_1_YEAR);
            projects=projectRepositoryGenerator.create(25, seed);
            for (int i=0; i<projects.size();i++)
            {
                project = projects.get(i);
                randomNumber= rng.nextInt(assistants.size()-1);
                project.setAssistant(assistants.get(randomNumber).getFirstname()+" "+assistants.get(randomNumber).getLastname());
                projects.set(i,project);
            }
            projectRepository.saveAll(projects);


            logger.info("... generating 25 BachelorsThesis entities...");
            seed = 456;
            List<BachelorsThesis> bachelorsTheses;
            BachelorsThesis bachelorsThesis;

            ExampleDataGenerator<BachelorsThesis> bachelorarbeitRepositoryGenerator = new ExampleDataGenerator<>(
                    BachelorsThesis.class, LocalDateTime.of(2022, 5, 24, 0, 0, 0));
            bachelorarbeitRepositoryGenerator.setData(BachelorsThesis::setTitle, DataType.SENTENCE);
            bachelorarbeitRepositoryGenerator.setData(BachelorsThesis::setDeadline, DataType.DATE_NEXT_1_YEAR);
            bachelorsTheses=bachelorarbeitRepositoryGenerator.create(25, seed);
            for (int i=0; i<projects.size();i++)
            {
                bachelorsThesis = bachelorsTheses.get(i);
                randomNumber= rng.nextInt(assistants.size()-1);
                bachelorsThesis.setAssistant(assistants.get(randomNumber).getFirstname()+" "+assistants.get(randomNumber).getLastname());
                bachelorsTheses.set(i,bachelorsThesis);
            }
            bachelorsThesisRepository.saveAll(bachelorsTheses);


            logger.info("... generating 25 MastersThesis entities...");
            seed = 567;
            List<MastersThesis> mastersTheses;
            MastersThesis mastersThesis;

            ExampleDataGenerator<MastersThesis> masterarbeitRepositoryGenerator = new ExampleDataGenerator<>(
                    MastersThesis.class, LocalDateTime.of(2022, 5, 24, 0, 0, 0));
            masterarbeitRepositoryGenerator.setData(MastersThesis::setTitle, DataType.SENTENCE);
            masterarbeitRepositoryGenerator.setData(MastersThesis::setDeadline, DataType.DATE_NEXT_1_YEAR);
            masterarbeitRepositoryGenerator.setData(MastersThesis::setExamDate, DataType.DATE_NEXT_1_YEAR);
            mastersTheses= masterarbeitRepositoryGenerator.create(25, seed);
            for (int i=0; i<projects.size();i++)
            {
                mastersThesis = mastersTheses.get(i);
                randomNumber= rng.nextInt(assistants.size()-1);
                mastersThesis.setAssistant(assistants.get(randomNumber).getFirstname()+" "+assistants.get(randomNumber).getLastname());
                mastersTheses.set(i,mastersThesis);
            }
            mastersThesisRepository.saveAll(mastersTheses);

            logger.info("Generated demo data");
        };
    }

}