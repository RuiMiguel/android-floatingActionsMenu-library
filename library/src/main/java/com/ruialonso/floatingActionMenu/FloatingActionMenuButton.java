package com.ruialonso.floatingactionmenu;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class FloatingActionMenuButton extends FloatingActionButton {
  @DrawableRes private int buttonDefaultIcon;

  public FloatingActionMenuButton(Context context) {
    super(context);
    init(null, 0);
  }

  public FloatingActionMenuButton(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs, 0);
  }

  public FloatingActionMenuButton(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(attrs, defStyle);
  }

  @Override protected void init(AttributeSet attributeSet, int defStyle) {
    super.init(attributeSet, defStyle);

    loadAttributes(attributeSet, defStyle);
  }

  private void loadAttributes(AttributeSet attributeSet, int defStyle) {
    TypedArray attr =
        getContext().obtainStyledAttributes(attributeSet, R.styleable.FloatingActionMenuButton, 0,
            0);

    attr.recycle();

    initView();
  }

  private void initView() {
    buttonDefaultIcon = getIcon();
  }

  public void setIconDrawableToDefault() {
    setIcon(buttonDefaultIcon);
  }

  //region animator
  @Override
  protected void initDefaultAnimation() {
    /*
    ObjectAnimator flipAnim = ObjectAnimator.ofFloat(this, View.ROTATION_Y, -90);
    flipAnim.setDuration(500);
    flipAnim.setInterpolator(new LinearInterpolator());

    ObjectAnimator flipAnim2 = ObjectAnimator.ofFloat(this, View.ROTATION_Y, 0);
    flipAnim2.setDuration(500);
    flipAnim2.setInterpolator(new LinearInterpolator());

    animatorSet = new AnimatorSet();
    animatorSet.play(flipAnim).before(flipAnim2);
    */
  }
  //endregion
}