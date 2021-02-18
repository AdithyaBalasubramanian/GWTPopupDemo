package com.aris.admin.client;


import com.aris.admin.shared.Person;
import com.aris.admin.shared.Utils;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class UserInfoEntryPoint implements EntryPoint {
    @Override
    public void onModuleLoad() {
        Utils.consoleLog("UserInfoEntryPoint onModuleLoad()");
        String file = "com/aris/admin/shared/person.properties";
        Utils.consoleLog("Calling the service readPersonFromFile...");
        Model.SERVICE.readPersonFromFile(file, new AsyncCallback<Person>() {
            @Override
            public void onFailure(Throwable throwable) {
                Utils.consoleLog("Error in getting the userInfo");
            }

            @Override
            public void onSuccess(Person userInfo) {
                //Give the user details to the presenter to fill it with
                Utils.consoleLog("Successfully obtained the userInfo");
                final UserInfoPresenter presenter = new UserInfoPresenter();
                RootLayoutPanel container = RootLayoutPanel.get();
                container.clear();
                presenter.go(container, userInfo);
            }
        });

    }
}
