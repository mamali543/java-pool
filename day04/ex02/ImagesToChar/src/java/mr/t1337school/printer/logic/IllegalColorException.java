package mr.t1337school.printer.logic;

public class IllegalColorException extends RuntimeException {
	public IllegalColorException() {
		super("Illegal color specified!");
	}
}