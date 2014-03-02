package at.ac.tuwien.dsg.myx.util;

/**
 * A basic tuple implementation.
 * 
 * @author bernd.rathmanner
 * 
 * @param <X>
 * @param <Y>
 */
public final class Tuple<X, Y> {

    private X fst;
    private Y snd;

    public Tuple() {
    }

    public Tuple(X fst, Y snd) {
        this.fst = fst;
        this.snd = snd;
    }

    /**
     * Set the first element.
     * 
     * @param fst
     */
    public void setFst(X fst) {
        this.fst = fst;
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
     * Set the second element.
     * 
     * @param snd
     */
    public void setSnd(Y snd) {
        this.snd = snd;
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
