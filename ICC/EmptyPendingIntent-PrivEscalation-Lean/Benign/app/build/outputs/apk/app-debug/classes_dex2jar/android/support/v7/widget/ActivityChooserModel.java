/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.ActivityInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.ResolveInfo
 *  android.database.DataSetObservable
 *  android.os.AsyncTask
 *  android.text.TextUtils
 *  android.util.Log
 *  android.util.Xml
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 *  org.xmlpull.v1.XmlSerializer
 */
package android.support.v7.widget;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.DataSetObservable;
import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

class ActivityChooserModel
extends DataSetObservable {
    static final String ATTRIBUTE_ACTIVITY = "activity";
    static final String ATTRIBUTE_TIME = "time";
    static final String ATTRIBUTE_WEIGHT = "weight";
    static final boolean DEBUG = false;
    private static final int DEFAULT_ACTIVITY_INFLATION = 5;
    private static final float DEFAULT_HISTORICAL_RECORD_WEIGHT = 1.0f;
    public static final String DEFAULT_HISTORY_FILE_NAME = "activity_choser_model_history.xml";
    public static final int DEFAULT_HISTORY_MAX_LENGTH = 50;
    private static final String HISTORY_FILE_EXTENSION = ".xml";
    private static final int INVALID_INDEX = -1;
    static final String LOG_TAG = ActivityChooserModel.class.getSimpleName();
    static final String TAG_HISTORICAL_RECORD = "historical-record";
    static final String TAG_HISTORICAL_RECORDS = "historical-records";
    private static final Map<String, ActivityChooserModel> sDataModelRegistry;
    private static final Object sRegistryLock;
    private final List<ActivityResolveInfo> mActivities = new ArrayList<ActivityResolveInfo>();
    private OnChooseActivityListener mActivityChoserModelPolicy;
    private ActivitySorter mActivitySorter;
    boolean mCanReadHistoricalData;
    final Context mContext;
    private final List<HistoricalRecord> mHistoricalRecords = new ArrayList<HistoricalRecord>();
    private boolean mHistoricalRecordsChanged;
    final String mHistoryFileName;
    private int mHistoryMaxSize;
    private final Object mInstanceLock = new Object();
    private Intent mIntent;
    private boolean mReadShareHistoryCalled;
    private boolean mReloadActivities;

    static {
        sRegistryLock = new Object();
        sDataModelRegistry = new HashMap<String, ActivityChooserModel>();
    }

    private ActivityChooserModel(Context context, String string2) {
        this.mActivitySorter = new DefaultSorter();
        this.mHistoryMaxSize = 50;
        this.mCanReadHistoricalData = true;
        this.mReadShareHistoryCalled = false;
        this.mHistoricalRecordsChanged = true;
        this.mReloadActivities = false;
        this.mContext = context.getApplicationContext();
        if (!TextUtils.isEmpty((CharSequence)string2) && !string2.endsWith(".xml")) {
            this.mHistoryFileName = string2 + ".xml";
            return;
        }
        this.mHistoryFileName = string2;
    }

    private boolean addHistoricalRecord(HistoricalRecord historicalRecord) {
        boolean bl = this.mHistoricalRecords.add(historicalRecord);
        if (bl) {
            this.mHistoricalRecordsChanged = true;
            this.pruneExcessiveHistoricalRecordsIfNeeded();
            this.persistHistoricalDataIfNeeded();
            this.sortActivitiesIfNeeded();
            this.notifyChanged();
        }
        return bl;
    }

    private void ensureConsistentState() {
        boolean bl = this.loadActivitiesIfNeeded() | this.readHistoricalDataIfNeeded();
        this.pruneExcessiveHistoricalRecordsIfNeeded();
        if (bl) {
            this.sortActivitiesIfNeeded();
            this.notifyChanged();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static ActivityChooserModel get(Context context, String string2) {
        Object object = sRegistryLock;
        synchronized (object) {
            ActivityChooserModel activityChooserModel = sDataModelRegistry.get(string2);
            if (activityChooserModel == null) {
                activityChooserModel = new ActivityChooserModel(context, string2);
                sDataModelRegistry.put(string2, activityChooserModel);
            }
            return activityChooserModel;
        }
    }

    private boolean loadActivitiesIfNeeded() {
        boolean bl = this.mReloadActivities;
        boolean bl2 = false;
        if (bl) {
            Intent intent = this.mIntent;
            bl2 = false;
            if (intent != null) {
                this.mReloadActivities = false;
                this.mActivities.clear();
                List list = this.mContext.getPackageManager().queryIntentActivities(this.mIntent, 0);
                int n = list.size();
                for (int i = 0; i < n; ++i) {
                    ResolveInfo resolveInfo = (ResolveInfo)list.get(i);
                    this.mActivities.add(new ActivityResolveInfo(resolveInfo));
                }
                bl2 = true;
            }
        }
        return bl2;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void persistHistoricalDataIfNeeded() {
        if (!this.mReadShareHistoryCalled) {
            throw new IllegalStateException("No preceding call to #readHistoricalData");
        }
        if (!this.mHistoricalRecordsChanged) {
            return;
        }
        this.mHistoricalRecordsChanged = false;
        if (TextUtils.isEmpty((CharSequence)this.mHistoryFileName)) return;
        PersistHistoryAsyncTask persistHistoryAsyncTask = new PersistHistoryAsyncTask();
        Object[] arrobject = new Object[]{new ArrayList<HistoricalRecord>(this.mHistoricalRecords), this.mHistoryFileName};
        AsyncTaskCompat.executeParallel(persistHistoryAsyncTask, arrobject);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void pruneExcessiveHistoricalRecordsIfNeeded() {
        int n = this.mHistoricalRecords.size() - this.mHistoryMaxSize;
        if (n <= 0) {
            return;
        }
        this.mHistoricalRecordsChanged = true;
        int n2 = 0;
        while (n2 < n) {
            this.mHistoricalRecords.remove(0);
            ++n2;
        }
    }

    private boolean readHistoricalDataIfNeeded() {
        if (this.mCanReadHistoricalData && this.mHistoricalRecordsChanged && !TextUtils.isEmpty((CharSequence)this.mHistoryFileName)) {
            this.mCanReadHistoricalData = false;
            this.mReadShareHistoryCalled = true;
            this.readHistoricalDataImpl();
            return true;
        }
        return false;
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private void readHistoricalDataImpl() {
        var2_1 = this.mContext.openFileInput(this.mHistoryFileName);
        try {
            var11_2 = Xml.newPullParser();
            var11_2.setInput((InputStream)var2_1, "UTF-8");
            var12_3 = 0;
            while (var12_3 != 1 && var12_3 != 2) {
                var12_3 = var11_2.next();
            }
            if (!"historical-records".equals(var11_2.getName())) {
                throw new XmlPullParserException("Share records file does not start with historical-records tag.");
            }
            var13_6 = this.mHistoricalRecords;
            var13_6.clear();
            ** GOTO lbl27
            catch (FileNotFoundException var1_13) {
                // empty catch block
            }
            return;
            {
                catch (XmlPullParserException var8_4) {
                    Log.e((String)ActivityChooserModel.LOG_TAG, (String)("Error reading historical recrod file: " + this.mHistoryFileName), (Throwable)var8_4);
                    if (var2_1 == null) return;
                    try {
                        var2_1.close();
                        return;
                    }
                    catch (IOException var10_5) {
                        return;
                    }
                }
lbl27: // 1 sources:
                do {
                    if ((var14_7 = var11_2.next()) == 1) {
                        if (var2_1 == null) return;
                        try {
                            var2_1.close();
                            return;
                        }
                        catch (IOException var16_8) {
                            return;
                        }
                    }
                    if (var14_7 == 3 || var14_7 == 4) continue;
                    ** try [egrp 6[TRYBLOCK] [21 : 179->239)] { 
lbl38: // 2 sources:
                    if ("historical-record".equals(var11_2.getName())) ** break block23
                    throw new XmlPullParserException("Share records file not well-formed.");
                    break;
                } while (true);
                catch (IOException var5_9) {
                    Log.e((String)ActivityChooserModel.LOG_TAG, (String)("Error reading historical recrod file: " + this.mHistoryFileName), (Throwable)var5_9);
                    if (var2_1 == null) return;
                    try {
                        var2_1.close();
                        return;
                    }
                    catch (IOException var7_10) {
                        return;
                    }
                }
                {
                    
                    var13_6.add(new HistoricalRecord(var11_2.getAttributeValue(null, "activity"), Long.parseLong(var11_2.getAttributeValue(null, "time")), Float.parseFloat(var11_2.getAttributeValue(null, "weight"))));
                    continue;
                }
            }
        }
lbl52: // 3 sources:
        catch (Throwable var3_11) {
            if (var2_1 == null) throw var3_11;
            try {
                var2_1.close();
            }
            catch (IOException var4_12) {
                throw var3_11;
            }
            throw var3_11;
        }
    }

    private boolean sortActivitiesIfNeeded() {
        if (this.mActivitySorter != null && this.mIntent != null && !this.mActivities.isEmpty() && !this.mHistoricalRecords.isEmpty()) {
            this.mActivitySorter.sort(this.mIntent, this.mActivities, Collections.unmodifiableList(this.mHistoricalRecords));
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Intent chooseActivity(int n) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            Intent intent;
            if (this.mIntent == null) {
                return null;
            }
            this.ensureConsistentState();
            ActivityResolveInfo activityResolveInfo = this.mActivities.get(n);
            ComponentName componentName = new ComponentName(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name);
            Intent intent2 = new Intent(this.mIntent);
            intent2.setComponent(componentName);
            if (this.mActivityChoserModelPolicy != null && this.mActivityChoserModelPolicy.onChooseActivity(this, intent = new Intent(intent2))) {
                return null;
            }
            this.addHistoricalRecord(new HistoricalRecord(componentName, System.currentTimeMillis(), 1.0f));
            return intent2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public ResolveInfo getActivity(int n) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            this.ensureConsistentState();
            return this.mActivities.get((int)n).resolveInfo;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int getActivityCount() {
        Object object = this.mInstanceLock;
        synchronized (object) {
            this.ensureConsistentState();
            return this.mActivities.size();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int getActivityIndex(ResolveInfo resolveInfo) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            this.ensureConsistentState();
            List<ActivityResolveInfo> list = this.mActivities;
            int n = list.size();
            int n2 = 0;
            while (n2 < n) {
                if (list.get((int)n2).resolveInfo == resolveInfo) {
                    return n2;
                }
                ++n2;
            }
            return -1;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public ResolveInfo getDefaultActivity() {
        Object object = this.mInstanceLock;
        synchronized (object) {
            this.ensureConsistentState();
            if (this.mActivities.isEmpty()) return null;
            return this.mActivities.get((int)0).resolveInfo;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int getHistoryMaxSize() {
        Object object = this.mInstanceLock;
        synchronized (object) {
            return this.mHistoryMaxSize;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int getHistorySize() {
        Object object = this.mInstanceLock;
        synchronized (object) {
            this.ensureConsistentState();
            return this.mHistoricalRecords.size();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Intent getIntent() {
        Object object = this.mInstanceLock;
        synchronized (object) {
            return this.mIntent;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setActivitySorter(ActivitySorter activitySorter) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            if (this.mActivitySorter == activitySorter) {
                return;
            }
            this.mActivitySorter = activitySorter;
            if (this.sortActivitiesIfNeeded()) {
                this.notifyChanged();
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setDefaultActivity(int n) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            this.ensureConsistentState();
            ActivityResolveInfo activityResolveInfo = this.mActivities.get(n);
            ActivityResolveInfo activityResolveInfo2 = this.mActivities.get(0);
            float f = activityResolveInfo2 != null ? 5.0f + (activityResolveInfo2.weight - activityResolveInfo.weight) : 1.0f;
            this.addHistoricalRecord(new HistoricalRecord(new ComponentName(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name), System.currentTimeMillis(), f));
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setHistoryMaxSize(int n) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            if (this.mHistoryMaxSize == n) {
                return;
            }
            this.mHistoryMaxSize = n;
            this.pruneExcessiveHistoricalRecordsIfNeeded();
            if (this.sortActivitiesIfNeeded()) {
                this.notifyChanged();
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setIntent(Intent intent) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            if (this.mIntent == intent) {
                return;
            }
            this.mIntent = intent;
            this.mReloadActivities = true;
            this.ensureConsistentState();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setOnChooseActivityListener(OnChooseActivityListener onChooseActivityListener) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            this.mActivityChoserModelPolicy = onChooseActivityListener;
            return;
        }
    }

    public static interface ActivityChooserModelClient {
        public void setActivityChooserModel(ActivityChooserModel var1);
    }

    public final class ActivityResolveInfo
    implements Comparable<ActivityResolveInfo> {
        public final ResolveInfo resolveInfo;
        public float weight;

        public ActivityResolveInfo(ResolveInfo resolveInfo) {
            this.resolveInfo = resolveInfo;
        }

        @Override
        public int compareTo(ActivityResolveInfo activityResolveInfo) {
            return Float.floatToIntBits(activityResolveInfo.weight) - Float.floatToIntBits(this.weight);
        }

        /*
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null) {
                return false;
            }
            if (this.getClass() != object.getClass()) {
                return false;
            }
            ActivityResolveInfo activityResolveInfo = (ActivityResolveInfo)object;
            if (Float.floatToIntBits(this.weight) == Float.floatToIntBits(activityResolveInfo.weight)) return true;
            return false;
        }

        public int hashCode() {
            return 31 + Float.floatToIntBits(this.weight);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            stringBuilder.append("resolveInfo:").append(this.resolveInfo.toString());
            stringBuilder.append("; weight:").append(new BigDecimal(this.weight));
            stringBuilder.append("]");
            return stringBuilder.toString();
        }
    }

    public static interface ActivitySorter {
        public void sort(Intent var1, List<ActivityResolveInfo> var2, List<HistoricalRecord> var3);
    }

    private final class DefaultSorter
    implements ActivitySorter {
        private static final float WEIGHT_DECAY_COEFFICIENT = 0.95f;
        private final Map<ComponentName, ActivityResolveInfo> mPackageNameToActivityMap;

        DefaultSorter() {
            this.mPackageNameToActivityMap = new HashMap<ComponentName, ActivityResolveInfo>();
        }

        @Override
        public void sort(Intent intent, List<ActivityResolveInfo> list, List<HistoricalRecord> list2) {
            Map<ComponentName, ActivityResolveInfo> map = this.mPackageNameToActivityMap;
            map.clear();
            int n = list.size();
            for (int i = 0; i < n; ++i) {
                ActivityResolveInfo activityResolveInfo = list.get(i);
                activityResolveInfo.weight = 0.0f;
                map.put(new ComponentName(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name), activityResolveInfo);
            }
            int n2 = -1 + list2.size();
            float f = 1.0f;
            for (int j = n2; j >= 0; --j) {
                HistoricalRecord historicalRecord = list2.get(j);
                ActivityResolveInfo activityResolveInfo = map.get((Object)historicalRecord.activity);
                if (activityResolveInfo == null) continue;
                activityResolveInfo.weight += f * historicalRecord.weight;
                f *= 0.95f;
            }
            Collections.sort(list);
        }
    }

    public static final class HistoricalRecord {
        public final ComponentName activity;
        public final long time;
        public final float weight;

        public HistoricalRecord(ComponentName componentName, long l, float f) {
            this.activity = componentName;
            this.time = l;
            this.weight = f;
        }

        public HistoricalRecord(String string2, long l, float f) {
            this(ComponentName.unflattenFromString((String)string2), l, f);
        }

        /*
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null) {
                return false;
            }
            if (this.getClass() != object.getClass()) {
                return false;
            }
            HistoricalRecord historicalRecord = (HistoricalRecord)object;
            if (this.activity == null ? historicalRecord.activity != null : !this.activity.equals((Object)historicalRecord.activity)) {
                return false;
            }
            if (this.time != historicalRecord.time) {
                return false;
            }
            if (Float.floatToIntBits(this.weight) == Float.floatToIntBits(historicalRecord.weight)) return true;
            return false;
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public int hashCode() {
            int n;
            if (this.activity == null) {
                n = 0;
                do {
                    return 31 * (31 * (n + 31) + (int)(this.time ^ this.time >>> 32)) + Float.floatToIntBits(this.weight);
                    break;
                } while (true);
            }
            n = this.activity.hashCode();
            return 31 * (31 * (n + 31) + (int)(this.time ^ this.time >>> 32)) + Float.floatToIntBits(this.weight);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            stringBuilder.append("; activity:").append((Object)this.activity);
            stringBuilder.append("; time:").append(this.time);
            stringBuilder.append("; weight:").append(new BigDecimal(this.weight));
            stringBuilder.append("]");
            return stringBuilder.toString();
        }
    }

    public static interface OnChooseActivityListener {
        public boolean onChooseActivity(ActivityChooserModel var1, Intent var2);
    }

    private final class PersistHistoryAsyncTask
    extends AsyncTask<Object, Void, Void> {
        PersistHistoryAsyncTask() {
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        public /* varargs */ Void doInBackground(Object ... var1_1) {
            var2_2 = (List)var1_1[0];
            var3_3 = (String)var1_1[1];
            var6_4 = ActivityChooserModel.this.mContext.openFileOutput(var3_3, 0);
            var7_5 = Xml.newSerializer();
            try {
                var7_5.setOutput((OutputStream)var6_4, null);
                var7_5.startDocument("UTF-8", Boolean.valueOf(true));
                var7_5.startTag(null, "historical-records");
                var20_6 = var2_2.size();
                for (var21_7 = 0; var21_7 < var20_6; ++var21_7) {
                    var22_8 = (HistoricalRecord)var2_2.remove(0);
                    var7_5.startTag(null, "historical-record");
                    var7_5.attribute(null, "activity", var22_8.activity.flattenToString());
                    var7_5.attribute(null, "time", String.valueOf(var22_8.time));
                    var7_5.attribute(null, "weight", String.valueOf(var22_8.weight));
                    var7_5.endTag(null, "historical-record");
                }
                ** GOTO lbl23
                catch (FileNotFoundException var4_9) {
                    Log.e((String)ActivityChooserModel.LOG_TAG, (String)("Error writing historical record file: " + var3_3), (Throwable)var4_9);
                    return null;
                }
lbl23: // 1 sources:
                var7_5.endTag(null, "historical-records");
                var7_5.endDocument();
                return null;
            }
            catch (IllegalArgumentException var16_10) {
                Log.e((String)ActivityChooserModel.LOG_TAG, (String)("Error writing historical record file: " + ActivityChooserModel.this.mHistoryFileName), (Throwable)var16_10);
                return null;
            }
            catch (IllegalStateException var13_12) {
                Log.e((String)ActivityChooserModel.LOG_TAG, (String)("Error writing historical record file: " + ActivityChooserModel.this.mHistoryFileName), (Throwable)var13_12);
                return null;
                {
                    catch (Throwable var8_16) {
                        throw var8_16;
                    }
                }
                catch (IOException var10_14) {
                    Log.e((String)ActivityChooserModel.LOG_TAG, (String)("Error writing historical record file: " + ActivityChooserModel.this.mHistoryFileName), (Throwable)var10_14);
                    return null;
                }
            }
            finally {
                ActivityChooserModel.this.mCanReadHistoricalData = true;
                if (var6_4 == null) return null;
                var6_4.close();
                return null;
            }
        }
    }

}

