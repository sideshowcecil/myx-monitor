package at.ac.tuwien.dsg.myx.monitor.aim;

public class ArchitectureInstantiationException extends Exception {

    private static final long serialVersionUID = 831121738635884280L;

    public ArchitectureInstantiationException(){
        super();
    }

    public ArchitectureInstantiationException(String message){
        super(message);
    }

    public ArchitectureInstantiationException(Throwable cause){
        super(cause);
    }

    public ArchitectureInstantiationException(String message, Throwable cause){
        super(message, cause);
    }

}
