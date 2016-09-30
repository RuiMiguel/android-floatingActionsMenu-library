package com.ruialonso.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rui.alonso on 27/9/16.
 */

public class FloatingActionsSubmenu extends ViewGroup {
  private boolean isVisible = false;

  private int expandDirection;
  private int buttonSpacing;

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

  private void loadAttributes(AttributeSet attrs, int defStyle) {
    /*
    final TypedArray attr =
        getContext().obtainStyledAttributes(attrs, R.styleable.FloatingActionsSubmenu, defStyle, 0);

    try {
      expandDirection = attr.getInt(R.styleable.FloatingActionsMenu_fab_expandDirection, FloatingActionsMenu.EXPAND_UP);
      buttonSpacing = (int) (getResources().getDimension(R.dimen.fab_actions_spacing)
          - getResources().getDimension(R.dimen.fab_shadow_radius)
          - getResources().getDimension(R.dimen.fab_shadow_offset));


      gridColumns = a.getInteger(R.styleable.MultipleGridRecyclerView_columns, DEFAULT_COLUMNS);
      cellAspectRatio =
          a.getFloat(R.styleable.MultipleGridRecyclerView_aspect_ratio, DEFAULT_ASPECT_RATIO);
      loadingResourceId = a.getResourceId(R.styleable.MultipleGridRecyclerView_loading_view_layout,
          DEFAULT_LOADING_VIEW_RESOURCE);
      emptyResourceId = a.getResourceId(R.styleable.MultipleGridRecyclerView_empty_view_layout,
          DEFAULT_EMPTY_VIEW_RESOURCE);
    } finally {
      a.recycle();
    }
    */
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

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int count = getChildCount();
    // Measurement will ultimately be computing these values.
    int maxHeight = 0;
    int maxWidth = 0;
    int width = 0;
    int height = 0;

    for (int i = 0; i < count; i++) {
      final View child = getChildAt(i);

      if (child.getVisibility() == GONE) return;

      measureChild(child, widthMeasureSpec, heightMeasureSpec);

      width = child.getMeasuredWidth();
      height = child.getMeasuredHeight();

      /*
      switch (expandDirection) {
        case FloatingActionsMenu.EXPAND_UP:
        case FloatingActionsMenu.EXPAND_DOWN:
          maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
          height += child.getMeasuredHeight();
          height += buttonSpacing * (getChildCount() - 1);
          height = adjustForOvershoot(height);
          break;
        case FloatingActionsMenu.EXPAND_LEFT:
        case FloatingActionsMenu.EXPAND_RIGHT:
          maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
          width += child.getMeasuredWidth();
          width += buttonSpacing * (getChildCount() - 1);
          width = adjustForOvershoot(width);
          break;
        case FloatingActionsMenu.EXPAND_HORIZONTAL:
          maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
          width += child.getMeasuredWidth();
          width += buttonSpacing * (getChildCount() - 1);
          width = adjustForOvershoot(width);
          break;
        case FloatingActionsMenu.EXPAND_ROUND:
        case FloatingActionsMenu.EXPAND_FAN:
          maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
          maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
          height += child.getMeasuredHeight();
          width += child.getMeasuredWidth();
          height += buttonSpacing * (getChildCount() - 1);
          height = adjustForOvershoot(height);
          width += buttonSpacing * (getChildCount() - 1);
          width = adjustForOvershoot(width);
          break;
      }
      */

      maxWidth += Math.max(maxWidth, child.getMeasuredWidth());
      maxHeight += Math.max(maxHeight, child.getMeasuredHeight());
    }

    // Report our final dimensions.
    setMeasuredDimension(maxWidth, maxHeight);
  }

  private int adjustForOvershoot(int dimension) {
    return dimension * 12 / 10;
  }

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    /*
    final int count = getChildCount();
    int curWidth, curHeight, curLeft, curTop, maxHeight;

    //get the available size of child view
    final int childLeft = this.getPaddingLeft();
    final int childTop = this.getPaddingTop();
    final int childRight = this.getMeasuredWidth() - this.getPaddingRight();
    final int childBottom = this.getMeasuredHeight() - this.getPaddingBottom();
    final int childWidth = childRight - childLeft;
    final int childHeight = childBottom - childTop;

    maxHeight = 0;
    curLeft = childLeft;
    curTop = childTop;

    for (int i = 0; i < count; i++) {
      View child = getChildAt(i);
      if (child.getVisibility() == GONE) return;

      switch (expandDirection) {
        case FloatingActionsMenu.EXPAND_UP:
          break;
        case FloatingActionsMenu.EXPAND_DOWN:
          break;
        case FloatingActionsMenu.EXPAND_LEFT:
          break;
        case FloatingActionsMenu.EXPAND_RIGHT:
          break;
        case FloatingActionsMenu.EXPAND_HORIZONTAL:
          break;
        case FloatingActionsMenu.EXPAND_ROUND:
          break;
        case FloatingActionsMenu.EXPAND_FAN:
          break;
        default:
          break;
      }

      //Get the maximum size of the child
      child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST));
      curWidth = child.getMeasuredWidth();
      curHeight = child.getMeasuredHeight();


      //do the layout
      child.layout(curLeft, curTop, curLeft + curWidth, curTop + curHeight);
    }
    */
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

  public void setOnFloatingActionSubmenuUpdateListener(
      OnFloatingActionSubmenuUpdateListener listener) {
    this.submenuUpdateListener = listener;
  }

  public interface OnFloatingActionSubmenuUpdateListener {
    void onMenuExpanded();

    void onMenuCollapsed();
  }
}
