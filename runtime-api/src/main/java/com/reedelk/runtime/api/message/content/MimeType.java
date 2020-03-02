package com.reedelk.runtime.api.message.content;


import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.*;

@AutocompleteType
public class MimeType implements Serializable {

    private static final String CHARSET_PARAM = "charset";

    private final String subType;
    private final String charset;
    private final String primaryType;
    private final Map<String, String> params;
    private final List<String> fileExtensions;

    public static MimeType fromFileExtension(String extension) {
        if (EXTENSION_MIME_TYPE_MAP.containsKey(extension)) {
            return EXTENSION_MIME_TYPE_MAP.get(extension);
        }
        return MimeType.UNKNOWN;
    }

    public static MimeType of(String primaryType, String subType, String charset) {
        return new MimeType(primaryType, subType, charset, null, null);
    }

    public static MimeType of(String primaryType, String subType) {
        return new MimeType(primaryType, subType, null, null, null);
    }

    public static MimeType of(String primaryType, String subType, List<String> fileExtensions) {
        return new MimeType(primaryType, subType, null, null, fileExtensions);
    }

    public static MimeType of(String primaryType, String subType, Map<String, String> params) {
        return new MimeType(primaryType, subType, null, params, null);
    }

    public static MimeType of(MimeType mimeType, String charset) {
        return new MimeType(mimeType.primaryType, mimeType.subType, charset, null, null);
    }

    public static MimeType parse(String mimeType) {
        return parse(mimeType, UNKNOWN);
    }

    public static MimeType parse(String mimeType, MimeType defaultMimeType) {
        if (mimeType == null) return defaultMimeType;
        try {
            return _parse(mimeType);
        } catch (Exception e) {
            return defaultMimeType;
        }
    }

    public MimeType(String primaryType, String subType, String charset, Map<String, String> params, List<String> fileExtensions) {
        this.subType = subType;
        this.charset = charset;
        this.primaryType = primaryType;
        this.params = params != null ? unmodifiableMap(params) : emptyMap();
        this.fileExtensions = fileExtensions != null ? unmodifiableList(fileExtensions) : emptyList();
    }

    @AutocompleteItem(signature = "getSubType()", description = "Returns the sub-type of the mime type.")
    public String getSubType() {
        return subType;
    }

    public Optional<Charset> getCharset() {
        return Optional.ofNullable(getCharset(charset, params));
    }

    @AutocompleteItem(signature = "getPrimaryType()", description = "Returns the primary type.")
    public String getPrimaryType() {
        return primaryType;
    }

    @AutocompleteItem(signature = "getFileExtensions()", description = "Returns a list of file extensions associated to this mime type.")
    public List<String> getFileExtensions() {
        return fileExtensions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MimeType mimeType = (MimeType) o;
        return Objects.equals(subType, mimeType.subType) &&
                Objects.equals(primaryType, mimeType.primaryType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subType, primaryType);
    }

    private static MimeType _parse(String mimeType) {
        String[] parts = mimeType.split(";");
        Map<String, String> params = new HashMap<>();

        for (int i = 1; i < parts.length; ++i) {
            String p = parts[i];
            String[] subParts = p.split("=");
            if (subParts.length == 2)
                params.put(subParts[0].trim(), subParts[1].trim());
        }
        String fullType = parts[0].trim();

        if (fullType.equals("*")) fullType = "*/*";

        String[] types = fullType.split("/");
        String primaryType = types[0].trim();
        String subType = types[1].trim();

        return of(primaryType, subType, params);
    }

    private Charset getCharset(String given, Map<String, String> params) {
        if (given != null) return Charset.forName(given);
        return params.containsKey(CHARSET_PARAM) ? Charset.forName(params.get(CHARSET_PARAM)) : null;
    }

    // Text Mime Types

