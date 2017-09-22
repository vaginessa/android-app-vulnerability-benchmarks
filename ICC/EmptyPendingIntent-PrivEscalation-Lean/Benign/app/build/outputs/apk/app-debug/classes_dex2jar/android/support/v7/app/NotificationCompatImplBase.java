package android.support.v7.app;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompat.Action;
import android.support.v4.app.NotificationCompatBase.Action;
import android.support.v7.appcompat.R.color;
import android.support.v7.appcompat.R.dimen;
import android.support.v7.appcompat.R.drawable;
import android.support.v7.appcompat.R.id;
import android.support.v7.appcompat.R.integer;
import android.support.v7.appcompat.R.layout;
import android.support.v7.appcompat.R.string;
import android.widget.RemoteViews;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@TargetApi(9)
@RequiresApi(9)
class NotificationCompatImplBase
{
  private static final int MAX_ACTION_BUTTONS = 3;
  static final int MAX_MEDIA_BUTTONS = 5;
  static final int MAX_MEDIA_BUTTONS_IN_COMPACT = 3;
  
  NotificationCompatImplBase() {}
  
  public static RemoteViews applyStandardTemplate(Context paramContext, CharSequence paramCharSequence1, CharSequence paramCharSequence2, CharSequence paramCharSequence3, int paramInt1, int paramInt2, Bitmap paramBitmap, CharSequence paramCharSequence4, boolean paramBoolean1, long paramLong, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean2)
  {
    Resources localResources = paramContext.getResources();
    RemoteViews localRemoteViews = new RemoteViews(paramContext.getPackageName(), paramInt5);
    int i;
    label77:
    label109:
    label171:
    label180:
    int j;
    int k;
    label228:
    label256:
    int m;
    label314:
    label426:
    int i1;
    label442:
    int i2;
    if (paramInt3 < -1)
    {
      i = 1;
      if ((Build.VERSION.SDK_INT >= 16) && (Build.VERSION.SDK_INT < 21))
      {
        if (i == 0) {
          break label482;
        }
        localRemoteViews.setInt(R.id.notification_background, "setBackgroundResource", R.drawable.notification_bg_low);
        localRemoteViews.setInt(R.id.icon, "setBackgroundResource", R.drawable.notification_template_icon_low_bg);
      }
      if (paramBitmap == null) {
        break label542;
      }
      if (Build.VERSION.SDK_INT < 16) {
        break label511;
      }
      localRemoteViews.setViewVisibility(R.id.icon, 0);
      localRemoteViews.setImageViewBitmap(R.id.icon, paramBitmap);
      if (paramInt2 != 0)
      {
        int i5 = localResources.getDimensionPixelSize(R.dimen.notification_right_icon_size);
        int i6 = i5 - 2 * localResources.getDimensionPixelSize(R.dimen.notification_small_icon_background_padding);
        if (Build.VERSION.SDK_INT < 21) {
          break label524;
        }
        Bitmap localBitmap2 = createIconWithBackground(paramContext, paramInt2, i5, i6, paramInt4);
        localRemoteViews.setImageViewBitmap(R.id.right_icon, localBitmap2);
        localRemoteViews.setViewVisibility(R.id.right_icon, 0);
      }
      if (paramCharSequence1 != null) {
        localRemoteViews.setTextViewText(R.id.title, paramCharSequence1);
      }
      j = 0;
      if (paramCharSequence2 != null)
      {
        localRemoteViews.setTextViewText(R.id.text, paramCharSequence2);
        j = 1;
      }
      if ((Build.VERSION.SDK_INT >= 21) || (paramBitmap == null)) {
        break label630;
      }
      k = 1;
      if (paramCharSequence3 == null) {
        break label636;
      }
      localRemoteViews.setTextViewText(R.id.info, paramCharSequence3);
      localRemoteViews.setViewVisibility(R.id.info, 0);
      j = 1;
      k = 1;
      m = 0;
      if (paramCharSequence4 != null)
      {
        int i4 = Build.VERSION.SDK_INT;
        m = 0;
        if (i4 >= 16)
        {
          localRemoteViews.setTextViewText(R.id.text, paramCharSequence4);
          if (paramCharSequence2 == null) {
            break label725;
          }
          localRemoteViews.setTextViewText(R.id.text2, paramCharSequence2);
          localRemoteViews.setViewVisibility(R.id.text2, 0);
          m = 1;
        }
      }
      if ((m != 0) && (Build.VERSION.SDK_INT >= 16))
      {
        if (paramBoolean2)
        {
          float f = localResources.getDimensionPixelSize(R.dimen.notification_subtext_size);
          localRemoteViews.setTextViewTextSize(R.id.text, 0, f);
        }
        localRemoteViews.setViewPadding(R.id.line1, 0, 0, 0, 0);
      }
      if (paramLong != 0L)
      {
        if ((!paramBoolean1) || (Build.VERSION.SDK_INT < 16)) {
          break label741;
        }
        localRemoteViews.setViewVisibility(R.id.chronometer, 0);
        localRemoteViews.setLong(R.id.chronometer, "setBase", paramLong + (SystemClock.elapsedRealtime() - System.currentTimeMillis()));
        localRemoteViews.setBoolean(R.id.chronometer, "setStarted", true);
        k = 1;
      }
      int n = R.id.right_side;
      if (k == 0) {
        break label765;
      }
      i1 = 0;
      localRemoteViews.setViewVisibility(n, i1);
      i2 = R.id.line3;
      if (j == 0) {
        break label772;
      }
    }
    label482:
    label511:
    label524:
    label542:
    label630:
    label636:
    label725:
    label741:
    label765:
    label772:
    for (int i3 = 0;; i3 = 8)
    {
      localRemoteViews.setViewVisibility(i2, i3);
      return localRemoteViews;
      i = 0;
      break;
      localRemoteViews.setInt(R.id.notification_background, "setBackgroundResource", R.drawable.notification_bg);
      localRemoteViews.setInt(R.id.icon, "setBackgroundResource", R.drawable.notification_template_icon_bg);
      break label77;
      localRemoteViews.setViewVisibility(R.id.icon, 8);
      break label109;
      localRemoteViews.setImageViewBitmap(R.id.right_icon, createColoredBitmap(paramContext, paramInt2, -1));
      break label171;
      if (paramInt2 == 0) {
        break label180;
      }
      localRemoteViews.setViewVisibility(R.id.icon, 0);
      if (Build.VERSION.SDK_INT >= 21)
      {
        Bitmap localBitmap1 = createIconWithBackground(paramContext, paramInt2, localResources.getDimensionPixelSize(R.dimen.notification_large_icon_width) - localResources.getDimensionPixelSize(R.dimen.notification_big_circle_margin), localResources.getDimensionPixelSize(R.dimen.notification_small_icon_size_as_large), paramInt4);
        localRemoteViews.setImageViewBitmap(R.id.icon, localBitmap1);
        break label180;
      }
      localRemoteViews.setImageViewBitmap(R.id.icon, createColoredBitmap(paramContext, paramInt2, -1));
      break label180;
      k = 0;
      break label228;
      if (paramInt1 > 0)
      {
        if (paramInt1 > localResources.getInteger(R.integer.status_bar_notification_info_maxnum)) {
          localRemoteViews.setTextViewText(R.id.info, localResources.getString(R.string.status_bar_notification_info_overflow));
        }
        for (;;)
        {
          localRemoteViews.setViewVisibility(R.id.info, 0);
          j = 1;
          k = 1;
          break;
          NumberFormat localNumberFormat = NumberFormat.getIntegerInstance();
          localRemoteViews.setTextViewText(R.id.info, localNumberFormat.format(paramInt1));
        }
      }
      localRemoteViews.setViewVisibility(R.id.info, 8);
      break label256;
      localRemoteViews.setViewVisibility(R.id.text2, 8);
      m = 0;
      break label314;
      localRemoteViews.setViewVisibility(R.id.time, 0);
      localRemoteViews.setLong(R.id.time, "setTime", paramLong);
      break label426;
      i1 = 8;
      break label442;
    }
  }
  
