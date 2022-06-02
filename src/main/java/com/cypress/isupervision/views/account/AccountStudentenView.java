package com.cypress.isupervision.views.account;

import com.cypress.isupervision.data.entity.user.Student;
import com.cypress.isupervision.data.service.StudentService;
import com.cypress.isupervision.security.AuthenticatedUser;
import com.cypress.isupervision.views.MainLayout;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.security.RolesAllowed;

@PageTitle("Account Studenten")
@Route(value = "account/student", layout = MainLayout.class)
@RolesAllowed("STUDENT")
public class AccountStudentenView extends VerticalLayout
{
    private ComboBox<String> projectBox = new ComboBox("Deine hochwertigste Arbeit");
    private Student student;

    @Autowired
    public AccountStudentenView(AuthenticatedUser authenticatedUser, StudentService studentService )
    {
        student = studentService.get(authenticatedUser.get().get().getUsername());
        createProjectBox();

        projectBox.addValueChangeListener(event -> {
            if(projectBox.getValue().equals("Kein Projekt"))
            {
                student.setLevel(0);
                Notification.show("Hochwertigste Arbeit: Kein Projekt.");
            }
            else if(projectBox.getValue().equals("Projekt"))
            {
                student.setLevel(1);
                Notification.show("Hochwertigste Arbeit:Projekt.");
            }
            else if(projectBox.getValue().equals("Bachelorarbeit"))
            {
                student.setLevel(2);
                Notification.show("Hochwertigste Arbeit: Bachelorarbeit.");
            }
            else if(projectBox.getValue().equals("Masterarbeit"))
            {
                student.setLevel(3);
                Notification.show("Hochwertigste Arbeit: Masterarbeit.");
            }
        studentService.update(student);
        });
    }

    private void createProjectBox()
    {
        projectBox.setAllowCustomValue(false);
        projectBox.setPlaceholder("Rolle auswählen");
        projectBox.setItems("Kein Projekt", "Projekt", "Bachelorarbeit", "Masterarbeit");
        if (student != null)
        {
            if (student.getLevel() == 0)
            {
                projectBox.setValue("Kein Projekt");
            } else if (student.getLevel() == 1)
            {
                projectBox.setValue("Projekt");
            } else if (student.getLevel() == 2)
            {
                projectBox.setValue("Bachelorarbeit");
            } else if (student.getLevel() == 3)
            {
                projectBox.setValue("Masterarbeit");
            }
        }
        add(projectBox);
    }
}