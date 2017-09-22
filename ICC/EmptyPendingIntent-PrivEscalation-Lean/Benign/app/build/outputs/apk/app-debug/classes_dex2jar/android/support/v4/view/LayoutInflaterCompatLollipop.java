package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;

@TargetApi(21)
@RequiresApi(21)
class LayoutInflaterCompatLollipop
{
  LayoutInflaterCompatLollipop() {}
  
  static void setFactory(LayoutInflater paramLayoutInflater, LayoutInflaterFactory paramLayoutInflaterFactory)
  {
    if (paramLayoutInflaterFactory != null) {}
    for (LayoutInflaterCompatHC.FactoryWrapperHC localFactoryWrapperHC = new LayoutInflaterCompatHC.FactoryWrapperHC(paramLayoutInflaterFactory);; localFactoryWrapperHC = null)
    {
      paramLayoutInflater.setFactory2(localFactoryWrapperHC);
      return;
    }
  }
}
