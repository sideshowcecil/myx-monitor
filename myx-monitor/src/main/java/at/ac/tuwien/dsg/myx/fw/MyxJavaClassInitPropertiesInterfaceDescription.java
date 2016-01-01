package at.ac.tuwien.dsg.myx.fw;

import java.util.Properties;

import edu.uci.isr.myx.fw.MyxJavaClassInterfaceDescription;

public class MyxJavaClassInitPropertiesInterfaceDescription extends MyxJavaClassInterfaceDescription implements
        IMyxInitPropertiesInterfaceDescription {

    private static final long serialVersionUID = -899802385323739445L;
    
    protected Properties initParams;

    public MyxJavaClassInitPropertiesInterfaceDescription(String[] serviceObjectInterfaceNames, Properties initParams) {
        super(serviceObjectInterfaceNames);
        this.initParams = initParams;
    }

    @Override
    public Properties getInitParams() {
        return initParams;
    }

}
