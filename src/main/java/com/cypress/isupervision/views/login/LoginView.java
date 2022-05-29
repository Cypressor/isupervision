package com.cypress.isupervision.views.login;

import com.cypress.isupervision.data.service.AssistantService;
import com.cypress.isupervision.data.service.StudentService;
import com.cypress.isupervision.data.service.UserService;
import com.cypress.isupervision.views.registration.RegistrationView;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@PageTitle("Login")
@Route(value = "login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver
{
    private final LoginForm loginForm  = new LoginForm();

    @Autowired
    public LoginView(StudentService studentService, AssistantService assistantService, UserService userService, PasswordEncoder passwordEncoder)
    {
        createLoginComponent();
        setAlignItems(Alignment.CENTER);
    }

    public void createLoginComponent()
    {
        addClassNames("login");
        setSizeFull();
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        loginForm.setAction("login");
        add(new H1("iSupervision"),loginForm);
        add(new Paragraph("Bitte loggen Sie sich ein, um Zugriff auf die Projektdaten zu erhalten."),loginForm);
        loginForm.setForgotPasswordButtonVisible(false);
        add(loginForm);
        add(new RouterLink("Registrierung", RegistrationView.class));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent)
    {
        if(beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error"))
        {
            loginForm.setError(true);
        }
    }










    }





