package android.support.v4.graphics.drawable;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;

@TargetApi(19)
@RequiresApi(19)
class DrawableCompatKitKat
{
  DrawableCompatKitKat() {}
  
  public static int getAlpha(Drawable paramDrawable)
  {
    return paramDrawable.getAlpha();
  }
  
  public static boolean isAutoMirrored(Drawable paramDrawable)
  {
    return paramDrawable.isAutoMirrored();
  }
  
  public static void setAutoMirrored(Drawable paramDrawable, boolean paramBoolean)
  {
    paramDrawable.setAutoMirrored(paramBoolean);
  }
  
  public static Drawable wrapForTinting(Drawable paramDrawable)
  {
    if (!(paramDrawable instanceof TintAwareDrawable)) {
      paramDrawable = new DrawableWrapperKitKat(paramDrawable);
    }
    return paramDrawable;
  }
}
