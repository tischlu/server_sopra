package ch.uzh.ifi.seal.soprafs19.exceptions;

public class userException extends RuntimeException {

    private String message;

    public userException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
