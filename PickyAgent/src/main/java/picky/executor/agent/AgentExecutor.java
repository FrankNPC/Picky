package picky.executor.agent;

import java.util.List;

import picky.command.Command;
import picky.command.CommandBuilder;
import picky.executor.Executor;

public class AgentExecutor implements Executor{

	private static AgentExecutor agentExecutor = new AgentExecutor();
	public static AgentExecutor getInstance() {
		return agentExecutor;
	}
	
	@Override
	public Command run(Command command) {
		switch(command.getKind()) {
		case Script : return new DefaultScriptExecutor().run(command);
		case Sharding : return new DefaultShardingExecutor().run(command);
		case Record : return new DefaultRecordExecutor().run(command);
		case Schema : return new DefaultSchemaExecutor().run(command);
		case Agent : return new DefaultAgentExecutor().run(command);
		case Storage : return new DefaultStorageExecutor().run(command);
		case Config : return new DefaultConfigExecutor().run(command);
		case Lock : return new DefaultAtomicExecutor().run(command);
		case Atomic : return new DefaultAtomicExecutor().run(command);
		case Transaction : return new DefaultAtomicExecutor().run(command);
		case Message : return new DefaultMessageExecutor().run(command);
		case Service : return new DefaultServiceExecutor().run(command);
		}
		return CommandBuilder.buildEmptyCommand();
	}

	@Override
	public void runBatch(List<Command> commands) {
	}
	
}
