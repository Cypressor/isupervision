package com.cypress.isupervision.views.studenten;

import com.cypress.isupervision.data.entity.user.Student;
import com.cypress.isupervision.data.entity.user.User;
import com.cypress.isupervision.data.service.StudentRepository;
import com.cypress.isupervision.data.service.StudentService;
import com.cypress.isupervision.data.service.UserRepository;
import com.cypress.isupervision.data.service.UserService;
import com.cypress.isupervision.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.security.RolesAllowed;
import javax.swing.*;

import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mapping.model.Property;


@PageTitle("Studenten")
@Route(value = "studenten/:studentID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class StudentenView extends Div implements BeforeEnterObserver {

    private final String STUDENT_ID = "studentID";
    private final String STUDENT_EDIT_ROUTE_TEMPLATE = "studenten/%s/edit";

    private Grid<Student> grid = new Grid<>(Student.class, false);

    private TextField username;
    private TextField vorname;
    private TextField nachname;
    private TextField email;
    private TextField passwort;
    private TextField level;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");
    private Button edit = new Button("Edit");

    private BeanValidationBinder<Student> binder;

    private Student student;

    private final StudentService studentService;
    private StudentRepository studentRepository;

    @Autowired
    public StudentenView(StudentService studentService, UserService userService) {

        this.studentService = studentService;

        addClassNames("studenten-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("username").setAutoWidth(true);
        grid.addColumn("vorname").setAutoWidth(true);
        grid.addColumn("nachname").setAutoWidth(true);
        grid.addColumn("email").setAutoWidth(true);
        grid.addColumn("passwort").setAutoWidth(true);
        grid.addColumn("level").setAutoWidth(true);
        grid.setItems(query -> studentService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(STUDENT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(StudentenView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Student.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(level).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("level");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
            //    if (this.student == null) {
                    this.student = new Student();
            //    }
                binder.writeBean(this.student);

                if(username.getValue().trim()=="" || vorname.getValue().trim()=="" || nachname.getValue().trim()=="" || email.getValue().trim()=="" || passwort.getValue().trim()=="" || level.getValue().trim()=="")
                {
                    Notification.show("Bitte alle Felder ausfüllen.");
                }
                else
                {
                    int exists = userService.exists(this.student);
                    if (exists == 0 || exists == 1)
                    {
                        studentService.update(this.student);
                        clearForm();
                        refreshGrid();
                        Notification.show("Neuer Student wurde angelegt.");
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
                UI.getCurrent().navigate(StudentenView.class);
            } catch (ValidationException validationException) {
                Notification.show("Es ist leider etwas schief gegangen.");
            }
        });

        delete.addClickListener(e -> {
            binder.readBean(this.student);
            studentService.delete(this.student.getId());
            refreshGrid();
        });

        edit.addClickListener(e-> {
            try
            {
                if (this.student != null)
                {
                    binder.writeBean(this.student);
                    if(username.getValue().trim()=="" || vorname.getValue().trim()=="" || nachname.getValue().trim()=="" || email.getValue().trim()=="" || passwort.getValue().trim()=="" || level.getValue().trim()=="")
                    {
                        Notification.show("Bitte alle Felder ausfüllen.");
                    }
                    else
                    {
                            studentService.update(this.student);
                            clearForm();
                            refreshGrid();
                            Notification.show("Neuer Student wurde angelegt.");
                    }
               }




            UI.getCurrent().navigate(StudentenView.class);
        } catch (ValidationException validationException) {
            Notification.show("Es ist leider etwas schief gegangen.");
        }});

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

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        username = new TextField("Username");
        vorname = new TextField("Vorname");
        nachname = new TextField("Nachname");
        email = new TextField("Email");
        passwort = new TextField("Passwort");
        level = new TextField("Level");
        Component[] fields = new Component[]{username, vorname, nachname, email, passwort, level};

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
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout1.add(save, cancel );
        buttonLayout2.add(edit,delete);
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


}
