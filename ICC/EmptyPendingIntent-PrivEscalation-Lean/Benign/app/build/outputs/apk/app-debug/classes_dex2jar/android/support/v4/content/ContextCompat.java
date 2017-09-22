/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.ApplicationInfo
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Environment
 *  android.os.Process
 *  android.util.Log
 *  android.util.TypedValue
 */
package android.support.v4.content;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompatApi21;
import android.support.v4.content.ContextCompatApi23;
import android.support.v4.content.ContextCompatApi24;
import android.support.v4.content.ContextCompatHoneycomb;
import android.support.v4.content.ContextCompatJellybean;
import android.support.v4.content.ContextCompatKitKat;
import android.support.v4.os.BuildCompat;
import android.util.Log;
import android.util.TypedValue;
import java.io.File;

public class ContextCompat {
    private static final String DIR_ANDROID = "Android";
    private static final String DIR_OBB = "obb";
    private static final String TAG = "ContextCompat";
    private static final Object sLock = new Object();
    private static TypedValue sTempValue;

    protected ContextCompat() {
    }

    /*
     * Enabled aggressive block sorting
     */
    private static /* varargs */ File buildPath(File file, String ... arrstring) {
        int n = arrstring.length;
        int n2 = 0;
        File file2 = file;
        while (n2 < n) {
            String string2 = arrstring[n2];
            File file3 = file2 == null ? new File(string2) : (string2 != null ? new File(file2, string2) : file2);
            ++n2;
            file2 = file3;
        }
        return file2;
    }

    public static int checkSelfPermission(@NonNull Context context, @NonNull String string2) {
        if (string2 == null) {
            throw new IllegalArgumentException("permission is null");
        }
        return context.checkPermission(string2, Process.myPid(), Process.myUid());
    }

    public static Context createDeviceProtectedStorageContext(Context context) {
        if (BuildCompat.isAtLeastN()) {
            return ContextCompatApi24.createDeviceProtectedStorageContext(context);
        }
        return null;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static File createFilesDir(File file) {
        synchronized (ContextCompat.class) {
            block4 : {
                if (file.exists()) return file;
                if (file.mkdirs()) return file;
                boolean bl = file.exists();
                if (!bl) break block4;
                return file;
            }
            Log.w((String)"ContextCompat", (String)("Unable to create files subdir " + file.getPath()));
            return null;
        }
    }

    public static File getCodeCacheDir(Context context) {
        if (Build.VERSION.SDK_INT >= 21) {
            return ContextCompatApi21.getCodeCacheDir(context);
        }
        return ContextCompat.createFilesDir(new File(context.getApplicationInfo().dataDir, "code_cache"));
    }

    @ColorInt
    public static final int getColor(Context context, @ColorRes int n) {
        if (Build.VERSION.SDK_INT >= 23) {
            return ContextCompatApi23.getColor(context, n);
        }
        return context.getResources().getColor(n);
    }

    public static final ColorStateList getColorStateList(Context context, @ColorRes int n) {
        if (Build.VERSION.SDK_INT >= 23) {
            return ContextCompatApi23.getColorStateList(context, n);
        }
        return context.getResources().getColorStateList(n);
    }

    public static File getDataDir(Context context) {
        if (BuildCompat.isAtLeastN()) {
            return ContextCompatApi24.getDataDir(context);
        }
        String string2 = context.getApplicationInfo().dataDir;
        if (string2 != null) {
            return new File(string2);
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static final Drawable getDrawable(Context context, @DrawableRes int n) {
        int n2 = Build.VERSION.SDK_INT;
        if (n2 >= 21) {
            return ContextCompatApi21.getDrawable(context, n);
        }
        if (n2 >= 16) {
            return context.getResources().getDrawable(n);
        }
        Object object = sLock;
        synchronized (object) {
            if (sTempValue == null) {
                sTempValue = new TypedValue();
            }
            context.getResources().getValue(n, sTempValue, true);
            int n3 = ContextCompat.sTempValue.resourceId;
            return context.getResources().getDrawable(n3);
        }
    }

    public static File[] getExternalCacheDirs(Context context) {
        if (Build.VERSION.SDK_INT >= 19) {
            return ContextCompatKitKat.getExternalCacheDirs(context);
        }
        File[] arrfile = new File[]{context.getExternalCacheDir()};
        return arrfile;
    }

    public static File[] getExternalFilesDirs(Context context, String string2) {
        if (Build.VERSION.SDK_INT >= 19) {
            return ContextCompatKitKat.getExternalFilesDirs(context, string2);
        }
        File[] arrfile = new File[]{context.getExternalFilesDir(string2)};
        return arrfile;
    }

    public static final File getNoBackupFilesDir(Context context) {
        if (Build.VERSION.SDK_INT >= 21) {
            return ContextCompatApi21.getNoBackupFilesDir(context);
        }
        return ContextCompat.createFilesDir(new File(context.getApplicationInfo().dataDir, "no_backup"));
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static File[] getObbDirs(Context context) {
        File file;
        int n = Build.VERSION.SDK_INT;
        if (n >= 19) {
            return ContextCompatKitKat.getObbDirs(context);
        }
        if (n >= 11) {
            file = ContextCompatHoneycomb.getObbDir(context);
            do {
                return new File[]{file};
                break;
            } while (true);
        }
        File file2 = Environment.getExternalStorageDirectory();
        String[] arrstring = new String[]{"Android", "obb", context.getPackageName()};
        file = ContextCompat.buildPath(file2, arrstring);
        return new File[]{file};
    }

    public static boolean isDeviceProtectedStorage(Context context) {
        if (BuildCompat.isAtLeastN()) {
            return ContextCompatApi24.isDeviceProtectedStorage(context);
        }
        return false;
    }

    public static boolean startActivities(Context context, Intent[] arrintent) {
        return ContextCompat.startActivities(context, arrintent, null);
    }

    public static boolean startActivities(Context context, Intent[] arrintent, Bundle bundle) {
        int n = Build.VERSION.SDK_INT;
        if (n >= 16) {
            ContextCompatJellybean.startActivities(context, arrintent, bundle);
            return true;
        }
        if (n >= 11) {
            ContextCompatHoneycomb.startActivities(context, arrintent);
            return true;
        }
        return false;
    }

    public static void startActivity(Context context, Intent intent, @Nullable Bundle bundle) {
        if (Build.VERSION.SDK_INT >= 16) {
            ContextCompatJellybean.startActivity(context, intent, bundle);
            return;
        }
        context.startActivity(intent);
    }
}

