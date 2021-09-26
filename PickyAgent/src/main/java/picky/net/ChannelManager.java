package picky.net;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import io.netty.channel.ChannelHandlerContext;
import picky.PickyClassManager;
import picky.command.Command;
import picky.executor.Executor;
import picky.executor.ExecutorBuilder;
import picky.serializer.CommandSerializer;

/**
 * channel manager.
 * 
 * @author FrankNPC
 *
 */
public final class ChannelManager {

	private static volatile ChannelManager channelManager;

	private static CommandSerializer commandSerializer;
	
	private static Executor executor;

	public static ChannelManager getInstance() {
		if (channelManager == null) {
			synchronized (ChannelManager.class) {
				if (channelManager == null) {
					channelManager = new ChannelManager();
				}
				try {
					commandSerializer = (CommandSerializer) PickyClassManager.getInstance().getCommandSerializer()
							.newInstance();
					executor = ExecutorBuilder.getInstance().getExecutor();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return channelManager;
	}

	private Map<String, ChannelHandlerContext> communicationChannel = new ConcurrentHashMap<>();
	private Map<String, Deque<Command>> cachedCommands = new ConcurrentHashMap<>();
	private Map<String, Deque<Command>> retryCommands = new ConcurrentHashMap<>();

	public void invalid(ChannelHandlerContext channelContext) {
		for(String key : communicationChannel.keySet()) {
			if (communicationChannel.get(key).equals(channelContext)) {
				communicationChannel.remove(key);
				break;
			}
		}
	}
	
	public void receive(ChannelHandlerContext channelContext, byte[] bytes) {
		List<Command> commands = commandSerializer.deserialize(bytes);
		executor.runBatch(commands);
		
//		List<Command> commandReturns = new ArrayList<>();
//		for(Command command : commands) {
//			communicationChannel.putIfAbsent(command.getTargetNodeName(), channelContext);
//			Deque<Command> commandDeque = retryCommands.remove(command.getTargetNodeName());
//			if (commandDeque!=null) {
//				Deque<Command> cachedCommandDeque = cachedCommands.get(command.getTargetNodeName());
//				if (cachedCommandDeque==null) {
//					cachedCommandDeque = new ConcurrentLinkedDeque<>();
//				}
//				cachedCommandDeque.addAll(commandDeque);
//			}
//			command = executor.run(command);
//			if (command!=null) {
//				commandReturns.add(command);
//			}
//		};
//		if (!commandReturns.isEmpty()) {
//			send(commandReturns);
//		}
	}

	public void send(Command command) {
		send(Collections.singletonList(command));
	}

	public void send(List<Command> commandList) {
		if (commandList.isEmpty()) {
			return;
		}
		for(String key : cachedCommands.keySet()) {
			ChannelHandlerContext channelContext = communicationChannel.get(key);
			Deque<Command> commands = cachedCommands.get(key);
			if (channelContext==null) {
				Deque<Command> retryCommandDeque = retryCommands.get(key);
				if (retryCommandDeque==null) {
					retryCommandDeque = new ConcurrentLinkedDeque<>();
					retryCommands.put(key, retryCommandDeque);
				}
				retryCommandDeque.addAll(commands);
				PickyClient.getInstance().connect(key);
			}else {
				Deque<Command> commandDeque = retryCommands.get(key);
				if (commandDeque!=null) {
					commands.addAll(commandDeque);
				}
				channelContext.writeAndFlush(commandSerializer.serialize(new ArrayList<>(commands)));
			}
		}
	}

}
