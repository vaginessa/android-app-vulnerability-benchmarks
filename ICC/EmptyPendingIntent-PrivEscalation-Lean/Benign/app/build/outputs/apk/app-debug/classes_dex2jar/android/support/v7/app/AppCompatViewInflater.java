/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.ContextWrapper
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.view.InflateException
 *  android.view.View
 *  android.view.View$OnClickListener
 */
package android.support.v7.app;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.TintContextWrapper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.View;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

class AppCompatViewInflater {
    private static final String LOG_TAG = "AppCompatViewInflater";
    private static final String[] sClassPrefixList;
    private static final Map<String, Constructor<? extends View>> sConstructorMap;
    private static final Class<?>[] sConstructorSignature;
    private static final int[] sOnClickAttrs;
    private final Object[] mConstructorArgs = new Object[2];

    static {
        sConstructorSignature = new Class[]{Context.class, AttributeSet.class};
        sOnClickAttrs = new int[]{16843375};
        sClassPrefixList = new String[]{"android.widget.", "android.view.", "android.webkit."};
        sConstructorMap = new ArrayMap<String, Constructor<? extends View>>();
    }

    AppCompatViewInflater() {
    }

    private void checkOnClickListener(View view, AttributeSet attributeSet) {
        Context context = view.getContext();
        if (!(context instanceof ContextWrapper) || Build.VERSION.SDK_INT >= 15 && !ViewCompat.hasOnClickListeners(view)) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, sOnClickAttrs);
        String string2 = typedArray.getString(0);
        if (string2 != null) {
            view.setOnClickListener((View.OnClickListener)new DeclaredOnClickListener(view, string2));
        }
        typedArray.recycle();
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private View createView(Context var1_1, String var2_2, String var3_3) throws ClassNotFoundException, InflateException {
        var4_4 = AppCompatViewInflater.sConstructorMap.get(var2_2);
        if (var4_4 != null) ** GOTO lbl8
        try {
            var7_7 = var1_1.getClassLoader();
            var8_8 = var3_3 != null ? var3_3 + var2_2 : var2_2;
            var4_5 = var7_7.loadClass(var8_8).asSubclass(View.class).getConstructor(AppCompatViewInflater.sConstructorSignature);
            AppCompatViewInflater.sConstructorMap.put(var2_2, var4_5);
lbl8: // 2 sources:
            var4_6.setAccessible(true);
            return (View)var4_6.newInstance(this.mConstructorArgs);
        }
        catch (Exception var5_10) {
            return null;
        }
    }

    private View createViewFromTag(Context context, String string2, AttributeSet attributeSet) {
        block8 : {
            if (string2.equals("view")) {
                string2 = attributeSet.getAttributeValue(null, "class");
            }
            this.mConstructorArgs[0] = context;
            this.mConstructorArgs[1] = attributeSet;
            if (-1 != string2.indexOf(46)) break block8;
            int n = 0;
            do {
                block9 : {
                    if (n >= sClassPrefixList.length) break;
                    View view = this.createView(context, string2, sClassPrefixList[n]);
                    if (view == null) break block9;
                    this.mConstructorArgs[0] = null;
                    this.mConstructorArgs[1] = null;
                    return view;
                }
                ++n;
            } while (true);
            this.mConstructorArgs[0] = null;
            this.mConstructorArgs[1] = null;
            return null;
        }
        try {
            View view = this.createView(context, string2, null);
            return view;
        }
        catch (Exception var5_7) {
            return null;
        }
        catch (Throwable var4_8) {
            throw var4_8;
        }
        finally {
            this.mConstructorArgs[0] = null;
            this.mConstructorArgs[1] = null;
        }
    }

