package at.ac.tuwien.infosys.pubsub.message;

import java.util.regex.Pattern;

public class Topic {

    private String name;
    private Pattern pattern;

    public Topic(String topic) {
        this(topic, Type.GLOB);
    }

    public Topic(String name, Type type) {
        this.name = name;
        if (type == Type.GLOB) {
            name = buildPatternFromGlob(name);
        }
        pattern = Pattern.compile(name);
    }

    public String getName() {
        return name;
    }

    public boolean match(String name) {
        return pattern.matcher(name).matches();
    }

    public enum Type {
        REGEX, GLOB
    }

    /**
     * Build a regular expression pattern from a glob-style pattern.
     * 
     * @param glob
     * @return
     */
    private static String buildPatternFromGlob(String glob) {
        StringBuilder pattern = new StringBuilder();
        pattern.append('^');
        boolean esc = false;
        for (int i = 0; i < glob.length(); ++i) {
            final char c = glob.charAt(i);
            switch (c) {
            case '*':
                if (esc) {
                    pattern.append("\\*");
                    esc = false;
                } else {
                    pattern.append(".*");
                }
                break;
            case '?':
                if (esc) {
                    pattern.append("\\?");
                    esc = false;
                } else {
                    pattern.append('.');
                }
                break;
            case '.':
            case '(':
            case ')':
            case '+':
            case '|':
            case '^':
            case '$':
            case '@':
            case '%':
                pattern.append('\\');
                pattern.append(c);
                esc = false;
                break;
            case '\\':
                if (esc) {
                    pattern.append('\\');
                    esc = false;
                } else {
                    esc = true;
                }
                break;
            default:
                pattern.append(c);
            }
        }
        pattern.append('$');
        return pattern.toString();
    }
}
