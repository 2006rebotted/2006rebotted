package astraeus.net.codec.login;

/**
 * Represents the enumerated states of the login protocol
 * 
 * @author Vult-R
 */
public enum LoginDecoderState {

		/**
		 * The state where the size of the encrypted data is determined.
		 */
		HEADER,

		/**
		 * The state where the encrypted data is decoded.
		 */
		PAYLOAD;
	
}
