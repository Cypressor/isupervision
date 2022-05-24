package com.cypress.isupervision.data.generator;

import com.cypress.isupervision.data.Role;
import com.cypress.isupervision.data.entity.user.Administrator;
import com.cypress.isupervision.data.entity.user.Assistent;
import com.cypress.isupervision.data.entity.project.Bachelorarbeit;
import com.cypress.isupervision.data.entity.project.Masterarbeit;
import com.cypress.isupervision.data.entity.project.Projekt;
import com.cypress.isupervision.data.entity.user.Student;
import com.cypress.isupervision.data.service.*;
import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import java.time.LocalDateTime;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(PasswordEncoder passwordEncoder, UserRepository userRepository,
            ProjektRepository projektRepository, BachelorarbeitRepository bachelorarbeitRepository,
            MasterarbeitRepository masterarbeitRepository, StudentRepository studentRepository,
            AssistentRepository assistentRepository, AdministratorRepository administratorRepository) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (userRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");

            logger.info("... generating 3 User entities...");
            Student student = new Student();
            student.setVorname("stu");
            student.setNachname("dent");
            student.setUsername("student");
            student.setEmail("student@edu.at");
            student.setLevel(0);
            student.setPasswort("student");
            student.setHashedPassword(passwordEncoder.encode("student"));
            student.setProfilePictureUrl(
                    "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=128&h=128&q=80");
            student.setRoles(Collections.singleton(Role.STUDENT));
            studentRepository.save(student);

            Assistent assistent = new Assistent();
            assistent.setVorname("assi");
            assistent.setNachname("stent");
            assistent.setUsername("assistent");
            assistent.setEmail("assistent@edu.at");
            assistent.setProjLimit(20);
            assistent.setBaLimit(10);
            assistent.setMaLimit(5);
            assistent.setPasswort("assistent");
            assistent.setHashedPassword(passwordEncoder.encode("assistent"));
            assistent.setProfilePictureUrl(
                    "https://t3.ftcdn.net/jpg/02/05/76/62/240_F_205766203_bGiVmoVpbipIAAM3CWL84FWbROgSIcik.jpg");
            student.setRoles(Collections.singleton(Role.ASSISTENT));
            assistentRepository.save(assistent);

            Administrator admin = new Administrator();
            admin.setVorname("ad");
            admin.setNachname("min");
            admin.setUsername("admin");
            admin.setHashedPassword(passwordEncoder.encode("admin"));
            admin.setProfilePictureUrl(
                    "https://images.unsplash.com/photo-1607746882042-944635dfe10e?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=128&h=128&q=80");
            //admin.setRoles(Set.of(Role.STUDENT, Role.ASSISTENT, Role.ADMIN));
            administratorRepository.save(admin);


            logger.info("... generating 100 Projekt entities...");
            ExampleDataGenerator<Projekt> projektRepositoryGenerator = new ExampleDataGenerator<>(Projekt.class,
                    LocalDateTime.of(2022, 5, 24, 0, 0, 0));
            projektRepositoryGenerator.setData(Projekt::setTitel, DataType.SENTENCE);
            projektRepositoryGenerator.setData(Projekt::setAssistant, DataType.FULL_NAME);
            projektRepositoryGenerator.setData(Projekt::setStudent, DataType.FULL_NAME);
            projektRepositoryGenerator.setData(Projekt::setDeadline, DataType.DATE_OF_BIRTH);
            projektRepository.saveAll(projektRepositoryGenerator.create(100, seed));

            logger.info("... generating 100 Bachelorarbeit entities...");
            ExampleDataGenerator<Bachelorarbeit> bachelorarbeitRepositoryGenerator = new ExampleDataGenerator<>(
                    Bachelorarbeit.class, LocalDateTime.of(2022, 5, 24, 0, 0, 0));
            bachelorarbeitRepositoryGenerator.setData(Bachelorarbeit::setTitel, DataType.SENTENCE);
            bachelorarbeitRepositoryGenerator.setData(Bachelorarbeit::setAssistent, DataType.FULL_NAME);
            bachelorarbeitRepositoryGenerator.setData(Bachelorarbeit::setStudent, DataType.FULL_NAME);
            bachelorarbeitRepositoryGenerator.setData(Bachelorarbeit::setDeadline, DataType.DATE_OF_BIRTH);
            bachelorarbeitRepository.saveAll(bachelorarbeitRepositoryGenerator.create(100, seed));

            logger.info("... generating 100 Masterarbeit entities...");
            ExampleDataGenerator<Masterarbeit> masterarbeitRepositoryGenerator = new ExampleDataGenerator<>(
                    Masterarbeit.class, LocalDateTime.of(2022, 5, 24, 0, 0, 0));
            masterarbeitRepositoryGenerator.setData(Masterarbeit::setTitel, DataType.SENTENCE);
            masterarbeitRepositoryGenerator.setData(Masterarbeit::setAssistent, DataType.FULL_NAME);
            masterarbeitRepositoryGenerator.setData(Masterarbeit::setStudent, DataType.FULL_NAME);
            masterarbeitRepositoryGenerator.setData(Masterarbeit::setDeadline, DataType.DATE_OF_BIRTH);
            masterarbeitRepositoryGenerator.setData(Masterarbeit::setPruefungstermin, DataType.DATE_OF_BIRTH);
            masterarbeitRepository.saveAll(masterarbeitRepositoryGenerator.create(100, seed));

            logger.info("... generating 100 Student entities...");
            ExampleDataGenerator<Student> studentRepositoryGenerator = new ExampleDataGenerator<>(Student.class,
                    LocalDateTime.of(2022, 5, 24, 0, 0, 0));
            studentRepositoryGenerator.setData(Student::setVorname, DataType.FIRST_NAME);
            studentRepositoryGenerator.setData(Student::setNachname, DataType.LAST_NAME);
            studentRepositoryGenerator.setData(Student::setEmail, DataType.EMAIL);
            studentRepositoryGenerator.setData(Student::setPasswort, DataType.TWO_WORDS);
            studentRepositoryGenerator.setData(Student::setLevel, DataType.NUMBER_UP_TO_10);
            studentRepository.saveAll(studentRepositoryGenerator.create(100, seed));

            logger.info("... generating 100 Assistent entities...");
            ExampleDataGenerator<Assistent> assistentRepositoryGenerator = new ExampleDataGenerator<>(Assistent.class,
                    LocalDateTime.of(2022, 5, 24, 0, 0, 0));
            assistentRepositoryGenerator.setData(Assistent::setVorname, DataType.FIRST_NAME);
            assistentRepositoryGenerator.setData(Assistent::setNachname, DataType.LAST_NAME);
            assistentRepositoryGenerator.setData(Assistent::setEmail, DataType.EMAIL);
            assistentRepositoryGenerator.setData(Assistent::setPasswort, DataType.TWO_WORDS);
            assistentRepositoryGenerator.setData(Assistent::setProjLimit, DataType.NUMBER_UP_TO_100);
            assistentRepositoryGenerator.setData(Assistent::setBaLimit, DataType.NUMBER_UP_TO_100);
            assistentRepositoryGenerator.setData(Assistent::setMaLimit, DataType.NUMBER_UP_TO_100);
            assistentRepository.saveAll(assistentRepositoryGenerator.create(100, seed));

            logger.info("Generated demo data");
        };
    }

}