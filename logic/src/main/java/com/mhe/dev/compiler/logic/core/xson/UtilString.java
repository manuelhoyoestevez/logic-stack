package com.mhe.dev.compiler.logic.core.xson;

/**
 * UtilString.
 */
public class UtilString
{

    /**
     * Parse String.
     *
     * @param unparsed Unparsed String
     * @return Parsed String
     */
    public static String parseString(String unparsed)
    {
        StringBuilder parsed = new StringBuilder();

        if (unparsed.length() < 2)
        {
            return null;
        }

        if (unparsed.charAt(0) != '"')
        {
            return null;
        }

        if (unparsed.charAt(unparsed.length() - 1) != '"')
        {
            return null;
        }

        for (int i = 1; i < unparsed.length() - 1; i++)
        {
            char current = unparsed.charAt(i);
            if (current != '\\')
            {
                parsed.append(current);
                continue;
            }

            char next = unparsed.charAt(++i);
            switch (next)
            {
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
                    parsed.append(next);
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
    public static String escapeString(String unescaped)
    {

        StringBuilder escaped = new StringBuilder("\"");

        for (int i = 0; i < unescaped.length(); i++)
        {
            char current = unescaped.charAt(i);
            switch (current)
            {
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
}

