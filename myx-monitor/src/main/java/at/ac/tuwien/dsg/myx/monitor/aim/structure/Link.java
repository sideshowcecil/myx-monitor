package at.ac.tuwien.dsg.myx.monitor.aim.structure;

public class Link extends ArchitectureElement {

    private InstantiationElement fromElement;
    private Interface fromInterface;
    private InstantiationElement toElement;
    private Interface toInterface;

    public Link(String id) {
        super(id);
    }

    public InstantiationElement getFromElement() {
        return fromElement;
    }

    public void setFromElement(InstantiationElement fromElement) {
        this.fromElement = fromElement;
    }

    public Interface getFromInterface() {
        return fromInterface;
    }

    public void setFromInterface(Interface fromInterface) {
        this.fromInterface = fromInterface;
    }

    public InstantiationElement getToElement() {
        return toElement;
    }

    public void setToElement(InstantiationElement toElement) {
        this.toElement = toElement;
    }

    public Interface getToInterface() {
        return toInterface;
    }

    public void setToInterface(Interface toInterface) {
        this.toInterface = toInterface;
    }

}
