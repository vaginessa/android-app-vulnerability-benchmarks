package android.support.v4.app;

import android.annotation.TargetApi;
import android.graphics.Rect;
import android.support.annotation.RequiresApi;
import android.transition.Transition;
import android.transition.Transition.EpicenterCallback;
import android.transition.Transition.TransitionListener;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@TargetApi(21)
@RequiresApi(21)
class FragmentTransitionCompat21
{
  FragmentTransitionCompat21() {}
  
  public static void addTarget(Object paramObject, View paramView)
  {
    if (paramObject != null) {
      ((Transition)paramObject).addTarget(paramView);
    }
  }
  
  public static void addTargets(Object paramObject, ArrayList<View> paramArrayList)
  {
    Transition localTransition = (Transition)paramObject;
    if (localTransition == null) {}
    for (;;)
    {
      return;
      if ((localTransition instanceof TransitionSet))
      {
        TransitionSet localTransitionSet = (TransitionSet)localTransition;
        int k = localTransitionSet.getTransitionCount();
        for (int m = 0; m < k; m++) {
          addTargets(localTransitionSet.getTransitionAt(m), paramArrayList);
        }
      }
      else if ((!hasSimpleTarget(localTransition)) && (isNullOrEmpty(localTransition.getTargets())))
      {
        int i = paramArrayList.size();
        for (int j = 0; j < i; j++) {
          localTransition.addTarget((View)paramArrayList.get(j));
        }
      }
    }
  }
  
  public static void beginDelayedTransition(ViewGroup paramViewGroup, Object paramObject)
  {
    TransitionManager.beginDelayedTransition(paramViewGroup, (Transition)paramObject);
  }
  
  private static void bfsAddViewChildren(List<View> paramList, View paramView)
  {
    int i = paramList.size();
    if (containedBeforeIndex(paramList, paramView, i)) {}
    for (;;)
    {
      return;
      paramList.add(paramView);
      for (int j = i; j < paramList.size(); j++)
      {
        View localView1 = (View)paramList.get(j);
        if ((localView1 instanceof ViewGroup))
        {
          ViewGroup localViewGroup = (ViewGroup)localView1;
          int k = localViewGroup.getChildCount();
          for (int m = 0; m < k; m++)
          {
            View localView2 = localViewGroup.getChildAt(m);
            if (!containedBeforeIndex(paramList, localView2, i)) {
              paramList.add(localView2);
            }
          }
        }
      }
    }
  }
  
  public static void captureTransitioningViews(ArrayList<View> paramArrayList, View paramView)
  {
    ViewGroup localViewGroup;
    if (paramView.getVisibility() == 0)
    {
      if (!(paramView instanceof ViewGroup)) {
        break label65;
      }
      localViewGroup = (ViewGroup)paramView;
      if (!localViewGroup.isTransitionGroup()) {
        break label33;
      }
      paramArrayList.add(localViewGroup);
    }
    for (;;)
    {
      return;
      label33:
      int i = localViewGroup.getChildCount();
      for (int j = 0; j < i; j++) {
        captureTransitioningViews(paramArrayList, localViewGroup.getChildAt(j));
      }
    }
    label65:
    paramArrayList.add(paramView);
  }
  
  public static Object cloneTransition(Object paramObject)
  {
    Transition localTransition = null;
    if (paramObject != null) {
      localTransition = ((Transition)paramObject).clone();
    }
    return localTransition;
  }
  
  private static boolean containedBeforeIndex(List<View> paramList, View paramView, int paramInt)
  {
    for (int i = 0; i < paramInt; i++) {
      if (paramList.get(i) == paramView) {
        return true;
      }
    }
    return false;
  }
  
