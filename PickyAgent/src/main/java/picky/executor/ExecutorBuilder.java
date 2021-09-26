package picky.executor;

import picky.Configuration;
import picky.executor.agent.AgentExecutor;
import picky.executor.storage.StorageExecutor;
import picky.executor.terminal.TernimalExecutor;

public final class ExecutorBuilder {
	
	private static ExecutorBuilder executorBuilder = new ExecutorBuilder();
	private static Configuration configuration;
	public static ExecutorBuilder getInstance() {
		configuration = Configuration.getConfiguration();
		return executorBuilder;
	}

	private Executor executor;

	public Executor getExecutor() {
		if (executor!=null) {
			return executor;
		}
		
		switch(configuration.getRole()) {
		case Agent :
//		case AgentReadOnly :
			return AgentExecutor.getInstance();
		case Storage :
//		case StorageReadOnly :
			return StorageExecutor.getInstance();
//		case Client :
		case Ternimal :
			return TernimalExecutor.getInstance();
		}
		return executor;
	}

	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

}
