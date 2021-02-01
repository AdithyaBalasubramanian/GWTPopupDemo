package com.aris.client;

import com.aris.shared.Person;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.io.IOException;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("redirect")
public interface IProcessMiningServlet extends RemoteService {

  Person redirectURI(String authCode, String hostName) throws IllegalArgumentException, IOException;

}
