package com.cypress.isupervision.views.assistenten;

import com.cypress.isupervision.data.entity.Assistent;
import com.cypress.isupervision.data.service.AssistentService;
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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@PageTitle("Assistenten")
@Route(value = "assistenten/:assistentID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class AssistentenView extends Div implements BeforeEnterObserver {

    private final String ASSISTENT_ID = "assistentID";
    private final String ASSISTENT_EDIT_ROUTE_TEMPLATE = "assistenten/%s/edit";

    private Grid<Assistent> grid = new Grid<>(Assistent.class, false);

    private TextField vorname;
    private TextField nachname;
    private TextField email;
    private TextField passwort;
    private TextField projLimit;
    private TextField baLimit;
    private TextField maLimit;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private BeanValidationBinder<Assistent> binder;

    private Assistent assistent;

    private final AssistentService assistentService;

    @Autowired
    public AssistentenView(AssistentService assistentService) {
        this.assistentService = assistentService;
        addClassNames("assistenten-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("vorname").setAutoWidth(true);
        grid.addColumn("nachname").setAutoWidth(true);
        grid.addColumn("email").setAutoWidth(true);
        grid.addColumn("passwort").setAutoWidth(true);
        grid.addColumn("projLimit").setAutoWidth(true);
        grid.addColumn("baLimit").setAutoWidth(true);
        grid.addColumn("maLimit").setAutoWidth(true);
        grid.setItems(query -> assistentService.list(
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
        binder = new BeanValidationBinder<>(Assistent.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(projLimit).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("projLimit");
        binder.forField(baLimit).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("baLimit");
        binder.forField(maLimit).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("maLimit");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.assistent == null) {
                    this.assistent = new Assistent();
                }
                binder.writeBean(this.assistent);

                assistentService.update(this.assistent);
                clearForm();
                refreshGrid();
                Notification.show("Assistent details stored.");
                UI.getCurrent().navigate(AssistentenView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the assistent details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> assistentId = event.getRouteParameters().get(ASSISTENT_ID).map(UUID::fromString);
        if (assistentId.isPresent()) {
            Optional<Assistent> assistentFromBackend = assistentService.get(assistentId.get());
            if (assistentFromBackend.isPresent()) {
                populateForm(assistentFromBackend.get());
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
        vorname = new TextField("Vorname");
        nachname = new TextField("Nachname");
        email = new TextField("Email");
        passwort = new TextField("Passwort");
        projLimit = new TextField("Proj Limit");
        baLimit = new TextField("Ba Limit");
        maLimit = new TextField("Ma Limit");
        Component[] fields = new Component[]{vorname, nachname, email, passwort, projLimit, baLimit, maLimit};

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

    private void populateForm(Assistent value) {
        this.assistent = value;
        binder.readBean(this.assistent);

    }
}
