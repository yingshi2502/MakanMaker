
package rest.datamodel.customer;

import entity.ReviewEntity;
import java.util.List;

/**
 *
 * @author yingshi
 */
public class RetrieveReviewsResponse {
    private List<ReviewEntity> reviews;
    private String message;
    private boolean result;

    public RetrieveReviewsResponse() {
    }

    public RetrieveReviewsResponse(List<ReviewEntity> reviews, String message, boolean result) {
        this.reviews = reviews;
        this.message = message;
        this.result = result;
    }

    /**
     * @return the reviews
     */
    public List<ReviewEntity> getReviews() {
        return reviews;
    }

    /**
     * @param reviews the reviews to set
     */
    public void setReviews(List<ReviewEntity> reviews) {
        this.reviews = reviews;
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
