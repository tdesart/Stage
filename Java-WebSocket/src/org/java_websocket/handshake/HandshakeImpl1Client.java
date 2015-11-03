package org.java_websocket.handshake;

/**
 * Original version from TooTallNate, changed to meet our needs, by Sandro Reis
 * @author TooTallNate - Original
 *
 */
public class HandshakeImpl1Client extends HandshakedataImpl1 implements ClientHandshakeBuilder {
	private String resourcedescriptor;

	/**
	 * 
	 */
	public HandshakeImpl1Client() {
	}

	@Override
	public void setResourceDescriptor( final String resourcedescriptor ) throws IllegalArgumentException {
		this.resourcedescriptor = resourcedescriptor;
	}

	@Override
	public String getResourceDescriptor() {
		return resourcedescriptor;
	}
}