package com.aris.admin.client;


import com.aris.admin.shared.Person;
import com.aris.admin.shared.Verifier;
import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class LoginEntryPointTest extends GWTTestCase {

  /**
   * Must refer to a valid module that sources this class.
   */
  public String getModuleName() {
    return "com.aris.umcJUnit";
  }

  /**
   * Tests the FieldVerifier.
   */
  public void testFieldVerifier() {
    assertFalse(Verifier.isValid(null));
    assertFalse(Verifier.isValid(""));
    assertFalse(Verifier.isValid("a"));
    assertFalse(Verifier.isValid("ab"));
    assertFalse(Verifier.isValid("abc"));
    assertTrue(Verifier.isValid("abcd"));
  }

  /**
   * This test will send a request to the server using the greetServer method in
   * GreetingService and verify the response.
   */
  public void testGreetingService() {
    // Create the service that we will test.
    IProcessMiningServletAsync greetingService = GWT.create(IProcessMiningServlet.class);
    ServiceDefTarget target = (ServiceDefTarget) greetingService;
    target.setServiceEntryPoint(GWT.getModuleBaseURL() + "umc/greet");

    // Since RPC calls are asynchronous, we will need to wait for a response
    // after this test method returns. This line tells the test runner to wait
    // up to 10 seconds before timing out.
    delayTestFinish(10000);

    // Send a request to the server.
    greetingService.redirectURI("GWT User", "", new AsyncCallback<Person>() {
      public void onFailure(Throwable caught) {
        // The request resulted in an unexpected error.
        fail("Request failure: " + caught.getMessage());
      }

      public void onSuccess(Person result) {
        // Verify that the response is correct.
        //assertTrue(result.startsWith("Hello, GWT User!"));

        // Now that we have received a response, we need to tell the test runner
        // that the test is complete. You must call finishTest() after an
        // asynchronous test finishes successfully, or the test will time out.
        finishTest();
      }
    });
  }


}
