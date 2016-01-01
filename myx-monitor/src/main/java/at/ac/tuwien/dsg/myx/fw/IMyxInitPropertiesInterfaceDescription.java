package at.ac.tuwien.dsg.myx.fw;

import java.util.Properties;

import edu.uci.isr.myx.fw.IMyxInterfaceDescription;

public interface IMyxInitPropertiesInterfaceDescription extends IMyxInterfaceDescription {
    Properties getInitParams();
}
