package com.aris.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class UserDetailsEntryPoint implements EntryPoint {
    @Override
    public void onModuleLoad() {
        String value = com.google.gwt.user.client.Window.Location.getParameter("code");
        String host = Window.Location.getHostName();
        RootLayoutPanel container = RootLayoutPanel.get();
        container.clear();
        final UserDetailsPresenter presenter = new UserDetailsPresenter();
        Model.SERVICE.redirectURI(value, host, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(String userInfo) {
                //Give the user details to the presenter to fill it with
                presenter.go(container);
            }
        });
    }
}
