package com.cypress.isupervision.views.bachelorarbeiten;

import com.cypress.isupervision.data.entity.project.BachelorsThesis;
import com.cypress.isupervision.data.service.BachelorsThesisService;
import com.cypress.isupervision.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
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
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@PageTitle("Bachelorarbeiten")
@Route(value = "bachelorsthesis/:bachelorsthesisID?/:action?(edit)", layout = MainLayout.class)
@PermitAll
public class BachelorarbeitenView extends Div implements BeforeEnterObserver {

    private final String BACHELORSTHESIS_ID = "bachelorsthesisID";
    private final String BACHELORSTHESIS_EDIT_ROUTE_TEMPLATE = "bachelorsthesis/%s/edit";

    private Grid<BachelorsThesis> grid = new Grid<>(BachelorsThesis.class, false);

    private TextField title;
    private TextField assistant;
    private TextField student;
    private DatePicker deadline;

    private Button cancel = new Button("Abbrechen");
    private Button save = new Button("Speichern");
    private Button delete = new Button("Löschen");
    private Button edit = new Button("Ändern");

    private BeanValidationBinder<BachelorsThesis> binder;

    private BachelorsThesis bachelorsThesis;

    private final BachelorsThesisService bachelorsThesisService;

    @Autowired
    public BachelorarbeitenView(BachelorsThesisService bachelorsThesisService) {
        this.bachelorsThesisService = bachelorsThesisService;
        addClassNames("bachelorarbeiten-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("title").setWidth("800px");
        grid.addColumn("assistant").setAutoWidth(true);
        grid.addColumn("student").setAutoWidth(true);
        grid.addColumn("deadline").setAutoWidth(true);

        grid.getColumnByKey("title").setHeader("Titel");
        grid.getColumnByKey("assistant").setHeader("Assistent");
        grid.getColumnByKey("student").setHeader("Student");
        grid.getColumnByKey("deadline").setHeader("Deadline");

        grid.setItems(query -> bachelorsThesisService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(BACHELORSTHESIS_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(BachelorarbeitenView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(BachelorsThesis.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.bachelorsThesis == null) {
                    this.bachelorsThesis = new BachelorsThesis();
                }
                binder.writeBean(this.bachelorsThesis);

                bachelorsThesisService.update(this.bachelorsThesis);
                clearForm();
                refreshGrid();
                Notification.show("Bachelorarbeit details stored.");
                UI.getCurrent().navigate(BachelorarbeitenView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the bachelorarbeit details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> bachelorarbeitId = event.getRouteParameters().get(BACHELORSTHESIS_ID).map(UUID::fromString);
        if (bachelorarbeitId.isPresent()) {
            Optional<BachelorsThesis> bachelorarbeitFromBackend = bachelorsThesisService.get(bachelorarbeitId.get());
            if (bachelorarbeitFromBackend.isPresent()) {
                populateForm(bachelorarbeitFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested bachelorarbeit was not found, ID = %s", bachelorarbeitId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(BachelorarbeitenView.class);
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
        title = new TextField("Titel");
        assistant = new TextField("Assistent");
        student = new TextField("Student");
        deadline = new DatePicker("Deadline");
        Component[] fields = new Component[]{title, assistant, student, deadline};

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

    private void populateForm(BachelorsThesis value) {
        this.bachelorsThesis = value;
        binder.readBean(this.bachelorsThesis);

    }
}
