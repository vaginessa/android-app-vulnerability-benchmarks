package android.support.v7.view.menu;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.PopupWindow.OnDismissListener;

abstract class MenuPopup
  implements ShowableListMenu, MenuPresenter, AdapterView.OnItemClickListener
{
  private Rect mEpicenterBounds;
  
  MenuPopup() {}
  
  protected static int measureIndividualMenuWidth(ListAdapter paramListAdapter, ViewGroup paramViewGroup, Context paramContext, int paramInt)
  {
    int i = 0;
    View localView = null;
    int j = 0;
    int k = View.MeasureSpec.makeMeasureSpec(0, 0);
    int m = View.MeasureSpec.makeMeasureSpec(0, 0);
    int n = paramListAdapter.getCount();
    for (int i1 = 0; i1 < n; i1++)
    {
      int i2 = paramListAdapter.getItemViewType(i1);
      if (i2 != j)
      {
        j = i2;
        localView = null;
      }
      if (paramViewGroup == null) {
        paramViewGroup = new FrameLayout(paramContext);
      }
      localView = paramListAdapter.getView(i1, localView, paramViewGroup);
      localView.measure(k, m);
      int i3 = localView.getMeasuredWidth();
      if (i3 >= paramInt) {
        return paramInt;
      }
      if (i3 > i) {
        i = i3;
      }
    }
    return i;
  }
  
  protected static boolean shouldPreserveIconSpacing(MenuBuilder paramMenuBuilder)
  {
    int i = paramMenuBuilder.size();
    for (int j = 0;; j++)
    {
      boolean bool = false;
      if (j < i)
      {
        MenuItem localMenuItem = paramMenuBuilder.getItem(j);
        if ((localMenuItem.isVisible()) && (localMenuItem.getIcon() != null)) {
          bool = true;
        }
      }
      else
      {
        return bool;
      }
    }
  }
  
  protected static MenuAdapter toMenuAdapter(ListAdapter paramListAdapter)
  {
    if ((paramListAdapter instanceof HeaderViewListAdapter)) {
      return (MenuAdapter)((HeaderViewListAdapter)paramListAdapter).getWrappedAdapter();
    }
    return (MenuAdapter)paramListAdapter;
  }
  
  public abstract void addMenu(MenuBuilder paramMenuBuilder);
  
  protected boolean closeMenuOnSubMenuOpened()
  {
    return true;
  }
  
  public boolean collapseItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl)
  {
    return false;
  }
  
  public boolean expandItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl)
  {
    return false;
  }
  
  public Rect getEpicenterBounds()
  {
    return this.mEpicenterBounds;
  }
  
  public int getId()
  {
    return 0;
  }
  
  public MenuView getMenuView(ViewGroup paramViewGroup)
  {
    throw new UnsupportedOperationException("MenuPopups manage their own views");
  }
  
  public void initForMenu(@NonNull Context paramContext, @Nullable MenuBuilder paramMenuBuilder) {}
  
  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    ListAdapter localListAdapter = (ListAdapter)paramAdapterView.getAdapter();
    MenuBuilder localMenuBuilder = toMenuAdapter(localListAdapter).mAdapterMenu;
    MenuItem localMenuItem = (MenuItem)localListAdapter.getItem(paramInt);
    if (closeMenuOnSubMenuOpened()) {}
    for (int i = 0;; i = 4)
    {
      localMenuBuilder.performItemAction(localMenuItem, this, i);
      return;
    }
  }
  
  public abstract void setAnchorView(View paramView);
  
  public void setEpicenterBounds(Rect paramRect)
  {
    this.mEpicenterBounds = paramRect;
  }
  
  public abstract void setForceShowIcon(boolean paramBoolean);
  
  public abstract void setGravity(int paramInt);
  
  public abstract void setHorizontalOffset(int paramInt);
  
  public abstract void setOnDismissListener(PopupWindow.OnDismissListener paramOnDismissListener);
  
  public abstract void setShowTitle(boolean paramBoolean);
  
  public abstract void setVerticalOffset(int paramInt);
}
