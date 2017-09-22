package android.support.v7.widget;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.support.v4.graphics.drawable.DrawableWrapper;
import android.util.AttributeSet;
import android.widget.ProgressBar;

class AppCompatProgressBarHelper
{
  private static final int[] TINT_ATTRS = { 16843067, 16843068 };
  private Bitmap mSampleTile;
  private final ProgressBar mView;
  
  AppCompatProgressBarHelper(ProgressBar paramProgressBar)
  {
    this.mView = paramProgressBar;
  }
  
  private Shape getDrawableShape()
  {
    return new RoundRectShape(new float[] { 5.0F, 5.0F, 5.0F, 5.0F, 5.0F, 5.0F, 5.0F, 5.0F }, null, null);
  }
  
  private Drawable tileify(Drawable paramDrawable, boolean paramBoolean)
  {
    if ((paramDrawable instanceof DrawableWrapper))
    {
      Drawable localDrawable2 = ((DrawableWrapper)paramDrawable).getWrappedDrawable();
      if (localDrawable2 != null)
      {
        Drawable localDrawable3 = tileify(localDrawable2, paramBoolean);
        ((DrawableWrapper)paramDrawable).setWrappedDrawable(localDrawable3);
      }
    }
    do
    {
      Object localObject2 = paramDrawable;
      for (;;)
      {
        return localObject2;
        if (!(paramDrawable instanceof LayerDrawable)) {
          break;
        }
        LayerDrawable localLayerDrawable = (LayerDrawable)paramDrawable;
        int i = localLayerDrawable.getNumberOfLayers();
        Drawable[] arrayOfDrawable = new Drawable[i];
        int j = 0;
        if (j < i)
        {
          int m = localLayerDrawable.getId(j);
          Drawable localDrawable1 = localLayerDrawable.getDrawable(j);
          if ((m == 16908301) || (m == 16908303)) {}
          for (boolean bool = true;; bool = false)
          {
            arrayOfDrawable[j] = tileify(localDrawable1, bool);
            j++;
            break;
          }
        }
        localObject2 = new LayerDrawable(arrayOfDrawable);
        for (int k = 0; k < i; k++) {
          ((LayerDrawable)localObject2).setId(k, localLayerDrawable.getId(k));
        }
      }
    } while (!(paramDrawable instanceof BitmapDrawable));
    BitmapDrawable localBitmapDrawable = (BitmapDrawable)paramDrawable;
    Bitmap localBitmap = localBitmapDrawable.getBitmap();
    if (this.mSampleTile == null) {
      this.mSampleTile = localBitmap;
    }
    Object localObject1 = new ShapeDrawable(getDrawableShape());
    BitmapShader localBitmapShader = new BitmapShader(localBitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
    ((ShapeDrawable)localObject1).getPaint().setShader(localBitmapShader);
    ((ShapeDrawable)localObject1).getPaint().setColorFilter(localBitmapDrawable.getPaint().getColorFilter());
    if (paramBoolean) {
      localObject1 = new ClipDrawable((Drawable)localObject1, 3, 1);
    }
    return localObject1;
  }
  
  private Drawable tileifyIndeterminate(Drawable paramDrawable)
  {
    if ((paramDrawable instanceof AnimationDrawable))
    {
      AnimationDrawable localAnimationDrawable1 = (AnimationDrawable)paramDrawable;
      int i = localAnimationDrawable1.getNumberOfFrames();
      AnimationDrawable localAnimationDrawable2 = new AnimationDrawable();
      localAnimationDrawable2.setOneShot(localAnimationDrawable1.isOneShot());
      for (int j = 0; j < i; j++)
      {
        Drawable localDrawable = tileify(localAnimationDrawable1.getFrame(j), true);
        localDrawable.setLevel(10000);
        localAnimationDrawable2.addFrame(localDrawable, localAnimationDrawable1.getDuration(j));
      }
      localAnimationDrawable2.setLevel(10000);
      paramDrawable = localAnimationDrawable2;
    }
    return paramDrawable;
  }
  
  Bitmap getSampleTime()
  {
    return this.mSampleTile;
  }
  
  void loadFromAttributes(AttributeSet paramAttributeSet, int paramInt)
  {
    TintTypedArray localTintTypedArray = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), paramAttributeSet, TINT_ATTRS, paramInt, 0);
    Drawable localDrawable1 = localTintTypedArray.getDrawableIfKnown(0);
    if (localDrawable1 != null) {
      this.mView.setIndeterminateDrawable(tileifyIndeterminate(localDrawable1));
    }
    Drawable localDrawable2 = localTintTypedArray.getDrawableIfKnown(1);
    if (localDrawable2 != null) {
      this.mView.setProgressDrawable(tileify(localDrawable2, false));
    }
    localTintTypedArray.recycle();
  }
}
