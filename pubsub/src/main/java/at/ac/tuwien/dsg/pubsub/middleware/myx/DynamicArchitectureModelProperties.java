package at.ac.tuwien.dsg.pubsub.middleware.myx;

/**
 * Interface containing the model properties of the dynamically created
 * components/connectors and the external interface specifications.
 * 
 * @author bernd.rathmanner
 * 
 */
public interface DynamicArchitectureModelProperties {
    public static final String MESSAGE_DISTRIBUTOR_BLUEPRINT_ID = "connectorffa80065-dd56fa89-04a4013e-18f70890";

    public static final String PUBLISHER_ENDPOINT_BLUEPRINT_ID = "connectorffa83f01-fa73cff6-1ec5f067-25c300d0";
    public static final String PUBLISHER_ENDPOINT_VIRTUAL_EXTERNAL_INTERFACE_NAME = "VirtualExternalPublisher";
    public static final String PUBLISHER_ENDPOINT_VIRTUAL_EXTERNAL_INTERFACE_TYPE = "interfaceTypeee82474e-1d76-4603-a5ba-fadbcf8b0acb";

    public static final String SUBSCRIBER_ENDPOINT_BLUEPRINT_ID = "connectorffa83f01-fa731b1e-0ca1cde8-25c3008b";
    public static final String SUBSCRIBER_ENDPOINT_VIRTUAL_EXTERNAL_INTERFACE_NAME = "VirtualExternalSubscriber";
    public static final String SUBSCRIBER_ENDPOINT_VIRTUAL_EXTERNAL_INTERFACE_TYPE = "interfaceType0bcf68ee-6bf6-488c-af3f-105447849d8e";

    public static final String DISPATCHER_INTERFACE_TYPE = "interfaceTypefff20c0e-f5675386-d56cf5c7-a26e073b";
    public static final String SUBSCRIBER_INTERFACE_TYPE = "interfaceTypeffa83f01-faeae925-a33ee5e9-25c3039d";
    public static final String MYX_ADAPTER_INTERFACE_TYPE = "interfaceTypefff20c0e-f569edef-56edff1d-a26e0908";
}
