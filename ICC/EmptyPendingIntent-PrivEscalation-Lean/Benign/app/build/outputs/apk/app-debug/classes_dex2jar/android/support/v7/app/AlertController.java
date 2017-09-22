package android.support.v7.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.NestedScrollView.OnScrollChangeListener;
import android.support.v7.appcompat.R.attr;
import android.support.v7.appcompat.R.id;
import android.support.v7.appcompat.R.styleable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.ViewStub;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import java.lang.ref.WeakReference;

class AlertController
{
  ListAdapter mAdapter;
  private int mAlertDialogLayout;
  private final View.OnClickListener mButtonHandler = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      Message localMessage;
      if ((paramAnonymousView == AlertController.this.mButtonPositive) && (AlertController.this.mButtonPositiveMessage != null)) {
        localMessage = Message.obtain(AlertController.this.mButtonPositiveMessage);
      }
      for (;;)
      {
        if (localMessage != null) {
          localMessage.sendToTarget();
        }
        AlertController.this.mHandler.obtainMessage(1, AlertController.this.mDialog).sendToTarget();
        return;
        if ((paramAnonymousView == AlertController.this.mButtonNegative) && (AlertController.this.mButtonNegativeMessage != null)) {
          localMessage = Message.obtain(AlertController.this.mButtonNegativeMessage);
        } else if ((paramAnonymousView == AlertController.this.mButtonNeutral) && (AlertController.this.mButtonNeutralMessage != null)) {
          localMessage = Message.obtain(AlertController.this.mButtonNeutralMessage);
        } else {
          localMessage = null;
        }
      }
    }
  };
  Button mButtonNegative;
  Message mButtonNegativeMessage;
  private CharSequence mButtonNegativeText;
  Button mButtonNeutral;
  Message mButtonNeutralMessage;
  private CharSequence mButtonNeutralText;
  private int mButtonPanelLayoutHint = 0;
  private int mButtonPanelSideLayout;
  Button mButtonPositive;
  Message mButtonPositiveMessage;
  private CharSequence mButtonPositiveText;
  int mCheckedItem = -1;
  private final Context mContext;
  private View mCustomTitleView;
  final AppCompatDialog mDialog;
  Handler mHandler;
  private Drawable mIcon;
  private int mIconId = 0;
  private ImageView mIconView;
  int mListItemLayout;
  int mListLayout;
  ListView mListView;
  private CharSequence mMessage;
  private TextView mMessageView;
  int mMultiChoiceItemLayout;
  NestedScrollView mScrollView;
  private boolean mShowTitle;
  int mSingleChoiceItemLayout;
  private CharSequence mTitle;
  private TextView mTitleView;
  private View mView;
  private int mViewLayoutResId;
  private int mViewSpacingBottom;
  private int mViewSpacingLeft;
  private int mViewSpacingRight;
  private boolean mViewSpacingSpecified = false;
  private int mViewSpacingTop;
  private final Window mWindow;
  
  public AlertController(Context paramContext, AppCompatDialog paramAppCompatDialog, Window paramWindow)
  {
    this.mContext = paramContext;
    this.mDialog = paramAppCompatDialog;
    this.mWindow = paramWindow;
    this.mHandler = new ButtonHandler(paramAppCompatDialog);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(null, R.styleable.AlertDialog, R.attr.alertDialogStyle, 0);
    this.mAlertDialogLayout = localTypedArray.getResourceId(R.styleable.AlertDialog_android_layout, 0);
    this.mButtonPanelSideLayout = localTypedArray.getResourceId(R.styleable.AlertDialog_buttonPanelSideLayout, 0);
    this.mListLayout = localTypedArray.getResourceId(R.styleable.AlertDialog_listLayout, 0);
    this.mMultiChoiceItemLayout = localTypedArray.getResourceId(R.styleable.AlertDialog_multiChoiceItemLayout, 0);
    this.mSingleChoiceItemLayout = localTypedArray.getResourceId(R.styleable.AlertDialog_singleChoiceItemLayout, 0);
    this.mListItemLayout = localTypedArray.getResourceId(R.styleable.AlertDialog_listItemLayout, 0);
    this.mShowTitle = localTypedArray.getBoolean(R.styleable.AlertDialog_showTitle, true);
    localTypedArray.recycle();
    paramAppCompatDialog.supportRequestWindowFeature(1);
  }
  
  static boolean canTextInput(View paramView)
  {
    if (paramView.onCheckIsTextEditor()) {
      return true;
    }
    if (!(paramView instanceof ViewGroup)) {
      return false;
    }
    ViewGroup localViewGroup = (ViewGroup)paramView;
    int i = localViewGroup.getChildCount();
    while (i > 0)
    {
      i--;
      if (canTextInput(localViewGroup.getChildAt(i))) {
        return true;
      }
    }
    return false;
  }
  
  private void centerButton(Button paramButton)
  {
    LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)paramButton.getLayoutParams();
    localLayoutParams.gravity = 1;
    localLayoutParams.weight = 0.5F;
    paramButton.setLayoutParams(localLayoutParams);
  }
  
  static void manageScrollIndicators(View paramView1, View paramView2, View paramView3)
  {
    int j;
    int i;
    if (paramView2 != null)
    {
      if (ViewCompat.canScrollVertically(paramView1, -1))
      {
        j = 0;
        paramView2.setVisibility(j);
      }
    }
    else if (paramView3 != null)
    {
      boolean bool = ViewCompat.canScrollVertically(paramView1, 1);
      i = 0;
      if (!bool) {
        break label51;
      }
    }
    for (;;)
    {
      paramView3.setVisibility(i);
      return;
      j = 4;
      break;
      label51:
      i = 4;
    }
  }
  
  @Nullable
  private ViewGroup resolvePanel(@Nullable View paramView1, @Nullable View paramView2)
  {
    if (paramView1 == null)
    {
      if ((paramView2 instanceof ViewStub)) {
        paramView2 = ((ViewStub)paramView2).inflate();
      }
      return (ViewGroup)paramView2;
    }
    if (paramView2 != null)
    {
      ViewParent localViewParent = paramView2.getParent();
      if ((localViewParent instanceof ViewGroup)) {
        ((ViewGroup)localViewParent).removeView(paramView2);
      }
    }
    if ((paramView1 instanceof ViewStub)) {
      paramView1 = ((ViewStub)paramView1).inflate();
    }
    return (ViewGroup)paramView1;
  }
  
  private int selectContentView()
  {
    if (this.mButtonPanelSideLayout == 0) {
      return this.mAlertDialogLayout;
    }
    if (this.mButtonPanelLayoutHint == 1) {
      return this.mButtonPanelSideLayout;
    }
    return this.mAlertDialogLayout;
  }
  
  private void setScrollIndicators(ViewGroup paramViewGroup, View paramView, int paramInt1, int paramInt2)
  {
    View localView1 = this.mWindow.findViewById(R.id.scrollIndicatorUp);
    View localView2 = this.mWindow.findViewById(R.id.scrollIndicatorDown);
    if (Build.VERSION.SDK_INT >= 23)
    {
      ViewCompat.setScrollIndicators(paramView, paramInt1, paramInt2);
      if (localView1 != null) {
        paramViewGroup.removeView(localView1);
      }
      if (localView2 != null) {
        paramViewGroup.removeView(localView2);
      }
    }
    final View localView4;
    do
    {
      do
      {
        return;
        if ((localView1 != null) && ((paramInt1 & 0x1) == 0))
        {
          paramViewGroup.removeView(localView1);
          localView1 = null;
        }
        if ((localView2 != null) && ((paramInt1 & 0x2) == 0))
        {
          paramViewGroup.removeView(localView2);
          localView2 = null;
        }
      } while ((localView1 == null) && (localView2 == null));
      final View localView3 = localView1;
      localView4 = localView2;
      if (this.mMessage != null)
      {
        this.mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener()
        {
          public void onScrollChange(NestedScrollView paramAnonymousNestedScrollView, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, int paramAnonymousInt4)
          {
            AlertController.manageScrollIndicators(paramAnonymousNestedScrollView, localView3, localView4);
          }
        });
        this.mScrollView.post(new Runnable()
        {
          public void run()
          {
            AlertController.manageScrollIndicators(AlertController.this.mScrollView, localView3, localView4);
          }
        });
        return;
      }
      if (this.mListView != null)
      {
        this.mListView.setOnScrollListener(new AbsListView.OnScrollListener()
        {
          public void onScroll(AbsListView paramAnonymousAbsListView, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
          {
            AlertController.manageScrollIndicators(paramAnonymousAbsListView, localView3, localView4);
          }
          
          public void onScrollStateChanged(AbsListView paramAnonymousAbsListView, int paramAnonymousInt) {}
        });
        this.mListView.post(new Runnable()
        {
          public void run()
          {
            AlertController.manageScrollIndicators(AlertController.this.mListView, localView3, localView4);
          }
        });
        return;
      }
      if (localView3 != null) {
        paramViewGroup.removeView(localView3);
      }
    } while (localView4 == null);
    paramViewGroup.removeView(localView4);
  }
  
  private void setupButtons(ViewGroup paramViewGroup)
  {
    int i = 0;
    this.mButtonPositive = ((Button)paramViewGroup.findViewById(16908313));
    this.mButtonPositive.setOnClickListener(this.mButtonHandler);
    if (TextUtils.isEmpty(this.mButtonPositiveText))
    {
      this.mButtonPositive.setVisibility(8);
      this.mButtonNegative = ((Button)paramViewGroup.findViewById(16908314));
      this.mButtonNegative.setOnClickListener(this.mButtonHandler);
      if (!TextUtils.isEmpty(this.mButtonNegativeText)) {
        break label202;
      }
      this.mButtonNegative.setVisibility(8);
      label90:
      this.mButtonNeutral = ((Button)paramViewGroup.findViewById(16908315));
      this.mButtonNeutral.setOnClickListener(this.mButtonHandler);
      if (!TextUtils.isEmpty(this.mButtonNeutralText)) {
        break label228;
      }
      this.mButtonNeutral.setVisibility(8);
      label134:
      if (shouldCenterSingleButton(this.mContext))
      {
        if (i != 1) {
          break label254;
        }
        centerButton(this.mButtonPositive);
      }
    }
    for (;;)
    {
      int j = 0;
      if (i != 0) {
        j = 1;
      }
      if (j == 0) {
        paramViewGroup.setVisibility(8);
      }
      return;
      this.mButtonPositive.setText(this.mButtonPositiveText);
      this.mButtonPositive.setVisibility(0);
      i = 0x0 | 0x1;
      break;
      label202:
      this.mButtonNegative.setText(this.mButtonNegativeText);
      this.mButtonNegative.setVisibility(0);
      i |= 0x2;
      break label90;
      label228:
      this.mButtonNeutral.setText(this.mButtonNeutralText);
      this.mButtonNeutral.setVisibility(0);
      i |= 0x4;
      break label134;
      label254:
      if (i == 2) {
        centerButton(this.mButtonNegative);
      } else if (i == 4) {
        centerButton(this.mButtonNeutral);
      }
    }
  }
  
  private void setupContent(ViewGroup paramViewGroup)
  {
    this.mScrollView = ((NestedScrollView)this.mWindow.findViewById(R.id.scrollView));
    this.mScrollView.setFocusable(false);
    this.mScrollView.setNestedScrollingEnabled(false);
    this.mMessageView = ((TextView)paramViewGroup.findViewById(16908299));
    if (this.mMessageView == null) {
      return;
    }
    if (this.mMessage != null)
    {
      this.mMessageView.setText(this.mMessage);
      return;
    }
    this.mMessageView.setVisibility(8);
    this.mScrollView.removeView(this.mMessageView);
    if (this.mListView != null)
    {
      ViewGroup localViewGroup = (ViewGroup)this.mScrollView.getParent();
      int i = localViewGroup.indexOfChild(this.mScrollView);
      localViewGroup.removeViewAt(i);
      localViewGroup.addView(this.mListView, i, new ViewGroup.LayoutParams(-1, -1));
      return;
    }
    paramViewGroup.setVisibility(8);
  }
  
  private void setupCustomContent(ViewGroup paramViewGroup)
  {
    View localView;
    if (this.mView != null) {
      localView = this.mView;
    }
    for (;;)
    {
      int i = 0;
      if (localView != null) {
        i = 1;
      }
      if ((i == 0) || (!canTextInput(localView))) {
        this.mWindow.setFlags(131072, 131072);
      }
      if (i == 0) {
        break;
      }
      FrameLayout localFrameLayout = (FrameLayout)this.mWindow.findViewById(R.id.custom);
      localFrameLayout.addView(localView, new ViewGroup.LayoutParams(-1, -1));
      if (this.mViewSpacingSpecified) {
        localFrameLayout.setPadding(this.mViewSpacingLeft, this.mViewSpacingTop, this.mViewSpacingRight, this.mViewSpacingBottom);
      }
      if (this.mListView != null) {
        ((LinearLayout.LayoutParams)paramViewGroup.getLayoutParams()).weight = 0.0F;
      }
      return;
      if (this.mViewLayoutResId != 0) {
        localView = LayoutInflater.from(this.mContext).inflate(this.mViewLayoutResId, paramViewGroup, false);
      } else {
        localView = null;
      }
    }
    paramViewGroup.setVisibility(8);
  }
  
  private void setupTitle(ViewGroup paramViewGroup)
  {
    if (this.mCustomTitleView != null)
    {
      ViewGroup.LayoutParams localLayoutParams = new ViewGroup.LayoutParams(-1, -2);
      paramViewGroup.addView(this.mCustomTitleView, 0, localLayoutParams);
      this.mWindow.findViewById(R.id.title_template).setVisibility(8);
      return;
    }
    this.mIconView = ((ImageView)this.mWindow.findViewById(16908294));
    boolean bool = TextUtils.isEmpty(this.mTitle);
    int i = 0;
    if (!bool) {
      i = 1;
    }
    if ((i != 0) && (this.mShowTitle))
    {
      this.mTitleView = ((TextView)this.mWindow.findViewById(R.id.alertTitle));
      this.mTitleView.setText(this.mTitle);
      if (this.mIconId != 0)
      {
        this.mIconView.setImageResource(this.mIconId);
        return;
      }
      if (this.mIcon != null)
      {
        this.mIconView.setImageDrawable(this.mIcon);
        return;
      }
      this.mTitleView.setPadding(this.mIconView.getPaddingLeft(), this.mIconView.getPaddingTop(), this.mIconView.getPaddingRight(), this.mIconView.getPaddingBottom());
      this.mIconView.setVisibility(8);
      return;
    }
    this.mWindow.findViewById(R.id.title_template).setVisibility(8);
    this.mIconView.setVisibility(8);
    paramViewGroup.setVisibility(8);
  }
  
  private void setupView()
  {
    View localView1 = this.mWindow.findViewById(R.id.parentPanel);
    View localView2 = localView1.findViewById(R.id.topPanel);
    View localView3 = localView1.findViewById(R.id.contentPanel);
    View localView4 = localView1.findViewById(R.id.buttonPanel);
    ViewGroup localViewGroup1 = (ViewGroup)localView1.findViewById(R.id.customPanel);
    setupCustomContent(localViewGroup1);
    View localView5 = localViewGroup1.findViewById(R.id.topPanel);
    View localView6 = localViewGroup1.findViewById(R.id.contentPanel);
    View localView7 = localViewGroup1.findViewById(R.id.buttonPanel);
    ViewGroup localViewGroup2 = resolvePanel(localView5, localView2);
    ViewGroup localViewGroup3 = resolvePanel(localView6, localView3);
    ViewGroup localViewGroup4 = resolvePanel(localView7, localView4);
    setupContent(localViewGroup3);
    setupButtons(localViewGroup4);
    setupTitle(localViewGroup2);
    int i;
    boolean bool1;
    label166:
    boolean bool2;
    label184:
    label286:
    Object localObject;
    label328:
    int k;
    if ((localViewGroup1 != null) && (localViewGroup1.getVisibility() != 8))
    {
      i = 1;
      if ((localViewGroup2 == null) || (localViewGroup2.getVisibility() == 8)) {
        break label424;
      }
      bool1 = true;
      if ((localViewGroup4 == null) || (localViewGroup4.getVisibility() == 8)) {
        break label430;
      }
      bool2 = true;
      if ((!bool2) && (localViewGroup3 != null))
      {
        View localView10 = localViewGroup3.findViewById(R.id.textSpacerNoButtons);
        if (localView10 != null) {
          localView10.setVisibility(0);
        }
      }
      if (!bool1) {
        break label436;
      }
      if (this.mScrollView != null) {
        this.mScrollView.setClipToPadding(true);
      }
      View localView9;
      if ((this.mMessage == null) && (this.mListView == null))
      {
        localView9 = null;
        if (i == 0) {}
      }
      else
      {
        localView9 = null;
        if (i == 0) {
          localView9 = localViewGroup2.findViewById(R.id.titleDividerNoCustom);
        }
      }
      if (localView9 != null) {
        localView9.setVisibility(0);
      }
      if ((this.mListView instanceof RecycleListView)) {
        ((RecycleListView)this.mListView).setHasDecor(bool1, bool2);
      }
      if (i == 0)
      {
        if (this.mListView == null) {
          break label465;
        }
        localObject = this.mListView;
        if (localObject != null)
        {
          if (!bool1) {
            break label474;
          }
          k = 1;
          label341:
          if (!bool2) {
            break label480;
          }
        }
      }
    }
    label424:
    label430:
    label436:
    label465:
    label474:
    label480:
    for (int m = 2;; m = 0)
    {
      setScrollIndicators(localViewGroup3, (View)localObject, k | m, 3);
      ListView localListView = this.mListView;
      if ((localListView != null) && (this.mAdapter != null))
      {
        localListView.setAdapter(this.mAdapter);
        int j = this.mCheckedItem;
        if (j > -1)
        {
          localListView.setItemChecked(j, true);
          localListView.setSelection(j);
        }
      }
      return;
      i = 0;
      break;
      bool1 = false;
      break label166;
      bool2 = false;
      break label184;
      if (localViewGroup3 == null) {
        break label286;
      }
      View localView8 = localViewGroup3.findViewById(R.id.textSpacerNoTitle);
      if (localView8 == null) {
        break label286;
      }
      localView8.setVisibility(0);
      break label286;
      localObject = this.mScrollView;
      break label328;
      k = 0;
      break label341;
    }
  }
  
  private static boolean shouldCenterSingleButton(Context paramContext)
  {
    TypedValue localTypedValue = new TypedValue();
    paramContext.getTheme().resolveAttribute(R.attr.alertDialogCenterButtons, localTypedValue, true);
    return localTypedValue.data != 0;
  }
  
  public Button getButton(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return null;
    case -1: 
      return this.mButtonPositive;
    case -2: 
      return this.mButtonNegative;
    }
    return this.mButtonNeutral;
  }
  
  public int getIconAttributeResId(int paramInt)
  {
    TypedValue localTypedValue = new TypedValue();
    this.mContext.getTheme().resolveAttribute(paramInt, localTypedValue, true);
    return localTypedValue.resourceId;
  }
  
  public ListView getListView()
  {
    return this.mListView;
  }
  
  public void installContent()
  {
    int i = selectContentView();
    this.mDialog.setContentView(i);
    setupView();
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    return (this.mScrollView != null) && (this.mScrollView.executeKeyEvent(paramKeyEvent));
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    return (this.mScrollView != null) && (this.mScrollView.executeKeyEvent(paramKeyEvent));
  }
  
  public void setButton(int paramInt, CharSequence paramCharSequence, DialogInterface.OnClickListener paramOnClickListener, Message paramMessage)
  {
    if ((paramMessage == null) && (paramOnClickListener != null)) {
      paramMessage = this.mHandler.obtainMessage(paramInt, paramOnClickListener);
    }
    switch (paramInt)
    {
    default: 
      throw new IllegalArgumentException("Button does not exist");
    case -1: 
      this.mButtonPositiveText = paramCharSequence;
      this.mButtonPositiveMessage = paramMessage;
      return;
    case -2: 
      this.mButtonNegativeText = paramCharSequence;
      this.mButtonNegativeMessage = paramMessage;
      return;
    }
    this.mButtonNeutralText = paramCharSequence;
    this.mButtonNeutralMessage = paramMessage;
  }
  
  public void setButtonPanelLayoutHint(int paramInt)
  {
    this.mButtonPanelLayoutHint = paramInt;
  }
  
  public void setCustomTitle(View paramView)
  {
    this.mCustomTitleView = paramView;
  }
  
  public void setIcon(int paramInt)
  {
    this.mIcon = null;
    this.mIconId = paramInt;
    if (this.mIconView != null)
    {
      if (paramInt != 0)
      {
        this.mIconView.setVisibility(0);
        this.mIconView.setImageResource(this.mIconId);
      }
    }
    else {
      return;
    }
    this.mIconView.setVisibility(8);
  }
  
  public void setIcon(Drawable paramDrawable)
  {
    this.mIcon = paramDrawable;
    this.mIconId = 0;
    if (this.mIconView != null)
    {
      if (paramDrawable != null)
      {
        this.mIconView.setVisibility(0);
        this.mIconView.setImageDrawable(paramDrawable);
      }
    }
    else {
      return;
    }
    this.mIconView.setVisibility(8);
  }
  
  public void setMessage(CharSequence paramCharSequence)
  {
    this.mMessage = paramCharSequence;
    if (this.mMessageView != null) {
      this.mMessageView.setText(paramCharSequence);
    }
  }
  
  public void setTitle(CharSequence paramCharSequence)
  {
    this.mTitle = paramCharSequence;
    if (this.mTitleView != null) {
      this.mTitleView.setText(paramCharSequence);
    }
  }
  
  public void setView(int paramInt)
  {
    this.mView = null;
    this.mViewLayoutResId = paramInt;
    this.mViewSpacingSpecified = false;
  }
  
  public void setView(View paramView)
  {
    this.mView = paramView;
    this.mViewLayoutResId = 0;
    this.mViewSpacingSpecified = false;
  }
  
  public void setView(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    this.mView = paramView;
    this.mViewLayoutResId = 0;
    this.mViewSpacingSpecified = true;
    this.mViewSpacingLeft = paramInt1;
    this.mViewSpacingTop = paramInt2;
    this.mViewSpacingRight = paramInt3;
    this.mViewSpacingBottom = paramInt4;
  }
  
  public static class AlertParams
  {
    public ListAdapter mAdapter;
    public boolean mCancelable;
    public int mCheckedItem = -1;
    public boolean[] mCheckedItems;
    public final Context mContext;
    public Cursor mCursor;
    public View mCustomTitleView;
    public boolean mForceInverseBackground;
    public Drawable mIcon;
    public int mIconAttrId = 0;
    public int mIconId = 0;
    public final LayoutInflater mInflater;
    public String mIsCheckedColumn;
    public boolean mIsMultiChoice;
    public boolean mIsSingleChoice;
    public CharSequence[] mItems;
    public String mLabelColumn;
    public CharSequence mMessage;
    public DialogInterface.OnClickListener mNegativeButtonListener;
    public CharSequence mNegativeButtonText;
    public DialogInterface.OnClickListener mNeutralButtonListener;
    public CharSequence mNeutralButtonText;
    public DialogInterface.OnCancelListener mOnCancelListener;
    public DialogInterface.OnMultiChoiceClickListener mOnCheckboxClickListener;
    public DialogInterface.OnClickListener mOnClickListener;
    public DialogInterface.OnDismissListener mOnDismissListener;
    public AdapterView.OnItemSelectedListener mOnItemSelectedListener;
    public DialogInterface.OnKeyListener mOnKeyListener;
    public OnPrepareListViewListener mOnPrepareListViewListener;
    public DialogInterface.OnClickListener mPositiveButtonListener;
    public CharSequence mPositiveButtonText;
    public boolean mRecycleOnMeasure = true;
    public CharSequence mTitle;
    public View mView;
    public int mViewLayoutResId;
    public int mViewSpacingBottom;
    public int mViewSpacingLeft;
    public int mViewSpacingRight;
    public boolean mViewSpacingSpecified = false;
    public int mViewSpacingTop;
    
    public AlertParams(Context paramContext)
    {
      this.mContext = paramContext;
      this.mCancelable = true;
      this.mInflater = ((LayoutInflater)paramContext.getSystemService("layout_inflater"));
    }
    
    private void createListView(final AlertController paramAlertController)
    {
      final AlertController.RecycleListView localRecycleListView = (AlertController.RecycleListView)this.mInflater.inflate(paramAlertController.mListLayout, null);
      Object localObject;
      if (this.mIsMultiChoice) {
        if (this.mCursor == null)
        {
          localObject = new ArrayAdapter(this.mContext, paramAlertController.mMultiChoiceItemLayout, 16908308, this.mItems)
          {
            public View getView(int paramAnonymousInt, View paramAnonymousView, ViewGroup paramAnonymousViewGroup)
            {
              View localView = super.getView(paramAnonymousInt, paramAnonymousView, paramAnonymousViewGroup);
              if ((AlertController.AlertParams.this.mCheckedItems != null) && (AlertController.AlertParams.this.mCheckedItems[paramAnonymousInt] != 0)) {
                localRecycleListView.setItemChecked(paramAnonymousInt, true);
              }
              return localView;
            }
          };
          if (this.mOnPrepareListViewListener != null) {
            this.mOnPrepareListViewListener.onPrepareListView(localRecycleListView);
          }
          paramAlertController.mAdapter = ((ListAdapter)localObject);
          paramAlertController.mCheckedItem = this.mCheckedItem;
          if (this.mOnClickListener == null) {
            break label290;
          }
          localRecycleListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
          {
            public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
            {
              AlertController.AlertParams.this.mOnClickListener.onClick(paramAlertController.mDialog, paramAnonymousInt);
              if (!AlertController.AlertParams.this.mIsSingleChoice) {
                paramAlertController.mDialog.dismiss();
              }
            }
          });
          label106:
          if (this.mOnItemSelectedListener != null) {
            localRecycleListView.setOnItemSelectedListener(this.mOnItemSelectedListener);
          }
          if (!this.mIsSingleChoice) {
            break label314;
          }
          localRecycleListView.setChoiceMode(1);
        }
      }
      for (;;)
      {
        paramAlertController.mListView = localRecycleListView;
        return;
        Context localContext2 = this.mContext;
        Cursor localCursor2 = this.mCursor;
        localObject = new CursorAdapter(localContext2, localCursor2, false)
        {
          private final int mIsCheckedIndex;
          private final int mLabelIndex;
          
          public void bindView(View paramAnonymousView, Context paramAnonymousContext, Cursor paramAnonymousCursor)
          {
            int i = 1;
            ((CheckedTextView)paramAnonymousView.findViewById(16908308)).setText(paramAnonymousCursor.getString(this.mLabelIndex));
            AlertController.RecycleListView localRecycleListView = localRecycleListView;
            int k = paramAnonymousCursor.getPosition();
            if (paramAnonymousCursor.getInt(this.mIsCheckedIndex) == i) {}
            for (;;)
            {
              localRecycleListView.setItemChecked(k, i);
              return;
              int j = 0;
            }
          }
          
          public View newView(Context paramAnonymousContext, Cursor paramAnonymousCursor, ViewGroup paramAnonymousViewGroup)
          {
            return AlertController.AlertParams.this.mInflater.inflate(paramAlertController.mMultiChoiceItemLayout, paramAnonymousViewGroup, false);
          }
        };
        break;
        if (this.mIsSingleChoice) {}
        for (int i = paramAlertController.mSingleChoiceItemLayout;; i = paramAlertController.mListItemLayout)
        {
          if (this.mCursor == null) {
            break label251;
          }
          Context localContext1 = this.mContext;
          Cursor localCursor1 = this.mCursor;
          String[] arrayOfString = new String[1];
          arrayOfString[0] = this.mLabelColumn;
          localObject = new SimpleCursorAdapter(localContext1, i, localCursor1, arrayOfString, new int[] { 16908308 });
          break;
        }
        label251:
        if (this.mAdapter != null)
        {
          localObject = this.mAdapter;
          break;
        }
        localObject = new AlertController.CheckedItemAdapter(this.mContext, i, 16908308, this.mItems);
        break;
        label290:
        if (this.mOnCheckboxClickListener == null) {
          break label106;
        }
        localRecycleListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
          public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
          {
            if (AlertController.AlertParams.this.mCheckedItems != null) {
              AlertController.AlertParams.this.mCheckedItems[paramAnonymousInt] = localRecycleListView.isItemChecked(paramAnonymousInt);
            }
            AlertController.AlertParams.this.mOnCheckboxClickListener.onClick(paramAlertController.mDialog, paramAnonymousInt, localRecycleListView.isItemChecked(paramAnonymousInt));
          }
        });
        break label106;
        label314:
        if (this.mIsMultiChoice) {
          localRecycleListView.setChoiceMode(2);
        }
      }
    }
    
    public void apply(AlertController paramAlertController)
    {
      if (this.mCustomTitleView != null)
      {
        paramAlertController.setCustomTitle(this.mCustomTitleView);
        if (this.mMessage != null) {
          paramAlertController.setMessage(this.mMessage);
        }
        if (this.mPositiveButtonText != null) {
          paramAlertController.setButton(-1, this.mPositiveButtonText, this.mPositiveButtonListener, null);
        }
        if (this.mNegativeButtonText != null) {
          paramAlertController.setButton(-2, this.mNegativeButtonText, this.mNegativeButtonListener, null);
        }
        if (this.mNeutralButtonText != null) {
          paramAlertController.setButton(-3, this.mNeutralButtonText, this.mNeutralButtonListener, null);
        }
        if ((this.mItems != null) || (this.mCursor != null) || (this.mAdapter != null)) {
          createListView(paramAlertController);
        }
        if (this.mView == null) {
          break label236;
        }
        if (!this.mViewSpacingSpecified) {
          break label227;
        }
        paramAlertController.setView(this.mView, this.mViewSpacingLeft, this.mViewSpacingTop, this.mViewSpacingRight, this.mViewSpacingBottom);
      }
      label227:
      label236:
      while (this.mViewLayoutResId == 0)
      {
        return;
        if (this.mTitle != null) {
          paramAlertController.setTitle(this.mTitle);
        }
        if (this.mIcon != null) {
          paramAlertController.setIcon(this.mIcon);
        }
        if (this.mIconId != 0) {
          paramAlertController.setIcon(this.mIconId);
        }
        if (this.mIconAttrId == 0) {
          break;
        }
        paramAlertController.setIcon(paramAlertController.getIconAttributeResId(this.mIconAttrId));
        break;
        paramAlertController.setView(this.mView);
        return;
      }
      paramAlertController.setView(this.mViewLayoutResId);
    }
    
    public static abstract interface OnPrepareListViewListener
    {
      public abstract void onPrepareListView(ListView paramListView);
    }
  }
  
  private static final class ButtonHandler
    extends Handler
  {
    private static final int MSG_DISMISS_DIALOG = 1;
    private WeakReference<DialogInterface> mDialog;
    
    public ButtonHandler(DialogInterface paramDialogInterface)
    {
      this.mDialog = new WeakReference(paramDialogInterface);
    }
    
    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      case 0: 
      default: 
        return;
      case -3: 
      case -2: 
      case -1: 
        ((DialogInterface.OnClickListener)paramMessage.obj).onClick((DialogInterface)this.mDialog.get(), paramMessage.what);
        return;
      }
      ((DialogInterface)paramMessage.obj).dismiss();
    }
  }
  
  private static class CheckedItemAdapter
    extends ArrayAdapter<CharSequence>
  {
    public CheckedItemAdapter(Context paramContext, int paramInt1, int paramInt2, CharSequence[] paramArrayOfCharSequence)
    {
      super(paramInt1, paramInt2, paramArrayOfCharSequence);
    }
    
    public long getItemId(int paramInt)
    {
      return paramInt;
    }
    
    public boolean hasStableIds()
    {
      return true;
    }
  }
  
  public static class RecycleListView
    extends ListView
  {
    private final int mPaddingBottomNoButtons;
    private final int mPaddingTopNoTitle;
    
    public RecycleListView(Context paramContext)
    {
      this(paramContext, null);
    }
    
    public RecycleListView(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
      TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.RecycleListView);
      this.mPaddingBottomNoButtons = localTypedArray.getDimensionPixelOffset(R.styleable.RecycleListView_paddingBottomNoButtons, -1);
      this.mPaddingTopNoTitle = localTypedArray.getDimensionPixelOffset(R.styleable.RecycleListView_paddingTopNoTitle, -1);
    }
    
    public void setHasDecor(boolean paramBoolean1, boolean paramBoolean2)
    {
      int i;
      int j;
      int k;
      if ((!paramBoolean2) || (!paramBoolean1))
      {
        i = getPaddingLeft();
        if (!paramBoolean1) {
          break label51;
        }
        j = getPaddingTop();
        k = getPaddingRight();
        if (!paramBoolean2) {
          break label60;
        }
      }
      label51:
      label60:
      for (int m = getPaddingBottom();; m = this.mPaddingBottomNoButtons)
      {
        setPadding(i, j, k, m);
        return;
        j = this.mPaddingTopNoTitle;
        break;
      }
    }
  }
}
