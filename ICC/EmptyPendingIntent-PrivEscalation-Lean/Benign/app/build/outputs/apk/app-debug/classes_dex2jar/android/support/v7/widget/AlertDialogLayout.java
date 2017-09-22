package android.support.v7.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R.id;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;

@RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
public class AlertDialogLayout
  extends LinearLayoutCompat
{
  public AlertDialogLayout(@Nullable Context paramContext)
  {
    super(paramContext);
  }
  
  public AlertDialogLayout(@Nullable Context paramContext, @Nullable AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  private void forceUniformWidth(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
    for (int j = 0; j < paramInt1; j++)
    {
      View localView = getChildAt(j);
      if (localView.getVisibility() != 8)
      {
        LinearLayoutCompat.LayoutParams localLayoutParams = (LinearLayoutCompat.LayoutParams)localView.getLayoutParams();
        if (localLayoutParams.width == -1)
        {
          int k = localLayoutParams.height;
          localLayoutParams.height = localView.getMeasuredHeight();
          measureChildWithMargins(localView, i, 0, paramInt2, 0);
          localLayoutParams.height = k;
        }
      }
    }
  }
  
  private static int resolveMinimumHeight(View paramView)
  {
    int i = ViewCompat.getMinimumHeight(paramView);
    if (i > 0) {
      return i;
    }
    if ((paramView instanceof ViewGroup))
    {
      ViewGroup localViewGroup = (ViewGroup)paramView;
      if (localViewGroup.getChildCount() == 1) {
        return resolveMinimumHeight(localViewGroup.getChildAt(0));
      }
    }
    return 0;
  }
  
  private void setChildFrame(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    paramView.layout(paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4);
  }
  
  private boolean tryOnMeasure(int paramInt1, int paramInt2)
  {
    Object localObject1 = null;
    Object localObject2 = null;
    Object localObject3 = null;
    int i = getChildCount();
    int j = 0;
    if (j < i)
    {
      View localView2 = getChildAt(j);
      if (localView2.getVisibility() == 8) {}
      for (;;)
      {
        j++;
        break;
        int i16 = localView2.getId();
        if (i16 == R.id.topPanel)
        {
          localObject1 = localView2;
        }
        else if (i16 == R.id.buttonPanel)
        {
          localObject2 = localView2;
        }
        else
        {
          if ((i16 != R.id.contentPanel) && (i16 != R.id.customPanel)) {
            break label114;
          }
          if (localObject3 != null) {
            return false;
          }
          localObject3 = localView2;
        }
      }
      label114:
      return false;
    }
    int k = View.MeasureSpec.getMode(paramInt2);
    int m = View.MeasureSpec.getSize(paramInt2);
    int n = View.MeasureSpec.getMode(paramInt1);
    int i1 = getPaddingTop() + getPaddingBottom();
    int i2 = 0;
    if (localObject1 != null)
    {
      localObject1.measure(paramInt1, 0);
      i1 += localObject1.getMeasuredHeight();
      i2 = ViewCompat.combineMeasuredStates(0, ViewCompat.getMeasuredState(localObject1));
    }
    int i3 = 0;
    int i4 = 0;
    if (localObject2 != null)
    {
      localObject2.measure(paramInt1, 0);
      i3 = resolveMinimumHeight(localObject2);
      i4 = localObject2.getMeasuredHeight() - i3;
      i1 += i3;
      i2 = ViewCompat.combineMeasuredStates(i2, ViewCompat.getMeasuredState(localObject2));
    }
    int i5 = 0;
    if (localObject3 != null) {
      if (k != 0) {
        break label485;
      }
    }
    int i7;
    label485:
    for (int i15 = 0;; i15 = View.MeasureSpec.makeMeasureSpec(Math.max(0, m - i1), k))
    {
      localObject3.measure(paramInt1, i15);
      i5 = localObject3.getMeasuredHeight();
      i1 += i5;
      i2 = ViewCompat.combineMeasuredStates(i2, ViewCompat.getMeasuredState(localObject3));
      int i6 = m - i1;
      if (localObject2 != null)
      {
        int i13 = i1 - i3;
        int i14 = Math.min(i6, i4);
        if (i14 > 0)
        {
          i6 -= i14;
          i3 += i14;
        }
        localObject2.measure(paramInt1, View.MeasureSpec.makeMeasureSpec(i3, 1073741824));
        i1 = i13 + localObject2.getMeasuredHeight();
        i2 = ViewCompat.combineMeasuredStates(i2, ViewCompat.getMeasuredState(localObject2));
      }
      if ((localObject3 != null) && (i6 > 0))
      {
        int i10 = i1 - i5;
        int i11 = i6;
        (i6 - i11);
        int i12 = View.MeasureSpec.makeMeasureSpec(i5 + i11, k);
        localObject3.measure(paramInt1, i12);
        i1 = i10 + localObject3.getMeasuredHeight();
        i2 = ViewCompat.combineMeasuredStates(i2, ViewCompat.getMeasuredState(localObject3));
      }
      i7 = 0;
      for (int i8 = 0; i8 < i; i8++)
      {
        View localView1 = getChildAt(i8);
        if (localView1.getVisibility() != 8)
        {
          int i9 = localView1.getMeasuredWidth();
          i7 = Math.max(i7, i9);
        }
      }
    }
    setMeasuredDimension(ViewCompat.resolveSizeAndState(i7 + (getPaddingLeft() + getPaddingRight()), paramInt1, i2), ViewCompat.resolveSizeAndState(i1, paramInt2, 0));
    if (n != 1073741824) {
      forceUniformWidth(i, paramInt2);
    }
    return true;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = getPaddingLeft();
    int j = paramInt3 - paramInt1;
    int k = j - getPaddingRight();
    int m = j - i - getPaddingRight();
    int n = getMeasuredHeight();
    int i1 = getChildCount();
    int i2 = getGravity();
    int i3 = i2 & 0x70;
    int i4 = i2 & 0x800007;
    int i5;
    Drawable localDrawable;
    int i6;
    label112:
    int i7;
    label115:
    View localView;
    int i8;
    int i9;
    LinearLayoutCompat.LayoutParams localLayoutParams;
    int i12;
    switch (i3)
    {
    default: 
      i5 = getPaddingTop();
      localDrawable = getDividerDrawable();
      if (localDrawable == null)
      {
        i6 = 0;
        i7 = 0;
        if (i7 >= i1) {
          return;
        }
        localView = getChildAt(i7);
        if ((localView != null) && (localView.getVisibility() != 8))
        {
          i8 = localView.getMeasuredWidth();
          i9 = localView.getMeasuredHeight();
          localLayoutParams = (LinearLayoutCompat.LayoutParams)localView.getLayoutParams();
          int i10 = localLayoutParams.gravity;
          if (i10 < 0) {
            i10 = i4;
          }
          int i11 = ViewCompat.getLayoutDirection(this);
          switch (0x7 & GravityCompat.getAbsoluteGravity(i10, i11))
          {
          default: 
            i12 = i + localLayoutParams.leftMargin;
          }
        }
      }
      break;
    }
    for (;;)
    {
      if (hasDividerBeforeChildAt(i7)) {
        i5 += i6;
      }
      int i13 = i5 + localLayoutParams.topMargin;
      setChildFrame(localView, i12, i13, i8, i9);
      i5 = i13 + (i9 + localLayoutParams.bottomMargin);
      i7++;
      break label115;
      i5 = paramInt4 + getPaddingTop() - paramInt2 - n;
      break;
      i5 = getPaddingTop() + (paramInt4 - paramInt2 - n) / 2;
      break;
      i6 = localDrawable.getIntrinsicHeight();
      break label112;
      i12 = i + (m - i8) / 2 + localLayoutParams.leftMargin - localLayoutParams.rightMargin;
      continue;
      i12 = k - i8 - localLayoutParams.rightMargin;
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (!tryOnMeasure(paramInt1, paramInt2)) {
      super.onMeasure(paramInt1, paramInt2);
    }
  }
}
