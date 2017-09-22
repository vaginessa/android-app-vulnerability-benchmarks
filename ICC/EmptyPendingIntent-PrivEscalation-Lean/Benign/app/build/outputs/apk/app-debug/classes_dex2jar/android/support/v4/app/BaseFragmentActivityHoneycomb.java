package android.support.v4.app;

import android.content.Context;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View;

abstract class BaseFragmentActivityHoneycomb
  extends BaseFragmentActivityGingerbread
{
  BaseFragmentActivityHoneycomb() {}
  
  public View onCreateView(View paramView, String paramString, Context paramContext, AttributeSet paramAttributeSet)
  {
    View localView = dispatchFragmentsOnCreateView(paramView, paramString, paramContext, paramAttributeSet);
    if ((localView == null) && (Build.VERSION.SDK_INT >= 11)) {
      localView = super.onCreateView(paramView, paramString, paramContext, paramAttributeSet);
    }
    return localView;
  }
}
