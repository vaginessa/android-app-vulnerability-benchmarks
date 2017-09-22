package android.support.v4.widget;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.widget.TextView;

@TargetApi(17)
@RequiresApi(17)
class TextViewCompatJbMr1
{
  TextViewCompatJbMr1() {}
  
  public static Drawable[] getCompoundDrawablesRelative(@NonNull TextView paramTextView)
  {
    int i = 1;
    if (paramTextView.getLayoutDirection() == i) {}
    for (;;)
    {
      Drawable[] arrayOfDrawable = paramTextView.getCompoundDrawables();
      if (i != 0)
      {
        Drawable localDrawable1 = arrayOfDrawable[2];
        Drawable localDrawable2 = arrayOfDrawable[0];
        arrayOfDrawable[0] = localDrawable1;
        arrayOfDrawable[2] = localDrawable2;
      }
      return arrayOfDrawable;
      i = 0;
    }
  }
  
  public static void setCompoundDrawablesRelative(@NonNull TextView paramTextView, @Nullable Drawable paramDrawable1, @Nullable Drawable paramDrawable2, @Nullable Drawable paramDrawable3, @Nullable Drawable paramDrawable4)
  {
    int i = 1;
    Drawable localDrawable;
    if (paramTextView.getLayoutDirection() == i)
    {
      if (i == 0) {
        break label42;
      }
      localDrawable = paramDrawable3;
      label20:
      if (i == 0) {
        break label48;
      }
    }
    for (;;)
    {
      paramTextView.setCompoundDrawables(localDrawable, paramDrawable2, paramDrawable1, paramDrawable4);
      return;
      i = 0;
      break;
      label42:
      localDrawable = paramDrawable1;
      break label20;
      label48:
      paramDrawable1 = paramDrawable3;
    }
  }
  
  public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView paramTextView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = 1;
    int j;
    if (paramTextView.getLayoutDirection() == i)
    {
      if (i == 0) {
        break label42;
      }
      j = paramInt3;
      label20:
      if (i == 0) {
        break label48;
      }
    }
    for (;;)
    {
      paramTextView.setCompoundDrawablesWithIntrinsicBounds(j, paramInt2, paramInt1, paramInt4);
      return;
      i = 0;
      break;
      label42:
      j = paramInt1;
      break label20;
      label48:
      paramInt1 = paramInt3;
    }
  }
  
  public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView paramTextView, @Nullable Drawable paramDrawable1, @Nullable Drawable paramDrawable2, @Nullable Drawable paramDrawable3, @Nullable Drawable paramDrawable4)
  {
    int i = 1;
    Drawable localDrawable;
    if (paramTextView.getLayoutDirection() == i)
    {
      if (i == 0) {
        break label42;
      }
      localDrawable = paramDrawable3;
      label20:
      if (i == 0) {
        break label48;
      }
    }
    for (;;)
    {
      paramTextView.setCompoundDrawablesWithIntrinsicBounds(localDrawable, paramDrawable2, paramDrawable1, paramDrawable4);
      return;
      i = 0;
      break;
      label42:
      localDrawable = paramDrawable1;
      break label20;
      label48:
      paramDrawable1 = paramDrawable3;
    }
  }
}
