package common;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int REQUEST = 0;
    public static final int REPLY = 1;

    private int messageType;          
    private int requestId;            
    private String objectReference;   
    private String methodId;          
    private byte[] arguments;         

    
    public Message(int requestId, String objectReference, String methodId, byte[] arguments) {
        this(REQUEST, requestId, objectReference, methodId, arguments);
    }

    
    public Message(byte[] reply, int requestId, String objectReference, String methodId) {
        this(REPLY, requestId, objectReference, methodId, reply);
    }

    private Message(int messageType, int requestId, String objectReference,
                    String methodId, byte[] arguments) {
        this.messageType = messageType;
        this.requestId = requestId;
        this.objectReference = objectReference;
        this.methodId = methodId;
        this.arguments = arguments != null ? arguments : new byte[0];
    }

    

    public int getMessageType() { return messageType; }
    public int getRequestId() { return requestId; }
    public String getObjectReference() { return objectReference; }
    public String getMethodId() { return methodId; }
    public byte[] getArguments() { return arguments; }

    public void setArguments(byte[] arguments) { this.arguments = arguments; }

    public boolean isRequest() { return messageType == REQUEST; }
    public boolean isReply() { return messageType == REPLY; }

    public String getMessageTypeName() {
        return messageType == REQUEST ? "REQUEST" : "REPLY";
    }

    @Override
    public String toString() {
        return "Message{"
            + "messageType=" + getMessageTypeName() + " (" + messageType + ")"
            + ", requestId=" + requestId
            + ", objectReference='" + objectReference + "'"
            + ", methodId='" + methodId + "'"
            + ", arguments=" + (arguments != null ? arguments.length : 0) + " bytes"
            + "}";
    }
}
