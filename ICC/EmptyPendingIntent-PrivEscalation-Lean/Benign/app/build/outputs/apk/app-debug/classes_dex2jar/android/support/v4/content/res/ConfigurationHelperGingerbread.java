package android.support.v4.content.res;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;

@TargetApi(9)
@RequiresApi(9)
class ConfigurationHelperGingerbread
{
  ConfigurationHelperGingerbread() {}
  
  static int getDensityDpi(@NonNull Resources paramResources)
  {
    return paramResources.getDisplayMetrics().densityDpi;
  }
  
  static int getScreenHeightDp(@NonNull Resources paramResources)
  {
    DisplayMetrics localDisplayMetrics = paramResources.getDisplayMetrics();
    return (int)(localDisplayMetrics.heightPixels / localDisplayMetrics.density);
  }
  
  static int getScreenWidthDp(@NonNull Resources paramResources)
  {
    DisplayMetrics localDisplayMetrics = paramResources.getDisplayMetrics();
    return (int)(localDisplayMetrics.widthPixels / localDisplayMetrics.density);
  }
  
  static int getSmallestScreenWidthDp(@NonNull Resources paramResources)
  {
    return Math.min(getScreenWidthDp(paramResources), getScreenHeightDp(paramResources));
  }
}