  public static RemoteViews applyStandardTemplateWithActions(Context paramContext, CharSequence paramCharSequence1, CharSequence paramCharSequence2, CharSequence paramCharSequence3, int paramInt1, int paramInt2, Bitmap paramBitmap, CharSequence paramCharSequence4, boolean paramBoolean1, long paramLong, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean2, ArrayList<NotificationCompat.Action> paramArrayList)
  {
    RemoteViews localRemoteViews1 = applyStandardTemplate(paramContext, paramCharSequence1, paramCharSequence2, paramCharSequence3, paramInt1, paramInt2, paramBitmap, paramCharSequence4, paramBoolean1, paramLong, paramInt3, paramInt4, paramInt5, paramBoolean2);
    localRemoteViews1.removeAllViews(R.id.actions);
    int i = 0;
    if (paramArrayList != null)
    {
      int k = paramArrayList.size();
      i = 0;
      if (k > 0)
      {
        i = 1;
        if (k > 3) {
          k = 3;
        }
        for (int m = 0; m < k; m++)
        {
          RemoteViews localRemoteViews2 = generateActionButton(paramContext, (NotificationCompat.Action)paramArrayList.get(m));
          localRemoteViews1.addView(R.id.actions, localRemoteViews2);
        }
      }
    }
    if (i != 0) {}
    for (int j = 0;; j = 8)
    {
      localRemoteViews1.setViewVisibility(R.id.actions, j);
      localRemoteViews1.setViewVisibility(R.id.action_divider, j);
      return localRemoteViews1;
    }
  }
  
