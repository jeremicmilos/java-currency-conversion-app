package bankAccount.errorHandling;

import java.util.List;

public class Error {

    private String message;
    
    public Error() {
    	
    }
    
	public Error(String message) {
        this.message = message;
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
