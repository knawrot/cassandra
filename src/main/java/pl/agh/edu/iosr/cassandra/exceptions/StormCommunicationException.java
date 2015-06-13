package pl.agh.edu.iosr.cassandra.exceptions;

public class StormCommunicationException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public StormCommunicationException(String msg) {
		super("Communication with Storm failed: " + msg);
	}

}
