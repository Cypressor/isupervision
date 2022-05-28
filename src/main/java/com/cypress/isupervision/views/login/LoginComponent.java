package com.cypress.isupervision.views.login;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

public class LoginComponent extends VerticalLayout implements BeforeEnterObserver
{
    private final LoginForm loginForm  = new LoginForm();

    public LoginComponent()
    {
        addClassNames("login");
        setSizeFull();
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        loginForm.setAction("login");
        add(new H1("iSupervision"),loginForm);
        add(new Paragraph("Bitte loggen Sie sich ein, um Zugriff auf die Projektdaten zu erhalten."),loginForm);
        loginForm.setForgotPasswordButtonVisible(false);
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
