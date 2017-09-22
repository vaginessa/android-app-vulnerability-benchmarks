package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.Notification.Action.Builder;
import android.app.Notification.Builder;
import android.app.Notification.MessagingStyle;
import android.app.Notification.MessagingStyle.Message;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@TargetApi(24)
@RequiresApi(24)
class NotificationCompatApi24
{
  public static final String CATEGORY_ALARM = "alarm";
  public static final String CATEGORY_CALL = "call";
  public static final String CATEGORY_EMAIL = "email";
  public static final String CATEGORY_ERROR = "err";
  public static final String CATEGORY_EVENT = "event";
  public static final String CATEGORY_MESSAGE = "msg";
  public static final String CATEGORY_PROGRESS = "progress";
  public static final String CATEGORY_PROMO = "promo";
  public static final String CATEGORY_RECOMMENDATION = "recommendation";
  public static final String CATEGORY_SERVICE = "service";
  public static final String CATEGORY_SOCIAL = "social";
  public static final String CATEGORY_STATUS = "status";
  public static final String CATEGORY_SYSTEM = "sys";
  public static final String CATEGORY_TRANSPORT = "transport";
  
  NotificationCompatApi24() {}
  
  public static void addMessagingStyle(NotificationBuilderWithBuilderAccessor paramNotificationBuilderWithBuilderAccessor, CharSequence paramCharSequence1, CharSequence paramCharSequence2, List<CharSequence> paramList1, List<Long> paramList, List<CharSequence> paramList2, List<String> paramList3, List<Uri> paramList4)
  {
    Notification.MessagingStyle localMessagingStyle = new Notification.MessagingStyle(paramCharSequence1).setConversationTitle(paramCharSequence2);
    for (int i = 0; i < paramList1.size(); i++)
    {
      Notification.MessagingStyle.Message localMessage = new Notification.MessagingStyle.Message((CharSequence)paramList1.get(i), ((Long)paramList.get(i)).longValue(), (CharSequence)paramList2.get(i));
      if (paramList3.get(i) != null) {
        localMessage.setData((String)paramList3.get(i), (Uri)paramList4.get(i));
      }
      localMessagingStyle.addMessage(localMessage);
    }
    localMessagingStyle.setBuilder(paramNotificationBuilderWithBuilderAccessor.getBuilder());
  }
  
  public static class Builder
    implements NotificationBuilderWithBuilderAccessor, NotificationBuilderWithActions
  {
    private Notification.Builder b;
    
    public Builder(Context paramContext, Notification paramNotification1, CharSequence paramCharSequence1, CharSequence paramCharSequence2, CharSequence paramCharSequence3, RemoteViews paramRemoteViews1, int paramInt1, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, Bitmap paramBitmap, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt4, CharSequence paramCharSequence4, boolean paramBoolean4, String paramString1, ArrayList<String> paramArrayList, Bundle paramBundle, int paramInt5, int paramInt6, Notification paramNotification2, String paramString2, boolean paramBoolean5, String paramString3, CharSequence[] paramArrayOfCharSequence, RemoteViews paramRemoteViews2, RemoteViews paramRemoteViews3, RemoteViews paramRemoteViews4)
    {
      Notification.Builder localBuilder1 = new Notification.Builder(paramContext).setWhen(paramNotification1.when).setShowWhen(paramBoolean2).setSmallIcon(paramNotification1.icon, paramNotification1.iconLevel).setContent(paramNotification1.contentView).setTicker(paramNotification1.tickerText, paramRemoteViews1).setSound(paramNotification1.sound, paramNotification1.audioStreamType).setVibrate(paramNotification1.vibrate).setLights(paramNotification1.ledARGB, paramNotification1.ledOnMS, paramNotification1.ledOffMS);
      boolean bool1;
      boolean bool2;
      label120:
      boolean bool3;
      label142:
      Notification.Builder localBuilder4;
      if ((0x2 & paramNotification1.flags) != 0)
      {
        bool1 = true;
        Notification.Builder localBuilder2 = localBuilder1.setOngoing(bool1);
        if ((0x8 & paramNotification1.flags) == 0) {
          break label388;
        }
        bool2 = true;
        Notification.Builder localBuilder3 = localBuilder2.setOnlyAlertOnce(bool2);
        if ((0x10 & paramNotification1.flags) == 0) {
          break label394;
        }
        bool3 = true;
        localBuilder4 = localBuilder3.setAutoCancel(bool3).setDefaults(paramNotification1.defaults).setContentTitle(paramCharSequence1).setContentText(paramCharSequence2).setSubText(paramCharSequence4).setContentInfo(paramCharSequence3).setContentIntent(paramPendingIntent1).setDeleteIntent(paramNotification1.deleteIntent);
        if ((0x80 & paramNotification1.flags) == 0) {
          break label400;
        }
      }
      label388:
      label394:
      label400:
      for (boolean bool4 = true;; bool4 = false)
      {
        this.b = localBuilder4.setFullScreenIntent(paramPendingIntent2, bool4).setLargeIcon(paramBitmap).setNumber(paramInt1).setUsesChronometer(paramBoolean3).setPriority(paramInt4).setProgress(paramInt2, paramInt3, paramBoolean1).setLocalOnly(paramBoolean4).setExtras(paramBundle).setGroup(paramString2).setGroupSummary(paramBoolean5).setSortKey(paramString3).setCategory(paramString1).setColor(paramInt5).setVisibility(paramInt6).setPublicVersion(paramNotification2).setRemoteInputHistory(paramArrayOfCharSequence);
        if (paramRemoteViews2 != null) {
          this.b.setCustomContentView(paramRemoteViews2);
        }
        if (paramRemoteViews3 != null) {
          this.b.setCustomBigContentView(paramRemoteViews3);
        }
        if (paramRemoteViews4 != null) {
          this.b.setCustomHeadsUpContentView(paramRemoteViews4);
        }
        Iterator localIterator = paramArrayList.iterator();
        while (localIterator.hasNext())
        {
          String str = (String)localIterator.next();
          this.b.addPerson(str);
        }
        bool1 = false;
        break;
        bool2 = false;
        break label120;
        bool3 = false;
        break label142;
      }
    }
    
    public void addAction(NotificationCompatBase.Action paramAction)
    {
      Notification.Action.Builder localBuilder = new Notification.Action.Builder(paramAction.getIcon(), paramAction.getTitle(), paramAction.getActionIntent());
      if (paramAction.getRemoteInputs() != null)
      {
        RemoteInput[] arrayOfRemoteInput = RemoteInputCompatApi20.fromCompat(paramAction.getRemoteInputs());
        int i = arrayOfRemoteInput.length;
        for (int j = 0; j < i; j++) {
          localBuilder.addRemoteInput(arrayOfRemoteInput[j]);
        }
      }
      if (paramAction.getExtras() != null) {}
      for (Bundle localBundle = new Bundle(paramAction.getExtras());; localBundle = new Bundle())
      {
        localBundle.putBoolean("android.support.allowGeneratedReplies", paramAction.getAllowGeneratedReplies());
        localBuilder.addExtras(localBundle);
        localBuilder.setAllowGeneratedReplies(paramAction.getAllowGeneratedReplies());
        this.b.addAction(localBuilder.build());
        return;
      }
    }
    
    public Notification build()
    {
      return this.b.build();
    }
    
    public Notification.Builder getBuilder()
    {
      return this.b;
    }
  }
}
