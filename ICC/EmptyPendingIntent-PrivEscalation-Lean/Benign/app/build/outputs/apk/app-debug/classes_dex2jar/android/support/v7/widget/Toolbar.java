package android.support.v7.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.appcompat.R.attr;
import android.support.v7.appcompat.R.styleable;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.CollapsibleActionView;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuBuilder.Callback;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuPresenter.Callback;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.SubMenuBuilder;
import android.text.Layout;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class Toolbar
  extends ViewGroup
{
  private static final String TAG = "Toolbar";
  private MenuPresenter.Callback mActionMenuPresenterCallback;
  int mButtonGravity;
  ImageButton mCollapseButtonView;
  private CharSequence mCollapseDescription;
  private Drawable mCollapseIcon;
  private boolean mCollapsible;
  private int mContentInsetEndWithActions;
  private int mContentInsetStartWithNavigation;
  private RtlSpacingHelper mContentInsets;
  private boolean mEatingHover;
  private boolean mEatingTouch;
  View mExpandedActionView;
  private ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
  private int mGravity = 8388627;
  private final ArrayList<View> mHiddenViews = new ArrayList();
  private ImageView mLogoView;
  private int mMaxButtonHeight;
  private MenuBuilder.Callback mMenuBuilderCallback;
  private ActionMenuView mMenuView;
  private final ActionMenuView.OnMenuItemClickListener mMenuViewItemClickListener;
  private ImageButton mNavButtonView;
  OnMenuItemClickListener mOnMenuItemClickListener;
  private ActionMenuPresenter mOuterActionMenuPresenter;
  private Context mPopupContext;
  private int mPopupTheme;
  private final Runnable mShowOverflowMenuRunnable;
  private CharSequence mSubtitleText;
  private int mSubtitleTextAppearance;
  private int mSubtitleTextColor;
  private TextView mSubtitleTextView;
  private final int[] mTempMargins = new int[2];
  private final ArrayList<View> mTempViews = new ArrayList();
  private int mTitleMarginBottom;
  private int mTitleMarginEnd;
  private int mTitleMarginStart;
  private int mTitleMarginTop;
  private CharSequence mTitleText;
  private int mTitleTextAppearance;
  private int mTitleTextColor;
  private TextView mTitleTextView;
  private ToolbarWidgetWrapper mWrapper;
  
  public Toolbar(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public Toolbar(Context paramContext, @Nullable AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, R.attr.toolbarStyle);
  }
  
  public Toolbar(Context paramContext, @Nullable AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    ActionMenuView.OnMenuItemClickListener local1 = new ActionMenuView.OnMenuItemClickListener()
    {
      public boolean onMenuItemClick(MenuItem paramAnonymousMenuItem)
      {
        if (Toolbar.this.mOnMenuItemClickListener != null) {
          return Toolbar.this.mOnMenuItemClickListener.onMenuItemClick(paramAnonymousMenuItem);
        }
        return false;
      }
    };
    this.mMenuViewItemClickListener = local1;
    Runnable local2 = new Runnable()
    {
      public void run()
      {
        Toolbar.this.showOverflowMenu();
      }
    };
    this.mShowOverflowMenuRunnable = local2;
    TintTypedArray localTintTypedArray = TintTypedArray.obtainStyledAttributes(getContext(), paramAttributeSet, R.styleable.Toolbar, paramInt, 0);
    this.mTitleTextAppearance = localTintTypedArray.getResourceId(R.styleable.Toolbar_titleTextAppearance, 0);
    this.mSubtitleTextAppearance = localTintTypedArray.getResourceId(R.styleable.Toolbar_subtitleTextAppearance, 0);
    this.mGravity = localTintTypedArray.getInteger(R.styleable.Toolbar_android_gravity, this.mGravity);
    this.mButtonGravity = localTintTypedArray.getInteger(R.styleable.Toolbar_buttonGravity, 48);
    int i = localTintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_titleMargin, 0);
    if (localTintTypedArray.hasValue(R.styleable.Toolbar_titleMargins)) {
      i = localTintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_titleMargins, i);
    }
    this.mTitleMarginBottom = i;
    this.mTitleMarginTop = i;
    this.mTitleMarginEnd = i;
    this.mTitleMarginStart = i;
    int j = localTintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginStart, -1);
    if (j >= 0) {
      this.mTitleMarginStart = j;
    }
    int k = localTintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginEnd, -1);
    if (k >= 0) {
      this.mTitleMarginEnd = k;
    }
    int m = localTintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginTop, -1);
    if (m >= 0) {
      this.mTitleMarginTop = m;
    }
    int n = localTintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginBottom, -1);
    if (n >= 0) {
      this.mTitleMarginBottom = n;
    }
    this.mMaxButtonHeight = localTintTypedArray.getDimensionPixelSize(R.styleable.Toolbar_maxButtonHeight, -1);
    int i1 = localTintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetStart, Integer.MIN_VALUE);
    int i2 = localTintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetEnd, Integer.MIN_VALUE);
    int i3 = localTintTypedArray.getDimensionPixelSize(R.styleable.Toolbar_contentInsetLeft, 0);
    int i4 = localTintTypedArray.getDimensionPixelSize(R.styleable.Toolbar_contentInsetRight, 0);
    ensureContentInsets();
    this.mContentInsets.setAbsolute(i3, i4);
    if ((i1 != Integer.MIN_VALUE) || (i2 != Integer.MIN_VALUE)) {
      this.mContentInsets.setRelative(i1, i2);
    }
    this.mContentInsetStartWithNavigation = localTintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetStartWithNavigation, Integer.MIN_VALUE);
    this.mContentInsetEndWithActions = localTintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetEndWithActions, Integer.MIN_VALUE);
    this.mCollapseIcon = localTintTypedArray.getDrawable(R.styleable.Toolbar_collapseIcon);
    this.mCollapseDescription = localTintTypedArray.getText(R.styleable.Toolbar_collapseContentDescription);
    CharSequence localCharSequence1 = localTintTypedArray.getText(R.styleable.Toolbar_title);
    if (!TextUtils.isEmpty(localCharSequence1)) {
      setTitle(localCharSequence1);
    }
    CharSequence localCharSequence2 = localTintTypedArray.getText(R.styleable.Toolbar_subtitle);
    if (!TextUtils.isEmpty(localCharSequence2)) {
      setSubtitle(localCharSequence2);
    }
    this.mPopupContext = getContext();
    setPopupTheme(localTintTypedArray.getResourceId(R.styleable.Toolbar_popupTheme, 0));
    Drawable localDrawable1 = localTintTypedArray.getDrawable(R.styleable.Toolbar_navigationIcon);
    if (localDrawable1 != null) {
      setNavigationIcon(localDrawable1);
    }
    CharSequence localCharSequence3 = localTintTypedArray.getText(R.styleable.Toolbar_navigationContentDescription);
    if (!TextUtils.isEmpty(localCharSequence3)) {
      setNavigationContentDescription(localCharSequence3);
    }
    Drawable localDrawable2 = localTintTypedArray.getDrawable(R.styleable.Toolbar_logo);
    if (localDrawable2 != null) {
      setLogo(localDrawable2);
    }
    CharSequence localCharSequence4 = localTintTypedArray.getText(R.styleable.Toolbar_logoDescription);
    if (!TextUtils.isEmpty(localCharSequence4)) {
      setLogoDescription(localCharSequence4);
    }
    if (localTintTypedArray.hasValue(R.styleable.Toolbar_titleTextColor)) {
      setTitleTextColor(localTintTypedArray.getColor(R.styleable.Toolbar_titleTextColor, -1));
    }
    if (localTintTypedArray.hasValue(R.styleable.Toolbar_subtitleTextColor)) {
      setSubtitleTextColor(localTintTypedArray.getColor(R.styleable.Toolbar_subtitleTextColor, -1));
    }
    localTintTypedArray.recycle();
  }
  
  private void addCustomViewsWithGravity(List<View> paramList, int paramInt)
  {
    int i = 1;
    if (ViewCompat.getLayoutDirection(this) == i) {}
    int j;
    int k;
    for (;;)
    {
      j = getChildCount();
      k = GravityCompat.getAbsoluteGravity(paramInt, ViewCompat.getLayoutDirection(this));
      paramList.clear();
      if (i == 0) {
        break;
      }
      for (int n = j - 1; n >= 0; n--)
      {
        View localView2 = getChildAt(n);
        LayoutParams localLayoutParams2 = (LayoutParams)localView2.getLayoutParams();
        if ((localLayoutParams2.mViewType == 0) && (shouldLayout(localView2)) && (getChildHorizontalGravity(localLayoutParams2.gravity) == k)) {
          paramList.add(localView2);
        }
      }
      i = 0;
    }
    for (int m = 0; m < j; m++)
    {
      View localView1 = getChildAt(m);
      LayoutParams localLayoutParams1 = (LayoutParams)localView1.getLayoutParams();
      if ((localLayoutParams1.mViewType == 0) && (shouldLayout(localView1)) && (getChildHorizontalGravity(localLayoutParams1.gravity) == k)) {
        paramList.add(localView1);
      }
    }
  }
  
  private void addSystemView(View paramView, boolean paramBoolean)
  {
    ViewGroup.LayoutParams localLayoutParams = paramView.getLayoutParams();
    LayoutParams localLayoutParams1;
    if (localLayoutParams == null) {
      localLayoutParams1 = generateDefaultLayoutParams();
    }
    for (;;)
    {
      localLayoutParams1.mViewType = 1;
      if ((!paramBoolean) || (this.mExpandedActionView == null)) {
        break;
      }
      paramView.setLayoutParams(localLayoutParams1);
      this.mHiddenViews.add(paramView);
      return;
      if (!checkLayoutParams(localLayoutParams)) {
        localLayoutParams1 = generateLayoutParams(localLayoutParams);
      } else {
        localLayoutParams1 = (LayoutParams)localLayoutParams;
      }
    }
    addView(paramView, localLayoutParams1);
  }
  
  private void ensureContentInsets()
  {
    if (this.mContentInsets == null) {
      this.mContentInsets = new RtlSpacingHelper();
    }
  }
  
  private void ensureLogoView()
  {
    if (this.mLogoView == null) {
      this.mLogoView = new AppCompatImageView(getContext());
    }
  }
  
  private void ensureMenu()
  {
    ensureMenuView();
    if (this.mMenuView.peekMenu() == null)
    {
      MenuBuilder localMenuBuilder = (MenuBuilder)this.mMenuView.getMenu();
      if (this.mExpandedMenuPresenter == null) {
        this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter();
      }
      this.mMenuView.setExpandedActionViewsExclusive(true);
      localMenuBuilder.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
    }
  }
  
  private void ensureMenuView()
  {
    if (this.mMenuView == null)
    {
      this.mMenuView = new ActionMenuView(getContext());
      this.mMenuView.setPopupTheme(this.mPopupTheme);
      this.mMenuView.setOnMenuItemClickListener(this.mMenuViewItemClickListener);
      this.mMenuView.setMenuCallbacks(this.mActionMenuPresenterCallback, this.mMenuBuilderCallback);
      LayoutParams localLayoutParams = generateDefaultLayoutParams();
      localLayoutParams.gravity = (0x800005 | 0x70 & this.mButtonGravity);
      this.mMenuView.setLayoutParams(localLayoutParams);
      addSystemView(this.mMenuView, false);
    }
  }
  
  private void ensureNavButtonView()
  {
    if (this.mNavButtonView == null)
    {
      this.mNavButtonView = new AppCompatImageButton(getContext(), null, R.attr.toolbarNavigationButtonStyle);
      LayoutParams localLayoutParams = generateDefaultLayoutParams();
      localLayoutParams.gravity = (0x800003 | 0x70 & this.mButtonGravity);
      this.mNavButtonView.setLayoutParams(localLayoutParams);
    }
  }
  
  private int getChildHorizontalGravity(int paramInt)
  {
    int i = ViewCompat.getLayoutDirection(this);
    int j = 0x7 & GravityCompat.getAbsoluteGravity(paramInt, i);
    switch (j)
    {
    case 2: 
    case 4: 
    default: 
      if (i != 1) {
        break;
      }
    }
    for (int k = 5;; k = 3)
    {
      j = k;
      return j;
    }
  }
  
  private int getChildTop(View paramView, int paramInt)
  {
    LayoutParams localLayoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = paramView.getMeasuredHeight();
    int j;
    int k;
    int m;
    int n;
    int i1;
    if (paramInt > 0)
    {
      j = (i - paramInt) / 2;
      switch (getChildVerticalGravity(localLayoutParams.gravity))
      {
      default: 
        k = getPaddingTop();
        m = getPaddingBottom();
        n = getHeight();
        i1 = (n - k - m - i) / 2;
        if (i1 < localLayoutParams.topMargin) {
          i1 = localLayoutParams.topMargin;
        }
        break;
      }
    }
    for (;;)
    {
      return k + i1;
      j = 0;
      break;
      return getPaddingTop() - j;
      return getHeight() - getPaddingBottom() - i - localLayoutParams.bottomMargin - j;
      int i2 = n - m - i - i1 - k;
      if (i2 < localLayoutParams.bottomMargin) {
        i1 = Math.max(0, i1 - (localLayoutParams.bottomMargin - i2));
      }
    }
  }
  
  private int getChildVerticalGravity(int paramInt)
  {
    int i = paramInt & 0x70;
    switch (i)
    {
    default: 
      i = 0x70 & this.mGravity;
    }
    return i;
  }
  
  private int getHorizontalMargins(View paramView)
  {
    ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    return MarginLayoutParamsCompat.getMarginStart(localMarginLayoutParams) + MarginLayoutParamsCompat.getMarginEnd(localMarginLayoutParams);
  }
  
  private MenuInflater getMenuInflater()
  {
    return new SupportMenuInflater(getContext());
  }
  
  private int getVerticalMargins(View paramView)
  {
    ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    return localMarginLayoutParams.topMargin + localMarginLayoutParams.bottomMargin;
  }
  
  private int getViewListMeasuredWidth(List<View> paramList, int[] paramArrayOfInt)
  {
    int i = paramArrayOfInt[0];
    int j = paramArrayOfInt[1];
    int k = 0;
    int m = paramList.size();
    for (int n = 0; n < m; n++)
    {
      View localView = (View)paramList.get(n);
      LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
      int i1 = localLayoutParams.leftMargin - i;
      int i2 = localLayoutParams.rightMargin - j;
      int i3 = Math.max(0, i1);
      int i4 = Math.max(0, i2);
      i = Math.max(0, -i1);
      j = Math.max(0, -i2);
      k += i4 + (i3 + localView.getMeasuredWidth());
    }
    return k;
  }
  
  private boolean isChildOrHidden(View paramView)
  {
    return (paramView.getParent() == this) || (this.mHiddenViews.contains(paramView));
  }
  
  private static boolean isCustomView(View paramView)
  {
    return ((LayoutParams)paramView.getLayoutParams()).mViewType == 0;
  }
  
  private int layoutChildLeft(View paramView, int paramInt1, int[] paramArrayOfInt, int paramInt2)
  {
    LayoutParams localLayoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = localLayoutParams.leftMargin - paramArrayOfInt[0];
    int j = paramInt1 + Math.max(0, i);
    paramArrayOfInt[0] = Math.max(0, -i);
    int k = getChildTop(paramView, paramInt2);
    int m = paramView.getMeasuredWidth();
    paramView.layout(j, k, j + m, k + paramView.getMeasuredHeight());
    return j + (m + localLayoutParams.rightMargin);
  }
  
  private int layoutChildRight(View paramView, int paramInt1, int[] paramArrayOfInt, int paramInt2)
  {
    LayoutParams localLayoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = localLayoutParams.rightMargin - paramArrayOfInt[1];
    int j = paramInt1 - Math.max(0, i);
    paramArrayOfInt[1] = Math.max(0, -i);
    int k = getChildTop(paramView, paramInt2);
    int m = paramView.getMeasuredWidth();
    paramView.layout(j - m, k, j, k + paramView.getMeasuredHeight());
    return j - (m + localLayoutParams.leftMargin);
  }
  
  private int measureChildCollapseMargins(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt)
  {
    ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    int i = localMarginLayoutParams.leftMargin - paramArrayOfInt[0];
    int j = localMarginLayoutParams.rightMargin - paramArrayOfInt[1];
    int k = Math.max(0, i) + Math.max(0, j);
    paramArrayOfInt[0] = Math.max(0, -i);
    paramArrayOfInt[1] = Math.max(0, -j);
    paramView.measure(getChildMeasureSpec(paramInt1, paramInt2 + (k + (getPaddingLeft() + getPaddingRight())), localMarginLayoutParams.width), getChildMeasureSpec(paramInt3, paramInt4 + (getPaddingTop() + getPaddingBottom() + localMarginLayoutParams.topMargin + localMarginLayoutParams.bottomMargin), localMarginLayoutParams.height));
    return k + paramView.getMeasuredWidth();
  }
  
  private void measureChildConstrained(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    int i = getChildMeasureSpec(paramInt1, paramInt2 + (getPaddingLeft() + getPaddingRight() + localMarginLayoutParams.leftMargin + localMarginLayoutParams.rightMargin), localMarginLayoutParams.width);
    int j = getChildMeasureSpec(paramInt3, paramInt4 + (getPaddingTop() + getPaddingBottom() + localMarginLayoutParams.topMargin + localMarginLayoutParams.bottomMargin), localMarginLayoutParams.height);
    int k = View.MeasureSpec.getMode(j);
    if ((k != 1073741824) && (paramInt5 >= 0)) {
      if (k == 0) {
        break label135;
      }
    }
    label135:
    for (int m = Math.min(View.MeasureSpec.getSize(j), paramInt5);; m = paramInt5)
    {
      j = View.MeasureSpec.makeMeasureSpec(m, 1073741824);
      paramView.measure(i, j);
      return;
    }
  }
  
  private void postShowOverflowMenu()
  {
    removeCallbacks(this.mShowOverflowMenuRunnable);
    post(this.mShowOverflowMenuRunnable);
  }
  
  private boolean shouldCollapse()
  {
    if (!this.mCollapsible) {
      return false;
    }
    int i = getChildCount();
    for (int j = 0;; j++)
    {
      if (j >= i) {
        break label55;
      }
      View localView = getChildAt(j);
      if ((shouldLayout(localView)) && (localView.getMeasuredWidth() > 0) && (localView.getMeasuredHeight() > 0)) {
        break;
      }
    }
    label55:
    return true;
  }
  
  private boolean shouldLayout(View paramView)
  {
    return (paramView != null) && (paramView.getParent() == this) && (paramView.getVisibility() != 8);
  }
  
  void addChildrenForExpandedActionView()
  {
    for (int i = -1 + this.mHiddenViews.size(); i >= 0; i--) {
      addView((View)this.mHiddenViews.get(i));
    }
    this.mHiddenViews.clear();
  }
  
  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public boolean canShowOverflowMenu()
  {
    return (getVisibility() == 0) && (this.mMenuView != null) && (this.mMenuView.isOverflowReserved());
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return (super.checkLayoutParams(paramLayoutParams)) && ((paramLayoutParams instanceof LayoutParams));
  }
  
  public void collapseActionView()
  {
    if (this.mExpandedMenuPresenter == null) {}
    for (MenuItemImpl localMenuItemImpl = null;; localMenuItemImpl = this.mExpandedMenuPresenter.mCurrentExpandedItem)
    {
      if (localMenuItemImpl != null) {
        localMenuItemImpl.collapseActionView();
      }
      return;
    }
  }
  
  public void dismissPopupMenus()
  {
    if (this.mMenuView != null) {
      this.mMenuView.dismissPopupMenus();
    }
  }
  
  void ensureCollapseButtonView()
  {
    if (this.mCollapseButtonView == null)
    {
      this.mCollapseButtonView = new AppCompatImageButton(getContext(), null, R.attr.toolbarNavigationButtonStyle);
      this.mCollapseButtonView.setImageDrawable(this.mCollapseIcon);
      this.mCollapseButtonView.setContentDescription(this.mCollapseDescription);
      LayoutParams localLayoutParams = generateDefaultLayoutParams();
      localLayoutParams.gravity = (0x800003 | 0x70 & this.mButtonGravity);
      localLayoutParams.mViewType = 2;
      this.mCollapseButtonView.setLayoutParams(localLayoutParams);
      this.mCollapseButtonView.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          Toolbar.this.collapseActionView();
        }
      });
    }
  }
  
  protected LayoutParams generateDefaultLayoutParams()
  {
    return new LayoutParams(-2, -2);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    if ((paramLayoutParams instanceof LayoutParams)) {
      return new LayoutParams((LayoutParams)paramLayoutParams);
    }
    if ((paramLayoutParams instanceof ActionBar.LayoutParams)) {
      return new LayoutParams((ActionBar.LayoutParams)paramLayoutParams);
    }
    if ((paramLayoutParams instanceof ViewGroup.MarginLayoutParams)) {
      return new LayoutParams((ViewGroup.MarginLayoutParams)paramLayoutParams);
    }
    return new LayoutParams(paramLayoutParams);
  }
  
  public int getContentInsetEnd()
  {
    if (this.mContentInsets != null) {
      return this.mContentInsets.getEnd();
    }
    return 0;
  }
  
  public int getContentInsetEndWithActions()
  {
    if (this.mContentInsetEndWithActions != Integer.MIN_VALUE) {
      return this.mContentInsetEndWithActions;
    }
    return getContentInsetEnd();
  }
  
  public int getContentInsetLeft()
  {
    if (this.mContentInsets != null) {
      return this.mContentInsets.getLeft();
    }
    return 0;
  }
  
  public int getContentInsetRight()
  {
    if (this.mContentInsets != null) {
      return this.mContentInsets.getRight();
    }
    return 0;
  }
  
  public int getContentInsetStart()
  {
    if (this.mContentInsets != null) {
      return this.mContentInsets.getStart();
    }
    return 0;
  }
  
  public int getContentInsetStartWithNavigation()
  {
    if (this.mContentInsetStartWithNavigation != Integer.MIN_VALUE) {
      return this.mContentInsetStartWithNavigation;
    }
    return getContentInsetStart();
  }
  
  public int getCurrentContentInsetEnd()
  {
    ActionMenuView localActionMenuView = this.mMenuView;
    int i = 0;
    if (localActionMenuView != null)
    {
      MenuBuilder localMenuBuilder = this.mMenuView.peekMenu();
      if ((localMenuBuilder == null) || (!localMenuBuilder.hasVisibleItems())) {
        break label52;
      }
    }
    label52:
    for (i = 1; i != 0; i = 0) {
      return Math.max(getContentInsetEnd(), Math.max(this.mContentInsetEndWithActions, 0));
    }
    return getContentInsetEnd();
  }
  
  public int getCurrentContentInsetLeft()
  {
    if (ViewCompat.getLayoutDirection(this) == 1) {
      return getCurrentContentInsetEnd();
    }
    return getCurrentContentInsetStart();
  }
  
  public int getCurrentContentInsetRight()
  {
    if (ViewCompat.getLayoutDirection(this) == 1) {
      return getCurrentContentInsetStart();
    }
    return getCurrentContentInsetEnd();
  }
  
  public int getCurrentContentInsetStart()
  {
    if (getNavigationIcon() != null) {
      return Math.max(getContentInsetStart(), Math.max(this.mContentInsetStartWithNavigation, 0));
    }
    return getContentInsetStart();
  }
  
  public Drawable getLogo()
  {
    if (this.mLogoView != null) {
      return this.mLogoView.getDrawable();
    }
    return null;
  }
  
  public CharSequence getLogoDescription()
  {
    if (this.mLogoView != null) {
      return this.mLogoView.getContentDescription();
    }
    return null;
  }
  
  public Menu getMenu()
  {
    ensureMenu();
    return this.mMenuView.getMenu();
  }
  
  @Nullable
  public CharSequence getNavigationContentDescription()
  {
    if (this.mNavButtonView != null) {
      return this.mNavButtonView.getContentDescription();
    }
    return null;
  }
  
  @Nullable
  public Drawable getNavigationIcon()
  {
    if (this.mNavButtonView != null) {
      return this.mNavButtonView.getDrawable();
    }
    return null;
  }
  
  @Nullable
  public Drawable getOverflowIcon()
  {
    ensureMenu();
    return this.mMenuView.getOverflowIcon();
  }
  
  public int getPopupTheme()
  {
    return this.mPopupTheme;
  }
  
  public CharSequence getSubtitle()
  {
    return this.mSubtitleText;
  }
  
  public CharSequence getTitle()
  {
    return this.mTitleText;
  }
  
  public int getTitleMarginBottom()
  {
    return this.mTitleMarginBottom;
  }
  
  public int getTitleMarginEnd()
  {
    return this.mTitleMarginEnd;
  }
  
  public int getTitleMarginStart()
  {
    return this.mTitleMarginStart;
  }
  
  public int getTitleMarginTop()
  {
    return this.mTitleMarginTop;
  }
  
  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public DecorToolbar getWrapper()
  {
    if (this.mWrapper == null) {
      this.mWrapper = new ToolbarWidgetWrapper(this, true);
    }
    return this.mWrapper;
  }
  
  public boolean hasExpandedActionView()
  {
    return (this.mExpandedMenuPresenter != null) && (this.mExpandedMenuPresenter.mCurrentExpandedItem != null);
  }
  
  public boolean hideOverflowMenu()
  {
    return (this.mMenuView != null) && (this.mMenuView.hideOverflowMenu());
  }
  
  public void inflateMenu(@MenuRes int paramInt)
  {
    getMenuInflater().inflate(paramInt, getMenu());
  }
  
  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public boolean isOverflowMenuShowPending()
  {
    return (this.mMenuView != null) && (this.mMenuView.isOverflowMenuShowPending());
  }
  
  public boolean isOverflowMenuShowing()
  {
    return (this.mMenuView != null) && (this.mMenuView.isOverflowMenuShowing());
  }
  
  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public boolean isTitleTruncated()
  {
    if (this.mTitleTextView == null) {}
    for (;;)
    {
      return false;
      Layout localLayout = this.mTitleTextView.getLayout();
      if (localLayout != null)
      {
        int i = localLayout.getLineCount();
        for (int j = 0; j < i; j++) {
          if (localLayout.getEllipsisCount(j) > 0) {
            return true;
          }
        }
      }
    }
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    removeCallbacks(this.mShowOverflowMenuRunnable);
  }
  
  public boolean onHoverEvent(MotionEvent paramMotionEvent)
  {
    int i = MotionEventCompat.getActionMasked(paramMotionEvent);
    if (i == 9) {
      this.mEatingHover = false;
    }
    if (!this.mEatingHover)
    {
      boolean bool = super.onHoverEvent(paramMotionEvent);
      if ((i == 9) && (!bool)) {
        this.mEatingHover = true;
      }
    }
    if ((i == 10) || (i == 3)) {
      this.mEatingHover = false;
    }
    return true;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i;
    int j;
    int k;
    int m;
    int n;
    int i1;
    int i2;
    int i3;
    int i4;
    int[] arrayOfInt;
    int i6;
    label96:
    label128:
    label160:
    label192:
    int i9;
    int i11;
    label296:
    label328:
    boolean bool1;
    boolean bool2;
    int i12;
    TextView localTextView1;
    label453:
    TextView localTextView2;
    label464:
    LayoutParams localLayoutParams1;
    LayoutParams localLayoutParams2;
    int i13;
    label517:
    int i42;
    label598:
    int i14;
    if (ViewCompat.getLayoutDirection(this) == 1)
    {
      i = 1;
      j = getWidth();
      k = getHeight();
      m = getPaddingLeft();
      n = getPaddingRight();
      i1 = getPaddingTop();
      i2 = getPaddingBottom();
      i3 = m;
      i4 = j - n;
      arrayOfInt = this.mTempMargins;
      arrayOfInt[1] = 0;
      arrayOfInt[0] = 0;
      int i5 = ViewCompat.getMinimumHeight(this);
      if (i5 < 0) {
        break label897;
      }
      i6 = Math.min(i5, paramInt4 - paramInt2);
      if (shouldLayout(this.mNavButtonView))
      {
        if (i == 0) {
          break label903;
        }
        i4 = layoutChildRight(this.mNavButtonView, i4, arrayOfInt, i6);
      }
      if (shouldLayout(this.mCollapseButtonView))
      {
        if (i == 0) {
          break label922;
        }
        i4 = layoutChildRight(this.mCollapseButtonView, i4, arrayOfInt, i6);
      }
      if (shouldLayout(this.mMenuView))
      {
        if (i == 0) {
          break label941;
        }
        i3 = layoutChildLeft(this.mMenuView, i3, arrayOfInt, i6);
      }
      int i7 = getCurrentContentInsetLeft();
      int i8 = getCurrentContentInsetRight();
      arrayOfInt[0] = Math.max(0, i7 - i3);
      arrayOfInt[1] = Math.max(0, i8 - (j - n - i4));
      i9 = Math.max(i3, i7);
      int i10 = j - n - i8;
      i11 = Math.min(i4, i10);
      if (shouldLayout(this.mExpandedActionView))
      {
        if (i == 0) {
          break label960;
        }
        i11 = layoutChildRight(this.mExpandedActionView, i11, arrayOfInt, i6);
      }
      if (shouldLayout(this.mLogoView))
      {
        if (i == 0) {
          break label979;
        }
        i11 = layoutChildRight(this.mLogoView, i11, arrayOfInt, i6);
      }
      bool1 = shouldLayout(this.mTitleTextView);
      bool2 = shouldLayout(this.mSubtitleTextView);
      i12 = 0;
      if (bool1)
      {
        LayoutParams localLayoutParams8 = (LayoutParams)this.mTitleTextView.getLayoutParams();
        i12 = 0 + (localLayoutParams8.topMargin + this.mTitleTextView.getMeasuredHeight() + localLayoutParams8.bottomMargin);
      }
      if (bool2)
      {
        LayoutParams localLayoutParams7 = (LayoutParams)this.mSubtitleTextView.getLayoutParams();
        i12 += localLayoutParams7.topMargin + this.mSubtitleTextView.getMeasuredHeight() + localLayoutParams7.bottomMargin;
      }
      if ((bool1) || (bool2))
      {
        if (!bool1) {
          break label998;
        }
        localTextView1 = this.mTitleTextView;
        if (!bool2) {
          break label1007;
        }
        localTextView2 = this.mSubtitleTextView;
        localLayoutParams1 = (LayoutParams)localTextView1.getLayoutParams();
        localLayoutParams2 = (LayoutParams)localTextView2.getLayoutParams();
        if (((!bool1) || (this.mTitleTextView.getMeasuredWidth() <= 0)) && ((!bool2) || (this.mSubtitleTextView.getMeasuredWidth() <= 0))) {
          break label1016;
        }
        i13 = 1;
        switch (0x70 & this.mGravity)
        {
        default: 
          i42 = (k - i1 - i2 - i12) / 2;
          int i43 = localLayoutParams1.topMargin + this.mTitleMarginTop;
          if (i42 < i43)
          {
            i42 = localLayoutParams1.topMargin + this.mTitleMarginTop;
            i14 = i1 + i42;
            label605:
            if (i == 0) {
              break label1128;
            }
            if (i13 == 0) {
              break label1122;
            }
          }
          break;
        }
      }
    }
    label897:
    label903:
    label922:
    label941:
    label960:
    label979:
    label998:
    label1007:
    label1016:
    label1122:
    for (int i33 = this.mTitleMarginStart;; i33 = 0)
    {
      int i34 = i33 - arrayOfInt[1];
      i11 -= Math.max(0, i34);
      arrayOfInt[1] = Math.max(0, -i34);
      int i35 = i11;
      int i36 = i11;
      if (bool1)
      {
        LayoutParams localLayoutParams6 = (LayoutParams)this.mTitleTextView.getLayoutParams();
        int i40 = i35 - this.mTitleTextView.getMeasuredWidth();
        int i41 = i14 + this.mTitleTextView.getMeasuredHeight();
        this.mTitleTextView.layout(i40, i14, i35, i41);
        i35 = i40 - this.mTitleMarginEnd;
        i14 = i41 + localLayoutParams6.bottomMargin;
      }
      if (bool2)
      {
        LayoutParams localLayoutParams5 = (LayoutParams)this.mSubtitleTextView.getLayoutParams();
        int i37 = i14 + localLayoutParams5.topMargin;
        int i38 = i36 - this.mSubtitleTextView.getMeasuredWidth();
        int i39 = i37 + this.mSubtitleTextView.getMeasuredHeight();
        this.mSubtitleTextView.layout(i38, i37, i36, i39);
        i36 -= this.mTitleMarginEnd;
        (i39 + localLayoutParams5.bottomMargin);
      }
      if (i13 != 0) {
        i11 = Math.min(i35, i36);
      }
      addCustomViewsWithGravity(this.mTempViews, 3);
      int i19 = this.mTempViews.size();
      for (int i20 = 0; i20 < i19; i20++) {
        i9 = layoutChildLeft((View)this.mTempViews.get(i20), i9, arrayOfInt, i6);
      }
      i = 0;
      break;
      i6 = 0;
      break label96;
      i3 = layoutChildLeft(this.mNavButtonView, i3, arrayOfInt, i6);
      break label128;
      i3 = layoutChildLeft(this.mCollapseButtonView, i3, arrayOfInt, i6);
      break label160;
      i4 = layoutChildRight(this.mMenuView, i4, arrayOfInt, i6);
      break label192;
      i9 = layoutChildLeft(this.mExpandedActionView, i9, arrayOfInt, i6);
      break label296;
      i9 = layoutChildLeft(this.mLogoView, i9, arrayOfInt, i6);
      break label328;
      localTextView1 = this.mSubtitleTextView;
      break label453;
      localTextView2 = this.mTitleTextView;
      break label464;
      i13 = 0;
      break label517;
      i14 = getPaddingTop() + localLayoutParams1.topMargin + this.mTitleMarginTop;
      break label605;
      int i44 = k - i2 - i12 - i42 - i1;
      if (i44 >= localLayoutParams1.bottomMargin + this.mTitleMarginBottom) {
        break label598;
      }
      i42 = Math.max(0, i42 - (localLayoutParams2.bottomMargin + this.mTitleMarginBottom - i44));
      break label598;
      i14 = k - i2 - localLayoutParams2.bottomMargin - this.mTitleMarginBottom - i12;
      break label605;
    }
    label1128:
    if (i13 != 0) {}
    for (int i15 = this.mTitleMarginStart;; i15 = 0)
    {
      int i16 = i15 - arrayOfInt[0];
      i9 += Math.max(0, i16);
      arrayOfInt[0] = Math.max(0, -i16);
      int i17 = i9;
      int i18 = i9;
      if (bool1)
      {
        LayoutParams localLayoutParams4 = (LayoutParams)this.mTitleTextView.getLayoutParams();
        int i31 = i17 + this.mTitleTextView.getMeasuredWidth();
        int i32 = i14 + this.mTitleTextView.getMeasuredHeight();
        this.mTitleTextView.layout(i17, i14, i31, i32);
        i17 = i31 + this.mTitleMarginEnd;
        i14 = i32 + localLayoutParams4.bottomMargin;
      }
      if (bool2)
      {
        LayoutParams localLayoutParams3 = (LayoutParams)this.mSubtitleTextView.getLayoutParams();
        int i28 = i14 + localLayoutParams3.topMargin;
        int i29 = i18 + this.mSubtitleTextView.getMeasuredWidth();
        int i30 = i28 + this.mSubtitleTextView.getMeasuredHeight();
        this.mSubtitleTextView.layout(i18, i28, i29, i30);
        i18 = i29 + this.mTitleMarginEnd;
        (i30 + localLayoutParams3.bottomMargin);
      }
      if (i13 == 0) {
        break;
      }
      i9 = Math.max(i17, i18);
      break;
    }
    addCustomViewsWithGravity(this.mTempViews, 5);
    int i21 = this.mTempViews.size();
    for (int i22 = 0; i22 < i21; i22++) {
      i11 = layoutChildRight((View)this.mTempViews.get(i22), i11, arrayOfInt, i6);
    }
    addCustomViewsWithGravity(this.mTempViews, 1);
    int i23 = getViewListMeasuredWidth(this.mTempViews, arrayOfInt);
    int i24 = m + (j - m - n) / 2 - i23 / 2;
    int i25 = i24 + i23;
    if (i24 < i9) {
      i24 = i9;
    }
    for (;;)
    {
      int i26 = this.mTempViews.size();
      for (int i27 = 0; i27 < i26; i27++) {
        i24 = layoutChildLeft((View)this.mTempViews.get(i27), i24, arrayOfInt, i6);
      }
      if (i25 > i11) {
        i24 -= i25 - i11;
      }
    }
    this.mTempViews.clear();
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int[] arrayOfInt = this.mTempMargins;
    int j;
    int i;
    int k;
    int m;
    int i5;
    int i7;
    label519:
    View localView;
    if (ViewUtils.isLayoutRtl(this))
    {
      j = 1;
      i = 0;
      boolean bool1 = shouldLayout(this.mNavButtonView);
      k = 0;
      m = 0;
      int n = 0;
      if (bool1)
      {
        measureChildConstrained(this.mNavButtonView, paramInt1, 0, paramInt2, 0, this.mMaxButtonHeight);
        n = this.mNavButtonView.getMeasuredWidth() + getHorizontalMargins(this.mNavButtonView);
        m = Math.max(0, this.mNavButtonView.getMeasuredHeight() + getVerticalMargins(this.mNavButtonView));
        k = ViewUtils.combineMeasuredStates(0, ViewCompat.getMeasuredState(this.mNavButtonView));
      }
      if (shouldLayout(this.mCollapseButtonView))
      {
        measureChildConstrained(this.mCollapseButtonView, paramInt1, 0, paramInt2, 0, this.mMaxButtonHeight);
        n = this.mCollapseButtonView.getMeasuredWidth() + getHorizontalMargins(this.mCollapseButtonView);
        int i29 = this.mCollapseButtonView.getMeasuredHeight() + getVerticalMargins(this.mCollapseButtonView);
        m = Math.max(m, i29);
        int i30 = ViewCompat.getMeasuredState(this.mCollapseButtonView);
        k = ViewUtils.combineMeasuredStates(k, i30);
      }
      int i1 = getCurrentContentInsetStart();
      int i2 = 0 + Math.max(i1, n);
      arrayOfInt[j] = Math.max(0, i1 - n);
      boolean bool2 = shouldLayout(this.mMenuView);
      int i3 = 0;
      if (bool2)
      {
        measureChildConstrained(this.mMenuView, paramInt1, i2, paramInt2, 0, this.mMaxButtonHeight);
        i3 = this.mMenuView.getMeasuredWidth() + getHorizontalMargins(this.mMenuView);
        int i27 = this.mMenuView.getMeasuredHeight() + getVerticalMargins(this.mMenuView);
        m = Math.max(m, i27);
        int i28 = ViewCompat.getMeasuredState(this.mMenuView);
        k = ViewUtils.combineMeasuredStates(k, i28);
      }
      int i4 = getCurrentContentInsetEnd();
      i5 = i2 + Math.max(i4, i3);
      arrayOfInt[i] = Math.max(0, i4 - i3);
      if (shouldLayout(this.mExpandedActionView))
      {
        i5 += measureChildCollapseMargins(this.mExpandedActionView, paramInt1, i5, paramInt2, 0, arrayOfInt);
        int i25 = this.mExpandedActionView.getMeasuredHeight() + getVerticalMargins(this.mExpandedActionView);
        m = Math.max(m, i25);
        int i26 = ViewCompat.getMeasuredState(this.mExpandedActionView);
        k = ViewUtils.combineMeasuredStates(k, i26);
      }
      if (shouldLayout(this.mLogoView))
      {
        i5 += measureChildCollapseMargins(this.mLogoView, paramInt1, i5, paramInt2, 0, arrayOfInt);
        int i23 = this.mLogoView.getMeasuredHeight() + getVerticalMargins(this.mLogoView);
        m = Math.max(m, i23);
        int i24 = ViewCompat.getMeasuredState(this.mLogoView);
        k = ViewUtils.combineMeasuredStates(k, i24);
      }
      int i6 = getChildCount();
      i7 = 0;
      if (i7 >= i6) {
        break label631;
      }
      localView = getChildAt(i7);
      if ((((LayoutParams)localView.getLayoutParams()).mViewType == 0) && (shouldLayout(localView))) {
        break label572;
      }
    }
    for (;;)
    {
      i7++;
      break label519;
      i = 1;
      j = 0;
      break;
      label572:
      i5 += measureChildCollapseMargins(localView, paramInt1, i5, paramInt2, 0, arrayOfInt);
      int i21 = localView.getMeasuredHeight() + getVerticalMargins(localView);
      m = Math.max(m, i21);
      int i22 = ViewCompat.getMeasuredState(localView);
      k = ViewUtils.combineMeasuredStates(k, i22);
    }
    label631:
    int i8 = this.mTitleMarginTop + this.mTitleMarginBottom;
    int i9 = this.mTitleMarginStart + this.mTitleMarginEnd;
    boolean bool3 = shouldLayout(this.mTitleTextView);
    int i10 = 0;
    int i11 = 0;
    if (bool3)
    {
      measureChildCollapseMargins(this.mTitleTextView, paramInt1, i5 + i9, paramInt2, i8, arrayOfInt);
      i11 = this.mTitleTextView.getMeasuredWidth() + getHorizontalMargins(this.mTitleTextView);
      i10 = this.mTitleTextView.getMeasuredHeight() + getVerticalMargins(this.mTitleTextView);
      int i20 = ViewCompat.getMeasuredState(this.mTitleTextView);
      k = ViewUtils.combineMeasuredStates(k, i20);
    }
    if (shouldLayout(this.mSubtitleTextView))
    {
      int i18 = measureChildCollapseMargins(this.mSubtitleTextView, paramInt1, i5 + i9, paramInt2, i10 + i8, arrayOfInt);
      i11 = Math.max(i11, i18);
      i10 += this.mSubtitleTextView.getMeasuredHeight() + getVerticalMargins(this.mSubtitleTextView);
      int i19 = ViewCompat.getMeasuredState(this.mSubtitleTextView);
      k = ViewUtils.combineMeasuredStates(k, i19);
    }
    int i12 = i5 + i11;
    int i13 = Math.max(m, i10);
    int i14 = i12 + (getPaddingLeft() + getPaddingRight());
    int i15 = i13 + (getPaddingTop() + getPaddingBottom());
    int i16 = ViewCompat.resolveSizeAndState(Math.max(i14, getSuggestedMinimumWidth()), paramInt1, 0xFF000000 & k);
    int i17 = ViewCompat.resolveSizeAndState(Math.max(i15, getSuggestedMinimumHeight()), paramInt2, k << 16);
    if (shouldCollapse()) {
      i17 = 0;
    }
    setMeasuredDimension(i16, i17);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if (!(paramParcelable instanceof SavedState))
    {
      super.onRestoreInstanceState(paramParcelable);
      return;
    }
    SavedState localSavedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(localSavedState.getSuperState());
    if (this.mMenuView != null) {}
    for (MenuBuilder localMenuBuilder = this.mMenuView.peekMenu();; localMenuBuilder = null)
    {
      if ((localSavedState.expandedMenuItemId != 0) && (this.mExpandedMenuPresenter != null) && (localMenuBuilder != null))
      {
        MenuItem localMenuItem = localMenuBuilder.findItem(localSavedState.expandedMenuItemId);
        if (localMenuItem != null) {
          MenuItemCompat.expandActionView(localMenuItem);
        }
      }
      if (!localSavedState.isOverflowOpen) {
        break;
      }
      postShowOverflowMenu();
      return;
    }
  }
  
  public void onRtlPropertiesChanged(int paramInt)
  {
    int i = 1;
    if (Build.VERSION.SDK_INT >= 17) {
      super.onRtlPropertiesChanged(paramInt);
    }
    ensureContentInsets();
    RtlSpacingHelper localRtlSpacingHelper = this.mContentInsets;
    if (paramInt == i) {}
    for (;;)
    {
      localRtlSpacingHelper.setDirection(i);
      return;
      i = 0;
    }
  }
  
  protected Parcelable onSaveInstanceState()
  {
    SavedState localSavedState = new SavedState(super.onSaveInstanceState());
    if ((this.mExpandedMenuPresenter != null) && (this.mExpandedMenuPresenter.mCurrentExpandedItem != null)) {
      localSavedState.expandedMenuItemId = this.mExpandedMenuPresenter.mCurrentExpandedItem.getItemId();
    }
    localSavedState.isOverflowOpen = isOverflowMenuShowing();
    return localSavedState;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = MotionEventCompat.getActionMasked(paramMotionEvent);
    if (i == 0) {
      this.mEatingTouch = false;
    }
    if (!this.mEatingTouch)
    {
      boolean bool = super.onTouchEvent(paramMotionEvent);
      if ((i == 0) && (!bool)) {
        this.mEatingTouch = true;
      }
    }
    if ((i == 1) || (i == 3)) {
      this.mEatingTouch = false;
    }
    return true;
  }
  
  void removeChildrenForExpandedActionView()
  {
    for (int i = -1 + getChildCount(); i >= 0; i--)
    {
      View localView = getChildAt(i);
      if ((((LayoutParams)localView.getLayoutParams()).mViewType != 2) && (localView != this.mMenuView))
      {
        removeViewAt(i);
        this.mHiddenViews.add(localView);
      }
    }
  }
  
  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public void setCollapsible(boolean paramBoolean)
  {
    this.mCollapsible = paramBoolean;
    requestLayout();
  }
  
  public void setContentInsetEndWithActions(int paramInt)
  {
    if (paramInt < 0) {
      paramInt = Integer.MIN_VALUE;
    }
    if (paramInt != this.mContentInsetEndWithActions)
    {
      this.mContentInsetEndWithActions = paramInt;
      if (getNavigationIcon() != null) {
        requestLayout();
      }
    }
  }
  
  public void setContentInsetStartWithNavigation(int paramInt)
  {
    if (paramInt < 0) {
      paramInt = Integer.MIN_VALUE;
    }
    if (paramInt != this.mContentInsetStartWithNavigation)
    {
      this.mContentInsetStartWithNavigation = paramInt;
      if (getNavigationIcon() != null) {
        requestLayout();
      }
    }
  }
  
  public void setContentInsetsAbsolute(int paramInt1, int paramInt2)
  {
    ensureContentInsets();
    this.mContentInsets.setAbsolute(paramInt1, paramInt2);
  }
  
  public void setContentInsetsRelative(int paramInt1, int paramInt2)
  {
    ensureContentInsets();
    this.mContentInsets.setRelative(paramInt1, paramInt2);
  }
  
  public void setLogo(@DrawableRes int paramInt)
  {
    setLogo(AppCompatResources.getDrawable(getContext(), paramInt));
  }
  
  public void setLogo(Drawable paramDrawable)
  {
    if (paramDrawable != null)
    {
      ensureLogoView();
      if (!isChildOrHidden(this.mLogoView)) {
        addSystemView(this.mLogoView, true);
      }
    }
    for (;;)
    {
      if (this.mLogoView != null) {
        this.mLogoView.setImageDrawable(paramDrawable);
      }
      return;
      if ((this.mLogoView != null) && (isChildOrHidden(this.mLogoView)))
      {
        removeView(this.mLogoView);
        this.mHiddenViews.remove(this.mLogoView);
      }
    }
  }
  
  public void setLogoDescription(@StringRes int paramInt)
  {
    setLogoDescription(getContext().getText(paramInt));
  }
  
  public void setLogoDescription(CharSequence paramCharSequence)
  {
    if (!TextUtils.isEmpty(paramCharSequence)) {
      ensureLogoView();
    }
    if (this.mLogoView != null) {
      this.mLogoView.setContentDescription(paramCharSequence);
    }
  }
  
  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public void setMenu(MenuBuilder paramMenuBuilder, ActionMenuPresenter paramActionMenuPresenter)
  {
    if ((paramMenuBuilder == null) && (this.mMenuView == null)) {}
    MenuBuilder localMenuBuilder;
    do
    {
      return;
      ensureMenuView();
      localMenuBuilder = this.mMenuView.peekMenu();
    } while (localMenuBuilder == paramMenuBuilder);
    if (localMenuBuilder != null)
    {
      localMenuBuilder.removeMenuPresenter(this.mOuterActionMenuPresenter);
      localMenuBuilder.removeMenuPresenter(this.mExpandedMenuPresenter);
    }
    if (this.mExpandedMenuPresenter == null) {
      this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter();
    }
    paramActionMenuPresenter.setExpandedActionViewsExclusive(true);
    if (paramMenuBuilder != null)
    {
      paramMenuBuilder.addMenuPresenter(paramActionMenuPresenter, this.mPopupContext);
      paramMenuBuilder.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
    }
    for (;;)
    {
      this.mMenuView.setPopupTheme(this.mPopupTheme);
      this.mMenuView.setPresenter(paramActionMenuPresenter);
      this.mOuterActionMenuPresenter = paramActionMenuPresenter;
      return;
      paramActionMenuPresenter.initForMenu(this.mPopupContext, null);
      this.mExpandedMenuPresenter.initForMenu(this.mPopupContext, null);
      paramActionMenuPresenter.updateMenuView(true);
      this.mExpandedMenuPresenter.updateMenuView(true);
    }
  }
  
  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public void setMenuCallbacks(MenuPresenter.Callback paramCallback, MenuBuilder.Callback paramCallback1)
  {
    this.mActionMenuPresenterCallback = paramCallback;
    this.mMenuBuilderCallback = paramCallback1;
    if (this.mMenuView != null) {
      this.mMenuView.setMenuCallbacks(paramCallback, paramCallback1);
    }
  }
  
  public void setNavigationContentDescription(@StringRes int paramInt)
  {
    if (paramInt != 0) {}
    for (CharSequence localCharSequence = getContext().getText(paramInt);; localCharSequence = null)
    {
      setNavigationContentDescription(localCharSequence);
      return;
    }
  }
  
  public void setNavigationContentDescription(@Nullable CharSequence paramCharSequence)
  {
    if (!TextUtils.isEmpty(paramCharSequence)) {
      ensureNavButtonView();
    }
    if (this.mNavButtonView != null) {
      this.mNavButtonView.setContentDescription(paramCharSequence);
    }
  }
  
  public void setNavigationIcon(@DrawableRes int paramInt)
  {
    setNavigationIcon(AppCompatResources.getDrawable(getContext(), paramInt));
  }
  
  public void setNavigationIcon(@Nullable Drawable paramDrawable)
  {
    if (paramDrawable != null)
    {
      ensureNavButtonView();
      if (!isChildOrHidden(this.mNavButtonView)) {
        addSystemView(this.mNavButtonView, true);
      }
    }
    for (;;)
    {
      if (this.mNavButtonView != null) {
        this.mNavButtonView.setImageDrawable(paramDrawable);
      }
      return;
      if ((this.mNavButtonView != null) && (isChildOrHidden(this.mNavButtonView)))
      {
        removeView(this.mNavButtonView);
        this.mHiddenViews.remove(this.mNavButtonView);
      }
    }
  }
  
  public void setNavigationOnClickListener(View.OnClickListener paramOnClickListener)
  {
    ensureNavButtonView();
    this.mNavButtonView.setOnClickListener(paramOnClickListener);
  }
  
  public void setOnMenuItemClickListener(OnMenuItemClickListener paramOnMenuItemClickListener)
  {
    this.mOnMenuItemClickListener = paramOnMenuItemClickListener;
  }
  
  public void setOverflowIcon(@Nullable Drawable paramDrawable)
  {
    ensureMenu();
    this.mMenuView.setOverflowIcon(paramDrawable);
  }
  
  public void setPopupTheme(@StyleRes int paramInt)
  {
    if (this.mPopupTheme != paramInt)
    {
      this.mPopupTheme = paramInt;
      if (paramInt == 0) {
        this.mPopupContext = getContext();
      }
    }
    else
    {
      return;
    }
    this.mPopupContext = new ContextThemeWrapper(getContext(), paramInt);
  }
  
  public void setSubtitle(@StringRes int paramInt)
  {
    setSubtitle(getContext().getText(paramInt));
  }
  
  public void setSubtitle(CharSequence paramCharSequence)
  {
    if (!TextUtils.isEmpty(paramCharSequence))
    {
      if (this.mSubtitleTextView == null)
      {
        Context localContext = getContext();
        this.mSubtitleTextView = new AppCompatTextView(localContext);
        this.mSubtitleTextView.setSingleLine();
        this.mSubtitleTextView.setEllipsize(TextUtils.TruncateAt.END);
        if (this.mSubtitleTextAppearance != 0) {
          this.mSubtitleTextView.setTextAppearance(localContext, this.mSubtitleTextAppearance);
        }
        if (this.mSubtitleTextColor != 0) {
          this.mSubtitleTextView.setTextColor(this.mSubtitleTextColor);
        }
      }
      if (!isChildOrHidden(this.mSubtitleTextView)) {
        addSystemView(this.mSubtitleTextView, true);
      }
    }
    for (;;)
    {
      if (this.mSubtitleTextView != null) {
        this.mSubtitleTextView.setText(paramCharSequence);
      }
      this.mSubtitleText = paramCharSequence;
      return;
      if ((this.mSubtitleTextView != null) && (isChildOrHidden(this.mSubtitleTextView)))
      {
        removeView(this.mSubtitleTextView);
        this.mHiddenViews.remove(this.mSubtitleTextView);
      }
    }
  }
  
  public void setSubtitleTextAppearance(Context paramContext, @StyleRes int paramInt)
  {
    this.mSubtitleTextAppearance = paramInt;
    if (this.mSubtitleTextView != null) {
      this.mSubtitleTextView.setTextAppearance(paramContext, paramInt);
    }
  }
  
  public void setSubtitleTextColor(@ColorInt int paramInt)
  {
    this.mSubtitleTextColor = paramInt;
    if (this.mSubtitleTextView != null) {
      this.mSubtitleTextView.setTextColor(paramInt);
    }
  }
  
  public void setTitle(@StringRes int paramInt)
  {
    setTitle(getContext().getText(paramInt));
  }
  
  public void setTitle(CharSequence paramCharSequence)
  {
    if (!TextUtils.isEmpty(paramCharSequence))
    {
      if (this.mTitleTextView == null)
      {
        Context localContext = getContext();
        this.mTitleTextView = new AppCompatTextView(localContext);
        this.mTitleTextView.setSingleLine();
        this.mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
        if (this.mTitleTextAppearance != 0) {
          this.mTitleTextView.setTextAppearance(localContext, this.mTitleTextAppearance);
        }
        if (this.mTitleTextColor != 0) {
          this.mTitleTextView.setTextColor(this.mTitleTextColor);
        }
      }
      if (!isChildOrHidden(this.mTitleTextView)) {
        addSystemView(this.mTitleTextView, true);
      }
    }
    for (;;)
    {
      if (this.mTitleTextView != null) {
        this.mTitleTextView.setText(paramCharSequence);
      }
      this.mTitleText = paramCharSequence;
      return;
      if ((this.mTitleTextView != null) && (isChildOrHidden(this.mTitleTextView)))
      {
        removeView(this.mTitleTextView);
        this.mHiddenViews.remove(this.mTitleTextView);
      }
    }
  }
  
  public void setTitleMargin(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    this.mTitleMarginStart = paramInt1;
    this.mTitleMarginTop = paramInt2;
    this.mTitleMarginEnd = paramInt3;
    this.mTitleMarginBottom = paramInt4;
    requestLayout();
  }
  
  public void setTitleMarginBottom(int paramInt)
  {
    this.mTitleMarginBottom = paramInt;
    requestLayout();
  }
  
  public void setTitleMarginEnd(int paramInt)
  {
    this.mTitleMarginEnd = paramInt;
    requestLayout();
  }
  
  public void setTitleMarginStart(int paramInt)
  {
    this.mTitleMarginStart = paramInt;
    requestLayout();
  }
  
  public void setTitleMarginTop(int paramInt)
  {
    this.mTitleMarginTop = paramInt;
    requestLayout();
  }
  
  public void setTitleTextAppearance(Context paramContext, @StyleRes int paramInt)
  {
    this.mTitleTextAppearance = paramInt;
    if (this.mTitleTextView != null) {
      this.mTitleTextView.setTextAppearance(paramContext, paramInt);
    }
  }
  
  public void setTitleTextColor(@ColorInt int paramInt)
  {
    this.mTitleTextColor = paramInt;
    if (this.mTitleTextView != null) {
      this.mTitleTextView.setTextColor(paramInt);
    }
  }
  
  public boolean showOverflowMenu()
  {
    return (this.mMenuView != null) && (this.mMenuView.showOverflowMenu());
  }
  
  private class ExpandedActionViewMenuPresenter
    implements MenuPresenter
  {
    MenuItemImpl mCurrentExpandedItem;
    MenuBuilder mMenu;
    
    ExpandedActionViewMenuPresenter() {}
    
    public boolean collapseItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl)
    {
      if ((Toolbar.this.mExpandedActionView instanceof CollapsibleActionView)) {
        ((CollapsibleActionView)Toolbar.this.mExpandedActionView).onActionViewCollapsed();
      }
      Toolbar.this.removeView(Toolbar.this.mExpandedActionView);
      Toolbar.this.removeView(Toolbar.this.mCollapseButtonView);
      Toolbar.this.mExpandedActionView = null;
      Toolbar.this.addChildrenForExpandedActionView();
      this.mCurrentExpandedItem = null;
      Toolbar.this.requestLayout();
      paramMenuItemImpl.setActionViewExpanded(false);
      return true;
    }
    
    public boolean expandItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl)
    {
      Toolbar.this.ensureCollapseButtonView();
      if (Toolbar.this.mCollapseButtonView.getParent() != Toolbar.this) {
        Toolbar.this.addView(Toolbar.this.mCollapseButtonView);
      }
      Toolbar.this.mExpandedActionView = paramMenuItemImpl.getActionView();
      this.mCurrentExpandedItem = paramMenuItemImpl;
      if (Toolbar.this.mExpandedActionView.getParent() != Toolbar.this)
      {
        Toolbar.LayoutParams localLayoutParams = Toolbar.this.generateDefaultLayoutParams();
        localLayoutParams.gravity = (0x800003 | 0x70 & Toolbar.this.mButtonGravity);
        localLayoutParams.mViewType = 2;
        Toolbar.this.mExpandedActionView.setLayoutParams(localLayoutParams);
        Toolbar.this.addView(Toolbar.this.mExpandedActionView);
      }
      Toolbar.this.removeChildrenForExpandedActionView();
      Toolbar.this.requestLayout();
      paramMenuItemImpl.setActionViewExpanded(true);
      if ((Toolbar.this.mExpandedActionView instanceof CollapsibleActionView)) {
        ((CollapsibleActionView)Toolbar.this.mExpandedActionView).onActionViewExpanded();
      }
      return true;
    }
    
    public boolean flagActionItems()
    {
      return false;
    }
    
    public int getId()
    {
      return 0;
    }
    
    public MenuView getMenuView(ViewGroup paramViewGroup)
    {
      return null;
    }
    
    public void initForMenu(Context paramContext, MenuBuilder paramMenuBuilder)
    {
      if ((this.mMenu != null) && (this.mCurrentExpandedItem != null)) {
        this.mMenu.collapseItemActionView(this.mCurrentExpandedItem);
      }
      this.mMenu = paramMenuBuilder;
    }
    
    public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean) {}
    
    public void onRestoreInstanceState(Parcelable paramParcelable) {}
    
    public Parcelable onSaveInstanceState()
    {
      return null;
    }
    
    public boolean onSubMenuSelected(SubMenuBuilder paramSubMenuBuilder)
    {
      return false;
    }
    
    public void setCallback(MenuPresenter.Callback paramCallback) {}
    
    public void updateMenuView(boolean paramBoolean)
    {
      int i;
      int j;
      if (this.mCurrentExpandedItem != null)
      {
        MenuBuilder localMenuBuilder = this.mMenu;
        i = 0;
        if (localMenuBuilder != null) {
          j = this.mMenu.size();
        }
      }
      for (int k = 0;; k++)
      {
        i = 0;
        if (k < j)
        {
          if (this.mMenu.getItem(k) == this.mCurrentExpandedItem) {
            i = 1;
          }
        }
        else
        {
          if (i == 0) {
            collapseItemActionView(this.mMenu, this.mCurrentExpandedItem);
          }
          return;
        }
      }
    }
  }
  
  public static class LayoutParams
    extends ActionBar.LayoutParams
  {
    static final int CUSTOM = 0;
    static final int EXPANDED = 2;
    static final int SYSTEM = 1;
    int mViewType = 0;
    
    public LayoutParams(int paramInt)
    {
      this(-2, -1, paramInt);
    }
    
    public LayoutParams(int paramInt1, int paramInt2)
    {
      super(paramInt2);
      this.gravity = 8388627;
    }
    
    public LayoutParams(int paramInt1, int paramInt2, int paramInt3)
    {
      super(paramInt2);
      this.gravity = paramInt3;
    }
    
    public LayoutParams(@NonNull Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
    }
    
    public LayoutParams(ActionBar.LayoutParams paramLayoutParams)
    {
      super();
    }
    
    public LayoutParams(LayoutParams paramLayoutParams)
    {
      super();
      this.mViewType = paramLayoutParams.mViewType;
    }
    
    public LayoutParams(ViewGroup.LayoutParams paramLayoutParams)
    {
      super();
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams paramMarginLayoutParams)
    {
      super();
      copyMarginsFromCompat(paramMarginLayoutParams);
    }
    
    void copyMarginsFromCompat(ViewGroup.MarginLayoutParams paramMarginLayoutParams)
    {
      this.leftMargin = paramMarginLayoutParams.leftMargin;
      this.topMargin = paramMarginLayoutParams.topMargin;
      this.rightMargin = paramMarginLayoutParams.rightMargin;
      this.bottomMargin = paramMarginLayoutParams.bottomMargin;
    }
  }
  
  public static abstract interface OnMenuItemClickListener
  {
    public abstract boolean onMenuItemClick(MenuItem paramMenuItem);
  }
  
  public static class SavedState
    extends AbsSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks()
    {
      public Toolbar.SavedState createFromParcel(Parcel paramAnonymousParcel, ClassLoader paramAnonymousClassLoader)
      {
        return new Toolbar.SavedState(paramAnonymousParcel, paramAnonymousClassLoader);
      }
      
      public Toolbar.SavedState[] newArray(int paramAnonymousInt)
      {
        return new Toolbar.SavedState[paramAnonymousInt];
      }
    });
    int expandedMenuItemId;
    boolean isOverflowOpen;
    
    public SavedState(Parcel paramParcel)
    {
      this(paramParcel, null);
    }
    
    public SavedState(Parcel paramParcel, ClassLoader paramClassLoader)
    {
      super(paramClassLoader);
      this.expandedMenuItemId = paramParcel.readInt();
      if (paramParcel.readInt() != 0) {}
      for (boolean bool = true;; bool = false)
      {
        this.isOverflowOpen = bool;
        return;
      }
    }
    
    public SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeInt(this.expandedMenuItemId);
      if (this.isOverflowOpen) {}
      for (int i = 1;; i = 0)
      {
        paramParcel.writeInt(i);
        return;
      }
    }
  }
}
