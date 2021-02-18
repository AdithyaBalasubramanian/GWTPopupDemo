package com.aris.admin;


import com.aris.admin.client.LoginEntryPointTest;
import com.google.gwt.junit.tools.GWTTestSuite;
import junit.framework.Test;
import junit.framework.TestSuite;

public class LoginEntryPointSuite extends GWTTestSuite {
  public static Test suite() {
    TestSuite suite = new TestSuite("Tests for umc");
    suite.addTestSuite(LoginEntryPointTest.class);
    return suite;
  }
}