  public static void buildIntoRemoteViews(Context paramContext, RemoteViews paramRemoteViews1, RemoteViews paramRemoteViews2)
  {
    hideNormalContent(paramRemoteViews1);
    paramRemoteViews1.removeAllViews(R.id.notification_main_column);
    paramRemoteViews1.addView(R.id.notification_main_column, paramRemoteViews2.clone());
    paramRemoteViews1.setViewVisibility(R.id.notification_main_column, 0);
    if (Build.VERSION.SDK_INT >= 21) {
      paramRemoteViews1.setViewPadding(R.id.notification_main_column_container, 0, calculateTopPadding(paramContext), 0, 0);
    }
  }
  
  public static int calculateTopPadding(Context paramContext)
  {
    int i = paramContext.getResources().getDimensionPixelSize(R.dimen.notification_top_pad);
    int j = paramContext.getResources().getDimensionPixelSize(R.dimen.notification_top_pad_large_text);
    float f = (constrain(paramContext.getResources().getConfiguration().fontScale, 1.0F, 1.3F) - 1.0F) / 0.29999995F;
    return Math.round((1.0F - f) * i + f * j);
  }
  
  public static float constrain(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (paramFloat1 < paramFloat2) {
      return paramFloat2;
    }
    if (paramFloat1 > paramFloat3) {
      return paramFloat3;
    }
    return paramFloat1;
  }
  
  private static Bitmap createColoredBitmap(Context paramContext, int paramInt1, int paramInt2)
  {
    return createColoredBitmap(paramContext, paramInt1, paramInt2, 0);
  }
  
