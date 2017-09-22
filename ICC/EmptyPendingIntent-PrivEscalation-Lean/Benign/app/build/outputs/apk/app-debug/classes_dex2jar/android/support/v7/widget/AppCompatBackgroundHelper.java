package android.support.v7.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R.styleable;
import android.util.AttributeSet;
import android.view.View;

class AppCompatBackgroundHelper
{
  private int mBackgroundResId = -1;
  private TintInfo mBackgroundTint;
  private final AppCompatDrawableManager mDrawableManager;
  private TintInfo mInternalBackgroundTint;
  private TintInfo mTmpInfo;
  private final View mView;
  
  AppCompatBackgroundHelper(View paramView)
  {
    this.mView = paramView;
    this.mDrawableManager = AppCompatDrawableManager.get();
  }
  
  private boolean applyFrameworkTintUsingColorFilter(@NonNull Drawable paramDrawable)
  {
    if (this.mTmpInfo == null) {
      this.mTmpInfo = new TintInfo();
    }
    TintInfo localTintInfo = this.mTmpInfo;
    localTintInfo.clear();
    ColorStateList localColorStateList = ViewCompat.getBackgroundTintList(this.mView);
    if (localColorStateList != null)
    {
      localTintInfo.mHasTintList = true;
      localTintInfo.mTintList = localColorStateList;
    }
    PorterDuff.Mode localMode = ViewCompat.getBackgroundTintMode(this.mView);
    if (localMode != null)
    {
      localTintInfo.mHasTintMode = true;
      localTintInfo.mTintMode = localMode;
    }
    if ((localTintInfo.mHasTintList) || (localTintInfo.mHasTintMode))
    {
      AppCompatDrawableManager.tintDrawable(paramDrawable, localTintInfo, this.mView.getDrawableState());
      return true;
    }
    return false;
  }
  
  private boolean shouldApplyFrameworkTintUsingColorFilter()
  {
    boolean bool = true;
    int i = Build.VERSION.SDK_INT;
    if (i < 21) {
      bool = false;
    }
    while ((i == 21) || (this.mInternalBackgroundTint != null)) {
      return bool;
    }
    return false;
  }
  
  void applySupportBackgroundTint()
  {
    Drawable localDrawable = this.mView.getBackground();
    if ((localDrawable == null) || ((shouldApplyFrameworkTintUsingColorFilter()) && (applyFrameworkTintUsingColorFilter(localDrawable)))) {}
    do
    {
      return;
      if (this.mBackgroundTint != null)
      {
        AppCompatDrawableManager.tintDrawable(localDrawable, this.mBackgroundTint, this.mView.getDrawableState());
        return;
      }
    } while (this.mInternalBackgroundTint == null);
    AppCompatDrawableManager.tintDrawable(localDrawable, this.mInternalBackgroundTint, this.mView.getDrawableState());
  }
  
  ColorStateList getSupportBackgroundTintList()
  {
    if (this.mBackgroundTint != null) {
      return this.mBackgroundTint.mTintList;
    }
    return null;
  }
  
  PorterDuff.Mode getSupportBackgroundTintMode()
  {
    if (this.mBackgroundTint != null) {
      return this.mBackgroundTint.mTintMode;
    }
    return null;
  }
  
  void loadFromAttributes(AttributeSet paramAttributeSet, int paramInt)
  {
    TintTypedArray localTintTypedArray = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), paramAttributeSet, R.styleable.ViewBackgroundHelper, paramInt, 0);
    try
    {
      if (localTintTypedArray.hasValue(R.styleable.ViewBackgroundHelper_android_background))
      {
        this.mBackgroundResId = localTintTypedArray.getResourceId(R.styleable.ViewBackgroundHelper_android_background, -1);
        ColorStateList localColorStateList = this.mDrawableManager.getTintList(this.mView.getContext(), this.mBackgroundResId);
        if (localColorStateList != null) {
          setInternalBackgroundTint(localColorStateList);
        }
      }
      if (localTintTypedArray.hasValue(R.styleable.ViewBackgroundHelper_backgroundTint)) {
        ViewCompat.setBackgroundTintList(this.mView, localTintTypedArray.getColorStateList(R.styleable.ViewBackgroundHelper_backgroundTint));
      }
      if (localTintTypedArray.hasValue(R.styleable.ViewBackgroundHelper_backgroundTintMode)) {
        ViewCompat.setBackgroundTintMode(this.mView, DrawableUtils.parseTintMode(localTintTypedArray.getInt(R.styleable.ViewBackgroundHelper_backgroundTintMode, -1), null));
      }
      return;
    }
    finally
    {
      localTintTypedArray.recycle();
    }
  }
  
  void onSetBackgroundDrawable(Drawable paramDrawable)
  {
    this.mBackgroundResId = -1;
    setInternalBackgroundTint(null);
    applySupportBackgroundTint();
  }
  
  void onSetBackgroundResource(int paramInt)
  {
    this.mBackgroundResId = paramInt;
    if (this.mDrawableManager != null) {}
    for (ColorStateList localColorStateList = this.mDrawableManager.getTintList(this.mView.getContext(), paramInt);; localColorStateList = null)
    {
      setInternalBackgroundTint(localColorStateList);
      applySupportBackgroundTint();
      return;
    }
  }
  
  void setInternalBackgroundTint(ColorStateList paramColorStateList)
  {
    if (paramColorStateList != null)
    {
      if (this.mInternalBackgroundTint == null) {
        this.mInternalBackgroundTint = new TintInfo();
      }
      this.mInternalBackgroundTint.mTintList = paramColorStateList;
      this.mInternalBackgroundTint.mHasTintList = true;
    }
    for (;;)
    {
      applySupportBackgroundTint();
      return;
      this.mInternalBackgroundTint = null;
    }
  }
  
  void setSupportBackgroundTintList(ColorStateList paramColorStateList)
  {
    if (this.mBackgroundTint == null) {
      this.mBackgroundTint = new TintInfo();
    }
    this.mBackgroundTint.mTintList = paramColorStateList;
    this.mBackgroundTint.mHasTintList = true;
    applySupportBackgroundTint();
  }
  
  void setSupportBackgroundTintMode(PorterDuff.Mode paramMode)
  {
    if (this.mBackgroundTint == null) {
      this.mBackgroundTint = new TintInfo();
    }
    this.mBackgroundTint.mTintMode = paramMode;
    this.mBackgroundTint.mHasTintMode = true;
    applySupportBackgroundTint();
  }
}
