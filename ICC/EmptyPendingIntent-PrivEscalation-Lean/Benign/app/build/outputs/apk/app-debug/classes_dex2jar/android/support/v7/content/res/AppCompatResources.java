package android.support.v7.content.res;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import java.util.WeakHashMap;

public final class AppCompatResources
{
  private static final String LOG_TAG = "AppCompatResources";
  private static final ThreadLocal<TypedValue> TL_TYPED_VALUE = new ThreadLocal();
  private static final Object sColorStateCacheLock = new Object();
  private static final WeakHashMap<Context, SparseArray<ColorStateListCacheEntry>> sColorStateCaches = new WeakHashMap(0);
  
  private AppCompatResources() {}
  
  private static void addColorStateListToCache(@NonNull Context paramContext, @ColorRes int paramInt, @NonNull ColorStateList paramColorStateList)
  {
    synchronized (sColorStateCacheLock)
    {
      SparseArray localSparseArray = (SparseArray)sColorStateCaches.get(paramContext);
      if (localSparseArray == null)
      {
        localSparseArray = new SparseArray();
        sColorStateCaches.put(paramContext, localSparseArray);
      }
      localSparseArray.append(paramInt, new ColorStateListCacheEntry(paramColorStateList, paramContext.getResources().getConfiguration()));
      return;
    }
  }
  
  @Nullable
  private static ColorStateList getCachedColorStateList(@NonNull Context paramContext, @ColorRes int paramInt)
  {
    synchronized (sColorStateCacheLock)
    {
      SparseArray localSparseArray = (SparseArray)sColorStateCaches.get(paramContext);
      if ((localSparseArray != null) && (localSparseArray.size() > 0))
      {
        ColorStateListCacheEntry localColorStateListCacheEntry = (ColorStateListCacheEntry)localSparseArray.get(paramInt);
        if (localColorStateListCacheEntry != null)
        {
          if (localColorStateListCacheEntry.configuration.equals(paramContext.getResources().getConfiguration()))
          {
            ColorStateList localColorStateList = localColorStateListCacheEntry.value;
            return localColorStateList;
          }
          localSparseArray.remove(paramInt);
        }
      }
      return null;
    }
  }
  
  public static ColorStateList getColorStateList(@NonNull Context paramContext, @ColorRes int paramInt)
  {
    ColorStateList localColorStateList1;
    if (Build.VERSION.SDK_INT >= 23) {
      localColorStateList1 = paramContext.getColorStateList(paramInt);
    }
    do
    {
      return localColorStateList1;
      localColorStateList1 = getCachedColorStateList(paramContext, paramInt);
    } while (localColorStateList1 != null);
    ColorStateList localColorStateList2 = inflateColorStateList(paramContext, paramInt);
    if (localColorStateList2 != null)
    {
      addColorStateListToCache(paramContext, paramInt, localColorStateList2);
      return localColorStateList2;
    }
    return ContextCompat.getColorStateList(paramContext, paramInt);
  }
  
  @Nullable
  public static Drawable getDrawable(@NonNull Context paramContext, @DrawableRes int paramInt)
  {
    return AppCompatDrawableManager.get().getDrawable(paramContext, paramInt);
  }
  
  @NonNull
  private static TypedValue getTypedValue()
  {
    TypedValue localTypedValue = (TypedValue)TL_TYPED_VALUE.get();
    if (localTypedValue == null)
    {
      localTypedValue = new TypedValue();
      TL_TYPED_VALUE.set(localTypedValue);
    }
    return localTypedValue;
  }
  
  @Nullable
  private static ColorStateList inflateColorStateList(Context paramContext, int paramInt)
  {
    if (isColorInt(paramContext, paramInt)) {
      return null;
    }
    Resources localResources = paramContext.getResources();
    XmlResourceParser localXmlResourceParser = localResources.getXml(paramInt);
    try
    {
      ColorStateList localColorStateList = AppCompatColorStateListInflater.createFromXml(localResources, localXmlResourceParser, paramContext.getTheme());
      return localColorStateList;
    }
    catch (Exception localException)
    {
      Log.e("AppCompatResources", "Failed to inflate ColorStateList, leaving it to the framework", localException);
    }
    return null;
  }
  
  private static boolean isColorInt(@NonNull Context paramContext, @ColorRes int paramInt)
  {
    Resources localResources = paramContext.getResources();
    TypedValue localTypedValue = getTypedValue();
    localResources.getValue(paramInt, localTypedValue, true);
    return (localTypedValue.type >= 28) && (localTypedValue.type <= 31);
  }
  
  private static class ColorStateListCacheEntry
  {
    final Configuration configuration;
    final ColorStateList value;
    
    ColorStateListCacheEntry(@NonNull ColorStateList paramColorStateList, @NonNull Configuration paramConfiguration)
    {
      this.value = paramColorStateList;
      this.configuration = paramConfiguration;
    }
  }
}
