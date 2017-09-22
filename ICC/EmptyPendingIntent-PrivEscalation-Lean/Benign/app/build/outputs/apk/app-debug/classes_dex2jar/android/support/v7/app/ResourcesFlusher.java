package android.support.v7.app;

import android.content.res.Resources;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.LongSparseArray;
import java.lang.reflect.Field;
import java.util.Map;

class ResourcesFlusher
{
  private static final String TAG = "ResourcesFlusher";
  private static Field sDrawableCacheField;
  private static boolean sDrawableCacheFieldFetched;
  private static Field sResourcesImplField;
  private static boolean sResourcesImplFieldFetched;
  private static Class sThemedResourceCacheClazz;
  private static boolean sThemedResourceCacheClazzFetched;
  private static Field sThemedResourceCache_mUnthemedEntriesField;
  private static boolean sThemedResourceCache_mUnthemedEntriesFieldFetched;
  
  ResourcesFlusher() {}
  
  static boolean flush(@NonNull Resources paramResources)
  {
    int i = Build.VERSION.SDK_INT;
    if (i >= 24) {
      return flushNougats(paramResources);
    }
    if (i >= 23) {
      return flushMarshmallows(paramResources);
    }
    if (i >= 21) {
      return flushLollipops(paramResources);
    }
    return false;
  }
  
  private static boolean flushLollipops(@NonNull Resources paramResources)
  {
    if (!sDrawableCacheFieldFetched) {}
    try
    {
      sDrawableCacheField = Resources.class.getDeclaredField("mDrawableCache");
      sDrawableCacheField.setAccessible(true);
      sDrawableCacheFieldFetched = true;
      if (sDrawableCacheField == null) {}
    }
    catch (NoSuchFieldException localNoSuchFieldException)
    {
      try
      {
        localMap = (Map)sDrawableCacheField.get(paramResources);
        if (localMap != null)
        {
          localMap.clear();
          return true;
          localNoSuchFieldException = localNoSuchFieldException;
          Log.e("ResourcesFlusher", "Could not retrieve Resources#mDrawableCache field", localNoSuchFieldException);
        }
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        for (;;)
        {
          Log.e("ResourcesFlusher", "Could not retrieve value from Resources#mDrawableCache", localIllegalAccessException);
          Map localMap = null;
        }
      }
    }
    return false;
  }
  
  private static boolean flushMarshmallows(@NonNull Resources paramResources)
  {
    boolean bool = true;
    if (!sDrawableCacheFieldFetched) {}
    Object localObject1;
    try
    {
      sDrawableCacheField = Resources.class.getDeclaredField("mDrawableCache");
      sDrawableCacheField.setAccessible(true);
      sDrawableCacheFieldFetched = bool;
      Field localField = sDrawableCacheField;
      localObject1 = null;
      if (localField != null) {}
      try
      {
        Object localObject2 = sDrawableCacheField.get(paramResources);
        localObject1 = localObject2;
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        for (;;)
        {
          Log.e("ResourcesFlusher", "Could not retrieve value from Resources#mDrawableCache", localIllegalAccessException);
          localObject1 = null;
        }
        if (localObject1 == null) {
          break label102;
        }
      }
      if (localObject1 == null) {
        return false;
      }
    }
    catch (NoSuchFieldException localNoSuchFieldException)
    {
      for (;;)
      {
        Log.e("ResourcesFlusher", "Could not retrieve Resources#mDrawableCache field", localNoSuchFieldException);
      }
    }
    if (flushThemedResourcesCache(localObject1)) {}
    for (;;)
    {
      return bool;
      label102:
      bool = false;
    }
  }
  
