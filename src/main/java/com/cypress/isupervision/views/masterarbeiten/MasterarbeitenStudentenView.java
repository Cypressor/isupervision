package com.cypress.isupervision.views.masterarbeiten;

import com.cypress.isupervision.data.entity.project.MastersThesis;
import com.cypress.isupervision.data.entity.user.Student;
import com.cypress.isupervision.data.service.MastersThesisService;
import com.cypress.isupervision.data.service.ProjectEntityService;
import com.cypress.isupervision.data.service.StudentService;
import com.cypress.isupervision.security.AuthenticatedUser;
import com.cypress.isupervision.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;

@PageTitle("Masterarbeiten Studenten")
@Route(value = "mastersthesis/student/:mastersthesisID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("STUDENT")
public class MasterarbeitenStudentenView extends Div
{
    private Grid<MastersThesis> grid = new Grid<>(MastersThesis.class, false);
    private AuthenticatedUser authenticatedUser;
    private List<MastersThesis> mastersTheses;
    private MastersThesis mastersThesis;
    private ComboBox<String> mastersThesisBox = new ComboBox("Masterarbeit-Anmeldung");
    private Button signup = new Button("Anmelden");
    private Dialog warning = new Dialog();
    private Student student;
    private MastersThesisService mastersThesisService;

    @Autowired
    MasterarbeitenStudentenView(AuthenticatedUser authenticatedUser, MastersThesisService mastersThesisService, ProjectEntityService projectEntityService, StudentService studentService)
    {
        addClassNames("masterarbeiten-view");
        this.mastersThesisService=mastersThesisService;
        student = studentService.get(authenticatedUser.get().get().getUsername());

        // Create UI
        mastersTheses=mastersThesisService.searchOpenProjects();
        SplitLayout splitLayout = new SplitLayout();
        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);
        createDialog();
        add(splitLayout);
        configureGrid();
        grid.setItems(mastersTheses);

        signup.addClickListener(e->{
            if(!mastersThesisBox.isEmpty())
            {
                if (student != null)
                {
                    if (projectEntityService.searchForStudent(student.getFirstname() + " " + student.getLastname()).size() > 0)
                    {
                        Notification.show("Sie sind bereits für eine Masterarbeit angemeldet");
                    } else
                    {
                        if(!(studentService.get(authenticatedUser.get().get().getUsername()).getLevel()<2))
                        {
                            warning.open();
                        }
                        else
                        {
                            Notification.show("Sie können sich nicht für eine Masterarbeit anmelden, da Sie noch keine Bachelorarbeit abgeschlossen haben");
                        }
                    }
                }
            }
            else
            {
                Notification.show("Bitte wählen Sie zuerst ein Masterarbeit aus.");
            }
        });
    }

    private void configureGrid()
    {
        grid.addColumn("title").setWidth("800px");
        grid.addColumn("assistant").setAutoWidth(true);
        grid.addColumn("student").setAutoWidth(true);
        grid.addColumn("deadline").setAutoWidth(true);
        grid.addColumn("examDate").setAutoWidth(true);

        grid.getColumnByKey("title").setHeader("Titel");
        grid.getColumnByKey("assistant").setHeader("Assistent");
        grid.getColumnByKey("student").setHeader("Student");
        grid.getColumnByKey("deadline").setHeader("Deadline");
        grid.getColumnByKey("examDate").setHeader("Prüfungsdatum");
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");
        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        createMasterThesisBox();

        Span warning = new Span("Die Anmeldung kann nicht rückgängig gemacht werden. Bei einer fehlerhaften Anmeldung kontaktieren Sie bitte den zuständigen Projektassistenten, oder einen Administrator.");
        formLayout.add(mastersThesisBox,signup, warning);
        editorDiv.add(formLayout);
        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void createMasterThesisBox()
    {
        mastersThesisBox.setAllowCustomValue(false);
        mastersThesisBox.setPlaceholder("Masterarbeit auswählen");
        List<String> titleList= new ArrayList<>();
        for(MastersThesis m : mastersTheses)
        {
            titleList.add(m.getTitle());
        }
        mastersThesisBox.setItems(titleList);
        add(mastersThesisBox);
    }
    private void createDialog()
    {
        warning.add(new H4("Anmeldung"));
        warning.add(new Paragraph("Die Anmeldung kann nicht rückgängig gemacht werden. Fortfahren?"));
        Button delete = new Button("Anmelden");
        Button cancel = new Button("Abbrechen");
        warning.add(delete, cancel);
        delete.addClickListener(event -> {confirmSignup();});
        cancel.addClickListener(event->{cancelSignup();});
    }
    private void confirmSignup()
    {
        for(MastersThesis m : mastersTheses)
        {
            if(m.getTitle().equals(mastersThesisBox.getValue()))
            {
                mastersThesis=m;
            }
        }
        mastersThesis.setStudent(student.getFirstname()+ " " + student.getLastname());
        mastersThesisService.update(mastersThesis);
        Notification.show("Du wurdest für die Masterarbeit angemeldet.");
        warning.close();
    }

    private void cancelSignup()
    {
        Notification.show("Anmeldung abgebrochen.");
        warning.close();
    }
}
