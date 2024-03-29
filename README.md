
# iSupervision README
## Introduction:
This application has been created as part of the exercise course "Objektorientierte Programmierung", wich contributes to the study course "Business Software Development" at FH Campus02.
This is an experimental version of a business software application and should be treated as such. Be aware that this version of the software is not meant to be used in a real world scenario.
When using this software, be aware that data like passwords are intentionally not handled securely within the application to simplify the process of testing.

### Framework/Dependencies:

Maven, Spring Boot, Spring Security, Spring JPA, Vaadin.

***
## Troubleshooting:

### Logger prints out "node.exe cannot be found" during the first time of building the project in IntelliJ:
the framework is probably failing to install npm. To resolve this issue please try the following:

try to install node.js and npm:

you can either install node.js from here: https://nodejs.org/en/download/
or try to add this dependecies: https://github.com/eirslett/frontend-maven-plugin

after installing node.js, intellij might ask you to install npm. in this case install npm.

### Login doesn't work:
There is an issue where the password manager of the browser may save the wrong fields of the registration form. If you run into issues logging in, try to not use the password manager and do input your login credentials manually.

### Troubles with filling in the assistant name at project creation:
If you don't know the name of the account you are using, click on the "Abbrechen"-Button,  then the name should be filled in automatically.
Additionally, you can log in  with the admin account and take a look at the "Assistenten" tab. The application has been designed like this, because
in a real world application a user would surely always know his full name.

***

## Running the application:

The project is a standard Maven project. To run it from the command line,
type `mvnw` (Windows), or `./mvnw` (Mac & Linux), then open
http://localhost:8080 in your browser.

