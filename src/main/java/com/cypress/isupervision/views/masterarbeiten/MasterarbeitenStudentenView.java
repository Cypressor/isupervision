package com.cypress.isupervision.views.masterarbeiten;

import com.cypress.isupervision.views.MainLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;

@PageTitle("Masterarbeiten Studenten")
@Route(value = "mastersthesis/student/:mastersthesisID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("STUDENT")
public class MasterarbeitenStudentenView extends Div implements BeforeEnterObserver
{
    private final String MASTERSTHESIS_ID = "mastersthesisID";
    private final String MASTERSTHESIS_EDIT_ROUTE_TEMPLATE = "mastersthesis/student/%s/edit";

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent)
    {

    }
}
