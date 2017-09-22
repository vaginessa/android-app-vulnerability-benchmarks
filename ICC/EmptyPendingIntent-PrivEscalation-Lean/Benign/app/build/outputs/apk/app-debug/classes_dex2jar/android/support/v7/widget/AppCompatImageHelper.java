package android.support.v7.widget;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build.VERSION;
import android.support.annotation.RestrictTo;
import android.support.v7.appcompat.R.styleable;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.widget.ImageView;

@RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
public class AppCompatImageHelper
{
  private final ImageView mView;
  
  public AppCompatImageHelper(ImageView paramImageView)
  {
    this.mView = paramImageView;
  }
  
  boolean hasOverlappingRendering()
  {
    Drawable localDrawable = this.mView.getBackground();
    return (Build.VERSION.SDK_INT < 21) || (!(localDrawable instanceof RippleDrawable));
  }
  
  public void loadFromAttributes(AttributeSet paramAttributeSet, int paramInt)
  {
    TintTypedArray localTintTypedArray = null;
    try
    {
      Drawable localDrawable = this.mView.getDrawable();
      localTintTypedArray = null;
      if (localDrawable == null)
      {
        localTintTypedArray = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), paramAttributeSet, R.styleable.AppCompatImageView, paramInt, 0);
        int i = localTintTypedArray.getResourceId(R.styleable.AppCompatImageView_srcCompat, -1);
        if (i != -1)
        {
          localDrawable = AppCompatResources.getDrawable(this.mView.getContext(), i);
          if (localDrawable != null) {
            this.mView.setImageDrawable(localDrawable);
          }
        }
      }
      if (localDrawable != null) {
        DrawableUtils.fixDrawable(localDrawable);
      }
      return;
    }
    finally
    {
      if (localTintTypedArray != null) {
        localTintTypedArray.recycle();
      }
    }
  }
  
  public void setImageResource(int paramInt)
  {
    if (paramInt != 0)
    {
      Drawable localDrawable = AppCompatResources.getDrawable(this.mView.getContext(), paramInt);
      if (localDrawable != null) {
        DrawableUtils.fixDrawable(localDrawable);
      }
      this.mView.setImageDrawable(localDrawable);
      return;
    }
    this.mView.setImageDrawable(null);
  }
}
