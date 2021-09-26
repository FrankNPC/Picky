package picky.serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import picky.command.Command;
import picky.command.Instructor;
import picky.command.Kind;

public final class DefaultCommandSerializer implements CommandSerializer {

	@Override
	public byte[] serialize(Command command) {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		serialize(command, buffer);
		return buffer.toByteArray();
	}

	@Override
	public void serialize(Command command, OutputStream buffer) {
		try {
			writeToBuffer(command, buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public byte[] serialize(List<Command> commands) {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		serialize(commands, buffer);
		return buffer.toByteArray();
	}

	@Override
	public void serialize(List<Command> commands, OutputStream buffer) {
		try {
			for(Command command : commands) {
				writeToBuffer(command, buffer);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeToBuffer(Command command, OutputStream buffer) throws IOException {
		buffer.write(command.getInstructor().toString().getBytes());
		buffer.write(' ');
		buffer.write(command.getKind().toString().getBytes());
		buffer.write(' ');
		buffer.write(command.getSourceNodeName().getBytes());
		buffer.write(' ');
		buffer.write(command.getRelayNodeName().getBytes());
		buffer.write(' ');
		buffer.write(command.getTargetNodeName().getBytes());
		buffer.write(' ');
		buffer.write(command.getTokenId().getBytes());
		buffer.write(' ');

		long time = command.getTime();
		for (int j = 0; j < 8; j++) {
			buffer.write((byte)time);
			time <<= 8;
		}
		buffer.write(' ');
		
		int length = command.getParameters().length;
		for (int j = 0; j < 4; j++) {
			buffer.write((byte)length);
			length <<= 8;
		}
		buffer.write(command.getParameters());

		buffer.write(' ');
	}

	@Override
	public List<Command> deserialize(byte[] bytes) {
		List<Command> commandList = new ArrayList<>();
		for (int index = 0, last = 0; index < bytes.length; index++, last = index) {
			Command command = new Command();

			while (index < bytes.length && bytes[index] != ' ') {}
			command.setInstructor(Instructor.valueOf(new String(bytes, last, index)));
			last = index += 1;

			while (index < bytes.length && bytes[index] != ' ') {}
			command.setKind(Kind.valueOf(new String(bytes, last, index)));
			last = index += 1;

			while (index < bytes.length && bytes[index] != ' ') {}
			command.setRelayNodeName(new String(bytes, last, index));
			last = index += 1;
			
			while (index < bytes.length && bytes[index] != ' ') {}
			command.setSourceNodeName(new String(bytes, last, index));
			last = index += 1;

			while (index < bytes.length && bytes[index] != ' ') {}
			command.setTargetNodeName(new String(bytes, last, index));
			last = index += 1;

			while (index < bytes.length && bytes[index] != ' ') {}
			command.setTokenId(new String(bytes, last, index));
			last = index += 1;

			long time = 0;
			for (int j = 0; j < 8; j++) {
				time <<= 8;
				time |= bytes[index++];
			}
			command.setTime(time);
			last = index += 1;

			int length = 0;
			for (int j = 0; j < 4; j++) {
				time <<= 8;
				time |= bytes[index++];
			}
			command.setParameters(Arrays.copyOfRange(bytes, index, index + length));
			index += length+1;
			commandList.add(command);
		}

		return commandList;
	}
}
