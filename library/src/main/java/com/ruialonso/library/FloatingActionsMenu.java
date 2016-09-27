package com.ruialonso.library;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rui.alonso on 27/9/16.
 */

public class FloatingActionsMenu {
  private int currentSubmenuIndex = -1;
  private boolean isExpanded = false;
  private List<FloatingActionsSubmenu> floatingActionSubmenuItems;
  private List<FloatingActionButton> floatingActionButtonItems;

  public FloatingActionsMenu() {

  }

  public void addActionSubmenu(FloatingActionsSubmenu floatingActionsSubmenu) {
    if (floatingActionSubmenuItems == null) {
      floatingActionSubmenuItems = new ArrayList<>();
    }
    floatingActionSubmenuItems.add(floatingActionsSubmenu);
  }

  public void removeActionSubmenu(FloatingActionsSubmenu floatingActionsSubmenu) {
    if (floatingActionSubmenuItems != null) {
      floatingActionSubmenuItems.remove(floatingActionsSubmenu);
    }
  }

  public void removeActionSubmenu(int floatingActionsSubmenuIndex) {
    if (floatingActionSubmenuItems != null) {
      floatingActionSubmenuItems.remove(floatingActionsSubmenuIndex);
    }
  }

  public void addActionButton(FloatingActionButton floatingActionButton) {
    if (floatingActionButtonItems == null) {
      floatingActionButtonItems = new ArrayList<>();
    }
    floatingActionButtonItems.add(floatingActionButton);
  }

  public void removeActionButton(FloatingActionButton floatingActionButton) {
    if (floatingActionButtonItems != null) {
      floatingActionButtonItems.remove(floatingActionButton);
    }
  }

  public void removeActionButton(int floatingActionButtonIndex) {
    if (floatingActionButtonItems != null) {
      floatingActionButtonItems.remove(floatingActionButtonIndex);
    }
  }

  public void toogle() {
    if (hasSubmenu()) {
      toogleSubmenus();
    } else {
      toggleActionButtons();
    }
  }

  private void toogleSubmenus() {
    floatingActionSubmenuItems.get(currentSubmenuIndex).hide();

    if (currentSubmenuIndex < floatingActionSubmenuItems.size() - 1) {
      currentSubmenuIndex++;
    } else {
      currentSubmenuIndex = 0;
    }

    floatingActionSubmenuItems.get(currentSubmenuIndex).show();
  }

  private void toggleActionButtons() {
    if (isExpanded) {
      collapse();
    } else {
      expand();
    }
  }

  public void expand() {
    if (!isExpanded) {
      isExpanded = true;
      //TO-DO: show floating action buttons
    }
  }

  public void collapse() {
    if (isExpanded) {
      isExpanded = false;
      //TO-DO: hide floating action buttons
    }
  }

  private boolean hasSubmenu() {
    return floatingActionSubmenuItems != null && !floatingActionSubmenuItems.isEmpty();
  }
}
