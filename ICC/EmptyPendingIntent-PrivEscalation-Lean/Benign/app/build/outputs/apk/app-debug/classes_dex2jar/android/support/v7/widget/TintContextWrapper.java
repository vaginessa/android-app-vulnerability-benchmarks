package android.support.v7.widget;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

@RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
public class TintContextWrapper
  extends ContextWrapper
{
  private static final Object CACHE_LOCK = new Object();
  private static ArrayList<WeakReference<TintContextWrapper>> sCache;
  private final Resources mResources;
  private final Resources.Theme mTheme;
  
  private TintContextWrapper(@NonNull Context paramContext)
  {
    super(paramContext);
    if (VectorEnabledTintResources.shouldBeUsed())
    {
      this.mResources = new VectorEnabledTintResources(this, paramContext.getResources());
      this.mTheme = this.mResources.newTheme();
      this.mTheme.setTo(paramContext.getTheme());
      return;
    }
    this.mResources = new TintResources(this, paramContext.getResources());
    this.mTheme = null;
  }
  
  private static boolean shouldWrap(@NonNull Context paramContext)
  {
    if (((paramContext instanceof TintContextWrapper)) || ((paramContext.getResources() instanceof TintResources)) || ((paramContext.getResources() instanceof VectorEnabledTintResources))) {}
    while ((Build.VERSION.SDK_INT >= 21) && (!VectorEnabledTintResources.shouldBeUsed())) {
      return false;
    }
    return true;
  }
  
  public static Context wrap(@NonNull Context paramContext)
  {
    if (shouldWrap(paramContext)) {}
    for (;;)
    {
      int i;
      int j;
      synchronized (CACHE_LOCK)
      {
        if (sCache == null)
        {
          sCache = new ArrayList();
          TintContextWrapper localTintContextWrapper2 = new TintContextWrapper(paramContext);
          sCache.add(new WeakReference(localTintContextWrapper2));
          return localTintContextWrapper2;
        }
        i = -1 + sCache.size();
        if (i >= 0)
        {
          WeakReference localWeakReference1 = (WeakReference)sCache.get(i);
          if ((localWeakReference1 != null) && (localWeakReference1.get() != null)) {
            break label190;
          }
          sCache.remove(i);
          break label190;
        }
        j = -1 + sCache.size();
        if (j < 0) {
          continue;
        }
        WeakReference localWeakReference2 = (WeakReference)sCache.get(j);
        if (localWeakReference2 != null)
        {
          localTintContextWrapper1 = (TintContextWrapper)localWeakReference2.get();
          if ((localTintContextWrapper1 == null) || (localTintContextWrapper1.getBaseContext() != paramContext)) {
            break label182;
          }
          return localTintContextWrapper1;
        }
      }
      TintContextWrapper localTintContextWrapper1 = null;
      continue;
      label182:
      j--;
      continue;
      return paramContext;
      label190:
      i--;
    }
  }
  
  public AssetManager getAssets()
  {
    return this.mResources.getAssets();
  }
  
  public Resources getResources()
  {
    return this.mResources;
  }
  
  public Resources.Theme getTheme()
  {
    if (this.mTheme == null) {
      return super.getTheme();
    }
    return this.mTheme;
  }
  
  public void setTheme(int paramInt)
  {
    if (this.mTheme == null)
    {
      super.setTheme(paramInt);
      return;
    }
    this.mTheme.applyStyle(paramInt, true);
  }
}
