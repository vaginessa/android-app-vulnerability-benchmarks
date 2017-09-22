package android.support.v4.view;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.View.OnApplyWindowInsetsListener;
import android.view.ViewParent;
import android.view.WindowInsets;

@TargetApi(21)
@RequiresApi(21)
class ViewCompatLollipop
{
  private static ThreadLocal<Rect> sThreadLocalRect;
  
  ViewCompatLollipop() {}
  
  public static Object dispatchApplyWindowInsets(View paramView, Object paramObject)
  {
    WindowInsets localWindowInsets1 = (WindowInsets)paramObject;
    WindowInsets localWindowInsets2 = paramView.dispatchApplyWindowInsets(localWindowInsets1);
    if (localWindowInsets2 != localWindowInsets1) {
      paramObject = new WindowInsets(localWindowInsets2);
    }
    return paramObject;
  }
  
  public static boolean dispatchNestedFling(View paramView, float paramFloat1, float paramFloat2, boolean paramBoolean)
  {
    return paramView.dispatchNestedFling(paramFloat1, paramFloat2, paramBoolean);
  }
  
  public static boolean dispatchNestedPreFling(View paramView, float paramFloat1, float paramFloat2)
  {
    return paramView.dispatchNestedPreFling(paramFloat1, paramFloat2);
  }
  
  public static boolean dispatchNestedPreScroll(View paramView, int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    return paramView.dispatchNestedPreScroll(paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfInt2);
  }
  
  public static boolean dispatchNestedScroll(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt)
  {
    return paramView.dispatchNestedScroll(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt);
  }
  
  static ColorStateList getBackgroundTintList(View paramView)
  {
    return paramView.getBackgroundTintList();
  }
  
  static PorterDuff.Mode getBackgroundTintMode(View paramView)
  {
    return paramView.getBackgroundTintMode();
  }
  
  public static float getElevation(View paramView)
  {
    return paramView.getElevation();
  }
  
  private static Rect getEmptyTempRect()
  {
    if (sThreadLocalRect == null) {
      sThreadLocalRect = new ThreadLocal();
    }
    Rect localRect = (Rect)sThreadLocalRect.get();
    if (localRect == null)
    {
      localRect = new Rect();
      sThreadLocalRect.set(localRect);
    }
    localRect.setEmpty();
    return localRect;
  }
  
  public static String getTransitionName(View paramView)
  {
    return paramView.getTransitionName();
  }
  
  public static float getTranslationZ(View paramView)
  {
    return paramView.getTranslationZ();
  }
  
  public static float getZ(View paramView)
  {
    return paramView.getZ();
  }
  
  public static boolean hasNestedScrollingParent(View paramView)
  {
    return paramView.hasNestedScrollingParent();
  }
  
  public static boolean isImportantForAccessibility(View paramView)
  {
    return paramView.isImportantForAccessibility();
  }
  
  public static boolean isNestedScrollingEnabled(View paramView)
  {
    return paramView.isNestedScrollingEnabled();
  }
  
  static void offsetLeftAndRight(View paramView, int paramInt)
  {
    Rect localRect = getEmptyTempRect();
    ViewParent localViewParent = paramView.getParent();
    boolean bool = localViewParent instanceof View;
    int i = 0;
    if (bool)
    {
      View localView = (View)localViewParent;
      localRect.set(localView.getLeft(), localView.getTop(), localView.getRight(), localView.getBottom());
      if (localRect.intersects(paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom())) {
        break label121;
      }
    }
    label121:
    for (i = 1;; i = 0)
    {
      ViewCompatHC.offsetLeftAndRight(paramView, paramInt);
      if ((i != 0) && (localRect.intersect(paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom()))) {
        ((View)localViewParent).invalidate(localRect);
      }
      return;
    }
  }
  
