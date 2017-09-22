package android.support.v7.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleRes;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.appcompat.R.styleable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.PopupWindow;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

class AppCompatPopupWindow
  extends PopupWindow
{
  private static final boolean COMPAT_OVERLAP_ANCHOR = false;
  private static final String TAG = "AppCompatPopupWindow";
  private boolean mOverlapAnchor;
  
  static
  {
    if (Build.VERSION.SDK_INT < 21) {}
    for (boolean bool = true;; bool = false)
    {
      COMPAT_OVERLAP_ANCHOR = bool;
      return;
    }
  }
  
  public AppCompatPopupWindow(@NonNull Context paramContext, @Nullable AttributeSet paramAttributeSet, @AttrRes int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  @TargetApi(11)
  public AppCompatPopupWindow(@NonNull Context paramContext, @Nullable AttributeSet paramAttributeSet, @AttrRes int paramInt1, @StyleRes int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    init(paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  private void init(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    TintTypedArray localTintTypedArray = TintTypedArray.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.PopupWindow, paramInt1, paramInt2);
    if (localTintTypedArray.hasValue(R.styleable.PopupWindow_overlapAnchor)) {
      setSupportOverlapAnchor(localTintTypedArray.getBoolean(R.styleable.PopupWindow_overlapAnchor, false));
    }
    setBackgroundDrawable(localTintTypedArray.getDrawable(R.styleable.PopupWindow_android_popupBackground));
    int i = Build.VERSION.SDK_INT;
    if ((paramInt2 != 0) && (i < 11) && (localTintTypedArray.hasValue(R.styleable.PopupWindow_android_popupAnimationStyle))) {
      setAnimationStyle(localTintTypedArray.getResourceId(R.styleable.PopupWindow_android_popupAnimationStyle, -1));
    }
    localTintTypedArray.recycle();
    if (Build.VERSION.SDK_INT < 14) {
      wrapOnScrollChangedListener(this);
    }
  }
  
  private static void wrapOnScrollChangedListener(final PopupWindow paramPopupWindow)
  {
    try
    {
      Field localField1 = PopupWindow.class.getDeclaredField("mAnchor");
      localField1.setAccessible(true);
      Field localField2 = PopupWindow.class.getDeclaredField("mOnScrollChangedListener");
      localField2.setAccessible(true);
      localField2.set(paramPopupWindow, new ViewTreeObserver.OnScrollChangedListener()
      {
        public void onScrollChanged()
        {
          try
          {
            WeakReference localWeakReference = (WeakReference)this.val$fieldAnchor.get(paramPopupWindow);
            if (localWeakReference != null)
            {
              if (localWeakReference.get() == null) {
                return;
              }
              this.val$originalListener.onScrollChanged();
              return;
            }
          }
          catch (IllegalAccessException localIllegalAccessException) {}
        }
      });
      return;
    }
    catch (Exception localException)
    {
      Log.d("AppCompatPopupWindow", "Exception while installing workaround OnScrollChangedListener", localException);
    }
  }
  
  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public boolean getSupportOverlapAnchor()
  {
    if (COMPAT_OVERLAP_ANCHOR) {
      return this.mOverlapAnchor;
    }
    return PopupWindowCompat.getOverlapAnchor(this);
  }
  
  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public void setSupportOverlapAnchor(boolean paramBoolean)
  {
    if (COMPAT_OVERLAP_ANCHOR)
    {
      this.mOverlapAnchor = paramBoolean;
      return;
    }
    PopupWindowCompat.setOverlapAnchor(this, paramBoolean);
  }
  
  public void showAsDropDown(View paramView, int paramInt1, int paramInt2)
  {
    if ((COMPAT_OVERLAP_ANCHOR) && (this.mOverlapAnchor)) {
      paramInt2 -= paramView.getHeight();
    }
    super.showAsDropDown(paramView, paramInt1, paramInt2);
  }
  
  @TargetApi(19)
  public void showAsDropDown(View paramView, int paramInt1, int paramInt2, int paramInt3)
  {
    if ((COMPAT_OVERLAP_ANCHOR) && (this.mOverlapAnchor)) {
      paramInt2 -= paramView.getHeight();
    }
    super.showAsDropDown(paramView, paramInt1, paramInt2, paramInt3);
  }
  
  public void update(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if ((COMPAT_OVERLAP_ANCHOR) && (this.mOverlapAnchor)) {
      paramInt2 -= paramView.getHeight();
    }
    super.update(paramView, paramInt1, paramInt2, paramInt3, paramInt4);
  }
}