    private static Context themifyContext(Context object, AttributeSet attributeSet, boolean bl, boolean bl2) {
        TypedArray typedArray = object.obtainStyledAttributes(attributeSet, R.styleable.View, 0, 0);
        int n = 0;
        if (bl) {
            n = typedArray.getResourceId(R.styleable.View_android_theme, 0);
        }
        if (bl2 && n == 0 && (n = typedArray.getResourceId(R.styleable.View_theme, 0)) != 0) {
            Log.i((String)"AppCompatViewInflater", (String)"app:theme is now deprecated. Please move to using android:theme instead.");
        }
        typedArray.recycle();
        if (!(n == 0 || object instanceof ContextThemeWrapper && ((ContextThemeWrapper)((Object)object)).getThemeResId() == n)) {
            object = new ContextThemeWrapper((Context)object, n);
        }
        return object;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public final View createView(View var1_1, String var2_2, @NonNull Context var3_3, @NonNull AttributeSet var4_4, boolean var5_5, boolean var6_6, boolean var7_7, boolean var8_8) {
        var9_9 = var3_3;
        if (var5_5 && var1_1 != null) {
            var3_3 = var1_1.getContext();
        }
        if (var6_6 || var7_7) {
            var3_3 = AppCompatViewInflater.themifyContext(var3_3, var4_4, var6_6, var7_7);
        }
        if (var8_8) {
            var3_3 = TintContextWrapper.wrap(var3_3);
        }
        var10_10 = -1;
        switch (var2_2.hashCode()) {
            case -938935918: {
                if (var2_2.equals("TextView")) {
                    var10_10 = 0;
                    ** break;
                }
                ** GOTO lbl68
            }
            case 1125864064: {
                if (var2_2.equals("ImageView")) {
                    var10_10 = 1;
                    ** break;
                }
                ** GOTO lbl68
            }
            case 2001146706: {
                if (var2_2.equals("Button")) {
                    var10_10 = 2;
                    ** break;
                }
                ** GOTO lbl68
            }
            case 1666676343: {
                if (var2_2.equals("EditText")) {
                    var10_10 = 3;
                    ** break;
                }
                ** GOTO lbl68
            }
            case -339785223: {
                if (var2_2.equals("Spinner")) {
                    var10_10 = 4;
                    ** break;
                }
                ** GOTO lbl68
            }
            case -937446323: {
                if (var2_2.equals("ImageButton")) {
                    var10_10 = 5;
                    ** break;
                }
                ** GOTO lbl68
            }
            case 1601505219: {
                if (var2_2.equals("CheckBox")) {
                    var10_10 = 6;
                    ** break;
                }
                ** GOTO lbl68
            }
            case 776382189: {
                if (var2_2.equals("RadioButton")) {
                    var10_10 = 7;
                    ** break;
                }
                ** GOTO lbl68
            }
            case -1455429095: {
                if (var2_2.equals("CheckedTextView")) {
                    var10_10 = 8;
                    ** break;
                }
                ** GOTO lbl68
            }
            case 1413872058: {
                if (var2_2.equals("AutoCompleteTextView")) {
                    var10_10 = 9;
                    ** break;
                }
                ** GOTO lbl68
            }
            case -1346021293: {
                if (var2_2.equals("MultiAutoCompleteTextView")) {
                    var10_10 = 10;
                    ** break;
                }
                ** GOTO lbl68
            }
            case -1946472170: {
                if (var2_2.equals("RatingBar")) {
                    var10_10 = 11;
                }
            }
lbl68: // 26 sources:
            default: {
                ** GOTO lbl73
            }
            case -658531749: 
        }
        if (var2_2.equals("SeekBar")) {
            var10_10 = 12;
        }
lbl73: // 4 sources:
        var11_11 = null;
        switch (var10_10) {
            case 0: {
                var11_11 = new AppCompatTextView(var3_3, var4_4);
                ** break;
            }
            case 1: {
                var11_11 = new AppCompatImageView(var3_3, var4_4);
                ** break;
            }
            case 2: {
                var11_11 = new AppCompatButton(var3_3, var4_4);
                ** break;
            }
            case 3: {
                var11_11 = new AppCompatEditText(var3_3, var4_4);
                ** break;
            }
            case 4: {
                var11_11 = new AppCompatSpinner(var3_3, var4_4);
                ** break;
            }
            case 5: {
                var11_11 = new AppCompatImageButton(var3_3, var4_4);
                ** break;
            }
            case 6: {
                var11_11 = new AppCompatCheckBox(var3_3, var4_4);
                ** break;
            }
            case 7: {
                var11_11 = new AppCompatRadioButton(var3_3, var4_4);
                ** break;
            }
            case 8: {
                var11_11 = new AppCompatCheckedTextView(var3_3, var4_4);
                ** break;
            }
            case 9: {
                var11_11 = new AppCompatAutoCompleteTextView(var3_3, var4_4);
                ** break;
            }
            case 10: {
                var11_11 = new AppCompatMultiAutoCompleteTextView(var3_3, var4_4);
                ** break;
            }
            case 11: {
                var11_11 = new AppCompatRatingBar(var3_3, var4_4);
            }
lbl110: // 13 sources:
            default: {
                ** GOTO lbl114
            }
            case 12: 
        }
        var11_11 = new AppCompatSeekBar(var3_3, var4_4);
lbl114: // 2 sources:
        if (var11_11 == null && var9_9 != var3_3) {
            var11_11 = this.createViewFromTag(var3_3, var2_2, var4_4);
        }
        if (var11_11 == null) return var11_11;
        this.checkOnClickListener((View)var11_11, var4_4);
        return var11_11;
    }

    private static class DeclaredOnClickListener
    implements View.OnClickListener {
        private final View mHostView;
        private final String mMethodName;
        private Context mResolvedContext;
        private Method mResolvedMethod;

        public DeclaredOnClickListener(@NonNull View view, @NonNull String string2) {
            this.mHostView = view;
            this.mMethodName = string2;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @NonNull
        private void resolveMethod(@Nullable Context context, @NonNull String string2) {
            String string3;
            while (context != null) {
                try {
                    Method method;
                    if (!context.isRestricted() && (method = context.getClass().getMethod(this.mMethodName, View.class)) != null) {
                        this.mResolvedMethod = method;
                        this.mResolvedContext = context;
                        return;
                    }
                }
                catch (NoSuchMethodException var5_3) {
                    // empty catch block
                }
                if (context instanceof ContextWrapper) {
                    context = ((ContextWrapper)context).getBaseContext();
                    continue;
                }
                context = null;
            }
            int n = this.mHostView.getId();
            if (n == -1) {
                string3 = "";
                throw new IllegalStateException("Could not find method " + this.mMethodName + "(View) in a parent or ancestor Context for android:onClick " + "attribute defined on view " + this.mHostView.getClass() + string3);
            }
            string3 = " with id '" + this.mHostView.getContext().getResources().getResourceEntryName(n) + "'";
            throw new IllegalStateException("Could not find method " + this.mMethodName + "(View) in a parent or ancestor Context for android:onClick " + "attribute defined on view " + this.mHostView.getClass() + string3);
        }

        public void onClick(@NonNull View view) {
            if (this.mResolvedMethod == null) {
                this.resolveMethod(this.mHostView.getContext(), this.mMethodName);
            }
            try {
                this.mResolvedMethod.invoke((Object)this.mResolvedContext, new Object[]{view});
                return;
            }
            catch (IllegalAccessException var3_2) {
                throw new IllegalStateException("Could not execute non-public method for android:onClick", var3_2);
            }
            catch (InvocationTargetException var2_3) {
                throw new IllegalStateException("Could not execute method for android:onClick", var2_3);
            }
        }
    }

}