You can also import the project to your IDE of choice as you would with any
Maven project. Read more on [how to import Vaadin projects to different 
IDEs](https://vaadin.com/docs/latest/flow/guide/step-by-step/importing) (Eclipse, IntelliJ IDEA, NetBeans, and VS Code).
***
## Deploying to production:

To create a production build, call `mvnw clean package -Pproduction` (Windows),
or `./mvnw clean package -Pproduction` (Mac & Linux).
This will build a JAR file with all the dependencies and front-end resources,
ready to be deployed. The file can be found in the `target` folder after the build completes.

Once the JAR file is built, you can run it using
`java -jar target/isupervision-1.0-SNAPSHOT.jar`
***
## Registration:
To register a new account you will need one of the following keys.
When testing, be sure to note your credentials in case you might need them at a later point.
### Keys:
- Student key: "FH02student"
- Assistant key: "FH02assistent"

***
## Creating, editing and deleting projects
- Admins can put any name into the "Assistent" field.
- Assistants can only put their own full name into the assistant field. If you are testing, you might not know the full name of the account.
  If you don't know the name of the account you are using, click on the "Abbrechen"-Button, then the name should be filled in automatically.
  Additionally, you can log in  with the admin account and take a look at the "Assistenten" tab. The application has been designed like this, because
  in a real world application a user would surely always know his full name.
- The fields "Titel", "Assistent" and "Deadline" are mandatory to be filled out to create a project.
- The "Student" field is not meant to be filled out at project creation, because the students are supposed to sign up for the projects themselves. Feel free to fill the "Student"-field if needed tho.
- The "Prüfungstermin" field of "Masterarbeiten" is not mandatory to be filled out. It is supposed to be edited at a later point in time, because the date of exam might not be known at the time of project creation.

***
## TestUsers:
### Administrator:
- username: admin
- password: verysecureadminpw
- full name: ad min (needed for project creation)
### Assistant:
- username: assistant
- password: testassistantpw
- full name: assi stant (needed for project creation)
### Student:
- username: student
- password: teststudentpw
- full name: stu dent
### Additional users:
If you want to take a look at additional user data, log in with admin account and look at the "Studenten"/"Assistenten" tabs.
There are lots of test users generated beforehand that can be used for testing.
The unhashed passwords are intentionally stored and shown in the "Studenten"/"Assistenten" view, just to make testing easier.
I am totally aware that this shouldn't be the case in any proper application.


***
## Design choices:

### Administrator:
- Administrator can see, create and edit all projects, bachelorstheses, masterstheses, students and assistants.
- Administrator also acts like an assistant.
- Administrator has roles ADMIN, ASSISTANT, STUDENT.

### Assistants:
- Are meant as project-assistants.
- Can see all projects/bachelortheses/mastertheses.
- Can only create, edit or delete projects, if the assistant property of the Project is the full name of the Assistant (FirstName + " " + Lastname).
- Can adjust their project-limits in the Account section.

### Students:
- Can only see the projects/bachelorstheses/mastertheses where no student is assigned to yet. Can additionally see the projects they are assigned to in the "Meine Projekte" tab.
- Can signup for project/bachelorsthesis/mastersthesis depending on their completed projects.
- Can only have one active project/bachelorsthesis/mastersthesis at a time.

### Projects, Bachelorstheses and Masterstheses:
- Have a title, student, assistant deadline and projectType.
- Masterstheses additionally have an examDate.

### Database
- Database is created at application startup.
- Test users and projects are created at startup.
- Database is dropped at application shutdown.
- There is no persistent data in the test version of this application.

***

## Comments:

### Unhashed passwords being stored:
The unhashed passwords are intentionally stored and shown to the admin in the "Studenten"/"Assistenten" view, just to make testing easier.
I am totally aware that this shouldn't be the case in any proper application.

### English/German:
All the class names of the Views are German. This is because the application is supposed to be German, and it was quite tedious not to have the view-names and page titles in the same language.


### Regarding the Project and code quality:
This project was quite a learning experience for me. This was my first time working with Maven, Spring Boot, Spring Security, Spring JPA and Vaadin.
For that reason I focused more on learning about the framework than writing perfectly clean code.
The quality of the code is therefore not the best, due to the lack of experience and time. Still, the functionalities are working and all requirements are met.

### Regarding classes in general:
There is quite a lot of code repetition throughout the entire project. This happened due to a lack of experience with the framework and web development in general.
For example one working view has been copied multiple times, instead of spending too much time on writing a class that works for all of them. The same is true for services and repositories.
The code could have been simplified by implementing some more classes, interfaces and methods, but unfortunately I wasn't able to refactor the code before the deadline.

***
## Requirements (German):
- Intuitives Benutzerinterface (es sollte kein Handbuch nötig sein, um die Anwendung
bedienen zu können). CHECK


- Konsistente Zuordnung von Student:innen zu Projekten, Bachelor- und Masterarbeiten. CHECK


- Die Studierenden können sich für Projekte, Bachelor- und Masterarbeiten
  einschreiben und sehen die Anmeldung (nur die eigenen Daten!). CHECK


- Rollen: Administrator:innen, Assistent:innen, Student:innen. CHECK


- Administratoren sind für verschiedene Wartungsaktivitäten verantwortlich (z. B.
  Anpassung und Löschung von Informationen, Anpassung von Details der Projekte,
  Bachelor- und Masterarbeiten. CHECK


- Administratoren können Einschreibungen manuell anpassen und die Konsistenz des
  resultierenden Betreuungsplans überprüfen. CHECK


- Das Lehrpersonal (Administrator:innen und Assistent:innen) kann Präferenzen
  hinsichtlich des Anzahl der möglichen Betreuungen pro Semester hinzufügen. CHECK


- Das Lehrpersonal (Administrator:innen und Assistent:innen) erstellt offene Themen zu
Projekten, Bachelor- und Masterarbeiten, welche von den Studierenden gebucht
werden können. CHECK


- Zwei Einschreibungen desselben Studierenden sind nicht gestattet. CHECK


- Pro Thema darf nur ein Studierender eingeschrieben sein -> Studierende sollten eine
  Warnung erhalten, wenn die Einschreibung zum Thema nicht durchgeführt werden
  kann. CHECK


- Studierende dürfen sich erst zur Bachelorarbeit anmelden, sofern bereits eine
  Projektarbeit absolviert wurde -> Studierende sollten eine Warnung erhalten, wenn
  die Einschreibung zum Thema nicht durchgeführt werden kann. CHECK


- Studierende dürfen sich erst zur Masterarbeit anmelden, sofern bereits eine
  Bachelorarbeit absolviert wurde -> Studierende sollten eine Warnung erhalten, wenn
  die Einschreibung zum Thema nicht durchgeführt werden kann. CHECK

ALL REQUIREMENTS HAVE BEEN FULFILLED.