package com.aris.client;

import com.aris.shared.Person;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class UserDetailsEntryPoint implements EntryPoint {
    @Override
    public void onModuleLoad() {
        String value = com.google.gwt.user.client.Window.Location.getParameter("code");
        String host = "http://localhost/umc";
        RootLayoutPanel container = RootLayoutPanel.get();
        container.clear();
        final UserDetailsPresenter presenter = new UserDetailsPresenter();
        Model.SERVICE.redirectURI(value, host, new AsyncCallback<Person>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(Person userInfo) {
                //Give the user details to the presenter to fill it with
                presenter.go(container, userInfo);
            }
        });
    }
}
