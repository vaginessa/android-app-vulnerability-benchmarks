package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.TintableBackgroundView;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R.attr;
import android.support.v7.appcompat.R.layout;
import android.support.v7.appcompat.R.styleable;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.view.menu.ShowableListMenu;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class AppCompatSpinner
  extends Spinner
  implements TintableBackgroundView
{
  private static final int[] ATTRS_ANDROID_SPINNERMODE = { 16843505 };
  private static final int MAX_ITEMS_MEASURED = 15;
  private static final int MODE_DIALOG = 0;
  private static final int MODE_DROPDOWN = 1;
  private static final int MODE_THEME = -1;
  private static final String TAG = "AppCompatSpinner";
  private AppCompatBackgroundHelper mBackgroundTintHelper;
  int mDropDownWidth;
  private ForwardingListener mForwardingListener;
  DropdownPopup mPopup;
  private Context mPopupContext;
  private boolean mPopupSet;
  private SpinnerAdapter mTempAdapter;
  final Rect mTempRect = new Rect();
  
  public AppCompatSpinner(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public AppCompatSpinner(Context paramContext, int paramInt)
  {
    this(paramContext, null, R.attr.spinnerStyle, paramInt);
  }
  
  public AppCompatSpinner(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, R.attr.spinnerStyle);
  }
  
  public AppCompatSpinner(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, -1);
  }
  
  public AppCompatSpinner(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    this(paramContext, paramAttributeSet, paramInt1, paramInt2, null);
  }
  
  public AppCompatSpinner(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2, Resources.Theme paramTheme)
  {
    super(paramContext, paramAttributeSet, paramInt1);
    TintTypedArray localTintTypedArray1 = TintTypedArray.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.Spinner, paramInt1, 0);
    this.mBackgroundTintHelper = new AppCompatBackgroundHelper(this);
    TypedArray localTypedArray;
    if (paramTheme != null)
    {
      this.mPopupContext = new ContextThemeWrapper(paramContext, paramTheme);
      if (this.mPopupContext != null) {
        if (paramInt2 == -1)
        {
          if (Build.VERSION.SDK_INT < 11) {
            break label414;
          }
          localTypedArray = null;
        }
      }
    }
    for (;;)
    {
      try
      {
        localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, ATTRS_ANDROID_SPINNERMODE, paramInt1, 0);
        if (localTypedArray.hasValue(0))
        {
          int j = localTypedArray.getInt(0, 0);
          paramInt2 = j;
        }
      }
      catch (Exception localException)
      {
        final DropdownPopup localDropdownPopup;
        TintTypedArray localTintTypedArray2;
        CharSequence[] arrayOfCharSequence;
        ArrayAdapter localArrayAdapter;
        int i;
        Context localContext;
        Log.i("AppCompatSpinner", "Could not read android:spinnerMode", localException);
        if (localTypedArray == null) {
          continue;
        }
        localTypedArray.recycle();
        continue;
      }
      finally
      {
        if (localTypedArray == null) {
          continue;
        }
        localTypedArray.recycle();
      }
      if (paramInt2 == 1)
      {
        localDropdownPopup = new DropdownPopup(this.mPopupContext, paramAttributeSet, paramInt1);
        localTintTypedArray2 = TintTypedArray.obtainStyledAttributes(this.mPopupContext, paramAttributeSet, R.styleable.Spinner, paramInt1, 0);
        this.mDropDownWidth = localTintTypedArray2.getLayoutDimension(R.styleable.Spinner_android_dropDownWidth, -2);
        localDropdownPopup.setBackgroundDrawable(localTintTypedArray2.getDrawable(R.styleable.Spinner_android_popupBackground));
        localDropdownPopup.setPromptText(localTintTypedArray1.getString(R.styleable.Spinner_android_prompt));
        localTintTypedArray2.recycle();
        this.mPopup = localDropdownPopup;
        this.mForwardingListener = new ForwardingListener(this)
        {
          public ShowableListMenu getPopup()
          {
            return localDropdownPopup;
          }
          
          public boolean onForwardingStarted()
          {
            if (!AppCompatSpinner.this.mPopup.isShowing()) {
              AppCompatSpinner.this.mPopup.show();
            }
            return true;
          }
        };
      }
      arrayOfCharSequence = localTintTypedArray1.getTextArray(R.styleable.Spinner_android_entries);
      if (arrayOfCharSequence != null)
      {
        localArrayAdapter = new ArrayAdapter(paramContext, 17367048, arrayOfCharSequence);
        localArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        setAdapter(localArrayAdapter);
      }
      localTintTypedArray1.recycle();
      this.mPopupSet = true;
      if (this.mTempAdapter != null)
      {
        setAdapter(this.mTempAdapter);
        this.mTempAdapter = null;
      }
      this.mBackgroundTintHelper.loadFromAttributes(paramAttributeSet, paramInt1);
      return;
      i = localTintTypedArray1.getResourceId(R.styleable.Spinner_popupTheme, 0);
      if (i != 0)
      {
        this.mPopupContext = new ContextThemeWrapper(paramContext, i);
        break;
      }
      if (Build.VERSION.SDK_INT < 23)
      {
        localContext = paramContext;
        this.mPopupContext = localContext;
        break;
      }
      localContext = null;
      continue;
      label414:
      paramInt2 = 1;
    }
  }
  
  int compatMeasureContentWidth(SpinnerAdapter paramSpinnerAdapter, Drawable paramDrawable)
  {
    int i;
    if (paramSpinnerAdapter == null) {
      i = 0;
    }
    do
    {
      return i;
      i = 0;
      View localView = null;
      int j = 0;
      int k = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 0);
      int m = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 0);
      int n = Math.max(0, getSelectedItemPosition());
      int i1 = Math.min(paramSpinnerAdapter.getCount(), n + 15);
      for (int i2 = Math.max(0, n - (15 - (i1 - n))); i2 < i1; i2++)
      {
        int i3 = paramSpinnerAdapter.getItemViewType(i2);
        if (i3 != j)
        {
          j = i3;
          localView = null;
        }
        localView = paramSpinnerAdapter.getView(i2, localView, this);
        if (localView.getLayoutParams() == null) {
          localView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        }
        localView.measure(k, m);
        i = Math.max(i, localView.getMeasuredWidth());
      }
    } while (paramDrawable == null);
    paramDrawable.getPadding(this.mTempRect);
    return i + (this.mTempRect.left + this.mTempRect.right);
  }
  
  protected void drawableStateChanged()
  {
    super.drawableStateChanged();
    if (this.mBackgroundTintHelper != null) {
      this.mBackgroundTintHelper.applySupportBackgroundTint();
    }
  }
  
  public int getDropDownHorizontalOffset()
  {
    if (this.mPopup != null) {
      return this.mPopup.getHorizontalOffset();
    }
    if (Build.VERSION.SDK_INT >= 16) {
      return super.getDropDownHorizontalOffset();
    }
    return 0;
  }
  
  public int getDropDownVerticalOffset()
  {
    if (this.mPopup != null) {
      return this.mPopup.getVerticalOffset();
    }
    if (Build.VERSION.SDK_INT >= 16) {
      return super.getDropDownVerticalOffset();
    }
    return 0;
  }
  
  public int getDropDownWidth()
  {
    if (this.mPopup != null) {
      return this.mDropDownWidth;
    }
    if (Build.VERSION.SDK_INT >= 16) {
      return super.getDropDownWidth();
    }
    return 0;
  }
  
  public Drawable getPopupBackground()
  {
    if (this.mPopup != null) {
      return this.mPopup.getBackground();
    }
    if (Build.VERSION.SDK_INT >= 16) {
      return super.getPopupBackground();
    }
    return null;
  }
  
  public Context getPopupContext()
  {
    if (this.mPopup != null) {
      return this.mPopupContext;
    }
    if (Build.VERSION.SDK_INT >= 23) {
      return super.getPopupContext();
    }
    return null;
  }
  
  public CharSequence getPrompt()
  {
    if (this.mPopup != null) {
      return this.mPopup.getHintText();
    }
    return super.getPrompt();
  }
  
  @Nullable
  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public ColorStateList getSupportBackgroundTintList()
  {
    if (this.mBackgroundTintHelper != null) {
      return this.mBackgroundTintHelper.getSupportBackgroundTintList();
    }
    return null;
  }
  
  @Nullable
  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public PorterDuff.Mode getSupportBackgroundTintMode()
  {
    if (this.mBackgroundTintHelper != null) {
      return this.mBackgroundTintHelper.getSupportBackgroundTintMode();
    }
    return null;
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    if ((this.mPopup != null) && (this.mPopup.isShowing())) {
      this.mPopup.dismiss();
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    if ((this.mPopup != null) && (View.MeasureSpec.getMode(paramInt1) == Integer.MIN_VALUE)) {
      setMeasuredDimension(Math.min(Math.max(getMeasuredWidth(), compatMeasureContentWidth(getAdapter(), getBackground())), View.MeasureSpec.getSize(paramInt1)), getMeasuredHeight());
    }
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if ((this.mForwardingListener != null) && (this.mForwardingListener.onTouch(this, paramMotionEvent))) {
      return true;
    }
    return super.onTouchEvent(paramMotionEvent);
  }
  
  public boolean performClick()
  {
    if (this.mPopup != null)
    {
      if (!this.mPopup.isShowing()) {
        this.mPopup.show();
      }
      return true;
    }
    return super.performClick();
  }
  
  public void setAdapter(SpinnerAdapter paramSpinnerAdapter)
  {
    if (!this.mPopupSet) {
      this.mTempAdapter = paramSpinnerAdapter;
    }
    do
    {
      return;
      super.setAdapter(paramSpinnerAdapter);
    } while (this.mPopup == null);
    if (this.mPopupContext == null) {}
    for (Context localContext = getContext();; localContext = this.mPopupContext)
    {
      this.mPopup.setAdapter(new DropDownAdapter(paramSpinnerAdapter, localContext.getTheme()));
      return;
    }
  }
  
  public void setBackgroundDrawable(Drawable paramDrawable)
  {
    super.setBackgroundDrawable(paramDrawable);
    if (this.mBackgroundTintHelper != null) {
      this.mBackgroundTintHelper.onSetBackgroundDrawable(paramDrawable);
    }
  }
  
  public void setBackgroundResource(@DrawableRes int paramInt)
  {
    super.setBackgroundResource(paramInt);
    if (this.mBackgroundTintHelper != null) {
      this.mBackgroundTintHelper.onSetBackgroundResource(paramInt);
    }
  }
  
  public void setDropDownHorizontalOffset(int paramInt)
  {
    if (this.mPopup != null) {
      this.mPopup.setHorizontalOffset(paramInt);
    }
    while (Build.VERSION.SDK_INT < 16) {
      return;
    }
    super.setDropDownHorizontalOffset(paramInt);
  }
  
  public void setDropDownVerticalOffset(int paramInt)
  {
    if (this.mPopup != null) {
      this.mPopup.setVerticalOffset(paramInt);
    }
    while (Build.VERSION.SDK_INT < 16) {
      return;
    }
    super.setDropDownVerticalOffset(paramInt);
  }
  
  public void setDropDownWidth(int paramInt)
  {
    if (this.mPopup != null) {
      this.mDropDownWidth = paramInt;
    }
    while (Build.VERSION.SDK_INT < 16) {
      return;
    }
    super.setDropDownWidth(paramInt);
  }
  
  public void setPopupBackgroundDrawable(Drawable paramDrawable)
  {
    if (this.mPopup != null) {
      this.mPopup.setBackgroundDrawable(paramDrawable);
    }
    while (Build.VERSION.SDK_INT < 16) {
      return;
    }
    super.setPopupBackgroundDrawable(paramDrawable);
  }
  
  public void setPopupBackgroundResource(@DrawableRes int paramInt)
  {
    setPopupBackgroundDrawable(AppCompatResources.getDrawable(getPopupContext(), paramInt));
  }
  
  public void setPrompt(CharSequence paramCharSequence)
  {
    if (this.mPopup != null)
    {
      this.mPopup.setPromptText(paramCharSequence);
      return;
    }
    super.setPrompt(paramCharSequence);
  }
  
  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public void setSupportBackgroundTintList(@Nullable ColorStateList paramColorStateList)
  {
    if (this.mBackgroundTintHelper != null) {
      this.mBackgroundTintHelper.setSupportBackgroundTintList(paramColorStateList);
    }
  }
  
  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public void setSupportBackgroundTintMode(@Nullable PorterDuff.Mode paramMode)
  {
    if (this.mBackgroundTintHelper != null) {
      this.mBackgroundTintHelper.setSupportBackgroundTintMode(paramMode);
    }
  }
  
  private static class DropDownAdapter
    implements ListAdapter, SpinnerAdapter
  {
    private SpinnerAdapter mAdapter;
    private ListAdapter mListAdapter;
    
    public DropDownAdapter(@Nullable SpinnerAdapter paramSpinnerAdapter, @Nullable Resources.Theme paramTheme)
    {
      this.mAdapter = paramSpinnerAdapter;
      if ((paramSpinnerAdapter instanceof ListAdapter)) {
        this.mListAdapter = ((ListAdapter)paramSpinnerAdapter);
      }
      if (paramTheme != null)
      {
        if ((Build.VERSION.SDK_INT < 23) || (!(paramSpinnerAdapter instanceof android.widget.ThemedSpinnerAdapter))) {
          break label69;
        }
        android.widget.ThemedSpinnerAdapter localThemedSpinnerAdapter1 = (android.widget.ThemedSpinnerAdapter)paramSpinnerAdapter;
        if (localThemedSpinnerAdapter1.getDropDownViewTheme() != paramTheme) {
          localThemedSpinnerAdapter1.setDropDownViewTheme(paramTheme);
        }
      }
      label69:
      ThemedSpinnerAdapter localThemedSpinnerAdapter;
      do
      {
        do
        {
          return;
        } while (!(paramSpinnerAdapter instanceof ThemedSpinnerAdapter));
        localThemedSpinnerAdapter = (ThemedSpinnerAdapter)paramSpinnerAdapter;
      } while (localThemedSpinnerAdapter.getDropDownViewTheme() != null);
      localThemedSpinnerAdapter.setDropDownViewTheme(paramTheme);
    }
    
    public boolean areAllItemsEnabled()
    {
      ListAdapter localListAdapter = this.mListAdapter;
      if (localListAdapter != null) {
        return localListAdapter.areAllItemsEnabled();
      }
      return true;
    }
    
    public int getCount()
    {
      if (this.mAdapter == null) {
        return 0;
      }
      return this.mAdapter.getCount();
    }
    
    public View getDropDownView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (this.mAdapter == null) {
        return null;
      }
      return this.mAdapter.getDropDownView(paramInt, paramView, paramViewGroup);
    }
    
    public Object getItem(int paramInt)
    {
      if (this.mAdapter == null) {
        return null;
      }
      return this.mAdapter.getItem(paramInt);
    }
    
    public long getItemId(int paramInt)
    {
      if (this.mAdapter == null) {
        return -1L;
      }
      return this.mAdapter.getItemId(paramInt);
    }
    
    public int getItemViewType(int paramInt)
    {
      return 0;
    }
    
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      return getDropDownView(paramInt, paramView, paramViewGroup);
    }
    
    public int getViewTypeCount()
    {
      return 1;
    }
    
    public boolean hasStableIds()
    {
      return (this.mAdapter != null) && (this.mAdapter.hasStableIds());
    }
    
    public boolean isEmpty()
    {
      return getCount() == 0;
    }
    
    public boolean isEnabled(int paramInt)
    {
      ListAdapter localListAdapter = this.mListAdapter;
      if (localListAdapter != null) {
        return localListAdapter.isEnabled(paramInt);
      }
      return true;
    }
    
    public void registerDataSetObserver(DataSetObserver paramDataSetObserver)
    {
      if (this.mAdapter != null) {
        this.mAdapter.registerDataSetObserver(paramDataSetObserver);
      }
    }
    
    public void unregisterDataSetObserver(DataSetObserver paramDataSetObserver)
    {
      if (this.mAdapter != null) {
        this.mAdapter.unregisterDataSetObserver(paramDataSetObserver);
      }
    }
  }
  
  private class DropdownPopup
    extends ListPopupWindow
  {
    ListAdapter mAdapter;
    private CharSequence mHintText;
    private final Rect mVisibleRect = new Rect();
    
    public DropdownPopup(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
    {
      super(paramAttributeSet, paramInt);
      setAnchorView(AppCompatSpinner.this);
      setModal(true);
      setPromptPosition(0);
      setOnItemClickListener(new AdapterView.OnItemClickListener()
      {
        public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
        {
          AppCompatSpinner.this.setSelection(paramAnonymousInt);
          if (AppCompatSpinner.this.getOnItemClickListener() != null) {
            AppCompatSpinner.this.performItemClick(paramAnonymousView, paramAnonymousInt, AppCompatSpinner.DropdownPopup.this.mAdapter.getItemId(paramAnonymousInt));
          }
          AppCompatSpinner.DropdownPopup.this.dismiss();
        }
      });
    }
    
    void computeContentWidth()
    {
      Drawable localDrawable = getBackground();
      int i;
      int j;
      int k;
      int m;
      if (localDrawable != null)
      {
        localDrawable.getPadding(AppCompatSpinner.this.mTempRect);
        if (ViewUtils.isLayoutRtl(AppCompatSpinner.this))
        {
          i = AppCompatSpinner.this.mTempRect.right;
          j = AppCompatSpinner.this.getPaddingLeft();
          k = AppCompatSpinner.this.getPaddingRight();
          m = AppCompatSpinner.this.getWidth();
          if (AppCompatSpinner.this.mDropDownWidth != -2) {
            break label244;
          }
          int i1 = AppCompatSpinner.this.compatMeasureContentWidth((SpinnerAdapter)this.mAdapter, getBackground());
          int i2 = AppCompatSpinner.this.getContext().getResources().getDisplayMetrics().widthPixels - AppCompatSpinner.this.mTempRect.left - AppCompatSpinner.this.mTempRect.right;
          if (i1 > i2) {
            i1 = i2;
          }
          setContentWidth(Math.max(i1, m - j - k));
          label169:
          if (!ViewUtils.isLayoutRtl(AppCompatSpinner.this)) {
            break label284;
          }
        }
      }
      label244:
      label284:
      for (int n = i + (m - k - getWidth());; n = i + j)
      {
        setHorizontalOffset(n);
        return;
        i = -AppCompatSpinner.this.mTempRect.left;
        break;
        Rect localRect = AppCompatSpinner.this.mTempRect;
        AppCompatSpinner.this.mTempRect.right = 0;
        localRect.left = 0;
        i = 0;
        break;
        if (AppCompatSpinner.this.mDropDownWidth == -1)
        {
          setContentWidth(m - j - k);
          break label169;
        }
        setContentWidth(AppCompatSpinner.this.mDropDownWidth);
        break label169;
      }
    }
    
    public CharSequence getHintText()
    {
      return this.mHintText;
    }
    
    boolean isVisibleToUser(View paramView)
    {
      return (ViewCompat.isAttachedToWindow(paramView)) && (paramView.getGlobalVisibleRect(this.mVisibleRect));
    }
    
    public void setAdapter(ListAdapter paramListAdapter)
    {
      super.setAdapter(paramListAdapter);
      this.mAdapter = paramListAdapter;
    }
    
    public void setPromptText(CharSequence paramCharSequence)
    {
      this.mHintText = paramCharSequence;
    }
    
    public void show()
    {
      boolean bool = isShowing();
      computeContentWidth();
      setInputMethodMode(2);
      super.show();
      getListView().setChoiceMode(1);
      setSelection(AppCompatSpinner.this.getSelectedItemPosition());
      if (bool) {}
      ViewTreeObserver localViewTreeObserver;
      do
      {
        return;
        localViewTreeObserver = AppCompatSpinner.this.getViewTreeObserver();
      } while (localViewTreeObserver == null);
      final ViewTreeObserver.OnGlobalLayoutListener local2 = new ViewTreeObserver.OnGlobalLayoutListener()
      {
        public void onGlobalLayout()
        {
          if (!AppCompatSpinner.DropdownPopup.this.isVisibleToUser(AppCompatSpinner.this))
          {
            AppCompatSpinner.DropdownPopup.this.dismiss();
            return;
          }
          AppCompatSpinner.DropdownPopup.this.computeContentWidth();
          AppCompatSpinner.DropdownPopup.this.show();
        }
      };
      localViewTreeObserver.addOnGlobalLayoutListener(local2);
      setOnDismissListener(new PopupWindow.OnDismissListener()
      {
        public void onDismiss()
        {
          ViewTreeObserver localViewTreeObserver = AppCompatSpinner.this.getViewTreeObserver();
          if (localViewTreeObserver != null) {
            localViewTreeObserver.removeGlobalOnLayoutListener(local2);
          }
        }
      });
    }
  }
}
