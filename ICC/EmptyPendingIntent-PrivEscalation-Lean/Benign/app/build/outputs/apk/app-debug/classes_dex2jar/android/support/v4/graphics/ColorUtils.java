package android.support.v4.graphics;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

public final class ColorUtils
{
  private static final int MIN_ALPHA_SEARCH_MAX_ITERATIONS = 10;
  private static final int MIN_ALPHA_SEARCH_PRECISION = 1;
  private static final ThreadLocal<double[]> TEMP_ARRAY = new ThreadLocal();
  private static final double XYZ_EPSILON = 0.008856D;
  private static final double XYZ_KAPPA = 903.3D;
  private static final double XYZ_WHITE_REFERENCE_X = 95.047D;
  private static final double XYZ_WHITE_REFERENCE_Y = 100.0D;
  private static final double XYZ_WHITE_REFERENCE_Z = 108.883D;
  
  private ColorUtils() {}
  
  @ColorInt
  public static int HSLToColor(@NonNull float[] paramArrayOfFloat)
  {
    float f1 = paramArrayOfFloat[0];
    float f2 = paramArrayOfFloat[1];
    float f3 = paramArrayOfFloat[2];
    float f4 = f2 * (1.0F - Math.abs(2.0F * f3 - 1.0F));
    float f5 = f3 - 0.5F * f4;
    float f6 = f4 * (1.0F - Math.abs(f1 / 60.0F % 2.0F - 1.0F));
    int i = (int)f1 / 60;
    int j = 0;
    int k = 0;
    int m = 0;
    switch (i)
    {
    }
    for (;;)
    {
      return Color.rgb(constrain(m, 0, 255), constrain(k, 0, 255), constrain(j, 0, 255));
      m = Math.round(255.0F * (f4 + f5));
      k = Math.round(255.0F * (f6 + f5));
      j = Math.round(255.0F * f5);
      continue;
      m = Math.round(255.0F * (f6 + f5));
      k = Math.round(255.0F * (f4 + f5));
      j = Math.round(255.0F * f5);
      continue;
      m = Math.round(255.0F * f5);
      k = Math.round(255.0F * (f4 + f5));
      j = Math.round(255.0F * (f6 + f5));
      continue;
      m = Math.round(255.0F * f5);
      k = Math.round(255.0F * (f6 + f5));
      j = Math.round(255.0F * (f4 + f5));
      continue;
      m = Math.round(255.0F * (f6 + f5));
      k = Math.round(255.0F * f5);
      j = Math.round(255.0F * (f4 + f5));
      continue;
      m = Math.round(255.0F * (f4 + f5));
      k = Math.round(255.0F * f5);
      j = Math.round(255.0F * (f6 + f5));
    }
  }
  
  @ColorInt
  public static int LABToColor(@FloatRange(from=0.0D, to=100.0D) double paramDouble1, @FloatRange(from=-128.0D, to=127.0D) double paramDouble2, @FloatRange(from=-128.0D, to=127.0D) double paramDouble3)
  {
    double[] arrayOfDouble = getTempDouble3Array();
    LABToXYZ(paramDouble1, paramDouble2, paramDouble3, arrayOfDouble);
    return XYZToColor(arrayOfDouble[0], arrayOfDouble[1], arrayOfDouble[2]);
  }
  
