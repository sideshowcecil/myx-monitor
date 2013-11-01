package at.ac.tuwien.infosys.pubsub.util;

public final class Tuple<X, Y> {
    
    private final X fst;
    private final Y snd;
    
    public Tuple(X fst, Y snd) {
        this.fst = fst;
        this.snd = snd;
    }

    public X getFst() {
        return fst;
    }

    public Y getSnd() {
        return snd;
    }
}
