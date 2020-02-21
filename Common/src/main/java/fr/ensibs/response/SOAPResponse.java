package fr.ensibs.response;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.ArrayList;

@XmlRootElement
@XmlSeeAlso(ArrayList.class)
public class SOAPResponse {
    private String message;
    private SOAPResponseStatus status;
    private Object requestObject;

    public SOAPResponse() { }

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
