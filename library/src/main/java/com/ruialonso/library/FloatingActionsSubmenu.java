package com.ruialonso.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rui.alonso on 27/9/16.
 */

public class FloatingActionsSubmenu extends ViewGroup {
  public static final int EXPAND_UP = 0;
  public static final int EXPAND_DOWN = 1;
  public static final int EXPAND_LEFT = 2;
  public static final int EXPAND_RIGHT = 3;
  public static final int EXPAND_ROUND = 4;
  public static final int EXPAND_FAN = 5;

  public boolean isVisible = false;

  private boolean enableOverlay;
  private int expandDirection;
  private String submenuGroup;
  private int buttonSpacing;
  @DrawableRes private int submenuIconRes;
  private Drawable submenuIcon;

  private int menuLeft;
  private int menuTop;
  private int menuRight;
  private int menuBottom;

  private FloatingActionsMenu menu;

  private List<FloatingActionButton> floatingActionButtonItems;

  private OnFloatingActionSubmenuUpdateListener submenuUpdateListener;

  //region constructor
  public FloatingActionsSubmenu(Context context) {
    super(context);
    init(null, 0);
  }

  public FloatingActionsSubmenu(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs, 0);
  }

  public FloatingActionsSubmenu(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs, defStyleAttr);
  }
  //endregion

  private void init(AttributeSet attrs, int defStyle) {
    loadAttributes(attrs, defStyle);

    initViews();
  }

  private void loadAttributes(AttributeSet attributeSet, int defStyle) {
    TypedArray attrSubmenu =
        getContext().obtainStyledAttributes(attributeSet, R.styleable.FloatingActionsSubmenu, 0, 0);

    enableOverlay =
        attrSubmenu.getBoolean(R.styleable.FloatingActionsSubmenu_fab_enable_overlay, false);
    expandDirection =
        attrSubmenu.getInt(R.styleable.FloatingActionsSubmenu_fab_expand_direction, EXPAND_UP);
    submenuGroup = attrSubmenu.getString(R.styleable.FloatingActionsSubmenu_fab_submenu_group);

    buttonSpacing = attrSubmenu.getInt(R.styleable.FloatingActionsSubmenu_fab_button_spacing, 5);
    submenuIconRes =
        attrSubmenu.getResourceId(R.styleable.FloatingActionsSubmenu_fab_submenu_icon, 0);

    attrSubmenu.recycle();
  }

  private void initViews() {
    floatingActionButtonItems = new ArrayList<>();

    if (submenuIconRes != 0) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        submenuIcon = getContext().getDrawable(submenuIconRes);
      } else {
        submenuIcon = getContext().getResources().getDrawable(submenuIconRes);
      }
    }
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();

    int size = getChildCount();
    for (int i = 0; i < size; i++) {
      Object nextChild = getChildAt(i);
      if (nextChild instanceof FloatingActionButton) {
        floatingActionButtonItems.add((FloatingActionButton) nextChild);
      }
    }
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    menu = (FloatingActionsMenu) getParent();
    menu.floatingActionMenuButton.setIconDrawable(submenuIcon);

    validateAttributes();
  }

  private void validateAttributes() {
    switch (menu.verticalAlignment) {
      case FloatingActionsMenu.ALIGNMENT_CENTER:
        break;
      case FloatingActionsMenu.ALIGNMENT_TOP:
        if (expandDirection == EXPAND_UP) {
          throw new IllegalArgumentException("Menu alignment top cannot support submenu expand up");
        }
        break;
      case FloatingActionsMenu.ALIGNMENT_BOTTOM:
        if (expandDirection == EXPAND_DOWN) {
          throw new IllegalArgumentException(
              "Menu alignment bottom cannot support submenu expand down");
        }
        break;
    }

    switch (menu.horizontalAlignment) {
      case FloatingActionsMenu.ALIGNMENT_CENTER:
        break;
      case FloatingActionsMenu.ALIGNMENT_LEFT:
        if (expandDirection == EXPAND_LEFT) {
          throw new IllegalArgumentException(
              "Menu alignment left cannot support submenu expand left");
        }
        break;
      case FloatingActionsMenu.ALIGNMENT_RIGHT:
        if (expandDirection == EXPAND_RIGHT) {
          throw new IllegalArgumentException(
              "Menu alignment right cannot support submenu expand right");
        }
        break;
    }
  }

  //region measure
  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int count = getChildCount();
    // Measurement will ultimately be computing these values.
    int maxHeight = 0;
    int maxWidth = 0;
    int width;
    int height;

    for (int i = 0; i < count; i++) {
      final View child = getChildAt(i);

      if (child.getVisibility() == GONE) return;

      child.measure(widthMeasureSpec, heightMeasureSpec);
      width = child.getMeasuredWidth();
      height = child.getMeasuredHeight();

      switch (expandDirection) {
        case EXPAND_UP:
        case EXPAND_DOWN:
          maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
          height += buttonSpacing * (getChildCount() - 1);
          height = adjustForOvershoot(height);
          break;
        case EXPAND_LEFT:
        case EXPAND_RIGHT:
          width += buttonSpacing * (getChildCount() - 1);
          width = adjustForOvershoot(width);
          break;
        case EXPAND_ROUND:
        case EXPAND_FAN:
          maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
          height += buttonSpacing * (getChildCount() - 1);
          height = adjustForOvershoot(height);

          maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
          width += buttonSpacing * (getChildCount() - 1);
          width = adjustForOvershoot(width);
          break;
      }

      maxWidth = Math.max(maxWidth, width);
      maxHeight = Math.max(maxHeight, height);
    }

    // Report our final dimensions.
    setMeasuredDimension(maxWidth, maxHeight);
  }

  private int adjustForOvershoot(int dimension) {
    return dimension * 12 / 10;
  }
  //endregion

  //region layout
  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    //get the available size of child view
    menuLeft = this.getPaddingLeft();
    menuTop = this.getPaddingTop();
    menuRight = this.getMeasuredWidth() - this.getPaddingRight();
    menuBottom = this.getMeasuredHeight() - this.getPaddingBottom();

    setRootLayout(menuLeft, menuTop, menuRight, menuBottom);
    setChildrenLayout(menuLeft, menuTop, menuRight, menuBottom);
  }

  private void setRootLayout(int left, int top, int right, int bottom) {
    final int maxWidth = right - left;
    final int maxHeight = bottom - top;

    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();

    switch (expandDirection) {
      case EXPAND_UP:
        layoutParams.addRule(RelativeLayout.ABOVE, R.id.fab_menu_button);
        layoutParams.setMargins(0, 0, 0, menu.floatingActionMenuButton.getMeasuredHeight());
        break;
      case EXPAND_DOWN:
        layoutParams.addRule(RelativeLayout.BELOW, R.id.fab_menu_button);
        layoutParams.setMargins(0, menu.floatingActionMenuButton.getMeasuredHeight(), 0, 0);

        break;
      case EXPAND_LEFT:
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.fab_menu_button);
        layoutParams.setMargins(0, 0, menu.floatingActionMenuButton.getMeasuredWidth(), 0);

        break;
      case EXPAND_RIGHT:
        layoutParams.addRule(RelativeLayout.ALIGN_RIGHT, R.id.fab_menu_button);
        layoutParams.setMargins(menu.floatingActionMenuButton.getMeasuredWidth(), 0, 0, 0);

        break;
      case EXPAND_ROUND:

        break;
      case EXPAND_FAN:

        break;
    }

    setLayoutParams(layoutParams);
  }

  private void setChildrenLayout(int left, int top, int right, int bottom) {
    int count = getChildCount();

    int childWidth, childHeight, childLeft, childTop;
    final int maxWidth = right - left;
    final int maxHeight = bottom - top;

    int nextX = left;
    int nextY = top;

    for (int i = 0; i < count; i++) {
      View child = getChildAt(i);
      if (child.getVisibility() == GONE) return;

      childLeft = nextX;
      childTop = nextY;

      /*
      switch (expandDirection) {
        case FloatingActionsMenu.EXPAND_UP:
          childTop -= child.getMeasuredHeight();
          nextY = childTop - buttonSpacing;
          break;
        case FloatingActionsMenu.EXPAND_DOWN:
          childTop = top;
          nextY = childTop + buttonSpacing;
          break;
        case FloatingActionsMenu.EXPAND_LEFT:
          childLeft -= child.getMeasuredWidth();
          nextX = childLeft - buttonSpacing;
          break;
        case FloatingActionsMenu.EXPAND_RIGHT:
          childLeft = left;
          nextX = childLeft + buttonSpacing;
          break;
        case FloatingActionsMenu.EXPAND_HORIZONTAL:

          break;
        case FloatingActionsMenu.EXPAND_ROUND:
        case FloatingActionsMenu.EXPAND_FAN:

          break;
      }
      */

      //Get the maximum size of the child
      child.measure(MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.AT_MOST),
          MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST));
      childWidth = child.getMeasuredWidth();
      childHeight = child.getMeasuredHeight();

      //do the layout
      child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
    }
  }
  //endregion

  //region Add/Remove buttons
  public boolean addButton(FloatingActionButton floatingActionButton) {
    boolean added;
    if (this.floatingActionButtonItems == null) {
      this.floatingActionButtonItems = new ArrayList<>();
    }
    added = this.floatingActionButtonItems.add(floatingActionButton);
    if (added) addView(floatingActionButton);
    return added;
  }

  public void addButtons(List<FloatingActionButton> floatingActionButtonItems) {
    for (FloatingActionButton floatingActionButton : floatingActionButtonItems) {
      addButton(floatingActionButton);
    }
  }

  public boolean removeButton(FloatingActionButton floatingActionButton) {
    boolean removed = false;
    if (this.floatingActionButtonItems != null) {
      removeView(floatingActionButton);
      removed = this.floatingActionButtonItems.remove(floatingActionButton);
    }
    return removed;
  }
  //endregion

  //region Expand/Collapse submenu
  public void expand() {
    if (!isVisible) {
      isVisible = true;

      setVisibility(VISIBLE);
      //TO-DO: expand floating action buttons with animation
      for (FloatingActionButton floatingActionButton : floatingActionButtonItems) {
      }

      if (submenuUpdateListener != null) {
        submenuUpdateListener.onMenuExpanded();
      }
    }
  }

  public void collapse() {
    if (isVisible) {
      isVisible = false;

      setVisibility(INVISIBLE);
      //TO-DO: collapse floating action buttons with animation
      for (FloatingActionButton floatingActionButton : floatingActionButtonItems) {
      }

      if (submenuUpdateListener != null) {
        submenuUpdateListener.onMenuCollapsed();
      }
    }
  }

  @Override public void setVisibility(int visibility) {
    if (visibility == VISIBLE) {
      isVisible = true;
    } else {
      isVisible = false;
    }
    super.setVisibility(visibility);
  }
  //endregion

  public String getSubmenuGroup() {
    return submenuGroup;
  }

  public boolean isEnableOverlay() {
    return enableOverlay;
  }

  public void setOnFloatingActionSubmenuUpdateListener(
      OnFloatingActionSubmenuUpdateListener listener) {
    this.submenuUpdateListener = listener;
  }

  public interface OnFloatingActionSubmenuUpdateListener {
    void onMenuExpanded();

    void onMenuCollapsed();
  }
}
