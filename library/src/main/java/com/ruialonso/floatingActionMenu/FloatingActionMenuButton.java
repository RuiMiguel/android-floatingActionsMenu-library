package com.ruialonso.floatingactionmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;

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

  public void setDefaultIconDrawable(Drawable icon) {
    setIconDrawable(icon);
  }
}