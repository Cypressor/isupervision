/*
 * iSupervision
 * BachelorarbeitenView
 * links users to their corresponding bachelorstheses views
 * Author: Martin Lunelli
 * Last Change: 04.06.2022
 */

package com.cypress.isupervision.views.bachelorarbeiten;

import com.cypress.isupervision.data.Role;
import com.cypress.isupervision.security.AuthenticatedUser;
import com.cypress.isupervision.views.MainLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.Set;
import javax.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Bachelorarbeiten")
@Route(value = "bachelorstheses", layout = MainLayout.class)
@PermitAll
public class BachelorarbeitenView extends Div implements BeforeEnterObserver {

    private AuthenticatedUser authenticatedUser;

    @Autowired
    public BachelorarbeitenView(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser=authenticatedUser;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        redirect(event);
    }

    private void redirect(BeforeEnterEvent event)
    {
        Set<Role> roles = authenticatedUser.get().get().getRoles();
        if (roles.toString().contains("ADMIN") || roles.toString().contains("ASSISTANT"))
        {
            event.rerouteTo(BachelorarbeitenAssistentenView.class);
        }
        else if (roles.toString().contains("STUDENT"))
        {
            event.rerouteTo(BachelorarbeitenStudentenView.class);
        }
    }
}
