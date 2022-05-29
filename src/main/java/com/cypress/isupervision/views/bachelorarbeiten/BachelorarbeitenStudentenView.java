package com.cypress.isupervision.views.bachelorarbeiten;

import com.cypress.isupervision.views.MainLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;

@PageTitle("Bachelorarbeiten Studenten")
@Route(value = "bachelorsthesis/student/:bachelorsthesisID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("STUDENT")
public class BachelorarbeitenStudentenView extends Div implements BeforeEnterObserver
{
    private final String BACHELORSTHESIS_ID = "bachelorsthesisID";
    private final String BACHELORSTHESIS_EDIT_ROUTE_TEMPLATE = "bachelorsthesis/student/%s/edit";

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent)
    {

    }
}
