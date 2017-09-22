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
import android.util.Log;
import java.util.ArrayList;

@TargetApi(value=21)
@RequiresApi(value=21)
class DocumentsContractApi21 {
    private static final String TAG = "DocumentFile";

    DocumentsContractApi21() {
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

    public static Uri createDirectory(Context context, Uri uri, String string2) {
        return DocumentsContractApi21.createFile(context, uri, "vnd.android.document/directory", string2);
    }

    public static Uri createFile(Context context, Uri uri, String string2, String string3) {
        return DocumentsContract.createDocument((ContentResolver)context.getContentResolver(), (Uri)uri, (String)string2, (String)string3);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static Uri[] listFiles(Context context, Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri2 = DocumentsContract.buildChildDocumentsUriUsingTree((Uri)uri, (String)DocumentsContract.getDocumentId((Uri)uri));
        ArrayList<Uri> arrayList = new ArrayList<Uri>();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(uri2, new String[]{"document_id"}, null, null, null);
            while (cursor.moveToNext()) {
                arrayList.add(DocumentsContract.buildDocumentUriUsingTree((Uri)uri, (String)cursor.getString(0)));
            }
            return arrayList.toArray((T[])new Uri[arrayList.size()]);
        }
        catch (Exception var7_6) {
            Log.w((String)"DocumentFile", (String)("Failed query: " + var7_6));
            do {
                return arrayList.toArray((T[])new Uri[arrayList.size()]);
                break;
            } while (true);
        }
        finally {
            DocumentsContractApi21.closeQuietly((AutoCloseable)cursor);
            return arrayList.toArray((T[])new Uri[arrayList.size()]);
        }
    }

    public static Uri prepareTreeUri(Uri uri) {
        return DocumentsContract.buildDocumentUriUsingTree((Uri)uri, (String)DocumentsContract.getTreeDocumentId((Uri)uri));
    }

    public static Uri renameTo(Context context, Uri uri, String string2) {
        return DocumentsContract.renameDocument((ContentResolver)context.getContentResolver(), (Uri)uri, (String)string2);
    }
}

