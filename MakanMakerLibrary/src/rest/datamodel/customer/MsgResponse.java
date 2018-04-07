
package rest.datamodel.customer;

/**
 *
 * @author yingshi
 */
public class MsgResponse {
    private String message;
    private boolean result;

    public MsgResponse() {
    }

    public MsgResponse(String message, boolean result) {
        this.message = message;
        this.result = result;
    }

    
    
    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the result
     */
    public boolean isResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(boolean result) {
        this.result = result;
    }
    
    
}
