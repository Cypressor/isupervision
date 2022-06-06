/*
 * iSupervision
 * ProjekteStudentenView
 * Projekte view for assistant users
 * Author: Martin Lunelli
 * Last Change: 04.06.2022
 */

package com.cypress.isupervision.views.projekte;

import com.cypress.isupervision.data.entity.project.Project;
import com.cypress.isupervision.data.entity.project.ProjectEntity;
import com.cypress.isupervision.data.entity.user.Student;
import com.cypress.isupervision.data.service.ProjectEntityService;
import com.cypress.isupervision.data.service.ProjectService;
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

@PageTitle("Projekte Studenten")
@Route(value = "projects/student/student:projectID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("STUDENT")
public class ProjekteStudentenView extends Div
{
    private Grid<Project> grid = new Grid<>(Project.class, false);
    private List<Project> projects;
    private Project project;
    private ComboBox<String> projectBox = new ComboBox("Projekt-Anmeldung");
    private Button signup = new Button("Anmelden");
    private Dialog warning = new Dialog();
    private Student student;
    private final ProjectService projectService;
    private final ProjectEntityService projectEntityService;

    @Autowired
    ProjekteStudentenView(AuthenticatedUser authenticatedUser, ProjectService projectService, ProjectEntityService projectEntityService, StudentService studentService)
    {
        addClassNames("projekte-view");
        this.projectService=projectService;
        this.projectEntityService=projectEntityService;
        student = studentService.get(authenticatedUser.get().get().getUsername());
        // Create UI
        projects=projectService.searchOpenProjects();
        SplitLayout splitLayout = new SplitLayout();
        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);
        createDialog();
        add(splitLayout);
        configureGrid();
        grid.setItems(projects);
        signupButtonListener();
    }

    private void signupButtonListener()
    {
        signup.addClickListener(e->{
            if(!projectBox.isEmpty())
            {
                if (student != null)
                {

                    if(projectEntityService.get(projectBox.getValue()).getStudent()==null ||projectEntityService.get(projectBox.getValue()).getStudent().equals(""))
                    {
                        List<ProjectEntity> projectEntities = projectEntityService.searchForStudent(student);
                        for(int i=0;i<projectEntities.size();i++)
                        {
                            if(projectEntities.get(i).isFinished())
                            {
                                projectEntities.remove(projectEntities.get(i));
                                i--;
                            }
                        }
                        if (projectEntities.size() > 0)
                        {
                            Notification.show("Sie sind bereits für eine Arbeit angemeldet.");
                        } else
                        {
                            warning.open();
                        }
                    }
                    else
                    {
                        Notification.show("Projekt bereits vergeben.");
                    }
                }
            }
            else
            {
                Notification.show("Bitte wählen Sie zuerst ein Projekt aus.");
            }
            UI.getCurrent().navigate("projects");
        });
    }

    private void configureGrid()
    {
        grid.addColumn("title").setWidth("800px");
        grid.addColumn(project -> project.getAssistant().getFirstname()+" "+project.getAssistant().getLastname(), "assistant.firstname")
                .setHeader("Assistent")
                .setKey("assistant")
                .setAutoWidth(true)
                .setSortable(true);
        grid.addColumn("deadline").setAutoWidth(true);
        grid.getColumnByKey("title").setHeader("Titel");
        grid.getColumnByKey("deadline").setHeader("Deadline");
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");
        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        createProjectBox();

        Span warning = new Span("Die Anmeldung kann nicht rückgängig gemacht werden. Bei einer fehlerhaften Anmeldung kontaktieren Sie bitte den zuständigen Projektassistenten, oder einen Administrator.");
        formLayout.add(projectBox,signup, warning);
        editorDiv.add(formLayout);
        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid()
    {
        grid.select(null);
        grid.getLazyDataView().refreshAll();
    }

    private void createProjectBox()
    {
        projectBox.setAllowCustomValue(false);
        projectBox.setPlaceholder("Projekt auswählen");
        List<String> titleList= new ArrayList<>();
                for(Project p : projects)
    {
        titleList.add(p.getTitle());
    }
        projectBox.setItems(titleList);

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
        for(Project p : projects)
        {
            if(p.getTitle().equals(projectBox.getValue()))
            {
                project=p;
            }
        }
        project.setStudent(student);
        projectService.update(project);
        Notification.show("Du wurdest für das Projekt angemeldet.");
        warning.close();

    }

    private void cancelSignup()
    {
        Notification.show("Anmeldung abgebrochen.");
        warning.close();
    }
}

