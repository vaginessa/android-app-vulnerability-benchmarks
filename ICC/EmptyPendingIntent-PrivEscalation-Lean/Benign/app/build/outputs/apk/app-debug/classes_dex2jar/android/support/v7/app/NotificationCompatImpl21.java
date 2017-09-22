package android.support.v7.app;

import android.annotation.TargetApi;
import android.app.Notification.MediaStyle;
import android.media.session.MediaSession.Token;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;

@TargetApi(21)
@RequiresApi(21)
class NotificationCompatImpl21
{
  NotificationCompatImpl21() {}
  
  public static void addMediaStyle(NotificationBuilderWithBuilderAccessor paramNotificationBuilderWithBuilderAccessor, int[] paramArrayOfInt, Object paramObject)
  {
    Notification.MediaStyle localMediaStyle = new Notification.MediaStyle(paramNotificationBuilderWithBuilderAccessor.getBuilder());
    if (paramArrayOfInt != null) {
      localMediaStyle.setShowActionsInCompactView(paramArrayOfInt);
    }
    if (paramObject != null) {
      localMediaStyle.setMediaSession((MediaSession.Token)paramObject);
    }
  }
}
