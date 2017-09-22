/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.text.Spannable
 *  android.text.SpannableString
 *  android.text.method.LinkMovementMethod
 *  android.text.method.MovementMethod
 *  android.text.style.URLSpan
 *  android.text.util.Linkify
 *  android.text.util.Linkify$MatchFilter
 *  android.text.util.Linkify$TransformFilter
 *  android.webkit.WebView
 *  android.widget.TextView
 */
package android.support.v4.text.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.PatternsCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.webkit.WebView;
import android.widget.TextView;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LinkifyCompat {
    private static final Comparator<LinkSpec> COMPARATOR;
    private static final String[] EMPTY_STRING;

    static {
        EMPTY_STRING = new String[0];
        COMPARATOR = new Comparator<LinkSpec>(){

            /*
             * Enabled aggressive block sorting
             * Lifted jumps to return sites
             */
            @Override
            public final int compare(LinkSpec linkSpec, LinkSpec linkSpec2) {
                if (linkSpec.start < linkSpec2.start) {
                    return -1;
                }
                if (linkSpec.start > linkSpec2.start) {
                    return 1;
                }
                if (linkSpec.end < linkSpec2.end) {
                    return 1;
                }
                if (linkSpec.end > linkSpec2.end) return -1;
                return 0;
            }
        };
    }

    private LinkifyCompat() {
    }

    private static void addLinkMovementMethod(@NonNull TextView textView) {
        MovementMethod movementMethod = textView.getMovementMethod();
        if ((movementMethod == null || !(movementMethod instanceof LinkMovementMethod)) && textView.getLinksClickable()) {
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    public static final void addLinks(@NonNull TextView textView, @NonNull Pattern pattern, @Nullable String string2) {
        LinkifyCompat.addLinks(textView, pattern, string2, null, null, null);
    }

    public static final void addLinks(@NonNull TextView textView, @NonNull Pattern pattern, @Nullable String string2, @Nullable Linkify.MatchFilter matchFilter, @Nullable Linkify.TransformFilter transformFilter) {
        LinkifyCompat.addLinks(textView, pattern, string2, null, matchFilter, transformFilter);
    }

    public static final void addLinks(@NonNull TextView textView, @NonNull Pattern pattern, @Nullable String string2, @Nullable String[] arrstring, @Nullable Linkify.MatchFilter matchFilter, @Nullable Linkify.TransformFilter transformFilter) {
        SpannableString spannableString = SpannableString.valueOf((CharSequence)textView.getText());
        if (LinkifyCompat.addLinks((Spannable)spannableString, pattern, string2, arrstring, matchFilter, transformFilter)) {
            textView.setText((CharSequence)spannableString);
            LinkifyCompat.addLinkMovementMethod(textView);
        }
    }

    public static final boolean addLinks(@NonNull Spannable spannable, int n) {
        if (n == 0) {
            return false;
        }
        URLSpan[] arruRLSpan = (URLSpan[])spannable.getSpans(0, spannable.length(), (Class)URLSpan.class);
        for (int i = -1 + arruRLSpan.length; i >= 0; --i) {
            spannable.removeSpan((Object)arruRLSpan[i]);
        }
        if ((n & 4) != 0) {
            Linkify.addLinks((Spannable)spannable, (int)4);
        }
        ArrayList<LinkSpec> arrayList = new ArrayList<LinkSpec>();
        if ((n & 1) != 0) {
            LinkifyCompat.gatherLinks(arrayList, spannable, PatternsCompat.AUTOLINK_WEB_URL, new String[]{"http://", "https://", "rtsp://"}, Linkify.sUrlMatchFilter, null);
        }
        if ((n & 2) != 0) {
            LinkifyCompat.gatherLinks(arrayList, spannable, PatternsCompat.AUTOLINK_EMAIL_ADDRESS, new String[]{"mailto:"}, null, null);
        }
        if ((n & 8) != 0) {
            LinkifyCompat.gatherMapLinks(arrayList, spannable);
        }
        LinkifyCompat.pruneOverlaps(arrayList, spannable);
        if (arrayList.size() == 0) {
            return false;
        }
        for (LinkSpec linkSpec : arrayList) {
            if (linkSpec.frameworkAddedSpan != null) continue;
            LinkifyCompat.applyLink(linkSpec.url, linkSpec.start, linkSpec.end, spannable);
        }
        return true;
    }

    public static final boolean addLinks(@NonNull Spannable spannable, @NonNull Pattern pattern, @Nullable String string2) {
        return LinkifyCompat.addLinks(spannable, pattern, string2, null, null, null);
    }

    public static final boolean addLinks(@NonNull Spannable spannable, @NonNull Pattern pattern, @Nullable String string2, @Nullable Linkify.MatchFilter matchFilter, @Nullable Linkify.TransformFilter transformFilter) {
        return LinkifyCompat.addLinks(spannable, pattern, string2, null, matchFilter, transformFilter);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static final boolean addLinks(@NonNull Spannable spannable, @NonNull Pattern pattern, @Nullable String string2, @Nullable String[] arrstring, @Nullable Linkify.MatchFilter matchFilter, @Nullable Linkify.TransformFilter transformFilter) {
        if (string2 == null) {
            string2 = "";
        }
        if (arrstring == null || arrstring.length < 1) {
            arrstring = EMPTY_STRING;
        }
        String[] arrstring2 = new String[1 + arrstring.length];
        arrstring2[0] = string2.toLowerCase(Locale.ROOT);
        for (int i = 0; i < arrstring.length; ++i) {
            String string3 = arrstring[i];
            int n = i + 1;
            String string4 = string3 == null ? "" : string3.toLowerCase(Locale.ROOT);
            arrstring2[n] = string4;
        }
        boolean bl = false;
        Matcher matcher = pattern.matcher((CharSequence)spannable);
        while (matcher.find()) {
            int n = matcher.start();
            int n2 = matcher.end();
            boolean bl2 = true;
            if (matchFilter != null && !(bl2 = matchFilter.acceptMatch((CharSequence)spannable, n, n2))) continue;
            LinkifyCompat.applyLink(LinkifyCompat.makeUrl(matcher.group(0), arrstring2, matcher, transformFilter), n, n2, spannable);
            bl = true;
        }
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static final boolean addLinks(@NonNull TextView textView, int n) {
        if (n == 0) return false;
        CharSequence charSequence = textView.getText();
        if (charSequence instanceof Spannable) {
            if (!LinkifyCompat.addLinks((Spannable)charSequence, n)) return false;
            {
                LinkifyCompat.addLinkMovementMethod(textView);
                return true;
            }
        }
        SpannableString spannableString = SpannableString.valueOf((CharSequence)charSequence);
        if (!LinkifyCompat.addLinks((Spannable)spannableString, n)) {
            return false;
        }
        LinkifyCompat.addLinkMovementMethod(textView);
        textView.setText((CharSequence)spannableString);
        return true;
    }

    private static void applyLink(String string2, int n, int n2, Spannable spannable) {
        spannable.setSpan((Object)new URLSpan(string2), n, n2, 33);
    }

    private static void gatherLinks(ArrayList<LinkSpec> arrayList, Spannable spannable, Pattern pattern, String[] arrstring, Linkify.MatchFilter matchFilter, Linkify.TransformFilter transformFilter) {
        Matcher matcher = pattern.matcher((CharSequence)spannable);
        while (matcher.find()) {
            int n = matcher.start();
            int n2 = matcher.end();
            if (matchFilter != null && !matchFilter.acceptMatch((CharSequence)spannable, n, n2)) continue;
            LinkSpec linkSpec = new LinkSpec();
            linkSpec.url = LinkifyCompat.makeUrl(matcher.group(0), arrstring, matcher, transformFilter);
            linkSpec.start = n;
            linkSpec.end = n2;
            arrayList.add(linkSpec);
        }
    }

    private static final void gatherMapLinks(ArrayList<LinkSpec> arrayList, Spannable spannable) {
        String string2 = spannable.toString();
        int n = 0;
        do {
            String string3;
            int n2;
            String string4;
            block8 : {
                try {
                    string4 = WebView.findAddress((String)string2);
                    if (string4 == null) break;
                }
                catch (UnsupportedOperationException var4_11) {
                    return;
                }
                n2 = string2.indexOf(string4);
                if (n2 >= 0) break block8;
                return;
            }
            LinkSpec linkSpec = new LinkSpec();
            int n3 = n2 + string4.length();
            linkSpec.start = n + n2;
            linkSpec.end = n + n3;
            string2 = string3 = string2.substring(n3);
            n += n3;
            try {
                String string5 = URLEncoder.encode(string4, "UTF-8");
                linkSpec.url = "geo:0,0?q=" + string5;
                arrayList.add(linkSpec);
            }
            catch (UnsupportedEncodingException var10_9) {}
            continue;
            break;
        } while (true);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private static String makeUrl(@NonNull String var0, @NonNull String[] var1_1, Matcher var2_2, @Nullable Linkify.TransformFilter var3_3) {
        if (var3_3 != null) {
            var0 = var3_3.transformUrl(var2_2, var0);
        }
        var4_4 = 0;
        do {
            var5_5 = var1_1.length;
            var6_6 = false;
            if (var4_4 >= var5_5) ** GOTO lbl17
            var7_7 = var1_1[var4_4];
            var8_8 = var1_1[var4_4].length();
            if (var0.regionMatches(true, 0, var7_7, 0, var8_8)) {
                var6_6 = true;
                var9_9 = var1_1[var4_4];
                var10_10 = var1_1[var4_4].length();
                if (var0.regionMatches(false, 0, var9_9, 0, var10_10) != false) return var0;
                var0 = var1_1[var4_4] + var0.substring(var1_1[var4_4].length());
                if (var6_6 != false) return var0;
lbl17: // 2 sources:
                if (var1_1.length <= 0) return var0;
                return var1_1[0] + var0;
            }
            ++var4_4;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    private static final void pruneOverlaps(ArrayList<LinkSpec> arrayList, Spannable spannable) {
        URLSpan[] arruRLSpan = (URLSpan[])spannable.getSpans(0, spannable.length(), (Class)URLSpan.class);
        for (int i = 0; i < arruRLSpan.length; ++i) {
            LinkSpec linkSpec = new LinkSpec();
            linkSpec.frameworkAddedSpan = arruRLSpan[i];
            linkSpec.start = spannable.getSpanStart((Object)arruRLSpan[i]);
            linkSpec.end = spannable.getSpanEnd((Object)arruRLSpan[i]);
            arrayList.add(linkSpec);
        }
        Collections.sort(arrayList, COMPARATOR);
        int n = arrayList.size();
        int n2 = 0;
        while (n2 < n - 1) {
            LinkSpec linkSpec = arrayList.get(n2);
            LinkSpec linkSpec2 = arrayList.get(n2 + 1);
            int n3 = -1;
            if (linkSpec.start <= linkSpec2.start && linkSpec.end > linkSpec2.start) {
                if (linkSpec2.end <= linkSpec.end) {
                    n3 = n2 + 1;
                } else if (linkSpec.end - linkSpec.start > linkSpec2.end - linkSpec2.start) {
                    n3 = n2 + 1;
                } else if (linkSpec.end - linkSpec.start < linkSpec2.end - linkSpec2.start) {
                    n3 = n2;
                }
                if (n3 != -1) {
                    URLSpan uRLSpan = arrayList.get((int)n3).frameworkAddedSpan;
                    if (uRLSpan != null) {
                        spannable.removeSpan((Object)uRLSpan);
                    }
                    arrayList.remove(n3);
                    --n;
                    continue;
                }
            }
            ++n2;
        }
    }

    private static class LinkSpec {
        int end;
        URLSpan frameworkAddedSpan;
        int start;
        String url;

        LinkSpec() {
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface LinkifyMask {
    }

}