  private static String findKeyForValue(Map<String, String> paramMap, String paramString)
  {
    Iterator localIterator = paramMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      if (paramString.equals(localEntry.getValue())) {
        return (String)localEntry.getKey();
      }
    }
    return null;
  }
  
  public static void findNamedViews(Map<String, View> paramMap, View paramView)
  {
    if (paramView.getVisibility() == 0)
    {
      String str = paramView.getTransitionName();
      if (str != null) {
        paramMap.put(str, paramView);
      }
      if ((paramView instanceof ViewGroup))
      {
        ViewGroup localViewGroup = (ViewGroup)paramView;
        int i = localViewGroup.getChildCount();
        for (int j = 0; j < i; j++) {
          findNamedViews(paramMap, localViewGroup.getChildAt(j));
        }
      }
    }
  }
  
  public static void getBoundsOnScreen(View paramView, Rect paramRect)
  {
    int[] arrayOfInt = new int[2];
    paramView.getLocationOnScreen(arrayOfInt);
    paramRect.set(arrayOfInt[0], arrayOfInt[1], arrayOfInt[0] + paramView.getWidth(), arrayOfInt[1] + paramView.getHeight());
  }
  
  private static boolean hasSimpleTarget(Transition paramTransition)
  {
    return (!isNullOrEmpty(paramTransition.getTargetIds())) || (!isNullOrEmpty(paramTransition.getTargetNames())) || (!isNullOrEmpty(paramTransition.getTargetTypes()));
  }
  
  private static boolean isNullOrEmpty(List paramList)
  {
    return (paramList == null) || (paramList.isEmpty());
  }
  
  public static Object mergeTransitionsInSequence(Object paramObject1, Object paramObject2, Object paramObject3)
  {
    Transition localTransition1 = (Transition)paramObject1;
    Transition localTransition2 = (Transition)paramObject2;
    Transition localTransition3 = (Transition)paramObject3;
    Object localObject;
    if ((localTransition1 != null) && (localTransition2 != null)) {
      localObject = new TransitionSet().addTransition(localTransition1).addTransition(localTransition2).setOrdering(1);
    }
    while (localTransition3 != null)
    {
      TransitionSet localTransitionSet = new TransitionSet();
      if (localObject != null) {
        localTransitionSet.addTransition((Transition)localObject);
      }
      localTransitionSet.addTransition(localTransition3);
      return localTransitionSet;
      if (localTransition1 != null)
      {
        localObject = localTransition1;
      }
      else
      {
        localObject = null;
        if (localTransition2 != null) {
          localObject = localTransition2;
        }
      }
    }
    return localObject;
  }
  
  public static Object mergeTransitionsTogether(Object paramObject1, Object paramObject2, Object paramObject3)
  {
    TransitionSet localTransitionSet = new TransitionSet();
    if (paramObject1 != null) {
      localTransitionSet.addTransition((Transition)paramObject1);
    }
    if (paramObject2 != null) {
      localTransitionSet.addTransition((Transition)paramObject2);
    }
    if (paramObject3 != null) {
      localTransitionSet.addTransition((Transition)paramObject3);
    }
    return localTransitionSet;
  }
  
  public static ArrayList<String> prepareSetNameOverridesOptimized(ArrayList<View> paramArrayList)
  {
    ArrayList localArrayList = new ArrayList();
    int i = paramArrayList.size();
    for (int j = 0; j < i; j++)
    {
      View localView = (View)paramArrayList.get(j);
      localArrayList.add(localView.getTransitionName());
      localView.setTransitionName(null);
    }
    return localArrayList;
  }
  
  public static void removeTarget(Object paramObject, View paramView)
  {
    if (paramObject != null) {
      ((Transition)paramObject).removeTarget(paramView);
    }
  }
  
  public static void replaceTargets(Object paramObject, ArrayList<View> paramArrayList1, ArrayList<View> paramArrayList2)
  {
    Transition localTransition = (Transition)paramObject;
    if ((localTransition instanceof TransitionSet))
    {
      TransitionSet localTransitionSet = (TransitionSet)localTransition;
      int m = localTransitionSet.getTransitionCount();
      for (int n = 0; n < m; n++) {
        replaceTargets(localTransitionSet.getTransitionAt(n), paramArrayList1, paramArrayList2);
      }
    }
    if (!hasSimpleTarget(localTransition))
    {
      List localList = localTransition.getTargets();
      if ((localList != null) && (localList.size() == paramArrayList1.size()) && (localList.containsAll(paramArrayList1)))
      {
        if (paramArrayList2 == null) {}
        for (int i = 0;; i = paramArrayList2.size()) {
          for (int j = 0; j < i; j++) {
            localTransition.addTarget((View)paramArrayList2.get(j));
          }
        }
        for (int k = -1 + paramArrayList1.size(); k >= 0; k--) {
          localTransition.removeTarget((View)paramArrayList1.get(k));
        }
      }
    }
  }
  
  public static void scheduleHideFragmentView(Object paramObject, View paramView, final ArrayList<View> paramArrayList)
  {
    ((Transition)paramObject).addListener(new Transition.TransitionListener()
    {
      public void onTransitionCancel(Transition paramAnonymousTransition) {}
      
      public void onTransitionEnd(Transition paramAnonymousTransition)
      {
        paramAnonymousTransition.removeListener(this);
        this.val$fragmentView.setVisibility(8);
        int i = paramArrayList.size();
        for (int j = 0; j < i; j++) {
          ((View)paramArrayList.get(j)).setVisibility(0);
        }
      }
      
      public void onTransitionPause(Transition paramAnonymousTransition) {}
      
      public void onTransitionResume(Transition paramAnonymousTransition) {}
      
      public void onTransitionStart(Transition paramAnonymousTransition) {}
    });
  }
  
  public static void scheduleNameReset(ViewGroup paramViewGroup, ArrayList<View> paramArrayList, final Map<String, String> paramMap)
  {
    OneShotPreDrawListener.add(paramViewGroup, new Runnable()
    {
      public void run()
      {
        int i = this.val$sharedElementsIn.size();
        for (int j = 0; j < i; j++)
        {
          View localView = (View)this.val$sharedElementsIn.get(j);
          String str = localView.getTransitionName();
          localView.setTransitionName((String)paramMap.get(str));
        }
      }
    });
  }
  
  public static void scheduleRemoveTargets(Object paramObject1, Object paramObject2, final ArrayList<View> paramArrayList1, final Object paramObject3, final ArrayList<View> paramArrayList2, final Object paramObject4, final ArrayList<View> paramArrayList3)
  {
    ((Transition)paramObject1).addListener(new Transition.TransitionListener()
    {
      public void onTransitionCancel(Transition paramAnonymousTransition) {}
      
      public void onTransitionEnd(Transition paramAnonymousTransition) {}
      
      public void onTransitionPause(Transition paramAnonymousTransition) {}
      
      public void onTransitionResume(Transition paramAnonymousTransition) {}
      
      public void onTransitionStart(Transition paramAnonymousTransition)
      {
        if (this.val$enterTransition != null) {
          FragmentTransitionCompat21.replaceTargets(this.val$enterTransition, paramArrayList1, null);
        }
        if (paramObject3 != null) {
          FragmentTransitionCompat21.replaceTargets(paramObject3, paramArrayList2, null);
        }
        if (paramObject4 != null) {
          FragmentTransitionCompat21.replaceTargets(paramObject4, paramArrayList3, null);
        }
      }
    });
  }
  
  public static void setEpicenter(Object paramObject, Rect paramRect)
  {
    if (paramObject != null) {
      ((Transition)paramObject).setEpicenterCallback(new Transition.EpicenterCallback()
      {
        public Rect onGetEpicenter(Transition paramAnonymousTransition)
        {
          if ((this.val$epicenter == null) || (this.val$epicenter.isEmpty())) {
            return null;
          }
          return this.val$epicenter;
        }
      });
    }
  }
  
  public static void setEpicenter(Object paramObject, View paramView)
  {
    if (paramView != null)
    {
      Transition localTransition = (Transition)paramObject;
      Rect localRect = new Rect();
      getBoundsOnScreen(paramView, localRect);
      localTransition.setEpicenterCallback(new Transition.EpicenterCallback()
      {
        public Rect onGetEpicenter(Transition paramAnonymousTransition)
        {
          return this.val$epicenter;
        }
      });
    }
  }
  
  public static void setNameOverridesOptimized(View paramView, final ArrayList<View> paramArrayList1, final ArrayList<View> paramArrayList2, final ArrayList<String> paramArrayList, Map<String, String> paramMap)
  {
    int i = paramArrayList2.size();
    final ArrayList localArrayList = new ArrayList();
    int j = 0;
    if (j < i)
    {
      View localView = (View)paramArrayList1.get(j);
      String str1 = localView.getTransitionName();
      localArrayList.add(str1);
      if (str1 == null) {}
      label127:
      for (;;)
      {
        j++;
        break;
        localView.setTransitionName(null);
        String str2 = (String)paramMap.get(str1);
        for (int k = 0;; k++)
        {
          if (k >= i) {
            break label127;
          }
          if (str2.equals(paramArrayList.get(k)))
          {
            ((View)paramArrayList2.get(k)).setTransitionName(str1);
            break;
          }
        }
      }
    }
    OneShotPreDrawListener.add(paramView, new Runnable()
    {
      public void run()
      {
        for (int i = 0; i < this.val$numSharedElements; i++)
        {
          ((View)paramArrayList2.get(i)).setTransitionName((String)paramArrayList.get(i));
          ((View)paramArrayList1.get(i)).setTransitionName((String)localArrayList.get(i));
        }
      }
    });
  }
  
  public static void setNameOverridesUnoptimized(View paramView, ArrayList<View> paramArrayList, final Map<String, String> paramMap)
  {
    OneShotPreDrawListener.add(paramView, new Runnable()
    {
      public void run()
      {
        int i = this.val$sharedElementsIn.size();
        for (int j = 0; j < i; j++)
        {
          View localView = (View)this.val$sharedElementsIn.get(j);
          String str = localView.getTransitionName();
          if (str != null) {
            localView.setTransitionName(FragmentTransitionCompat21.findKeyForValue(paramMap, str));
          }
        }
      }
    });
  }
  
  public static void setSharedElementTargets(Object paramObject, View paramView, ArrayList<View> paramArrayList)
  {
    TransitionSet localTransitionSet = (TransitionSet)paramObject;
    List localList = localTransitionSet.getTargets();
    localList.clear();
    int i = paramArrayList.size();
    for (int j = 0; j < i; j++) {
      bfsAddViewChildren(localList, (View)paramArrayList.get(j));
    }
    localList.add(paramView);
    paramArrayList.add(paramView);
    addTargets(localTransitionSet, paramArrayList);
  }
  
  public static void swapSharedElementTargets(Object paramObject, ArrayList<View> paramArrayList1, ArrayList<View> paramArrayList2)
  {
    TransitionSet localTransitionSet = (TransitionSet)paramObject;
    if (localTransitionSet != null)
    {
      localTransitionSet.getTargets().clear();
      localTransitionSet.getTargets().addAll(paramArrayList2);
      replaceTargets(localTransitionSet, paramArrayList1, paramArrayList2);
    }
  }
  
  public static Object wrapTransitionInSet(Object paramObject)
  {
    if (paramObject == null) {
      return null;
    }
    TransitionSet localTransitionSet = new TransitionSet();
    localTransitionSet.addTransition((Transition)paramObject);
    return localTransitionSet;
  }
}
