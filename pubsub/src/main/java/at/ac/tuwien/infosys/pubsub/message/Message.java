package at.ac.tuwien.infosys.pubsub.message;

public class Message<E> {

    private E data;
    private Type type;
    
    public Message(E data) {
        this(data, Type.DATA);
    }
    
    public Message(E data, Type type) {
        this.data = data;
        this.type = type;
    }

    public E getData() {
        return data;
    }

    public Type getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return "[" + type + "] " + data;
    }

    public enum Type {
        TOPIC, INIT, DATA, CLOSE, ERROR
    }
}