  public static void LABToXYZ(@FloatRange(from=0.0D, to=100.0D) double paramDouble1, @FloatRange(from=-128.0D, to=127.0D) double paramDouble2, @FloatRange(from=-128.0D, to=127.0D) double paramDouble3, @NonNull double[] paramArrayOfDouble)
  {
    double d1 = (16.0D + paramDouble1) / 116.0D;
    double d2 = d1 + paramDouble2 / 500.0D;
    double d3 = d1 - paramDouble3 / 200.0D;
    double d4 = Math.pow(d2, 3.0D);
    double d5;
    double d6;
    label73:
    double d7;
    if (d4 > 0.008856D)
    {
      d5 = d4;
      if (paramDouble1 <= 7.9996247999999985D) {
        break label146;
      }
      d6 = Math.pow(d1, 3.0D);
      d7 = Math.pow(d3, 3.0D);
      if (d7 <= 0.008856D) {
        break label156;
      }
    }
    label146:
    label156:
    for (double d8 = d7;; d8 = (116.0D * d3 - 16.0D) / 903.3D)
    {
      paramArrayOfDouble[0] = (95.047D * d5);
      paramArrayOfDouble[1] = (100.0D * d6);
      paramArrayOfDouble[2] = (108.883D * d8);
      return;
      d5 = (116.0D * d2 - 16.0D) / 903.3D;
      break;
      d6 = paramDouble1 / 903.3D;
      break label73;
    }
  }
  
  public static void RGBToHSL(@IntRange(from=0L, to=255L) int paramInt1, @IntRange(from=0L, to=255L) int paramInt2, @IntRange(from=0L, to=255L) int paramInt3, @NonNull float[] paramArrayOfFloat)
  {
    float f1 = paramInt1 / 255.0F;
    float f2 = paramInt2 / 255.0F;
    float f3 = paramInt3 / 255.0F;
    float f4 = Math.max(f1, Math.max(f2, f3));
    float f5 = Math.min(f1, Math.min(f2, f3));
    float f6 = f4 - f5;
    float f7 = (f4 + f5) / 2.0F;
    float f9;
    float f8;
    if (f4 == f5)
    {
      f9 = 0.0F;
      f8 = 0.0F;
      float f10 = 60.0F * f8 % 360.0F;
      if (f10 < 0.0F) {
        f10 += 360.0F;
      }
      paramArrayOfFloat[0] = constrain(f10, 0.0F, 360.0F);
      paramArrayOfFloat[1] = constrain(f9, 0.0F, 1.0F);
      paramArrayOfFloat[2] = constrain(f7, 0.0F, 1.0F);
      return;
    }
    if (f4 == f1) {
      f8 = (f2 - f3) / f6 % 6.0F;
    }
    for (;;)
    {
      f9 = f6 / (1.0F - Math.abs(2.0F * f7 - 1.0F));
      break;
      if (f4 == f2) {
        f8 = 2.0F + (f3 - f1) / f6;
      } else {
        f8 = 4.0F + (f1 - f2) / f6;
      }
    }
  }
  
  public static void RGBToLAB(@IntRange(from=0L, to=255L) int paramInt1, @IntRange(from=0L, to=255L) int paramInt2, @IntRange(from=0L, to=255L) int paramInt3, @NonNull double[] paramArrayOfDouble)
  {
    RGBToXYZ(paramInt1, paramInt2, paramInt3, paramArrayOfDouble);
    XYZToLAB(paramArrayOfDouble[0], paramArrayOfDouble[1], paramArrayOfDouble[2], paramArrayOfDouble);
  }
  
  public static void RGBToXYZ(@IntRange(from=0L, to=255L) int paramInt1, @IntRange(from=0L, to=255L) int paramInt2, @IntRange(from=0L, to=255L) int paramInt3, @NonNull double[] paramArrayOfDouble)
  {
    if (paramArrayOfDouble.length != 3) {
      throw new IllegalArgumentException("outXyz must have a length of 3.");
    }
    double d1 = paramInt1 / 255.0D;
    double d2;
    double d3;
    double d4;
    label66:
    double d5;
    if (d1 < 0.04045D)
    {
      d2 = d1 / 12.92D;
      d3 = paramInt2 / 255.0D;
      if (d3 >= 0.04045D) {
        break label194;
      }
      d4 = d3 / 12.92D;
      d5 = paramInt3 / 255.0D;
      if (d5 >= 0.04045D) {
        break label215;
      }
    }
    label194:
    label215:
    for (double d6 = d5 / 12.92D;; d6 = Math.pow((0.055D + d5) / 1.055D, 2.4D))
    {
      paramArrayOfDouble[0] = (100.0D * (0.4124D * d2 + 0.3576D * d4 + 0.1805D * d6));
      paramArrayOfDouble[1] = (100.0D * (0.2126D * d2 + 0.7152D * d4 + 0.0722D * d6));
      paramArrayOfDouble[2] = (100.0D * (0.0193D * d2 + 0.1192D * d4 + 0.9505D * d6));
      return;
      d2 = Math.pow((0.055D + d1) / 1.055D, 2.4D);
      break;
      d4 = Math.pow((0.055D + d3) / 1.055D, 2.4D);
      break label66;
    }
  }
  
