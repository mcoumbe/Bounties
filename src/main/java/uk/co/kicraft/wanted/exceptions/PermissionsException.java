package uk.co.kicraft.wanted.exceptions;

public class PermissionsException extends Throwable {

	private String command;
	
	public PermissionsException(String command) {
		this.command = command;
	}
		
	@Override
	public String toString() {
		return "You do not have permission to run the command : " + command;
	}
	
}
