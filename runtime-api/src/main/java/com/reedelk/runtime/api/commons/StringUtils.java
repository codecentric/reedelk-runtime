package com.reedelk.runtime.api.commons;

public class StringUtils {

    public static final String EMPTY = "";

    public static boolean isNotNull(final CharSequence sequence) {
        return sequence != null;
    }

    public static boolean isNull(final CharSequence sequence) {
        return sequence == null;
    }

    public static boolean isBlank(final CharSequence sequence) {
        if (sequence == null) return true;

        int sequenceLength = sequence.length();
        if (sequenceLength == 0) return true;

        for (int i = 0; i < sequenceLength; i++) {
            if (!Character.isWhitespace(sequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(final CharSequence sequence) {
        return !isBlank(sequence);
    }

    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    public static String escapeQuotes(String str) {
        return str == null ?
                null :
                str.replace("\'", "\\'");
    }
}
