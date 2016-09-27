package com.ruialonso.library;

import java.util.List;

/**
 * Created by rui.alonso on 27/9/16.
 */

public class FloatingActionsSubmenu {
  private boolean isVisible = false;

  private List<FloatingActionButton> floatingActionButtons;

  public FloatingActionsSubmenu() {

  }

  public void show() {
    if (!isVisible) {
      isVisible = true;
      //TO-DO: show floating action buttons
    }
  }

  public void hide() {
    if (isVisible) {
      isVisible = false;
      //TO-DO: hide floating action buttons
    }
  }
}