  private static boolean flushNougats(@NonNull Resources paramResources)
  {
    bool = true;
    if (!sResourcesImplFieldFetched) {}
    do
    {
      try
      {
        sResourcesImplField = Resources.class.getDeclaredField("mResourcesImpl");
        sResourcesImplField.setAccessible(true);
        sResourcesImplFieldFetched = bool;
        if (sResourcesImplField == null) {
          return false;
        }
      }
      catch (NoSuchFieldException localNoSuchFieldException2)
      {
        for (;;)
        {
          Log.e("ResourcesFlusher", "Could not retrieve Resources#mResourcesImpl field", localNoSuchFieldException2);
        }
      }
      try
      {
        Object localObject4 = sResourcesImplField.get(paramResources);
        localObject1 = localObject4;
      }
      catch (IllegalAccessException localIllegalAccessException1)
      {
        try
        {
          sDrawableCacheField = localObject1.getClass().getDeclaredField("mDrawableCache");
          sDrawableCacheField.setAccessible(true);
          sDrawableCacheFieldFetched = bool;
          Field localField = sDrawableCacheField;
          localObject2 = null;
          if (localField == null) {
            break label127;
          }
          try
          {
            Object localObject3 = sDrawableCacheField.get(localObject1);
            localObject2 = localObject3;
          }
          catch (IllegalAccessException localIllegalAccessException2)
          {
            for (;;)
            {
              Log.e("ResourcesFlusher", "Could not retrieve value from ResourcesImpl#mDrawableCache", localIllegalAccessException2);
              localObject2 = null;
              continue;
              bool = false;
            }
          }
          if ((localObject2 == null) || (!flushThemedResourcesCache(localObject2))) {
            break label191;
          }
          return bool;
          localIllegalAccessException1 = localIllegalAccessException1;
          Log.e("ResourcesFlusher", "Could not retrieve value from Resources#mResourcesImpl", localIllegalAccessException1);
          Object localObject1 = null;
        }
        catch (NoSuchFieldException localNoSuchFieldException1)
        {
          for (;;)
          {
            Log.e("ResourcesFlusher", "Could not retrieve ResourcesImpl#mDrawableCache field", localNoSuchFieldException1);
          }
        }
      }
    } while (localObject1 == null);
    if (sDrawableCacheFieldFetched) {}
  }
  
  private static boolean flushThemedResourcesCache(@NonNull Object paramObject)
  {
    if (!sThemedResourceCacheClazzFetched) {}
    try
    {
      sThemedResourceCacheClazz = Class.forName("android.content.res.ThemedResourceCache");
      sThemedResourceCacheClazzFetched = true;
      if (sThemedResourceCacheClazz == null) {
        return false;
      }
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      for (;;)
      {
        Log.e("ResourcesFlusher", "Could not find ThemedResourceCache class", localClassNotFoundException);
      }
      if (!sThemedResourceCache_mUnthemedEntriesFieldFetched) {}
      try
      {
        sThemedResourceCache_mUnthemedEntriesField = sThemedResourceCacheClazz.getDeclaredField("mUnthemedEntries");
        sThemedResourceCache_mUnthemedEntriesField.setAccessible(true);
        sThemedResourceCache_mUnthemedEntriesFieldFetched = true;
        if (sThemedResourceCache_mUnthemedEntriesField == null) {
          return false;
        }
      }
      catch (NoSuchFieldException localNoSuchFieldException)
      {
        for (;;)
        {
          Log.e("ResourcesFlusher", "Could not retrieve ThemedResourceCache#mUnthemedEntries field", localNoSuchFieldException);
        }
        try
        {
          localLongSparseArray = (LongSparseArray)sThemedResourceCache_mUnthemedEntriesField.get(paramObject);
          if (localLongSparseArray != null)
          {
            localLongSparseArray.clear();
            return true;
          }
        }
        catch (IllegalAccessException localIllegalAccessException)
        {
          for (;;)
          {
            Log.e("ResourcesFlusher", "Could not retrieve value from ThemedResourceCache#mUnthemedEntries", localIllegalAccessException);
            LongSparseArray localLongSparseArray = null;
          }
        }
      }
    }
    return false;
  }
}
