package com.cypress.isupervision.views.assistenten;

import com.cypress.isupervision.data.entity.user.Assistant;
import com.cypress.isupervision.data.service.AssistantService;
import com.cypress.isupervision.data.service.UserService;
import com.cypress.isupervision.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.EmailField;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@PageTitle("Assistenten")
@Route(value = "assistenten/:assistentID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class AssistentenView extends Div implements BeforeEnterObserver {

    private final String ASSISTENT_ID = "assistentID";
    private final String ASSISTENT_EDIT_ROUTE_TEMPLATE = "assistenten/%s/edit";

    private Grid<Assistant> grid = new Grid<>(Assistant.class, false);

    private TextField username;
    private TextField firstname;
    private TextField lastname;
    private EmailField email;
    private TextField password;
    private TextField projLimit;
    private TextField baLimit;
    private TextField maLimit;

    private Button cancel = new Button("Abbrechen");
    private Button save = new Button("Speichern");
    private Button delete = new Button("Löschen");
    private Button edit = new Button("Ändern");

    private BeanValidationBinder<Assistant> binder;
    private Assistant assistant;
    private final AssistantService assistantService;
    private Dialog warning = new Dialog();

    @Autowired
    public AssistentenView(AssistantService assistantService, UserService userService) {
        this.assistantService = assistantService;
        addClassNames("assistenten-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);
        createDialog();
        add(splitLayout);

        // Configure Grid
        grid.addColumn("username").setAutoWidth(true);
        grid.addColumn("firstname").setAutoWidth(true);
        grid.addColumn("lastname").setAutoWidth(true);
        grid.addColumn("email").setAutoWidth(true);
        grid.addColumn("password").setAutoWidth(true);
        grid.addColumn("projLimit").setAutoWidth(true);
        grid.addColumn("baLimit").setAutoWidth(true);
        grid.addColumn("maLimit").setAutoWidth(true);

        grid.getColumnByKey("username").setHeader("Benutzername");
        grid.getColumnByKey("firstname").setHeader("Vorname");
        grid.getColumnByKey("lastname").setHeader("Nachname");
        grid.getColumnByKey("email").setHeader("Email");
        grid.getColumnByKey("password").setHeader("Passwort");
        grid.getColumnByKey("projLimit").setHeader("PA-Limit");
        grid.getColumnByKey("baLimit").setHeader("BA-Limit");
        grid.getColumnByKey("maLimit").setHeader("MA-Limit");

        grid.setItems(query -> assistantService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(ASSISTENT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(AssistentenView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Assistant.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(projLimit).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("projLimit");
        binder.forField(baLimit).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("baLimit");
        binder.forField(maLimit).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("maLimit");
        binder.bindInstanceFields(this);

        //Hook up Cancel Button
        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        //Hook up Save Button
        save.addClickListener(e -> {
            try {
                this.assistant = new Assistant();
                binder.writeBean(this.assistant);

                if(username.getValue().trim().equals("") || firstname.getValue().trim().equals("") || lastname.getValue().trim().equals("") || email.getValue().trim().equals("") || password.getValue().trim().equals("") || projLimit.getValue().trim().equals("") || baLimit.getValue().trim().equals("")  || maLimit.getValue().trim().equals(""))
                {
                    Notification.show("Bitte alle Felder ausfüllen.");
                }
                else
                {
                    if (this.assistant != null)
                    {
                        int exists = userService.exists(this.assistant);
                        if (exists == 0)
                        {
                            assistantService.update(this.assistant);
                            clearForm();
                            refreshGrid();
                            Notification.show("Neuer Assistent wurde angelegt.");
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
                }
                UI.getCurrent().navigate(AssistentenView.class);
            } catch (ValidationException validationException) {
                Notification.show("Es ist leider etwas schief gegangen.");
            }
        });

        //Hook up Delete Button
        delete.addClickListener(e -> {
            warning.open();
        });

        //Hook up Edit Button
        edit.addClickListener(e-> {
            try
            {
                if (this.assistant != null)
                {
                    binder.writeBean(this.assistant);
                    if(username.getValue().trim().equals("") || firstname.getValue().trim().equals("") || lastname.getValue().trim().equals("") || email.getValue().trim().equals("") || password.getValue().trim().equals("")  || projLimit.getValue().trim().equals("") || baLimit.getValue().trim().equals("")  || maLimit.getValue().trim().equals(""))
                    {
                        Notification.show("Bitte alle Felder ausfüllen.");
                    }
                    else
                    {
                        assistantService.update(this.assistant);
                        clearForm();
                        refreshGrid();
                        Notification.show("Assistent wurde bearbeitet.");
                    }
                }
                UI.getCurrent().navigate(AssistentenView.class);
            } catch (ValidationException validationException) {
                Notification.show("Es ist leider etwas schief gegangen.");
            }});
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> assistentId = event.getRouteParameters().get(ASSISTENT_ID).map(UUID::fromString);
        if (assistentId.isPresent()) {
            Optional<Assistant> assistantFromBackend = assistantService.get(assistentId.get());
            if (assistantFromBackend.isPresent()) {
                populateForm(assistantFromBackend.get());
            } else {
                Notification.show(String.format("The requested assistent was not found, ID = %s", assistentId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(AssistentenView.class);
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
        username = new TextField("Benutzername");
        firstname = new TextField("Vorname");
        lastname = new TextField("Nachname");
        email = new EmailField("Email");
        password = new TextField("Passwort");
        projLimit = new TextField("Proj Limit");
        baLimit = new TextField("Ba Limit");
        maLimit = new TextField("Ma Limit");
        Component[] fields = new Component[]{username, firstname, lastname, email, password, projLimit, baLimit, maLimit};

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

    private void createDialog()
    {
        warning.add(new H4("Löschen"));
        warning.add(new Paragraph("Sind Sie sich sicher, dass Sie diesen Assistenten löschen möchten?"));
        Button delete = new Button("Löschen");
        Button cancel = new Button("Abbrechen");
        warning.add(delete, cancel);
        delete.addClickListener(event -> {confirmDelete();});
        cancel.addClickListener(event->{cancelDelete();});
    }
    private void confirmDelete()
    {
        binder.readBean(this.assistant);
        assistantService.delete(this.assistant.getId());
        refreshGrid();
        Notification.show("Assistent wurde gelöscht.");
        warning.close();
    }
    private void cancelDelete()
    {
        Notification.show("Löschen wurde abgebrochen.");
        warning.close();
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

    private void populateForm(Assistant value) {
        this.assistant = value;
        binder.readBean(this.assistant);
    }
}