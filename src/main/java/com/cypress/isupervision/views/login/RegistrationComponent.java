package com.cypress.isupervision.views.login;

import com.cypress.isupervision.data.entity.user.Assistant;
import com.cypress.isupervision.data.entity.user.Student;
import com.cypress.isupervision.data.entity.user.User;
import com.cypress.isupervision.data.service.AssistantService;
import com.cypress.isupervision.data.service.StudentService;
import com.cypress.isupervision.data.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class RegistrationComponent extends VerticalLayout
{

    final String ROLE_STUDENT = "Student";
    final String ROLE_ASSISTENT = "Assistent";
    final String KEY_STUDENT = "FH02student";
    final String KEY_ASSISTENT = "FH02assistent";

    private BeanValidationBinder<User> userBinder;
    private Student student;
    private Assistant assistant;

    private TextField username = new TextField("Benutzername");
    private TextField firstname = new TextField("Vorname");
    private TextField lastname = new TextField("Nachname");
    private EmailField email = new EmailField ("Email");
    private PasswordField password1 = new PasswordField("Passwort");
    private PasswordField password2 = new PasswordField(("Passwort bestätigen."));
    private TextField key = new TextField("Schlüssel");
    private ComboBox<String> role = new ComboBox("Rolle");
    private Button registerButton = new Button ("Registrieren");


    RegistrationComponent(StudentService studentService, AssistantService assistantService, UserService userService, PasswordEncoder passwordEncoder)
    {


        createRegistrationForm();

        // Configure Form
        userBinder = new BeanValidationBinder<>(User.class);

        // Bind fields. This is where you'd define e.g. validation rules
        userBinder.bindInstanceFields(this);

        registerButton.addClickListener(e -> {
            try
            {
                if (username.getValue().trim().equals("") || firstname.getValue().trim().equals("") || lastname.getValue().trim().equals("") || email.getValue().trim().equals("") || password1.getValue().trim().equals("") || password2.getValue().trim().equals("") || key.getValue().trim().equals(""))
                {
                    Notification.show("Bitte alle Felder ausfüllen.");
                }
                else if (role.isEmpty())
                {
                    Notification.show("Bitte eine Rolle auswählen.");
                }
                else if(!password1.getValue().equals(password2.getValue()))
                {
                    Notification.show("Passwörter unterscheiden sich.");
                }
                else
                {
                    if (role.getValue().trim().equals("Student") && key.getValue().equals(KEY_STUDENT))
                    {
                        this.student = new Student();

                        userBinder.writeBean(this.student);
                        this.student.setPassword(password1.getValue());
                        int exists = userService.exists(this.student);
                        if (exists == 0)
                        {
                            this.student.setHashedPassword(passwordEncoder.encode(this.student.getPassword()));
                            studentService.update(this.student);
                            Notification.show("Neuer Student wurde erfolgreich registriert.");
                            clearForm();
                        }
                        if (exists == 1 || exists == 3)
                        {
                            Notification.show("Username existiert bereits.");
                        }
                        if (exists == 2 || exists == 3)
                        {
                            Notification.show("Email existiert bereits");
                        }
                    }
                    else if (role.getValue().equals("Assistent") && key.getValue().equals(KEY_ASSISTENT))
                    {
                        this.assistant = new Assistant();

                        userBinder.writeBean(this.assistant);
                        this.assistant.setPassword(password1.getValue());
                        int exists = userService.exists(this.assistant);
                        if (exists == 0)
                        {
                            this.assistant.setHashedPassword(passwordEncoder.encode(this.assistant.getPassword()));
                            assistantService.update(this.assistant);
                            Notification.show("Neuer Assistent wurde erfolgreich registriert.");
                            clearForm();
                        }
                        if (exists == 1 || exists == 3)
                        {
                            Notification.show("Username existiert bereits.");
                        }
                        if (exists == 2 || exists == 3)
                        {
                            Notification.show("Email existiert bereits");
                        }
                    }
                    else
                    {
                            Notification.show("Rolle und Schlüssel passen nicht zusammen.");
                    }
                }
            }

            catch (ValidationException validationException) {
                Notification.show("Es ist leider etwas schief gegangen.");
            }

        });
    }

    private void clearForm() {
        userBinder.readBean(null);
        role.setValue("");
        key.setValue("");
        password1.setValue("");
        password2.setValue("");
    }

    private void createRegistrationForm()
    {
        role.setAllowCustomValue(false);
        role.setPlaceholder("Rolle auswählen");
        role.setItems(ROLE_ASSISTENT,ROLE_STUDENT);

        HorizontalLayout usernameLayout = new HorizontalLayout();
        usernameLayout.add(username,email);
        HorizontalLayout nameLayout = new HorizontalLayout();
        nameLayout.add(firstname,lastname);
        HorizontalLayout roleLayout = new HorizontalLayout();
        roleLayout.add(role, key);
        HorizontalLayout passwordLayout= new HorizontalLayout();
        passwordLayout.add(password1,password2);

        VerticalLayout registrationLayout = new VerticalLayout();
        registrationLayout.add(usernameLayout,nameLayout,roleLayout,passwordLayout, registerButton);
        registrationLayout.setAlignItems(Alignment.CENTER);
        Details registrationForm = new Details("Registrierung",registrationLayout);
        add(registrationForm);
    }
}
