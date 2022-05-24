package com.cypress.isupervision.views.bachelorarbeiten;

import com.cypress.isupervision.data.entity.Bachelorarbeit;
import com.cypress.isupervision.data.service.BachelorarbeitService;
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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
@Route(value = "bachelorarbeiten/:bachelorarbeitID?/:action?(edit)", layout = MainLayout.class)
@PermitAll
public class BachelorarbeitenView extends Div implements BeforeEnterObserver {

    private final String BACHELORARBEIT_ID = "bachelorarbeitID";
    private final String BACHELORARBEIT_EDIT_ROUTE_TEMPLATE = "bachelorarbeiten/%s/edit";

    private Grid<Bachelorarbeit> grid = new Grid<>(Bachelorarbeit.class, false);

    private TextField titel;
    private TextField assistent;
    private TextField student;
    private DatePicker deadline;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private BeanValidationBinder<Bachelorarbeit> binder;

    private Bachelorarbeit bachelorarbeit;

    private final BachelorarbeitService bachelorarbeitService;

    @Autowired
    public BachelorarbeitenView(BachelorarbeitService bachelorarbeitService) {
        this.bachelorarbeitService = bachelorarbeitService;
        addClassNames("bachelorarbeiten-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("titel").setAutoWidth(true);
        grid.addColumn("assistent").setAutoWidth(true);
        grid.addColumn("student").setAutoWidth(true);
        grid.addColumn("deadline").setAutoWidth(true);
        grid.setItems(query -> bachelorarbeitService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(BACHELORARBEIT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(BachelorarbeitenView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Bachelorarbeit.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.bachelorarbeit == null) {
                    this.bachelorarbeit = new Bachelorarbeit();
                }
                binder.writeBean(this.bachelorarbeit);

                bachelorarbeitService.update(this.bachelorarbeit);
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
        Optional<UUID> bachelorarbeitId = event.getRouteParameters().get(BACHELORARBEIT_ID).map(UUID::fromString);
        if (bachelorarbeitId.isPresent()) {
            Optional<Bachelorarbeit> bachelorarbeitFromBackend = bachelorarbeitService.get(bachelorarbeitId.get());
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
        titel = new TextField("Titel");
        assistent = new TextField("Assistent");
        student = new TextField("Student");
        deadline = new DatePicker("Deadline");
        Component[] fields = new Component[]{titel, assistent, student, deadline};

        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
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

    private void populateForm(Bachelorarbeit value) {
        this.bachelorarbeit = value;
        binder.readBean(this.bachelorarbeit);

    }
}
