/*
 * iSupervision
 * AccountStudentenView
 * AccountView for student users
 * Author: Martin Lunelli
 * Last Change: 04.06.2022
 */

package com.cypress.isupervision.views.account;

import com.cypress.isupervision.views.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import javax.annotation.security.RolesAllowed;

@PageTitle("Account Studenten")
@Route(value = "account/student", layout = MainLayout.class)
@RolesAllowed("STUDENT")
public class AccountStudentenView extends VerticalLayout
{

    public AccountStudentenView()
    {

    }
}