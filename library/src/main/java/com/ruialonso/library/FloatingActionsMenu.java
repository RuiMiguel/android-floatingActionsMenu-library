package com.ruialonso.library;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;
import com.ruialonso.library.animation.RotationDrawable;
import java.util.ArrayList;
import java.util.List;

public class FloatingActionsMenu extends ViewGroup {
  public static final int EXPAND_UP = 0;
  public static final int EXPAND_DOWN = 1;
  public static final int EXPAND_LEFT = 2;
  public static final int EXPAND_RIGHT = 3;
  public static final int EXPAND_HORIZONTAL = 4;
  public static final int EXPAND_ROUND = 5;
  public static final int EXPAND_FAN = 6;

  private static final int ALIGNMENT_CENTER = 0;
  private static final int ALIGNMENT_LEFT = 1;
  private static final int ALIGNMENT_RIGHT = 2;
  private static final int ALIGNMENT_TOP = 1;
  private static final int ALIGNMENT_BOTTOM = 2;


  private static final int ANIMATION_DURATION = 300;
  private static final float FLIP_PLUS_ROTATION = 180f;

  private RotationDrawable rotationDrawable;
  private AnimatorSet flipAnimation;

  private FloatingActionMenuButton floatingActionMenuButton;

  private int expandDirection;
  private int verticalAlignment;
  private int horizontalAlignment;

  private int mAddButtonPlusColor;
  private int mAddButtonColorNormal;
  private int mAddButtonColorPressed;
  private int mAddButtonSize;
  private boolean mAddButtonStrokeVisible;
  @DrawableRes private int mIconASV;
  private Drawable mIconDrawableASV;

  private int currentSubmenuIndex = -1;
  private List<FloatingActionsSubmenu> floatingActionsSubmenuList;

  private TouchDelegateGroup mTouchDelegateGroup;
  private OnToggleListener onToggleListener;

  public FloatingActionsMenu(Context context) {
    super(context);
    init(null, 0);
  }

