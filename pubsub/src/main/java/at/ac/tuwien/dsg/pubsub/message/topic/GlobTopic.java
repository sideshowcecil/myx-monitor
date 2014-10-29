package at.ac.tuwien.dsg.pubsub.message.topic;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements {@link Topic} based on the glob pattern.
 * 
 * @author bernd.rathmanner
 * 
 */
public final class GlobTopic extends RegexTopic {

    /**
     * Constructor.
     * 
     * @param pattern
     */
    public GlobTopic(String pattern) {
        super(globToRegexp(pattern));
    }

    /**
     * Convert a glob pattern into a regular expression.
     * 
     * @param globPattern
     * @return
     */
    private static final String globToRegexp(String globPattern) {
        StringBuilder buffer = new StringBuilder();

        int length = globPattern.length();
        for (int i = 0; i < length; ++i) {
            char c = globPattern.charAt(i);

            switch (c) {
            case '*':
                buffer.append(".*");
                break;

            case '?':
                buffer.append('.');
                break;

            case '[': {
                int j = findClosingSquareBracket(globPattern, i);
                if (j <= i + 1) {
                    // Something like "[" or "[]" has no special meaning.
                    buffer.append("\\[");
                } else {
                    buffer.append(globPattern.substring(i, j + 1));
                    i = j;
                }
            }
                break;

            case '{': {
                List<String> parts = new ArrayList<String>();
                int j = splitCurlyBraceGroup(globPattern, i, parts);
                int partCount = parts.size();
                if (j <= i + 1 || partCount == 0) {
                    // Something like "{", "{}", "{,}", or "{,,}" has no
                    // special meaning.
                    buffer.append("\\{");
                } else {
                    if (partCount == 1) {
                        // Not very useful but why not?
                        buffer.append('(');
                        buffer.append(globToRegexp(parts.get(0)));
                        buffer.append(')');
                    } else {
                        buffer.append('(');
                        for (int k = 0; k < partCount; ++k) {
                            if (k > 0) {
                                buffer.append('|');
                            }
                            buffer.append('(');
                            buffer.append(globToRegexp(parts.get(k)));
                            buffer.append(')');
                        }
                        buffer.append(')');
                    }
                    i = j;
                }
            }
                break;

            case '\\':
                // Escaped char: add as is (that is, escaped).
                buffer.append(c);
                if (i + 1 < length) {
                    buffer.append(globPattern.charAt(++i));
                }
                break;

            default:
                if (!Character.isLetterOrDigit(c)) {
                    // Escape special chars such as '(' or '|'.
                    buffer.append('\\');
                }
                buffer.append(c);
            }
        }

        return buffer.toString();
    }

    /**
     * Find the closing bracket in a the given string.
     * 
     * @param s
     * @param offset
     * @return
     */
    private static int findClosingSquareBracket(String s, int offset) {
        int nesting = 0;
        char prevC = '\0';
        int length = s.length();

        for (int i = offset; i < length; ++i) {
            char c = s.charAt(i);

            switch (c) {
            case '[':
                if (prevC != '\\') {
                    ++nesting;
                }
                break;
            case ']':
                if (prevC != '\\' &&
                // Something like "[]a-b]" is equivalent to "[\]a-b]".
                        i != offset + 1) {
                    --nesting;
                }

                if (nesting == 0) {
                    return i;
                }
                break;
            }

            prevC = c;
        }

        return -1;
    }

    /**
     * Split a curly braced group.
     * 
     * @param s
     * @param offset
     * @param parts
     * @return
     */
    private static int splitCurlyBraceGroup(String s, int offset, List<String> parts) {
        int groupOffset = offset;
        int nesting = 0;
        char prevC = '\0';
        int length = s.length();

        for (int i = offset; i < length; ++i) {
            char c = s.charAt(i);

            switch (c) {
            case '{':
                if (prevC != '\\') {
                    ++nesting;
                }
                break;
            case '}':
                if (prevC != '\\') {
                    --nesting;
                }

                if (nesting == 0) {
                    String part = s.substring(groupOffset + 1, i);
                    if (part.length() > 0) {
                        parts.add(part);
                    }

                    return i;
                }
                break;
            case ',':
                if (nesting == 1) {
                    String part = s.substring(groupOffset + 1, i);
                    if (part.length() > 0) {
                        parts.add(part);
                    }

                    groupOffset = i;
                }
                break;
            }

            prevC = c;
        }

        return -1;
    }

}
