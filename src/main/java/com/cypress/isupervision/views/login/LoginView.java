package com.cypress.isupervision.views.login;

import com.cypress.isupervision.data.service.AssistantService;
import com.cypress.isupervision.data.service.StudentService;
import com.cypress.isupervision.data.service.UserService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@PageTitle("Login")
@Route(value = "login")
public class LoginView extends VerticalLayout
{
    LoginComponent loginComponent=new LoginComponent();
    private StudentService studentService;
    private AssistantService assistantService;
    private UserService userService;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public LoginView(StudentService studentService, AssistantService assistantService, UserService userService, PasswordEncoder passwordEncoder)
    {

        this.studentService=studentService;
        this.assistantService=assistantService;
        this.userService=userService;

        RegistrationComponent registrationComponent=new RegistrationComponent(studentService, assistantService, userService, passwordEncoder);

        add(loginComponent,registrationComponent);
        registrationComponent.setAlignItems(Alignment.CENTER);
        setAlignItems(Alignment.CENTER);
    }











    }





