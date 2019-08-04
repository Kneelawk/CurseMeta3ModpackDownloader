package com.github.kneelawk.cursemodpackdownloader.cursemeta3.net;

import org.apache.commons.codec.DecoderException;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.BitSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurseURIUtils {
    public static final Pattern FULL_URI_PATTERN = Pattern.compile(
            "^(?<scheme>[^\\/]+):\\/\\/(?<host>[^\\/]+)(?<path>\\/.*)?$");
    public static final String FULL_URI_FORMAT = "%s://%s%s";
    public static final Pattern HOSTLESS_URI_PATTERN =
            Pattern.compile("^(?<scheme>[^\\/]+):\\/\\/(?<path>\\/.*)$");
    public static final String HOSTLESS_URI_FORMAT = "%s://%s";
    public static final Pattern HOST_RELATIVE_URI_PATTERN =
            Pattern.compile("^\\/\\/(?<host>[^\\/]+)(?<path>\\/.*)?$");
    public static final String HOST_RELATIVE_URI_FORMAT = "//%s%s";
    public static final Pattern HOSTLESS_RELATIVE_URI_PATTERN =
            Pattern.compile("^\\/\\/(?<path>\\/.*)$");
    public static final String HOSTLESS_RELATIVE_URI_FORMAT = "//%s";
    public static final Pattern PATH_RELATIVE_URI_PATTERN =
            Pattern.compile("^(?<path>\\/.*)$");
    public static final String PATH_RELATIVE_URI_FORMAT = "%s";

    public static final Charset UTF_8 = Charset.forName("UTF-8");

    /*
     * ### COPIED FROM ### Apache Commons Http Client:
     * org.apache.http.client.utils.URLEncodedUtils.java
     */

    /**
     * Unreserved characters, i.e. alphanumeric, plus: {@code _ - ! . ~ ' ( ) *}
     * <p>
     * This list is the same as the {@code unreserved} list in
     * <a href="http://www.ietf.org/rfc/rfc2396.txt">RFC 2396</a>
     */
    public static final BitSet UNRESERVED = new BitSet(256);
    /**
     * Punctuation characters: , ; : $ & + =
     * <p>
     * These are the additional characters allowed by userinfo.
     */
    public static final BitSet PUNCT = new BitSet(256);
    /**
     * Characters which are safe to use in userinfo, i.e. {@link #UNRESERVED}
     * plus {@link #PUNCT}uation
     */
    public static final BitSet USERINFO = new BitSet(256);
    /**
     * Characters which are safe to use in a path, i.e. {@link #UNRESERVED} plus
     * {@link #PUNCT}uation plus / @
     */
    public static final BitSet PATHSAFE = new BitSet(256);
    /**
     * Characters which are safe to use in an encoded path, i.e.
     * {@link #PATHSAFE} plus %
     */
    public static final BitSet ENCPATHSAFE = new BitSet(256);
    /**
     * Characters which are safe to use in a query or a fragment, i.e.
     * {@link #RESERVED} plus {@link #UNRESERVED}
     */
    public static final BitSet URIC = new BitSet(256);

    /**
     * Reserved characters, i.e. {@code ;/?:@&=+$,[]}
     * <p>
     * This list is the same as the {@code reserved} list in
     * <a href="http://www.ietf.org/rfc/rfc2396.txt">RFC 2396</a> as augmented
     * by <a href="http://www.ietf.org/rfc/rfc2732.txt">RFC 2732</a>
     */
    public static final BitSet RESERVED = new BitSet(256);

    /**
     * Safe characters for x-www-form-urlencoded data, as per
     * java.net.URLEncoder and browser behaviour, i.e. alphanumeric plus
     * {@code "-", "_", ".", "*"}
     */
    public static final BitSet URLENCODER = new BitSet(256);

    static {
        // unreserved chars
        // alpha characters
        for (int i = 'a'; i <= 'z'; i++) {
            UNRESERVED.set(i);
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            UNRESERVED.set(i);
        }
        // numeric characters
        for (int i = '0'; i <= '9'; i++) {
            UNRESERVED.set(i);
        }
        UNRESERVED.set('_'); // these are the charactes of the "mark" list
        UNRESERVED.set('-');
        UNRESERVED.set('.');
        UNRESERVED.set('*');
        URLENCODER.or(UNRESERVED); // skip remaining unreserved characters
        UNRESERVED.set('!');
        UNRESERVED.set('~');
        UNRESERVED.set('\'');
        UNRESERVED.set('(');
        UNRESERVED.set(')');
        // punct chars
        PUNCT.set(',');
        PUNCT.set(';');
        PUNCT.set(':');
        PUNCT.set('$');
        PUNCT.set('&');
        PUNCT.set('+');
        PUNCT.set('=');
        // Safe for userinfo
        USERINFO.or(UNRESERVED);
        USERINFO.or(PUNCT);

        // URL path safe
        PATHSAFE.or(UNRESERVED);
        PATHSAFE.set('/'); // segment separator
        PATHSAFE.set(';'); // param separator
        PATHSAFE.set(':'); // rest as per list in 2396, i.e. : @ & = + $ ,
        PATHSAFE.set('@');
        PATHSAFE.set('&');
        PATHSAFE.set('=');
        PATHSAFE.set('+');
        PATHSAFE.set('$');
        PATHSAFE.set(',');

        ENCPATHSAFE.or(PATHSAFE);
        ENCPATHSAFE.set('%');

        RESERVED.set(';');
        RESERVED.set('/');
        RESERVED.set('?');
        RESERVED.set(':');
        RESERVED.set('@');
        RESERVED.set('&');
        RESERVED.set('=');
        RESERVED.set('+');
        RESERVED.set('$');
        RESERVED.set(',');
        RESERVED.set('['); // added by RFC 2732
        RESERVED.set(']'); // added by RFC 2732

        URIC.or(RESERVED);
        URIC.or(UNRESERVED);
    }

    private static final int RADIX = 16;

    /*
     * ### END OF COPY ###
     */

    public static URI sanitizeCurseDownloadUri(String insaneUri,
                                               boolean escapePath) throws URISyntaxException, DecoderException {
        return sanitizeUri("https", "files.forgecdn.net", "/files/", insaneUri,
                escapePath);
    }

    public static URI sanitizeUri(String insaneUri, boolean escapePath)
            throws URISyntaxException, DecoderException {
        return sanitizeUri(null, null, null, insaneUri, escapePath);
    }

    public static URI sanitizeUri(String baseScheme, String baseHost,
                                  String basePath, String insaneUri, boolean escapePath)
            throws URISyntaxException, DecoderException {
        Matcher fullUri = FULL_URI_PATTERN.matcher(insaneUri);
        Matcher hostlessUri = HOSTLESS_URI_PATTERN.matcher(insaneUri);
        Matcher hostRelativeUri = HOST_RELATIVE_URI_PATTERN.matcher(insaneUri);
        Matcher hostlessRelativeUri =
                HOSTLESS_RELATIVE_URI_PATTERN.matcher(insaneUri);
        Matcher pathRelativeUri = PATH_RELATIVE_URI_PATTERN.matcher(insaneUri);

        if (fullUri.matches()) {
            String scheme = fullUri.group("scheme");
            String host = fullUri.group("host");
            String path = fullUri.group("path");
            if (escapePath) {
                path = urlEncode(path, UTF_8, ENCPATHSAFE, false);
            }
            return new URI(String.format(FULL_URI_FORMAT, scheme, host, path));
        } else if (hostlessUri.matches()) {
            String scheme = fullUri.group("scheme");
            String path = hostlessUri.group("path");
            if (escapePath) {
                path = urlEncode(path, UTF_8, ENCPATHSAFE, false);
            }
            return new URI(String.format(HOSTLESS_URI_FORMAT, scheme, path));
        } else if (hostRelativeUri.matches()) {
            String host = fullUri.group("host");
            String path = hostRelativeUri.group("path");
            if (escapePath) {
                path = urlEncode(path, UTF_8, ENCPATHSAFE, false);
            }
            return new URI(
                    String.format(FULL_URI_FORMAT, baseScheme, host, path));
        } else if (hostlessRelativeUri.matches()) {
            String path = hostlessRelativeUri.group("path");
            if (escapePath) {
                path = urlEncode(path, UTF_8, ENCPATHSAFE, false);
            }
            return new URI(
                    String.format(HOSTLESS_URI_FORMAT, baseScheme, path));
        } else if (pathRelativeUri.matches()) {
            String path = pathRelativeUri.group("path");
            if (escapePath) {
                path = urlEncode(path, UTF_8, ENCPATHSAFE, false);
            }
            return new URI(
                    String.format(FULL_URI_FORMAT, baseScheme, baseHost, path));
        } else {
            return new URI(baseScheme, baseHost, basePath, null).resolve(
                    escapePath ? urlEncode(insaneUri, UTF_8, ENCPATHSAFE, false)
                            : insaneUri);
        }
    }

    /*
     * ### COPIED FROM ### Apache Commons Http Client:
     * org.apache.http.client.utils.URLEncodedUtils.java
     */

    public static String urlEncode(final String content, final Charset charset,
                                   final BitSet safechars, final boolean blankAsPlus) {
        if (content == null) {
            return null;
        }
        final StringBuilder buf = new StringBuilder();
        final ByteBuffer bb = charset.encode(content);
        while (bb.hasRemaining()) {
            final int b = bb.get() & 0xff;
            if (safechars.get(b)) {
                buf.append((char) b);
            } else if (blankAsPlus && b == ' ') {
                buf.append('+');
            } else {
                buf.append("%");
                final char hex1 = Character
                        .toUpperCase(Character.forDigit((b >> 4) & 0xF, RADIX));
                final char hex2 = Character
                        .toUpperCase(Character.forDigit(b & 0xF, RADIX));
                buf.append(hex1);
                buf.append(hex2);
            }
        }
        return buf.toString();
    }

    /**
     * Decode/unescape a portion of a URL, to use with the query part ensure
     * {@code plusAsBlank} is true.
     *
     * @param content     the portion to decode
     * @param charset     the charset to use
     * @param plusAsBlank if {@code true}, then convert '+' to space (e.g. for
     *                    www-url-form-encoded content), otherwise leave as is.
     * @return encoded string
     */
    public static String urlDecode(final String content, final Charset charset,
                                   BitSet safechars, final boolean plusAsBlank) {
        if (content == null) {
            return null;
        }
        final ByteBuffer bb = ByteBuffer.allocate(content.length());
        final CharBuffer cb = CharBuffer.wrap(content);
        while (cb.hasRemaining()) {
            final char c = cb.get();
            if (c == '%' && cb.remaining() >= 2) {
                final char uc = cb.get();
                final char lc = cb.get();
                final int u = Character.digit(uc, 16);
                final int l = Character.digit(lc, 16);
                final byte nch = (byte) ((u << 4) + l);
                if (u != -1 && l != -1 && !safechars.get(nch)) {
                    bb.put(nch);
                } else {
                    bb.put((byte) '%');
                    bb.put((byte) uc);
                    bb.put((byte) lc);
                }
            } else if (plusAsBlank && c == '+') {
                bb.put((byte) ' ');
            } else {
                bb.put((byte) c);
            }
        }
        bb.flip();
        return charset.decode(bb).toString();
    }

    /*
     * ### END OF COPY ###
     */
}
