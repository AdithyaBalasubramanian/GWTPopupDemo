package com.aris.client;

import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.ControlLabel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class UserDetailsPresenter {

    public interface Display {

        ControlLabel getUserNameLabel();

        ControlLabel getFirstNameLabel();

        ControlLabel getLastNameLabel();

        ControlGroup getUserDetails();

        Widget asWidget();
    }

    private final Display display;

    public UserDetailsPresenter() {
        this.display = new UserDetailsView();
    }

    public void go(final HasWidgets container) {
        container.clear();
        displayUser();
        container.add(display.asWidget());
    }

    public void displayUser() {
        display.getUserDetails().setStyleName("details");
        display.getFirstNameLabel().getElement().setInnerHTML("First Name");
        display.getLastNameLabel().getElement().setInnerHTML("Last Name");
        display.getUserNameLabel().getElement().setInnerHTML("User Id");
    }
}
