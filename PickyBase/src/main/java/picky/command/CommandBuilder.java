package picky.command;

/**
 * Command builder
 * 
 * @author FrankNPC
 *
 */
public final class CommandBuilder {

	public static Command buildEmptyCommand() {
		Command command = new Command();
		return command;
	}

	public static Command buildOKCommand() {
		Command command = new Command();
		command.setInstructor(Instructor.Forward);
		command.setKind(Kind.Message);
		command.setParameters("OK".getBytes());
		command.setTime(System.currentTimeMillis());
		return command;
	}

}
