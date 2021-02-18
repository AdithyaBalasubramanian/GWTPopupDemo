package com.aris.admin.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.*;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class LoginEntryPoint implements EntryPoint {


  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {

    final LoginDetailsPresenter loginDetailsPresenter = new LoginDetailsPresenter();
    RootLayoutPanel container = RootLayoutPanel.get();
    container.clear();
    loginDetailsPresenter.go(container);

  }
}
