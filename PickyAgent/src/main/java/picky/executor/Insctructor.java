package picky.executor;

import picky.command.Command;

public interface Insctructor {
	
	public Command put(Command command);
	public Command replace(Command command);
	public Command take(Command command);
	public Command get(Command command);
	public Command kick(Command command);
	public Command execute(Command command);
	public Command forward(Command command);
	
}
