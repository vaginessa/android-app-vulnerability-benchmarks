package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.support.annotation.RequiresApi;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@TargetApi(19)
@RequiresApi(19)
class NotificationManagerCompatKitKat
{
  private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
  private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
  
  NotificationManagerCompatKitKat() {}
  
  public static boolean areNotificationsEnabled(Context paramContext)
  {
    AppOpsManager localAppOpsManager = (AppOpsManager)paramContext.getSystemService("appops");
    ApplicationInfo localApplicationInfo = paramContext.getApplicationInfo();
    String str = paramContext.getApplicationContext().getPackageName();
    int i = localApplicationInfo.uid;
    try
    {
      Class localClass = Class.forName(AppOpsManager.class.getName());
      Class[] arrayOfClass = new Class[3];
      arrayOfClass[0] = Integer.TYPE;
      arrayOfClass[1] = Integer.TYPE;
      arrayOfClass[2] = String.class;
      Method localMethod = localClass.getMethod("checkOpNoThrow", arrayOfClass);
      int j = ((Integer)localClass.getDeclaredField("OP_POST_NOTIFICATION").get(Integer.class)).intValue();
      Object[] arrayOfObject = new Object[3];
      arrayOfObject[0] = Integer.valueOf(j);
      arrayOfObject[1] = Integer.valueOf(i);
      arrayOfObject[2] = str;
      int k = ((Integer)localMethod.invoke(localAppOpsManager, arrayOfObject)).intValue();
      return k == 0;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      return true;
    }
    catch (RuntimeException localRuntimeException)
    {
      for (;;) {}
    }
    catch (NoSuchFieldException localNoSuchFieldException)
    {
      for (;;) {}
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      for (;;) {}
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      for (;;) {}
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      for (;;) {}
    }
  }
}
