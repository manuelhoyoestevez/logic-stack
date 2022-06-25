package com.mhe.dev.logic.stack.core.compiler.mhe;

/**
 * UtilString.
 */
public class UtilString {

    /**
     * Parse String.
     *
     * @param unparsed Unparsed String
     * @return Parsed String
     */
    public static String parseString(String unparsed) {
        StringBuilder parsed = new StringBuilder();

        if (unparsed.length() < 2) {
            return null;
        }

        if (unparsed.charAt(0) != '"') {
            return null;
        }

        if (unparsed.charAt(unparsed.length() - 1) != '"') {
            return null;
        }

        for (int i = 1; i < unparsed.length() - 1; i++) {
            char current = unparsed.charAt(i);
            if (current == '\\') {
                current = unparsed.charAt(++i);
                switch (current) {
                    case 'r':
                        parsed.append('\r');
                        break;
                    case 'n':
                        parsed.append('\n');
                        break;
                    case 'f':
                        parsed.append('\f');
                        break;
                    case 't':
                        parsed.append('\t');
                        break;
                    default:
                        parsed.append(current);
                }
            } else {
                parsed.append(current);
            }
        }

        return parsed.toString();
    }

    /**
     * Escape String.
     *
     * @param unescaped Unescaped String
     * @return Escaped String
     */
    public static String escapeString(String unescaped) {

        StringBuilder escaped = new StringBuilder("\"");

        for (int i = 0; i < unescaped.length(); i++) {
            char current = unescaped.charAt(i);
            switch (current) {
                case '\r':
                    escaped.append("\\r");
                    break;
                case '\n':
                    escaped.append("\\n");
                    break;
                case '\f':
                    escaped.append("\\f");
                    break;
                case '\t':
                    escaped.append("\\t");
                    break;
                case '\\':
                    escaped.append("\\\\");
                    break;
                default:
                    escaped.append(current);
            }
        }

        return escaped + "\"";
    }

    /*
     *
     * unescape_perl_string()
     *
     *      Tom Christiansen <tchrist@perl.com>
     *      Sun Nov 28 12:55:24 MST 2010
     *
     * It's completely ridiculous that there's no standard
     * unescape_java_string function.  Since I have to do the
     * damn thing myself, I might as well make it halfway useful
     * by supporting things Java was too stupid to consider in
     * strings:
     *
     *   => "?" items  are additions to Java string escapes
     *                 but normal in Java regexes
     *
     *   => "!" items  are also additions to Java regex escapes
     *
     * Standard singletons: ?\a ?\e \f \n \r \t
     *
     *      NB: \b is unsupported as backspace so it can pass-through
     *          to the regex translator untouched; I refuse to make anyone
     *          doublebackslash it as doublebackslashing is a Java idiocy
     *          I desperately wish would die out.  There are plenty of
     *          other ways to write it:
     *
     *              \cH, \12, \012, \x08 \x{8}, \u0008, \U00000008
     *
     * Octal escapes: \0 \0N \0NN \N \NN \NNN
     *    Can range up to !\777 not \377
     *
     *      TODO: add !\o{NNNNN}
     *          last Unicode is 4177777
     *          maxint is 37777777777
     *
     * Control chars: ?\cX
     *      Means: ord(X) ^ ord('@')
     *
     * Old hex escapes: \xXX
     *      unbraced must be 2 xdigits
     *
     * Perl hex escapes: !\x{XXX} braced may be 1-8 xdigits
     *       NB: proper Unicode never needs more than 6, as highest
     *           valid codepoint is 0x10FFFF, not maxint 0xFFFFFFFF
     *
     * Lame Java escape: \[IDIOT JAVA PREPROCESSOR]uXXXX must be
     *                   exactly 4 xdigits;
     *
     *       I can't write XXXX in this comment where it belongs
     *       because the damned Java Preprocessor can't mind its
     *       own business.  Idiots!
     *
     * Lame Python escape: !\UXXXXXXXX must be exactly 8 xdigits
     *
     * TODO: Perl translation escapes: \Q \U \L \E \[IDIOT JAVA PREPROCESSOR]u \l
     *       These are not so important to cover if you're passing the
     *       result to Pattern.compile(), since it handles them for you
     *       further downstream.  Hm, what about \[IDIOT JAVA PREPROCESSOR]u?
     *
     */

