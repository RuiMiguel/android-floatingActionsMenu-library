package com.ruialonso.floatingactionmenu;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class FloatingActionsMenu extends RelativeLayout {

  private static final long MENU_FLIP_ANIMATION_DURATION = 100;
  private static final long BUTTON_BOUNCE_ANIMATION_DURATION = 700;
  private static final long BUTTON_ACCELERATE_ANIMATION_DURATION = 100;
  float[] fabSearchPosition;
  float[] fabNotificationsPosition;
  float[] fabLightPosition;
  float[] fabHeartPosition;
  float[] fabThunderPosition;
  float[] fabSmilePosition;
  private AnimatorSet menuButtonFlipIconAnimator;
  private AnimatorSet menuButtonFlipBackIconAnimator;
  private View rootView;
  private RelativeLayout floatingActionsOverlay;
  private RelativeLayout floatingActionsMenu;
  private RelativeLayout floatingActionsSubmenuHorizontal;
  private RelativeLayout floatingActionsSubmenuFan;
  private ImageView floatingActionMenuButton;
  private View floatingActionFakeMenuButtonHorizontal;
  private ImageView floatingActionButton1;
  private ImageView floatingActionButton2;
  private View floatingActionFakeMenuButtonFan;
  private ImageView floatingActionButton3;
  private ImageView floatingActionButton4;
  private ImageView floatingActionButton5;
  private ImageView floatingActionButton6;
  private OnSearchButtonClickListener searchButtonClickedListener;
  private OnNotificationsButtonClickListener notificationsButtonClickedListener;
  private OnLightButtonClickListener lightButtonClickedListener;
  private OnHeartButtonClickListener heartButtonClickedListener;
  private OnThunderButtonClickListener thunderButtonClickedListener;
  private OnSmileButtonClickListener smileButtonClickedListener;

  private boolean isMenuHorizontalVisible = true;

  //region constructor
  public FloatingActionsMenu(Context context) {
    super(context);
    init(null, 0);
  }

  public FloatingActionsMenu(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs, 0);
  }

  public FloatingActionsMenu(Context context, AttributeSet attrs, int defStyleAttr) {
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
    rootView = inflater.inflate(R.layout.fab_menu_layout, this, true);

    floatingActionsOverlay = (RelativeLayout) findViewById(R.id.floating_actions_overlay);

    floatingActionsMenu = (RelativeLayout) findViewById(R.id.floating_actions_menu);
    floatingActionsSubmenuHorizontal =
        (RelativeLayout) findViewById(R.id.floating_actions_submenu_horizontal);
    floatingActionsSubmenuFan = (RelativeLayout) findViewById(R.id.floating_actions_submenu_fan);

    floatingActionMenuButton = (ImageView) findViewById(R.id.floating_action_menu_button);

    floatingActionFakeMenuButtonHorizontal = findViewById(R.id.fake_menu_button_submenu_horizontal);
    floatingActionButton1 = (ImageView) findViewById(R.id.floating_action_button1);
    floatingActionButton2 =
        (ImageView) findViewById(R.id.floating_action_button2);

    floatingActionFakeMenuButtonFan = findViewById(R.id.fake_menu_button_submenu_fan);
    floatingActionButton3 = (ImageView) findViewById(R.id.floating_action_button3);
    floatingActionButton4 = (ImageView) findViewById(R.id.floating_action_button4);
    floatingActionButton5 = (ImageView) findViewById(R.id.floating_action_button5);
    floatingActionButton6 = (ImageView) findViewById(R.id.floating_action_button6);

    floatingActionsOverlay.setVisibility(View.INVISIBLE);
    floatingActionsSubmenuFan.setVisibility(GONE);
  }

  private void bindListeners() {
    floatingActionsOverlay.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View view) {
        view.invalidate();

        if (!isMenuHorizontalVisible) {
          toggleMenu();
        }
      }
    });

    floatingActionMenuButton.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View view) {

        toggleMenu();
      }
    });

    floatingActionButton1.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View view) {
        if (searchButtonClickedListener != null) {
          searchButtonClickedListener.searchButtonClicked(view);
        }
      }
    });
    floatingActionButton2.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View view) {
        if (notificationsButtonClickedListener != null) {
          notificationsButtonClickedListener.notificationsButtonClicked(view);
        }
      }
    });
    floatingActionButton3.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View view) {
        if (lightButtonClickedListener != null) lightButtonClickedListener.lightButtonClicked(view);
      }
    });
    floatingActionButton4.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View view) {
        if (heartButtonClickedListener != null) heartButtonClickedListener.heartButtonClicked(view);
      }
    });
    floatingActionButton5.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View view) {
        if (thunderButtonClickedListener != null) {
          thunderButtonClickedListener.thunderButtonClicked(view);
        }
      }
    });
    floatingActionButton6.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View view) {
        if (smileButtonClickedListener != null) smileButtonClickedListener.smileButtonClicked(view);
      }
    });
  }

  //region fab listener
  public void setSearchButtonClickedListener(
      OnSearchButtonClickListener searchButtonClickedListener) {
    this.searchButtonClickedListener = searchButtonClickedListener;
  }

  public void setNotificationsButtonClickedListener(
      OnNotificationsButtonClickListener notificationsButtonClickedListener) {
    this.notificationsButtonClickedListener = notificationsButtonClickedListener;
  }

  public void setLightButtonClickedListener(OnLightButtonClickListener lightButtonClickedListener) {
    this.lightButtonClickedListener = lightButtonClickedListener;
  }

  public void setHeartButtonClickedListener(OnHeartButtonClickListener heartButtonClickedListener) {
    this.heartButtonClickedListener = heartButtonClickedListener;
  }

  public void setThunderButtonClickedListener(
      OnThunderButtonClickListener thunderButtonClickedListener) {
    this.thunderButtonClickedListener = thunderButtonClickedListener;
  }

  public void setSmileButtonClickedListener(OnSmileButtonClickListener smileButtonClickedListener) {
    this.smileButtonClickedListener = smileButtonClickedListener;
  }
  //endregion

  //region animators
  private void initAnimators() {
    setMenuButtonFlipIconAnimator();
    setMenuButtonFlipBackIconAnimator();
  }

  private void setMenuButtonFlipIconAnimator() {
    ObjectAnimator flipAnim =
        ObjectAnimator.ofFloat(floatingActionMenuButton, View.ROTATION_Y, -90);
    flipAnim.setDuration(MENU_FLIP_ANIMATION_DURATION);
    flipAnim.setInterpolator(new LinearInterpolator());
    flipAnim.addListener(new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animation) {

      }

      @Override public void onAnimationEnd(Animator animation) {
        floatingActionMenuButton.setImageResource(android.R.drawable.ic_dialog_email);
      }

      @Override public void onAnimationCancel(Animator animation) {

      }

      @Override public void onAnimationRepeat(Animator animation) {

      }
    });

    ObjectAnimator flipAnimReturn =
        ObjectAnimator.ofFloat(floatingActionMenuButton, View.ROTATION_Y, 0);
    flipAnimReturn.setDuration(MENU_FLIP_ANIMATION_DURATION);
    flipAnimReturn.setInterpolator(new LinearInterpolator());

    menuButtonFlipIconAnimator = new AnimatorSet();
    menuButtonFlipIconAnimator.play(flipAnim).before(flipAnimReturn);
  }

  private void setMenuButtonFlipBackIconAnimator() {
    ObjectAnimator flipAnim = ObjectAnimator.ofFloat(floatingActionMenuButton, View.ROTATION_Y, 90);
    flipAnim.setDuration(MENU_FLIP_ANIMATION_DURATION);
    flipAnim.setInterpolator(new LinearInterpolator());
    flipAnim.addListener(new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animation) {

      }

      @Override public void onAnimationEnd(Animator animation) {
        floatingActionMenuButton.setImageResource(android.R.drawable.ic_dialog_map);
      }

      @Override public void onAnimationCancel(Animator animation) {

      }

      @Override public void onAnimationRepeat(Animator animation) {

      }
    });

    ObjectAnimator flipAnimReturn =
        ObjectAnimator.ofFloat(floatingActionMenuButton, View.ROTATION_Y, 0);
    flipAnimReturn.setDuration(MENU_FLIP_ANIMATION_DURATION);
    flipAnimReturn.setInterpolator(new LinearInterpolator());

    menuButtonFlipBackIconAnimator = new AnimatorSet();
    menuButtonFlipBackIconAnimator.play(flipAnim).before(flipAnimReturn);
  }

  private void setHorizontalButtonsAnimations() {
    floatingActionsSubmenuHorizontal.getViewTreeObserver()
        .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
          @Override public boolean onPreDraw() {
            int width = floatingActionsSubmenuHorizontal.getWidth();
            int height = floatingActionsSubmenuHorizontal.getHeight();
            if (width > 0 && height > 0) {
              floatingActionsSubmenuHorizontal.getViewTreeObserver().removeOnPreDrawListener(this);

              backupHorizontalButtonPositions();
              runHorizontalButtonAnimator(floatingActionButton1, fabSearchPosition, 0);
              runHorizontalButtonAnimator(floatingActionButton2,
                  fabNotificationsPosition, 0);
            }

            return false;
          }
        });
  }

  /**
   * backup horizontal submenu buttons positions, to prevent animation bug on quick switching
   */
  private void backupHorizontalButtonPositions() {
    if (fabSearchPosition == null) {
      fabSearchPosition = new float[2];
      fabSearchPosition[0] = floatingActionButton1.getX();
      fabSearchPosition[1] = floatingActionButton1.getY();
    }
    if (fabNotificationsPosition == null) {
      fabNotificationsPosition = new float[2];
      fabNotificationsPosition[0] = floatingActionButton2.getX();
      fabNotificationsPosition[1] = floatingActionButton2.getY();
    }
  }

  private void setFanButtonsAnimations() {
    floatingActionsSubmenuFan.getViewTreeObserver()
        .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
          @Override public boolean onPreDraw() {
            int width = floatingActionsSubmenuFan.getWidth();
            int height = floatingActionsSubmenuFan.getHeight();
            if (width > 0 && height > 0) {
              floatingActionsSubmenuFan.getViewTreeObserver().removeOnPreDrawListener(this);

              floatingActionButton3.setVisibility(INVISIBLE);
              floatingActionButton4.setVisibility(INVISIBLE);
              floatingActionButton5.setVisibility(INVISIBLE);
              floatingActionButton6.setVisibility(INVISIBLE);

              backupFanButtonPositions();
              runFanButtonAnimator(floatingActionButton3, fabLightPosition, 0);
              runFanButtonAnimator(floatingActionButton4, fabHeartPosition, 80);
              runFanButtonAnimator(floatingActionButton5, fabThunderPosition, 160);
              runFanButtonAnimator(floatingActionButton6, fabSmilePosition, 240);
            }

            return false;
          }
        });
  }

  /**
   * backup fan submenu buttons positions, to prevent animation bug on quick switching
   */
  private void backupFanButtonPositions() {
    if (fabLightPosition == null) {
      fabLightPosition = new float[2];
      fabLightPosition[0] = floatingActionButton3.getX();
      fabLightPosition[1] = floatingActionButton3.getY();
    }

    if (fabHeartPosition == null) {
      fabHeartPosition = new float[2];
      fabHeartPosition[0] = floatingActionButton4.getX();
      fabHeartPosition[1] = floatingActionButton4.getY();
    }

    if (fabThunderPosition == null) {
      fabThunderPosition = new float[2];
      fabThunderPosition[0] = floatingActionButton5.getX();
      fabThunderPosition[1] = floatingActionButton5.getY();
    }

    if (fabSmilePosition == null) {
      fabSmilePosition = new float[2];
      fabSmilePosition[0] = floatingActionButton6.getX();
      fabSmilePosition[1] = floatingActionButton6.getY();
    }
  }

  private void runHorizontalButtonAnimator(final View button, float[] toPosition, int startOffset) {
    final float fromXDelta = floatingActionFakeMenuButtonHorizontal.getX()
        - floatingActionFakeMenuButtonHorizontal.getWidth() / 2;

    final float toXDelta = toPosition[0];

    ObjectAnimator translateXAnim = ObjectAnimator.ofFloat(button, "x", fromXDelta, toXDelta);
    translateXAnim.setDuration(BUTTON_ACCELERATE_ANIMATION_DURATION);
    translateXAnim.setInterpolator(new AccelerateInterpolator());
    translateXAnim.addListener(new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animation) {
        button.setVisibility(VISIBLE);
      }

      @Override public void onAnimationEnd(Animator animation) {

      }

      @Override public void onAnimationCancel(Animator animation) {

      }

      @Override public void onAnimationRepeat(Animator animation) {

      }
    });

    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.playTogether(translateXAnim);
    animatorSet.setStartDelay(startOffset);
    animatorSet.start();
  }

  private void runFanButtonAnimator(final View button, float[] toPosition, int startOffset) {
    final float fromXDelta =
        floatingActionFakeMenuButtonFan.getX() - floatingActionFakeMenuButtonFan.getWidth() / 2;
    final float fromYDelta =
        floatingActionFakeMenuButtonFan.getY() - floatingActionFakeMenuButtonFan.getHeight() / 2;

    final float toXDelta = toPosition[0];
    final float toYDelta = toPosition[1];

    ObjectAnimator translateXAnim = ObjectAnimator.ofFloat(button, "x", fromXDelta, toXDelta);
    translateXAnim.setDuration(BUTTON_BOUNCE_ANIMATION_DURATION);
    translateXAnim.setInterpolator(new BounceInterpolator());
    translateXAnim.addListener(new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animation) {
        button.setVisibility(VISIBLE);
      }

      @Override public void onAnimationEnd(Animator animation) {

      }

      @Override public void onAnimationCancel(Animator animation) {

      }

      @Override public void onAnimationRepeat(Animator animation) {

      }
    });

    ObjectAnimator translateYAnim = ObjectAnimator.ofFloat(button, "y", fromYDelta, toYDelta);
    translateYAnim.setDuration(BUTTON_BOUNCE_ANIMATION_DURATION);
    translateYAnim.setInterpolator(new BounceInterpolator());
    translateYAnim.addListener(new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animation) {
        button.setVisibility(VISIBLE);
      }

      @Override public void onAnimationEnd(Animator animation) {

      }

      @Override public void onAnimationCancel(Animator animation) {

      }

      @Override public void onAnimationRepeat(Animator animation) {

      }
    });

    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.playTogether(translateXAnim, translateYAnim);

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

    if (!isMenuHorizontalVisible) {
      menuButtonFlipBackIconAnimator.start();
      setHorizontalButtonsAnimations();
    }
  }

  private void showFanMenu() {
    floatingActionsSubmenuHorizontal.setVisibility(GONE);
    floatingActionsSubmenuFan.setVisibility(VISIBLE);

    if (isMenuHorizontalVisible) {
      menuButtonFlipIconAnimator.start();
      setFanButtonsAnimations();
    }
  }

  private void toogleOverlay(boolean overlayVisible) {
    Animation slide;
    final int screenHeight = getScreenHeight();

    if (overlayVisible) {
      floatingActionsOverlay.setVisibility(View.VISIBLE);
      slide = new TranslateAnimation(0, 0, screenHeight, 0);
    } else {
      floatingActionsOverlay.setVisibility(View.GONE);
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

  public interface OnSearchButtonClickListener {
    void searchButtonClicked(View view);
  }

  public interface OnNotificationsButtonClickListener {
    void notificationsButtonClicked(View view);
  }

  public interface OnLightButtonClickListener {
    void lightButtonClicked(View view);
  }

  public interface OnHeartButtonClickListener {
    void heartButtonClicked(View view);
  }

  public interface OnThunderButtonClickListener {
    void thunderButtonClicked(View view);
  }

  public interface OnSmileButtonClickListener {
    void smileButtonClicked(View view);
  }
}