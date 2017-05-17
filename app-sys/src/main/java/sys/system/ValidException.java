package sys.system;

/**
 * Valid Exception
 * 
 * @author Fantasy
 * 
 */
public class ValidException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ValidException(String message) {
		super(message);
	}

	public ValidException(Throwable cause) {
		super(cause);
	}

	public ValidException(String message, Throwable cause) {
		super(message, cause);
	}
}
