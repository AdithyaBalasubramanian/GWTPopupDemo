package com.aris.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface IProcessMiningServlet extends RemoteService {

  String redirectURI(String name) throws IllegalArgumentException;

}
