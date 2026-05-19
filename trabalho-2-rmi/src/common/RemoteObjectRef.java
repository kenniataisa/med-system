package common;

import java.io.Serializable;

public class RemoteObjectRef implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String serviceName;   
    private final String host;          
    private final int    port;          

    public RemoteObjectRef(String serviceName, String host, int port) {
        this.serviceName = serviceName;
        this.host = host;
        this.port = port;
    }

    public String getServiceName() { return serviceName; }
    public String getHost()        { return host; }
    public int getPort()        { return port; }

    
    public String toLookupUrl() {
        return "//" + host + ":" + port + "/" + serviceName;
    }

    @Override
    public String toString() {
        return "RemoteObjectRef{service='" + serviceName
            + "', host='" + host + "', port=" + port + "}";
    }
}
