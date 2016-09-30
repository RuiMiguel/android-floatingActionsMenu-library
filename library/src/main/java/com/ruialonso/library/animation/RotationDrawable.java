package com.ruialonso.library.animation;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

/**
 * Created by rui.alonso on 29/9/16.
 */

public class RotationDrawable extends LayerDrawable {
  private float degrees;

  public RotationDrawable(Drawable drawable) {
    super(new Drawable[] { drawable });
  }

  @SuppressWarnings("UnusedDeclaration") public float getDegrees() {
    return degrees;
  }

  @SuppressWarnings("UnusedDeclaration") public void setDegrees(float degrees) {
    degrees = degrees;
    invalidateSelf();
  }

  @Override public void draw(Canvas canvas) {
    canvas.save();
    canvas.rotate(degrees, getBounds().centerX(), getBounds().centerY());
    super.draw(canvas);
    canvas.restore();
  }
}