    public static final MimeType TEXT_CSS = of("text", "css", singletonList("css"));
    public static final MimeType TEXT_HTML = of("text", "html", asList("htm", "html", "stm"));
    public static final MimeType TEXT = of("text", "plain", asList("bas", "c", "h", "java", "txt"));
    public static final MimeType TEXT_RICH = of("text", "richtext", singletonList("rtx"));
    public static final MimeType TEXT_SCRIPTLET = of("text", "scriptlet", singletonList("sct"));
    public static final MimeType TEXT_TAB_SEPARATED_VALUES = of("text", "tab-separated-values", singletonList("tsv"));
    public static final MimeType TEXT_COMMA_SEPARATED_VALUES = of("text", "csv", singletonList("csv"));
    public static final MimeType TEXT_HYPERTEXT_TEMPLATE_FILE = of("text", "webviewhtml", singletonList("htt"));
    public static final MimeType TEXT_HTML_COMPONENT_FILE = of("text", "x-component", singletonList("htc"));
    public static final MimeType TEXT_TEX_FONT_ENCLOSING_FILE = of("text", "x-setext", singletonList("etx"));
    public static final MimeType TEXT_VCARD_FILE = of("text", "x-vcard", singletonList("vcf"));
    public static final MimeType TEXT_XML = of("text", "xml", singletonList("xml"));
    public static final MimeType TEXT_JSON = of("text", "json", singletonList("json"));
    public static final MimeType TEXT_JAVASCRIPT = of("text", "javascript", singletonList("js"));


    // Images Mime Types

    public static final MimeType IMAGE_BITMAP = of("image", "bmp", singletonList("bmp"));
    public static final MimeType IMAGE_GIF = of("image", "gif", singletonList("gif"));
    public static final MimeType IMAGE_PNG = of("image", "png", singletonList("png"));
    public static final MimeType IMAGE_JPEG = of("image", "jpeg", asList("jpe", "jpeg", "jpg"));
    public static final MimeType IMAGE_JPEG_INTERCHANGE = of("image", "pipeg", singletonList("jfif"));
    public static final MimeType IMAGE_SVG = of("image", "svg+xml", singletonList("svg"));
    public static final MimeType IMAGE_TIFF = of("image", "tiff", asList("tiff", "tif"));
    public static final MimeType IMAGE_SUN_RASTER_GRAPHIC = of("image", "x-cmu-raster", singletonList("ras"));
    public static final MimeType IMAGE_COREL_METAFILE_EXCHANGE_FILE = of("image", "x-cmx", singletonList("cmx"));
    public static final MimeType IMAGE_ICON = of("image", "x-icon", singletonList("ico"));
    public static final MimeType IMAGE_PORTABLE_ANY_MAP_IMAGE = of("image", "x-portable-anymap", singletonList("pnm"));
    public static final MimeType IMAGE_PORTABLE_BITMAP_IMAGE = of("image", "x-portable-bitmap", singletonList("pbm"));
    public static final MimeType IMAGE_PORTABLE_GRAYMAP_IMAGE = of("image", "x-portable-graymap", singletonList("pgm"));
    public static final MimeType IMAGE_PORTABLE_PIXMAP_IMAGE = of("image", "x-portable-pixmap", singletonList("ppm"));
    public static final MimeType IMAGE_RGB_BITMAP = of("image", "x-rgb", singletonList("rgb"));
    public static final MimeType IMAGE_X11_BITMAP = of("image", "x-xbitmap", singletonList("xbm"));
    public static final MimeType IMAGE_X11_PIXMAP = of("image", "x-xpixmap", singletonList("xpm"));
    public static final MimeType IMAGE_X_WINDOWS_DUMP = of("image", "x-xwindowdump", singletonList("xwd"));
    public static final MimeType IMAGE_FILE = of("image", "ief", singletonList("ief"));

    // Mail Messages Mime Types

    public static final MimeType MESSAGE_RFC822 = of("message", "rfc822", asList("mht", "mhtml", "nws"));

    // Video Mime Types

    public static final MimeType VIDEO_MPEG = of("video", "mpeg", asList("mp2", "mpa", "mpe", "mpeg", "mpg", "mpv2"));
    public static final MimeType VIDEO_MPEG4 = of("video", "mp4", singletonList("mp4"));
    public static final MimeType VIDEO_QUICKTIME = of("video", "quicktime", asList("mov", "qt"));
    public static final MimeType VIDEO_LOGOS_LIBRARY_FILE = of("video", "x-la-asf", asList("lsf", "lsx"));
    public static final MimeType VIDEO_MICROSOFT_ASF = of("video", "x-ms-asf", asList("asf", "asr", "asx"));
    public static final MimeType VIDEO_AVI_FILE = of("video", "x-msvideo", singletonList("avi"));
    public static final MimeType VIDEO_QUICKTIME_MOVIE = of("video", "x-sgi-movie", singletonList("movie"));

    // Audio Mime Types