  @ColorInt
  public static int XYZToColor(@FloatRange(from=0.0D, to=95.047D) double paramDouble1, @FloatRange(from=0.0D, to=100.0D) double paramDouble2, @FloatRange(from=0.0D, to=108.883D) double paramDouble3)
  {
    double d1 = (3.2406D * paramDouble1 + -1.5372D * paramDouble2 + -0.4986D * paramDouble3) / 100.0D;
    double d2 = (-0.9689D * paramDouble1 + 1.8758D * paramDouble2 + 0.0415D * paramDouble3) / 100.0D;
    double d3 = (0.0557D * paramDouble1 + -0.204D * paramDouble2 + 1.057D * paramDouble3) / 100.0D;
    double d4;
    double d5;
    if (d1 > 0.0031308D)
    {
      d4 = 1.055D * Math.pow(d1, 0.4166666666666667D) - 0.055D;
      if (d2 <= 0.0031308D) {
        break label219;
      }
      d5 = 1.055D * Math.pow(d2, 0.4166666666666667D) - 0.055D;
      label126:
      if (d3 <= 0.0031308D) {
        break label230;
      }
    }
    label219:
    label230:
    for (double d6 = 1.055D * Math.pow(d3, 0.4166666666666667D) - 0.055D;; d6 = d3 * 12.92D)
    {
      return Color.rgb(constrain((int)Math.round(255.0D * d4), 0, 255), constrain((int)Math.round(255.0D * d5), 0, 255), constrain((int)Math.round(255.0D * d6), 0, 255));
      d4 = d1 * 12.92D;
      break;
      d5 = d2 * 12.92D;
      break label126;
    }
  }
  
  public static void XYZToLAB(@FloatRange(from=0.0D, to=95.047D) double paramDouble1, @FloatRange(from=0.0D, to=100.0D) double paramDouble2, @FloatRange(from=0.0D, to=108.883D) double paramDouble3, @NonNull double[] paramArrayOfDouble)
  {
    if (paramArrayOfDouble.length != 3) {
      throw new IllegalArgumentException("outLab must have a length of 3.");
    }
    double d1 = pivotXyzComponent(paramDouble1 / 95.047D);
    double d2 = pivotXyzComponent(paramDouble2 / 100.0D);
    double d3 = pivotXyzComponent(paramDouble3 / 108.883D);
    paramArrayOfDouble[0] = Math.max(0.0D, 116.0D * d2 - 16.0D);
    paramArrayOfDouble[1] = (500.0D * (d1 - d2));
    paramArrayOfDouble[2] = (200.0D * (d2 - d3));
  }
  
  @ColorInt
  public static int blendARGB(@ColorInt int paramInt1, @ColorInt int paramInt2, @FloatRange(from=0.0D, to=1.0D) float paramFloat)
  {
    float f1 = 1.0F - paramFloat;
    float f2 = f1 * Color.alpha(paramInt1) + paramFloat * Color.alpha(paramInt2);
    float f3 = f1 * Color.red(paramInt1) + paramFloat * Color.red(paramInt2);
    float f4 = f1 * Color.green(paramInt1) + paramFloat * Color.green(paramInt2);
    float f5 = f1 * Color.blue(paramInt1) + paramFloat * Color.blue(paramInt2);
    return Color.argb((int)f2, (int)f3, (int)f4, (int)f5);
  }
  
