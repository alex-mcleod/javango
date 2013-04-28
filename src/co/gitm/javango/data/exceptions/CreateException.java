package co.gitm.javango.data.exceptions;

/**
 * @author Alex McLeod Exception to be thrown if there is an error creating a
 *         new object in a datasource.
 * 
 */
public class CreateException extends Exception {
	private String message;

	public CreateException(String msg) {
		this.setMessage(msg);
	}

	public void setMessage(String msg) {
		this.message = msg;
	}

	public String getMessage() {
		return this.message;
	}
}
