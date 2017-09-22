package android.support.graphics.drawable;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.graphics.drawable.VectorDrawable;
import android.os.Build.VERSION;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class VectorDrawableCompat
  extends VectorDrawableCommon
{
  private static final boolean DBG_VECTOR_DRAWABLE = false;
  static final PorterDuff.Mode DEFAULT_TINT_MODE = PorterDuff.Mode.SRC_IN;
  private static final int LINECAP_BUTT = 0;
  private static final int LINECAP_ROUND = 1;
  private static final int LINECAP_SQUARE = 2;
  private static final int LINEJOIN_BEVEL = 2;
  private static final int LINEJOIN_MITER = 0;
  private static final int LINEJOIN_ROUND = 1;
  static final String LOGTAG = "VectorDrawableCompat";
  private static final int MAX_CACHED_BITMAP_SIZE = 2048;
  private static final String SHAPE_CLIP_PATH = "clip-path";
  private static final String SHAPE_GROUP = "group";
  private static final String SHAPE_PATH = "path";
  private static final String SHAPE_VECTOR = "vector";
  private boolean mAllowCaching = true;
  private Drawable.ConstantState mCachedConstantStateDelegate;
  private ColorFilter mColorFilter;
  private boolean mMutated;
  private PorterDuffColorFilter mTintFilter;
  private final Rect mTmpBounds = new Rect();
  private final float[] mTmpFloats = new float[9];
  private final Matrix mTmpMatrix = new Matrix();
  private VectorDrawableCompatState mVectorState;
  
  VectorDrawableCompat()
  {
    this.mVectorState = new VectorDrawableCompatState();
  }
  
  VectorDrawableCompat(@NonNull VectorDrawableCompatState paramVectorDrawableCompatState)
  {
    this.mVectorState = paramVectorDrawableCompatState;
    this.mTintFilter = updateTintFilter(this.mTintFilter, paramVectorDrawableCompatState.mTint, paramVectorDrawableCompatState.mTintMode);
  }
  
  static int applyAlpha(int paramInt, float paramFloat)
  {
    int i = Color.alpha(paramInt);
    return paramInt & 0xFFFFFF | (int)(paramFloat * i) << 24;
  }
  
  @SuppressLint({"NewApi"})
  @Nullable
  public static VectorDrawableCompat create(@NonNull Resources paramResources, @DrawableRes int paramInt, @Nullable Resources.Theme paramTheme)
  {
    if (Build.VERSION.SDK_INT >= 24)
    {
      VectorDrawableCompat localVectorDrawableCompat1 = new VectorDrawableCompat();
      localVectorDrawableCompat1.mDelegateDrawable = ResourcesCompat.getDrawable(paramResources, paramInt, paramTheme);
      localVectorDrawableCompat1.mCachedConstantStateDelegate = new VectorDrawableDelegateState(localVectorDrawableCompat1.mDelegateDrawable.getConstantState());
      return localVectorDrawableCompat1;
    }
    try
    {
      localXmlResourceParser = paramResources.getXml(paramInt);
      localAttributeSet = Xml.asAttributeSet(localXmlResourceParser);
      int i;
      do
      {
        i = localXmlResourceParser.next();
      } while ((i != 2) && (i != 1));
      if (i != 2) {
        throw new XmlPullParserException("No start tag found");
      }
    }
    catch (XmlPullParserException localXmlPullParserException)
    {
      XmlResourceParser localXmlResourceParser;
      AttributeSet localAttributeSet;
      Log.e("VectorDrawableCompat", "parser error", localXmlPullParserException);
      return null;
      VectorDrawableCompat localVectorDrawableCompat2 = createFromXmlInner(paramResources, localXmlResourceParser, localAttributeSet, paramTheme);
      return localVectorDrawableCompat2;
    }
    catch (IOException localIOException)
    {
      for (;;)
      {
        Log.e("VectorDrawableCompat", "parser error", localIOException);
      }
    }
  }
  
  @SuppressLint({"NewApi"})
  public static VectorDrawableCompat createFromXmlInner(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    VectorDrawableCompat localVectorDrawableCompat = new VectorDrawableCompat();
    localVectorDrawableCompat.inflate(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
    return localVectorDrawableCompat;
  }
  
  private void inflateInternal(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    VectorDrawableCompatState localVectorDrawableCompatState = this.mVectorState;
    VPathRenderer localVPathRenderer = localVectorDrawableCompatState.mVPathRenderer;
    int i = 1;
    Stack localStack = new Stack();
    localStack.push(localVPathRenderer.mRootGroup);
    int j = paramXmlPullParser.getEventType();
    int k = 1 + paramXmlPullParser.getDepth();
    if ((j != 1) && ((paramXmlPullParser.getDepth() >= k) || (j != 3)))
    {
      String str;
      VGroup localVGroup1;
      if (j == 2)
      {
        str = paramXmlPullParser.getName();
        localVGroup1 = (VGroup)localStack.peek();
        if ("path".equals(str))
        {
          VFullPath localVFullPath = new VFullPath();
          localVFullPath.inflate(paramResources, paramAttributeSet, paramTheme, paramXmlPullParser);
          localVGroup1.mChildren.add(localVFullPath);
          if (localVFullPath.getPathName() != null) {
            localVPathRenderer.mVGTargetsMap.put(localVFullPath.getPathName(), localVFullPath);
          }
          i = 0;
          localVectorDrawableCompatState.mChangingConfigurations |= localVFullPath.mChangingConfigurations;
        }
      }
      for (;;)
      {
        j = paramXmlPullParser.next();
        break;
        if ("clip-path".equals(str))
        {
          VClipPath localVClipPath = new VClipPath();
          localVClipPath.inflate(paramResources, paramAttributeSet, paramTheme, paramXmlPullParser);
          localVGroup1.mChildren.add(localVClipPath);
          if (localVClipPath.getPathName() != null) {
            localVPathRenderer.mVGTargetsMap.put(localVClipPath.getPathName(), localVClipPath);
          }
          localVectorDrawableCompatState.mChangingConfigurations |= localVClipPath.mChangingConfigurations;
        }
        else if ("group".equals(str))
        {
          VGroup localVGroup2 = new VGroup();
          localVGroup2.inflate(paramResources, paramAttributeSet, paramTheme, paramXmlPullParser);
          localVGroup1.mChildren.add(localVGroup2);
          localStack.push(localVGroup2);
          if (localVGroup2.getGroupName() != null) {
            localVPathRenderer.mVGTargetsMap.put(localVGroup2.getGroupName(), localVGroup2);
          }
          localVectorDrawableCompatState.mChangingConfigurations |= localVGroup2.mChangingConfigurations;
          continue;
          if ((j == 3) && ("group".equals(paramXmlPullParser.getName()))) {
            localStack.pop();
          }
        }
      }
    }
    if (i != 0)
    {
      StringBuffer localStringBuffer = new StringBuffer();
      if (localStringBuffer.length() > 0) {
        localStringBuffer.append(" or ");
      }
      localStringBuffer.append("path");
      throw new XmlPullParserException("no " + localStringBuffer + " defined");
    }
  }
  
  @SuppressLint({"NewApi"})
  private boolean needMirroring()
  {
    int i = 1;
    if (Build.VERSION.SDK_INT < 17) {
      return false;
    }
    if ((isAutoMirrored()) && (getLayoutDirection() == i)) {}
    for (;;)
    {
      return i;
      int j = 0;
    }
  }
  
  private static PorterDuff.Mode parseTintModeCompat(int paramInt, PorterDuff.Mode paramMode)
  {
    switch (paramInt)
    {
    }
    do
    {
      return paramMode;
      return PorterDuff.Mode.SRC_OVER;
      return PorterDuff.Mode.SRC_IN;
      return PorterDuff.Mode.SRC_ATOP;
      return PorterDuff.Mode.MULTIPLY;
      return PorterDuff.Mode.SCREEN;
    } while (Build.VERSION.SDK_INT < 11);
    return PorterDuff.Mode.ADD;
  }
  
  private void printGroupTree(VGroup paramVGroup, int paramInt)
  {
    String str = "";
    for (int i = 0; i < paramInt; i++) {
      str = str + "    ";
    }
    Log.v("VectorDrawableCompat", str + "current group is :" + paramVGroup.getGroupName() + " rotation is " + paramVGroup.mRotate);
    Log.v("VectorDrawableCompat", str + "matrix is :" + paramVGroup.getLocalMatrix().toString());
    int j = 0;
    if (j < paramVGroup.mChildren.size())
    {
      Object localObject = paramVGroup.mChildren.get(j);
      if ((localObject instanceof VGroup)) {
        printGroupTree((VGroup)localObject, paramInt + 1);
      }
      for (;;)
      {
        j++;
        break;
        ((VPath)localObject).printVPath(paramInt + 1);
      }
    }
  }
  
  private void updateStateFromTypedArray(TypedArray paramTypedArray, XmlPullParser paramXmlPullParser)
    throws XmlPullParserException
  {
    VectorDrawableCompatState localVectorDrawableCompatState = this.mVectorState;
    VPathRenderer localVPathRenderer = localVectorDrawableCompatState.mVPathRenderer;
    localVectorDrawableCompatState.mTintMode = parseTintModeCompat(TypedArrayUtils.getNamedInt(paramTypedArray, paramXmlPullParser, "tintMode", 6, -1), PorterDuff.Mode.SRC_IN);
    ColorStateList localColorStateList = paramTypedArray.getColorStateList(1);
    if (localColorStateList != null) {
      localVectorDrawableCompatState.mTint = localColorStateList;
    }
    localVectorDrawableCompatState.mAutoMirrored = TypedArrayUtils.getNamedBoolean(paramTypedArray, paramXmlPullParser, "autoMirrored", 5, localVectorDrawableCompatState.mAutoMirrored);
    localVPathRenderer.mViewportWidth = TypedArrayUtils.getNamedFloat(paramTypedArray, paramXmlPullParser, "viewportWidth", 7, localVPathRenderer.mViewportWidth);
    localVPathRenderer.mViewportHeight = TypedArrayUtils.getNamedFloat(paramTypedArray, paramXmlPullParser, "viewportHeight", 8, localVPathRenderer.mViewportHeight);
    if (localVPathRenderer.mViewportWidth <= 0.0F) {
      throw new XmlPullParserException(paramTypedArray.getPositionDescription() + "<vector> tag requires viewportWidth > 0");
    }
    if (localVPathRenderer.mViewportHeight <= 0.0F) {
      throw new XmlPullParserException(paramTypedArray.getPositionDescription() + "<vector> tag requires viewportHeight > 0");
    }
    localVPathRenderer.mBaseWidth = paramTypedArray.getDimension(3, localVPathRenderer.mBaseWidth);
    localVPathRenderer.mBaseHeight = paramTypedArray.getDimension(2, localVPathRenderer.mBaseHeight);
    if (localVPathRenderer.mBaseWidth <= 0.0F) {
      throw new XmlPullParserException(paramTypedArray.getPositionDescription() + "<vector> tag requires width > 0");
    }
    if (localVPathRenderer.mBaseHeight <= 0.0F) {
      throw new XmlPullParserException(paramTypedArray.getPositionDescription() + "<vector> tag requires height > 0");
    }
    localVPathRenderer.setAlpha(TypedArrayUtils.getNamedFloat(paramTypedArray, paramXmlPullParser, "alpha", 4, localVPathRenderer.getAlpha()));
    String str = paramTypedArray.getString(0);
    if (str != null)
    {
      localVPathRenderer.mRootName = str;
      localVPathRenderer.mVGTargetsMap.put(str, localVPathRenderer);
    }
  }
  
  public boolean canApplyTheme()
  {
    if (this.mDelegateDrawable != null) {
      DrawableCompat.canApplyTheme(this.mDelegateDrawable);
    }
    return false;
  }
  
  public void draw(Canvas paramCanvas)
  {
    if (this.mDelegateDrawable != null) {
      this.mDelegateDrawable.draw(paramCanvas);
    }
    Object localObject;
    int k;
    int m;
    do
    {
      do
      {
        return;
        copyBounds(this.mTmpBounds);
      } while ((this.mTmpBounds.width() <= 0) || (this.mTmpBounds.height() <= 0));
      if (this.mColorFilter != null) {
        break;
      }
      localObject = this.mTintFilter;
      paramCanvas.getMatrix(this.mTmpMatrix);
      this.mTmpMatrix.getValues(this.mTmpFloats);
      float f1 = Math.abs(this.mTmpFloats[0]);
      float f2 = Math.abs(this.mTmpFloats[4]);
      float f3 = Math.abs(this.mTmpFloats[1]);
      float f4 = Math.abs(this.mTmpFloats[3]);
      if ((f3 != 0.0F) || (f4 != 0.0F))
      {
        f1 = 1.0F;
        f2 = 1.0F;
      }
      int i = (int)(f1 * this.mTmpBounds.width());
      int j = (int)(f2 * this.mTmpBounds.height());
      k = Math.min(2048, i);
      m = Math.min(2048, j);
    } while ((k <= 0) || (m <= 0));
    int n = paramCanvas.save();
    paramCanvas.translate(this.mTmpBounds.left, this.mTmpBounds.top);
    if (needMirroring())
    {
      paramCanvas.translate(this.mTmpBounds.width(), 0.0F);
      paramCanvas.scale(-1.0F, 1.0F);
    }
    this.mTmpBounds.offsetTo(0, 0);
    this.mVectorState.createCachedBitmapIfNeeded(k, m);
    if (!this.mAllowCaching) {
      this.mVectorState.updateCachedBitmap(k, m);
    }
    for (;;)
    {
      this.mVectorState.drawCachedBitmapWithRootAlpha(paramCanvas, (ColorFilter)localObject, this.mTmpBounds);
      paramCanvas.restoreToCount(n);
      return;
      localObject = this.mColorFilter;
      break;
      if (!this.mVectorState.canReuseCache())
      {
        this.mVectorState.updateCachedBitmap(k, m);
        this.mVectorState.updateCacheStates();
      }
    }
  }
  
  public int getAlpha()
  {
    if (this.mDelegateDrawable != null) {
      return DrawableCompat.getAlpha(this.mDelegateDrawable);
    }
    return this.mVectorState.mVPathRenderer.getRootAlpha();
  }
  
  public int getChangingConfigurations()
  {
    if (this.mDelegateDrawable != null) {
      return this.mDelegateDrawable.getChangingConfigurations();
    }
    return super.getChangingConfigurations() | this.mVectorState.getChangingConfigurations();
  }
  
  public Drawable.ConstantState getConstantState()
  {
    if (this.mDelegateDrawable != null) {
      return new VectorDrawableDelegateState(this.mDelegateDrawable.getConstantState());
    }
    this.mVectorState.mChangingConfigurations = getChangingConfigurations();
    return this.mVectorState;
  }
  
  public int getIntrinsicHeight()
  {
    if (this.mDelegateDrawable != null) {
      return this.mDelegateDrawable.getIntrinsicHeight();
    }
    return (int)this.mVectorState.mVPathRenderer.mBaseHeight;
  }
  
  public int getIntrinsicWidth()
  {
    if (this.mDelegateDrawable != null) {
      return this.mDelegateDrawable.getIntrinsicWidth();
    }
    return (int)this.mVectorState.mVPathRenderer.mBaseWidth;
  }
  
  public int getOpacity()
  {
    if (this.mDelegateDrawable != null) {
      return this.mDelegateDrawable.getOpacity();
    }
    return -3;
  }
  
  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public float getPixelSize()
  {
    if (((this.mVectorState == null) && (this.mVectorState.mVPathRenderer == null)) || (this.mVectorState.mVPathRenderer.mBaseWidth == 0.0F) || (this.mVectorState.mVPathRenderer.mBaseHeight == 0.0F) || (this.mVectorState.mVPathRenderer.mViewportHeight == 0.0F) || (this.mVectorState.mVPathRenderer.mViewportWidth == 0.0F)) {
      return 1.0F;
    }
    float f1 = this.mVectorState.mVPathRenderer.mBaseWidth;
    float f2 = this.mVectorState.mVPathRenderer.mBaseHeight;
    float f3 = this.mVectorState.mVPathRenderer.mViewportWidth;
    float f4 = this.mVectorState.mVPathRenderer.mViewportHeight;
    return Math.min(f3 / f1, f4 / f2);
  }
  
  Object getTargetByName(String paramString)
  {
    return this.mVectorState.mVPathRenderer.mVGTargetsMap.get(paramString);
  }
  
  @SuppressLint({"NewApi"})
  public void inflate(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet)
    throws XmlPullParserException, IOException
  {
    if (this.mDelegateDrawable != null)
    {
      this.mDelegateDrawable.inflate(paramResources, paramXmlPullParser, paramAttributeSet);
      return;
    }
    inflate(paramResources, paramXmlPullParser, paramAttributeSet, null);
  }
  
  public void inflate(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    if (this.mDelegateDrawable != null)
    {
      DrawableCompat.inflate(this.mDelegateDrawable, paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
      return;
    }
    VectorDrawableCompatState localVectorDrawableCompatState = this.mVectorState;
    localVectorDrawableCompatState.mVPathRenderer = new VPathRenderer();
    TypedArray localTypedArray = obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.styleable_VectorDrawableTypeArray);
    updateStateFromTypedArray(localTypedArray, paramXmlPullParser);
    localTypedArray.recycle();
    localVectorDrawableCompatState.mChangingConfigurations = getChangingConfigurations();
    localVectorDrawableCompatState.mCacheDirty = true;
    inflateInternal(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
    this.mTintFilter = updateTintFilter(this.mTintFilter, localVectorDrawableCompatState.mTint, localVectorDrawableCompatState.mTintMode);
  }
  
  public void invalidateSelf()
  {
    if (this.mDelegateDrawable != null)
    {
      this.mDelegateDrawable.invalidateSelf();
      return;
    }
    super.invalidateSelf();
  }
  
  public boolean isAutoMirrored()
  {
    if (this.mDelegateDrawable != null) {
      return DrawableCompat.isAutoMirrored(this.mDelegateDrawable);
    }
    return this.mVectorState.mAutoMirrored;
  }
  
  public boolean isStateful()
  {
    if (this.mDelegateDrawable != null) {
      return this.mDelegateDrawable.isStateful();
    }
    return (super.isStateful()) || ((this.mVectorState != null) && (this.mVectorState.mTint != null) && (this.mVectorState.mTint.isStateful()));
  }
  
  public Drawable mutate()
  {
    if (this.mDelegateDrawable != null) {
      this.mDelegateDrawable.mutate();
    }
    while ((this.mMutated) || (super.mutate() != this)) {
      return this;
    }
    this.mVectorState = new VectorDrawableCompatState(this.mVectorState);
    this.mMutated = true;
    return this;
  }
  
  protected void onBoundsChange(Rect paramRect)
  {
    if (this.mDelegateDrawable != null) {
      this.mDelegateDrawable.setBounds(paramRect);
    }
  }
  
  protected boolean onStateChange(int[] paramArrayOfInt)
  {
    if (this.mDelegateDrawable != null) {
      return this.mDelegateDrawable.setState(paramArrayOfInt);
    }
    VectorDrawableCompatState localVectorDrawableCompatState = this.mVectorState;
    if ((localVectorDrawableCompatState.mTint != null) && (localVectorDrawableCompatState.mTintMode != null))
    {
      this.mTintFilter = updateTintFilter(this.mTintFilter, localVectorDrawableCompatState.mTint, localVectorDrawableCompatState.mTintMode);
      invalidateSelf();
      return true;
    }
    return false;
  }
  
  public void scheduleSelf(Runnable paramRunnable, long paramLong)
  {
    if (this.mDelegateDrawable != null)
    {
      this.mDelegateDrawable.scheduleSelf(paramRunnable, paramLong);
      return;
    }
    super.scheduleSelf(paramRunnable, paramLong);
  }
  
  void setAllowCaching(boolean paramBoolean)
  {
    this.mAllowCaching = paramBoolean;
  }
  
  public void setAlpha(int paramInt)
  {
    if (this.mDelegateDrawable != null) {
      this.mDelegateDrawable.setAlpha(paramInt);
    }
    while (this.mVectorState.mVPathRenderer.getRootAlpha() == paramInt) {
      return;
    }
    this.mVectorState.mVPathRenderer.setRootAlpha(paramInt);
    invalidateSelf();
  }
  
  public void setAutoMirrored(boolean paramBoolean)
  {
    if (this.mDelegateDrawable != null)
    {
      DrawableCompat.setAutoMirrored(this.mDelegateDrawable, paramBoolean);
      return;
    }
    this.mVectorState.mAutoMirrored = paramBoolean;
  }
  
  public void setColorFilter(ColorFilter paramColorFilter)
  {
    if (this.mDelegateDrawable != null)
    {
      this.mDelegateDrawable.setColorFilter(paramColorFilter);
      return;
    }
    this.mColorFilter = paramColorFilter;
    invalidateSelf();
  }
  
  @SuppressLint({"NewApi"})
  public void setTint(int paramInt)
  {
    if (this.mDelegateDrawable != null)
    {
      DrawableCompat.setTint(this.mDelegateDrawable, paramInt);
      return;
    }
    setTintList(ColorStateList.valueOf(paramInt));
  }
  
  public void setTintList(ColorStateList paramColorStateList)
  {
    if (this.mDelegateDrawable != null) {
      DrawableCompat.setTintList(this.mDelegateDrawable, paramColorStateList);
    }
    VectorDrawableCompatState localVectorDrawableCompatState;
    do
    {
      return;
      localVectorDrawableCompatState = this.mVectorState;
    } while (localVectorDrawableCompatState.mTint == paramColorStateList);
    localVectorDrawableCompatState.mTint = paramColorStateList;
    this.mTintFilter = updateTintFilter(this.mTintFilter, paramColorStateList, localVectorDrawableCompatState.mTintMode);
    invalidateSelf();
  }
  
  public void setTintMode(PorterDuff.Mode paramMode)
  {
    if (this.mDelegateDrawable != null) {
      DrawableCompat.setTintMode(this.mDelegateDrawable, paramMode);
    }
    VectorDrawableCompatState localVectorDrawableCompatState;
    do
    {
      return;
      localVectorDrawableCompatState = this.mVectorState;
    } while (localVectorDrawableCompatState.mTintMode == paramMode);
    localVectorDrawableCompatState.mTintMode = paramMode;
    this.mTintFilter = updateTintFilter(this.mTintFilter, localVectorDrawableCompatState.mTint, paramMode);
    invalidateSelf();
  }
  
  public boolean setVisible(boolean paramBoolean1, boolean paramBoolean2)
  {
    if (this.mDelegateDrawable != null) {
      return this.mDelegateDrawable.setVisible(paramBoolean1, paramBoolean2);
    }
    return super.setVisible(paramBoolean1, paramBoolean2);
  }
  
  public void unscheduleSelf(Runnable paramRunnable)
  {
    if (this.mDelegateDrawable != null)
    {
      this.mDelegateDrawable.unscheduleSelf(paramRunnable);
      return;
    }
    super.unscheduleSelf(paramRunnable);
  }
  
  PorterDuffColorFilter updateTintFilter(PorterDuffColorFilter paramPorterDuffColorFilter, ColorStateList paramColorStateList, PorterDuff.Mode paramMode)
  {
    if ((paramColorStateList == null) || (paramMode == null)) {
      return null;
    }
    return new PorterDuffColorFilter(paramColorStateList.getColorForState(getState(), 0), paramMode);
  }
  
  private static class VClipPath
    extends VectorDrawableCompat.VPath
  {
    public VClipPath() {}
    
    public VClipPath(VClipPath paramVClipPath)
    {
      super();
    }
    
    private void updateStateFromTypedArray(TypedArray paramTypedArray)
    {
      String str1 = paramTypedArray.getString(0);
      if (str1 != null) {
        this.mPathName = str1;
      }
      String str2 = paramTypedArray.getString(1);
      if (str2 != null) {
        this.mNodes = PathParser.createNodesFromPathData(str2);
      }
    }
    
    public void inflate(Resources paramResources, AttributeSet paramAttributeSet, Resources.Theme paramTheme, XmlPullParser paramXmlPullParser)
    {
      if (!TypedArrayUtils.hasAttribute(paramXmlPullParser, "pathData")) {
        return;
      }
      TypedArray localTypedArray = VectorDrawableCommon.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.styleable_VectorDrawableClipPath);
      updateStateFromTypedArray(localTypedArray);
      localTypedArray.recycle();
    }
    
    public boolean isClipPath()
    {
      return true;
    }
  }
  
  private static class VFullPath
    extends VectorDrawableCompat.VPath
  {
    float mFillAlpha = 1.0F;
    int mFillColor = 0;
    int mFillRule;
    float mStrokeAlpha = 1.0F;
    int mStrokeColor = 0;
    Paint.Cap mStrokeLineCap = Paint.Cap.BUTT;
    Paint.Join mStrokeLineJoin = Paint.Join.MITER;
    float mStrokeMiterlimit = 4.0F;
    float mStrokeWidth = 0.0F;
    private int[] mThemeAttrs;
    float mTrimPathEnd = 1.0F;
    float mTrimPathOffset = 0.0F;
    float mTrimPathStart = 0.0F;
    
    public VFullPath() {}
    
    public VFullPath(VFullPath paramVFullPath)
    {
      super();
      this.mThemeAttrs = paramVFullPath.mThemeAttrs;
      this.mStrokeColor = paramVFullPath.mStrokeColor;
      this.mStrokeWidth = paramVFullPath.mStrokeWidth;
      this.mStrokeAlpha = paramVFullPath.mStrokeAlpha;
      this.mFillColor = paramVFullPath.mFillColor;
      this.mFillRule = paramVFullPath.mFillRule;
      this.mFillAlpha = paramVFullPath.mFillAlpha;
      this.mTrimPathStart = paramVFullPath.mTrimPathStart;
      this.mTrimPathEnd = paramVFullPath.mTrimPathEnd;
      this.mTrimPathOffset = paramVFullPath.mTrimPathOffset;
      this.mStrokeLineCap = paramVFullPath.mStrokeLineCap;
      this.mStrokeLineJoin = paramVFullPath.mStrokeLineJoin;
      this.mStrokeMiterlimit = paramVFullPath.mStrokeMiterlimit;
    }
    
    private Paint.Cap getStrokeLineCap(int paramInt, Paint.Cap paramCap)
    {
      switch (paramInt)
      {
      default: 
        return paramCap;
      case 0: 
        return Paint.Cap.BUTT;
      case 1: 
        return Paint.Cap.ROUND;
      }
      return Paint.Cap.SQUARE;
    }
    
    private Paint.Join getStrokeLineJoin(int paramInt, Paint.Join paramJoin)
    {
      switch (paramInt)
      {
      default: 
        return paramJoin;
      case 0: 
        return Paint.Join.MITER;
      case 1: 
        return Paint.Join.ROUND;
      }
      return Paint.Join.BEVEL;
    }
    
    private void updateStateFromTypedArray(TypedArray paramTypedArray, XmlPullParser paramXmlPullParser)
    {
      this.mThemeAttrs = null;
      if (!TypedArrayUtils.hasAttribute(paramXmlPullParser, "pathData")) {
        return;
      }
      String str1 = paramTypedArray.getString(0);
      if (str1 != null) {
        this.mPathName = str1;
      }
      String str2 = paramTypedArray.getString(2);
      if (str2 != null) {
        this.mNodes = PathParser.createNodesFromPathData(str2);
      }
      this.mFillColor = TypedArrayUtils.getNamedColor(paramTypedArray, paramXmlPullParser, "fillColor", 1, this.mFillColor);
      this.mFillAlpha = TypedArrayUtils.getNamedFloat(paramTypedArray, paramXmlPullParser, "fillAlpha", 12, this.mFillAlpha);
      this.mStrokeLineCap = getStrokeLineCap(TypedArrayUtils.getNamedInt(paramTypedArray, paramXmlPullParser, "strokeLineCap", 8, -1), this.mStrokeLineCap);
      this.mStrokeLineJoin = getStrokeLineJoin(TypedArrayUtils.getNamedInt(paramTypedArray, paramXmlPullParser, "strokeLineJoin", 9, -1), this.mStrokeLineJoin);
      this.mStrokeMiterlimit = TypedArrayUtils.getNamedFloat(paramTypedArray, paramXmlPullParser, "strokeMiterLimit", 10, this.mStrokeMiterlimit);
      this.mStrokeColor = TypedArrayUtils.getNamedColor(paramTypedArray, paramXmlPullParser, "strokeColor", 3, this.mStrokeColor);
      this.mStrokeAlpha = TypedArrayUtils.getNamedFloat(paramTypedArray, paramXmlPullParser, "strokeAlpha", 11, this.mStrokeAlpha);
      this.mStrokeWidth = TypedArrayUtils.getNamedFloat(paramTypedArray, paramXmlPullParser, "strokeWidth", 4, this.mStrokeWidth);
      this.mTrimPathEnd = TypedArrayUtils.getNamedFloat(paramTypedArray, paramXmlPullParser, "trimPathEnd", 6, this.mTrimPathEnd);
      this.mTrimPathOffset = TypedArrayUtils.getNamedFloat(paramTypedArray, paramXmlPullParser, "trimPathOffset", 7, this.mTrimPathOffset);
      this.mTrimPathStart = TypedArrayUtils.getNamedFloat(paramTypedArray, paramXmlPullParser, "trimPathStart", 5, this.mTrimPathStart);
    }
    
    public void applyTheme(Resources.Theme paramTheme)
    {
      if (this.mThemeAttrs == null) {}
    }
    
    public boolean canApplyTheme()
    {
      return this.mThemeAttrs != null;
    }
    
    float getFillAlpha()
    {
      return this.mFillAlpha;
    }
    
    int getFillColor()
    {
      return this.mFillColor;
    }
    
    float getStrokeAlpha()
    {
      return this.mStrokeAlpha;
    }
    
    int getStrokeColor()
    {
      return this.mStrokeColor;
    }
    
    float getStrokeWidth()
    {
      return this.mStrokeWidth;
    }
    
    float getTrimPathEnd()
    {
      return this.mTrimPathEnd;
    }
    
    float getTrimPathOffset()
    {
      return this.mTrimPathOffset;
    }
    
    float getTrimPathStart()
    {
      return this.mTrimPathStart;
    }
    
    public void inflate(Resources paramResources, AttributeSet paramAttributeSet, Resources.Theme paramTheme, XmlPullParser paramXmlPullParser)
    {
      TypedArray localTypedArray = VectorDrawableCommon.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.styleable_VectorDrawablePath);
      updateStateFromTypedArray(localTypedArray, paramXmlPullParser);
      localTypedArray.recycle();
    }
    
    void setFillAlpha(float paramFloat)
    {
      this.mFillAlpha = paramFloat;
    }
    
    void setFillColor(int paramInt)
    {
      this.mFillColor = paramInt;
    }
    
    void setStrokeAlpha(float paramFloat)
    {
      this.mStrokeAlpha = paramFloat;
    }
    
    void setStrokeColor(int paramInt)
    {
      this.mStrokeColor = paramInt;
    }
    
    void setStrokeWidth(float paramFloat)
    {
      this.mStrokeWidth = paramFloat;
    }
    
    void setTrimPathEnd(float paramFloat)
    {
      this.mTrimPathEnd = paramFloat;
    }
    
    void setTrimPathOffset(float paramFloat)
    {
      this.mTrimPathOffset = paramFloat;
    }
    
    void setTrimPathStart(float paramFloat)
    {
      this.mTrimPathStart = paramFloat;
    }
  }
  
  private static class VGroup
  {
    int mChangingConfigurations;
    final ArrayList<Object> mChildren = new ArrayList();
    private String mGroupName = null;
    private final Matrix mLocalMatrix = new Matrix();
    private float mPivotX = 0.0F;
    private float mPivotY = 0.0F;
    float mRotate = 0.0F;
    private float mScaleX = 1.0F;
    private float mScaleY = 1.0F;
    private final Matrix mStackedMatrix = new Matrix();
    private int[] mThemeAttrs;
    private float mTranslateX = 0.0F;
    private float mTranslateY = 0.0F;
    
    public VGroup() {}
    
    public VGroup(VGroup paramVGroup, ArrayMap<String, Object> paramArrayMap)
    {
      this.mRotate = paramVGroup.mRotate;
      this.mPivotX = paramVGroup.mPivotX;
      this.mPivotY = paramVGroup.mPivotY;
      this.mScaleX = paramVGroup.mScaleX;
      this.mScaleY = paramVGroup.mScaleY;
      this.mTranslateX = paramVGroup.mTranslateX;
      this.mTranslateY = paramVGroup.mTranslateY;
      this.mThemeAttrs = paramVGroup.mThemeAttrs;
      this.mGroupName = paramVGroup.mGroupName;
      this.mChangingConfigurations = paramVGroup.mChangingConfigurations;
      if (this.mGroupName != null) {
        paramArrayMap.put(this.mGroupName, this);
      }
      this.mLocalMatrix.set(paramVGroup.mLocalMatrix);
      ArrayList localArrayList = paramVGroup.mChildren;
      int i = 0;
      while (i < localArrayList.size())
      {
        Object localObject1 = localArrayList.get(i);
        if ((localObject1 instanceof VGroup))
        {
          VGroup localVGroup = (VGroup)localObject1;
          this.mChildren.add(new VGroup(localVGroup, paramArrayMap));
          i++;
        }
        else
        {
          if ((localObject1 instanceof VectorDrawableCompat.VFullPath)) {}
          for (Object localObject2 = new VectorDrawableCompat.VFullPath((VectorDrawableCompat.VFullPath)localObject1);; localObject2 = new VectorDrawableCompat.VClipPath((VectorDrawableCompat.VClipPath)localObject1))
          {
            this.mChildren.add(localObject2);
            if (((VectorDrawableCompat.VPath)localObject2).mPathName == null) {
              break;
            }
            paramArrayMap.put(((VectorDrawableCompat.VPath)localObject2).mPathName, localObject2);
            break;
            if (!(localObject1 instanceof VectorDrawableCompat.VClipPath)) {
              break label329;
            }
          }
          label329:
          throw new IllegalStateException("Unknown object in the tree!");
        }
      }
    }
    
    private void updateLocalMatrix()
    {
      this.mLocalMatrix.reset();
      this.mLocalMatrix.postTranslate(-this.mPivotX, -this.mPivotY);
      this.mLocalMatrix.postScale(this.mScaleX, this.mScaleY);
      this.mLocalMatrix.postRotate(this.mRotate, 0.0F, 0.0F);
      this.mLocalMatrix.postTranslate(this.mTranslateX + this.mPivotX, this.mTranslateY + this.mPivotY);
    }
    
    private void updateStateFromTypedArray(TypedArray paramTypedArray, XmlPullParser paramXmlPullParser)
    {
      this.mThemeAttrs = null;
      this.mRotate = TypedArrayUtils.getNamedFloat(paramTypedArray, paramXmlPullParser, "rotation", 5, this.mRotate);
      this.mPivotX = paramTypedArray.getFloat(1, this.mPivotX);
      this.mPivotY = paramTypedArray.getFloat(2, this.mPivotY);
      this.mScaleX = TypedArrayUtils.getNamedFloat(paramTypedArray, paramXmlPullParser, "scaleX", 3, this.mScaleX);
      this.mScaleY = TypedArrayUtils.getNamedFloat(paramTypedArray, paramXmlPullParser, "scaleY", 4, this.mScaleY);
      this.mTranslateX = TypedArrayUtils.getNamedFloat(paramTypedArray, paramXmlPullParser, "translateX", 6, this.mTranslateX);
      this.mTranslateY = TypedArrayUtils.getNamedFloat(paramTypedArray, paramXmlPullParser, "translateY", 7, this.mTranslateY);
      String str = paramTypedArray.getString(0);
      if (str != null) {
        this.mGroupName = str;
      }
      updateLocalMatrix();
    }
    
    public String getGroupName()
    {
      return this.mGroupName;
    }
    
    public Matrix getLocalMatrix()
    {
      return this.mLocalMatrix;
    }
    
    public float getPivotX()
    {
      return this.mPivotX;
    }
    
    public float getPivotY()
    {
      return this.mPivotY;
    }
    
    public float getRotation()
    {
      return this.mRotate;
    }
    
    public float getScaleX()
    {
      return this.mScaleX;
    }
    
    public float getScaleY()
    {
      return this.mScaleY;
    }
    
    public float getTranslateX()
    {
      return this.mTranslateX;
    }
    
    public float getTranslateY()
    {
      return this.mTranslateY;
    }
    
    public void inflate(Resources paramResources, AttributeSet paramAttributeSet, Resources.Theme paramTheme, XmlPullParser paramXmlPullParser)
    {
      TypedArray localTypedArray = VectorDrawableCommon.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.styleable_VectorDrawableGroup);
      updateStateFromTypedArray(localTypedArray, paramXmlPullParser);
      localTypedArray.recycle();
    }
    
    public void setPivotX(float paramFloat)
    {
      if (paramFloat != this.mPivotX)
      {
        this.mPivotX = paramFloat;
        updateLocalMatrix();
      }
    }
    
    public void setPivotY(float paramFloat)
    {
      if (paramFloat != this.mPivotY)
      {
        this.mPivotY = paramFloat;
        updateLocalMatrix();
      }
    }
    
    public void setRotation(float paramFloat)
    {
      if (paramFloat != this.mRotate)
      {
        this.mRotate = paramFloat;
        updateLocalMatrix();
      }
    }
    
    public void setScaleX(float paramFloat)
    {
      if (paramFloat != this.mScaleX)
      {
        this.mScaleX = paramFloat;
        updateLocalMatrix();
      }
    }
    
    public void setScaleY(float paramFloat)
    {
      if (paramFloat != this.mScaleY)
      {
        this.mScaleY = paramFloat;
        updateLocalMatrix();
      }
    }
    
    public void setTranslateX(float paramFloat)
    {
      if (paramFloat != this.mTranslateX)
      {
        this.mTranslateX = paramFloat;
        updateLocalMatrix();
      }
    }
    
    public void setTranslateY(float paramFloat)
    {
      if (paramFloat != this.mTranslateY)
      {
        this.mTranslateY = paramFloat;
        updateLocalMatrix();
      }
    }
  }
  
  private static class VPath
  {
    int mChangingConfigurations;
    protected PathParser.PathDataNode[] mNodes = null;
    String mPathName;
    
    public VPath() {}
    
    public VPath(VPath paramVPath)
    {
      this.mPathName = paramVPath.mPathName;
      this.mChangingConfigurations = paramVPath.mChangingConfigurations;
      this.mNodes = PathParser.deepCopyNodes(paramVPath.mNodes);
    }
    
    public String NodesToString(PathParser.PathDataNode[] paramArrayOfPathDataNode)
    {
      String str = " ";
      for (int i = 0; i < paramArrayOfPathDataNode.length; i++)
      {
        str = str + paramArrayOfPathDataNode[i].type + ":";
        float[] arrayOfFloat = paramArrayOfPathDataNode[i].params;
        for (int j = 0; j < arrayOfFloat.length; j++) {
          str = str + arrayOfFloat[j] + ",";
        }
      }
      return str;
    }
    
    public void applyTheme(Resources.Theme paramTheme) {}
    
    public boolean canApplyTheme()
    {
      return false;
    }
    
    public PathParser.PathDataNode[] getPathData()
    {
      return this.mNodes;
    }
    
    public String getPathName()
    {
      return this.mPathName;
    }
    
    public boolean isClipPath()
    {
      return false;
    }
    
    public void printVPath(int paramInt)
    {
      String str = "";
      for (int i = 0; i < paramInt; i++) {
        str = str + "    ";
      }
      Log.v("VectorDrawableCompat", str + "current path is :" + this.mPathName + " pathData is " + NodesToString(this.mNodes));
    }
    
    public void setPathData(PathParser.PathDataNode[] paramArrayOfPathDataNode)
    {
      if (!PathParser.canMorph(this.mNodes, paramArrayOfPathDataNode))
      {
        this.mNodes = PathParser.deepCopyNodes(paramArrayOfPathDataNode);
        return;
      }
      PathParser.updateNodes(this.mNodes, paramArrayOfPathDataNode);
    }
    
    public void toPath(Path paramPath)
    {
      paramPath.reset();
      if (this.mNodes != null) {
        PathParser.PathDataNode.nodesToPath(this.mNodes, paramPath);
      }
    }
  }
  
  private static class VPathRenderer
  {
    private static final Matrix IDENTITY_MATRIX = new Matrix();
    float mBaseHeight = 0.0F;
    float mBaseWidth = 0.0F;
    private int mChangingConfigurations;
    private Paint mFillPaint;
    private final Matrix mFinalPathMatrix = new Matrix();
    private final Path mPath;
    private PathMeasure mPathMeasure;
    private final Path mRenderPath;
    int mRootAlpha = 255;
    final VectorDrawableCompat.VGroup mRootGroup;
    String mRootName = null;
    private Paint mStrokePaint;
    final ArrayMap<String, Object> mVGTargetsMap = new ArrayMap();
    float mViewportHeight = 0.0F;
    float mViewportWidth = 0.0F;
    
    public VPathRenderer()
    {
      this.mRootGroup = new VectorDrawableCompat.VGroup();
      this.mPath = new Path();
      this.mRenderPath = new Path();
    }
    
    public VPathRenderer(VPathRenderer paramVPathRenderer)
    {
      this.mRootGroup = new VectorDrawableCompat.VGroup(paramVPathRenderer.mRootGroup, this.mVGTargetsMap);
      this.mPath = new Path(paramVPathRenderer.mPath);
      this.mRenderPath = new Path(paramVPathRenderer.mRenderPath);
      this.mBaseWidth = paramVPathRenderer.mBaseWidth;
      this.mBaseHeight = paramVPathRenderer.mBaseHeight;
      this.mViewportWidth = paramVPathRenderer.mViewportWidth;
      this.mViewportHeight = paramVPathRenderer.mViewportHeight;
      this.mChangingConfigurations = paramVPathRenderer.mChangingConfigurations;
      this.mRootAlpha = paramVPathRenderer.mRootAlpha;
      this.mRootName = paramVPathRenderer.mRootName;
      if (paramVPathRenderer.mRootName != null) {
        this.mVGTargetsMap.put(paramVPathRenderer.mRootName, this);
      }
    }
    
    private static float cross(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
    {
      return paramFloat1 * paramFloat4 - paramFloat2 * paramFloat3;
    }
    
    private void drawGroupTree(VectorDrawableCompat.VGroup paramVGroup, Matrix paramMatrix, Canvas paramCanvas, int paramInt1, int paramInt2, ColorFilter paramColorFilter)
    {
      paramVGroup.mStackedMatrix.set(paramMatrix);
      paramVGroup.mStackedMatrix.preConcat(paramVGroup.mLocalMatrix);
      paramCanvas.save();
      int i = 0;
      if (i < paramVGroup.mChildren.size())
      {
        Object localObject = paramVGroup.mChildren.get(i);
        if ((localObject instanceof VectorDrawableCompat.VGroup)) {
          drawGroupTree((VectorDrawableCompat.VGroup)localObject, paramVGroup.mStackedMatrix, paramCanvas, paramInt1, paramInt2, paramColorFilter);
        }
        for (;;)
        {
          i++;
          break;
          if ((localObject instanceof VectorDrawableCompat.VPath)) {
            drawPath(paramVGroup, (VectorDrawableCompat.VPath)localObject, paramCanvas, paramInt1, paramInt2, paramColorFilter);
          }
        }
      }
      paramCanvas.restore();
    }
    
    private void drawPath(VectorDrawableCompat.VGroup paramVGroup, VectorDrawableCompat.VPath paramVPath, Canvas paramCanvas, int paramInt1, int paramInt2, ColorFilter paramColorFilter)
    {
      float f1 = paramInt1 / this.mViewportWidth;
      float f2 = paramInt2 / this.mViewportHeight;
      float f3 = Math.min(f1, f2);
      Matrix localMatrix = paramVGroup.mStackedMatrix;
      this.mFinalPathMatrix.set(localMatrix);
      this.mFinalPathMatrix.postScale(f1, f2);
      float f4 = getMatrixScale(localMatrix);
      if (f4 == 0.0F) {
        return;
      }
      paramVPath.toPath(this.mPath);
      Path localPath = this.mPath;
      this.mRenderPath.reset();
      if (paramVPath.isClipPath())
      {
        this.mRenderPath.addPath(localPath, this.mFinalPathMatrix);
        paramCanvas.clipPath(this.mRenderPath);
        return;
      }
      VectorDrawableCompat.VFullPath localVFullPath = (VectorDrawableCompat.VFullPath)paramVPath;
      float f8;
      float f9;
      if ((localVFullPath.mTrimPathStart != 0.0F) || (localVFullPath.mTrimPathEnd != 1.0F))
      {
        float f5 = (localVFullPath.mTrimPathStart + localVFullPath.mTrimPathOffset) % 1.0F;
        float f6 = (localVFullPath.mTrimPathEnd + localVFullPath.mTrimPathOffset) % 1.0F;
        if (this.mPathMeasure == null) {
          this.mPathMeasure = new PathMeasure();
        }
        this.mPathMeasure.setPath(this.mPath, false);
        float f7 = this.mPathMeasure.getLength();
        f8 = f5 * f7;
        f9 = f6 * f7;
        localPath.reset();
        if (f8 <= f9) {
          break label529;
        }
        this.mPathMeasure.getSegment(f8, f7, localPath, true);
        this.mPathMeasure.getSegment(0.0F, f9, localPath, true);
      }
      for (;;)
      {
        localPath.rLineTo(0.0F, 0.0F);
        this.mRenderPath.addPath(localPath, this.mFinalPathMatrix);
        if (localVFullPath.mFillColor != 0)
        {
          if (this.mFillPaint == null)
          {
            this.mFillPaint = new Paint();
            this.mFillPaint.setStyle(Paint.Style.FILL);
            this.mFillPaint.setAntiAlias(true);
          }
          Paint localPaint2 = this.mFillPaint;
          localPaint2.setColor(VectorDrawableCompat.applyAlpha(localVFullPath.mFillColor, localVFullPath.mFillAlpha));
          localPaint2.setColorFilter(paramColorFilter);
          paramCanvas.drawPath(this.mRenderPath, localPaint2);
        }
        if (localVFullPath.mStrokeColor == 0) {
          break;
        }
        if (this.mStrokePaint == null)
        {
          this.mStrokePaint = new Paint();
          this.mStrokePaint.setStyle(Paint.Style.STROKE);
          this.mStrokePaint.setAntiAlias(true);
        }
        Paint localPaint1 = this.mStrokePaint;
        if (localVFullPath.mStrokeLineJoin != null) {
          localPaint1.setStrokeJoin(localVFullPath.mStrokeLineJoin);
        }
        if (localVFullPath.mStrokeLineCap != null) {
          localPaint1.setStrokeCap(localVFullPath.mStrokeLineCap);
        }
        localPaint1.setStrokeMiter(localVFullPath.mStrokeMiterlimit);
        localPaint1.setColor(VectorDrawableCompat.applyAlpha(localVFullPath.mStrokeColor, localVFullPath.mStrokeAlpha));
        localPaint1.setColorFilter(paramColorFilter);
        localPaint1.setStrokeWidth(f3 * f4 * localVFullPath.mStrokeWidth);
        paramCanvas.drawPath(this.mRenderPath, localPaint1);
        return;
        label529:
        this.mPathMeasure.getSegment(f8, f9, localPath, true);
      }
    }
    
    private float getMatrixScale(Matrix paramMatrix)
    {
      float[] arrayOfFloat = { 0.0F, 1.0F, 1.0F, 0.0F };
      paramMatrix.mapVectors(arrayOfFloat);
      float f1 = (float)Math.hypot(arrayOfFloat[0], arrayOfFloat[1]);
      float f2 = (float)Math.hypot(arrayOfFloat[2], arrayOfFloat[3]);
      float f3 = cross(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2], arrayOfFloat[3]);
      float f4 = Math.max(f1, f2);
      boolean bool = f4 < 0.0F;
      float f5 = 0.0F;
      if (bool) {
        f5 = Math.abs(f3) / f4;
      }
      return f5;
    }
    
    public void draw(Canvas paramCanvas, int paramInt1, int paramInt2, ColorFilter paramColorFilter)
    {
      drawGroupTree(this.mRootGroup, IDENTITY_MATRIX, paramCanvas, paramInt1, paramInt2, paramColorFilter);
    }
    
    public float getAlpha()
    {
      return getRootAlpha() / 255.0F;
    }
    
    public int getRootAlpha()
    {
      return this.mRootAlpha;
    }
    
    public void setAlpha(float paramFloat)
    {
      setRootAlpha((int)(255.0F * paramFloat));
    }
    
    public void setRootAlpha(int paramInt)
    {
      this.mRootAlpha = paramInt;
    }
  }
  
  private static class VectorDrawableCompatState
    extends Drawable.ConstantState
  {
    boolean mAutoMirrored;
    boolean mCacheDirty;
    boolean mCachedAutoMirrored;
    Bitmap mCachedBitmap;
    int mCachedRootAlpha;
    int[] mCachedThemeAttrs;
    ColorStateList mCachedTint;
    PorterDuff.Mode mCachedTintMode;
    int mChangingConfigurations;
    Paint mTempPaint;
    ColorStateList mTint = null;
    PorterDuff.Mode mTintMode = VectorDrawableCompat.DEFAULT_TINT_MODE;
    VectorDrawableCompat.VPathRenderer mVPathRenderer;
    
    public VectorDrawableCompatState()
    {
      this.mVPathRenderer = new VectorDrawableCompat.VPathRenderer();
    }
    
    public VectorDrawableCompatState(VectorDrawableCompatState paramVectorDrawableCompatState)
    {
      if (paramVectorDrawableCompatState != null)
      {
        this.mChangingConfigurations = paramVectorDrawableCompatState.mChangingConfigurations;
        this.mVPathRenderer = new VectorDrawableCompat.VPathRenderer(paramVectorDrawableCompatState.mVPathRenderer);
        if (paramVectorDrawableCompatState.mVPathRenderer.mFillPaint != null) {
          VectorDrawableCompat.VPathRenderer.access$002(this.mVPathRenderer, new Paint(paramVectorDrawableCompatState.mVPathRenderer.mFillPaint));
        }
        if (paramVectorDrawableCompatState.mVPathRenderer.mStrokePaint != null) {
          VectorDrawableCompat.VPathRenderer.access$102(this.mVPathRenderer, new Paint(paramVectorDrawableCompatState.mVPathRenderer.mStrokePaint));
        }
        this.mTint = paramVectorDrawableCompatState.mTint;
        this.mTintMode = paramVectorDrawableCompatState.mTintMode;
        this.mAutoMirrored = paramVectorDrawableCompatState.mAutoMirrored;
      }
    }
    
    public boolean canReuseBitmap(int paramInt1, int paramInt2)
    {
      return (paramInt1 == this.mCachedBitmap.getWidth()) && (paramInt2 == this.mCachedBitmap.getHeight());
    }
    
    public boolean canReuseCache()
    {
      return (!this.mCacheDirty) && (this.mCachedTint == this.mTint) && (this.mCachedTintMode == this.mTintMode) && (this.mCachedAutoMirrored == this.mAutoMirrored) && (this.mCachedRootAlpha == this.mVPathRenderer.getRootAlpha());
    }
    
    public void createCachedBitmapIfNeeded(int paramInt1, int paramInt2)
    {
      if ((this.mCachedBitmap == null) || (!canReuseBitmap(paramInt1, paramInt2)))
      {
        this.mCachedBitmap = Bitmap.createBitmap(paramInt1, paramInt2, Bitmap.Config.ARGB_8888);
        this.mCacheDirty = true;
      }
    }
    
    public void drawCachedBitmapWithRootAlpha(Canvas paramCanvas, ColorFilter paramColorFilter, Rect paramRect)
    {
      Paint localPaint = getPaint(paramColorFilter);
      paramCanvas.drawBitmap(this.mCachedBitmap, null, paramRect, localPaint);
    }
    
    public int getChangingConfigurations()
    {
      return this.mChangingConfigurations;
    }
    
    public Paint getPaint(ColorFilter paramColorFilter)
    {
      if ((!hasTranslucentRoot()) && (paramColorFilter == null)) {
        return null;
      }
      if (this.mTempPaint == null)
      {
        this.mTempPaint = new Paint();
        this.mTempPaint.setFilterBitmap(true);
      }
      this.mTempPaint.setAlpha(this.mVPathRenderer.getRootAlpha());
      this.mTempPaint.setColorFilter(paramColorFilter);
      return this.mTempPaint;
    }
    
    public boolean hasTranslucentRoot()
    {
      return this.mVPathRenderer.getRootAlpha() < 255;
    }
    
    public Drawable newDrawable()
    {
      return new VectorDrawableCompat(this);
    }
    
    public Drawable newDrawable(Resources paramResources)
    {
      return new VectorDrawableCompat(this);
    }
    
    public void updateCacheStates()
    {
      this.mCachedTint = this.mTint;
      this.mCachedTintMode = this.mTintMode;
      this.mCachedRootAlpha = this.mVPathRenderer.getRootAlpha();
      this.mCachedAutoMirrored = this.mAutoMirrored;
      this.mCacheDirty = false;
    }
    
    public void updateCachedBitmap(int paramInt1, int paramInt2)
    {
      this.mCachedBitmap.eraseColor(0);
      Canvas localCanvas = new Canvas(this.mCachedBitmap);
      this.mVPathRenderer.draw(localCanvas, paramInt1, paramInt2, null);
    }
  }
  
  private static class VectorDrawableDelegateState
    extends Drawable.ConstantState
  {
    private final Drawable.ConstantState mDelegateState;
    
    public VectorDrawableDelegateState(Drawable.ConstantState paramConstantState)
    {
      this.mDelegateState = paramConstantState;
    }
    
    public boolean canApplyTheme()
    {
      return this.mDelegateState.canApplyTheme();
    }
    
    public int getChangingConfigurations()
    {
      return this.mDelegateState.getChangingConfigurations();
    }
    
    public Drawable newDrawable()
    {
      VectorDrawableCompat localVectorDrawableCompat = new VectorDrawableCompat();
      localVectorDrawableCompat.mDelegateDrawable = ((VectorDrawable)this.mDelegateState.newDrawable());
      return localVectorDrawableCompat;
    }
    
    public Drawable newDrawable(Resources paramResources)
    {
      VectorDrawableCompat localVectorDrawableCompat = new VectorDrawableCompat();
      localVectorDrawableCompat.mDelegateDrawable = ((VectorDrawable)this.mDelegateState.newDrawable(paramResources));
      return localVectorDrawableCompat;
    }
    
    public Drawable newDrawable(Resources paramResources, Resources.Theme paramTheme)
    {
      VectorDrawableCompat localVectorDrawableCompat = new VectorDrawableCompat();
      localVectorDrawableCompat.mDelegateDrawable = ((VectorDrawable)this.mDelegateState.newDrawable(paramResources, paramTheme));
      return localVectorDrawableCompat;
    }
  }
}
