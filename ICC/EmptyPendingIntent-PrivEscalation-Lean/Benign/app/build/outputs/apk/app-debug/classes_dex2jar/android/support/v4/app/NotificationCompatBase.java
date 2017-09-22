/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.app.Notification
 *  android.app.PendingIntent
 *  android.content.Context
 *  android.os.Bundle
 */
package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.app.RemoteInputCompatBase;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@TargetApi(value=9)
@RequiresApi(value=9)
@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class NotificationCompatBase {
    private static Method sSetLatestEventInfo;

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static Notification add(Notification notification, Context context, CharSequence charSequence, CharSequence charSequence2, PendingIntent pendingIntent, PendingIntent pendingIntent2) {
        void var6_8;
        if (sSetLatestEventInfo == null) {
            sSetLatestEventInfo = Notification.class.getMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
        }
        sSetLatestEventInfo.invoke((Object)notification, new Object[]{context, charSequence, charSequence2, pendingIntent});
        notification.fullScreenIntent = pendingIntent2;
        return notification;
        catch (NoSuchMethodException noSuchMethodException) {
            throw new RuntimeException(noSuchMethodException);
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new RuntimeException((Throwable)var6_8);
        }
        catch (InvocationTargetException invocationTargetException) {
            throw new RuntimeException((Throwable)var6_8);
        }
    }

    public static abstract class Action {
        public abstract PendingIntent getActionIntent();

        public abstract boolean getAllowGeneratedReplies();

        public abstract Bundle getExtras();

        public abstract int getIcon();

        public abstract RemoteInputCompatBase.RemoteInput[] getRemoteInputs();

        public abstract CharSequence getTitle();

        public static interface Factory {
            public Action build(int var1, CharSequence var2, PendingIntent var3, Bundle var4, RemoteInputCompatBase.RemoteInput[] var5, boolean var6);

            public Action[] newArray(int var1);
        }

    }

    public static abstract class UnreadConversation {
        abstract long getLatestTimestamp();

        abstract String[] getMessages();

        abstract String getParticipant();

        abstract String[] getParticipants();

        abstract PendingIntent getReadPendingIntent();

        abstract RemoteInputCompatBase.RemoteInput getRemoteInput();

        abstract PendingIntent getReplyPendingIntent();

        public static interface Factory {
            public UnreadConversation build(String[] var1, RemoteInputCompatBase.RemoteInput var2, PendingIntent var3, PendingIntent var4, String[] var5, long var6);
        }

    }

}

