package android.support.v7.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.appcompat.R.attr;
import android.support.v7.appcompat.R.styleable;
import android.util.AttributeSet;
import android.util.StateSet;
import android.util.Xml;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

final class AppCompatColorStateListInflater
{
  private static final int DEFAULT_COLOR = -65536;
  
  private AppCompatColorStateListInflater() {}
  
  @NonNull
  public static ColorStateList createFromXml(@NonNull Resources paramResources, @NonNull XmlPullParser paramXmlPullParser, @Nullable Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    AttributeSet localAttributeSet = Xml.asAttributeSet(paramXmlPullParser);
    int i;
    do
    {
      i = paramXmlPullParser.next();
    } while ((i != 2) && (i != 1));
    if (i != 2) {
      throw new XmlPullParserException("No start tag found");
    }
    return createFromXmlInner(paramResources, paramXmlPullParser, localAttributeSet, paramTheme);
  }
  
  @NonNull
  private static ColorStateList createFromXmlInner(@NonNull Resources paramResources, @NonNull XmlPullParser paramXmlPullParser, @NonNull AttributeSet paramAttributeSet, @Nullable Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    String str = paramXmlPullParser.getName();
    if (!str.equals("selector")) {
      throw new XmlPullParserException(paramXmlPullParser.getPositionDescription() + ": invalid color state list tag " + str);
    }
    return inflate(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
  }
  
  private static ColorStateList inflate(@NonNull Resources paramResources, @NonNull XmlPullParser paramXmlPullParser, @NonNull AttributeSet paramAttributeSet, @Nullable Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    int i = 1 + paramXmlPullParser.getDepth();
    Object localObject = new int[20][];
    int[] arrayOfInt1 = new int[localObject.length];
    int j = 0;
    int k;
    int m;
    do
    {
      k = paramXmlPullParser.next();
      if (k == 1) {
        break;
      }
      m = paramXmlPullParser.getDepth();
      if ((m < i) && (k == 3)) {
        break;
      }
    } while ((k != 2) || (m > i) || (!paramXmlPullParser.getName().equals("item")));
    TypedArray localTypedArray = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.ColorStateListItem);
    int n = localTypedArray.getColor(R.styleable.ColorStateListItem_android_color, -65281);
    float f = 1.0F;
    label138:
    int[] arrayOfInt3;
    int i2;
    int i3;
    label163:
    int i5;
    int i6;
    if (localTypedArray.hasValue(R.styleable.ColorStateListItem_android_alpha))
    {
      f = localTypedArray.getFloat(R.styleable.ColorStateListItem_android_alpha, f);
      localTypedArray.recycle();
      int i1 = paramAttributeSet.getAttributeCount();
      arrayOfInt3 = new int[i1];
      i2 = 0;
      i3 = 0;
      if (i2 >= i1) {
        break label275;
      }
      i5 = paramAttributeSet.getAttributeNameResource(i2);
      if ((i5 == 16843173) || (i5 == 16843551)) {
        break label386;
      }
      int i7 = R.attr.alpha;
      if (i5 == i7) {
        break label386;
      }
      i6 = i3 + 1;
      if (!paramAttributeSet.getAttributeBooleanValue(i2, false)) {
        break label267;
      }
      label224:
      arrayOfInt3[i3] = i5;
    }
    for (;;)
    {
      i2++;
      i3 = i6;
      break label163;
      if (!localTypedArray.hasValue(R.styleable.ColorStateListItem_alpha)) {
        break label138;
      }
      f = localTypedArray.getFloat(R.styleable.ColorStateListItem_alpha, f);
      break label138;
      label267:
      i5 = -i5;
      break label224;
      label275:
      int[] arrayOfInt4 = StateSet.trimStateSet(arrayOfInt3, i3);
      int i4 = modulateColorAlpha(n, f);
      if ((j != 0) && (arrayOfInt4.length == 0)) {}
      arrayOfInt1 = GrowingArrayUtils.append(arrayOfInt1, j, i4);
      localObject = (int[][])GrowingArrayUtils.append((Object[])localObject, j, arrayOfInt4);
      j++;
      break;
      int[] arrayOfInt2 = new int[j];
      int[][] arrayOfInt = new int[j][];
      System.arraycopy(arrayOfInt1, 0, arrayOfInt2, 0, j);
      System.arraycopy(localObject, 0, arrayOfInt, 0, j);
      ColorStateList localColorStateList = new ColorStateList(arrayOfInt, arrayOfInt2);
      return localColorStateList;
      label386:
      i6 = i3;
    }
  }
  
  private static int modulateColorAlpha(int paramInt, float paramFloat)
  {
    return ColorUtils.setAlphaComponent(paramInt, Math.round(paramFloat * Color.alpha(paramInt)));
  }
  
  private static TypedArray obtainAttributes(Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet, int[] paramArrayOfInt)
  {
    if (paramTheme == null) {
      return paramResources.obtainAttributes(paramAttributeSet, paramArrayOfInt);
    }
    return paramTheme.obtainStyledAttributes(paramAttributeSet, paramArrayOfInt, 0, 0);
  }
}