  public static void blendHSL(@NonNull float[] paramArrayOfFloat1, @NonNull float[] paramArrayOfFloat2, @FloatRange(from=0.0D, to=1.0D) float paramFloat, @NonNull float[] paramArrayOfFloat3)
  {
    if (paramArrayOfFloat3.length != 3) {
      throw new IllegalArgumentException("result must have a length of 3.");
    }
    float f = 1.0F - paramFloat;
    paramArrayOfFloat3[0] = circularInterpolate(paramArrayOfFloat1[0], paramArrayOfFloat2[0], paramFloat);
    paramArrayOfFloat3[1] = (f * paramArrayOfFloat1[1] + paramFloat * paramArrayOfFloat2[1]);
    paramArrayOfFloat3[2] = (f * paramArrayOfFloat1[2] + paramFloat * paramArrayOfFloat2[2]);
  }
  
  public static void blendLAB(@NonNull double[] paramArrayOfDouble1, @NonNull double[] paramArrayOfDouble2, @FloatRange(from=0.0D, to=1.0D) double paramDouble, @NonNull double[] paramArrayOfDouble3)
  {
    if (paramArrayOfDouble3.length != 3) {
      throw new IllegalArgumentException("outResult must have a length of 3.");
    }
    double d = 1.0D - paramDouble;
    paramArrayOfDouble3[0] = (d * paramArrayOfDouble1[0] + paramDouble * paramArrayOfDouble2[0]);
    paramArrayOfDouble3[1] = (d * paramArrayOfDouble1[1] + paramDouble * paramArrayOfDouble2[1]);
    paramArrayOfDouble3[2] = (d * paramArrayOfDouble1[2] + paramDouble * paramArrayOfDouble2[2]);
  }
  
  public static double calculateContrast(@ColorInt int paramInt1, @ColorInt int paramInt2)
  {
    if (Color.alpha(paramInt2) != 255) {
      throw new IllegalArgumentException("background can not be translucent: #" + Integer.toHexString(paramInt2));
    }
    if (Color.alpha(paramInt1) < 255) {
      paramInt1 = compositeColors(paramInt1, paramInt2);
    }
    double d1 = 0.05D + calculateLuminance(paramInt1);
    double d2 = 0.05D + calculateLuminance(paramInt2);
    return Math.max(d1, d2) / Math.min(d1, d2);
  }
  
  @FloatRange(from=0.0D, to=1.0D)
  public static double calculateLuminance(@ColorInt int paramInt)
  {
    double[] arrayOfDouble = getTempDouble3Array();
    colorToXYZ(paramInt, arrayOfDouble);
    return arrayOfDouble[1] / 100.0D;
  }
  
  public static int calculateMinimumAlpha(@ColorInt int paramInt1, @ColorInt int paramInt2, float paramFloat)
  {
    if (Color.alpha(paramInt2) != 255) {
      throw new IllegalArgumentException("background can not be translucent: #" + Integer.toHexString(paramInt2));
    }
    if (calculateContrast(setAlphaComponent(paramInt1, 255), paramInt2) < paramFloat)
    {
      k = -1;
      return k;
    }
    int i = 0;
    int j = 0;
    int k = 255;
    label73:
    int m;
    if ((i <= 10) && (k - j > 1))
    {
      m = (j + k) / 2;
      if (calculateContrast(setAlphaComponent(paramInt1, m), paramInt2) >= paramFloat) {
        break label123;
      }
      j = m;
    }
    for (;;)
    {
      i++;
      break label73;
      break;
      label123:
      k = m;
    }
  }
  
