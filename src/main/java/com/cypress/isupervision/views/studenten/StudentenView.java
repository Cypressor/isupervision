/*
 * iSupervision
 * StudentenView
 * an editable list of students, only for administrators to view.
 * Author: Martin Lunelli
 * Last Change: 04.06.2022
 */

package com.cypress.isupervision.views.studenten;

import com.cypress.isupervision.data.ProjectType;
import com.cypress.isupervision.data.entity.project.ProjectEntity;
import com.cypress.isupervision.data.entity.user.Student;
import com.cypress.isupervision.data.service.ProjectEntityService;
import com.cypress.isupervision.data.service.StudentService;
import com.cypress.isupervision.data.service.UserService;
import com.cypress.isupervision.security.AuthenticatedUser;
import com.cypress.isupervision.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

@PageTitle("Studenten")
@Route(value = "students/:studentID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class StudentenView extends Div implements BeforeEnterObserver {

    private final String STUDENT_ID = "studentID";
    private final String STUDENT_EDIT_ROUTE_TEMPLATE = "students/%s/edit";
    private Grid<Student> grid = new Grid<>(Student.class, false);
    private TextField username;
    private TextField firstname;
    private TextField lastname;
    private EmailField email;
    private TextField password;
    private Button cancel = new Button("Abbrechen");
    private Button save = new Button("Speichern");

    private Button edit = new Button("Ändern");
    private Dialog warning = new Dialog();
    private BeanValidationBinder<Student> binder;
    private Student student;
    private final StudentService studentService;

    private final ProjectEntityService projectEntityService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public StudentenView(StudentService studentService, UserService userService, PasswordEncoder passwordEncoder, ProjectEntityService projectEntityService, AuthenticatedUser authenticatedUser) {

        this.projectEntityService =projectEntityService;
        this.studentService = studentService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        addClassNames("studenten-view");
        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);
        createGrid();
        //Hook up buttons
        reactToGridSelection();
        configureBinder();
        cancelButtonListener();
        saveButtonListener();

        editButtonListener();
    }

    private void editButtonListener()
    {
        //Hook up Edit Button
        edit.addClickListener(e-> {
            try
            {
                if (this.student != null)
                {
                    binder.writeBean(this.student);
                    if(username.getValue().trim().equals("") || firstname.getValue().trim().equals("") || lastname.getValue().trim().equals("") || email.getValue().trim().equals("") || password.getValue().trim().equals(""))
                    {
                        Notification.show("Bitte alle Felder ausfüllen.");
                    }
                    else
                    {
                        this.student.setHashedPassword(passwordEncoder.encode(this.student.getPassword()));
                        studentService.update(this.student);
                        clearForm();
                        refreshGrid();
                        Notification.show("Student wurde bearbeitet.");
                    }
                }
                else
                {
                    Notification.show("Kein Student ausgewählt.");
                }
                UI.getCurrent().navigate(StudentenView.class);
            } catch (ValidationException validationException) {
                Notification.show("Es ist leider etwas schief gegangen.");
            }});
    }



    private void saveButtonListener()
    {
        //Hook up Save Button
        save.addClickListener(e -> {
            try {
                this.student = new Student();
                binder.writeBean(this.student);

                if(username.getValue().trim().equals("") || firstname.getValue().trim().equals("") || lastname.getValue().trim().equals("") || email.getValue().trim().equals("") || password.getValue().trim().equals(""))
                {
                    Notification.show("Bitte alle Felder ausfüllen.");
                }
                else
                {
                    if (this.student != null)
                    {
                        if(student.getLevel()<4)
                        {
                            int exists = userService.exists(this.student);
                            if (exists == 0)
                            {
                                this.student.setHashedPassword(passwordEncoder.encode(this.student.getPassword()));
                                studentService.update(this.student);
                                clearForm();
                                refreshGrid();
                                Notification.show("Neuer Student wurde angelegt.");
                            }
                            if (exists == 1 || exists == 3)
                            {
                                clearForm();
                                refreshGrid();
                                Notification.show("Username existiert bereits.");
                            }
                            if (exists == 2 || exists == 3)
                            {
                                clearForm();
                                refreshGrid();
                                Notification.show("Email existiert bereits");
                            }
                        }
                        else
                        {
                            Notification.show("Bitte eine Zahl zwischen 0-3 für Level eintragen.");
                        }
                    }
                }
                UI.getCurrent().navigate(StudentenView.class);
            } catch (ValidationException validationException) {
                Notification.show("Es ist leider etwas schief gegangen.");
            }
        });

    }

    private void cancelButtonListener()
    {
        //Hook up Cancel Button
        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });
    }

    private void configureBinder()
    {
        // Configure Form
        binder = new BeanValidationBinder<>(Student.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.bindInstanceFields(this);
    }

    private void reactToGridSelection()
    {
        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(STUDENT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(StudentenView.class);
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> studentId = event.getRouteParameters().get(STUDENT_ID).map(UUID::fromString);
        if (studentId.isPresent()) {
            Optional<Student> studentFromBackend = studentService.get(studentId.get());
            if (studentFromBackend.isPresent()) {
                populateForm(studentFromBackend.get());
            } else {
                Notification.show(String.format("The requested student was not found, ID = %s", studentId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(StudentenView.class);
            }
        }
    }
    private void createGrid()
    {
        grid.addColumn("username").setAutoWidth(true);
        grid.addColumn("firstname").setAutoWidth(true);
        grid.addColumn("lastname").setAutoWidth(true);
        grid.addColumn("email").setAutoWidth(true);
        grid.addColumn("password").setAutoWidth(true);
        grid.addColumn(student -> getStudentMaxAchievement(student)).setHeader("Abschluss");

        grid.getColumnByKey("username").setHeader("Benutzername");
        grid.getColumnByKey("firstname").setHeader("Vorname");
        grid.getColumnByKey("lastname").setHeader("Nachname");
        grid.getColumnByKey("email").setHeader("Email");
        grid.getColumnByKey("password").setHeader("Passwort");

        grid.setItems(query -> studentService.list(
                        PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");
        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        username = new TextField("Username");
        firstname = new TextField("Vorname");
        lastname = new TextField("Nachname");
        email = new EmailField("Email");
        password = new TextField("Passwort");
        Component[] fields = new Component[]{username, firstname, lastname, email, password};

        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);
        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        VerticalLayout buttonLayout1 = new VerticalLayout();
        VerticalLayout buttonLayout2 = new VerticalLayout();
        buttonLayout1.setClassName("button-layout1");
        buttonLayout2.setClassName("button-layout2");

        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout1.add(save, cancel );
        buttonLayout2.add(edit);
        HorizontalLayout greaterButtonLayout= new HorizontalLayout();
        buttonLayout2.setAlignItems(FlexComponent.Alignment.STRETCH);
        buttonLayout1.setAlignItems(FlexComponent.Alignment.STRETCH);
        greaterButtonLayout.add(buttonLayout1,buttonLayout2);
        greaterButtonLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        editorLayoutDiv.add(greaterButtonLayout);
    }



    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

        private void refreshGrid() {
        grid.select(null);
        grid.getLazyDataView().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Student value) {
        this.student = value;
        binder.readBean(this.student);
    }

    private String getStudentMaxAchievement(Student student)
    {
        String returnString="Kein";
        List<ProjectEntity> projectEntities = projectEntityService.searchForStudent(student);
        for(ProjectEntity projectEntity : projectEntities)
        {
            if(!returnString.equals(ProjectType.PROJECT.toString()) && !returnString.equals(ProjectType.BACHELORSTHESIS.toString()) && !(returnString.equals(ProjectType.MASTERSTHESIS)) && (projectEntity.getProjectType() == ProjectType.PROJECT && projectEntity.isFinished()==true ))
            {
                returnString="Projekt";
            }
            if(!returnString.equals(ProjectType.BACHELORSTHESIS.toString()) && !(returnString.equals(ProjectType.MASTERSTHESIS)) && (projectEntity.getProjectType() == ProjectType.BACHELORSTHESIS && projectEntity.isFinished()==true ))
            {
                returnString="Bachelorarbeit";
            }
            if(!(returnString.equals(ProjectType.MASTERSTHESIS)) && (projectEntity.getProjectType() == ProjectType.MASTERSTHESIS && projectEntity.isFinished()==true ))
            {
                returnString="Masterarbeit";
            }
        }
        return returnString;
    }
}
