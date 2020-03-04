package fr.ensibs.response;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class used by web service to return something
 */
@XmlRootElement
@XmlSeeAlso({ArrayList.class, HashMap.class})
public class SOAPResponse {

    /**
     * Response's messafe
     */
    private String message;

    /**
     * status of the request
     */
    private SOAPResponseStatus status;

    /**
     * Object return by the request
     */
    private Object requestObject;

    /**
     * Constructor
     */
    public SOAPResponse() { }

    /**
     * Constructor
     * @param message
     * @param status
     * @param requestObject
     */
    public SOAPResponse(String message, SOAPResponseStatus status, Object requestObject) {
        this.message = message;
        this.status = status;
        this.requestObject = requestObject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SOAPResponseStatus getStatus() {
        return status;
    }

    public void setStatus(SOAPResponseStatus status) {
        this.status = status;
    }

    public Object getRequestObject() {
        return requestObject;
    }

    public void setRequestObject(Object requestObject) {
        this.requestObject = requestObject;
    }
}
