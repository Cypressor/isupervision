package com.cypress.isupervision.views.bachelorarbeiten;

import com.cypress.isupervision.data.ProjectType;
import com.cypress.isupervision.data.entity.project.BachelorsThesis;
import com.cypress.isupervision.data.entity.project.ProjectEntity;
import com.cypress.isupervision.data.entity.user.Student;
import com.cypress.isupervision.data.service.BachelorsThesisService;
import com.cypress.isupervision.data.service.ProjectEntityService;
import com.cypress.isupervision.data.service.StudentService;
import com.cypress.isupervision.security.AuthenticatedUser;
import com.cypress.isupervision.views.MainLayout;
import com.vaadin.flow.component.UI;
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

@PageTitle("Bachelorarbeiten Studenten")
@Route(value = "bachelorstheses/student/:bachelorsthesisID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("STUDENT")
public class BachelorarbeitenStudentenView extends Div
{
    private Grid<BachelorsThesis> grid = new Grid<>(BachelorsThesis.class, false);
    private AuthenticatedUser authenticatedUser;
    private List<BachelorsThesis> bachelorsTheses;
    private BachelorsThesis bachelorsThesis;
    private ComboBox<String> bachelorsThesisBox = new ComboBox("Bachelorarbeit-Anmeldung");
    private Button signup = new Button("Anmelden");
    private Dialog warning = new Dialog();
    private Student student;
    private BachelorsThesisService bachelorsThesisService;

    @Autowired
    BachelorarbeitenStudentenView(AuthenticatedUser authenticatedUser, BachelorsThesisService bachelorsThesisService, ProjectEntityService projectEntityService, StudentService studentService)
    {
        addClassNames("bachelorarbeiten-view");
        this.bachelorsThesisService=bachelorsThesisService;
        student = studentService.get(authenticatedUser.get().get().getUsername());

        // Create UI
        bachelorsTheses=bachelorsThesisService.searchOpenProjects();
        SplitLayout splitLayout = new SplitLayout();
        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);
        createDialog();
        add(splitLayout);
        configureGrid();
        grid.setItems(bachelorsTheses);

        signup.addClickListener(e->{
            if(!bachelorsThesisBox.isEmpty())
            {
                if (student != null)
                {
                    if(projectEntityService.get(bachelorsThesisBox.getValue()).getStudent()==null ||projectEntityService.get(bachelorsThesisBox.getValue()).getStudent().equals(""))
                    {
                        List<ProjectEntity> projectEntities = projectEntityService.searchForStudent(student.getFirstname() + " " + student.getLastname());
                        List<ProjectEntity> tempEntities = new ArrayList<>();
                        tempEntities.addAll(projectEntities);
                        for(int i=0;i<tempEntities.size();i++)
                        {
                            if(tempEntities.get(i).isFinished())
                            {
                                tempEntities.remove(i);
                                i--;
                            }
                        }
                        if (tempEntities.size() > 0)
                        {
                            Notification.show("Sie sind bereits für eine Arbeit angemeldet.");
                        } else
                        {
                            for(int i=0;i<projectEntities.size();i++)
                            {
                                if (projectEntities.get(i).getProjectType()!=ProjectType.Projekt)
                                {
                                    projectEntities.remove(i);
                                }
                            }

                            if (projectEntities.size() > 0)
                            {
                                warning.open();
                            } else
                            {
                                Notification.show("Sie können sich nicht für eine Bachelorarbeit anmelden, da Sie noch kein Projekt abgeschlossen haben.");
                            }
                        }
                    }
                    else
                    {
                        Notification.show("Bachelorarbeit bereits vergeben.");
                    }
                }
            }
            else
            {
                Notification.show("Bitte wählen Sie zuerst ein Bachelorarbeit aus.");
            }
            UI.getCurrent().navigate("bachelorstheses");
        });
    }

    private void configureGrid()
    {
        grid.addColumn("title").setWidth("800px");
        grid.addColumn("assistant").setAutoWidth(true);
        grid.addColumn("student").setAutoWidth(true);
        grid.addColumn("deadline").setAutoWidth(true);

        grid.getColumnByKey("title").setHeader("Titel");
        grid.getColumnByKey("assistant").setHeader("Assistent");
        grid.getColumnByKey("student").setHeader("Student");
        grid.getColumnByKey("deadline").setHeader("Deadline");

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
        formLayout.add(bachelorsThesisBox,signup, warning);
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
        bachelorsThesisBox.setAllowCustomValue(false);
        bachelorsThesisBox.setPlaceholder("Bachelorarbeit auswählen");
        List<String> titleList= new ArrayList<>();
        for(BachelorsThesis b : bachelorsTheses)
        {
            titleList.add(b.getTitle());
        }
        bachelorsThesisBox.setItems(titleList);
        add(bachelorsThesisBox);
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
        for(BachelorsThesis m : bachelorsTheses)
        {
            if(m.getTitle().equals(bachelorsThesisBox.getValue()))
            {
                bachelorsThesis=m;
            }
        }
        bachelorsThesis.setStudent(student.getFirstname()+ " " + student.getLastname());
        bachelorsThesisService.update(bachelorsThesis);
        Notification.show("Du wurdest für die Bachelorarbeit angemeldet.");
        warning.close();
    }

    private void cancelSignup()
    {
        Notification.show("Anmeldung abgebrochen.");
        warning.close();
    }
}