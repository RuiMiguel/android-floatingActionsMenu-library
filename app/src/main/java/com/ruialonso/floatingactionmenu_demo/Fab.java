package com.ruialonso.floatingactionmenu_demo;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import com.ruialonso.floatingactionmenu.FloatingActionsMenu;

public class Fab extends RelativeLayout {

  private View rootView;
  private RelativeLayout floatingActionsOverlay;
  private FloatingActionsMenu floatingActionsMenu;

  private OnFloatingActionMenuListener onFloatingActionMenuListener;

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

  private void init(AttributeSet attrs, int defStyleAttr) {
    loadAttributes(attrs, defStyleAttr);

    initViews();
    bindListeners();
  }

  private void loadAttributes(AttributeSet attrs, int defStyle) {

  }

  private void initViews() {
    LayoutInflater inflater =
        (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    rootView = inflater.inflate(R.layout.fab_layout, this, true);

    floatingActionsOverlay = (RelativeLayout) findViewById(R.id.floating_actions_overlay);
    floatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.floating_actions_menu);

    floatingActionsOverlay.setVisibility(View.INVISIBLE);
  }

  private void bindListeners() {
    floatingActionsOverlay.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View view) {
        view.invalidate();
      }
    });

    floatingActionsMenu.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        if (onFloatingActionMenuListener != null) {
          onFloatingActionMenuListener.onFloatingMenuClick();
        }
      }
    });
    floatingActionsMenu.setOnToggleListener(new FloatingActionsMenu.OnToggleListener() {
      @Override public void onToggle(boolean menuOverlayVisibility) {
        toogleOverlay(menuOverlayVisibility);
        if (onFloatingActionMenuListener != null) {
          onFloatingActionMenuListener.onFloatingMenuToggle();
        }
      }
    });
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

        RelativeLayout.LayoutParams lp =
            new RelativeLayout.LayoutParams(floatingActionsOverlay.getWidth(),
                floatingActionsOverlay.getHeight());
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

  public void setOnFloatingActionMenuListener(OnFloatingActionMenuListener listener) {
    this.onFloatingActionMenuListener = listener;
  }

  public interface OnFloatingActionMenuListener {
    void onFloatingMenuClick();

    void onFloatingMenuToggle();
  }
}