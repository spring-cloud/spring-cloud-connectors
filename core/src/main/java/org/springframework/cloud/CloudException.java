package org.springframework.cloud;

/**
 * Common exception for cloud-related operations
 * 
 * @author Ramnivas Laddad
 *
 */
@SuppressWarnings("serial")
public class CloudException extends RuntimeException {

	public CloudException(String message) {
		super(message);
	}

	public CloudException(Throwable cause) {
		super(cause);
	}
	
	public CloudException(String message, Throwable cause) {
		super(message, cause);
	}

}
