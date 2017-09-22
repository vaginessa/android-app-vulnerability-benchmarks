package android.support.v7.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.RestrictTo;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.ViewPropertyAnimatorUpdateListener;
import android.support.v7.appcompat.R.attr;
import android.support.v7.appcompat.R.id;
import android.support.v7.appcompat.R.styleable;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.ActionBarPolicy;
import android.support.v7.view.ActionMode;
import android.support.v7.view.ActionMode.Callback;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.ViewPropertyAnimatorCompatSet;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuBuilder.Callback;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.view.menu.SubMenuBuilder;
import android.support.v7.widget.ActionBarContainer;
import android.support.v7.widget.ActionBarContextView;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.support.v7.widget.ActionBarOverlayLayout.ActionBarVisibilityCallback;
import android.support.v7.widget.DecorToolbar;
import android.support.v7.widget.ScrollingTabContainerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.SpinnerAdapter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

@RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
public class WindowDecorActionBar
  extends ActionBar
  implements ActionBarOverlayLayout.ActionBarVisibilityCallback
{
  private static final boolean ALLOW_SHOW_HIDE_ANIMATIONS = false;
  private static final long FADE_IN_DURATION_MS = 200L;
  private static final long FADE_OUT_DURATION_MS = 100L;
  private static final int INVALID_POSITION = -1;
  private static final String TAG = "WindowDecorActionBar";
  private static final Interpolator sHideInterpolator;
  private static final Interpolator sShowInterpolator;
  ActionModeImpl mActionMode;
  private Activity mActivity;
  ActionBarContainer mContainerView;
  boolean mContentAnimations = true;
  View mContentView;
  Context mContext;
  ActionBarContextView mContextView;
  private int mCurWindowVisibility = 0;
  ViewPropertyAnimatorCompatSet mCurrentShowAnim;
  DecorToolbar mDecorToolbar;
  ActionMode mDeferredDestroyActionMode;
  ActionMode.Callback mDeferredModeDestroyCallback;
  private Dialog mDialog;
  private boolean mDisplayHomeAsUpSet;
  private boolean mHasEmbeddedTabs;
  boolean mHiddenByApp;
  boolean mHiddenBySystem;
  final ViewPropertyAnimatorListener mHideListener = new ViewPropertyAnimatorListenerAdapter()
  {
    public void onAnimationEnd(View paramAnonymousView)
    {
      if ((WindowDecorActionBar.this.mContentAnimations) && (WindowDecorActionBar.this.mContentView != null))
      {
        ViewCompat.setTranslationY(WindowDecorActionBar.this.mContentView, 0.0F);
        ViewCompat.setTranslationY(WindowDecorActionBar.this.mContainerView, 0.0F);
      }
      WindowDecorActionBar.this.mContainerView.setVisibility(8);
      WindowDecorActionBar.this.mContainerView.setTransitioning(false);
      WindowDecorActionBar.this.mCurrentShowAnim = null;
      WindowDecorActionBar.this.completeDeferredDestroyActionMode();
      if (WindowDecorActionBar.this.mOverlayLayout != null) {
        ViewCompat.requestApplyInsets(WindowDecorActionBar.this.mOverlayLayout);
      }
    }
  };
  boolean mHideOnContentScroll;
  private boolean mLastMenuVisibility;
  private ArrayList<ActionBar.OnMenuVisibilityListener> mMenuVisibilityListeners = new ArrayList();
  private boolean mNowShowing = true;
  ActionBarOverlayLayout mOverlayLayout;
  private int mSavedTabPosition = -1;
  private TabImpl mSelectedTab;
  private boolean mShowHideAnimationEnabled;
  final ViewPropertyAnimatorListener mShowListener = new ViewPropertyAnimatorListenerAdapter()
  {
    public void onAnimationEnd(View paramAnonymousView)
    {
      WindowDecorActionBar.this.mCurrentShowAnim = null;
      WindowDecorActionBar.this.mContainerView.requestLayout();
    }
  };
  private boolean mShowingForMode;
  ScrollingTabContainerView mTabScrollView;
  private ArrayList<TabImpl> mTabs = new ArrayList();
  private Context mThemedContext;
  final ViewPropertyAnimatorUpdateListener mUpdateListener = new ViewPropertyAnimatorUpdateListener()
  {
    public void onAnimationUpdate(View paramAnonymousView)
    {
      ((View)WindowDecorActionBar.this.mContainerView.getParent()).invalidate();
    }
  };
  
  static
  {
    boolean bool1 = true;
    boolean bool2;
    if (!WindowDecorActionBar.class.desiredAssertionStatus())
    {
      bool2 = bool1;
      $assertionsDisabled = bool2;
      sHideInterpolator = new AccelerateInterpolator();
      sShowInterpolator = new DecelerateInterpolator();
      if (Build.VERSION.SDK_INT < 14) {
        break label54;
      }
    }
    for (;;)
    {
      ALLOW_SHOW_HIDE_ANIMATIONS = bool1;
      return;
      bool2 = false;
      break;
      label54:
      bool1 = false;
    }
  }
  
  public WindowDecorActionBar(Activity paramActivity, boolean paramBoolean)
  {
    this.mActivity = paramActivity;
    View localView = paramActivity.getWindow().getDecorView();
    init(localView);
    if (!paramBoolean) {
      this.mContentView = localView.findViewById(16908290);
    }
  }
  
  public WindowDecorActionBar(Dialog paramDialog)
  {
    this.mDialog = paramDialog;
    init(paramDialog.getWindow().getDecorView());
  }
  
  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public WindowDecorActionBar(View paramView)
  {
    assert (paramView.isInEditMode());
    init(paramView);
  }
  
  static boolean checkShowingFlags(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    if (paramBoolean3) {}
    while ((!paramBoolean1) && (!paramBoolean2)) {
      return true;
    }
    return false;
  }
  
  private void cleanupTabs()
  {
    if (this.mSelectedTab != null) {
      selectTab(null);
    }
    this.mTabs.clear();
    if (this.mTabScrollView != null) {
      this.mTabScrollView.removeAllTabs();
    }
    this.mSavedTabPosition = -1;
  }
  
  private void configureTab(ActionBar.Tab paramTab, int paramInt)
  {
    TabImpl localTabImpl = (TabImpl)paramTab;
    if (localTabImpl.getCallback() == null) {
      throw new IllegalStateException("Action Bar Tab must have a Callback");
    }
    localTabImpl.setPosition(paramInt);
    this.mTabs.add(paramInt, localTabImpl);
    int i = this.mTabs.size();
    for (int j = paramInt + 1; j < i; j++) {
      ((TabImpl)this.mTabs.get(j)).setPosition(j);
    }
  }
  
  private void ensureTabsExist()
  {
    if (this.mTabScrollView != null) {
      return;
    }
    ScrollingTabContainerView localScrollingTabContainerView = new ScrollingTabContainerView(this.mContext);
    if (this.mHasEmbeddedTabs)
    {
      localScrollingTabContainerView.setVisibility(0);
      this.mDecorToolbar.setEmbeddedTabView(localScrollingTabContainerView);
      this.mTabScrollView = localScrollingTabContainerView;
      return;
    }
    if (getNavigationMode() == 2)
    {
      localScrollingTabContainerView.setVisibility(0);
      if (this.mOverlayLayout != null) {
        ViewCompat.requestApplyInsets(this.mOverlayLayout);
      }
    }
    for (;;)
    {
      this.mContainerView.setTabContainer(localScrollingTabContainerView);
      break;
      localScrollingTabContainerView.setVisibility(8);
    }
  }
  
  private DecorToolbar getDecorToolbar(View paramView)
  {
    if ((paramView instanceof DecorToolbar)) {
      return (DecorToolbar)paramView;
    }
    if ((paramView instanceof Toolbar)) {
      return ((Toolbar)paramView).getWrapper();
    }
    if ("Can't make a decor toolbar out of " + paramView != null) {}
    for (String str = paramView.getClass().getSimpleName();; str = "null") {
      throw new IllegalStateException(str);
    }
  }
  
  private void hideForActionMode()
  {
    if (this.mShowingForMode)
    {
      this.mShowingForMode = false;
      if (this.mOverlayLayout != null) {
        this.mOverlayLayout.setShowingForActionMode(false);
      }
      updateVisibility(false);
    }
  }
  
  private void init(View paramView)
  {
    this.mOverlayLayout = ((ActionBarOverlayLayout)paramView.findViewById(R.id.decor_content_parent));
    if (this.mOverlayLayout != null) {
      this.mOverlayLayout.setActionBarVisibilityCallback(this);
    }
    this.mDecorToolbar = getDecorToolbar(paramView.findViewById(R.id.action_bar));
    this.mContextView = ((ActionBarContextView)paramView.findViewById(R.id.action_context_bar));
    this.mContainerView = ((ActionBarContainer)paramView.findViewById(R.id.action_bar_container));
    if ((this.mDecorToolbar == null) || (this.mContextView == null) || (this.mContainerView == null)) {
      throw new IllegalStateException(getClass().getSimpleName() + " can only be used " + "with a compatible window decor layout");
    }
    this.mContext = this.mDecorToolbar.getContext();
    int i;
    ActionBarPolicy localActionBarPolicy;
    if ((0x4 & this.mDecorToolbar.getDisplayOptions()) != 0)
    {
      i = 1;
      if (i != 0) {
        this.mDisplayHomeAsUpSet = true;
      }
      localActionBarPolicy = ActionBarPolicy.get(this.mContext);
      if ((!localActionBarPolicy.enableHomeButtonByDefault()) && (i == 0)) {
        break label275;
      }
    }
    label275:
    for (boolean bool = true;; bool = false)
    {
      setHomeButtonEnabled(bool);
      setHasEmbeddedTabs(localActionBarPolicy.hasEmbeddedTabs());
      TypedArray localTypedArray = this.mContext.obtainStyledAttributes(null, R.styleable.ActionBar, R.attr.actionBarStyle, 0);
      if (localTypedArray.getBoolean(R.styleable.ActionBar_hideOnContentScroll, false)) {
        setHideOnContentScrollEnabled(true);
      }
      int j = localTypedArray.getDimensionPixelSize(R.styleable.ActionBar_elevation, 0);
      if (j != 0) {
        setElevation(j);
      }
      localTypedArray.recycle();
      return;
      i = 0;
      break;
    }
  }
  
  private void setHasEmbeddedTabs(boolean paramBoolean)
  {
    boolean bool1 = true;
    this.mHasEmbeddedTabs = paramBoolean;
    boolean bool2;
    label45:
    label78:
    boolean bool3;
    label98:
    ActionBarOverlayLayout localActionBarOverlayLayout;
    if (!this.mHasEmbeddedTabs)
    {
      this.mDecorToolbar.setEmbeddedTabView(null);
      this.mContainerView.setTabContainer(this.mTabScrollView);
      if (getNavigationMode() != 2) {
        break label155;
      }
      bool2 = bool1;
      if (this.mTabScrollView != null)
      {
        if (!bool2) {
          break label160;
        }
        this.mTabScrollView.setVisibility(0);
        if (this.mOverlayLayout != null) {
          ViewCompat.requestApplyInsets(this.mOverlayLayout);
        }
      }
      DecorToolbar localDecorToolbar = this.mDecorToolbar;
      if ((this.mHasEmbeddedTabs) || (!bool2)) {
        break label172;
      }
      bool3 = bool1;
      localDecorToolbar.setCollapsible(bool3);
      localActionBarOverlayLayout = this.mOverlayLayout;
      if ((this.mHasEmbeddedTabs) || (!bool2)) {
        break label178;
      }
    }
    for (;;)
    {
      localActionBarOverlayLayout.setHasNonEmbeddedTabs(bool1);
      return;
      this.mContainerView.setTabContainer(null);
      this.mDecorToolbar.setEmbeddedTabView(this.mTabScrollView);
      break;
      label155:
      bool2 = false;
      break label45;
      label160:
      this.mTabScrollView.setVisibility(8);
      break label78;
      label172:
      bool3 = false;
      break label98;
      label178:
      bool1 = false;
    }
  }
  
  private boolean shouldAnimateContextView()
  {
    return ViewCompat.isLaidOut(this.mContainerView);
  }
  
  private void showForActionMode()
  {
    if (!this.mShowingForMode)
    {
      this.mShowingForMode = true;
      if (this.mOverlayLayout != null) {
        this.mOverlayLayout.setShowingForActionMode(true);
      }
      updateVisibility(false);
    }
  }
  
  private void updateVisibility(boolean paramBoolean)
  {
    if (checkShowingFlags(this.mHiddenByApp, this.mHiddenBySystem, this.mShowingForMode)) {
      if (!this.mNowShowing)
      {
        this.mNowShowing = true;
        doShow(paramBoolean);
      }
    }
    while (!this.mNowShowing) {
      return;
    }
    this.mNowShowing = false;
    doHide(paramBoolean);
  }
  
  public void addOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener paramOnMenuVisibilityListener)
  {
    this.mMenuVisibilityListeners.add(paramOnMenuVisibilityListener);
  }
  
  public void addTab(ActionBar.Tab paramTab)
  {
    addTab(paramTab, this.mTabs.isEmpty());
  }
  
  public void addTab(ActionBar.Tab paramTab, int paramInt)
  {
    addTab(paramTab, paramInt, this.mTabs.isEmpty());
  }
  
  public void addTab(ActionBar.Tab paramTab, int paramInt, boolean paramBoolean)
  {
    ensureTabsExist();
    this.mTabScrollView.addTab(paramTab, paramInt, paramBoolean);
    configureTab(paramTab, paramInt);
    if (paramBoolean) {
      selectTab(paramTab);
    }
  }
  
  public void addTab(ActionBar.Tab paramTab, boolean paramBoolean)
  {
    ensureTabsExist();
    this.mTabScrollView.addTab(paramTab, paramBoolean);
    configureTab(paramTab, this.mTabs.size());
    if (paramBoolean) {
      selectTab(paramTab);
    }
  }
  
  public void animateToMode(boolean paramBoolean)
  {
    ViewPropertyAnimatorCompat localViewPropertyAnimatorCompat2;
    ViewPropertyAnimatorCompat localViewPropertyAnimatorCompat1;
    if (paramBoolean)
    {
      showForActionMode();
      if (!shouldAnimateContextView()) {
        break label105;
      }
      if (!paramBoolean) {
        break label75;
      }
      localViewPropertyAnimatorCompat2 = this.mDecorToolbar.setupAnimatorToVisibility(4, 100L);
      localViewPropertyAnimatorCompat1 = this.mContextView.setupAnimatorToVisibility(0, 200L);
    }
    for (;;)
    {
      ViewPropertyAnimatorCompatSet localViewPropertyAnimatorCompatSet = new ViewPropertyAnimatorCompatSet();
      localViewPropertyAnimatorCompatSet.playSequentially(localViewPropertyAnimatorCompat2, localViewPropertyAnimatorCompat1);
      localViewPropertyAnimatorCompatSet.start();
      return;
      hideForActionMode();
      break;
      label75:
      localViewPropertyAnimatorCompat1 = this.mDecorToolbar.setupAnimatorToVisibility(0, 200L);
      localViewPropertyAnimatorCompat2 = this.mContextView.setupAnimatorToVisibility(8, 100L);
    }
    label105:
    if (paramBoolean)
    {
      this.mDecorToolbar.setVisibility(4);
      this.mContextView.setVisibility(0);
      return;
    }
    this.mDecorToolbar.setVisibility(0);
    this.mContextView.setVisibility(8);
  }
  
  public boolean collapseActionView()
  {
    if ((this.mDecorToolbar != null) && (this.mDecorToolbar.hasExpandedActionView()))
    {
      this.mDecorToolbar.collapseActionView();
      return true;
    }
    return false;
  }
  
  void completeDeferredDestroyActionMode()
  {
    if (this.mDeferredModeDestroyCallback != null)
    {
      this.mDeferredModeDestroyCallback.onDestroyActionMode(this.mDeferredDestroyActionMode);
      this.mDeferredDestroyActionMode = null;
      this.mDeferredModeDestroyCallback = null;
    }
  }
  
  public void dispatchMenuVisibilityChanged(boolean paramBoolean)
  {
    if (paramBoolean == this.mLastMenuVisibility) {}
    for (;;)
    {
      return;
      this.mLastMenuVisibility = paramBoolean;
      int i = this.mMenuVisibilityListeners.size();
      for (int j = 0; j < i; j++) {
        ((ActionBar.OnMenuVisibilityListener)this.mMenuVisibilityListeners.get(j)).onMenuVisibilityChanged(paramBoolean);
      }
    }
  }
  
  public void doHide(boolean paramBoolean)
  {
    if (this.mCurrentShowAnim != null) {
      this.mCurrentShowAnim.cancel();
    }
    if ((this.mCurWindowVisibility == 0) && (ALLOW_SHOW_HIDE_ANIMATIONS) && ((this.mShowHideAnimationEnabled) || (paramBoolean)))
    {
      ViewCompat.setAlpha(this.mContainerView, 1.0F);
      this.mContainerView.setTransitioning(true);
      ViewPropertyAnimatorCompatSet localViewPropertyAnimatorCompatSet = new ViewPropertyAnimatorCompatSet();
      float f = -this.mContainerView.getHeight();
      if (paramBoolean)
      {
        int[] arrayOfInt = { 0, 0 };
        this.mContainerView.getLocationInWindow(arrayOfInt);
        f -= arrayOfInt[1];
      }
      ViewPropertyAnimatorCompat localViewPropertyAnimatorCompat = ViewCompat.animate(this.mContainerView).translationY(f);
      localViewPropertyAnimatorCompat.setUpdateListener(this.mUpdateListener);
      localViewPropertyAnimatorCompatSet.play(localViewPropertyAnimatorCompat);
      if ((this.mContentAnimations) && (this.mContentView != null)) {
        localViewPropertyAnimatorCompatSet.play(ViewCompat.animate(this.mContentView).translationY(f));
      }
      localViewPropertyAnimatorCompatSet.setInterpolator(sHideInterpolator);
      localViewPropertyAnimatorCompatSet.setDuration(250L);
      localViewPropertyAnimatorCompatSet.setListener(this.mHideListener);
      this.mCurrentShowAnim = localViewPropertyAnimatorCompatSet;
      localViewPropertyAnimatorCompatSet.start();
      return;
    }
    this.mHideListener.onAnimationEnd(null);
  }
  
  public void doShow(boolean paramBoolean)
  {
    if (this.mCurrentShowAnim != null) {
      this.mCurrentShowAnim.cancel();
    }
    this.mContainerView.setVisibility(0);
    if ((this.mCurWindowVisibility == 0) && (ALLOW_SHOW_HIDE_ANIMATIONS) && ((this.mShowHideAnimationEnabled) || (paramBoolean)))
    {
      ViewCompat.setTranslationY(this.mContainerView, 0.0F);
      float f = -this.mContainerView.getHeight();
      if (paramBoolean)
      {
        int[] arrayOfInt = { 0, 0 };
        this.mContainerView.getLocationInWindow(arrayOfInt);
        f -= arrayOfInt[1];
      }
      ViewCompat.setTranslationY(this.mContainerView, f);
      ViewPropertyAnimatorCompatSet localViewPropertyAnimatorCompatSet = new ViewPropertyAnimatorCompatSet();
      ViewPropertyAnimatorCompat localViewPropertyAnimatorCompat = ViewCompat.animate(this.mContainerView).translationY(0.0F);
      localViewPropertyAnimatorCompat.setUpdateListener(this.mUpdateListener);
      localViewPropertyAnimatorCompatSet.play(localViewPropertyAnimatorCompat);
      if ((this.mContentAnimations) && (this.mContentView != null))
      {
        ViewCompat.setTranslationY(this.mContentView, f);
        localViewPropertyAnimatorCompatSet.play(ViewCompat.animate(this.mContentView).translationY(0.0F));
      }
      localViewPropertyAnimatorCompatSet.setInterpolator(sShowInterpolator);
      localViewPropertyAnimatorCompatSet.setDuration(250L);
      localViewPropertyAnimatorCompatSet.setListener(this.mShowListener);
      this.mCurrentShowAnim = localViewPropertyAnimatorCompatSet;
      localViewPropertyAnimatorCompatSet.start();
    }
    for (;;)
    {
      if (this.mOverlayLayout != null) {
        ViewCompat.requestApplyInsets(this.mOverlayLayout);
      }
      return;
      ViewCompat.setAlpha(this.mContainerView, 1.0F);
      ViewCompat.setTranslationY(this.mContainerView, 0.0F);
      if ((this.mContentAnimations) && (this.mContentView != null)) {
        ViewCompat.setTranslationY(this.mContentView, 0.0F);
      }
      this.mShowListener.onAnimationEnd(null);
    }
  }
  
  public void enableContentAnimations(boolean paramBoolean)
  {
    this.mContentAnimations = paramBoolean;
  }
  
  public View getCustomView()
  {
    return this.mDecorToolbar.getCustomView();
  }
  
  public int getDisplayOptions()
  {
    return this.mDecorToolbar.getDisplayOptions();
  }
  
  public float getElevation()
  {
    return ViewCompat.getElevation(this.mContainerView);
  }
  
  public int getHeight()
  {
    return this.mContainerView.getHeight();
  }
  
  public int getHideOffset()
  {
    return this.mOverlayLayout.getActionBarHideOffset();
  }
  
  public int getNavigationItemCount()
  {
    switch (this.mDecorToolbar.getNavigationMode())
    {
    default: 
      return 0;
    case 2: 
      return this.mTabs.size();
    }
    return this.mDecorToolbar.getDropdownItemCount();
  }
  
  public int getNavigationMode()
  {
    return this.mDecorToolbar.getNavigationMode();
  }
  
  public int getSelectedNavigationIndex()
  {
    switch (this.mDecorToolbar.getNavigationMode())
    {
    default: 
    case 2: 
      do
      {
        return -1;
      } while (this.mSelectedTab == null);
      return this.mSelectedTab.getPosition();
    }
    return this.mDecorToolbar.getDropdownSelectedPosition();
  }
  
  public ActionBar.Tab getSelectedTab()
  {
    return this.mSelectedTab;
  }
  
  public CharSequence getSubtitle()
  {
    return this.mDecorToolbar.getSubtitle();
  }
  
  public ActionBar.Tab getTabAt(int paramInt)
  {
    return (ActionBar.Tab)this.mTabs.get(paramInt);
  }
  
  public int getTabCount()
  {
    return this.mTabs.size();
  }
  
  public Context getThemedContext()
  {
    int i;
    if (this.mThemedContext == null)
    {
      TypedValue localTypedValue = new TypedValue();
      this.mContext.getTheme().resolveAttribute(R.attr.actionBarWidgetTheme, localTypedValue, true);
      i = localTypedValue.resourceId;
      if (i == 0) {
        break label61;
      }
    }
    label61:
    for (this.mThemedContext = new ContextThemeWrapper(this.mContext, i);; this.mThemedContext = this.mContext) {
      return this.mThemedContext;
    }
  }
  
  public CharSequence getTitle()
  {
    return this.mDecorToolbar.getTitle();
  }
  
  public boolean hasIcon()
  {
    return this.mDecorToolbar.hasIcon();
  }
  
  public boolean hasLogo()
  {
    return this.mDecorToolbar.hasLogo();
  }
  
  public void hide()
  {
    if (!this.mHiddenByApp)
    {
      this.mHiddenByApp = true;
      updateVisibility(false);
    }
  }
  
  public void hideForSystem()
  {
    if (!this.mHiddenBySystem)
    {
      this.mHiddenBySystem = true;
      updateVisibility(true);
    }
  }
  
  public boolean isHideOnContentScrollEnabled()
  {
    return this.mOverlayLayout.isHideOnContentScrollEnabled();
  }
  
  public boolean isShowing()
  {
    int i = getHeight();
    return (this.mNowShowing) && ((i == 0) || (getHideOffset() < i));
  }
  
  public boolean isTitleTruncated()
  {
    return (this.mDecorToolbar != null) && (this.mDecorToolbar.isTitleTruncated());
  }
  
  public ActionBar.Tab newTab()
  {
    return new TabImpl();
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    setHasEmbeddedTabs(ActionBarPolicy.get(this.mContext).hasEmbeddedTabs());
  }
  
  public void onContentScrollStarted()
  {
    if (this.mCurrentShowAnim != null)
    {
      this.mCurrentShowAnim.cancel();
      this.mCurrentShowAnim = null;
    }
  }
  
  public void onContentScrollStopped() {}
  
  public void onWindowVisibilityChanged(int paramInt)
  {
    this.mCurWindowVisibility = paramInt;
  }
  
  public void removeAllTabs()
  {
    cleanupTabs();
  }
  
  public void removeOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener paramOnMenuVisibilityListener)
  {
    this.mMenuVisibilityListeners.remove(paramOnMenuVisibilityListener);
  }
  
  public void removeTab(ActionBar.Tab paramTab)
  {
    removeTabAt(paramTab.getPosition());
  }
  
  public void removeTabAt(int paramInt)
  {
    if (this.mTabScrollView == null) {}
    int i;
    do
    {
      return;
      if (this.mSelectedTab != null) {}
      for (i = this.mSelectedTab.getPosition();; i = this.mSavedTabPosition)
      {
        this.mTabScrollView.removeTabAt(paramInt);
        TabImpl localTabImpl = (TabImpl)this.mTabs.remove(paramInt);
        if (localTabImpl != null) {
          localTabImpl.setPosition(-1);
        }
        int j = this.mTabs.size();
        for (int k = paramInt; k < j; k++) {
          ((TabImpl)this.mTabs.get(k)).setPosition(k);
        }
      }
    } while (i != paramInt);
    if (this.mTabs.isEmpty()) {}
    for (Object localObject = null;; localObject = (TabImpl)this.mTabs.get(Math.max(0, paramInt - 1)))
    {
      selectTab((ActionBar.Tab)localObject);
      return;
    }
  }
  
  public boolean requestFocus()
  {
    ViewGroup localViewGroup = this.mDecorToolbar.getViewGroup();
    if ((localViewGroup != null) && (!localViewGroup.hasFocus()))
    {
      localViewGroup.requestFocus();
      return true;
    }
    return false;
  }
  
  public void selectTab(ActionBar.Tab paramTab)
  {
    int i = -1;
    int j;
    if (getNavigationMode() != 2) {
      if (paramTab != null)
      {
        j = paramTab.getPosition();
        this.mSavedTabPosition = j;
      }
    }
    label140:
    label218:
    for (;;)
    {
      return;
      j = i;
      break;
      FragmentTransaction localFragmentTransaction;
      if (((this.mActivity instanceof FragmentActivity)) && (!this.mDecorToolbar.getViewGroup().isInEditMode()))
      {
        localFragmentTransaction = ((FragmentActivity)this.mActivity).getSupportFragmentManager().beginTransaction().disallowAddToBackStack();
        if (this.mSelectedTab != paramTab) {
          break label140;
        }
        if (this.mSelectedTab != null)
        {
          this.mSelectedTab.getCallback().onTabReselected(this.mSelectedTab, localFragmentTransaction);
          this.mTabScrollView.animateToTab(paramTab.getPosition());
        }
      }
      for (;;)
      {
        if ((localFragmentTransaction == null) || (localFragmentTransaction.isEmpty())) {
          break label218;
        }
        localFragmentTransaction.commit();
        return;
        localFragmentTransaction = null;
        break;
        ScrollingTabContainerView localScrollingTabContainerView = this.mTabScrollView;
        if (paramTab != null) {
          i = paramTab.getPosition();
        }
        localScrollingTabContainerView.setTabSelected(i);
        if (this.mSelectedTab != null) {
          this.mSelectedTab.getCallback().onTabUnselected(this.mSelectedTab, localFragmentTransaction);
        }
        this.mSelectedTab = ((TabImpl)paramTab);
        if (this.mSelectedTab != null) {
          this.mSelectedTab.getCallback().onTabSelected(this.mSelectedTab, localFragmentTransaction);
        }
      }
    }
  }
  
  public void setBackgroundDrawable(Drawable paramDrawable)
  {
    this.mContainerView.setPrimaryBackground(paramDrawable);
  }
  
  public void setCustomView(int paramInt)
  {
    setCustomView(LayoutInflater.from(getThemedContext()).inflate(paramInt, this.mDecorToolbar.getViewGroup(), false));
  }
  
  public void setCustomView(View paramView)
  {
    this.mDecorToolbar.setCustomView(paramView);
  }
  
  public void setCustomView(View paramView, ActionBar.LayoutParams paramLayoutParams)
  {
    paramView.setLayoutParams(paramLayoutParams);
    this.mDecorToolbar.setCustomView(paramView);
  }
  
  public void setDefaultDisplayHomeAsUpEnabled(boolean paramBoolean)
  {
    if (!this.mDisplayHomeAsUpSet) {
      setDisplayHomeAsUpEnabled(paramBoolean);
    }
  }
  
  public void setDisplayHomeAsUpEnabled(boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (int i = 4;; i = 0)
    {
      setDisplayOptions(i, 4);
      return;
    }
  }
  
  public void setDisplayOptions(int paramInt)
  {
    if ((paramInt & 0x4) != 0) {
      this.mDisplayHomeAsUpSet = true;
    }
    this.mDecorToolbar.setDisplayOptions(paramInt);
  }
  
  public void setDisplayOptions(int paramInt1, int paramInt2)
  {
    int i = this.mDecorToolbar.getDisplayOptions();
    if ((paramInt2 & 0x4) != 0) {
      this.mDisplayHomeAsUpSet = true;
    }
    this.mDecorToolbar.setDisplayOptions(paramInt1 & paramInt2 | i & (paramInt2 ^ 0xFFFFFFFF));
  }
  
  public void setDisplayShowCustomEnabled(boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (int i = 16;; i = 0)
    {
      setDisplayOptions(i, 16);
      return;
    }
  }
  
  public void setDisplayShowHomeEnabled(boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (int i = 2;; i = 0)
    {
      setDisplayOptions(i, 2);
      return;
    }
  }
  
  public void setDisplayShowTitleEnabled(boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (int i = 8;; i = 0)
    {
      setDisplayOptions(i, 8);
      return;
    }
  }
  
  public void setDisplayUseLogoEnabled(boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (int i = 1;; i = 0)
    {
      setDisplayOptions(i, 1);
      return;
    }
  }
  
  public void setElevation(float paramFloat)
  {
    ViewCompat.setElevation(this.mContainerView, paramFloat);
  }
  
  public void setHideOffset(int paramInt)
  {
    if ((paramInt != 0) && (!this.mOverlayLayout.isInOverlayMode())) {
      throw new IllegalStateException("Action bar must be in overlay mode (Window.FEATURE_OVERLAY_ACTION_BAR) to set a non-zero hide offset");
    }
    this.mOverlayLayout.setActionBarHideOffset(paramInt);
  }
  
  public void setHideOnContentScrollEnabled(boolean paramBoolean)
  {
    if ((paramBoolean) && (!this.mOverlayLayout.isInOverlayMode())) {
      throw new IllegalStateException("Action bar must be in overlay mode (Window.FEATURE_OVERLAY_ACTION_BAR) to enable hide on content scroll");
    }
    this.mHideOnContentScroll = paramBoolean;
    this.mOverlayLayout.setHideOnContentScrollEnabled(paramBoolean);
  }
  
  public void setHomeActionContentDescription(int paramInt)
  {
    this.mDecorToolbar.setNavigationContentDescription(paramInt);
  }
  
  public void setHomeActionContentDescription(CharSequence paramCharSequence)
  {
    this.mDecorToolbar.setNavigationContentDescription(paramCharSequence);
  }
  
  public void setHomeAsUpIndicator(int paramInt)
  {
    this.mDecorToolbar.setNavigationIcon(paramInt);
  }
  
  public void setHomeAsUpIndicator(Drawable paramDrawable)
  {
    this.mDecorToolbar.setNavigationIcon(paramDrawable);
  }
  
  public void setHomeButtonEnabled(boolean paramBoolean)
  {
    this.mDecorToolbar.setHomeButtonEnabled(paramBoolean);
  }
  
  public void setIcon(int paramInt)
  {
    this.mDecorToolbar.setIcon(paramInt);
  }
  
  public void setIcon(Drawable paramDrawable)
  {
    this.mDecorToolbar.setIcon(paramDrawable);
  }
  
  public void setListNavigationCallbacks(SpinnerAdapter paramSpinnerAdapter, ActionBar.OnNavigationListener paramOnNavigationListener)
  {
    this.mDecorToolbar.setDropdownParams(paramSpinnerAdapter, new NavItemSelectedListener(paramOnNavigationListener));
  }
  
  public void setLogo(int paramInt)
  {
    this.mDecorToolbar.setLogo(paramInt);
  }
  
  public void setLogo(Drawable paramDrawable)
  {
    this.mDecorToolbar.setLogo(paramDrawable);
  }
  
  public void setNavigationMode(int paramInt)
  {
    boolean bool1 = true;
    int i = this.mDecorToolbar.getNavigationMode();
    label88:
    boolean bool2;
    label109:
    ActionBarOverlayLayout localActionBarOverlayLayout;
    switch (i)
    {
    default: 
      if ((i != paramInt) && (!this.mHasEmbeddedTabs) && (this.mOverlayLayout != null)) {
        ViewCompat.requestApplyInsets(this.mOverlayLayout);
      }
      this.mDecorToolbar.setNavigationMode(paramInt);
      switch (paramInt)
      {
      default: 
        DecorToolbar localDecorToolbar = this.mDecorToolbar;
        if ((paramInt == 2) && (!this.mHasEmbeddedTabs))
        {
          bool2 = bool1;
          localDecorToolbar.setCollapsible(bool2);
          localActionBarOverlayLayout = this.mOverlayLayout;
          if ((paramInt != 2) || (this.mHasEmbeddedTabs)) {
            break label210;
          }
        }
        break;
      }
      break;
    }
    for (;;)
    {
      localActionBarOverlayLayout.setHasNonEmbeddedTabs(bool1);
      return;
      this.mSavedTabPosition = getSelectedNavigationIndex();
      selectTab(null);
      this.mTabScrollView.setVisibility(8);
      break;
      ensureTabsExist();
      this.mTabScrollView.setVisibility(0);
      if (this.mSavedTabPosition == -1) {
        break label88;
      }
      setSelectedNavigationItem(this.mSavedTabPosition);
      this.mSavedTabPosition = -1;
      break label88;
      bool2 = false;
      break label109;
      label210:
      bool1 = false;
    }
  }
  
  public void setSelectedNavigationItem(int paramInt)
  {
    switch (this.mDecorToolbar.getNavigationMode())
    {
    default: 
      throw new IllegalStateException("setSelectedNavigationIndex not valid for current navigation mode");
    case 2: 
      selectTab((ActionBar.Tab)this.mTabs.get(paramInt));
      return;
    }
    this.mDecorToolbar.setDropdownSelectedPosition(paramInt);
  }
  
  public void setShowHideAnimationEnabled(boolean paramBoolean)
  {
    this.mShowHideAnimationEnabled = paramBoolean;
    if ((!paramBoolean) && (this.mCurrentShowAnim != null)) {
      this.mCurrentShowAnim.cancel();
    }
  }
  
  public void setSplitBackgroundDrawable(Drawable paramDrawable) {}
  
  public void setStackedBackgroundDrawable(Drawable paramDrawable)
  {
    this.mContainerView.setStackedBackground(paramDrawable);
  }
  
  public void setSubtitle(int paramInt)
  {
    setSubtitle(this.mContext.getString(paramInt));
  }
  
  public void setSubtitle(CharSequence paramCharSequence)
  {
    this.mDecorToolbar.setSubtitle(paramCharSequence);
  }
  
  public void setTitle(int paramInt)
  {
    setTitle(this.mContext.getString(paramInt));
  }
  
  public void setTitle(CharSequence paramCharSequence)
  {
    this.mDecorToolbar.setTitle(paramCharSequence);
  }
  
  public void setWindowTitle(CharSequence paramCharSequence)
  {
    this.mDecorToolbar.setWindowTitle(paramCharSequence);
  }
  
  public void show()
  {
    if (this.mHiddenByApp)
    {
      this.mHiddenByApp = false;
      updateVisibility(false);
    }
  }
  
  public void showForSystem()
  {
    if (this.mHiddenBySystem)
    {
      this.mHiddenBySystem = false;
      updateVisibility(true);
    }
  }
  
  public ActionMode startActionMode(ActionMode.Callback paramCallback)
  {
    if (this.mActionMode != null) {
      this.mActionMode.finish();
    }
    this.mOverlayLayout.setHideOnContentScrollEnabled(false);
    this.mContextView.killMode();
    ActionModeImpl localActionModeImpl = new ActionModeImpl(this.mContextView.getContext(), paramCallback);
    if (localActionModeImpl.dispatchOnCreate())
    {
      this.mActionMode = localActionModeImpl;
      localActionModeImpl.invalidate();
      this.mContextView.initForMode(localActionModeImpl);
      animateToMode(true);
      this.mContextView.sendAccessibilityEvent(32);
      return localActionModeImpl;
    }
    return null;
  }
  
  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public class ActionModeImpl
    extends ActionMode
    implements MenuBuilder.Callback
  {
    private final Context mActionModeContext;
    private ActionMode.Callback mCallback;
    private WeakReference<View> mCustomView;
    private final MenuBuilder mMenu;
    
    public ActionModeImpl(Context paramContext, ActionMode.Callback paramCallback)
    {
      this.mActionModeContext = paramContext;
      this.mCallback = paramCallback;
      this.mMenu = new MenuBuilder(paramContext).setDefaultShowAsAction(1);
      this.mMenu.setCallback(this);
    }
    
    public boolean dispatchOnCreate()
    {
      this.mMenu.stopDispatchingItemsChanged();
      try
      {
        boolean bool = this.mCallback.onCreateActionMode(this, this.mMenu);
        return bool;
      }
      finally
      {
        this.mMenu.startDispatchingItemsChanged();
      }
    }
    
    public void finish()
    {
      if (WindowDecorActionBar.this.mActionMode != this) {
        return;
      }
      if (!WindowDecorActionBar.checkShowingFlags(WindowDecorActionBar.this.mHiddenByApp, WindowDecorActionBar.this.mHiddenBySystem, false))
      {
        WindowDecorActionBar.this.mDeferredDestroyActionMode = this;
        WindowDecorActionBar.this.mDeferredModeDestroyCallback = this.mCallback;
      }
      for (;;)
      {
        this.mCallback = null;
        WindowDecorActionBar.this.animateToMode(false);
        WindowDecorActionBar.this.mContextView.closeMode();
        WindowDecorActionBar.this.mDecorToolbar.getViewGroup().sendAccessibilityEvent(32);
        WindowDecorActionBar.this.mOverlayLayout.setHideOnContentScrollEnabled(WindowDecorActionBar.this.mHideOnContentScroll);
        WindowDecorActionBar.this.mActionMode = null;
        return;
        this.mCallback.onDestroyActionMode(this);
      }
    }
    
    public View getCustomView()
    {
      if (this.mCustomView != null) {
        return (View)this.mCustomView.get();
      }
      return null;
    }
    
    public Menu getMenu()
    {
      return this.mMenu;
    }
    
    public MenuInflater getMenuInflater()
    {
      return new SupportMenuInflater(this.mActionModeContext);
    }
    
    public CharSequence getSubtitle()
    {
      return WindowDecorActionBar.this.mContextView.getSubtitle();
    }
    
    public CharSequence getTitle()
    {
      return WindowDecorActionBar.this.mContextView.getTitle();
    }
    
    public void invalidate()
    {
      if (WindowDecorActionBar.this.mActionMode != this) {
        return;
      }
      this.mMenu.stopDispatchingItemsChanged();
      try
      {
        this.mCallback.onPrepareActionMode(this, this.mMenu);
        return;
      }
      finally
      {
        this.mMenu.startDispatchingItemsChanged();
      }
    }
    
    public boolean isTitleOptional()
    {
      return WindowDecorActionBar.this.mContextView.isTitleOptional();
    }
    
    public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean) {}
    
    public void onCloseSubMenu(SubMenuBuilder paramSubMenuBuilder) {}
    
    public boolean onMenuItemSelected(MenuBuilder paramMenuBuilder, MenuItem paramMenuItem)
    {
      if (this.mCallback != null) {
        return this.mCallback.onActionItemClicked(this, paramMenuItem);
      }
      return false;
    }
    
    public void onMenuModeChange(MenuBuilder paramMenuBuilder)
    {
      if (this.mCallback == null) {
        return;
      }
      invalidate();
      WindowDecorActionBar.this.mContextView.showOverflowMenu();
    }
    
    public boolean onSubMenuSelected(SubMenuBuilder paramSubMenuBuilder)
    {
      boolean bool = true;
      if (this.mCallback == null) {
        bool = false;
      }
      while (!paramSubMenuBuilder.hasVisibleItems()) {
        return bool;
      }
      new MenuPopupHelper(WindowDecorActionBar.this.getThemedContext(), paramSubMenuBuilder).show();
      return bool;
    }
    
    public void setCustomView(View paramView)
    {
      WindowDecorActionBar.this.mContextView.setCustomView(paramView);
      this.mCustomView = new WeakReference(paramView);
    }
    
    public void setSubtitle(int paramInt)
    {
      setSubtitle(WindowDecorActionBar.this.mContext.getResources().getString(paramInt));
    }
    
    public void setSubtitle(CharSequence paramCharSequence)
    {
      WindowDecorActionBar.this.mContextView.setSubtitle(paramCharSequence);
    }
    
    public void setTitle(int paramInt)
    {
      setTitle(WindowDecorActionBar.this.mContext.getResources().getString(paramInt));
    }
    
    public void setTitle(CharSequence paramCharSequence)
    {
      WindowDecorActionBar.this.mContextView.setTitle(paramCharSequence);
    }
    
    public void setTitleOptionalHint(boolean paramBoolean)
    {
      super.setTitleOptionalHint(paramBoolean);
      WindowDecorActionBar.this.mContextView.setTitleOptional(paramBoolean);
    }
  }
  
  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public class TabImpl
    extends ActionBar.Tab
  {
    private ActionBar.TabListener mCallback;
    private CharSequence mContentDesc;
    private View mCustomView;
    private Drawable mIcon;
    private int mPosition = -1;
    private Object mTag;
    private CharSequence mText;
    
    public TabImpl() {}
    
    public ActionBar.TabListener getCallback()
    {
      return this.mCallback;
    }
    
    public CharSequence getContentDescription()
    {
      return this.mContentDesc;
    }
    
    public View getCustomView()
    {
      return this.mCustomView;
    }
    
    public Drawable getIcon()
    {
      return this.mIcon;
    }
    
    public int getPosition()
    {
      return this.mPosition;
    }
    
    public Object getTag()
    {
      return this.mTag;
    }
    
    public CharSequence getText()
    {
      return this.mText;
    }
    
    public void select()
    {
      WindowDecorActionBar.this.selectTab(this);
    }
    
    public ActionBar.Tab setContentDescription(int paramInt)
    {
      return setContentDescription(WindowDecorActionBar.this.mContext.getResources().getText(paramInt));
    }
    
    public ActionBar.Tab setContentDescription(CharSequence paramCharSequence)
    {
      this.mContentDesc = paramCharSequence;
      if (this.mPosition >= 0) {
        WindowDecorActionBar.this.mTabScrollView.updateTab(this.mPosition);
      }
      return this;
    }
    
    public ActionBar.Tab setCustomView(int paramInt)
    {
      return setCustomView(LayoutInflater.from(WindowDecorActionBar.this.getThemedContext()).inflate(paramInt, null));
    }
    
    public ActionBar.Tab setCustomView(View paramView)
    {
      this.mCustomView = paramView;
      if (this.mPosition >= 0) {
        WindowDecorActionBar.this.mTabScrollView.updateTab(this.mPosition);
      }
      return this;
    }
    
    public ActionBar.Tab setIcon(int paramInt)
    {
      return setIcon(AppCompatResources.getDrawable(WindowDecorActionBar.this.mContext, paramInt));
    }
    
    public ActionBar.Tab setIcon(Drawable paramDrawable)
    {
      this.mIcon = paramDrawable;
      if (this.mPosition >= 0) {
        WindowDecorActionBar.this.mTabScrollView.updateTab(this.mPosition);
      }
      return this;
    }
    
    public void setPosition(int paramInt)
    {
      this.mPosition = paramInt;
    }
    
    public ActionBar.Tab setTabListener(ActionBar.TabListener paramTabListener)
    {
      this.mCallback = paramTabListener;
      return this;
    }
    
    public ActionBar.Tab setTag(Object paramObject)
    {
      this.mTag = paramObject;
      return this;
    }
    
    public ActionBar.Tab setText(int paramInt)
    {
      return setText(WindowDecorActionBar.this.mContext.getResources().getText(paramInt));
    }
    
    public ActionBar.Tab setText(CharSequence paramCharSequence)
    {
      this.mText = paramCharSequence;
      if (this.mPosition >= 0) {
        WindowDecorActionBar.this.mTabScrollView.updateTab(this.mPosition);
      }
      return this;
    }
  }
}
