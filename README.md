# iSupervision README

## Troubleshooting:

### Logger prints out "node.exe cannot be found" during the first time of building the project in IntelliJ:
vaadin is probably failing to install npm. To resolve this issue please try the following:

try to install node.js and npm:
you can either install node.js from here: https://nodejs.org/en/download/
or try to add this dependecies: https://github.com/eirslett/frontend-maven-plugin

after installing node.js, intellij might ask you to install npm. in this case install npm.

### Login doesn't work:
There is an issue with the password manager of the browser saving the wrong fields of the registration form. If you run into issues logging in, try to not use the password manager and do input your login credentials manually.
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
### Keys:
- Student key: "FH02student"
- Assistant key: "FH02assistent"

***

## Design choices:

### Administrator:
- Admin can see, create and edit all Projects, Bachelortheses, Mastertheses, Students and Assistants.
- Adminstrator also acts like an Assistant.
- Administrator has Roles ADMIN, ASSISTANT, STUDENT.

### Assistants:
- Are meant as Project-Assistants.
- Can only create, edit or delete Projects if Project-Assistant is the Full Name of the Assistant (FirstName + " " +Lastname).
- Can adjust their project-limits in the Account section.

### Students:
- Can signup for a single ProjectEntity depending on their level.
- Can adjust their level in the Account section:
lvl 0: no accomplishment
lvl1: project done
lvl2: bachelors thesis done
lvl3: masters thesis done

***
## TestUsers:
### Administrator:
- username: admin
- password: verysecureadminpw
### Assistant:
- username: assistant
- password: testassistantpw
### Student:
- username: student
- password: teststudentpw
### Additional users:
If you want to take a look at additional test user data, log in with admin account and look at the "Studenten"/"Assistenten" tabs.
The unhashed passwords are intentionally stored and shown in the "Studenten"/"Assistenten" view, just to make testing easier.
I am totally aware that this shouldn't be the case in any proper application.
***
## Comments:

### Regarding the Project:
This project was quite a learning experience for me. This was my first time working with Maven, Spring Boot, Spring Security, Spring JPA and Vaadin. The quality of the code is clearly lacking due to the lack of experience and time.

### English/German:
All the class names of the Views are German. This is because the application is supposed to be german, and it was quite tedious not to have the view-names and page titles in the same language.

### Regarding entities:
The Project-Entities could have been done alot better. (For example: ProjectEntity.assistant is a String and could be replaced with an Assistant-Object.) Unfortunately there was not enough time to enhance the quality of the Code.

### Regarding classes in general:
There is quite alot of code repetition throughout the entire project. The code could be easily simplified by Implementing some more classes and interfaces. Again, there was a time issue.

### Unhashed passwords being stored:
The unhashed passwords are intentionally stored and shown in the "Studenten"/"Assistenten" view, just to make testing easier.
I am totally aware that this shouldn't be the case in any proper application.