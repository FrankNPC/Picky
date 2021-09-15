package picky.executor;

import java.util.List;

import picky.command.Command;

/**
 * execute the command. 
 * broad message, put record, sharding primary key etc.
 * 
 * @author FrankNPC
 *
 */
public interface Executor {
	
	public Command run(Command command);

	public void runBatch(List<Command> commands);
	
}
