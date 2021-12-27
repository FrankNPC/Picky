package picky.command;

import org.junit.Assert;
import org.junit.Test;

public class CommandBuilderTester {

	@Test
	public void test_buildEmptyCommand() {
		Command command = CommandBuilder.buildEmptyCommand();
		Assert.assertNotNull(command);
	}
	
	@Test
	public void test_buildOKCommand() {
		Command command = CommandBuilder.buildOKCommand();
		
		Assert.assertNotNull(command);
		Assert.assertNotNull(command.getInstructor());
		Assert.assertNotNull(command.getModule());
		Assert.assertNotNull(command.getParameters());
		Assert.assertTrue(command.getTime()>0);
		
		System.out.println(command.getInstructor());
		System.out.println(command.getModule());
		System.out.println(new String(command.getParameters()));
		System.out.println(command.getTime());
		
	}

}
