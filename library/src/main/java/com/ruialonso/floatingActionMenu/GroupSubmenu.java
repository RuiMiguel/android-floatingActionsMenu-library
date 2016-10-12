package com.ruialonso.floatingactionmenu;

import android.graphics.drawable.Drawable;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rui.alonso on 5/10/16.
 */

public class GroupSubmenu {
  private String groupName;
  private boolean active;
  private Drawable submenuIcon;
  private List<FloatingActionsSubmenu> submenuList;

  public GroupSubmenu() {
    this.groupName = "";
    this.active = false;
    this.submenuList = new ArrayList<>();
  }

  public boolean add(FloatingActionsSubmenu submenu) {
    if (this.submenuList == null) this.submenuList = new ArrayList<>();
    return this.submenuList.add(submenu);
  }

  public boolean remove(FloatingActionsSubmenu submenu) {
    if (this.submenuList == null) return false;
    return this.submenuList.remove(submenu);
  }

  public void collapse() {
    for (FloatingActionsSubmenu floatingActionsSubmenu : this.submenuList) {
      floatingActionsSubmenu.setVisibility(View.GONE);
      //floatingActionsSubmenu.toggle();
    }
  }

  public void expand() {
    for (FloatingActionsSubmenu floatingActionsSubmenu : this.submenuList) {
      floatingActionsSubmenu.setVisibility(View.VISIBLE);
      //floatingActionsSubmenu.toggle();

      floatingActionsSubmenu.animate();
      submenuIcon = floatingActionsSubmenu.getSubmenuIcon();
    }
  }

  public int size() {
    return this.submenuList.size();
  }

  public boolean isEmpty() {
    return this.submenuList.isEmpty();
  }

  public String getGroupName() {
    return this.groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public Drawable getSubmenuIcon() {
    return submenuIcon;
  }

  public boolean isOverlayEnabled() {
    boolean isEnabled = false;
    if (this.submenuList != null && this.submenuList.size() > 0) {
      isEnabled = this.submenuList.get(0).isEnableOverlay();
    }
    return isEnabled;
  }
}
