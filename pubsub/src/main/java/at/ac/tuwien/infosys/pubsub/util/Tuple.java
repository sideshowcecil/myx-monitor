package at.ac.tuwien.infosys.pubsub.util;

/**
 * A basic tuple implementation.
 * 
 * @author bernd.rathmanner
 * 
 * @param <X>
 * @param <Y>
 */
public final class Tuple<X, Y> {

    private final X fst;
    private final Y snd;

    public Tuple(X fst, Y snd) {
        this.fst = fst;
        this.snd = snd;
    }

    /**
     * Get the first element.
     * 
     * @return
     */
    public X getFst() {
        return fst;
    }

    /**
     * Get the second element.
     * 
     * @return
     */
    public Y getSnd() {
        return snd;
    }
}
