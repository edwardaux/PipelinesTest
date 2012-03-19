package com.hae.pipe;

import junit.framework.*;

public class TestStageCommand extends TestCase {
	public void testSyntax() {
		// TODO how do we do this properly?
		assertEquals(0, new Pipe().run("command | hole"));
		assertEquals(-5001, new Pipe().run("command xxx"));
	}

	public void testCommand() {
		Pipe pipe = new Pipe();
		if (isWindows())
			assertEquals(0, pipe.run("literal cmd /c set | command | hole"));
		else if (isMac()) 
			assertEquals(0, pipe.run("literal ls | command | hole"));
	}

	private boolean isWindows() {
		String os = System.getProperty("os.name").toUpperCase();
		return os.contains("WINDOWS");
	}
	private boolean isMac() {
		String os = System.getProperty("os.name").toUpperCase();
		return os.contains("MAC OS X");
	}
}
