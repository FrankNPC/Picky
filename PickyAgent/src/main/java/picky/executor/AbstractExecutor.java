package picky.executor;

import java.util.List;

import picky.command.Command;
import picky.command.CommandBuilder;

public abstract class AbstractExecutor implements Executor, Insctructor {

	@Override
	public Command run(Command command) {
		switch(command.getInstructor()) {
		case put :
			return put(command);
		case replace :
			return replace(command);
		case take :
			return take(command);
		case get :
			return get(command);
		case kick :
			return kick(command);
		case execute :
			return execute(command);
		case forward :
			return forward(command);
		}
		return CommandBuilder.buildEmptyCommand();
	}

	@Override
	public void runBatch(List<Command> commands) {
	}
}
