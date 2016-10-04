package com.ruialonso.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
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
  public boolean isVisible = false;

  private int expandDirection;
  private int buttonSpacing;

  private int menuLeft;
  private int menuTop;
  private int menuRight;
  private int menuBottom;

  private Point menuCenter;

  private List<FloatingActionButton> floatingActionButtonItems;

  private OnFloatingActionSubmenuUpdateListener submenuUpdateListener;

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

  private void init(AttributeSet attrs, int defStyle) {
    loadAttributes(attrs, defStyle);

    initViews();
  }

  private void loadAttributes(AttributeSet attributeSet, int defStyle) {
    TypedArray attrSubmenu =
        getContext().obtainStyledAttributes(attributeSet, R.styleable.FloatingActionsSubmenu, 0, 0);

    expandDirection =
        attrSubmenu.getInt(R.styleable.FloatingActionsSubmenu_fab_expand_direction, FloatingActionsMenu.EXPAND_UP);
    buttonSpacing =
        attrSubmenu.getInt(R.styleable.FloatingActionsSubmenu_fab_button_spacing, 5);


    attrSubmenu.recycle();
  }

  private void initViews() {
    floatingActionButtonItems = new ArrayList<>();
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

  //childTop += floatingActionMenuButton.getMeasuredHeight();
  //childLeft += floatingActionMenuButton.getMeasuredWidth();

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
        case FloatingActionsMenu.EXPAND_UP:
        case FloatingActionsMenu.EXPAND_DOWN:
          maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
          height += child.getMeasuredHeight();
          height += buttonSpacing * (getChildCount() - 1);
          height = adjustForOvershoot(height);
          break;
        case FloatingActionsMenu.EXPAND_LEFT:
        case FloatingActionsMenu.EXPAND_RIGHT:
          width += child.getMeasuredWidth();
          width += buttonSpacing * (getChildCount() - 1);
          width = adjustForOvershoot(width);
          break;
        case FloatingActionsMenu.EXPAND_HORIZONTAL:
          maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
          width += child.getMeasuredWidth();
          width += buttonSpacing * (getChildCount() - 1);
          width = adjustForOvershoot(width);
          break;
        case FloatingActionsMenu.EXPAND_ROUND:
        case FloatingActionsMenu.EXPAND_FAN:
          maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
          height += child.getMeasuredHeight();
          height += buttonSpacing * (getChildCount() - 1);
          height = adjustForOvershoot(height);

          maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
          width += child.getMeasuredWidth();
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

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    final int count = getChildCount();

    //get the available size of child view
    menuLeft = this.getPaddingLeft();
    menuTop = this.getPaddingTop();
    menuRight = this.getMeasuredWidth() - this.getPaddingRight();
    menuBottom = this.getMeasuredHeight() - this.getPaddingBottom();

    setRootLayout(menuLeft, menuTop, menuRight, menuBottom);

    for (int i = 0; i < count; i++) {
      View child = getChildAt(i);
      if (child.getVisibility() != GONE) {
        setChildrenLayout(child, menuLeft, menuTop, menuRight, menuBottom);
      }
    }
  }

  private void setRootLayout(int left, int top, int right, int bottom) {
    final int maxWidth = right - left;
    final int maxHeight = bottom - top;

    switch (expandDirection) {
      case FloatingActionsMenu.EXPAND_UP:
        menuBottom += 216;
        break;
      case FloatingActionsMenu.EXPAND_DOWN:
        menuTop += 216;
        break;
      case FloatingActionsMenu.EXPAND_LEFT:
        menuRight += 216;
        break;
      case FloatingActionsMenu.EXPAND_RIGHT:
        menuLeft += 216;
        break;
      case FloatingActionsMenu.EXPAND_HORIZONTAL:

        break;
      case FloatingActionsMenu.EXPAND_ROUND:
      case FloatingActionsMenu.EXPAND_FAN:

        break;
    }


  }

  private void setChildrenLayout(View child, int left, int top, int right, int bottom) {
    int childWidth, childHeight, childLeft, childTop;
    final int maxWidth = right - left;
    final int maxHeight = bottom - top;

    childLeft = left;
    childTop = top;
    /*
    switch (expandDirection) {
      case FloatingActionsMenu.EXPAND_UP:
        childTop = menuCenter.y
            - child.getMeasuredHeight();

        childLeft = menuCenter.x - child.getMeasuredWidth() / 2;
        break;
      case FloatingActionsMenu.EXPAND_DOWN:
        childTop = menuCenter.y;
        childLeft = menuCenter.x - child.getMeasuredWidth() / 2;
        break;
      case FloatingActionsMenu.EXPAND_LEFT:
        childLeft = menuCenter.x
            - child.getMeasuredWidth();

        childTop = menuCenter.y;
        break;
      case FloatingActionsMenu.EXPAND_RIGHT:
        childLeft = menuCenter.x;
        childTop = menuCenter.y;
        break;
      case FloatingActionsMenu.EXPAND_HORIZONTAL:
        childLeft = menuCenter.x - child.getMeasuredWidth() / 2;
        childTop = menuCenter.y - child.getMeasuredWidth() / 2;
        break;
      case FloatingActionsMenu.EXPAND_ROUND:
      case FloatingActionsMenu.EXPAND_FAN:
        childLeft = menuCenter.x - child.getMeasuredWidth() / 2;
        childTop = menuCenter.y - child.getMeasuredHeight() / 2;
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
      //TO-DO: show floating action buttons with animation
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
      //TO-DO: hide floating action buttons with animation
      for (FloatingActionButton floatingActionButton : floatingActionButtonItems) {
      }

      if (submenuUpdateListener != null) {
        submenuUpdateListener.onMenuCollapsed();
      }
    }
  }
  //endregion

  @Override public void setVisibility(int visibility) {
    if(visibility == VISIBLE) {
      isVisible = true;
    }
    else {
      isVisible = false;
    }
    super.setVisibility(visibility);
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
