package com.aris.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("redirect")
public interface IProcessMiningServlet extends RemoteService {

  String redirectURI(String name) throws IllegalArgumentException, IOException;

}