    /**
     * Unescape String.
     *
     * @param escapedString Escaped String.
     * @return Unescaped String.
     */
    @SuppressWarnings("checkstyle:FallThrough")
    public static String unescapeString(String escapedString) {

        /*
         * In contrast to fixing Java's broken regex charclasses,
         * this one need be no bigger, as unescaping shrinks the string
         * here, where in the other one, it grows it.
         */

        StringBuilder unescapedString = new StringBuilder(escapedString.length());

        boolean sawBackslash = false;

        for (int i = 0; i < escapedString.length(); i++) {
            int cp = escapedString.codePointAt(i);
            if (escapedString.codePointAt(i) > Character.MAX_VALUE) {
                i++;
            }

            if (!sawBackslash) {
                if (cp == '\\') {
                    sawBackslash = true;
                } else {
                    unescapedString.append(Character.toChars(cp));
                }
                continue; /* switch */
            }

            if (cp == '\\') {
                sawBackslash = false;
                unescapedString.append('\\');
                unescapedString.append('\\');
                continue; /* switch */
            }

            switch (cp) {

                case 'r':
                    unescapedString.append('\r');
                    break; /* switch */

                case 'n':
                    unescapedString.append('\n');
                    break; /* switch */

                case 'f':
                    unescapedString.append('\f');
                    break; /* switch */

                /* PASS a \b THROUGH!! */
                case 'b':
                    unescapedString.append("\\b");
                    break; /* switch */

                case 't':
                    unescapedString.append('\t');
                    break; /* switch */

                case 'a':
                    unescapedString.append('\007');
                    break; /* switch */

                case 'e':
                    unescapedString.append('\033');
                    break; /* switch */

                /*
                 * A "control" character is what you get when you xor its
                 * codepoint with '@'==64.  This only makes sense for ASCII,
                 * and may not yield a "control" character after all.
                 *
                 * Strange but true: "\c{" is ";", "\c}" is "=", etc.
                 */
                case 'c': {
                    if (++i == escapedString.length()) {
                        die("trailing \\c");
                    }
                    cp = escapedString.codePointAt(i);
                    /*
                     * don't need to grok surrogates, as next line blows them up
                     */
                    if (cp > 0x7f) {
                        die("expected ASCII after \\c");
                    }
                    unescapedString.append(Character.toChars(cp ^ 64));
                    break; /* switch */
                }

                case '8':
                case '9':
                    die("illegal octal digit");
                    /* NOTREACHED */

                    /*
                     * may be 0 to 2 octal digits following this one
                     * so back up one for fallthrough to next case;
                     * unread this digit and fall through to next case.
                     */
                    break;
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                    --i;
                    /* FALLTHROUGH */

                    /*
                     * Can have 0, 1, or 2 octal digits following a 0
                     * this permits larger values than octal 377, up to
                     * octal 777.
                     */
                case '0': {
                    if (i + 1 == escapedString.length()) {
                        /* found \0 at end of string */
                        unescapedString.append(Character.toChars(0));
                        break; /* switch */
                    }
                    i++;
                    int digits = 0;
                    int j;
                    for (j = 0; j <= 2; j++) {
                        if (i + j == escapedString.length()) {
                            break; /* for */
                        }
                        /* safe because will unread surrogate */
                        int ch = escapedString.charAt(i + j);
                        if (ch < '0' || ch > '7') {
                            break; /* for */
                        }
                        digits++;
                    }
                    if (digits == 0) {
                        --i;
                        unescapedString.append('\0');
                        break; /* switch */
                    }
                    int value = 0;
                    try {
                        value = Integer.parseInt(
                                escapedString.substring(i, i + digits), 8);
                    } catch (NumberFormatException nfe) {
                        die("invalid octal value for \\0 escape");
                    }
                    unescapedString.append(Character.toChars(value));
                    i += digits - 1;
                    break; /* switch */
                } /* end case '0' */

                case 'x': {
                    if (i + 2 > escapedString.length()) {
                        die("string too short for \\x escape");
                    }
                    i++;
                    boolean sawBrace = false;
                    if (escapedString.charAt(i) == '{') {
                        /* ^^^^^^ ok to ignore surrogates here */
                        i++;
                        sawBrace = true;
                    }
                    int j;
                    for (j = 0; j < 8; j++) {

                        if (!sawBrace && j == 2) {
                            break;  /* for */
                        }

                        /*
                         * ASCII test also catches surrogates
                         */
                        int ch = escapedString.charAt(i + j);
                        if (ch > 127) {
                            die("illegal non-ASCII hex digit in \\x escape");
                        }

                        if (sawBrace && ch == '}') {
                            break; /* for */
                        }

                        if (!((ch >= '0' && ch <= '9')
                                ||
                                (ch >= 'a' && ch <= 'f')
                                ||
                                (ch >= 'A' && ch <= 'F')
                        )
                        ) {
                            die(String.format(
                                    "illegal hex digit #%d '%c' in \\x", ch, ch));
                        }

                    }
                    if (j == 0) {
                        die("empty braces in \\x{} escape");
                    }
                    int value = 0;
                    try {
                        value = Integer.parseInt(escapedString.substring(i, i + j), 16);
                    } catch (NumberFormatException nfe) {
                        die("invalid hex value for \\x escape");
                    }
                    unescapedString.append(Character.toChars(value));
                    if (sawBrace) {
                        j++;
                    }
                    i += j - 1;
                    break; /* switch */
                }

                case 'u': {
                    if (i + 4 > escapedString.length()) {
                        die("string too short for \\u escape");
                    }
                    i++;
                    int j;
                    for (j = 0; j < 4; j++) {
                        /* this also handles the surrogate issue */
                        if (escapedString.charAt(i + j) > 127) {
                            die("illegal non-ASCII hex digit in \\u escape");
                        }
                    }
                    int value = 0;
                    try {
                        value = Integer.parseInt(escapedString.substring(i, i + j), 16);
                    } catch (NumberFormatException nfe) {
                        die("invalid hex value for \\u escape");
                    }
                    unescapedString.append(Character.toChars(value));
                    i += j - 1;
                    break; /* switch */
                }

                case 'U': {
                    if (i + 8 > escapedString.length()) {
                        die("string too short for \\U escape");
                    }
                    i++;
                    int j;
                    for (j = 0; j < 8; j++) {
                        /* this also handles the surrogate issue */
                        if (escapedString.charAt(i + j) > 127) {
                            die("illegal non-ASCII hex digit in \\U escape");
                        }
                    }
                    int value = 0;
                    try {
                        value = Integer.parseInt(escapedString.substring(i, i + j), 16);
                    } catch (NumberFormatException nfe) {
                        die("invalid hex value for \\U escape");
                    }
                    unescapedString.append(Character.toChars(value));
                    i += j - 1;
                    break; /* switch */
                }

                default:
                    unescapedString.append('\\');
                    unescapedString.append(Character.toChars(cp));
                    /*
                     * say(String.format(
                     *       "DEFAULT unrecognized escape %c passed through",
                     *       cp));
                     */
                    break; /* switch */

            }
            sawBackslash = false;
        }

        /* weird to leave one at the end */
        if (sawBackslash) {
            unescapedString.append('\\');
        }

        return unescapedString.toString();
    }

    private static void die(String foa) {
        throw new IllegalArgumentException(foa);
    }
}

