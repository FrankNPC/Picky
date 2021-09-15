package picky.executor;

import java.util.List;

import picky.command.Command;
import picky.command.CommandBuilder;

public abstract class AbstractExecutor implements Executor, Insctructor {

	@Override
	public Command run(Command command) {
		switch(command.getInstructor()) {
		case Put :
			return put(command);
		case Replace :
			return replace(command);
		case Take :
			return take(command);
		case Get :
			return get(command);
		case Kick :
			return kick(command);
		case Execute :
			return execute(command);
		case Forward :
			return forward(command);
		}
		return CommandBuilder.buildEmptyCommand();
	}

	@Override
	public void runBatch(List<Command> commands) {
	}
}
