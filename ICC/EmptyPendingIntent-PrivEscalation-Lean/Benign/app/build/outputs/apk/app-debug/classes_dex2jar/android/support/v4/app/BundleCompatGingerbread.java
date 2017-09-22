package android.support.v4.app;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@TargetApi(9)
@RequiresApi(9)
class BundleCompatGingerbread
{
  private static final String TAG = "BundleCompatGingerbread";
  private static Method sGetIBinderMethod;
  private static boolean sGetIBinderMethodFetched;
  private static Method sPutIBinderMethod;
  private static boolean sPutIBinderMethodFetched;
  
  BundleCompatGingerbread() {}
  
  public static IBinder getBinder(Bundle paramBundle, String paramString)
  {
    if (!sGetIBinderMethodFetched) {}
    try
    {
      sGetIBinderMethod = Bundle.class.getMethod("getIBinder", new Class[] { String.class });
      sGetIBinderMethod.setAccessible(true);
      sGetIBinderMethodFetched = true;
      if (sGetIBinderMethod == null) {}
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      for (;;)
      {
        try
        {
          IBinder localIBinder = (IBinder)sGetIBinderMethod.invoke(paramBundle, new Object[] { paramString });
          return localIBinder;
        }
        catch (IllegalAccessException localIllegalAccessException)
        {
          Log.i("BundleCompatGingerbread", "Failed to invoke getIBinder via reflection", localIllegalAccessException);
          sGetIBinderMethod = null;
          return null;
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          continue;
        }
        catch (InvocationTargetException localInvocationTargetException)
        {
          continue;
        }
        localNoSuchMethodException = localNoSuchMethodException;
        Log.i("BundleCompatGingerbread", "Failed to retrieve getIBinder method", localNoSuchMethodException);
      }
    }
  }
  
  public static void putBinder(Bundle paramBundle, String paramString, IBinder paramIBinder)
  {
    if (!sPutIBinderMethodFetched) {}
    try
    {
      sPutIBinderMethod = Bundle.class.getMethod("putIBinder", new Class[] { String.class, IBinder.class });
      sPutIBinderMethod.setAccessible(true);
      sPutIBinderMethodFetched = true;
      if (sPutIBinderMethod == null) {}
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      for (;;)
      {
        try
        {
          sPutIBinderMethod.invoke(paramBundle, new Object[] { paramString, paramIBinder });
          return;
        }
        catch (IllegalAccessException localIllegalAccessException)
        {
          Log.i("BundleCompatGingerbread", "Failed to invoke putIBinder via reflection", localIllegalAccessException);
          sPutIBinderMethod = null;
          return;
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          continue;
        }
        catch (InvocationTargetException localInvocationTargetException)
        {
          continue;
        }
        localNoSuchMethodException = localNoSuchMethodException;
        Log.i("BundleCompatGingerbread", "Failed to retrieve putIBinder method", localNoSuchMethodException);
      }
    }
  }
}
