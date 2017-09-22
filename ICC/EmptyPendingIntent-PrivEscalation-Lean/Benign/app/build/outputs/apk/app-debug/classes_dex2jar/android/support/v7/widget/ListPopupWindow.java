package android.support.v7.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Handler;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.appcompat.R.attr;
import android.support.v7.appcompat.R.styleable;
import android.support.v7.view.menu.ShowableListMenu;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.KeyEvent.DispatcherState;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import java.lang.reflect.Method;

public class ListPopupWindow
  implements ShowableListMenu
{
  private static final boolean DEBUG = false;
  static final int EXPAND_LIST_TIMEOUT = 250;
  public static final int INPUT_METHOD_FROM_FOCUSABLE = 0;
  public static final int INPUT_METHOD_NEEDED = 1;
  public static final int INPUT_METHOD_NOT_NEEDED = 2;
  public static final int MATCH_PARENT = -1;
  public static final int POSITION_PROMPT_ABOVE = 0;
  public static final int POSITION_PROMPT_BELOW = 1;
  private static final String TAG = "ListPopupWindow";
  public static final int WRAP_CONTENT = -2;
  private static Method sClipToWindowEnabledMethod;
  private static Method sGetMaxAvailableHeightMethod;
  private static Method sSetEpicenterBoundsMethod;
  private ListAdapter mAdapter;
  private Context mContext;
  private boolean mDropDownAlwaysVisible = false;
  private View mDropDownAnchorView;
  private int mDropDownGravity = 0;
  private int mDropDownHeight = -2;
  private int mDropDownHorizontalOffset;
  DropDownListView mDropDownList;
  private Drawable mDropDownListHighlight;
  private int mDropDownVerticalOffset;
  private boolean mDropDownVerticalOffsetSet;
  private int mDropDownWidth = -2;
  private int mDropDownWindowLayoutType = 1002;
  private Rect mEpicenterBounds;
  private boolean mForceIgnoreOutsideTouch = false;
  final Handler mHandler;
  private final ListSelectorHider mHideSelector = new ListSelectorHider();
  private boolean mIsAnimatedFromAnchor = true;
  private AdapterView.OnItemClickListener mItemClickListener;
  private AdapterView.OnItemSelectedListener mItemSelectedListener;
  int mListItemExpandMaximum = Integer.MAX_VALUE;
  private boolean mModal;
  private DataSetObserver mObserver;
  PopupWindow mPopup;
  private int mPromptPosition = 0;
  private View mPromptView;
  final ResizePopupRunnable mResizePopupRunnable = new ResizePopupRunnable();
  private final PopupScrollListener mScrollListener = new PopupScrollListener();
  private Runnable mShowDropDownRunnable;
  private final Rect mTempRect = new Rect();
  private final PopupTouchInterceptor mTouchInterceptor = new PopupTouchInterceptor();
  
  static
  {
    try
    {
      Class[] arrayOfClass2 = new Class[1];
      arrayOfClass2[0] = Boolean.TYPE;
      sClipToWindowEnabledMethod = PopupWindow.class.getDeclaredMethod("setClipToScreenEnabled", arrayOfClass2);
    }
    catch (NoSuchMethodException localNoSuchMethodException1)
    {
      try
      {
        for (;;)
        {
          Class[] arrayOfClass1 = new Class[3];
          arrayOfClass1[0] = View.class;
          arrayOfClass1[1] = Integer.TYPE;
          arrayOfClass1[2] = Boolean.TYPE;
          sGetMaxAvailableHeightMethod = PopupWindow.class.getDeclaredMethod("getMaxAvailableHeight", arrayOfClass1);
          try
          {
            sSetEpicenterBoundsMethod = PopupWindow.class.getDeclaredMethod("setEpicenterBounds", new Class[] { Rect.class });
            return;
          }
          catch (NoSuchMethodException localNoSuchMethodException3)
          {
            Log.i("ListPopupWindow", "Could not find method setEpicenterBounds(Rect) on PopupWindow. Oh well.");
          }
          localNoSuchMethodException1 = localNoSuchMethodException1;
          Log.i("ListPopupWindow", "Could not find method setClipToScreenEnabled() on PopupWindow. Oh well.");
        }
      }
      catch (NoSuchMethodException localNoSuchMethodException2)
      {
        for (;;)
        {
          Log.i("ListPopupWindow", "Could not find method getMaxAvailableHeight(View, int, boolean) on PopupWindow. Oh well.");
        }
      }
    }
  }
  
  public ListPopupWindow(@NonNull Context paramContext)
  {
    this(paramContext, null, R.attr.listPopupWindowStyle);
  }
  
  public ListPopupWindow(@NonNull Context paramContext, @Nullable AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, R.attr.listPopupWindowStyle);
  }
  
  public ListPopupWindow(@NonNull Context paramContext, @Nullable AttributeSet paramAttributeSet, @AttrRes int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public ListPopupWindow(@NonNull Context paramContext, @Nullable AttributeSet paramAttributeSet, @AttrRes int paramInt1, @StyleRes int paramInt2)
  {
    this.mContext = paramContext;
    this.mHandler = new Handler(paramContext.getMainLooper());
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ListPopupWindow, paramInt1, paramInt2);
    this.mDropDownHorizontalOffset = localTypedArray.getDimensionPixelOffset(R.styleable.ListPopupWindow_android_dropDownHorizontalOffset, 0);
    this.mDropDownVerticalOffset = localTypedArray.getDimensionPixelOffset(R.styleable.ListPopupWindow_android_dropDownVerticalOffset, 0);
    if (this.mDropDownVerticalOffset != 0) {
      this.mDropDownVerticalOffsetSet = true;
    }
    localTypedArray.recycle();
    if (Build.VERSION.SDK_INT >= 11) {}
    for (this.mPopup = new AppCompatPopupWindow(paramContext, paramAttributeSet, paramInt1, paramInt2);; this.mPopup = new AppCompatPopupWindow(paramContext, paramAttributeSet, paramInt1))
    {
      this.mPopup.setInputMethodMode(1);
      return;
    }
  }
  
  private int buildDropDown()
  {
    boolean bool2;
    Object localObject;
    View localView2;
    int i;
    LinearLayout localLinearLayout;
    LinearLayout.LayoutParams localLayoutParams2;
    label249:
    int i1;
    int i2;
    label267:
    label321:
    int j;
    if (this.mDropDownList == null)
    {
      Context localContext = this.mContext;
      this.mShowDropDownRunnable = new Runnable()
      {
        public void run()
        {
          View localView = ListPopupWindow.this.getAnchorView();
          if ((localView != null) && (localView.getWindowToken() != null)) {
            ListPopupWindow.this.show();
          }
        }
      };
      if (!this.mModal)
      {
        bool2 = true;
        this.mDropDownList = createDropDownListView(localContext, bool2);
        if (this.mDropDownListHighlight != null) {
          this.mDropDownList.setSelector(this.mDropDownListHighlight);
        }
        this.mDropDownList.setAdapter(this.mAdapter);
        this.mDropDownList.setOnItemClickListener(this.mItemClickListener);
        this.mDropDownList.setFocusable(true);
        this.mDropDownList.setFocusableInTouchMode(true);
        this.mDropDownList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
          public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
          {
            if (paramAnonymousInt != -1)
            {
              DropDownListView localDropDownListView = ListPopupWindow.this.mDropDownList;
              if (localDropDownListView != null) {
                localDropDownListView.setListSelectionHidden(false);
              }
            }
          }
          
          public void onNothingSelected(AdapterView<?> paramAnonymousAdapterView) {}
        });
        this.mDropDownList.setOnScrollListener(this.mScrollListener);
        if (this.mItemSelectedListener != null) {
          this.mDropDownList.setOnItemSelectedListener(this.mItemSelectedListener);
        }
        localObject = this.mDropDownList;
        localView2 = this.mPromptView;
        i = 0;
        if (localView2 != null)
        {
          localLinearLayout = new LinearLayout(localContext);
          localLinearLayout.setOrientation(1);
          localLayoutParams2 = new LinearLayout.LayoutParams(-1, 0, 1.0F);
        }
        switch (this.mPromptPosition)
        {
        default: 
          Log.e("ListPopupWindow", "Invalid hint position " + this.mPromptPosition);
          if (this.mDropDownWidth >= 0)
          {
            i1 = Integer.MIN_VALUE;
            i2 = this.mDropDownWidth;
            localView2.measure(View.MeasureSpec.makeMeasureSpec(i2, i1), 0);
            LinearLayout.LayoutParams localLayoutParams3 = (LinearLayout.LayoutParams)localView2.getLayoutParams();
            i = localView2.getMeasuredHeight() + localLayoutParams3.topMargin + localLayoutParams3.bottomMargin;
            localObject = localLinearLayout;
            this.mPopup.setContentView((View)localObject);
            Drawable localDrawable = this.mPopup.getBackground();
            if (localDrawable == null) {
              break label536;
            }
            localDrawable.getPadding(this.mTempRect);
            j = this.mTempRect.top + this.mTempRect.bottom;
            if (!this.mDropDownVerticalOffsetSet) {
              this.mDropDownVerticalOffset = (-this.mTempRect.top);
            }
            label381:
            if (this.mPopup.getInputMethodMode() != 2) {
              break label549;
            }
          }
          break;
        }
      }
    }
    int k;
    label536:
    label549:
    for (boolean bool1 = true;; bool1 = false)
    {
      k = getMaxAvailableHeight(getAnchorView(), this.mDropDownVerticalOffset, bool1);
      if ((!this.mDropDownAlwaysVisible) && (this.mDropDownHeight != -1)) {
        break label555;
      }
      return k + j;
      bool2 = false;
      break;
      localLinearLayout.addView((View)localObject, localLayoutParams2);
      localLinearLayout.addView(localView2);
      break label249;
      localLinearLayout.addView(localView2);
      localLinearLayout.addView((View)localObject, localLayoutParams2);
      break label249;
      i1 = 0;
      i2 = 0;
      break label267;
      ((ViewGroup)this.mPopup.getContentView());
      View localView1 = this.mPromptView;
      i = 0;
      if (localView1 == null) {
        break label321;
      }
      LinearLayout.LayoutParams localLayoutParams1 = (LinearLayout.LayoutParams)localView1.getLayoutParams();
      i = localView1.getMeasuredHeight() + localLayoutParams1.topMargin + localLayoutParams1.bottomMargin;
      break label321;
      this.mTempRect.setEmpty();
      j = 0;
      break label381;
    }
    label555:
    int m;
    switch (this.mDropDownWidth)
    {
    default: 
      m = View.MeasureSpec.makeMeasureSpec(this.mDropDownWidth, 1073741824);
    }
    for (;;)
    {
      int n = this.mDropDownList.measureHeightOfChildrenCompat(m, 0, -1, k - i, -1);
      if (n > 0) {
        i += j + (this.mDropDownList.getPaddingTop() + this.mDropDownList.getPaddingBottom());
      }
      return n + i;
      m = View.MeasureSpec.makeMeasureSpec(this.mContext.getResources().getDisplayMetrics().widthPixels - (this.mTempRect.left + this.mTempRect.right), Integer.MIN_VALUE);
      continue;
      m = View.MeasureSpec.makeMeasureSpec(this.mContext.getResources().getDisplayMetrics().widthPixels - (this.mTempRect.left + this.mTempRect.right), 1073741824);
    }
  }
  
  private int getMaxAvailableHeight(View paramView, int paramInt, boolean paramBoolean)
  {
    if (sGetMaxAvailableHeightMethod != null) {
      try
      {
        Method localMethod = sGetMaxAvailableHeightMethod;
        PopupWindow localPopupWindow = this.mPopup;
        Object[] arrayOfObject = new Object[3];
        arrayOfObject[0] = paramView;
        arrayOfObject[1] = Integer.valueOf(paramInt);
        arrayOfObject[2] = Boolean.valueOf(paramBoolean);
        int i = ((Integer)localMethod.invoke(localPopupWindow, arrayOfObject)).intValue();
        return i;
      }
      catch (Exception localException)
      {
        Log.i("ListPopupWindow", "Could not call getMaxAvailableHeightMethod(View, int, boolean) on PopupWindow. Using the public version.");
      }
    }
    return this.mPopup.getMaxAvailableHeight(paramView, paramInt);
  }
  
  private static boolean isConfirmKey(int paramInt)
  {
    return (paramInt == 66) || (paramInt == 23);
  }
  
  private void removePromptView()
  {
    if (this.mPromptView != null)
    {
      ViewParent localViewParent = this.mPromptView.getParent();
      if ((localViewParent instanceof ViewGroup)) {
        ((ViewGroup)localViewParent).removeView(this.mPromptView);
      }
    }
  }
  
  private void setPopupClipToScreenEnabled(boolean paramBoolean)
  {
    if (sClipToWindowEnabledMethod != null) {}
    try
    {
      Method localMethod = sClipToWindowEnabledMethod;
      PopupWindow localPopupWindow = this.mPopup;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Boolean.valueOf(paramBoolean);
      localMethod.invoke(localPopupWindow, arrayOfObject);
      return;
    }
    catch (Exception localException)
    {
      Log.i("ListPopupWindow", "Could not call setClipToScreenEnabled() on PopupWindow. Oh well.");
    }
  }
  
  public void clearListSelection()
  {
    DropDownListView localDropDownListView = this.mDropDownList;
    if (localDropDownListView != null)
    {
      localDropDownListView.setListSelectionHidden(true);
      localDropDownListView.requestLayout();
    }
  }
  
  public View.OnTouchListener createDragToOpenListener(View paramView)
  {
    new ForwardingListener(paramView)
    {
      public ListPopupWindow getPopup()
      {
        return ListPopupWindow.this;
      }
    };
  }
  
  @NonNull
  DropDownListView createDropDownListView(Context paramContext, boolean paramBoolean)
  {
    return new DropDownListView(paramContext, paramBoolean);
  }
  
  public void dismiss()
  {
    this.mPopup.dismiss();
    removePromptView();
    this.mPopup.setContentView(null);
    this.mDropDownList = null;
    this.mHandler.removeCallbacks(this.mResizePopupRunnable);
  }
  
  @Nullable
  public View getAnchorView()
  {
    return this.mDropDownAnchorView;
  }
  
  @StyleRes
  public int getAnimationStyle()
  {
    return this.mPopup.getAnimationStyle();
  }
  
  @Nullable
  public Drawable getBackground()
  {
    return this.mPopup.getBackground();
  }
  
  public int getHeight()
  {
    return this.mDropDownHeight;
  }
  
  public int getHorizontalOffset()
  {
    return this.mDropDownHorizontalOffset;
  }
  
  public int getInputMethodMode()
  {
    return this.mPopup.getInputMethodMode();
  }
  
  @Nullable
  public ListView getListView()
  {
    return this.mDropDownList;
  }
  
  public int getPromptPosition()
  {
    return this.mPromptPosition;
  }
  
  @Nullable
  public Object getSelectedItem()
  {
    if (!isShowing()) {
      return null;
    }
    return this.mDropDownList.getSelectedItem();
  }
  
  public long getSelectedItemId()
  {
    if (!isShowing()) {
      return Long.MIN_VALUE;
    }
    return this.mDropDownList.getSelectedItemId();
  }
  
  public int getSelectedItemPosition()
  {
    if (!isShowing()) {
      return -1;
    }
    return this.mDropDownList.getSelectedItemPosition();
  }
  
  @Nullable
  public View getSelectedView()
  {
    if (!isShowing()) {
      return null;
    }
    return this.mDropDownList.getSelectedView();
  }
  
  public int getSoftInputMode()
  {
    return this.mPopup.getSoftInputMode();
  }
  
  public int getVerticalOffset()
  {
    if (!this.mDropDownVerticalOffsetSet) {
      return 0;
    }
    return this.mDropDownVerticalOffset;
  }
  
  public int getWidth()
  {
    return this.mDropDownWidth;
  }
  
  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public boolean isDropDownAlwaysVisible()
  {
    return this.mDropDownAlwaysVisible;
  }
  
  public boolean isInputMethodNotNeeded()
  {
    return this.mPopup.getInputMethodMode() == 2;
  }
  
  public boolean isModal()
  {
    return this.mModal;
  }
  
  public boolean isShowing()
  {
    return this.mPopup.isShowing();
  }
  
  public boolean onKeyDown(int paramInt, @NonNull KeyEvent paramKeyEvent)
  {
    int i;
    int j;
    int k;
    int m;
    if ((isShowing()) && (paramInt != 62) && ((this.mDropDownList.getSelectedItemPosition() >= 0) || (!isConfirmKey(paramInt))))
    {
      i = this.mDropDownList.getSelectedItemPosition();
      ListAdapter localListAdapter;
      if (!this.mPopup.isAboveAnchor())
      {
        j = 1;
        localListAdapter = this.mAdapter;
        k = Integer.MAX_VALUE;
        m = Integer.MIN_VALUE;
        if (localListAdapter != null)
        {
          boolean bool = localListAdapter.areAllItemsEnabled();
          if (!bool) {
            break label162;
          }
          k = 0;
          label88:
          if (!bool) {
            break label176;
          }
        }
      }
      label162:
      label176:
      for (m = -1 + localListAdapter.getCount();; m = this.mDropDownList.lookForSelectablePosition(-1 + localListAdapter.getCount(), false))
      {
        if (((j == 0) || (paramInt != 19) || (i > k)) && ((j != 0) || (paramInt != 20) || (i < m))) {
          break label198;
        }
        clearListSelection();
        this.mPopup.setInputMethodMode(1);
        show();
        return true;
        j = 0;
        break;
        k = this.mDropDownList.lookForSelectablePosition(0, true);
        break label88;
      }
      label198:
      this.mDropDownList.setListSelectionHidden(false);
      if (!this.mDropDownList.onKeyDown(paramInt, paramKeyEvent)) {
        break label282;
      }
      this.mPopup.setInputMethodMode(2);
      this.mDropDownList.requestFocusFromTouch();
      show();
      switch (paramInt)
      {
      }
    }
    label282:
    do
    {
      do
      {
        return false;
        if ((j == 0) || (paramInt != 20)) {
          break;
        }
      } while (i != m);
      return true;
    } while ((j != 0) || (paramInt != 19) || (i != k));
    return true;
  }
  
  public boolean onKeyPreIme(int paramInt, @NonNull KeyEvent paramKeyEvent)
  {
    if ((paramInt == 4) && (isShowing()))
    {
      View localView = this.mDropDownAnchorView;
      if ((paramKeyEvent.getAction() == 0) && (paramKeyEvent.getRepeatCount() == 0))
      {
        KeyEvent.DispatcherState localDispatcherState2 = localView.getKeyDispatcherState();
        if (localDispatcherState2 != null) {
          localDispatcherState2.startTracking(paramKeyEvent, this);
        }
        return true;
      }
      if (paramKeyEvent.getAction() == 1)
      {
        KeyEvent.DispatcherState localDispatcherState1 = localView.getKeyDispatcherState();
        if (localDispatcherState1 != null) {
          localDispatcherState1.handleUpEvent(paramKeyEvent);
        }
        if ((paramKeyEvent.isTracking()) && (!paramKeyEvent.isCanceled()))
        {
          dismiss();
          return true;
        }
      }
    }
    return false;
  }
  
  public boolean onKeyUp(int paramInt, @NonNull KeyEvent paramKeyEvent)
  {
    if ((isShowing()) && (this.mDropDownList.getSelectedItemPosition() >= 0))
    {
      boolean bool = this.mDropDownList.onKeyUp(paramInt, paramKeyEvent);
      if ((bool) && (isConfirmKey(paramInt))) {
        dismiss();
      }
      return bool;
    }
    return false;
  }
  
  public boolean performItemClick(int paramInt)
  {
    if (isShowing())
    {
      if (this.mItemClickListener != null)
      {
        DropDownListView localDropDownListView = this.mDropDownList;
        View localView = localDropDownListView.getChildAt(paramInt - localDropDownListView.getFirstVisiblePosition());
        ListAdapter localListAdapter = localDropDownListView.getAdapter();
        this.mItemClickListener.onItemClick(localDropDownListView, localView, paramInt, localListAdapter.getItemId(paramInt));
      }
      return true;
    }
    return false;
  }
  
  public void postShow()
  {
    this.mHandler.post(this.mShowDropDownRunnable);
  }
  
  public void setAdapter(@Nullable ListAdapter paramListAdapter)
  {
    if (this.mObserver == null) {
      this.mObserver = new PopupDataSetObserver();
    }
    for (;;)
    {
      this.mAdapter = paramListAdapter;
      if (this.mAdapter != null) {
        paramListAdapter.registerDataSetObserver(this.mObserver);
      }
      if (this.mDropDownList != null) {
        this.mDropDownList.setAdapter(this.mAdapter);
      }
      return;
      if (this.mAdapter != null) {
        this.mAdapter.unregisterDataSetObserver(this.mObserver);
      }
    }
  }
  
  public void setAnchorView(@Nullable View paramView)
  {
    this.mDropDownAnchorView = paramView;
  }
  
  public void setAnimationStyle(@StyleRes int paramInt)
  {
    this.mPopup.setAnimationStyle(paramInt);
  }
  
  public void setBackgroundDrawable(@Nullable Drawable paramDrawable)
  {
    this.mPopup.setBackgroundDrawable(paramDrawable);
  }
  
  public void setContentWidth(int paramInt)
  {
    Drawable localDrawable = this.mPopup.getBackground();
    if (localDrawable != null)
    {
      localDrawable.getPadding(this.mTempRect);
      this.mDropDownWidth = (paramInt + (this.mTempRect.left + this.mTempRect.right));
      return;
    }
    setWidth(paramInt);
  }
  
  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public void setDropDownAlwaysVisible(boolean paramBoolean)
  {
    this.mDropDownAlwaysVisible = paramBoolean;
  }
  
  public void setDropDownGravity(int paramInt)
  {
    this.mDropDownGravity = paramInt;
  }
  
  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public void setEpicenterBounds(Rect paramRect)
  {
    this.mEpicenterBounds = paramRect;
  }
  
  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public void setForceIgnoreOutsideTouch(boolean paramBoolean)
  {
    this.mForceIgnoreOutsideTouch = paramBoolean;
  }
  
  public void setHeight(int paramInt)
  {
    this.mDropDownHeight = paramInt;
  }
  
  public void setHorizontalOffset(int paramInt)
  {
    this.mDropDownHorizontalOffset = paramInt;
  }
  
  public void setInputMethodMode(int paramInt)
  {
    this.mPopup.setInputMethodMode(paramInt);
  }
  
  void setListItemExpandMax(int paramInt)
  {
    this.mListItemExpandMaximum = paramInt;
  }
  
  public void setListSelector(Drawable paramDrawable)
  {
    this.mDropDownListHighlight = paramDrawable;
  }
  
  public void setModal(boolean paramBoolean)
  {
    this.mModal = paramBoolean;
    this.mPopup.setFocusable(paramBoolean);
  }
  
  public void setOnDismissListener(@Nullable PopupWindow.OnDismissListener paramOnDismissListener)
  {
    this.mPopup.setOnDismissListener(paramOnDismissListener);
  }
  
  public void setOnItemClickListener(@Nullable AdapterView.OnItemClickListener paramOnItemClickListener)
  {
    this.mItemClickListener = paramOnItemClickListener;
  }
  
  public void setOnItemSelectedListener(@Nullable AdapterView.OnItemSelectedListener paramOnItemSelectedListener)
  {
    this.mItemSelectedListener = paramOnItemSelectedListener;
  }
  
  public void setPromptPosition(int paramInt)
  {
    this.mPromptPosition = paramInt;
  }
  
  public void setPromptView(@Nullable View paramView)
  {
    boolean bool = isShowing();
    if (bool) {
      removePromptView();
    }
    this.mPromptView = paramView;
    if (bool) {
      show();
    }
  }
  
  public void setSelection(int paramInt)
  {
    DropDownListView localDropDownListView = this.mDropDownList;
    if ((isShowing()) && (localDropDownListView != null))
    {
      localDropDownListView.setListSelectionHidden(false);
      localDropDownListView.setSelection(paramInt);
      if ((Build.VERSION.SDK_INT >= 11) && (localDropDownListView.getChoiceMode() != 0)) {
        localDropDownListView.setItemChecked(paramInt, true);
      }
    }
  }
  
  public void setSoftInputMode(int paramInt)
  {
    this.mPopup.setSoftInputMode(paramInt);
  }
  
  public void setVerticalOffset(int paramInt)
  {
    this.mDropDownVerticalOffset = paramInt;
    this.mDropDownVerticalOffsetSet = true;
  }
  
  public void setWidth(int paramInt)
  {
    this.mDropDownWidth = paramInt;
  }
  
  public void setWindowLayoutType(int paramInt)
  {
    this.mDropDownWindowLayoutType = paramInt;
  }
  
  public void show()
  {
    boolean bool1 = true;
    int i = -1;
    int j = buildDropDown();
    boolean bool2 = isInputMethodNotNeeded();
    PopupWindowCompat.setWindowLayoutType(this.mPopup, this.mDropDownWindowLayoutType);
    int n;
    int i1;
    label63:
    int i6;
    label85:
    label100:
    int i4;
    if (this.mPopup.isShowing()) {
      if (this.mDropDownWidth == i)
      {
        n = -1;
        if (this.mDropDownHeight != i) {
          break label279;
        }
        if (!bool2) {
          break label226;
        }
        i1 = j;
        if (!bool2) {
          break label238;
        }
        PopupWindow localPopupWindow6 = this.mPopup;
        if (this.mDropDownWidth != i) {
          break label232;
        }
        i6 = i;
        localPopupWindow6.setWidth(i6);
        this.mPopup.setHeight(0);
        PopupWindow localPopupWindow3 = this.mPopup;
        boolean bool3 = this.mForceIgnoreOutsideTouch;
        boolean bool4 = false;
        if (!bool3)
        {
          boolean bool5 = this.mDropDownAlwaysVisible;
          bool4 = false;
          if (!bool5) {
            bool4 = bool1;
          }
        }
        localPopupWindow3.setOutsideTouchable(bool4);
        PopupWindow localPopupWindow4 = this.mPopup;
        View localView = getAnchorView();
        int i2 = this.mDropDownHorizontalOffset;
        int i3 = this.mDropDownVerticalOffset;
        if (n >= 0) {
          break label303;
        }
        i4 = i;
        label176:
        if (i1 >= 0) {
          break label310;
        }
        label181:
        localPopupWindow4.update(localView, i2, i3, i4, i);
      }
    }
    for (;;)
    {
      return;
      if (this.mDropDownWidth == -2)
      {
        n = getAnchorView().getWidth();
        break;
      }
      n = this.mDropDownWidth;
      break;
      label226:
      i1 = i;
      break label63;
      label232:
      i6 = 0;
      break label85;
      label238:
      PopupWindow localPopupWindow5 = this.mPopup;
      if (this.mDropDownWidth == i) {}
      for (int i5 = i;; i5 = 0)
      {
        localPopupWindow5.setWidth(i5);
        this.mPopup.setHeight(i);
        break;
      }
      label279:
      if (this.mDropDownHeight == -2)
      {
        i1 = j;
        break label100;
      }
      i1 = this.mDropDownHeight;
      break label100;
      label303:
      i4 = n;
      break label176;
      label310:
      i = i1;
      break label181;
      int k;
      label327:
      int m;
      if (this.mDropDownWidth == i)
      {
        k = -1;
        if (this.mDropDownHeight != i) {
          break label541;
        }
        m = -1;
        label338:
        this.mPopup.setWidth(k);
        this.mPopup.setHeight(m);
        setPopupClipToScreenEnabled(bool1);
        PopupWindow localPopupWindow1 = this.mPopup;
        if ((this.mForceIgnoreOutsideTouch) || (this.mDropDownAlwaysVisible)) {
          break label565;
        }
        localPopupWindow1.setOutsideTouchable(bool1);
        this.mPopup.setTouchInterceptor(this.mTouchInterceptor);
        if (sSetEpicenterBoundsMethod == null) {}
      }
      try
      {
        Method localMethod = sSetEpicenterBoundsMethod;
        PopupWindow localPopupWindow2 = this.mPopup;
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = this.mEpicenterBounds;
        localMethod.invoke(localPopupWindow2, arrayOfObject);
        PopupWindowCompat.showAsDropDown(this.mPopup, getAnchorView(), this.mDropDownHorizontalOffset, this.mDropDownVerticalOffset, this.mDropDownGravity);
        this.mDropDownList.setSelection(i);
        if ((!this.mModal) || (this.mDropDownList.isInTouchMode())) {
          clearListSelection();
        }
        if (this.mModal) {
          continue;
        }
        this.mHandler.post(this.mHideSelector);
        return;
        if (this.mDropDownWidth == -2)
        {
          k = getAnchorView().getWidth();
          break label327;
        }
        k = this.mDropDownWidth;
        break label327;
        label541:
        if (this.mDropDownHeight == -2)
        {
          m = j;
          break label338;
        }
        m = this.mDropDownHeight;
        break label338;
        label565:
        bool1 = false;
      }
      catch (Exception localException)
      {
        for (;;)
        {
          Log.e("ListPopupWindow", "Could not invoke setEpicenterBounds on PopupWindow", localException);
        }
      }
    }
  }
  
  private class ListSelectorHider
    implements Runnable
  {
    ListSelectorHider() {}
    
    public void run()
    {
      ListPopupWindow.this.clearListSelection();
    }
  }
  
  private class PopupDataSetObserver
    extends DataSetObserver
  {
    PopupDataSetObserver() {}
    
    public void onChanged()
    {
      if (ListPopupWindow.this.isShowing()) {
        ListPopupWindow.this.show();
      }
    }
    
    public void onInvalidated()
    {
      ListPopupWindow.this.dismiss();
    }
  }
  
  private class PopupScrollListener
    implements AbsListView.OnScrollListener
  {
    PopupScrollListener() {}
    
    public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3) {}
    
    public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt)
    {
      if ((paramInt == 1) && (!ListPopupWindow.this.isInputMethodNotNeeded()) && (ListPopupWindow.this.mPopup.getContentView() != null))
      {
        ListPopupWindow.this.mHandler.removeCallbacks(ListPopupWindow.this.mResizePopupRunnable);
        ListPopupWindow.this.mResizePopupRunnable.run();
      }
    }
  }
  
  private class PopupTouchInterceptor
    implements View.OnTouchListener
  {
    PopupTouchInterceptor() {}
    
    public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
    {
      int i = paramMotionEvent.getAction();
      int j = (int)paramMotionEvent.getX();
      int k = (int)paramMotionEvent.getY();
      if ((i == 0) && (ListPopupWindow.this.mPopup != null) && (ListPopupWindow.this.mPopup.isShowing()) && (j >= 0) && (j < ListPopupWindow.this.mPopup.getWidth()) && (k >= 0) && (k < ListPopupWindow.this.mPopup.getHeight())) {
        ListPopupWindow.this.mHandler.postDelayed(ListPopupWindow.this.mResizePopupRunnable, 250L);
      }
      for (;;)
      {
        return false;
        if (i == 1) {
          ListPopupWindow.this.mHandler.removeCallbacks(ListPopupWindow.this.mResizePopupRunnable);
        }
      }
    }
  }
  
  private class ResizePopupRunnable
    implements Runnable
  {
    ResizePopupRunnable() {}
    
    public void run()
    {
      if ((ListPopupWindow.this.mDropDownList != null) && (ViewCompat.isAttachedToWindow(ListPopupWindow.this.mDropDownList)) && (ListPopupWindow.this.mDropDownList.getCount() > ListPopupWindow.this.mDropDownList.getChildCount()) && (ListPopupWindow.this.mDropDownList.getChildCount() <= ListPopupWindow.this.mListItemExpandMaximum))
      {
        ListPopupWindow.this.mPopup.setInputMethodMode(2);
        ListPopupWindow.this.show();
      }
    }
  }
}
