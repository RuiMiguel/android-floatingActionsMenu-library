package com.ruialonso.floatingactionmenu;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Fab extends RelativeLayout {

  private View rootView;
  private RelativeLayout floatingActionsOverlay;

  private RelativeLayout floatingActionsMenu;
  private RelativeLayout floatingActionsSubmenuHorizontal;
  private FrameLayout floatingActionsSubmenuFan;

  private ImageView floatingActionMenuButton;

  private View floatingActionFakeMenuButtonHorizontal;
  private ImageView floatingActionButtonSearch;
  private ImageView floatingActionButtonNotifications;


  private View floatingActionFakeMenuButtonFan;
  private ImageView floatingActionButtonLight;
  private ImageView floatingActionButtonHeart;
  private ImageView floatingActionButtonThunder;
  private ImageView floatingActionButtonSmile;

  private boolean isMenuHorizontalVisible = true;

  private AnimatorSet menuButtonFlipIconAnimator;
  private AnimatorSet menuButtonFlipBackIconAnimator;

  //region constructor
  public Fab(Context context) {
    super(context);
    init(null, 0);
  }

  public Fab(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs, 0);
  }

  public Fab(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs, defStyleAttr);
  }
  //endregion

  private void init(AttributeSet attrs, int defStyleAttr) {
    loadAttributes(attrs, defStyleAttr);

    initViews();
    bindListeners();

    initAnimators();
  }

  private void loadAttributes(AttributeSet attrs, int defStyle) {

  }

  private void initViews() {
    LayoutInflater inflater =
        (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    rootView = inflater.inflate(R.layout.fab_woah, this, true);

    floatingActionsOverlay = (RelativeLayout) findViewById(R.id.floating_actions_overlay);

    floatingActionsMenu = (RelativeLayout) findViewById(R.id.floating_actions_menu);
    floatingActionsSubmenuHorizontal =
        (RelativeLayout) findViewById(R.id.floating_actions_submenu_horizontal);
    floatingActionsSubmenuFan = (FrameLayout) findViewById(R.id.floating_actions_submenu_fan);

    floatingActionMenuButton =
        (ImageView) findViewById(R.id.floating_action_menu_button);

    floatingActionFakeMenuButtonHorizontal = findViewById(R.id.fake_menu_button_submenu_horizontal);
    floatingActionButtonSearch = (ImageView) findViewById(R.id.floating_action_button_search);
    floatingActionButtonNotifications =
        (ImageView) findViewById(R.id.floating_action_button_notifications);

    floatingActionFakeMenuButtonFan = findViewById(R.id.fake_menu_button_submenu_fan);
    floatingActionButtonLight = (ImageView) findViewById(R.id.floating_action_button_light);
    floatingActionButtonHeart = (ImageView) findViewById(R.id.floating_action_button_heart);
    floatingActionButtonThunder = (ImageView) findViewById(R.id.floating_action_button_thunder);
    floatingActionButtonSmile = (ImageView) findViewById(R.id.floating_action_button_smile);


    floatingActionsOverlay.setVisibility(View.INVISIBLE);
    floatingActionsSubmenuFan.setVisibility(GONE);
  }

  private void bindListeners() {
    floatingActionsOverlay.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View view) {
        view.invalidate();
      }
    });

    floatingActionMenuButton.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        toggleMenu();
      }
    });
  }

  //region animators
  private void initAnimators() {
    setMenuButtonFlipIconAnimator();
    setMenuButtonFlipBackIconAnimator();
  }

  private void setMenuButtonFlipIconAnimator() {
    ObjectAnimator flipAnim =
        ObjectAnimator.ofFloat(floatingActionMenuButton, View.ROTATION_Y, -90);
    flipAnim.setDuration(100);
    flipAnim.setInterpolator(new LinearInterpolator());
    flipAnim.addListener(new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animation) {

      }

      @Override public void onAnimationEnd(Animator animation) {
        floatingActionMenuButton.setImageResource(R.drawable.ic_fab_menu_white);
      }

      @Override public void onAnimationCancel(Animator animation) {

      }

      @Override public void onAnimationRepeat(Animator animation) {

      }
    });

    ObjectAnimator flipAnimReturn =
        ObjectAnimator.ofFloat(floatingActionMenuButton, View.ROTATION_Y, 0);
    flipAnimReturn.setDuration(100);
    flipAnimReturn.setInterpolator(new LinearInterpolator());

    menuButtonFlipIconAnimator = new AnimatorSet();
    menuButtonFlipIconAnimator.play(flipAnim).before(flipAnimReturn);
  }

  private void setMenuButtonFlipBackIconAnimator() {
    ObjectAnimator flipAnim =
        ObjectAnimator.ofFloat(floatingActionMenuButton, View.ROTATION_Y, 90);
    flipAnim.setDuration(100);
    flipAnim.setInterpolator(new LinearInterpolator());
    flipAnim.addListener(new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animation) {

      }

      @Override public void onAnimationEnd(Animator animation) {
        floatingActionMenuButton.setImageResource(R.drawable.ic_fab_menu_red);
      }

      @Override public void onAnimationCancel(Animator animation) {

      }

      @Override public void onAnimationRepeat(Animator animation) {

      }
    });

    ObjectAnimator flipAnimReturn =
        ObjectAnimator.ofFloat(floatingActionMenuButton, View.ROTATION_Y, 0);
    flipAnimReturn.setDuration(100);
    flipAnimReturn.setInterpolator(new LinearInterpolator());

    menuButtonFlipBackIconAnimator = new AnimatorSet();
    menuButtonFlipBackIconAnimator.play(flipAnim).before(flipAnimReturn);
  }

  private void runFanButtonAnimator(final View button, int startOffset) {
    int[] fromLocation = {0 , 0};
    floatingActionFakeMenuButtonFan.getLocationOnScreen(fromLocation);

    int[] toLocation = {0 , 0};
    button.getLocationOnScreen(toLocation);

    final float fromXDelta = fromLocation[0];
    final float fromYDelta = fromLocation[1];
    final float toXDelta = toLocation[0];
    final float toYDelta = toLocation[1];


    ObjectAnimator translateXAnim = ObjectAnimator.ofFloat(button, "x",fromXDelta, toXDelta);
    translateXAnim.setDuration(1000);
    translateXAnim.setInterpolator(new BounceInterpolator());

    ObjectAnimator translateYAnim = ObjectAnimator.ofFloat(button, "y", fromYDelta, toYDelta);
    translateYAnim.setDuration(1000);
    translateYAnim.setInterpolator(new BounceInterpolator());

    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.play(translateXAnim).with(translateYAnim);

    animatorSet.setStartDelay(startOffset);
    animatorSet.start();
  }
  //endregion

  private void toggleMenu() {
    if (isMenuHorizontalVisible) {
      toogleOverlay(true);

      showFanMenu();

      isMenuHorizontalVisible = false;
    } else {
      toogleOverlay(false);

      showHorizontalMenu();

      isMenuHorizontalVisible = true;
    }
  }

  private void showHorizontalMenu() {
    floatingActionsSubmenuFan.setVisibility(GONE);
    floatingActionsSubmenuHorizontal.setVisibility(VISIBLE);

    menuButtonFlipBackIconAnimator.start();
  }

  private void showFanMenu() {
    floatingActionsSubmenuHorizontal.setVisibility(GONE);
    floatingActionsSubmenuFan.setVisibility(VISIBLE);

    menuButtonFlipIconAnimator.start();

    runFanButtonAnimator(floatingActionButtonLight, 0);
    runFanButtonAnimator(floatingActionButtonHeart, 1000);
    runFanButtonAnimator(floatingActionButtonThunder, 2000);
    runFanButtonAnimator(floatingActionButtonSmile, 3000);
  }

  private void toogleOverlay(boolean overlayVisible) {
    Animation slide;
    final int screenHeight = getScreenHeight();

    if (overlayVisible) {
      floatingActionsOverlay.setVisibility(View.VISIBLE);
      slide = new TranslateAnimation(0, 0, screenHeight, 0);
    } else {
      floatingActionsOverlay.setVisibility(View.INVISIBLE);
      slide = new TranslateAnimation(0, 0, 0, screenHeight);
    }
    slide.setInterpolator(new Interpolator() {
      @Override public float getInterpolation(float input) {
        if (input < screenHeight / 3) {
          return 1f;
        } else {
          return 3f;
        }
      }
    });
    slide.setDuration(3000);
    slide.setFillAfter(true);
    slide.setFillEnabled(true);
    floatingActionsOverlay.startAnimation(slide);

    slide.setAnimationListener(new Animation.AnimationListener() {

      @Override public void onAnimationStart(Animation animation) {

      }

      @Override public void onAnimationRepeat(Animation animation) {

      }

      @Override public void onAnimationEnd(Animation animation) {
        floatingActionsOverlay.clearAnimation();

        LayoutParams lp =
            new LayoutParams(floatingActionsOverlay.getWidth(), floatingActionsOverlay.getHeight());
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        floatingActionsOverlay.setLayoutParams(lp);
      }
    });
  }

  private int getScreenHeight() {
    WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();
    Point deviceDisplay = new Point();
    display.getSize(deviceDisplay);

    return deviceDisplay.y;
  }
}