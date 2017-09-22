package android.support.v4.animation;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

@TargetApi(9)
@RequiresApi(9)
class GingerbreadAnimatorCompatProvider
  implements AnimatorProvider
{
  GingerbreadAnimatorCompatProvider() {}
  
  public void clearInterpolator(View paramView) {}
  
  public ValueAnimatorCompat emptyValueAnimator()
  {
    return new GingerbreadFloatValueAnimator();
  }
  
  private static class GingerbreadFloatValueAnimator
    implements ValueAnimatorCompat
  {
    private long mDuration = 200L;
    private boolean mEnded = false;
    private float mFraction = 0.0F;
    List<AnimatorListenerCompat> mListeners = new ArrayList();
    private Runnable mLoopRunnable = new Runnable()
    {
      public void run()
      {
        float f = 1.0F * (float)(GingerbreadAnimatorCompatProvider.GingerbreadFloatValueAnimator.this.getTime() - GingerbreadAnimatorCompatProvider.GingerbreadFloatValueAnimator.this.mStartTime) / (float)GingerbreadAnimatorCompatProvider.GingerbreadFloatValueAnimator.this.mDuration;
        if ((f > 1.0F) || (GingerbreadAnimatorCompatProvider.GingerbreadFloatValueAnimator.this.mTarget.getParent() == null)) {
          f = 1.0F;
        }
        GingerbreadAnimatorCompatProvider.GingerbreadFloatValueAnimator.access$302(GingerbreadAnimatorCompatProvider.GingerbreadFloatValueAnimator.this, f);
        GingerbreadAnimatorCompatProvider.GingerbreadFloatValueAnimator.this.notifyUpdateListeners();
        if (GingerbreadAnimatorCompatProvider.GingerbreadFloatValueAnimator.this.mFraction >= 1.0F)
        {
          GingerbreadAnimatorCompatProvider.GingerbreadFloatValueAnimator.this.dispatchEnd();
          return;
        }
        GingerbreadAnimatorCompatProvider.GingerbreadFloatValueAnimator.this.mTarget.postDelayed(GingerbreadAnimatorCompatProvider.GingerbreadFloatValueAnimator.this.mLoopRunnable, 16L);
      }
    };
    private long mStartTime;
    private boolean mStarted = false;
    View mTarget;
    List<AnimatorUpdateListenerCompat> mUpdateListeners = new ArrayList();
    
    public GingerbreadFloatValueAnimator() {}
    
    private void dispatchCancel()
    {
      for (int i = -1 + this.mListeners.size(); i >= 0; i--) {
        ((AnimatorListenerCompat)this.mListeners.get(i)).onAnimationCancel(this);
      }
    }
    
    private void dispatchEnd()
    {
      for (int i = -1 + this.mListeners.size(); i >= 0; i--) {
        ((AnimatorListenerCompat)this.mListeners.get(i)).onAnimationEnd(this);
      }
    }
    
    private void dispatchStart()
    {
      for (int i = -1 + this.mListeners.size(); i >= 0; i--) {
        ((AnimatorListenerCompat)this.mListeners.get(i)).onAnimationStart(this);
      }
    }
    
    private long getTime()
    {
      return this.mTarget.getDrawingTime();
    }
    
    private void notifyUpdateListeners()
    {
      for (int i = -1 + this.mUpdateListeners.size(); i >= 0; i--) {
        ((AnimatorUpdateListenerCompat)this.mUpdateListeners.get(i)).onAnimationUpdate(this);
      }
    }
    
    public void addListener(AnimatorListenerCompat paramAnimatorListenerCompat)
    {
      this.mListeners.add(paramAnimatorListenerCompat);
    }
    
    public void addUpdateListener(AnimatorUpdateListenerCompat paramAnimatorUpdateListenerCompat)
    {
      this.mUpdateListeners.add(paramAnimatorUpdateListenerCompat);
    }
    
    public void cancel()
    {
      if (this.mEnded) {
        return;
      }
      this.mEnded = true;
      if (this.mStarted) {
        dispatchCancel();
      }
      dispatchEnd();
    }
    
    public float getAnimatedFraction()
    {
      return this.mFraction;
    }
    
    public void setDuration(long paramLong)
    {
      if (!this.mStarted) {
        this.mDuration = paramLong;
      }
    }
    
    public void setTarget(View paramView)
    {
      this.mTarget = paramView;
    }
    
    public void start()
    {
      if (this.mStarted) {
        return;
      }
      this.mStarted = true;
      dispatchStart();
      this.mFraction = 0.0F;
      this.mStartTime = getTime();
      this.mTarget.postDelayed(this.mLoopRunnable, 16L);
    }
  }
}
