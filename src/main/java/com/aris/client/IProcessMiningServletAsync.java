package com.aris.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>ProcessMiningServlet</code>.
 */
public interface IProcessMiningServletAsync {
  void redirectURI(String input, String hostName, AsyncCallback<String> callback)
      throws IllegalArgumentException;

}
