package android.support.v7.widget;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R.styleable;
import android.util.AttributeSet;
import android.widget.SeekBar;

class AppCompatSeekBarHelper
  extends AppCompatProgressBarHelper
{
  private boolean mHasTickMarkTint = false;
  private boolean mHasTickMarkTintMode = false;
  private Drawable mTickMark;
  private ColorStateList mTickMarkTintList = null;
  private PorterDuff.Mode mTickMarkTintMode = null;
  private final SeekBar mView;
  
  AppCompatSeekBarHelper(SeekBar paramSeekBar)
  {
    super(paramSeekBar);
    this.mView = paramSeekBar;
  }
  
  private void applyTickMarkTint()
  {
    if ((this.mTickMark != null) && ((this.mHasTickMarkTint) || (this.mHasTickMarkTintMode)))
    {
      this.mTickMark = DrawableCompat.wrap(this.mTickMark.mutate());
      if (this.mHasTickMarkTint) {
        DrawableCompat.setTintList(this.mTickMark, this.mTickMarkTintList);
      }
      if (this.mHasTickMarkTintMode) {
        DrawableCompat.setTintMode(this.mTickMark, this.mTickMarkTintMode);
      }
      if (this.mTickMark.isStateful()) {
        this.mTickMark.setState(this.mView.getDrawableState());
      }
    }
  }
  
  void drawTickMarks(Canvas paramCanvas)
  {
    int i = 1;
    if (this.mTickMark != null)
    {
      int j = this.mView.getMax();
      if (j > i)
      {
        int k = this.mTickMark.getIntrinsicWidth();
        int m = this.mTickMark.getIntrinsicHeight();
        if (k >= 0) {}
        int i1;
        for (int n = k / 2;; n = i)
        {
          if (m >= 0) {
            i = m / 2;
          }
          this.mTickMark.setBounds(-n, -i, n, i);
          float f = (this.mView.getWidth() - this.mView.getPaddingLeft() - this.mView.getPaddingRight()) / j;
          i1 = paramCanvas.save();
          paramCanvas.translate(this.mView.getPaddingLeft(), this.mView.getHeight() / 2);
          for (int i2 = 0; i2 <= j; i2++)
          {
            this.mTickMark.draw(paramCanvas);
            paramCanvas.translate(f, 0.0F);
          }
        }
        paramCanvas.restoreToCount(i1);
      }
    }
  }
  
  void drawableStateChanged()
  {
    Drawable localDrawable = this.mTickMark;
    if ((localDrawable != null) && (localDrawable.isStateful()) && (localDrawable.setState(this.mView.getDrawableState()))) {
      this.mView.invalidateDrawable(localDrawable);
    }
  }
  
  @Nullable
  Drawable getTickMark()
  {
    return this.mTickMark;
  }
  
  @Nullable
  ColorStateList getTickMarkTintList()
  {
    return this.mTickMarkTintList;
  }
  
  @Nullable
  PorterDuff.Mode getTickMarkTintMode()
  {
    return this.mTickMarkTintMode;
  }
  
  @TargetApi(11)
  @RequiresApi(11)
  void jumpDrawablesToCurrentState()
  {
    if (this.mTickMark != null) {
      this.mTickMark.jumpToCurrentState();
    }
  }
  
  void loadFromAttributes(AttributeSet paramAttributeSet, int paramInt)
  {
    super.loadFromAttributes(paramAttributeSet, paramInt);
    TintTypedArray localTintTypedArray = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), paramAttributeSet, R.styleable.AppCompatSeekBar, paramInt, 0);
    Drawable localDrawable = localTintTypedArray.getDrawableIfKnown(R.styleable.AppCompatSeekBar_android_thumb);
    if (localDrawable != null) {
      this.mView.setThumb(localDrawable);
    }
    setTickMark(localTintTypedArray.getDrawable(R.styleable.AppCompatSeekBar_tickMark));
    if (localTintTypedArray.hasValue(R.styleable.AppCompatSeekBar_tickMarkTintMode))
    {
      this.mTickMarkTintMode = DrawableUtils.parseTintMode(localTintTypedArray.getInt(R.styleable.AppCompatSeekBar_tickMarkTintMode, -1), this.mTickMarkTintMode);
      this.mHasTickMarkTintMode = true;
    }
    if (localTintTypedArray.hasValue(R.styleable.AppCompatSeekBar_tickMarkTint))
    {
      this.mTickMarkTintList = localTintTypedArray.getColorStateList(R.styleable.AppCompatSeekBar_tickMarkTint);
      this.mHasTickMarkTint = true;
    }
    localTintTypedArray.recycle();
    applyTickMarkTint();
  }
  
  void setTickMark(@Nullable Drawable paramDrawable)
  {
    if (this.mTickMark != null) {
      this.mTickMark.setCallback(null);
    }
    this.mTickMark = paramDrawable;
    if (paramDrawable != null)
    {
      paramDrawable.setCallback(this.mView);
      DrawableCompat.setLayoutDirection(paramDrawable, ViewCompat.getLayoutDirection(this.mView));
      if (paramDrawable.isStateful()) {
        paramDrawable.setState(this.mView.getDrawableState());
      }
      applyTickMarkTint();
    }
    this.mView.invalidate();
  }
  
  void setTickMarkTintList(@Nullable ColorStateList paramColorStateList)
  {
    this.mTickMarkTintList = paramColorStateList;
    this.mHasTickMarkTint = true;
    applyTickMarkTint();
  }
  
  void setTickMarkTintMode(@Nullable PorterDuff.Mode paramMode)
  {
    this.mTickMarkTintMode = paramMode;
    this.mHasTickMarkTintMode = true;
    applyTickMarkTint();
  }
}
