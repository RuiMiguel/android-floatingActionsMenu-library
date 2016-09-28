package com.ruialonso.floatingactionmenu;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.ruialonso.library.FloatingActionButton;
import com.ruialonso.library.FloatingActionsMenu;

public class Fab extends RelativeLayout {

  @InjectView(R.id.frmOverMenuButton) FrameLayout frameOverLay;

  @InjectView(R.id.MenuBack) RelativeLayout mnuBack;

  @InjectView(R.id.menuFAB) FloatingActionsMenu mnuFAB;

  @InjectView(R.id.btnQR) FloatingActionButton btnCodigo;

  @InjectView(R.id.btnSound) FloatingActionButton btnAudio;

  @InjectView(R.id.btnPhoto) FloatingActionButton btnImagen;

  boolean showMnuBack = true;
  int screenH;
  private FragmentActivity activity;

  public Fab(Context context) {
    super(context);
    this.activity = (FragmentActivity) context;

    init();
  }

  public Fab(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.activity = (FragmentActivity) context;

    init();
  }

  public Fab(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.activity = (FragmentActivity) context;

    init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public Fab(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    this.activity = (FragmentActivity) context;

    init();
  }

  private void init() {
    initViews();
    setListeners();
  }

  private void initViews() {
    LayoutInflater inflater =
        (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View v = inflater.inflate(R.layout.fab_layout, this, true);
    ButterKnife.inject(this, v);
  }

  private void setListeners() {
    frameOverLay.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        v.invalidate();
        showOrHideMnuLayerBack();
      }
    });
    mnuBack.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        v.invalidate();
      }
    });

    btnCodigo.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        // Toast.makeText(view.getContext(), "Boton escaner de codigo clickau", Toast.LENGTH_SHORT).show();
        showOrHideMnuLayerBack();
        onScanPressed();
      }
    });

    btnAudio.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        //  Toast.makeText(view.getContext(), "Boton escaner audio clickau", Toast.LENGTH_SHORT).show();
        showOrHideMnuLayerBack();
        onListen();

        //                Intent i = new Intent(activity, MapViewActivity.class);
        //                i.putExtra(Constants.ClaraTags.ID_PROMO, "pruebatest");
        //                activity.startActivity(i);
      }
    });
    btnImagen.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        //  Toast.makeText(view.getContext(), "Boton escaner de imagen clickau", Toast.LENGTH_SHORT).show();
        showOrHideMnuLayerBack();
        onImageScanPressed();
      }
    });
  }

  private void showOrHideMnuLayerBack() {
    Animation slide = null;
    screenH = getMaxHeigth();
    if (showMnuBack) {
      mnuBack.setVisibility(View.VISIBLE);
      mnuFAB.expand();
      slide = new TranslateAnimation(0, 0, screenH,
          0); //asv quizas esto es mas gitano q Camraon de la ISla...
    } else {
      mnuBack.setVisibility(View.INVISIBLE);
      mnuFAB.collapse();
      slide = new TranslateAnimation(0, 0, 0, screenH);
    }
    slide.setInterpolator(new Interpolator() {
      @Override public float getInterpolation(float input) {
        if (input < screenH / 3) {
          return 1f;
        } else {
          return 3f;
        }
      }
    });
    slide.setDuration(3000);
    slide.setFillAfter(true);
    slide.setFillEnabled(true);
    mnuBack.startAnimation(slide);

    slide.setAnimationListener(new Animation.AnimationListener() {

      @Override public void onAnimationStart(Animation animation) {

      }

      @Override public void onAnimationRepeat(Animation animation) {
      }

      @Override public void onAnimationEnd(Animation animation) {

        mnuBack.clearAnimation();

        RelativeLayout.LayoutParams lp =
            new RelativeLayout.LayoutParams(mnuBack.getWidth(), mnuBack.getHeight());
        // lp.setMargins(0, 0, 0, 0);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mnuBack.setLayoutParams(lp);
      }
    });
    showMnuBack = !showMnuBack;
  }

  private int getMaxHeigth() {
    WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();

    return display.getHeight();
  }

  public void onScanPressed() {

  }

  private void onImageScanPressed() {

  }

  public void onListen() {

  }
}