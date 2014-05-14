package org.springframework.cloud.service;

/**
 * Exception to represent issues with creating service connector. Note that this
 * exception indicates that something went wrong when creating service (for example,
 * missing driver class) and <em>not</em> interfacing with the underlying cloud.
 *  
 * @author Ramnivas Laddad
 *
 */
@SuppressWarnings("serial")
public class ServiceConnectorCreationException extends RuntimeException {
	
	public ServiceConnectorCreationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ServiceConnectorCreationException(Throwable cause) {
		super(cause);
	}

	public ServiceConnectorCreationException(String message) {
		super(message);
	}
}
