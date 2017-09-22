package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;

@TargetApi(18)
@RequiresApi(18)
class ActionBarDrawerToggleJellybeanMR2
{
  private static final String TAG = "ActionBarDrawerToggleImplJellybeanMR2";
  private static final int[] THEME_ATTRS = { 16843531 };
  
  ActionBarDrawerToggleJellybeanMR2() {}
  
  public static Drawable getThemeUpIndicator(Activity paramActivity)
  {
    ActionBar localActionBar = paramActivity.getActionBar();
    if (localActionBar != null) {}
    for (Object localObject = localActionBar.getThemedContext();; localObject = paramActivity)
    {
      TypedArray localTypedArray = ((Context)localObject).obtainStyledAttributes(null, THEME_ATTRS, 16843470, 0);
      Drawable localDrawable = localTypedArray.getDrawable(0);
      localTypedArray.recycle();
      return localDrawable;
    }
  }
  
  public static Object setActionBarDescription(Object paramObject, Activity paramActivity, int paramInt)
  {
    ActionBar localActionBar = paramActivity.getActionBar();
    if (localActionBar != null) {
      localActionBar.setHomeActionContentDescription(paramInt);
    }
    return paramObject;
  }
  
  public static Object setActionBarUpIndicator(Object paramObject, Activity paramActivity, Drawable paramDrawable, int paramInt)
  {
    ActionBar localActionBar = paramActivity.getActionBar();
    if (localActionBar != null)
    {
      localActionBar.setHomeAsUpIndicator(paramDrawable);
      localActionBar.setHomeActionContentDescription(paramInt);
    }
    return paramObject;
  }
}
