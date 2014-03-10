package at.ac.tuwien.dsg.myx.monitor.aim.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Implementation {

    private String mainClassName;
    private final List<String> auxClassNames = new ArrayList<>();
    private final Properties initProperties = new Properties();

    public String getMainClassName() {
        return mainClassName;
    }

    public void setMainClassName(String mainClassName) {
        this.mainClassName = mainClassName;
    }

    public List<String> getAuxClassNames() {
        return auxClassNames;
    }

    public Properties getInitProperties() {
        return initProperties;
    }
}
