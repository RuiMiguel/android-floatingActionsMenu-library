package com.ruialonso.floatingactionmenu;

import android.animation.Animator;
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
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FloatingActionsMenu extends RelativeLayout {
  public static final int ALIGNMENT_CENTER = 0;
  public static final int ALIGNMENT_LEFT = 1;
  public static final int ALIGNMENT_RIGHT = 2;
  public static final int ALIGNMENT_TOP = 1;
  public static final int ALIGNMENT_BOTTOM = 2;

  public static final int ANIMATION_DURATION = 100;
  public Point menuButtonCenter;
  public FloatingActionMenuButton floatingActionMenuButton;
  public int verticalAlignment;
  public int horizontalAlignment;
  private AnimatorSet animatorSet;
  private int menuButtonColorNormal;
  private int menuButtonColorPressed;
  private boolean mAddButtonStrokeVisible;

  private int menuButtonSize;
  @DrawableRes private int menuIconRes;
  private Drawable menuIcon;

  private Map<String, GroupSubmenu> floatingActionsGroupSubmenuMap;
  private int currentGroupSubmenuIndex = -1;
  private List<GroupSubmenu> floatingActionsGroupSubmenuList;

  private OnToggleListener onToggleListener;

  //region constructor
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
  //endregion

  private void init(AttributeSet attributeSet, int defStyle) {
    loadAttributes(attributeSet, defStyle);
  }

  private void loadAttributes(AttributeSet attributeSet, int defStyle) {
    TypedArray attrMenu =
        getContext().obtainStyledAttributes(attributeSet, R.styleable.FloatingActionsMenu, 0, 0);

    verticalAlignment =
        attrMenu.getInt(R.styleable.FloatingActionsMenu_fab_vertical_alignment, ALIGNMENT_CENTER);
    horizontalAlignment =
        attrMenu.getInt(R.styleable.FloatingActionsMenu_fab_horizontal_alignment, ALIGNMENT_CENTER);

    menuButtonSize = attrMenu.getInt(R.styleable.FloatingActionsMenu_fab_menu_button_size,
        FloatingActionButton.SIZE_NORMAL);

    menuButtonColorNormal =
        attrMenu.getColor(R.styleable.FloatingActionsMenu_fab_bg_menu_button_color_normal,
            getResources().getColor(android.R.color.transparent));
    menuButtonColorPressed =
        attrMenu.getColor(R.styleable.FloatingActionsMenu_fab_bg_menu_button_color_pressed,
            getResources().getColor(android.R.color.transparent));

    mAddButtonStrokeVisible =
        attrMenu.getBoolean(R.styleable.FloatingActionsMenu_fab_addButtonStrokeVisible, true);

    menuIconRes = attrMenu.getResourceId(R.styleable.FloatingActionsMenu_fab_menu_icon, 0);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      menuIcon = getContext().getDrawable(menuIconRes);
    } else {
      menuIcon = getContext().getResources().getDrawable(menuIconRes);
    }

    attrMenu.recycle();
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();

    floatingActionsGroupSubmenuList = new ArrayList<>();
    floatingActionsGroupSubmenuMap = new LinkedHashMap<>();
    List<FloatingActionButton> floatingActionButtonList = new ArrayList<>();

    for (int i = 0; i < getChildCount(); i++) {
      Object child = getChildAt(i);

      if (child instanceof FloatingActionsSubmenu) {
        String groupName = ((FloatingActionsSubmenu) child).getSubmenuGroup();
        if (groupName == null) {
          groupName = Integer.toString(((FloatingActionsSubmenu) child).getId());
        }

        GroupSubmenu groupSubmenu = floatingActionsGroupSubmenuMap.get(groupName);
        if (groupSubmenu == null) {
          groupSubmenu = new GroupSubmenu();
          groupSubmenu.setGroupName(groupName);
        }
        groupSubmenu.add((FloatingActionsSubmenu) child);
        floatingActionsGroupSubmenuMap.put(groupName, groupSubmenu);
      }

      if (child instanceof FloatingActionButton) {
        floatingActionButtonList.add((FloatingActionButton) child);
      }
    }

    addDefaultSubmenuIfNeeded(floatingActionButtonList);

    initViews();
  }

  private void addDefaultSubmenuIfNeeded(List<FloatingActionButton> floatingActionButtonList) {
    if (floatingActionButtonList.size() > 0
        && floatingActionsGroupSubmenuMap.values().size() == 0) {
      FloatingActionsSubmenu defaultActionSubmenu = new FloatingActionsSubmenu(getContext());
      for (int i = 0; i < floatingActionButtonList.size(); i++)
        removeView(floatingActionButtonList.get(i));

      defaultActionSubmenu.addButtons(floatingActionButtonList);
      addSubmenu(defaultActionSubmenu);
    }
  }

  private void initViews() {
    createFloatingActionMenuButton();
    bringChildToFront(floatingActionMenuButton);

    initSubmenus();

    setMenuButtonIcon();
  }

  private void createFloatingActionMenuButton() {
    floatingActionMenuButton = new FloatingActionMenuButton(getContext()) {
      @Override void updateBackground() {

        bgColorNormal = menuButtonColorNormal;
        bgColorNormal = menuButtonColorPressed;
        mStrokeVisible = mAddButtonStrokeVisible;

        super.updateBackground();
      }
    };

    floatingActionMenuButton.setId(R.id.fab_menu_button);
    floatingActionMenuButton.setSize(menuButtonSize);

    setMenuButtonFlipIconAnimator(menuIcon);
    floatingActionMenuButton.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        toggle();
      }
    });

    addView(floatingActionMenuButton, super.generateDefaultLayoutParams());
  }

  //region animator
  private void setMenuButtonFlipIconAnimator(final Drawable icon) {
    ObjectAnimator flipAnim =
        ObjectAnimator.ofFloat(floatingActionMenuButton, View.ROTATION_Y, -90);
    flipAnim.setDuration(ANIMATION_DURATION);
    flipAnim.setInterpolator(new LinearInterpolator());

    flipAnim.addListener(new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animator) {

      }

      @Override public void onAnimationEnd(Animator animator) {
        floatingActionMenuButton.setIconDrawable(icon);
      }

      @Override public void onAnimationCancel(Animator animator) {

      }

      @Override public void onAnimationRepeat(Animator animator) {

      }
    });

    ObjectAnimator flipAnimReturn =
        ObjectAnimator.ofFloat(floatingActionMenuButton, View.ROTATION_Y, 0);
    flipAnimReturn.setDuration(ANIMATION_DURATION);
    flipAnimReturn.setInterpolator(new LinearInterpolator());

    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.play(flipAnim).before(flipAnimReturn);
    setMenuButtonAnimator(animatorSet);
  }

  public void setMenuButtonAnimator(AnimatorSet animatorSet) {
    this.animatorSet = animatorSet;
    floatingActionMenuButton.setAnimatorSet(this.animatorSet);
  }
  //endregion

  private void initSubmenus() {
    resetGroupSubmenuList(floatingActionsGroupSubmenuMap.values());

    for (GroupSubmenu groupSubmenu : floatingActionsGroupSubmenuList) {
      groupSubmenu.collapse();
    }

    if (!isAloneSubmenu()) {
      currentGroupSubmenuIndex = 0;
      floatingActionsGroupSubmenuList.get(currentGroupSubmenuIndex).expand();
    }
  }

  private void setMenuButtonIcon() {
    if(menuIcon != null) {
      floatingActionMenuButton.setIconDrawable(menuIcon);
    }
    else {
      boolean iconFounded = false;
      for(int i = 0; !iconFounded && i<floatingActionsGroupSubmenuList.size(); i++) {
        Drawable icon = floatingActionsGroupSubmenuList.get(i).getSubmenuIcon();
        if(icon != null) {
          floatingActionMenuButton.setIconDrawable(icon);
          iconFounded = true;
        }
      }

      if(!iconFounded) {
        throw new IllegalArgumentException("Menu icon or Submenu icon cannot be empty");
      }
    }
  }

  //region measure
  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int count = getChildCount();
    // Measurement will ultimately be computing these values.
    int maxHeight = 0;
    int maxWidth = 0;
    boolean needsAdjustMenuHeight = false;
    boolean needsAdjustMenuWidth = false;

    // Iterate through all children, measuring them and computing our dimensions
    // from their size.
    for (int i = 0; i < count; i++) {
      final View child = getChildAt(i);

      if (child.getVisibility() == GONE) continue;

      child.measure(widthMeasureSpec, heightMeasureSpec);

      int childHeight = child.getMeasuredHeight();
      int childWidth = child.getMeasuredWidth();

      if (child instanceof FloatingActionsSubmenu) {
        int expandDirection = ((FloatingActionsSubmenu) child).getExpandDirection();

        if (verticalAlignment == ALIGNMENT_CENTER) {
          if (expandDirection == FloatingActionsSubmenu.EXPAND_UP
              || expandDirection == FloatingActionsSubmenu.EXPAND_DOWN) {
            childHeight *= 2;
            //TODO: not to duplicate height, just align menuButton to correct position, not only center in onRootLayout and onMenuButtonLayout
          }
        }
        if (horizontalAlignment == ALIGNMENT_CENTER) {
          if (expandDirection == FloatingActionsSubmenu.EXPAND_LEFT
              || expandDirection == FloatingActionsSubmenu.EXPAND_RIGHT) {
            childWidth *= 2;
            //TODO: not to duplicate width, just align menuButton to correct position, not only center in onRootLayout and onMenuButtonLayout
          }
        }

        if (expandDirection == FloatingActionsSubmenu.EXPAND_UP
            || expandDirection == FloatingActionsSubmenu.EXPAND_DOWN) {
          needsAdjustMenuHeight = true;
        }
        if (expandDirection == FloatingActionsSubmenu.EXPAND_LEFT
            || expandDirection == FloatingActionsSubmenu.EXPAND_RIGHT) {
          needsAdjustMenuWidth = true;
        }
      }

      maxHeight = Math.max(maxHeight, childHeight);
      maxWidth = Math.max(maxWidth, childWidth);
    }

    if (needsAdjustMenuWidth) {
      maxWidth += floatingActionMenuButton.getMeasuredWidth();
    }
    if (needsAdjustMenuHeight) {
      maxHeight += floatingActionMenuButton.getMeasuredHeight();
    }

    // Report our final dimensions.
    setMeasuredDimension(maxWidth, maxHeight);
  }
  //endregion

  //region layout
  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    final int count = getChildCount();

    //get the available size of child view
    final int menuLeft = this.getPaddingLeft();
    final int menuTop = this.getPaddingTop();
    final int menuRight = this.getMeasuredWidth() - this.getPaddingRight();
    final int menuBottom = this.getMeasuredHeight() - this.getPaddingBottom();

    setRootLayout(menuLeft, menuTop, menuRight, menuBottom);

    for (int i = 0; i < count; i++) {
      View child = getChildAt(i);
      if (child.getVisibility() != GONE) {
        if (child == floatingActionMenuButton) {
          setMenuButtonLayout(child, menuRight - menuLeft, menuBottom - menuTop);
        } else {
          setChildrenLayout(child, menuLeft, menuTop, menuRight, menuBottom);
        }
      }
    }
  }

  private void setRootLayout(int left, int top, int right, int bottom) {
    final int maxWidth = right - left;
    final int maxHeight = bottom - top;

    menuButtonCenter = new Point();

    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();

    switch (verticalAlignment) {
      case ALIGNMENT_CENTER:
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        menuButtonCenter.y = maxHeight / 2;
        //TODO: align menuButton to correct position
        break;
      case ALIGNMENT_TOP:
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        menuButtonCenter.y = top + floatingActionMenuButton.getMeasuredHeight() / 2;
        break;
      case ALIGNMENT_BOTTOM:
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        menuButtonCenter.y = bottom - floatingActionMenuButton.getMeasuredHeight() / 2;
        break;
    }
    switch (horizontalAlignment) {
      case ALIGNMENT_CENTER:
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        menuButtonCenter.x = maxWidth / 2;
        //TODO: align menuButton to correct position
        break;
      case ALIGNMENT_LEFT:
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        menuButtonCenter.x = left + floatingActionMenuButton.getMeasuredWidth() / 2;
        break;
      case ALIGNMENT_RIGHT:
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        menuButtonCenter.x = right - floatingActionMenuButton.getMeasuredWidth() / 2;
        break;
    }

    setLayoutParams(layoutParams);
  }

  private void setMenuButtonLayout(View child, int maxWidth, int maxHeight) {
    int menuButtonWidth, menuButtonHeight, menuButtonLeft, menuButtonTop;

    menuButtonLeft = menuButtonCenter.x - floatingActionMenuButton.getMeasuredWidth() / 2;
    menuButtonTop = menuButtonCenter.y - floatingActionMenuButton.getMeasuredHeight() / 2;

    //Get the maximum size of the child
    child.measure(MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.AT_MOST),
        MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST));
    menuButtonWidth = child.getMeasuredWidth();
    menuButtonHeight = child.getMeasuredHeight();

    //do the layout
    child.layout(menuButtonLeft, menuButtonTop, menuButtonLeft + menuButtonWidth,
        menuButtonTop + menuButtonHeight);
  }

  private void setChildrenLayout(View child, int left, int top, int right, int bottom) {
    int childWidth, childHeight, childLeft, childTop;
    final int maxWidth = right - left;
    final int maxHeight = bottom - top;

    childLeft = left;
    childTop = top;

    int expandDirection = ((FloatingActionsSubmenu) child).getExpandDirection();
    int childMeasuredHeight = child.getMeasuredHeight();
    int childMeasuredWidth = child.getMeasuredWidth();
    int menuButtonMeasuredHeight = floatingActionMenuButton.getMeasuredHeight();
    int menuButtonMeasuredWidth = floatingActionMenuButton.getMeasuredWidth();

    switch (verticalAlignment) {
      case ALIGNMENT_CENTER:
        childTop = menuButtonCenter.y - (childMeasuredHeight / 2);
        if (expandDirection == FloatingActionsSubmenu.EXPAND_DOWN) {
          childTop = menuButtonCenter.y + (menuButtonMeasuredHeight / 2);
        }
        if (expandDirection == FloatingActionsSubmenu.EXPAND_UP) {
          childTop = menuButtonCenter.y - (menuButtonMeasuredHeight / 2) - childMeasuredHeight;
        }

        break;
      case ALIGNMENT_TOP:
        if (expandDirection == FloatingActionsSubmenu.EXPAND_LEFT
            || expandDirection == FloatingActionsSubmenu.EXPAND_RIGHT) {
          childTop = menuButtonCenter.y - (childMeasuredHeight / 2);
        } else {
          childTop = top;
        }

        if (expandDirection == FloatingActionsSubmenu.EXPAND_DOWN) {
          childTop += menuButtonMeasuredHeight;
        }
        break;
      case ALIGNMENT_BOTTOM:
        if (expandDirection == FloatingActionsSubmenu.EXPAND_LEFT
            || expandDirection == FloatingActionsSubmenu.EXPAND_RIGHT) {
          childTop = menuButtonCenter.y - (childMeasuredHeight / 2);
        } else {
          childTop = bottom - childMeasuredHeight;
        }

        if (expandDirection == FloatingActionsSubmenu.EXPAND_UP) {
          childTop -= menuButtonMeasuredHeight;
        }
        break;
    }

    switch (horizontalAlignment) {
      case ALIGNMENT_CENTER:
        childLeft = menuButtonCenter.x - (childMeasuredWidth / 2);
        if (expandDirection == FloatingActionsSubmenu.EXPAND_RIGHT) {
          childLeft = menuButtonCenter.x + (menuButtonMeasuredWidth / 2);
        }
        if (expandDirection == FloatingActionsSubmenu.EXPAND_LEFT) {
          childLeft = menuButtonCenter.x - (menuButtonMeasuredWidth / 2) - childMeasuredWidth;
        }
        break;
      case ALIGNMENT_LEFT:
        if (expandDirection == FloatingActionsSubmenu.EXPAND_UP
            || expandDirection == FloatingActionsSubmenu.EXPAND_DOWN) {
          childLeft = menuButtonCenter.x - (childMeasuredWidth / 2);
        } else {
          childLeft = left;
        }

        if (expandDirection == FloatingActionsSubmenu.EXPAND_RIGHT) {
          childLeft += menuButtonMeasuredWidth;
        }
        break;
      case ALIGNMENT_RIGHT:
        if (expandDirection == FloatingActionsSubmenu.EXPAND_UP
            || expandDirection == FloatingActionsSubmenu.EXPAND_DOWN) {
          childLeft = menuButtonCenter.x - (childMeasuredWidth / 2);
        } else {
          childLeft = right - childMeasuredWidth;
        }

        if (expandDirection == FloatingActionsSubmenu.EXPAND_LEFT) {
          childLeft -= menuButtonMeasuredWidth;
        }
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
  //endregion

  //region Add/Remove submenus
  private void resetGroupSubmenuList(Collection<GroupSubmenu> values) {
    floatingActionsGroupSubmenuList = new ArrayList<>();
    floatingActionsGroupSubmenuList.addAll(values);
  }

  public void addSubmenu(FloatingActionsSubmenu floatingActionsSubmenu) {
    String groupName = floatingActionsSubmenu.getSubmenuGroup();
    GroupSubmenu groupSubmenu = floatingActionsGroupSubmenuMap.get(groupName);
    if (groupSubmenu == null) {
      groupSubmenu = new GroupSubmenu();
      groupSubmenu.setGroupName(groupName);
    }
    groupSubmenu.add(floatingActionsSubmenu);
    floatingActionsGroupSubmenuMap.put(groupName, groupSubmenu);

    resetGroupSubmenuList(floatingActionsGroupSubmenuMap.values());
    addView(floatingActionsSubmenu);
  }

  public void addSubmenus(List<FloatingActionsSubmenu> floatingActionsSubmenuItems) {
    for (FloatingActionsSubmenu floatingActionsSubmenu : floatingActionsSubmenuItems) {
      addSubmenu(floatingActionsSubmenu);
    }
  }

  public boolean removeSubmenu(FloatingActionsSubmenu floatingActionsSubmenu) {
    boolean removed = false;
    String groupName = floatingActionsSubmenu.getSubmenuGroup();
    GroupSubmenu groupSubmenu = floatingActionsGroupSubmenuMap.get(groupName);
    if (groupSubmenu != null) {
      groupSubmenu.remove(floatingActionsSubmenu);
      if (groupSubmenu.isEmpty()) {
        floatingActionsGroupSubmenuMap.remove(groupName);
      } else {
        floatingActionsGroupSubmenuMap.put(groupName, groupSubmenu);
      }
      removed = true;
    }

    if (removed) {
      resetGroupSubmenuList(floatingActionsGroupSubmenuMap.values());
      removeView(floatingActionsSubmenu);
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
    return (floatingActionsGroupSubmenuList.size() == 1) && (floatingActionsGroupSubmenuList.get(0)
        .size() == 1);
  }

  private void toggleAloneSubmenu() {
    if (isAloneSubmenuVisible()) {
      floatingActionsGroupSubmenuList.get(currentGroupSubmenuIndex).collapse();
      currentGroupSubmenuIndex = -1;
      notifyMenuOverlayVisibility(false);
      runMenuCollapseAnimator();
    } else {
      currentGroupSubmenuIndex = 0;
      floatingActionsGroupSubmenuList.get(currentGroupSubmenuIndex).expand();
      notifyMenuOverlayVisibility(true);
      runMenuExpandAnimator();
    }
  }

  private boolean isAloneSubmenuVisible() {
    return isAloneSubmenu() && (currentGroupSubmenuIndex == 0);
  }

  private void toggleMultipleSubmenu() {
    GroupSubmenu lastSubmenu = floatingActionsGroupSubmenuList.get(currentGroupSubmenuIndex);
    lastSubmenu.collapse();

    if (currentGroupSubmenuIndex < (floatingActionsGroupSubmenuList.size() - 1)) {
      currentGroupSubmenuIndex++;
    } else {
      currentGroupSubmenuIndex = 0;
    }
    GroupSubmenu activeSubmenu = floatingActionsGroupSubmenuList.get(currentGroupSubmenuIndex);

    notifyMenuOverlayVisibility(activeSubmenu.isOverlayEnabled());

    activeSubmenu.expand();

    runMenuToogleAnimator();
  }

  private void runMenuExpandAnimator() {
    runMenuToogleAnimator();
  }

  private void runMenuCollapseAnimator() {

  }

  private void runMenuToogleAnimator() {
    if(currentGroupSubmenuIndex != -1) {
      GroupSubmenu activeSubmenu = floatingActionsGroupSubmenuList.get(currentGroupSubmenuIndex);
      activeSubmenu.expand();

      setMenuButtonFlipIconAnimator(activeSubmenu.getSubmenuIcon());
      floatingActionMenuButton.runAnimation();
    }
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
