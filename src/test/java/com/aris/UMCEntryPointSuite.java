package com.aris;

import com.aris.client.UMCEntryPointTest;
import com.google.gwt.junit.tools.GWTTestSuite;
import junit.framework.Test;
import junit.framework.TestSuite;

public class UMCEntryPointSuite extends GWTTestSuite {
  public static Test suite() {
    TestSuite suite = new TestSuite("Tests for umc");
    suite.addTestSuite(UMCEntryPointTest.class);
    return suite;
  }
}
