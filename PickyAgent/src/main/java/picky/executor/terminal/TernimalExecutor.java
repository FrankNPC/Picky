package picky.executor.terminal;

import java.util.List;

import picky.command.Command;
import picky.executor.Executor;

public class TernimalExecutor implements Executor{

	private static volatile TernimalExecutor ternimalExecutor;
	public static TernimalExecutor getInstance() {
		if (ternimalExecutor==null) {
			synchronized(TernimalExecutor.class) {
				if (ternimalExecutor==null) {
					ternimalExecutor = new TernimalExecutor();
				}
			}
		}
		return ternimalExecutor;
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
		case Atomic : return new DefaultAtomicExecutor().run(command);
		case Message : return new DefaultMessageExecutor().run(command);
		case Service : return new DefaultServiceExecutor().run(command);
		}
		return null;
	}

	@Override
	public void runBatch(List<Command> commands) {
	}
}
