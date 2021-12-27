package picky.serializer;

import java.io.OutputStream;
import java.util.List;

import picky.command.Command;

public interface CommandSerializer {

	public byte[] serialize(Command commands);

	public void serialize(Command command, OutputStream buffer);
	
	public byte[] serialize(List<Command> commands);
	
	public void serialize(List<Command> commands, OutputStream buffer);

	public List<Command> deserialize(byte[] bytes);
	
}
