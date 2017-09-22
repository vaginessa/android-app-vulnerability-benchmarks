package android.support.v7.app;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.app.BundleCompat;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompat.BuilderExtender;
import android.support.v4.app.NotificationCompat.MessagingStyle;
import android.support.v4.app.NotificationCompat.MessagingStyle.Message;
import android.support.v4.app.NotificationCompat.Style;
import android.support.v4.media.session.MediaSessionCompat.Token;
import android.support.v4.text.BidiFormatter;
import android.support.v7.appcompat.R.color;
import android.support.v7.appcompat.R.id;
import android.support.v7.appcompat.R.layout;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.widget.RemoteViews;
import java.util.List;

public class NotificationCompat
  extends android.support.v4.app.NotificationCompat
{
  public NotificationCompat() {}
  
  @TargetApi(16)
  @RequiresApi(16)
  private static void addBigStyleToBuilderJellybean(Notification paramNotification, android.support.v4.app.NotificationCompat.Builder paramBuilder)
  {
    if ((paramBuilder.mStyle instanceof MediaStyle))
    {
      localMediaStyle = (MediaStyle)paramBuilder.mStyle;
      if (paramBuilder.getBigContentView() != null)
      {
        localRemoteViews = paramBuilder.getBigContentView();
        if ((!(paramBuilder.mStyle instanceof DecoratedMediaCustomViewStyle)) || (localRemoteViews == null)) {
          break label132;
        }
        bool = true;
        NotificationCompatImplBase.overrideMediaBigContentView(paramNotification, paramBuilder.mContext, paramBuilder.mContentTitle, paramBuilder.mContentText, paramBuilder.mContentInfo, paramBuilder.mNumber, paramBuilder.mLargeIcon, paramBuilder.mSubText, paramBuilder.mUseChronometer, paramBuilder.getWhenIfShowing(), paramBuilder.getPriority(), 0, paramBuilder.mActions, localMediaStyle.mShowCancelButton, localMediaStyle.mCancelButtonIntent, bool);
        if (bool) {
          NotificationCompatImplBase.buildIntoRemoteViews(paramBuilder.mContext, paramNotification.bigContentView, localRemoteViews);
        }
      }
    }
    label132:
    while (!(paramBuilder.mStyle instanceof DecoratedCustomViewStyle)) {
      for (;;)
      {
        MediaStyle localMediaStyle;
        return;
        RemoteViews localRemoteViews = paramBuilder.getContentView();
        continue;
        boolean bool = false;
      }
    }
    addDecoratedBigStyleToBuilderJellybean(paramNotification, paramBuilder);
  }
  
  @TargetApi(21)
  @RequiresApi(21)
  private static void addBigStyleToBuilderLollipop(Notification paramNotification, android.support.v4.app.NotificationCompat.Builder paramBuilder)
  {
    RemoteViews localRemoteViews;
    if (paramBuilder.getBigContentView() != null)
    {
      localRemoteViews = paramBuilder.getBigContentView();
      if ((!(paramBuilder.mStyle instanceof DecoratedMediaCustomViewStyle)) || (localRemoteViews == null)) {
        break label114;
      }
      NotificationCompatImplBase.overrideMediaBigContentView(paramNotification, paramBuilder.mContext, paramBuilder.mContentTitle, paramBuilder.mContentText, paramBuilder.mContentInfo, paramBuilder.mNumber, paramBuilder.mLargeIcon, paramBuilder.mSubText, paramBuilder.mUseChronometer, paramBuilder.getWhenIfShowing(), paramBuilder.getPriority(), 0, paramBuilder.mActions, false, null, true);
      NotificationCompatImplBase.buildIntoRemoteViews(paramBuilder.mContext, paramNotification.bigContentView, localRemoteViews);
      setBackgroundColor(paramBuilder.mContext, paramNotification.bigContentView, paramBuilder.getColor());
    }
    label114:
    while (!(paramBuilder.mStyle instanceof DecoratedCustomViewStyle))
    {
      return;
      localRemoteViews = paramBuilder.getContentView();
      break;
    }
    addDecoratedBigStyleToBuilderJellybean(paramNotification, paramBuilder);
  }
  
  @TargetApi(16)
  @RequiresApi(16)
  private static void addDecoratedBigStyleToBuilderJellybean(Notification paramNotification, android.support.v4.app.NotificationCompat.Builder paramBuilder)
  {
    RemoteViews localRemoteViews1 = paramBuilder.getBigContentView();
    if (localRemoteViews1 != null) {}
    for (RemoteViews localRemoteViews2 = localRemoteViews1; localRemoteViews2 == null; localRemoteViews2 = paramBuilder.getContentView()) {
      return;
    }
    RemoteViews localRemoteViews3 = NotificationCompatImplBase.applyStandardTemplateWithActions(paramBuilder.mContext, paramBuilder.mContentTitle, paramBuilder.mContentText, paramBuilder.mContentInfo, paramBuilder.mNumber, paramNotification.icon, paramBuilder.mLargeIcon, paramBuilder.mSubText, paramBuilder.mUseChronometer, paramBuilder.getWhenIfShowing(), paramBuilder.getPriority(), paramBuilder.getColor(), R.layout.notification_template_custom_big, false, paramBuilder.mActions);
    NotificationCompatImplBase.buildIntoRemoteViews(paramBuilder.mContext, localRemoteViews3, localRemoteViews2);
    paramNotification.bigContentView = localRemoteViews3;
  }
  
  @TargetApi(21)
  @RequiresApi(21)
  private static void addDecoratedHeadsUpToBuilderLollipop(Notification paramNotification, android.support.v4.app.NotificationCompat.Builder paramBuilder)
  {
    RemoteViews localRemoteViews1 = paramBuilder.getHeadsUpContentView();
    if (localRemoteViews1 != null) {}
    for (RemoteViews localRemoteViews2 = localRemoteViews1; localRemoteViews1 == null; localRemoteViews2 = paramBuilder.getContentView()) {
      return;
    }
    RemoteViews localRemoteViews3 = NotificationCompatImplBase.applyStandardTemplateWithActions(paramBuilder.mContext, paramBuilder.mContentTitle, paramBuilder.mContentText, paramBuilder.mContentInfo, paramBuilder.mNumber, paramNotification.icon, paramBuilder.mLargeIcon, paramBuilder.mSubText, paramBuilder.mUseChronometer, paramBuilder.getWhenIfShowing(), paramBuilder.getPriority(), paramBuilder.getColor(), R.layout.notification_template_custom_big, false, paramBuilder.mActions);
    NotificationCompatImplBase.buildIntoRemoteViews(paramBuilder.mContext, localRemoteViews3, localRemoteViews2);
    paramNotification.headsUpContentView = localRemoteViews3;
  }
  
  @TargetApi(21)
  @RequiresApi(21)
  private static void addHeadsUpToBuilderLollipop(Notification paramNotification, android.support.v4.app.NotificationCompat.Builder paramBuilder)
  {
    RemoteViews localRemoteViews;
    if (paramBuilder.getHeadsUpContentView() != null)
    {
      localRemoteViews = paramBuilder.getHeadsUpContentView();
      if ((!(paramBuilder.mStyle instanceof DecoratedMediaCustomViewStyle)) || (localRemoteViews == null)) {
        break label117;
      }
      paramNotification.headsUpContentView = NotificationCompatImplBase.generateMediaBigView(paramBuilder.mContext, paramBuilder.mContentTitle, paramBuilder.mContentText, paramBuilder.mContentInfo, paramBuilder.mNumber, paramBuilder.mLargeIcon, paramBuilder.mSubText, paramBuilder.mUseChronometer, paramBuilder.getWhenIfShowing(), paramBuilder.getPriority(), 0, paramBuilder.mActions, false, null, true);
      NotificationCompatImplBase.buildIntoRemoteViews(paramBuilder.mContext, paramNotification.headsUpContentView, localRemoteViews);
      setBackgroundColor(paramBuilder.mContext, paramNotification.headsUpContentView, paramBuilder.getColor());
    }
    label117:
    while (!(paramBuilder.mStyle instanceof DecoratedCustomViewStyle))
    {
      return;
      localRemoteViews = paramBuilder.getContentView();
      break;
    }
    addDecoratedHeadsUpToBuilderLollipop(paramNotification, paramBuilder);
  }
  
  private static void addMessagingFallBackStyle(NotificationCompat.MessagingStyle paramMessagingStyle, NotificationBuilderWithBuilderAccessor paramNotificationBuilderWithBuilderAccessor, android.support.v4.app.NotificationCompat.Builder paramBuilder)
  {
    SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder();
    List localList = paramMessagingStyle.getMessages();
    int i;
    int j;
    label45:
    NotificationCompat.MessagingStyle.Message localMessage;
    if ((paramMessagingStyle.getConversationTitle() != null) || (hasMessagesWithoutSender(paramMessagingStyle.getMessages())))
    {
      i = 1;
      j = -1 + localList.size();
      if (j < 0) {
        break label130;
      }
      localMessage = (NotificationCompat.MessagingStyle.Message)localList.get(j);
      if (i == 0) {
        break label120;
      }
    }
    label120:
    for (CharSequence localCharSequence = makeMessageLine(paramBuilder, paramMessagingStyle, localMessage);; localCharSequence = localMessage.getText())
    {
      if (j != -1 + localList.size()) {
        localSpannableStringBuilder.insert(0, "\n");
      }
      localSpannableStringBuilder.insert(0, localCharSequence);
      j--;
      break label45;
      i = 0;
      break;
    }
    label130:
    NotificationCompatImplJellybean.addBigTextStyle(paramNotificationBuilderWithBuilderAccessor, localSpannableStringBuilder);
  }
  
  @TargetApi(14)
  @RequiresApi(14)
  private static RemoteViews addStyleGetContentViewIcs(NotificationBuilderWithBuilderAccessor paramNotificationBuilderWithBuilderAccessor, android.support.v4.app.NotificationCompat.Builder paramBuilder)
  {
    if ((paramBuilder.mStyle instanceof MediaStyle))
    {
      MediaStyle localMediaStyle = (MediaStyle)paramBuilder.mStyle;
      if (((paramBuilder.mStyle instanceof DecoratedMediaCustomViewStyle)) && (paramBuilder.getContentView() != null)) {}
      for (boolean bool = true;; bool = false)
      {
        RemoteViews localRemoteViews = NotificationCompatImplBase.overrideContentViewMedia(paramNotificationBuilderWithBuilderAccessor, paramBuilder.mContext, paramBuilder.mContentTitle, paramBuilder.mContentText, paramBuilder.mContentInfo, paramBuilder.mNumber, paramBuilder.mLargeIcon, paramBuilder.mSubText, paramBuilder.mUseChronometer, paramBuilder.getWhenIfShowing(), paramBuilder.getPriority(), paramBuilder.mActions, localMediaStyle.mActionsToShowInCompact, localMediaStyle.mShowCancelButton, localMediaStyle.mCancelButtonIntent, bool);
        if (!bool) {
          break;
        }
        NotificationCompatImplBase.buildIntoRemoteViews(paramBuilder.mContext, localRemoteViews, paramBuilder.getContentView());
        return localRemoteViews;
      }
    }
    if ((paramBuilder.mStyle instanceof DecoratedCustomViewStyle)) {
      return getDecoratedContentView(paramBuilder);
    }
    return null;
  }
  
  @TargetApi(16)
  @RequiresApi(16)
  private static RemoteViews addStyleGetContentViewJellybean(NotificationBuilderWithBuilderAccessor paramNotificationBuilderWithBuilderAccessor, android.support.v4.app.NotificationCompat.Builder paramBuilder)
  {
    if ((paramBuilder.mStyle instanceof NotificationCompat.MessagingStyle)) {
      addMessagingFallBackStyle((NotificationCompat.MessagingStyle)paramBuilder.mStyle, paramNotificationBuilderWithBuilderAccessor, paramBuilder);
    }
    return addStyleGetContentViewIcs(paramNotificationBuilderWithBuilderAccessor, paramBuilder);
  }
  
  @TargetApi(21)
  @RequiresApi(21)
  private static RemoteViews addStyleGetContentViewLollipop(NotificationBuilderWithBuilderAccessor paramNotificationBuilderWithBuilderAccessor, android.support.v4.app.NotificationCompat.Builder paramBuilder)
  {
    if ((paramBuilder.mStyle instanceof MediaStyle))
    {
      MediaStyle localMediaStyle = (MediaStyle)paramBuilder.mStyle;
      int[] arrayOfInt = localMediaStyle.mActionsToShowInCompact;
      Object localObject;
      boolean bool;
      label56:
      int i;
      if (localMediaStyle.mToken != null)
      {
        localObject = localMediaStyle.mToken.getToken();
        NotificationCompatImpl21.addMediaStyle(paramNotificationBuilderWithBuilderAccessor, arrayOfInt, localObject);
        if (paramBuilder.getContentView() == null) {
          break label208;
        }
        bool = true;
        if ((Build.VERSION.SDK_INT < 21) || (Build.VERSION.SDK_INT > 23)) {
          break label214;
        }
        i = 1;
        label75:
        if ((!bool) && ((i == 0) || (paramBuilder.getBigContentView() == null))) {
          break label220;
        }
      }
      label208:
      label214:
      label220:
      for (int j = 1;; j = 0)
      {
        if ((!(paramBuilder.mStyle instanceof DecoratedMediaCustomViewStyle)) || (j == 0)) {
          break label226;
        }
        RemoteViews localRemoteViews = NotificationCompatImplBase.overrideContentViewMedia(paramNotificationBuilderWithBuilderAccessor, paramBuilder.mContext, paramBuilder.mContentTitle, paramBuilder.mContentText, paramBuilder.mContentInfo, paramBuilder.mNumber, paramBuilder.mLargeIcon, paramBuilder.mSubText, paramBuilder.mUseChronometer, paramBuilder.getWhenIfShowing(), paramBuilder.getPriority(), paramBuilder.mActions, localMediaStyle.mActionsToShowInCompact, false, null, bool);
        if (bool) {
          NotificationCompatImplBase.buildIntoRemoteViews(paramBuilder.mContext, localRemoteViews, paramBuilder.getContentView());
        }
        setBackgroundColor(paramBuilder.mContext, localRemoteViews, paramBuilder.getColor());
        return localRemoteViews;
        localObject = null;
        break;
        bool = false;
        break label56;
        i = 0;
        break label75;
      }
      label226:
      return null;
    }
    if ((paramBuilder.mStyle instanceof DecoratedCustomViewStyle)) {
      return getDecoratedContentView(paramBuilder);
    }
    return addStyleGetContentViewJellybean(paramNotificationBuilderWithBuilderAccessor, paramBuilder);
  }
  
  @TargetApi(24)
  @RequiresApi(24)
  private static void addStyleToBuilderApi24(NotificationBuilderWithBuilderAccessor paramNotificationBuilderWithBuilderAccessor, android.support.v4.app.NotificationCompat.Builder paramBuilder)
  {
    if ((paramBuilder.mStyle instanceof DecoratedCustomViewStyle)) {
      NotificationCompatImpl24.addDecoratedCustomViewStyle(paramNotificationBuilderWithBuilderAccessor);
    }
    do
    {
      return;
      if ((paramBuilder.mStyle instanceof DecoratedMediaCustomViewStyle))
      {
        NotificationCompatImpl24.addDecoratedMediaCustomViewStyle(paramNotificationBuilderWithBuilderAccessor);
        return;
      }
    } while ((paramBuilder.mStyle instanceof NotificationCompat.MessagingStyle));
    addStyleGetContentViewLollipop(paramNotificationBuilderWithBuilderAccessor, paramBuilder);
  }
  
  private static NotificationCompat.MessagingStyle.Message findLatestIncomingMessage(NotificationCompat.MessagingStyle paramMessagingStyle)
  {
    List localList = paramMessagingStyle.getMessages();
    for (int i = -1 + localList.size(); i >= 0; i--)
    {
      NotificationCompat.MessagingStyle.Message localMessage = (NotificationCompat.MessagingStyle.Message)localList.get(i);
      if (!TextUtils.isEmpty(localMessage.getSender())) {
        return localMessage;
      }
    }
    if (!localList.isEmpty()) {
      return (NotificationCompat.MessagingStyle.Message)localList.get(-1 + localList.size());
    }
    return null;
  }
  
  private static RemoteViews getDecoratedContentView(android.support.v4.app.NotificationCompat.Builder paramBuilder)
  {
    if (paramBuilder.getContentView() == null) {
      return null;
    }
    RemoteViews localRemoteViews = NotificationCompatImplBase.applyStandardTemplateWithActions(paramBuilder.mContext, paramBuilder.mContentTitle, paramBuilder.mContentText, paramBuilder.mContentInfo, paramBuilder.mNumber, paramBuilder.mNotification.icon, paramBuilder.mLargeIcon, paramBuilder.mSubText, paramBuilder.mUseChronometer, paramBuilder.getWhenIfShowing(), paramBuilder.getPriority(), paramBuilder.getColor(), R.layout.notification_template_custom_big, false, null);
    NotificationCompatImplBase.buildIntoRemoteViews(paramBuilder.mContext, localRemoteViews, paramBuilder.getContentView());
    return localRemoteViews;
  }
  
  public static MediaSessionCompat.Token getMediaSession(Notification paramNotification)
  {
    Bundle localBundle = getExtras(paramNotification);
    if (localBundle != null) {
      if (Build.VERSION.SDK_INT >= 21)
      {
        Parcelable localParcelable = localBundle.getParcelable("android.mediaSession");
        if (localParcelable != null) {
          return MediaSessionCompat.Token.fromToken(localParcelable);
        }
      }
      else
      {
        IBinder localIBinder = BundleCompat.getBinder(localBundle, "android.mediaSession");
        if (localIBinder != null)
        {
          Parcel localParcel = Parcel.obtain();
          localParcel.writeStrongBinder(localIBinder);
          localParcel.setDataPosition(0);
          MediaSessionCompat.Token localToken = (MediaSessionCompat.Token)MediaSessionCompat.Token.CREATOR.createFromParcel(localParcel);
          localParcel.recycle();
          return localToken;
        }
      }
    }
    return null;
  }
  
  private static boolean hasMessagesWithoutSender(List<NotificationCompat.MessagingStyle.Message> paramList)
  {
    for (int i = -1 + paramList.size(); i >= 0; i--) {
      if (((NotificationCompat.MessagingStyle.Message)paramList.get(i)).getSender() == null) {
        return true;
      }
    }
    return false;
  }
  
  private static TextAppearanceSpan makeFontColorSpan(int paramInt)
  {
    return new TextAppearanceSpan(null, 0, 0, ColorStateList.valueOf(paramInt), null);
  }
  
  private static CharSequence makeMessageLine(android.support.v4.app.NotificationCompat.Builder paramBuilder, NotificationCompat.MessagingStyle paramMessagingStyle, NotificationCompat.MessagingStyle.Message paramMessage)
  {
    BidiFormatter localBidiFormatter = BidiFormatter.getInstance();
    SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder();
    int i;
    int j;
    label42:
    Object localObject1;
    if (Build.VERSION.SDK_INT >= 21)
    {
      i = 1;
      if ((i == 0) && (Build.VERSION.SDK_INT > 10)) {
        break label173;
      }
      j = -16777216;
      localObject1 = paramMessage.getSender();
      if (TextUtils.isEmpty(paramMessage.getSender()))
      {
        if (paramMessagingStyle.getUserDisplayName() != null) {
          break label179;
        }
        localObject1 = "";
        label70:
        if ((i != 0) && (paramBuilder.getColor() != 0)) {
          j = paramBuilder.getColor();
        }
      }
      CharSequence localCharSequence = localBidiFormatter.unicodeWrap((CharSequence)localObject1);
      localSpannableStringBuilder.append(localCharSequence);
      localSpannableStringBuilder.setSpan(makeFontColorSpan(j), localSpannableStringBuilder.length() - localCharSequence.length(), localSpannableStringBuilder.length(), 33);
      if (paramMessage.getText() != null) {
        break label188;
      }
    }
    label173:
    label179:
    label188:
    for (Object localObject2 = "";; localObject2 = paramMessage.getText())
    {
      localSpannableStringBuilder.append("  ").append(localBidiFormatter.unicodeWrap((CharSequence)localObject2));
      return localSpannableStringBuilder;
      i = 0;
      break;
      j = -1;
      break label42;
      localObject1 = paramMessagingStyle.getUserDisplayName();
      break label70;
    }
  }
  
  private static void setBackgroundColor(Context paramContext, RemoteViews paramRemoteViews, int paramInt)
  {
    if (paramInt == 0) {
      paramInt = paramContext.getResources().getColor(R.color.notification_material_background_media_default_color);
    }
    paramRemoteViews.setInt(R.id.status_bar_latest_event_content, "setBackgroundColor", paramInt);
  }
  
  private static class Api24Extender
    extends NotificationCompat.BuilderExtender
  {
    private Api24Extender() {}
    
    public Notification build(android.support.v4.app.NotificationCompat.Builder paramBuilder, NotificationBuilderWithBuilderAccessor paramNotificationBuilderWithBuilderAccessor)
    {
      NotificationCompat.addStyleToBuilderApi24(paramNotificationBuilderWithBuilderAccessor, paramBuilder);
      return paramNotificationBuilderWithBuilderAccessor.build();
    }
  }
  
  public static class Builder
    extends android.support.v4.app.NotificationCompat.Builder
  {
    public Builder(Context paramContext)
    {
      super();
    }
    
    @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
    protected NotificationCompat.BuilderExtender getExtender()
    {
      if (Build.VERSION.SDK_INT >= 24) {
        return new NotificationCompat.Api24Extender(null);
      }
      if (Build.VERSION.SDK_INT >= 21) {
        return new NotificationCompat.LollipopExtender();
      }
      if (Build.VERSION.SDK_INT >= 16) {
        return new NotificationCompat.JellybeanExtender();
      }
      if (Build.VERSION.SDK_INT >= 14) {
        return new NotificationCompat.IceCreamSandwichExtender();
      }
      return super.getExtender();
    }
    
    @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
    protected CharSequence resolveText()
    {
      if ((this.mStyle instanceof NotificationCompat.MessagingStyle))
      {
        NotificationCompat.MessagingStyle localMessagingStyle = (NotificationCompat.MessagingStyle)this.mStyle;
        NotificationCompat.MessagingStyle.Message localMessage = NotificationCompat.findLatestIncomingMessage(localMessagingStyle);
        CharSequence localCharSequence = localMessagingStyle.getConversationTitle();
        if (localMessage != null)
        {
          if (localCharSequence != null) {
            return NotificationCompat.makeMessageLine(this, localMessagingStyle, localMessage);
          }
          return localMessage.getText();
        }
      }
      return super.resolveText();
    }
    
    @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
    protected CharSequence resolveTitle()
    {
      if ((this.mStyle instanceof NotificationCompat.MessagingStyle))
      {
        NotificationCompat.MessagingStyle localMessagingStyle = (NotificationCompat.MessagingStyle)this.mStyle;
        NotificationCompat.MessagingStyle.Message localMessage = NotificationCompat.findLatestIncomingMessage(localMessagingStyle);
        CharSequence localCharSequence = localMessagingStyle.getConversationTitle();
        if ((localCharSequence != null) || (localMessage != null))
        {
          if (localCharSequence != null) {
            return localCharSequence;
          }
          return localMessage.getSender();
        }
      }
      return super.resolveTitle();
    }
  }
  
  public static class DecoratedCustomViewStyle
    extends NotificationCompat.Style
  {
    public DecoratedCustomViewStyle() {}
  }
  
  public static class DecoratedMediaCustomViewStyle
    extends NotificationCompat.MediaStyle
  {
    public DecoratedMediaCustomViewStyle() {}
  }
  
  private static class IceCreamSandwichExtender
    extends NotificationCompat.BuilderExtender
  {
    IceCreamSandwichExtender() {}
    
    public Notification build(android.support.v4.app.NotificationCompat.Builder paramBuilder, NotificationBuilderWithBuilderAccessor paramNotificationBuilderWithBuilderAccessor)
    {
      RemoteViews localRemoteViews = NotificationCompat.addStyleGetContentViewIcs(paramNotificationBuilderWithBuilderAccessor, paramBuilder);
      Notification localNotification = paramNotificationBuilderWithBuilderAccessor.build();
      if (localRemoteViews != null) {
        localNotification.contentView = localRemoteViews;
      }
      while (paramBuilder.getContentView() == null) {
        return localNotification;
      }
      localNotification.contentView = paramBuilder.getContentView();
      return localNotification;
    }
  }
  
  private static class JellybeanExtender
    extends NotificationCompat.BuilderExtender
  {
    JellybeanExtender() {}
    
    public Notification build(android.support.v4.app.NotificationCompat.Builder paramBuilder, NotificationBuilderWithBuilderAccessor paramNotificationBuilderWithBuilderAccessor)
    {
      RemoteViews localRemoteViews = NotificationCompat.addStyleGetContentViewJellybean(paramNotificationBuilderWithBuilderAccessor, paramBuilder);
      Notification localNotification = paramNotificationBuilderWithBuilderAccessor.build();
      if (localRemoteViews != null) {
        localNotification.contentView = localRemoteViews;
      }
      NotificationCompat.addBigStyleToBuilderJellybean(localNotification, paramBuilder);
      return localNotification;
    }
  }
  
  private static class LollipopExtender
    extends NotificationCompat.BuilderExtender
  {
    LollipopExtender() {}
    
    public Notification build(android.support.v4.app.NotificationCompat.Builder paramBuilder, NotificationBuilderWithBuilderAccessor paramNotificationBuilderWithBuilderAccessor)
    {
      RemoteViews localRemoteViews = NotificationCompat.addStyleGetContentViewLollipop(paramNotificationBuilderWithBuilderAccessor, paramBuilder);
      Notification localNotification = paramNotificationBuilderWithBuilderAccessor.build();
      if (localRemoteViews != null) {
        localNotification.contentView = localRemoteViews;
      }
      NotificationCompat.addBigStyleToBuilderLollipop(localNotification, paramBuilder);
      NotificationCompat.addHeadsUpToBuilderLollipop(localNotification, paramBuilder);
      return localNotification;
    }
  }
  
  public static class MediaStyle
    extends NotificationCompat.Style
  {
    int[] mActionsToShowInCompact = null;
    PendingIntent mCancelButtonIntent;
    boolean mShowCancelButton;
    MediaSessionCompat.Token mToken;
    
    public MediaStyle() {}
    
    public MediaStyle(android.support.v4.app.NotificationCompat.Builder paramBuilder)
    {
      setBuilder(paramBuilder);
    }
    
    public MediaStyle setCancelButtonIntent(PendingIntent paramPendingIntent)
    {
      this.mCancelButtonIntent = paramPendingIntent;
      return this;
    }
    
    public MediaStyle setMediaSession(MediaSessionCompat.Token paramToken)
    {
      this.mToken = paramToken;
      return this;
    }
    
    public MediaStyle setShowActionsInCompactView(int... paramVarArgs)
    {
      this.mActionsToShowInCompact = paramVarArgs;
      return this;
    }
    
    public MediaStyle setShowCancelButton(boolean paramBoolean)
    {
      this.mShowCancelButton = paramBoolean;
      return this;
    }
  }
}