  private static Bitmap createColoredBitmap(Context paramContext, int paramInt1, int paramInt2, int paramInt3)
  {
    Drawable localDrawable = paramContext.getResources().getDrawable(paramInt1);
    int i;
    if (paramInt3 == 0)
    {
      i = localDrawable.getIntrinsicWidth();
      if (paramInt3 != 0) {
        break label101;
      }
    }
    label101:
    for (int j = localDrawable.getIntrinsicHeight();; j = paramInt3)
    {
      Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.ARGB_8888);
      localDrawable.setBounds(0, 0, i, j);
      if (paramInt2 != 0) {
        localDrawable.mutate().setColorFilter(new PorterDuffColorFilter(paramInt2, PorterDuff.Mode.SRC_IN));
      }
      localDrawable.draw(new Canvas(localBitmap));
      return localBitmap;
      i = paramInt3;
      break;
    }
  }
  
  public static Bitmap createIconWithBackground(Context paramContext, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = R.drawable.notification_icon_background;
    if (paramInt4 == 0) {
      paramInt4 = 0;
    }
    Bitmap localBitmap = createColoredBitmap(paramContext, i, paramInt4, paramInt2);
    Canvas localCanvas = new Canvas(localBitmap);
    Drawable localDrawable = paramContext.getResources().getDrawable(paramInt1).mutate();
    localDrawable.setFilterBitmap(true);
    int j = (paramInt2 - paramInt3) / 2;
    localDrawable.setBounds(j, j, paramInt3 + j, paramInt3 + j);
    localDrawable.setColorFilter(new PorterDuffColorFilter(-1, PorterDuff.Mode.SRC_ATOP));
    localDrawable.draw(localCanvas);
    return localBitmap;
  }
  
  private static RemoteViews generateActionButton(Context paramContext, NotificationCompat.Action paramAction)
  {
    int i;
    String str;
    if (paramAction.actionIntent == null)
    {
      i = 1;
      str = paramContext.getPackageName();
      if (i == 0) {
        break label117;
      }
    }
    label117:
    for (int j = getActionTombstoneLayoutResource();; j = getActionLayoutResource())
    {
      RemoteViews localRemoteViews = new RemoteViews(str, j);
      localRemoteViews.setImageViewBitmap(R.id.action_image, createColoredBitmap(paramContext, paramAction.getIcon(), paramContext.getResources().getColor(R.color.notification_action_color_filter)));
      localRemoteViews.setTextViewText(R.id.action_text, paramAction.title);
      if (i == 0) {
        localRemoteViews.setOnClickPendingIntent(R.id.action_container, paramAction.actionIntent);
      }
      if (Build.VERSION.SDK_INT >= 15) {
        localRemoteViews.setContentDescription(R.id.action_container, paramAction.title);
      }
      return localRemoteViews;
      i = 0;
      break;
    }
  }
  
  @TargetApi(11)
  @RequiresApi(11)
  private static <T extends NotificationCompatBase.Action> RemoteViews generateContentViewMedia(Context paramContext, CharSequence paramCharSequence1, CharSequence paramCharSequence2, CharSequence paramCharSequence3, int paramInt1, Bitmap paramBitmap, CharSequence paramCharSequence4, boolean paramBoolean1, long paramLong, int paramInt2, List<T> paramList, int[] paramArrayOfInt, boolean paramBoolean2, PendingIntent paramPendingIntent, boolean paramBoolean3)
  {
    int i;
    RemoteViews localRemoteViews1;
    int j;
    int k;
    if (paramBoolean3)
    {
      i = R.layout.notification_template_media_custom;
      localRemoteViews1 = applyStandardTemplate(paramContext, paramCharSequence1, paramCharSequence2, paramCharSequence3, paramInt1, 0, paramBitmap, paramCharSequence4, paramBoolean1, paramLong, paramInt2, 0, i, true);
      j = paramList.size();
      if (paramArrayOfInt != null) {
        break label133;
      }
      k = 0;
      label53:
      localRemoteViews1.removeAllViews(R.id.media_actions);
      if (k <= 0) {
        break label182;
      }
    }
    for (int m = 0;; m++)
    {
      if (m >= k) {
        break label182;
      }
      if (m >= j)
      {
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = Integer.valueOf(m);
        arrayOfObject[1] = Integer.valueOf(j - 1);
        throw new IllegalArgumentException(String.format("setShowActionsInCompactView: action %d out of bounds (max %d)", arrayOfObject));
        i = R.layout.notification_template_media;
        break;
        label133:
        k = Math.min(paramArrayOfInt.length, 3);
        break label53;
      }
      RemoteViews localRemoteViews2 = generateMediaActionButton(paramContext, (NotificationCompatBase.Action)paramList.get(paramArrayOfInt[m]));
      localRemoteViews1.addView(R.id.media_actions, localRemoteViews2);
    }
    label182:
    if (paramBoolean2)
    {
      localRemoteViews1.setViewVisibility(R.id.end_padder, 8);
      localRemoteViews1.setViewVisibility(R.id.cancel_action, 0);
      localRemoteViews1.setOnClickPendingIntent(R.id.cancel_action, paramPendingIntent);
      localRemoteViews1.setInt(R.id.cancel_action, "setAlpha", paramContext.getResources().getInteger(R.integer.cancel_button_image_alpha));
      return localRemoteViews1;
    }
    localRemoteViews1.setViewVisibility(R.id.end_padder, 0);
    localRemoteViews1.setViewVisibility(R.id.cancel_action, 8);
    return localRemoteViews1;
  }
  
  @TargetApi(11)
  @RequiresApi(11)
  private static RemoteViews generateMediaActionButton(Context paramContext, NotificationCompatBase.Action paramAction)
  {
    if (paramAction.getActionIntent() == null) {}
    for (int i = 1;; i = 0)
    {
      RemoteViews localRemoteViews = new RemoteViews(paramContext.getPackageName(), R.layout.notification_media_action);
      localRemoteViews.setImageViewResource(R.id.action0, paramAction.getIcon());
      if (i == 0) {
        localRemoteViews.setOnClickPendingIntent(R.id.action0, paramAction.getActionIntent());
      }
      if (Build.VERSION.SDK_INT >= 15) {
        localRemoteViews.setContentDescription(R.id.action0, paramAction.getTitle());
      }
      return localRemoteViews;
    }
  }
  
  @TargetApi(11)
  @RequiresApi(11)
  public static <T extends NotificationCompatBase.Action> RemoteViews generateMediaBigView(Context paramContext, CharSequence paramCharSequence1, CharSequence paramCharSequence2, CharSequence paramCharSequence3, int paramInt1, Bitmap paramBitmap, CharSequence paramCharSequence4, boolean paramBoolean1, long paramLong, int paramInt2, int paramInt3, List<T> paramList, boolean paramBoolean2, PendingIntent paramPendingIntent, boolean paramBoolean3)
  {
    int i = Math.min(paramList.size(), 5);
    RemoteViews localRemoteViews1 = applyStandardTemplate(paramContext, paramCharSequence1, paramCharSequence2, paramCharSequence3, paramInt1, 0, paramBitmap, paramCharSequence4, paramBoolean1, paramLong, paramInt2, paramInt3, getBigMediaLayoutResource(paramBoolean3, i), false);
    localRemoteViews1.removeAllViews(R.id.media_actions);
    if (i > 0) {
      for (int j = 0; j < i; j++)
      {
        RemoteViews localRemoteViews2 = generateMediaActionButton(paramContext, (NotificationCompatBase.Action)paramList.get(j));
        localRemoteViews1.addView(R.id.media_actions, localRemoteViews2);
      }
    }
    if (paramBoolean2)
    {
      localRemoteViews1.setViewVisibility(R.id.cancel_action, 0);
      localRemoteViews1.setInt(R.id.cancel_action, "setAlpha", paramContext.getResources().getInteger(R.integer.cancel_button_image_alpha));
      localRemoteViews1.setOnClickPendingIntent(R.id.cancel_action, paramPendingIntent);
      return localRemoteViews1;
    }
    localRemoteViews1.setViewVisibility(R.id.cancel_action, 8);
    return localRemoteViews1;
  }
  
  private static int getActionLayoutResource()
  {
    return R.layout.notification_action;
  }
  
  private static int getActionTombstoneLayoutResource()
  {
    return R.layout.notification_action_tombstone;
  }
  
  @TargetApi(11)
  @RequiresApi(11)
  private static int getBigMediaLayoutResource(boolean paramBoolean, int paramInt)
  {
    if (paramInt <= 3)
    {
      if (paramBoolean) {
        return R.layout.notification_template_big_media_narrow_custom;
      }
      return R.layout.notification_template_big_media_narrow;
    }
    if (paramBoolean) {
      return R.layout.notification_template_big_media_custom;
    }
    return R.layout.notification_template_big_media;
  }
  
  private static void hideNormalContent(RemoteViews paramRemoteViews)
  {
    paramRemoteViews.setViewVisibility(R.id.title, 8);
    paramRemoteViews.setViewVisibility(R.id.text2, 8);
    paramRemoteViews.setViewVisibility(R.id.text, 8);
  }
  
  @TargetApi(11)
  @RequiresApi(11)
  public static <T extends NotificationCompatBase.Action> RemoteViews overrideContentViewMedia(NotificationBuilderWithBuilderAccessor paramNotificationBuilderWithBuilderAccessor, Context paramContext, CharSequence paramCharSequence1, CharSequence paramCharSequence2, CharSequence paramCharSequence3, int paramInt1, Bitmap paramBitmap, CharSequence paramCharSequence4, boolean paramBoolean1, long paramLong, int paramInt2, List<T> paramList, int[] paramArrayOfInt, boolean paramBoolean2, PendingIntent paramPendingIntent, boolean paramBoolean3)
  {
    RemoteViews localRemoteViews = generateContentViewMedia(paramContext, paramCharSequence1, paramCharSequence2, paramCharSequence3, paramInt1, paramBitmap, paramCharSequence4, paramBoolean1, paramLong, paramInt2, paramList, paramArrayOfInt, paramBoolean2, paramPendingIntent, paramBoolean3);
    paramNotificationBuilderWithBuilderAccessor.getBuilder().setContent(localRemoteViews);
    if (paramBoolean2) {
      paramNotificationBuilderWithBuilderAccessor.getBuilder().setOngoing(true);
    }
    return localRemoteViews;
  }
  
  @TargetApi(16)
  @RequiresApi(16)
  public static <T extends NotificationCompatBase.Action> void overrideMediaBigContentView(Notification paramNotification, Context paramContext, CharSequence paramCharSequence1, CharSequence paramCharSequence2, CharSequence paramCharSequence3, int paramInt1, Bitmap paramBitmap, CharSequence paramCharSequence4, boolean paramBoolean1, long paramLong, int paramInt2, int paramInt3, List<T> paramList, boolean paramBoolean2, PendingIntent paramPendingIntent, boolean paramBoolean3)
  {
    paramNotification.bigContentView = generateMediaBigView(paramContext, paramCharSequence1, paramCharSequence2, paramCharSequence3, paramInt1, paramBitmap, paramCharSequence4, paramBoolean1, paramLong, paramInt2, paramInt3, paramList, paramBoolean2, paramPendingIntent, paramBoolean3);
    if (paramBoolean2) {
      paramNotification.flags = (0x2 | paramNotification.flags);
    }
  }
}
