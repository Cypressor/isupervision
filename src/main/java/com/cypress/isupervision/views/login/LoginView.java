package com.cypress.isupervision.views.login;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Login")
@Route(value = "login")
public class LoginView extends LoginOverlay
{
    public LoginView() {
        setAction("login");

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("iSupervision");
        i18n.getHeader().setDescription("Bitte loggen Sie sich ein, um Zugriff auf die Projektdaten zu bekommen.");

        LoginI18n.Form i18nForm = i18n.getForm();
        i18n.setForm(i18nForm);

        i18n.setErrorMessage(new LoginI18n.ErrorMessage());
        i18n.getErrorMessage().setTitle("Fehler beim Login!");
        i18n.getErrorMessage().setMessage("Username oder Passwort falsch. Bitte versuchen Sie es erneut.");

        i18n.setAdditionalInformation("bla");
        setI18n(i18n);

        setForgotPasswordButtonVisible(false);
        setOpened(true);



    }


}
