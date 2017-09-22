/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.BitmapFactory
 *  android.graphics.BitmapFactory$Options
 *  android.graphics.Canvas
 *  android.graphics.ColorFilter
 *  android.graphics.ColorMatrix
 *  android.graphics.ColorMatrixColorFilter
 *  android.graphics.Matrix
 *  android.graphics.Paint
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  android.graphics.pdf.PdfDocument
 *  android.graphics.pdf.PdfDocument$Page
 *  android.graphics.pdf.PdfDocument$PageInfo
 *  android.net.Uri
 *  android.os.AsyncTask
 *  android.os.Bundle
 *  android.os.CancellationSignal
 *  android.os.CancellationSignal$OnCancelListener
 *  android.os.ParcelFileDescriptor
 *  android.print.PageRange
 *  android.print.PrintAttributes
 *  android.print.PrintAttributes$Builder
 *  android.print.PrintAttributes$Margins
 *  android.print.PrintAttributes$MediaSize
 *  android.print.PrintAttributes$Resolution
 *  android.print.PrintDocumentAdapter
 *  android.print.PrintDocumentAdapter$LayoutResultCallback
 *  android.print.PrintDocumentAdapter$WriteResultCallback
 *  android.print.PrintDocumentInfo
 *  android.print.PrintDocumentInfo$Builder
 *  android.print.PrintJob
 *  android.print.PrintManager
 *  android.print.pdf.PrintedPdfDocument
 *  android.util.Log
 */
package android.support.v4.print;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintJob;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@TargetApi(value=19)
@RequiresApi(value=19)
class PrintHelperKitkat {
    public static final int COLOR_MODE_COLOR = 2;
    public static final int COLOR_MODE_MONOCHROME = 1;
    private static final String LOG_TAG = "PrintHelperKitkat";
    private static final int MAX_PRINT_SIZE = 3500;
    public static final int ORIENTATION_LANDSCAPE = 1;
    public static final int ORIENTATION_PORTRAIT = 2;
    public static final int SCALE_MODE_FILL = 2;
    public static final int SCALE_MODE_FIT = 1;
    int mColorMode = 2;
    final Context mContext;
    BitmapFactory.Options mDecodeOptions = null;
    protected boolean mIsMinMarginsHandlingCorrect = true;
    private final Object mLock = new Object();
    int mOrientation;
    protected boolean mPrintActivityRespectsOrientation = true;
    int mScaleMode = 2;

    PrintHelperKitkat(Context context) {
        this.mContext = context;
    }

    static /* synthetic */ Bitmap access$100(PrintHelperKitkat printHelperKitkat, Bitmap bitmap, int n) {
        return printHelperKitkat.convertBitmapForColorMode(bitmap, n);
    }

    static /* synthetic */ Matrix access$200(PrintHelperKitkat printHelperKitkat, int n, int n2, RectF rectF, int n3) {
        return printHelperKitkat.getMatrix(n, n2, rectF, n3);
    }

    private Bitmap convertBitmapForColorMode(Bitmap bitmap, int n) {
        if (n != 1) {
            return bitmap;
        }
        Bitmap bitmap2 = Bitmap.createBitmap((int)bitmap.getWidth(), (int)bitmap.getHeight(), (Bitmap.Config)Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap2);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.0f);
        paint.setColorFilter((ColorFilter)new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        canvas.setBitmap(null);
        return bitmap2;
    }

    /*
     * Enabled aggressive block sorting
     */
    private Matrix getMatrix(int n, int n2, RectF rectF, int n3) {
        Matrix matrix = new Matrix();
        float f = rectF.width() / (float)n;
        float f2 = n3 == 2 ? Math.max(f, rectF.height() / (float)n2) : Math.min(f, rectF.height() / (float)n2);
        matrix.postScale(f2, f2);
        matrix.postTranslate((rectF.width() - f2 * (float)n) / 2.0f, (rectF.height() - f2 * (float)n2) / 2.0f);
        return matrix;
    }

