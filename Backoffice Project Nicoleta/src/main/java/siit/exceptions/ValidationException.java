package siit.exceptions;

public class ValidationException extends RuntimeException {
    private String code;

    public ValidationException(String code){
        this.code = code;
    }

    public String getCode(){
        return code;
    }
}
