package com.aris.client;

import com.aris.shared.Person;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>ProcessMiningServlet</code>.
 */
public interface IProcessMiningServletAsync {
  void redirectURI(String authCode, String hostName, AsyncCallback<Person> callback)
      throws IllegalArgumentException;

}
