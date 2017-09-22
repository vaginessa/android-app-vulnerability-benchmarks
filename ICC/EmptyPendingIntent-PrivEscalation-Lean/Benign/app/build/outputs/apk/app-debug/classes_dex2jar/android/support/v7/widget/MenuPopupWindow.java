package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.v7.view.menu.ListMenuItemView;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.transition.Transition;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import java.lang.reflect.Method;

@RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
public class MenuPopupWindow
  extends ListPopupWindow
  implements MenuItemHoverListener
{
  private static final String TAG = "MenuPopupWindow";
  private static Method sSetTouchModalMethod;
  private MenuItemHoverListener mHoverListener;
  
  static
  {
    try
    {
      Class[] arrayOfClass = new Class[1];
      arrayOfClass[0] = Boolean.TYPE;
      sSetTouchModalMethod = PopupWindow.class.getDeclaredMethod("setTouchModal", arrayOfClass);
      return;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      Log.i("MenuPopupWindow", "Could not find method setTouchModal() on PopupWindow. Oh well.");
    }
  }
  
  public MenuPopupWindow(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  DropDownListView createDropDownListView(Context paramContext, boolean paramBoolean)
  {
    MenuDropDownListView localMenuDropDownListView = new MenuDropDownListView(paramContext, paramBoolean);
    localMenuDropDownListView.setHoverListener(this);
    return localMenuDropDownListView;
  }
  
  public void onItemHoverEnter(@NonNull MenuBuilder paramMenuBuilder, @NonNull MenuItem paramMenuItem)
  {
    if (this.mHoverListener != null) {
      this.mHoverListener.onItemHoverEnter(paramMenuBuilder, paramMenuItem);
    }
  }
  
  public void onItemHoverExit(@NonNull MenuBuilder paramMenuBuilder, @NonNull MenuItem paramMenuItem)
  {
    if (this.mHoverListener != null) {
      this.mHoverListener.onItemHoverExit(paramMenuBuilder, paramMenuItem);
    }
  }
  
  public void setEnterTransition(Object paramObject)
  {
    if (Build.VERSION.SDK_INT >= 23) {
      this.mPopup.setEnterTransition((Transition)paramObject);
    }
  }
  
  public void setExitTransition(Object paramObject)
  {
    if (Build.VERSION.SDK_INT >= 23) {
      this.mPopup.setExitTransition((Transition)paramObject);
    }
  }
  
  public void setHoverListener(MenuItemHoverListener paramMenuItemHoverListener)
  {
    this.mHoverListener = paramMenuItemHoverListener;
  }
  
  public void setTouchModal(boolean paramBoolean)
  {
    if (sSetTouchModalMethod != null) {}
    try
    {
      Method localMethod = sSetTouchModalMethod;
      PopupWindow localPopupWindow = this.mPopup;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Boolean.valueOf(paramBoolean);
      localMethod.invoke(localPopupWindow, arrayOfObject);
      return;
    }
    catch (Exception localException)
    {
      Log.i("MenuPopupWindow", "Could not invoke setTouchModal() on PopupWindow. Oh well.");
    }
  }
  
  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public static class MenuDropDownListView
    extends DropDownListView
  {
    final int mAdvanceKey;
    private MenuItemHoverListener mHoverListener;
    private MenuItem mHoveredMenuItem;
    final int mRetreatKey;
    
    public MenuDropDownListView(Context paramContext, boolean paramBoolean)
    {
      super(paramBoolean);
      Configuration localConfiguration = paramContext.getResources().getConfiguration();
      if ((Build.VERSION.SDK_INT >= 17) && (1 == localConfiguration.getLayoutDirection()))
      {
        this.mAdvanceKey = 21;
        this.mRetreatKey = 22;
        return;
      }
      this.mAdvanceKey = 22;
      this.mRetreatKey = 21;
    }
    
    public void clearSelection()
    {
      setSelection(-1);
    }
    
    public boolean onHoverEvent(MotionEvent paramMotionEvent)
    {
      ListAdapter localListAdapter;
      int i;
      MenuAdapter localMenuAdapter;
      if (this.mHoverListener != null)
      {
        localListAdapter = getAdapter();
        if (!(localListAdapter instanceof HeaderViewListAdapter)) {
          break label188;
        }
        HeaderViewListAdapter localHeaderViewListAdapter = (HeaderViewListAdapter)localListAdapter;
        i = localHeaderViewListAdapter.getHeadersCount();
        localMenuAdapter = (MenuAdapter)localHeaderViewListAdapter.getWrappedAdapter();
      }
      for (;;)
      {
        int j = paramMotionEvent.getAction();
        MenuItemImpl localMenuItemImpl = null;
        if (j != 10)
        {
          int k = pointToPosition((int)paramMotionEvent.getX(), (int)paramMotionEvent.getY());
          localMenuItemImpl = null;
          if (k != -1)
          {
            int m = k - i;
            localMenuItemImpl = null;
            if (m >= 0)
            {
              int n = localMenuAdapter.getCount();
              localMenuItemImpl = null;
              if (m < n) {
                localMenuItemImpl = localMenuAdapter.getItem(m);
              }
            }
          }
        }
        MenuItem localMenuItem = this.mHoveredMenuItem;
        if (localMenuItem != localMenuItemImpl)
        {
          MenuBuilder localMenuBuilder = localMenuAdapter.getAdapterMenu();
          if (localMenuItem != null) {
            this.mHoverListener.onItemHoverExit(localMenuBuilder, localMenuItem);
          }
          this.mHoveredMenuItem = localMenuItemImpl;
          if (localMenuItemImpl != null) {
            this.mHoverListener.onItemHoverEnter(localMenuBuilder, localMenuItemImpl);
          }
        }
        return super.onHoverEvent(paramMotionEvent);
        label188:
        localMenuAdapter = (MenuAdapter)localListAdapter;
        i = 0;
      }
    }
    
    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
    {
      ListMenuItemView localListMenuItemView = (ListMenuItemView)getSelectedView();
      if ((localListMenuItemView != null) && (paramInt == this.mAdvanceKey))
      {
        if ((localListMenuItemView.isEnabled()) && (localListMenuItemView.getItemData().hasSubMenu())) {
          performItemClick(localListMenuItemView, getSelectedItemPosition(), getSelectedItemId());
        }
        return true;
      }
      if ((localListMenuItemView != null) && (paramInt == this.mRetreatKey))
      {
        setSelection(-1);
        ((MenuAdapter)getAdapter()).getAdapterMenu().close(false);
        return true;
      }
      return super.onKeyDown(paramInt, paramKeyEvent);
    }
    
    public void setHoverListener(MenuItemHoverListener paramMenuItemHoverListener)
    {
      this.mHoverListener = paramMenuItemHoverListener;
    }
  }
}
