package at.ac.tuwien.infosys.pubsub.network.socket;

public final class Protocol {
    public static final char NULL = '\0';
    public static final char CR = '\r';
    public static final char LF = '\n';

    public static final char TOPIC = '#';
    public static final char INIT = '*';
    public static final char DATA = '+';
    public static final char CLOSE = '$';
    public static final char ERROR = '-';

    private Protocol() {
    }
}
