package ddc.support.util;

public class EzCacheException extends Exception {
	private static final long serialVersionUID = 3077763687878554877L;

	public EzCacheException(String message) {
        super(message);
    }
    
    public EzCacheException(Throwable cause) {
        super(cause);
    }
    
    public EzCacheException(String message, Throwable cause) {
        super(message, cause);
    }

}
