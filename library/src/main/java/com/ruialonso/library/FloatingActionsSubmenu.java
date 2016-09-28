package com.ruialonso.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rui.alonso on 27/9/16.
 */

public class FloatingActionsSubmenu extends ViewGroup {
  private boolean isVisible = false;

  private List<FloatingActionButton> floatingActionButtonItems;

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

    initActionButtons();
  }

  private void loadAttributes(AttributeSet attrs, int defStyle) {
    /*
    final TypedArray a =
        getContext().obtainStyledAttributes(attrs, R.styleable.FloatingActionsSubmenu, defStyle, 0);

    try {
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

  private void initActionButtons() {
    floatingActionButtonItems = new ArrayList<>();
  }

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    int size = getChildCount();
    for (int i = 0; i < size; i++) {
      Object nextChild = getChildAt(i);
      if (nextChild instanceof FloatingActionButton) {
        floatingActionButtonItems.add((FloatingActionButton) nextChild);
      }
    }
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  public void show() {
    if (!isVisible) {
      isVisible = true;

      setVisibility(VISIBLE);
      //TO-DO: show floating action buttons
      for (FloatingActionButton floatingActionButton : floatingActionButtonItems) {
        floatingActionButton.setVisibility(VISIBLE);
      }
    }
  }

  public void hide() {
    if (isVisible) {
      isVisible = false;

      setVisibility(GONE);
      //TO-DO: hide floating action buttons
      for (FloatingActionButton floatingActionButton : floatingActionButtonItems) {
        floatingActionButton.setVisibility(GONE);
      }
    }
  }
}
