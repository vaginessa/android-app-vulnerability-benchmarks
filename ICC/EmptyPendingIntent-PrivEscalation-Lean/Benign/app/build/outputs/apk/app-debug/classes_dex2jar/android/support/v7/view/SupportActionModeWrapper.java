package android.support.v7.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.RestrictTo;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.view.menu.MenuWrapperFactory;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import java.util.ArrayList;

@TargetApi(11)
@RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
public class SupportActionModeWrapper
  extends android.view.ActionMode
{
  final Context mContext;
  final ActionMode mWrappedObject;
  
  public SupportActionModeWrapper(Context paramContext, ActionMode paramActionMode)
  {
    this.mContext = paramContext;
    this.mWrappedObject = paramActionMode;
  }
  
  public void finish()
  {
    this.mWrappedObject.finish();
  }
  
  public View getCustomView()
  {
    return this.mWrappedObject.getCustomView();
  }
  
  public Menu getMenu()
  {
    return MenuWrapperFactory.wrapSupportMenu(this.mContext, (SupportMenu)this.mWrappedObject.getMenu());
  }
  
  public MenuInflater getMenuInflater()
  {
    return this.mWrappedObject.getMenuInflater();
  }
  
  public CharSequence getSubtitle()
  {
    return this.mWrappedObject.getSubtitle();
  }
  
  public Object getTag()
  {
    return this.mWrappedObject.getTag();
  }
  
  public CharSequence getTitle()
  {
    return this.mWrappedObject.getTitle();
  }
  
  public boolean getTitleOptionalHint()
  {
    return this.mWrappedObject.getTitleOptionalHint();
  }
  
  public void invalidate()
  {
    this.mWrappedObject.invalidate();
  }
  
  public boolean isTitleOptional()
  {
    return this.mWrappedObject.isTitleOptional();
  }
  
  public void setCustomView(View paramView)
  {
    this.mWrappedObject.setCustomView(paramView);
  }
  
  public void setSubtitle(int paramInt)
  {
    this.mWrappedObject.setSubtitle(paramInt);
  }
  
  public void setSubtitle(CharSequence paramCharSequence)
  {
    this.mWrappedObject.setSubtitle(paramCharSequence);
  }
  
  public void setTag(Object paramObject)
  {
    this.mWrappedObject.setTag(paramObject);
  }
  
  public void setTitle(int paramInt)
  {
    this.mWrappedObject.setTitle(paramInt);
  }
  
  public void setTitle(CharSequence paramCharSequence)
  {
    this.mWrappedObject.setTitle(paramCharSequence);
  }
  
  public void setTitleOptionalHint(boolean paramBoolean)
  {
    this.mWrappedObject.setTitleOptionalHint(paramBoolean);
  }
  
  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public static class CallbackWrapper
    implements ActionMode.Callback
  {
    final ArrayList<SupportActionModeWrapper> mActionModes;
    final Context mContext;
    final SimpleArrayMap<Menu, Menu> mMenus;
    final android.view.ActionMode.Callback mWrappedCallback;
    
    public CallbackWrapper(Context paramContext, android.view.ActionMode.Callback paramCallback)
    {
      this.mContext = paramContext;
      this.mWrappedCallback = paramCallback;
      this.mActionModes = new ArrayList();
      this.mMenus = new SimpleArrayMap();
    }
    
    private Menu getMenuWrapper(Menu paramMenu)
    {
      Menu localMenu = (Menu)this.mMenus.get(paramMenu);
      if (localMenu == null)
      {
        localMenu = MenuWrapperFactory.wrapSupportMenu(this.mContext, (SupportMenu)paramMenu);
        this.mMenus.put(paramMenu, localMenu);
      }
      return localMenu;
    }
    
    public android.view.ActionMode getActionModeWrapper(ActionMode paramActionMode)
    {
      int i = 0;
      int j = this.mActionModes.size();
      while (i < j)
      {
        SupportActionModeWrapper localSupportActionModeWrapper2 = (SupportActionModeWrapper)this.mActionModes.get(i);
        if ((localSupportActionModeWrapper2 != null) && (localSupportActionModeWrapper2.mWrappedObject == paramActionMode)) {
          return localSupportActionModeWrapper2;
        }
        i++;
      }
      SupportActionModeWrapper localSupportActionModeWrapper1 = new SupportActionModeWrapper(this.mContext, paramActionMode);
      this.mActionModes.add(localSupportActionModeWrapper1);
      return localSupportActionModeWrapper1;
    }
    
    public boolean onActionItemClicked(ActionMode paramActionMode, MenuItem paramMenuItem)
    {
      return this.mWrappedCallback.onActionItemClicked(getActionModeWrapper(paramActionMode), MenuWrapperFactory.wrapSupportMenuItem(this.mContext, (SupportMenuItem)paramMenuItem));
    }
    
    public boolean onCreateActionMode(ActionMode paramActionMode, Menu paramMenu)
    {
      return this.mWrappedCallback.onCreateActionMode(getActionModeWrapper(paramActionMode), getMenuWrapper(paramMenu));
    }
    
    public void onDestroyActionMode(ActionMode paramActionMode)
    {
      this.mWrappedCallback.onDestroyActionMode(getActionModeWrapper(paramActionMode));
    }
    
    public boolean onPrepareActionMode(ActionMode paramActionMode, Menu paramMenu)
    {
      return this.mWrappedCallback.onPrepareActionMode(getActionModeWrapper(paramActionMode), getMenuWrapper(paramMenu));
    }
  }
}
