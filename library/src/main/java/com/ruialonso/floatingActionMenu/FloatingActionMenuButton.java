package com.ruialonso.floatingactionmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;

public class FloatingActionMenuButton extends FloatingActionButton {
  private Drawable defaultIcon;
  int mPlusColor;

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
    mPlusColor = attr.getColor(R.styleable.FloatingActionMenuButton_fab_plusIconColor,
        getColor(android.R.color.white));

    attr.recycle();
  }

  @Override Drawable getIconDrawable() {
    final float iconSize = getDimension(R.dimen.fab_icon_size);
    final float iconHalfSize = iconSize / 2f;

    final float plusSize = getDimension(R.dimen.fab_plus_icon_size);
    final float plusHalfStroke = getDimension(R.dimen.fab_plus_icon_stroke) / 2f;
    final float plusOffset = (iconSize - plusSize) / 2f;

    final Shape shape = new Shape() {
      @Override public void draw(Canvas canvas, Paint paint) {
        canvas.drawRect(plusOffset, iconHalfSize - plusHalfStroke, iconSize - plusOffset,
            iconHalfSize + plusHalfStroke, paint);
        canvas.drawRect(iconHalfSize - plusHalfStroke, plusOffset, iconHalfSize + plusHalfStroke,
            iconSize - plusOffset, paint);
      }
    };

    ShapeDrawable drawable = new ShapeDrawable(shape);

    final Paint paint = drawable.getPaint();
    paint.setColor(mPlusColor);
    paint.setStyle(Style.FILL);
    paint.setAntiAlias(true);

    return drawable;
  }

  /*
  <objectAnimator
        android:valueFrom="-180"
        android:valueTo="0"
        android:propertyName="rotationY"
        android:interpolator="@android:interpolator/accelerate_decelerate"
        android:duration="@integer/card_flip_time_full" />
   */
}