/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.database.Cursor
 *  android.net.Uri
 *  android.provider.DocumentsContract
 *  android.text.TextUtils
 *  android.util.Log
 */
package android.support.v4.provider;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

@TargetApi(value=19)
@RequiresApi(value=19)
class DocumentsContractApi19 {
    private static final int FLAG_VIRTUAL_DOCUMENT = 512;
    private static final String TAG = "DocumentFile";

    DocumentsContractApi19() {
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean canRead(Context context, Uri uri) {
        if (context.checkCallingOrSelfUriPermission(uri, 1) != 0 || TextUtils.isEmpty((CharSequence)DocumentsContractApi19.getRawType(context, uri))) {
            return false;
        }
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public static boolean canWrite(Context context, Uri uri) {
        if (context.checkCallingOrSelfUriPermission(uri, 2) != 0) {
            return false;
        }
        String string2 = DocumentsContractApi19.getRawType(context, uri);
        int n = DocumentsContractApi19.queryForInt(context, uri, "flags", 0);
        if (TextUtils.isEmpty((CharSequence)string2)) return false;
        if ((n & 4) != 0) {
            return true;
        }
        if ("vnd.android.document/directory".equals(string2) && (n & 8) != 0) {
            return true;
        }
        if (TextUtils.isEmpty((CharSequence)string2)) return false;
        if ((n & 2) == 0) return false;
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static void closeQuietly(AutoCloseable autoCloseable) {
        if (autoCloseable == null) return;
        try {
            autoCloseable.close();
            return;
        }
        catch (RuntimeException var2_1) {
            throw var2_1;
        }
        catch (Exception var1_2) {
            return;
        }
    }

    public static boolean delete(Context context, Uri uri) {
        return DocumentsContract.deleteDocument((ContentResolver)context.getContentResolver(), (Uri)uri);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static boolean exists(Context context, Uri uri) {
        boolean bl;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(uri, new String[]{"document_id"}, null, null, null);
            int n = cursor.getCount();
            bl = n > 0;
        }
        catch (Exception var5_6) {
            Log.w((String)"DocumentFile", (String)("Failed query: " + var5_6));
            return false;
        }
        finally {
            DocumentsContractApi19.closeQuietly((AutoCloseable)cursor);
        }
        DocumentsContractApi19.closeQuietly((AutoCloseable)cursor);
        return bl;
    }

    public static long getFlags(Context context, Uri uri) {
        return DocumentsContractApi19.queryForLong(context, uri, "flags", 0);
    }

    public static String getName(Context context, Uri uri) {
        return DocumentsContractApi19.queryForString(context, uri, "_display_name", null);
    }

    private static String getRawType(Context context, Uri uri) {
        return DocumentsContractApi19.queryForString(context, uri, "mime_type", null);
    }

    public static String getType(Context context, Uri uri) {
        String string2 = DocumentsContractApi19.getRawType(context, uri);
        if ("vnd.android.document/directory".equals(string2)) {
            string2 = null;
        }
        return string2;
    }

    public static boolean isDirectory(Context context, Uri uri) {
        return "vnd.android.document/directory".equals(DocumentsContractApi19.getRawType(context, uri));
    }

    public static boolean isDocumentUri(Context context, Uri uri) {
        return DocumentsContract.isDocumentUri((Context)context, (Uri)uri);
    }

    public static boolean isFile(Context context, Uri uri) {
        String string2 = DocumentsContractApi19.getRawType(context, uri);
        if ("vnd.android.document/directory".equals(string2) || TextUtils.isEmpty((CharSequence)string2)) {
            return false;
        }
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean isVirtual(Context context, Uri uri) {
        if (!DocumentsContractApi19.isDocumentUri(context, uri) || (512 & DocumentsContractApi19.getFlags(context, uri)) == 0) {
            return false;
        }
        return true;
    }

    public static long lastModified(Context context, Uri uri) {
        return DocumentsContractApi19.queryForLong(context, uri, "last_modified", 0);
    }

    public static long length(Context context, Uri uri) {
        return DocumentsContractApi19.queryForLong(context, uri, "_size", 0);
    }

    private static int queryForInt(Context context, Uri uri, String string2, int n) {
        return (int)DocumentsContractApi19.queryForLong(context, uri, string2, n);
    }

    private static long queryForLong(Context context, Uri uri, String string2, long l) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(uri, new String[]{string2}, null, null, null);
            if (cursor.moveToFirst() && !cursor.isNull(0)) {
                long l2 = cursor.getLong(0);
                return l2;
            }
            return l;
        }
        catch (Exception var8_7) {
            Log.w((String)"DocumentFile", (String)("Failed query: " + var8_7));
            return l;
        }
        finally {
            DocumentsContractApi19.closeQuietly((AutoCloseable)cursor);
        }
    }

    private static String queryForString(Context context, Uri uri, String string2, String string3) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(uri, new String[]{string2}, null, null, null);
            if (cursor.moveToFirst() && !cursor.isNull(0)) {
                String string4 = cursor.getString(0);
                return string4;
            }
            return string3;
        }
        catch (Exception var7_7) {
            Log.w((String)"DocumentFile", (String)("Failed query: " + var7_7));
            return string3;
        }
        finally {
            DocumentsContractApi19.closeQuietly((AutoCloseable)cursor);
        }
    }
}