    public static final MimeType AUDIO_BASIC = of("audio", "basic", asList("au", "snd"));
    public static final MimeType AUDIO_MIDI = of("audio", "mid", asList("mid", "rmi"));
    public static final MimeType AUDIO_MP3 = of("audio", "mpeg", singletonList("mp3"));
    public static final MimeType AUDIO_INTERCHANGE_FORMAT = of("audio", "x-aiff", asList("aif", "aifc", "aiff"));
    public static final MimeType AUDIO_MEDIA_PLAYLIST_FILE = of("audio", "x-mpegurl", singletonList("m3u"));
    public static final MimeType AUDIO_REAL_AUDIO_FILE = of("audio", "x-pn-realaudio", asList("ra", "ram"));
    public static final MimeType AUDIO_WAVE = of("audio", "x-wav", singletonList("wav"));

    // Applications Mime Types

    public static final MimeType APPLICATION_COREL_ENVOY = of("application", "envoy", singletonList("evy"));
    public static final MimeType APPLICATION_FRACTAL_IMAGE_FILE = of("application", "fractals", singletonList("fif"));
    public static final MimeType APPLICATION_WINDOWS_PRINT_SPOOL_FILE = of("application", "futuresplash", singletonList("spl"));
    public static final MimeType APPLICATION_HTA = of("application", "hta", singletonList("hta"));
    public static final MimeType APPLICATION_WORD = of("application", "msword", asList("doc", "dot"));
    public static final MimeType APPLICATION_BINARY = of("application", "octet-stream", asList("*", "bin", "class", "dms", "exe", "jar", "lha", "lzh"));
    public static final MimeType APPLICATION_CALS_RASTER_IMAGE = of("application", "oda", singletonList("oda"));
    public static final MimeType APPLICATION_ACTIVEX_SCRIPT = of("application", "olescript", singletonList("axs"));
    public static final MimeType APPLICATION_ACROBAT_FILE = of("application", "pdf", singletonList("pdf"));
    public static final MimeType APPLICATION_OUTLOOK_PROFILE_FILE = of("application", "pics-rules", singletonList("prf"));
    public static final MimeType APPLICATION_CERTIFICATE_REQUEST_FILE = of("application", "pkcs10", singletonList("p10"));
    public static final MimeType APPLICATION_CERTIFICATE_REVOCATION_LIST_FILE = of("application", "pkix-crl", singletonList("crl"));
    public static final MimeType APPLICATION_ADOBE_ILLUSTRATOR_LIST_FILE = of("application", "postscript", asList("ai", "eps", "ps"));
    public static final MimeType APPLICATION_RICH_TEXT = of("application", "rtf", singletonList("rtf"));
    public static final MimeType APPLICATION_SET_PAYMENT_INITIATION = of("application", "set-payment-initiation", singletonList("setpay"));
    public static final MimeType APPLICATION_SET_REGISTRATION_INITIATION = of("application", "set-registration-initiation", singletonList("setreg"));
    public static final MimeType APPLICATION_EXCEL = of("application", "vnd.ms-excel", asList("xla", "xlc", "xlm", "xls", "xlt", "xlw"));
    public static final MimeType APPLICATION_OUTLOOK = of("application", "vnd.ms-outlook", singletonList("msg"));
    public static final MimeType APPLICATION_CERTIFICATE_STORE_FILE = of("application", "vnd.ms-pkicertstore", singletonList("sst"));
    public static final MimeType APPLICATION_WINDOWS_CATALOG_FILE = of("application", "vnd.ms-pkiseccat", singletonList("cat"));
    public static final MimeType APPLICATION_WINDOWS_STEREOLITHOGRAPHY_FILE = of("application", "vnd.ms-pkistl", singletonList("stl"));
    public static final MimeType APPLICATION_POWERPOINT = of("application", "vnd.ms-powerpoint", asList("pot", "pps", "ppt"));
    public static final MimeType APPLICATION_MICROSOFT_PROJECT = of("application", "vnd.ms-project", singletonList("mpp"));
    public static final MimeType APPLICATION_MICROSOFT_WORKS = of("application", "vnd.ms-works", asList("wcm", "wdb", "wks", "wps"));
    public static final MimeType APPLICATION_WINDOWS_HELP = of("application", "winhlp", singletonList("hlp"));
    public static final MimeType APPLICATION_BINARY_CPIO = of("application", "x-bcpio", singletonList("bcpio"));
    public static final MimeType APPLICATION_COMPUTABLE_DOCUMENT_FORMAT_FILE = of("application", "x-cdf", singletonList("cdf"));
    public static final MimeType APPLICATION_UNIX_COMPRESSED_FILE = of("application", "x-compress", singletonList("z"));
    public static final MimeType APPLICATION_GZIPPED_TAR_FILE = of("application", "x-compressed", singletonList("tgz"));
    public static final MimeType APPLICATION_UNIX_CPIO_ARCHIVE = of("application", "x-cpio", singletonList("cpio"));
    public static final MimeType APPLICATION_PHOTOSHOP_CUSTOM_SHAPE = of("application", "x-csh", singletonList("csh"));
    public static final MimeType APPLICATION_DIRECTOR_FILE = of("application", "x-director", asList("dcr", "dir", "dxr"));
    public static final MimeType APPLICATION_DEVICE_INDIPENDENT_FORMAT_FILE = of("application", "x-dvi", singletonList("dvi"));
    public static final MimeType APPLICATION_GNU_TAR_ARCHIVE = of("application", "x-gtar", singletonList("gtar"));
    public static final MimeType APPLICATION_GNU_ZIPPED_ARCHIVE = of("application", "x-gzip", singletonList("gz"));
    public static final MimeType APPLICATION_HIERARCHICAL_DATA_FORMAT = of("application", "x-hdf", singletonList("hdf"));
    public static final MimeType APPLICATION_INTERNET_SETTINGS_FILE = of("application", "x-internet-signup", asList("ins", "isp"));
    public static final MimeType APPLICATION_JAVASCRIPT = of("application", "x-javascript", singletonList("js"));
    public static final MimeType APPLICATION_LATEX_FILE = of("application", "x-latex", singletonList("latex"));
    public static final MimeType APPLICATION_MICROSOFT_ACCESS_DATABASE = of("application", "x-msaccess", singletonList("mdb"));
    public static final MimeType APPLICATION_WINDOWS_CARDSPACE_FILE = of("application", "x-mscardfile", singletonList("crd"));
    public static final MimeType APPLICATION_CRAZY_TALK_CLIP_FILE = of("application", "x-msclip", singletonList("clp"));
    public static final MimeType APPLICATION_DYNAMIC_LINK_LIBRARY = of("application", "x-msdownload", singletonList("dll"));
    public static final MimeType APPLICATION_MICROSOFT_MEDIA_VIEWER_FILE = of("application", "x-msmediaview", asList("m13", "m14", "mvb"));
    public static final MimeType APPLICATION_WINDOWS_META_FILE = of("application", "x-msmetafile", singletonList("wmf"));
    public static final MimeType APPLICATION_MICROSOFT_MONEY_FILE = of("application", "x-msmoney", singletonList("mny"));
    public static final MimeType APPLICATION_MICROSOFT_PUBLISHER_FILE = of("application", "x-mspublisher", singletonList("pub"));
    public static final MimeType APPLICATION_TURBO_TAX_SCHEDULER_LIST = of("application", "x-msschedule", singletonList("scd"));
    public static final MimeType APPLICATION_FTR_MEDIA_FILE = of("application", "x-msterminal", singletonList("trm"));
    public static final MimeType APPLICATION_MICROSOFT_WRITE_FILE = of("application", "x-mswrite", singletonList("wri"));
    public static final MimeType APPLICATION_NET_COMPUTABLE_DOCUMENT_FORMAT_FILE = of("application", "x-netcdf", asList("cdf", "nc"));
    public static final MimeType APPLICATION_PKCS12_FILE = of("application", "x-pkcs12", asList("p12", "pfx"));
    public static final MimeType APPLICATION_PKCS7_FILE = of("application", "x-pkcs7-certificates", asList("p7b", "spc"));
    public static final MimeType APPLICATION_PKCS7_RESPONSE_FILE = of("application", "x-pkcs7-certreqresp", singletonList("p7r"));
    public static final MimeType APPLICATION_PKCS7_MIME = of("application", "x-pkcs7-mime", asList("p7c", "p7m"));
    public static final MimeType APPLICATION_PKCS7_SIGNATURE = of("application", "x-pkcs7-signature", singletonList("p7s"));
    public static final MimeType APPLICATION_BASH_SHELL_SCRIPT = of("application", "x-sh", singletonList("sh"));
    public static final MimeType APPLICATION_UNIX_SHAR_ARCHIVE = of("application", "x-shar", singletonList("shar"));
    public static final MimeType APPLICATION_FLASH_FILE = of("application", "x-shockwave-flash", singletonList("swf"));
    public static final MimeType APPLICATION_X_TAR = of("application", "x-tar", singletonList("tar"));
    public static final MimeType APPLICATION_X_509 = of("application", "x-x509-ca-cert", asList("cer", "crt", "der"));
    public static final MimeType APPLICATION_ZIP = of("application", "zip", singletonList("zip"));
    public static final MimeType APPLICATION_ATOM = of("application", "atom+xml");
    public static final MimeType APPLICATION_RSS = of("application", "rss+xml");
    public static final MimeType APPLICATION_XML = of("application", "xml");
    public static final MimeType APPLICATION_JSON = of("application", "json");
    public static final MimeType APPLICATION_JAVA = of("application", "java");
    public static final MimeType APPLICATION_FORM_URL_ENCODED = of("application", "x-www-form-urlencoded");