    private static boolean isPortrait(Bitmap bitmap) {
        if (bitmap.getWidth() <= bitmap.getHeight()) {
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private Bitmap loadBitmap(Uri uri, BitmapFactory.Options options) throws FileNotFoundException {
        Bitmap bitmap;
        if (uri == null) throw new IllegalArgumentException("bad argument to loadBitmap");
        if (this.mContext == null) {
            throw new IllegalArgumentException("bad argument to loadBitmap");
        }
        InputStream inputStream = null;
        try {
            inputStream = this.mContext.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream((InputStream)inputStream, (Rect)null, (BitmapFactory.Options)options);
            if (inputStream == null) return bitmap;
        }
        catch (Throwable var4_6) {
            if (inputStream == null) throw var4_6;
            try {
                inputStream.close();
            }
            catch (IOException iOException) {
                Log.w((String)"PrintHelperKitkat", (String)"close fail ", (Throwable)iOException);
                throw var4_6;
            }
            throw var4_6;
        }
        try {
            inputStream.close();
            return bitmap;
        }
        catch (IOException var8_5) {
            Log.w((String)"PrintHelperKitkat", (String)"close fail ", (Throwable)var8_5);
            return bitmap;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private Bitmap loadConstrainedBitmap(Uri uri, int n) throws FileNotFoundException {
        if (n <= 0) throw new IllegalArgumentException("bad argument to getScaledBitmap");
        if (uri == null) throw new IllegalArgumentException("bad argument to getScaledBitmap");
        if (this.mContext == null) {
            throw new IllegalArgumentException("bad argument to getScaledBitmap");
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        this.loadBitmap(uri, options);
        int n2 = options.outWidth;
        int n3 = options.outHeight;
        if (n2 <= 0) return null;
        if (n3 <= 0) {
            return null;
        }
        int n4 = 1;
        for (int i = Math.max((int)n2, (int)n3); i > n; i >>>= 1, n4 <<= 1) {
        }
        if (n4 <= 0) return null;
        if (Math.min(n2, n3) / n4 <= 0) return null;
        Object object = this.mLock;
        // MONITORENTER : object
        this.mDecodeOptions = new BitmapFactory.Options();
        this.mDecodeOptions.inMutable = true;
        this.mDecodeOptions.inSampleSize = n4;
        BitmapFactory.Options options2 = this.mDecodeOptions;
        // MONITOREXIT : object
        try {
            Bitmap bitmap = this.loadBitmap(uri, options2);
            return bitmap;
        }
        finally {
            Object object2 = this.mLock;
            // MONITORENTER : object2
            this.mDecodeOptions = null;
            // MONITOREXIT : object2
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void writeBitmap(final PrintAttributes printAttributes, final int n, final Bitmap bitmap, final ParcelFileDescriptor parcelFileDescriptor, final CancellationSignal cancellationSignal, final PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
        final PrintAttributes printAttributes2 = this.mIsMinMarginsHandlingCorrect ? printAttributes : this.copyAttributes(printAttributes).setMinMargins(new PrintAttributes.Margins(0, 0, 0, 0)).build();
        new AsyncTask<Void, Void, Throwable>(){

            /*
             * Unable to fully structure code
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            protected /* varargs */ Throwable doInBackground(Void ... var1_1) {
                block20 : {
                    if (cancellationSignal.isCanceled()) {
                        return null;
                    }
                    var3_2 = new PrintedPdfDocument(PrintHelperKitkat.this.mContext, printAttributes2);
                    var4_3 = PrintHelperKitkat.access$100(PrintHelperKitkat.this, bitmap, printAttributes2.getColorMode());
                    var5_4 = cancellationSignal.isCanceled();
                    if (var5_4 != false) return null;
                    {
                        catch (Throwable var2_10) {
                            return var2_10;
                        }
                    }
                    try {
                        var9_5 = var3_2.startPage(1);
                        if (PrintHelperKitkat.this.mIsMinMarginsHandlingCorrect) {
                            var10_6 = new RectF(var9_5.getInfo().getContentRect());
                        } else {
                            var19_11 = new PrintedPdfDocument(PrintHelperKitkat.this.mContext, printAttributes);
                            var20_12 = var19_11.startPage(1);
                            var10_6 = new RectF(var20_12.getInfo().getContentRect());
                            var19_11.finishPage(var20_12);
                            var19_11.close();
                        }
                        var11_7 = PrintHelperKitkat.access$200(PrintHelperKitkat.this, var4_3.getWidth(), var4_3.getHeight(), var10_6, n);
                        if (!PrintHelperKitkat.this.mIsMinMarginsHandlingCorrect) {
                            var11_7.postTranslate(var10_6.left, var10_6.top);
                            var9_5.getCanvas().clipRect(var10_6);
                        }
                        var9_5.getCanvas().drawBitmap(var4_3, var11_7, null);
                        var3_2.finishPage(var9_5);
                        var12_8 = cancellationSignal.isCanceled();
                        ** if (!var12_8) goto lbl35
                    }
                    catch (Throwable var6_13) {
                        var3_2.close();
                        var7_14 = parcelFileDescriptor;
                        if (var7_14 == null) ** GOTO lbl54
                        parcelFileDescriptor.close();
lbl44: // 1 sources:
                        var3_2.writeTo((OutputStream)new FileOutputStream(parcelFileDescriptor.getFileDescriptor()));
                        var3_2.close();
                        var13_15 = parcelFileDescriptor;
                        if (var13_15 == null) break block20;
                        ** try [egrp 9[TRYBLOCK] [18 : 372->379)] { 
lbl-1000: // 1 sources:
                        {
                            parcelFileDescriptor.close();
                            break block20;
                        }
                        catch (IOException var8_16) {}
lbl54: // 3 sources:
                        if (var4_3 == bitmap) throw var6_13;
                        var4_3.recycle();
                        throw var6_13;
                    }
lbl-1000: // 1 sources:
                    {
                        var3_2.close();
                        var15_9 = parcelFileDescriptor;
                        if (var15_9 == null) ** GOTO lbl63
                        parcelFileDescriptor.close();
                    }
lbl35: // 1 sources:
                    ** GOTO lbl44
lbl57: // 1 sources:
                    catch (IOException var14_17) {}
                }
                if (var4_3 == bitmap) return null;
                var4_3.recycle();
                return null;
                catch (IOException var16_18) {}
lbl63: // 3 sources:
                if (var4_3 == bitmap) return null;
                var4_3.recycle();
                return null;
            }

            protected void onPostExecute(Throwable throwable) {
                if (cancellationSignal.isCanceled()) {
                    writeResultCallback.onWriteCancelled();
                    return;
                }
                if (throwable == null) {
                    PrintDocumentAdapter.WriteResultCallback writeResultCallback2 = writeResultCallback;
                    PageRange[] arrpageRange = new PageRange[]{PageRange.ALL_PAGES};
                    writeResultCallback2.onWriteFinished(arrpageRange);
                    return;
                }
                Log.e((String)"PrintHelperKitkat", (String)"Error writing printed content", (Throwable)throwable);
                writeResultCallback.onWriteFailed(null);
            }
        }.execute((Object[])new Void[0]);
    }

    protected PrintAttributes.Builder copyAttributes(PrintAttributes printAttributes) {
        PrintAttributes.Builder builder = new PrintAttributes.Builder().setMediaSize(printAttributes.getMediaSize()).setResolution(printAttributes.getResolution()).setMinMargins(printAttributes.getMinMargins());
        if (printAttributes.getColorMode() != 0) {
            builder.setColorMode(printAttributes.getColorMode());
        }
        return builder;
    }

    public int getColorMode() {
        return this.mColorMode;
    }

    public int getOrientation() {
        if (this.mOrientation == 0) {
            return 1;
        }
        return this.mOrientation;
    }

    public int getScaleMode() {
        return this.mScaleMode;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void printBitmap(final String string2, final Bitmap bitmap, final OnPrintFinishCallback onPrintFinishCallback) {
        if (bitmap == null) {
            return;
        }
        final int n = this.mScaleMode;
        PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
        PrintAttributes.MediaSize mediaSize = PrintHelperKitkat.isPortrait(bitmap) ? PrintAttributes.MediaSize.UNKNOWN_PORTRAIT : PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE;
        PrintAttributes printAttributes = new PrintAttributes.Builder().setMediaSize(mediaSize).setColorMode(this.mColorMode).build();
        printManager.print(string2, new PrintDocumentAdapter(){
            private PrintAttributes mAttributes;

            public void onFinish() {
                if (onPrintFinishCallback != null) {
                    onPrintFinishCallback.onFinish();
                }
            }

            /*
             * Enabled aggressive block sorting
             */
            public void onLayout(PrintAttributes printAttributes, PrintAttributes printAttributes2, CancellationSignal cancellationSignal, PrintDocumentAdapter.LayoutResultCallback layoutResultCallback, Bundle bundle) {
                boolean bl = true;
                this.mAttributes = printAttributes2;
                PrintDocumentInfo printDocumentInfo = new PrintDocumentInfo.Builder(string2).setContentType((int)bl ? 1 : 0).setPageCount((int)bl ? 1 : 0).build();
                if (printAttributes2.equals((Object)printAttributes)) {
                    bl = false;
                }
                layoutResultCallback.onLayoutFinished(printDocumentInfo, bl);
            }

            public void onWrite(PageRange[] arrpageRange, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
                PrintHelperKitkat.this.writeBitmap(this.mAttributes, n, bitmap, parcelFileDescriptor, cancellationSignal, writeResultCallback);
            }
        }, printAttributes);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void printBitmap(final String string2, final Uri uri, final OnPrintFinishCallback onPrintFinishCallback) throws FileNotFoundException {
        PrintDocumentAdapter printDocumentAdapter = new PrintDocumentAdapter(this.mScaleMode){
            private PrintAttributes mAttributes;
            Bitmap mBitmap;
            AsyncTask<Uri, Boolean, Bitmap> mLoadBitmap;
            final /* synthetic */ int val$fittingMode;

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            private void cancelLoad() {
                Object object = PrintHelperKitkat.this.mLock;
                synchronized (object) {
                    if (PrintHelperKitkat.this.mDecodeOptions != null) {
                        PrintHelperKitkat.this.mDecodeOptions.requestCancelDecode();
                        PrintHelperKitkat.this.mDecodeOptions = null;
                    }
                    return;
                }
            }

            public void onFinish() {
                super.onFinish();
                this.cancelLoad();
                if (this.mLoadBitmap != null) {
                    this.mLoadBitmap.cancel(true);
                }
                if (onPrintFinishCallback != null) {
                    onPrintFinishCallback.onFinish();
                }
                if (this.mBitmap != null) {
                    this.mBitmap.recycle();
                    this.mBitmap = null;
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Converted monitor instructions to comments
             * Lifted jumps to return sites
             */
            public void onLayout(final PrintAttributes printAttributes, final PrintAttributes printAttributes2, final CancellationSignal cancellationSignal, final PrintDocumentAdapter.LayoutResultCallback layoutResultCallback, Bundle bundle) {
                boolean bl = true;
                // MONITORENTER : this
                this.mAttributes = printAttributes2;
                // MONITOREXIT : this
                if (cancellationSignal.isCanceled()) {
                    layoutResultCallback.onLayoutCancelled();
                    return;
                }
                if (this.mBitmap == null) {
                    this.mLoadBitmap = new AsyncTask<Uri, Boolean, Bitmap>(){

                        protected /* varargs */ Bitmap doInBackground(Uri ... arruri) {
                            try {
                                Bitmap bitmap = PrintHelperKitkat.this.loadConstrainedBitmap(uri, 3500);
                                return bitmap;
                            }
                            catch (FileNotFoundException var2_3) {
                                return null;
                            }
                        }

                        protected void onCancelled(Bitmap bitmap) {
                            layoutResultCallback.onLayoutCancelled();
                            3.this.mLoadBitmap = null;
                        }

                        /*
                         * Enabled aggressive block sorting
                         * Enabled unnecessary exception pruning
                         * Enabled aggressive exception aggregation
                         * Converted monitor instructions to comments
                         * Lifted jumps to return sites
                         */
                        protected void onPostExecute(Bitmap bitmap) {
                            super.onPostExecute((Object)bitmap);
                            if (!(bitmap == null || PrintHelperKitkat.this.mPrintActivityRespectsOrientation && PrintHelperKitkat.this.mOrientation != 0)) {
                                // MONITORENTER : this
                                PrintAttributes.MediaSize mediaSize = 3.this.mAttributes.getMediaSize();
                                // MONITOREXIT : this
                                if (mediaSize != null && mediaSize.isPortrait() != PrintHelperKitkat.isPortrait(bitmap)) {
                                    Matrix matrix = new Matrix();
                                    matrix.postRotate(90.0f);
                                    int n = bitmap.getWidth();
                                    int n2 = bitmap.getHeight();
                                    bitmap = Bitmap.createBitmap((Bitmap)bitmap, (int)0, (int)0, (int)n, (int)n2, (Matrix)matrix, (boolean)true);
                                }
                            }
                            3.this.mBitmap = bitmap;
                            if (bitmap != null) {
                                PrintDocumentInfo printDocumentInfo = new PrintDocumentInfo.Builder(string2).setContentType(1).setPageCount(1).build();
                                boolean bl = !printAttributes2.equals((Object)printAttributes);
                                layoutResultCallback.onLayoutFinished(printDocumentInfo, bl);
                            } else {
                                layoutResultCallback.onLayoutFailed(null);
                            }
                            3.this.mLoadBitmap = null;
                        }

                        protected void onPreExecute() {
                            cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener(){

                                public void onCancel() {
                                    3.this.cancelLoad();
                                    1.this.cancel(false);
                                }
                            });
                        }

                    }.execute((Object[])new Uri[0]);
                    return;
                }
                PrintDocumentInfo printDocumentInfo = new PrintDocumentInfo.Builder(string2).setContentType((int)bl ? 1 : 0).setPageCount((int)bl ? 1 : 0).build();
                if (printAttributes2.equals((Object)printAttributes)) {
                    bl = false;
                }
                layoutResultCallback.onLayoutFinished(printDocumentInfo, bl);
            }

            public void onWrite(PageRange[] arrpageRange, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
                PrintHelperKitkat.this.writeBitmap(this.mAttributes, this.val$fittingMode, this.mBitmap, parcelFileDescriptor, cancellationSignal, writeResultCallback);
            }

        };
        PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setColorMode(this.mColorMode);
        if (this.mOrientation == 1 || this.mOrientation == 0) {
            builder.setMediaSize(PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE);
        } else if (this.mOrientation == 2) {
            builder.setMediaSize(PrintAttributes.MediaSize.UNKNOWN_PORTRAIT);
        }
        printManager.print(string2, printDocumentAdapter, builder.build());
    }

    public void setColorMode(int n) {
        this.mColorMode = n;
    }

    public void setOrientation(int n) {
        this.mOrientation = n;
    }

    public void setScaleMode(int n) {
        this.mScaleMode = n;
    }

    public static interface OnPrintFinishCallback {
        public void onFinish();
    }

}

