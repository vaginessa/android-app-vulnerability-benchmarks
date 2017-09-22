package android.support.v4.graphics.drawable;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.lang.reflect.Method;

@TargetApi(17)
@RequiresApi(17)
class DrawableCompatJellybeanMr1
{
  private static final String TAG = "DrawableCompatJellybeanMr1";
  private static Method sGetLayoutDirectionMethod;
  private static boolean sGetLayoutDirectionMethodFetched;
  private static Method sSetLayoutDirectionMethod;
  private static boolean sSetLayoutDirectionMethodFetched;
  
  DrawableCompatJellybeanMr1() {}
  
  public static int getLayoutDirection(Drawable paramDrawable)
  {
    if (!sGetLayoutDirectionMethodFetched) {}
    try
    {
      sGetLayoutDirectionMethod = Drawable.class.getDeclaredMethod("getLayoutDirection", new Class[0]);
      sGetLayoutDirectionMethod.setAccessible(true);
      sGetLayoutDirectionMethodFetched = true;
      if (sGetLayoutDirectionMethod == null) {}
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      for (;;)
      {
        try
        {
          int i = ((Integer)sGetLayoutDirectionMethod.invoke(paramDrawable, new Object[0])).intValue();
          return i;
        }
        catch (Exception localException)
        {
          Log.i("DrawableCompatJellybeanMr1", "Failed to invoke getLayoutDirection() via reflection", localException);
          sGetLayoutDirectionMethod = null;
        }
        localNoSuchMethodException = localNoSuchMethodException;
        Log.i("DrawableCompatJellybeanMr1", "Failed to retrieve getLayoutDirection() method", localNoSuchMethodException);
      }
    }
    return -1;
  }
  
  public static boolean setLayoutDirection(Drawable paramDrawable, int paramInt)
  {
    if (!sSetLayoutDirectionMethodFetched) {}
    try
    {
      Class[] arrayOfClass = new Class[1];
      arrayOfClass[0] = Integer.TYPE;
      sSetLayoutDirectionMethod = Drawable.class.getDeclaredMethod("setLayoutDirection", arrayOfClass);
      sSetLayoutDirectionMethod.setAccessible(true);
      sSetLayoutDirectionMethodFetched = true;
      if (sSetLayoutDirectionMethod == null) {}
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      for (;;)
      {
        try
        {
          Method localMethod = sSetLayoutDirectionMethod;
          Object[] arrayOfObject = new Object[1];
          arrayOfObject[0] = Integer.valueOf(paramInt);
          localMethod.invoke(paramDrawable, arrayOfObject);
          return true;
        }
        catch (Exception localException)
        {
          Log.i("DrawableCompatJellybeanMr1", "Failed to invoke setLayoutDirection(int) via reflection", localException);
          sSetLayoutDirectionMethod = null;
        }
        localNoSuchMethodException = localNoSuchMethodException;
        Log.i("DrawableCompatJellybeanMr1", "Failed to retrieve setLayoutDirection(int) method", localNoSuchMethodException);
      }
    }
    return false;
  }
}
