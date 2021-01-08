package com.aris.client;

import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.ControlLabel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class UserDetailsView extends Composite implements UserDetailsPresenter.Display {

    @UiTemplate("UserDetailsView.ui.xml")
    interface Binder extends UiBinder<DockLayoutPanel, UserDetailsView> {}
    private static final Binder binder = GWT.create(Binder.class);

    @UiField
    ControlLabel userNameLabel;

    @UiField
    ControlLabel firstNameLabel;

    @UiField
    ControlLabel lastNameLabel;

    @UiField
    ControlGroup userDetails;

    @Override
    public ControlLabel getUserNameLabel() {
        return userNameLabel;
    }

    @Override
    public ControlLabel getFirstNameLabel() {
        return firstNameLabel;
    }

    @Override
    public ControlLabel getLastNameLabel() {
        return lastNameLabel;
    }

    public ControlGroup getUserDetails() {
        return userDetails;
    }

    public Widget asWidget() {
        return this;
    }

    public UserDetailsView() {
        DockLayoutPanel panel = binder.createAndBindUi(this);
        panel.setStyleName("content");
        initWidget(panel);
    }
}
