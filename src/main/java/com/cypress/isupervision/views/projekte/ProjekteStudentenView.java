package com.cypress.isupervision.views.projekte;

import com.cypress.isupervision.views.MainLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;

@PageTitle("Projekte Studenten")
@Route(value = "projects/student:projectID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("STUDENT")
public class ProjekteStudentenView extends Div implements BeforeEnterObserver
{
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent)
    {

    }
}

