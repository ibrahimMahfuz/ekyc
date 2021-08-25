package id.co.pcsindonesia.ia.ekyc.util.exception;

public class RequestTimeOutException extends RuntimeException{
    public RequestTimeOutException(String message) {
        super(message);
    }
}
