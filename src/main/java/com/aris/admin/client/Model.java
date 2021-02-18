package com.aris.admin.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class Model {

    public static String CONTEXT = "login/";

    public static IProcessMiningServletAsync SERVICE;
    
    static {
        String hostPageURL = GWT.getHostPageBaseURL();
        if (!hostPageURL.endsWith(CONTEXT)) {
            hostPageURL += CONTEXT;
        }
        SERVICE = GWT.create(IProcessMiningServlet.class);
        ServiceDefTarget t = (ServiceDefTarget)SERVICE;
        String serviceEntryPoint = hostPageURL + "umc/redirect";
        t.setServiceEntryPoint(serviceEntryPoint);
    }
}