  @VisibleForTesting
  static float circularInterpolate(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (Math.abs(paramFloat2 - paramFloat1) > 180.0F)
    {
      if (paramFloat2 <= paramFloat1) {
        break label35;
      }
      paramFloat1 += 360.0F;
    }
    for (;;)
    {
      return (paramFloat1 + paramFloat3 * (paramFloat2 - paramFloat1)) % 360.0F;
      label35:
      paramFloat2 += 360.0F;
    }
  }
  
  public static void colorToHSL(@ColorInt int paramInt, @NonNull float[] paramArrayOfFloat)
  {
    RGBToHSL(Color.red(paramInt), Color.green(paramInt), Color.blue(paramInt), paramArrayOfFloat);
  }
  
  public static void colorToLAB(@ColorInt int paramInt, @NonNull double[] paramArrayOfDouble)
  {
    RGBToLAB(Color.red(paramInt), Color.green(paramInt), Color.blue(paramInt), paramArrayOfDouble);
  }
  
  public static void colorToXYZ(@ColorInt int paramInt, @NonNull double[] paramArrayOfDouble)
  {
    RGBToXYZ(Color.red(paramInt), Color.green(paramInt), Color.blue(paramInt), paramArrayOfDouble);
  }
  
  private static int compositeAlpha(int paramInt1, int paramInt2)
  {
    return 255 - (255 - paramInt2) * (255 - paramInt1) / 255;
  }
  
  public static int compositeColors(@ColorInt int paramInt1, @ColorInt int paramInt2)
  {
    int i = Color.alpha(paramInt2);
    int j = Color.alpha(paramInt1);
    int k = compositeAlpha(j, i);
    return Color.argb(k, compositeComponent(Color.red(paramInt1), j, Color.red(paramInt2), i, k), compositeComponent(Color.green(paramInt1), j, Color.green(paramInt2), i, k), compositeComponent(Color.blue(paramInt1), j, Color.blue(paramInt2), i, k));
  }
  
  private static int compositeComponent(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    if (paramInt5 == 0) {
      return 0;
    }
    return (paramInt2 * (paramInt1 * 255) + paramInt3 * paramInt4 * (255 - paramInt2)) / (paramInt5 * 255);
  }
  
  private static float constrain(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (paramFloat1 < paramFloat2) {
      return paramFloat2;
    }
    if (paramFloat1 > paramFloat3) {
      return paramFloat3;
    }
    return paramFloat1;
  }
  
  private static int constrain(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt1 < paramInt2) {
      return paramInt2;
    }
    if (paramInt1 > paramInt3) {
      return paramInt3;
    }
    return paramInt1;
  }
  
  public static double distanceEuclidean(@NonNull double[] paramArrayOfDouble1, @NonNull double[] paramArrayOfDouble2)
  {
    return Math.sqrt(Math.pow(paramArrayOfDouble1[0] - paramArrayOfDouble2[0], 2.0D) + Math.pow(paramArrayOfDouble1[1] - paramArrayOfDouble2[1], 2.0D) + Math.pow(paramArrayOfDouble1[2] - paramArrayOfDouble2[2], 2.0D));
  }
  
  private static double[] getTempDouble3Array()
  {
    double[] arrayOfDouble = (double[])TEMP_ARRAY.get();
    if (arrayOfDouble == null)
    {
      arrayOfDouble = new double[3];
      TEMP_ARRAY.set(arrayOfDouble);
    }
    return arrayOfDouble;
  }
  
  private static double pivotXyzComponent(double paramDouble)
  {
    if (paramDouble > 0.008856D) {
      return Math.pow(paramDouble, 0.3333333333333333D);
    }
    return (16.0D + 903.3D * paramDouble) / 116.0D;
  }
  
  @ColorInt
  public static int setAlphaComponent(@ColorInt int paramInt1, @IntRange(from=0L, to=255L) int paramInt2)
  {
    if ((paramInt2 < 0) || (paramInt2 > 255)) {
      throw new IllegalArgumentException("alpha must be between 0 and 255.");
    }
    return 0xFFFFFF & paramInt1 | paramInt2 << 24;
  }
}
