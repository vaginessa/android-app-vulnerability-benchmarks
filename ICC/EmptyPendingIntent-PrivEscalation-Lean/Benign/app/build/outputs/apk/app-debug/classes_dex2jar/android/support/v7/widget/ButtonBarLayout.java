package android.support.v7.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.ConfigurationHelper;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R.id;
import android.support.v7.appcompat.R.styleable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

@RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
public class ButtonBarLayout
  extends LinearLayout
{
  private static final int ALLOW_STACKING_MIN_HEIGHT_DP = 320;
  private static final int PEEK_BUTTON_DP = 16;
  private boolean mAllowStacking;
  private int mLastWidthSize = -1;
  
  public ButtonBarLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    if (ConfigurationHelper.getScreenHeightDp(getResources()) >= 320) {}
    for (boolean bool = true;; bool = false)
    {
      TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ButtonBarLayout);
      this.mAllowStacking = localTypedArray.getBoolean(R.styleable.ButtonBarLayout_allowStacking, bool);
      localTypedArray.recycle();
      return;
    }
  }
  
  private int getNextVisibleChildIndex(int paramInt)
  {
    int i = paramInt;
    int j = getChildCount();
    while (i < j)
    {
      if (getChildAt(i).getVisibility() == 0) {
        return i;
      }
      i++;
    }
    return -1;
  }
  
  private boolean isStacked()
  {
    return getOrientation() == 1;
  }
  
  private void setStacked(boolean paramBoolean)
  {
    int i;
    int j;
    label17:
    View localView;
    if (paramBoolean)
    {
      i = 1;
      setOrientation(i);
      if (!paramBoolean) {
        break label86;
      }
      j = 5;
      setGravity(j);
      localView = findViewById(R.id.spacer);
      if (localView != null) {
        if (!paramBoolean) {
          break label92;
        }
      }
    }
    label86:
    label92:
    for (int m = 8;; m = 4)
    {
      localView.setVisibility(m);
      for (int k = -2 + getChildCount(); k >= 0; k--) {
        bringChildToFront(getChildAt(k));
      }
      i = 0;
      break;
      j = 80;
      break label17;
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getSize(paramInt1);
    if (this.mAllowStacking)
    {
      if ((i > this.mLastWidthSize) && (isStacked())) {
        setStacked(false);
      }
      this.mLastWidthSize = i;
    }
    int j;
    int k;
    int i5;
    label108:
    int n;
    if ((!isStacked()) && (View.MeasureSpec.getMode(paramInt1) == 1073741824))
    {
      j = View.MeasureSpec.makeMeasureSpec(i, Integer.MIN_VALUE);
      k = 1;
      super.onMeasure(j, paramInt2);
      if ((this.mAllowStacking) && (!isStacked()))
      {
        if (Build.VERSION.SDK_INT < 11) {
          break label275;
        }
        if ((0xFF000000 & ViewCompat.getMeasuredWidthAndState(this)) != 16777216) {
          break label269;
        }
        i5 = 1;
        if (i5 != 0)
        {
          setStacked(true);
          k = 1;
        }
      }
      if (k != 0) {
        super.onMeasure(paramInt1, paramInt2);
      }
      int m = getNextVisibleChildIndex(0);
      n = 0;
      if (m >= 0)
      {
        View localView = getChildAt(m);
        LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)localView.getLayoutParams();
        n = 0 + (getPaddingTop() + localView.getMeasuredHeight() + localLayoutParams.topMargin + localLayoutParams.bottomMargin);
        if (!isStacked()) {
          break label342;
        }
        int i1 = getNextVisibleChildIndex(m + 1);
        if (i1 >= 0) {
          n = (int)(n + (getChildAt(i1).getPaddingTop() + 16.0F * getResources().getDisplayMetrics().density));
        }
      }
    }
    for (;;)
    {
      if (ViewCompat.getMinimumHeight(this) != n) {
        setMinimumHeight(n);
      }
      return;
      j = paramInt1;
      k = 0;
      break;
      label269:
      i5 = 0;
      break label108;
      label275:
      int i2 = 0;
      int i3 = 0;
      int i4 = getChildCount();
      while (i3 < i4)
      {
        i2 += getChildAt(i3).getMeasuredWidth();
        i3++;
      }
      if (i2 + getPaddingLeft() + getPaddingRight() > i) {}
      for (i5 = 1;; i5 = 0) {
        break;
      }
      label342:
      n += getPaddingBottom();
    }
  }
  
  public void setAllowStacking(boolean paramBoolean)
  {
    if (this.mAllowStacking != paramBoolean)
    {
      this.mAllowStacking = paramBoolean;
      if ((!this.mAllowStacking) && (getOrientation() == 1)) {
        setStacked(false);
      }
      requestLayout();
    }
  }
}