  public FloatingActionsMenu(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs, 0);
  }

  public FloatingActionsMenu(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(attrs, defStyle);
  }

  private void init(AttributeSet attributeSet, int defStyle) {
    loadAttributes(attributeSet, defStyle);

  }

  private void loadAttributes(AttributeSet attributeSet, int defStyle) {
    TypedArray attr =
        getContext().obtainStyledAttributes(attributeSet, R.styleable.FloatingActionsMenu, 0, 0);

    expandDirection = attr.getInt(R.styleable.FloatingActionsMenu_fab_expandDirection, EXPAND_UP);
    verticalAlignment = attr.getInt(R.styleable.FloatingActionsMenu_fab_vertical_alignment, ALIGNMENT_CENTER);
    horizontalAlignment = attr.getInt(R.styleable.FloatingActionsMenu_fab_horizontal_alignment, ALIGNMENT_CENTER);



    mTouchDelegateGroup = new TouchDelegateGroup(this);
    setTouchDelegate(mTouchDelegateGroup);

    mAddButtonPlusColor = attr.getColor(R.styleable.FloatingActionsMenu_fab_addButtonPlusIconColor,
        getResources().getColor(android.R.color.white));
    mAddButtonColorNormal = attr.getColor(R.styleable.FloatingActionsMenu_fab_addButtonColorNormal,
        getResources().getColor(android.R.color.holo_blue_dark));
    mAddButtonColorPressed =
        attr.getColor(R.styleable.FloatingActionsMenu_fab_addButtonColorPressed,
            getResources().getColor(android.R.color.holo_blue_light));
    mAddButtonSize = attr.getInt(R.styleable.FloatingActionsMenu_fab_addButtonSize,
        FloatingActionButton.SIZE_NORMAL);
    mAddButtonStrokeVisible =
        attr.getBoolean(R.styleable.FloatingActionsMenu_fab_addButtonStrokeVisible, true);

    TypedArray attr2 =
        getContext().obtainStyledAttributes(attributeSet, R.styleable.FloatingActionButton, 0, 0);
    mIconASV = attr2.getResourceId(R.styleable.FloatingActionButton_ggg_icono, 0);


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      mIconDrawableASV = getContext().getDrawable(mIconASV);
    } else {
      mIconDrawableASV = getContext().getResources().getDrawable(mIconASV);
    }

    attr.recycle();
  }

  private void createFloatingActionMenuButton() {
    floatingActionMenuButton = new FloatingActionMenuButton(getContext()) {
      @Override void updateBackground() {
        mPlusColor = mAddButtonPlusColor;
        mColorNormal = mAddButtonColorNormal;
        mColorPressed = mAddButtonColorPressed;
        mStrokeVisible = mAddButtonStrokeVisible;

        super.updateBackground();
      }

      @Override Drawable getIconDrawable() {

        // final RotatingDrawable rotatingDrawable = new RotatingDrawable(super.getIconDrawable());
        final RotationDrawable rotatingDrawable = new RotationDrawable(mIconDrawableASV);
        rotationDrawable = rotatingDrawable;

        final OvershootInterpolator interpolator = new OvershootInterpolator();

        final ObjectAnimator objectAnimator =
            ObjectAnimator.ofFloat(rotatingDrawable, "rotation", 0, FLIP_PLUS_ROTATION);

        objectAnimator.setInterpolator(interpolator);

        flipAnimation.play(objectAnimator);
        return rotatingDrawable;
      }
    };

    floatingActionMenuButton.setId(R.id.fab_expand_menu_button);
    floatingActionMenuButton.setSize(mAddButtonSize);
    floatingActionMenuButton.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        toggle();
      }
    });

    addView(floatingActionMenuButton, super.generateDefaultLayoutParams());
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();

    floatingActionsSubmenuList = new ArrayList<>();
    List<FloatingActionButton> floatingActionButtonList = new ArrayList<>();

    for (int i = 0; i < getChildCount(); i++) {
      Object child = getChildAt(i);
      if (child instanceof FloatingActionsSubmenu) {
        floatingActionsSubmenuList.add((FloatingActionsSubmenu) child);
      }
      if (child instanceof FloatingActionButton) {
        floatingActionButtonList.add((FloatingActionButton) child);
      }
    }

    addDefaultSubmenuIfNeeded(floatingActionButtonList);

    initViews();
  }

  private void addDefaultSubmenuIfNeeded(List<FloatingActionButton> floatingActionButtonList) {
    if (floatingActionButtonList.size() > 0 && floatingActionsSubmenuList.size() == 0) {
      FloatingActionsSubmenu defaultActionSubmenu = new FloatingActionsSubmenu(getContext());
      for(int i=0; i<floatingActionButtonList.size(); i++)
        removeView(floatingActionButtonList.get(i));

      defaultActionSubmenu.addButtons(floatingActionButtonList);
      addSubmenu(defaultActionSubmenu);
    }
  }

  private void initViews() {
    flipAnimation = new AnimatorSet().setDuration(ANIMATION_DURATION);

    createFloatingActionMenuButton();
    bringChildToFront(floatingActionMenuButton);

    initSubmenus();
  }

  private void initSubmenus() {
    for (FloatingActionsSubmenu floatingActionsSubmenu: floatingActionsSubmenuList) {
      floatingActionsSubmenu.setVisibility(INVISIBLE); //TO-DO: initialize visibility from attrs
    }

    if (floatingActionsSubmenuList.size() > 1) {
      currentSubmenuIndex = 0;
      floatingActionsSubmenuList.get(0).setVisibility(VISIBLE);
    }
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int count = getChildCount();
    // Measurement will ultimately be computing these values.
    int maxHeight = 0;
    int maxWidth = 0;

    // Iterate through all children, measuring them and computing our dimensions
    // from their size.
    for (int i = 0; i < count; i++) {
      final View child = getChildAt(i);
      if (child.getVisibility() == GONE) return;

      measureChild(child, widthMeasureSpec, heightMeasureSpec);

      switch (expandDirection) {
        case EXPAND_UP:
        case EXPAND_DOWN:
          maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
          break;
        case EXPAND_LEFT:
        case EXPAND_RIGHT:
          maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
          break;
        case EXPAND_HORIZONTAL:
          maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
          break;
        case EXPAND_ROUND:
        case EXPAND_FAN:
          maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
          maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
          break;
      }

      maxWidth += Math.max(maxWidth, child.getMeasuredWidth());
      maxHeight += Math.max(maxHeight, child.getMeasuredHeight());
    }

    // Report our final dimensions.
    setMeasuredDimension(maxWidth, maxHeight);
  }

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    final int count = getChildCount();

    //get the available size of child view
    final int menuLeft = this.getPaddingLeft();
    final int menuTop = this.getPaddingTop();
    final int menuRight = this.getMeasuredWidth() - this.getPaddingRight();
    final int menuBottom = this.getMeasuredHeight() - this.getPaddingBottom();

    setRootLayout();

    for (int i = 0; i < count; i++) {
      View child = getChildAt(i);

      setChildrenLayout(child, menuLeft, menuTop, menuRight, menuBottom);
    }
  }

  private void setRootLayout() {
    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)getLayoutParams();

    switch (verticalAlignment) {
      case ALIGNMENT_CENTER:
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        break;
      case ALIGNMENT_TOP:
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        break;
      case ALIGNMENT_BOTTOM:
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        break;
    }
    switch (horizontalAlignment) {
      case ALIGNMENT_CENTER:
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        break;
      case ALIGNMENT_LEFT:
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        break;
      case ALIGNMENT_RIGHT:
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        break;
    }

    setLayoutParams(layoutParams);
  }

  private void setChildrenLayout(View child, int left, int top, int right, int bottom) {
    int childWidth, childHeight, childLeft, childTop;
    final int maxWidth = right - left;
    final int maxHeight = bottom - top;

    Point menuCenter = new Point();
    menuCenter.x = maxWidth / 2;
    menuCenter.y = maxHeight / 2;

    if (child.getVisibility() == GONE) return;

    childLeft = left;
    childTop = top;

    switch (verticalAlignment) {
      case ALIGNMENT_CENTER:
        childTop = menuCenter.y - child.getMeasuredHeight() / 2;
        break;
      case ALIGNMENT_TOP:
        childTop = top;
        break;
      case ALIGNMENT_BOTTOM:
        childTop = bottom - child.getMeasuredHeight();
        break;
    }
    switch (horizontalAlignment) {
      case ALIGNMENT_CENTER:
        childLeft = menuCenter.x - child.getMeasuredWidth() / 2;
        break;
      case ALIGNMENT_LEFT:
        childLeft = left;
        break;
      case ALIGNMENT_RIGHT:
        childLeft = right - child.getMeasuredWidth();
        break;
    }

    child.layout(childLeft, childTop, childLeft + child.getMeasuredWidth(),
        childTop + child.getMeasuredHeight());

    switch (expandDirection) {
      case EXPAND_UP:

        break;
      case EXPAND_DOWN:

        break;
      case EXPAND_LEFT:
        break;
      case EXPAND_RIGHT:
        break;
      case EXPAND_HORIZONTAL:
        break;
      case EXPAND_ROUND:
        break;
      case EXPAND_FAN:
        break;
    }

    //Get the maximum size of the child
    child.measure(MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.AT_MOST),
        MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST));
    childWidth = child.getMeasuredWidth();
    childHeight = child.getMeasuredHeight();

    //do the layout
    child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
  }

  //region Add/Remove submenus
  public boolean addSubmenu(FloatingActionsSubmenu floatingActionsSubmenu) {
    boolean added;
    if (this.floatingActionsSubmenuList == null) {
      this.floatingActionsSubmenuList = new ArrayList<>();
    }
    added = floatingActionsSubmenuList.add(floatingActionsSubmenu);
    if (added) addView(floatingActionsSubmenu);
    return added;
  }

  public void addSubmenus(List<FloatingActionsSubmenu> floatingActionsSubmenuItems) {
    for (FloatingActionsSubmenu floatingActionsSubmenu : floatingActionsSubmenuItems) {
      addSubmenu(floatingActionsSubmenu);
    }
  }

  public boolean removeSubmenu(FloatingActionsSubmenu floatingActionsSubmenu) {
    boolean removed = false;
    if (this.floatingActionsSubmenuList != null) {
      removed = this.floatingActionsSubmenuList.remove(floatingActionsSubmenu);
      if (removed) removeView(floatingActionsSubmenu);
    }
    return removed;
  }
  //endregion

  //region Toggle submenus
  public void toggle() {
    if (isAloneSubmenu()) {
      toggleAloneSubmenu();
    } else {
      toggleMultipleSubmenu();
    }
  }

  private boolean isAloneSubmenu() {
    return floatingActionsSubmenuList.size() == 1;
  }

  private void toggleAloneSubmenu() {
    if (isAloneSubmenuVisible()) {
      floatingActionsSubmenuList.get(currentSubmenuIndex).collapse();
      currentSubmenuIndex = -1;
      notifyMenuOverlayVisibility(false);
    } else {
      currentSubmenuIndex = 0;
      floatingActionsSubmenuList.get(currentSubmenuIndex).expand();
      notifyMenuOverlayVisibility(true);
    }
  }

  private boolean isAloneSubmenuVisible() {
    return isAloneSubmenu() && (currentSubmenuIndex == 0);
  }

  private void toggleMultipleSubmenu() {
    floatingActionsSubmenuList.get(currentSubmenuIndex).collapse();

    if (currentSubmenuIndex < (floatingActionsSubmenuList.size() - 1)) {
      currentSubmenuIndex++;
      notifyMenuOverlayVisibility(true); //TO-DO: notify if submenu want to
    } else {
      currentSubmenuIndex = 0;
      notifyMenuOverlayVisibility(false); //TO-DO: notify if submenu want to
    }

    floatingActionsSubmenuList.get(currentSubmenuIndex).expand();
  }

  private void notifyMenuOverlayVisibility(boolean menuOverlayVisibility) {
    //mAddButton.playAnimation();
    if (onToggleListener != null) onToggleListener.onToggle(menuOverlayVisibility);
  }

  public void setOnToggleListener(OnToggleListener listener) {
    this.onToggleListener = listener;
  }

  public interface OnToggleListener {
    void onToggle(boolean visible);
  }
  //endregion
}
