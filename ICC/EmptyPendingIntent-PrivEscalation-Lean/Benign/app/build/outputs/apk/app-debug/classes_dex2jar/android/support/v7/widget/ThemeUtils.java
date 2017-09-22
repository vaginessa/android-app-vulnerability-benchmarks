package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources.Theme;
import android.graphics.Color;
import android.support.v4.graphics.ColorUtils;
import android.util.TypedValue;

class ThemeUtils
{
  static final int[] ACTIVATED_STATE_SET;
  static final int[] CHECKED_STATE_SET;
  static final int[] DISABLED_STATE_SET;
  static final int[] EMPTY_STATE_SET = new int[0];
  static final int[] FOCUSED_STATE_SET;
  static final int[] NOT_PRESSED_OR_FOCUSED_STATE_SET;
  static final int[] PRESSED_STATE_SET;
  static final int[] SELECTED_STATE_SET;
  private static final int[] TEMP_ARRAY = new int[1];
  private static final ThreadLocal<TypedValue> TL_TYPED_VALUE = new ThreadLocal();
  
  static
  {
    DISABLED_STATE_SET = new int[] { -16842910 };
    FOCUSED_STATE_SET = new int[] { 16842908 };
    ACTIVATED_STATE_SET = new int[] { 16843518 };
    PRESSED_STATE_SET = new int[] { 16842919 };
    CHECKED_STATE_SET = new int[] { 16842912 };
    SELECTED_STATE_SET = new int[] { 16842913 };
    NOT_PRESSED_OR_FOCUSED_STATE_SET = new int[] { -16842919, -16842908 };
  }
  
  ThemeUtils() {}
  
  public static ColorStateList createDisabledStateList(int paramInt1, int paramInt2)
  {
    int[][] arrayOfInt = new int[2][];
    int[] arrayOfInt1 = new int[2];
    arrayOfInt[0] = DISABLED_STATE_SET;
    arrayOfInt1[0] = paramInt2;
    int i = 0 + 1;
    arrayOfInt[i] = EMPTY_STATE_SET;
    arrayOfInt1[i] = paramInt1;
    (i + 1);
    return new ColorStateList(arrayOfInt, arrayOfInt1);
  }
  
  public static int getDisabledThemeAttrColor(Context paramContext, int paramInt)
  {
    ColorStateList localColorStateList = getThemeAttrColorStateList(paramContext, paramInt);
    if ((localColorStateList != null) && (localColorStateList.isStateful())) {
      return localColorStateList.getColorForState(DISABLED_STATE_SET, localColorStateList.getDefaultColor());
    }
    TypedValue localTypedValue = getTypedValue();
    paramContext.getTheme().resolveAttribute(16842803, localTypedValue, true);
    return getThemeAttrColor(paramContext, paramInt, localTypedValue.getFloat());
  }
  
  public static int getThemeAttrColor(Context paramContext, int paramInt)
  {
    TEMP_ARRAY[0] = paramInt;
    TintTypedArray localTintTypedArray = TintTypedArray.obtainStyledAttributes(paramContext, null, TEMP_ARRAY);
    try
    {
      int i = localTintTypedArray.getColor(0, 0);
      return i;
    }
    finally
    {
      localTintTypedArray.recycle();
    }
  }
  
  static int getThemeAttrColor(Context paramContext, int paramInt, float paramFloat)
  {
    int i = getThemeAttrColor(paramContext, paramInt);
    return ColorUtils.setAlphaComponent(i, Math.round(paramFloat * Color.alpha(i)));
  }
  
  public static ColorStateList getThemeAttrColorStateList(Context paramContext, int paramInt)
  {
    TEMP_ARRAY[0] = paramInt;
    TintTypedArray localTintTypedArray = TintTypedArray.obtainStyledAttributes(paramContext, null, TEMP_ARRAY);
    try
    {
      ColorStateList localColorStateList = localTintTypedArray.getColorStateList(0);
      return localColorStateList;
    }
    finally
    {
      localTintTypedArray.recycle();
    }
  }
  
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
}
