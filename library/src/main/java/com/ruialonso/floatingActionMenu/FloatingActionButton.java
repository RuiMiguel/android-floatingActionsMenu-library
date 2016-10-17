package com.ruialonso.floatingactionmenu;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.ShapeDrawable.ShaderFactory;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.animation.BounceInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class FloatingActionButton extends ImageButton {

  public static final int SIZE_NORMAL = 0;
  public static final int SIZE_MINI = 1;
  protected AnimatorSet animatorSet;
  int bgColorNormal;
  int bgColorPressed;
  int bgColorDisabled;
  boolean mStrokeVisible;
  private int buttonSize;
  @DrawableRes private int buttonDefaultIcon;
  @DrawableRes private int buttonIcon;
  private Drawable buttonIconDrawable;
  private float buttonCircleSize;
  private float shadowRadius;
  private float shadowOffset;
  private int buttonDrawableSize;

  //region constructor
  public FloatingActionButton(Context context) {
    super(context);
    init(null, 0);
  }

  public FloatingActionButton(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs, 0);
  }

  public FloatingActionButton(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(attrs, defStyle);
  }
  //endregion

  protected void init(AttributeSet attributeSet, int defStyle) {
    loadAttributes(attributeSet, defStyle);
    initViews();
  }

  private void loadAttributes(AttributeSet attributeSet, int defStyle) {
    TypedArray attr =
        getContext().obtainStyledAttributes(attributeSet, R.styleable.FloatingActionButton, 0, 0);

    bgColorNormal = attr.getColor(R.styleable.FloatingActionButton_fab_bg_color_normal,
        getResources().getColor(android.R.color.transparent));
    bgColorPressed = attr.getColor(R.styleable.FloatingActionButton_fab_bg_color_pressed,
        getResources().getColor(android.R.color.transparent));
    bgColorDisabled = attr.getColor(R.styleable.FloatingActionButton_fab_bg_color_disabled,
        getResources().getColor(android.R.color.darker_gray));

    buttonSize = attr.getInt(R.styleable.FloatingActionButton_fab_size, SIZE_NORMAL);
    buttonIcon = attr.getResourceId(R.styleable.FloatingActionButton_fab_icon, 0);

    mStrokeVisible = attr.getBoolean(R.styleable.FloatingActionButton_fab_stroke_visible, true);

    attr.recycle();
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    setMeasuredDimension(buttonDrawableSize, buttonDrawableSize);
  }

  private void initViews() {
    buttonDefaultIcon = buttonIcon;

    shadowRadius = getResources().getDimension(R.dimen.fab_shadow_radius);
    shadowOffset = getResources().getDimension(R.dimen.fab_shadow_offset);

    updateButtonCircleSize();
    updateButtonDrawableSize();
    updateBackground();
  }

  private void updateButtonCircleSize() {
    buttonCircleSize = getResources().getDimension(
        buttonSize == SIZE_NORMAL ? R.dimen.fab_size_normal : R.dimen.fab_size_mini);
  }

  private void updateButtonDrawableSize() {
    buttonDrawableSize = (int) (buttonCircleSize + 2 * shadowRadius);
  }

  @TargetApi(VERSION_CODES.LOLLIPOP) void updateBackground() {
    final float strokeWidth = getResources().getDimension(R.dimen.fab_stroke_width);
    final float halfStrokeWidth = strokeWidth / 2f;

    LayerDrawable layerDrawable = new LayerDrawable(new Drawable[] {
        createFillDrawable(strokeWidth), createOuterStrokeDrawable(strokeWidth),
        getIconDrawable()
    });

    int iconOffset =
        (int) (buttonCircleSize - getResources().getDimension(buttonSize == SIZE_NORMAL ? R.dimen.fab_size_normal : R.dimen.fab_size_mini)) / 2;

    int circleInsetHorizontal = (int) (shadowRadius);
    int circleInsetTop = (int) (shadowRadius - shadowOffset);
    int circleInsetBottom = (int) (shadowRadius + shadowOffset);

    layerDrawable.setLayerInset(0, circleInsetHorizontal, circleInsetTop, circleInsetHorizontal,
        circleInsetBottom);

    layerDrawable.setLayerInset(1, (int) (circleInsetHorizontal - halfStrokeWidth),
        (int) (circleInsetTop - halfStrokeWidth), (int) (circleInsetHorizontal - halfStrokeWidth),
        (int) (circleInsetBottom - halfStrokeWidth));

    layerDrawable.setLayerInset(2, circleInsetHorizontal + iconOffset, circleInsetTop + iconOffset,
        circleInsetHorizontal + iconOffset, circleInsetBottom + iconOffset);

    setBackgroundCompat(layerDrawable);
  }

  @FAB_SIZE public int getSize() {
    return buttonSize;
  }

  public void setSize(@FAB_SIZE int size) {
    if (size != SIZE_MINI && size != SIZE_NORMAL) {
      throw new IllegalArgumentException("Use @FAB_SIZE constants only!");
    }

    if (buttonSize != size) {
      buttonSize = size;
      updateButtonCircleSize();
      updateButtonDrawableSize();
      updateBackground();
    }
  }

  //region colors
  public int getBackgroundColorNormal() {
    return bgColorNormal;
  }

  public void setBackgroundColorNormal(int color) {
    if (bgColorNormal != color) {
      bgColorNormal = color;
      updateBackground();
    }
  }

  public void setBackgroundColorNormalResId(@ColorRes int colorNormal) {
    setBackgroundColorNormal(getResources().getColor(colorNormal));
  }

  public int getBackgroundColorPressed() {
    return bgColorPressed;
  }

  public void setBackgroundColorPressed(int color) {
    if (bgColorPressed != color) {
      bgColorPressed = color;
      updateBackground();
    }
  }

  public void setBackgroundColorPressedResId(@ColorRes int colorPressed) {
    setBackgroundColorPressed(getResources().getColor(colorPressed));
  }

  public int getBackgroundColorDisabled() {
    return bgColorDisabled;
  }

  public void setBackgroundColorDisabled(int color) {
    if (bgColorDisabled != color) {
      bgColorDisabled = color;
      updateBackground();
    }
  }

  public void setBackgroundColorDisabledResId(@ColorRes int colorDisabled) {
    setBackgroundColorDisabled(getResources().getColor(colorDisabled));
  }
  //endregion

  //region icon drawable
  public void setIcon(@DrawableRes int icon) {
    if (buttonIcon != icon) {
      buttonIcon = icon;
      buttonIconDrawable = null;
      updateBackground();
    }
  }

  public @DrawableRes int getIcon() {
    return buttonIcon;
  }

  public void setIconDrawableToDefault() {
    setIcon(buttonDefaultIcon);
  }

  @TargetApi(VERSION_CODES.LOLLIPOP) public Drawable getIconDrawable() {
    if (buttonIconDrawable != null) {
      return buttonIconDrawable;
    } else if (buttonIcon != 0) {

      Drawable drawAux;
      if (Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
        drawAux = this.getContext().getDrawable(buttonIcon);
      } else {
        drawAux = this.getContext().getResources().getDrawable(buttonIcon);
      }

      return drawAux;
    } else {
      return new ColorDrawable(Color.TRANSPARENT);
    }
  }

  public void setIconDrawable(@NonNull Drawable iconDrawable) {
    if (buttonIconDrawable != iconDrawable) {
      buttonIcon = 0;
      buttonIconDrawable = iconDrawable;
      updateBackground();
    }
  }
  //endregion

  //region create drawables
  private StateListDrawable createFillDrawable(float strokeWidth) {
    StateListDrawable drawable = new StateListDrawable();
    drawable.addState(new int[] { -android.R.attr.state_enabled },
        createCircleDrawable(bgColorDisabled, strokeWidth));
    drawable.addState(new int[] { android.R.attr.state_pressed },
        createCircleDrawable(bgColorPressed, strokeWidth));
    drawable.addState(new int[] {}, createCircleDrawable(bgColorNormal, strokeWidth));
    return drawable;
  }
  //endregion

  private Drawable createCircleDrawable(int color, float strokeWidth) {
    int alpha = Color.alpha(color);
    int opaqueColor = opaque(color);

    ShapeDrawable fillDrawable = new ShapeDrawable(new OvalShape());

    final Paint paint = fillDrawable.getPaint();
    paint.setAntiAlias(true);
    paint.setColor(opaqueColor);

    Drawable[] layers = {
        fillDrawable, createInnerStrokesDrawable(opaqueColor, strokeWidth)
    };

    LayerDrawable drawable = alpha == 255 || !mStrokeVisible ? new LayerDrawable(layers)
        : new TranslucentLayerDrawable(alpha, layers);

    int halfStrokeWidth = (int) (strokeWidth / 2f);
    drawable.setLayerInset(1, halfStrokeWidth, halfStrokeWidth, halfStrokeWidth, halfStrokeWidth);

    return drawable;
  }

  private Drawable createOuterStrokeDrawable(float strokeWidth) {
    ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());

    final Paint paint = shapeDrawable.getPaint();
    paint.setAntiAlias(true);
    paint.setStrokeWidth(strokeWidth);
    paint.setStyle(Style.STROKE);
    paint.setColor(Color.BLACK);
    paint.setAlpha(opacityToAlpha(0.02f));

    return shapeDrawable;
  }

  private Drawable createInnerStrokesDrawable(final int color, float strokeWidth) {
    if (!mStrokeVisible) {
      return new ColorDrawable(Color.TRANSPARENT);
    }

    ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());

    final int bottomStrokeColor = darkenColor(color);
    final int bottomStrokeColorHalfTransparent = halfTransparent(bottomStrokeColor);
    final int topStrokeColor = lightenColor(color);
    final int topStrokeColorHalfTransparent = halfTransparent(topStrokeColor);

    final Paint paint = shapeDrawable.getPaint();
    paint.setAntiAlias(true);
    paint.setStrokeWidth(strokeWidth);
    paint.setStyle(Style.STROKE);
    shapeDrawable.setShaderFactory(new ShaderFactory() {
      @Override public Shader resize(int width, int height) {
        return new LinearGradient(width / 2, 0, width / 2, height, new int[] {
            topStrokeColor, topStrokeColorHalfTransparent, color, bottomStrokeColorHalfTransparent,
            bottomStrokeColor
        }, new float[] { 0f, 0.2f, 0.5f, 0.8f, 1f }, TileMode.CLAMP);
      }
    });

    return shapeDrawable;
  }

  //region animator
  public void setAnimatorSet(AnimatorSet animatorSet) {
    this.animatorSet = animatorSet;
  }

  public void runAnimation() {
    if (animatorSet != null) {
      System.out.println("Inside runAnimation()");
      animatorSet.start();
    }
  }
  //endregion

  private int opacityToAlpha(float opacity) {
    return (int) (255f * opacity);
  }

  //endregion

  private int darkenColor(int argb) {
    return adjustColorBrightness(argb, 0.9f);
  }

  private int lightenColor(int argb) {
    return adjustColorBrightness(argb, 1.1f);
  }

  private int adjustColorBrightness(int argb, float factor) {
    float[] hsv = new float[3];
    Color.colorToHSV(argb, hsv);

    hsv[2] = Math.min(hsv[2] * factor, 1f);

    return Color.HSVToColor(Color.alpha(argb), hsv);
  }

  private int halfTransparent(int argb) {
    return Color.argb(Color.alpha(argb) / 2, Color.red(argb), Color.green(argb), Color.blue(argb));
  }

  private int opaque(int argb) {
    return Color.rgb(Color.red(argb), Color.green(argb), Color.blue(argb));
  }

  @SuppressWarnings("deprecation") @SuppressLint("NewApi")
  private void setBackgroundCompat(Drawable drawable) {
    if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
      setBackground(drawable);
    } else {
      setBackgroundDrawable(drawable);
    }
  }

  //region FAB size
  @Retention(RetentionPolicy.SOURCE) @IntDef({ SIZE_NORMAL, SIZE_MINI })
  public @interface FAB_SIZE {
  }

  private static class TranslucentLayerDrawable extends LayerDrawable {
    private final int mAlpha;

    public TranslucentLayerDrawable(int alpha, Drawable... layers) {
      super(layers);
      mAlpha = alpha;
    }

    @Override public void draw(Canvas canvas) {
      Rect bounds = getBounds();
      canvas.saveLayerAlpha(bounds.left, bounds.top, bounds.right, bounds.bottom, mAlpha,
          Canvas.ALL_SAVE_FLAG);
      super.draw(canvas);
      canvas.restore();
    }
  }
}