  static void offsetTopAndBottom(View paramView, int paramInt)
  {
    Rect localRect = getEmptyTempRect();
    ViewParent localViewParent = paramView.getParent();
    boolean bool = localViewParent instanceof View;
    int i = 0;
    if (bool)
    {
      View localView = (View)localViewParent;
      localRect.set(localView.getLeft(), localView.getTop(), localView.getRight(), localView.getBottom());
      if (localRect.intersects(paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom())) {
        break label121;
      }
    }
    label121:
    for (i = 1;; i = 0)
    {
      ViewCompatHC.offsetTopAndBottom(paramView, paramInt);
      if ((i != 0) && (localRect.intersect(paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom()))) {
        ((View)localViewParent).invalidate(localRect);
      }
      return;
    }
  }
  
  public static Object onApplyWindowInsets(View paramView, Object paramObject)
  {
    WindowInsets localWindowInsets1 = (WindowInsets)paramObject;
    WindowInsets localWindowInsets2 = paramView.onApplyWindowInsets(localWindowInsets1);
    if (localWindowInsets2 != localWindowInsets1) {
      paramObject = new WindowInsets(localWindowInsets2);
    }
    return paramObject;
  }
  
  public static void requestApplyInsets(View paramView)
  {
    paramView.requestApplyInsets();
  }
  
  static void setBackgroundTintList(View paramView, ColorStateList paramColorStateList)
  {
    paramView.setBackgroundTintList(paramColorStateList);
    Drawable localDrawable;
    if (Build.VERSION.SDK_INT == 21)
    {
      localDrawable = paramView.getBackground();
      if ((paramView.getBackgroundTintList() == null) || (paramView.getBackgroundTintMode() == null)) {
        break label64;
      }
    }
    label64:
    for (int i = 1;; i = 0)
    {
      if ((localDrawable != null) && (i != 0))
      {
        if (localDrawable.isStateful()) {
          localDrawable.setState(paramView.getDrawableState());
        }
        paramView.setBackground(localDrawable);
      }
      return;
    }
  }
  
  static void setBackgroundTintMode(View paramView, PorterDuff.Mode paramMode)
  {
    paramView.setBackgroundTintMode(paramMode);
    Drawable localDrawable;
    if (Build.VERSION.SDK_INT == 21)
    {
      localDrawable = paramView.getBackground();
      if ((paramView.getBackgroundTintList() == null) || (paramView.getBackgroundTintMode() == null)) {
        break label64;
      }
    }
    label64:
    for (int i = 1;; i = 0)
    {
      if ((localDrawable != null) && (i != 0))
      {
        if (localDrawable.isStateful()) {
          localDrawable.setState(paramView.getDrawableState());
        }
        paramView.setBackground(localDrawable);
      }
      return;
    }
  }
  
  public static void setElevation(View paramView, float paramFloat)
  {
    paramView.setElevation(paramFloat);
  }
  
  public static void setNestedScrollingEnabled(View paramView, boolean paramBoolean)
  {
    paramView.setNestedScrollingEnabled(paramBoolean);
  }
  
  public static void setOnApplyWindowInsetsListener(View paramView, OnApplyWindowInsetsListenerBridge paramOnApplyWindowInsetsListenerBridge)
  {
    if (paramOnApplyWindowInsetsListenerBridge == null)
    {
      paramView.setOnApplyWindowInsetsListener(null);
      return;
    }
    paramView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener()
    {
      public WindowInsets onApplyWindowInsets(View paramAnonymousView, WindowInsets paramAnonymousWindowInsets)
      {
        return (WindowInsets)this.val$bridge.onApplyWindowInsets(paramAnonymousView, paramAnonymousWindowInsets);
      }
    });
  }
  
  public static void setTransitionName(View paramView, String paramString)
  {
    paramView.setTransitionName(paramString);
  }
  
  public static void setTranslationZ(View paramView, float paramFloat)
  {
    paramView.setTranslationZ(paramFloat);
  }
  
  public static void setZ(View paramView, float paramFloat)
  {
    paramView.setZ(paramFloat);
  }
  
  public static boolean startNestedScroll(View paramView, int paramInt)
  {
    return paramView.startNestedScroll(paramInt);
  }
  
  public static void stopNestedScroll(View paramView)
  {
    paramView.stopNestedScroll();
  }
  
  public static abstract interface OnApplyWindowInsetsListenerBridge
  {
    public abstract Object onApplyWindowInsets(View paramView, Object paramObject);
  }
}