    // Misc

    public static final String MIME_TYPE_ANY = "*/*";
    public static final String MIME_TYPE_TEXT_PLAIN = "text/plain";
    public static final String MIME_TYPE_TEXT_XML = "text/xml";
    public static final String MIME_TYPE_IMAGE_JPEG = "image/jpeg";
    public static final String MIME_TYPE_APPLICATION_JSON = "application/json";
    public static final String MIME_TYPE_APPLICATION_BINARY = "application/octet-stream";

    public static final MimeType ANY = of("*", "*");
    public static final MimeType UNKNOWN = of("content", "unknown");
    public static final MimeType MULTIPART_FORM_DATA = of("multipart", "form-data");


    public static final MimeType[] ALL = new MimeType[]{

            TEXT_CSS,
            TEXT_HTML,
            TEXT,
            TEXT_RICH,
            TEXT_SCRIPTLET,
            TEXT_TAB_SEPARATED_VALUES,
            TEXT_COMMA_SEPARATED_VALUES,
            TEXT_HYPERTEXT_TEMPLATE_FILE,
            TEXT_HTML_COMPONENT_FILE,
            TEXT_TEX_FONT_ENCLOSING_FILE,
            TEXT_VCARD_FILE,
            TEXT_XML,
            TEXT_JSON,
            TEXT_JAVASCRIPT,

            IMAGE_BITMAP,
            IMAGE_GIF,
            IMAGE_PNG,
            IMAGE_JPEG,
            IMAGE_JPEG_INTERCHANGE,
            IMAGE_SVG,
            IMAGE_TIFF,
            IMAGE_SUN_RASTER_GRAPHIC,
            IMAGE_COREL_METAFILE_EXCHANGE_FILE,
            IMAGE_ICON,
            IMAGE_PORTABLE_ANY_MAP_IMAGE,
            IMAGE_PORTABLE_BITMAP_IMAGE,
            IMAGE_PORTABLE_GRAYMAP_IMAGE,
            IMAGE_PORTABLE_PIXMAP_IMAGE,
            IMAGE_RGB_BITMAP,
            IMAGE_X11_BITMAP,
            IMAGE_X11_PIXMAP,
            IMAGE_X_WINDOWS_DUMP,
            IMAGE_FILE,

            MESSAGE_RFC822,

            VIDEO_MPEG,
            VIDEO_MPEG4,
            VIDEO_QUICKTIME,
            VIDEO_LOGOS_LIBRARY_FILE,
            VIDEO_MICROSOFT_ASF,
            VIDEO_AVI_FILE,
            VIDEO_QUICKTIME_MOVIE,

            AUDIO_BASIC,
            AUDIO_MIDI,
            AUDIO_MP3,
            AUDIO_INTERCHANGE_FORMAT,
            AUDIO_MEDIA_PLAYLIST_FILE,
            AUDIO_REAL_AUDIO_FILE,
            AUDIO_WAVE,

            APPLICATION_COREL_ENVOY,
            APPLICATION_FRACTAL_IMAGE_FILE,
            APPLICATION_WINDOWS_PRINT_SPOOL_FILE,
            APPLICATION_HTA,
            APPLICATION_WORD,
            APPLICATION_BINARY,
            APPLICATION_CALS_RASTER_IMAGE,
            APPLICATION_ACTIVEX_SCRIPT,
            APPLICATION_ACROBAT_FILE,
            APPLICATION_OUTLOOK_PROFILE_FILE,
            APPLICATION_CERTIFICATE_REQUEST_FILE,
            APPLICATION_CERTIFICATE_REVOCATION_LIST_FILE,
            APPLICATION_ADOBE_ILLUSTRATOR_LIST_FILE,
            APPLICATION_RICH_TEXT,
            APPLICATION_SET_PAYMENT_INITIATION,
            APPLICATION_SET_REGISTRATION_INITIATION,
            APPLICATION_EXCEL,
            APPLICATION_OUTLOOK,
            APPLICATION_CERTIFICATE_STORE_FILE,
            APPLICATION_WINDOWS_CATALOG_FILE,
            APPLICATION_WINDOWS_STEREOLITHOGRAPHY_FILE,
            APPLICATION_POWERPOINT,
            APPLICATION_MICROSOFT_PROJECT,
            APPLICATION_MICROSOFT_WORKS,
            APPLICATION_WINDOWS_HELP,
            APPLICATION_BINARY_CPIO,
            APPLICATION_COMPUTABLE_DOCUMENT_FORMAT_FILE,
            APPLICATION_UNIX_COMPRESSED_FILE,
            APPLICATION_GZIPPED_TAR_FILE,
            APPLICATION_UNIX_CPIO_ARCHIVE,
            APPLICATION_PHOTOSHOP_CUSTOM_SHAPE,
            APPLICATION_DIRECTOR_FILE,
            APPLICATION_DEVICE_INDIPENDENT_FORMAT_FILE,
            APPLICATION_GNU_TAR_ARCHIVE,
            APPLICATION_GNU_ZIPPED_ARCHIVE,
            APPLICATION_HIERARCHICAL_DATA_FORMAT,
            APPLICATION_INTERNET_SETTINGS_FILE,
            APPLICATION_JAVASCRIPT,
            APPLICATION_LATEX_FILE,
            APPLICATION_MICROSOFT_ACCESS_DATABASE,
            APPLICATION_WINDOWS_CARDSPACE_FILE,
            APPLICATION_CRAZY_TALK_CLIP_FILE,
            APPLICATION_DYNAMIC_LINK_LIBRARY,
            APPLICATION_MICROSOFT_MEDIA_VIEWER_FILE,
            APPLICATION_WINDOWS_META_FILE,
            APPLICATION_MICROSOFT_MONEY_FILE,
            APPLICATION_MICROSOFT_PUBLISHER_FILE,
            APPLICATION_TURBO_TAX_SCHEDULER_LIST,
            APPLICATION_FTR_MEDIA_FILE,
            APPLICATION_MICROSOFT_WRITE_FILE,
            APPLICATION_NET_COMPUTABLE_DOCUMENT_FORMAT_FILE,
            APPLICATION_PKCS12_FILE,
            APPLICATION_PKCS7_FILE,
            APPLICATION_PKCS7_RESPONSE_FILE,
            APPLICATION_PKCS7_MIME,
            APPLICATION_PKCS7_SIGNATURE,
            APPLICATION_BASH_SHELL_SCRIPT,
            APPLICATION_UNIX_SHAR_ARCHIVE,
            APPLICATION_FLASH_FILE,
            APPLICATION_X_TAR,
            APPLICATION_X_509,
            APPLICATION_ZIP,
            APPLICATION_ATOM,
            APPLICATION_RSS,
            APPLICATION_XML,
            APPLICATION_JSON,
            APPLICATION_JAVA,
            APPLICATION_FORM_URL_ENCODED,

            ANY,
            UNKNOWN,
            MULTIPART_FORM_DATA
    };

    public static final String[] ALL_MIME_TYPES = Arrays.stream(ALL).map(MimeType::toString).toArray(String[]::new);

    public static final String MIME_TYPE_PROTOTYPE = "XXXXXXXXXXXXXXXXXXXXXXXXXX";

    private static final Map<String,MimeType> EXTENSION_MIME_TYPE_MAP;
    static {
        Map<String,MimeType> tmp = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        Arrays.stream(ALL).forEach(mimeType ->
                mimeType.getFileExtensions()
                        .forEach(fileExtension -> tmp.put(fileExtension, mimeType)));
        EXTENSION_MIME_TYPE_MAP = Collections.unmodifiableMap(tmp);
    }


    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        this.params.forEach((k, v) ->
                buffer.append("; ")
                        .append(k)
                        .append("=\"")
                        .append(v)
                        .append("\""));
        return this.primaryType + "/" + this.subType + (this.getCharset().isPresent() ? "; charset=" +
                this.getCharset().get().name() : "") +
                (!this.params.isEmpty() ? buffer.toString() : "");
    }
}
