package com.ruialonso.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rui.alonso on 27/9/16.
 */

public class FloatingActionsMenuOld extends ViewGroup {
  public static final int EXPAND_UP = 0;
  public static final int EXPAND_DOWN = 1;
  public static final int EXPAND_LEFT = 2;
  public static final int EXPAND_RIGHT = 3;

  public static final int LABELS_ON_LEFT_SIDE = 0;
  public static final int LABELS_ON_RIGHT_SIDE = 1;
  public static final int LABELS_ON_TOP_SIDE = 2;
  public static final int LABELS_ON_BOTTOM_SIDE = 3;

  private static final int ANIMATION_DURATION = 300;
  private static final float COLLAPSED_PLUS_ROTATION = 0f;
  private static final float EXPANDED_PLUS_ROTATION = 45f;// + 45f; this rotations make + will be x , you can modify this with setRotation


  private View view;
  private int currentSubmenuIndex = -1;
  private boolean isExpanded = false;
  private List<FloatingActionsSubmenu> floatingActionSubmenuItems;
  private List<FloatingActionButton> floatingActionButtonItems;

  private OnFloatingActionsMenuUpdateListener menuUpdateListener;

  public FloatingActionsMenuOld(Context context) {
    super(context);
    init(null, 0);
  }

  public FloatingActionsMenuOld(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs, 0);
  }

  public FloatingActionsMenuOld(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs, defStyleAttr);
  }

  private void init(AttributeSet attrs, int defStyle) {
    loadAttributes(attrs, defStyle);

    view = getRootView();
    /*
    view =
        LayoutInflater.from(getContext()).inflate(R.layout.multiple_grid_recycler_view, this, true);
    */

    initActionsSubmenus();
    initActionButtons();

  }

  private void loadAttributes(AttributeSet attrs, int defStyle) {
    final TypedArray a =
        getContext().obtainStyledAttributes(attrs, R.styleable.FloatingActionsMenu, defStyle, 0);

    try {
      //int icon = a.getResourceId(R.styleable.FloatingActionsMenu_fab_menu_icon, 0);
    } finally {
      a.recycle();
    }
  }

  private void initActionsSubmenus() {
    floatingActionSubmenuItems = new ArrayList<>();
  }

  private void initActionButtons() {
    floatingActionButtonItems = new ArrayList<>();
  }

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    int size = getChildCount();
    for (int i = 0; i < size; i++) {
      Object nextChild = getChildAt(i);
      if (nextChild instanceof FloatingActionsSubmenu) {
        floatingActionSubmenuItems.add((FloatingActionsSubmenu) nextChild);
      }
      if (nextChild instanceof FloatingActionButton) {
        floatingActionButtonItems.add((FloatingActionButton) nextChild);
      }
    }

    if (!floatingActionSubmenuItems.isEmpty()) {
      currentSubmenuIndex = 0;
      floatingActionSubmenuItems.get(currentSubmenuIndex).show();
    }
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
    if (menuUpdateListener != null) {
      menuUpdateListener.onSubmenuVisible();
    }
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

      if (menuUpdateListener != null) {
        menuUpdateListener.onMenuExpanded();
      }
    }
  }

  public void collapse() {
    if (isExpanded) {
      isExpanded = false;
      //TO-DO: hide floating action buttons

      if (menuUpdateListener != null) {
        menuUpdateListener.onMenuCollapsed();
      }
    }
  }

  private boolean hasSubmenu() {
    return floatingActionSubmenuItems != null && !floatingActionSubmenuItems.isEmpty();
  }

  public void setOnFloatingActionsMenuUpdateListener(OnFloatingActionsMenuUpdateListener listener) {
    this.menuUpdateListener = listener;
  }

  public interface OnFloatingActionsMenuUpdateListener {
    void onSubmenuVisible();

    void onMenuExpanded();

    void onMenuCollapsed();
  }
}
