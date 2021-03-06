package com.hae.pipe;

import junit.framework.*;

public class TestStageGate extends TestCase {
	public void testSyntax() {
		assertEquals(0, new Pipe().run("literal a|gate"));
		assertEquals(0, new Pipe().run("literal a|gate strict"));
		assertEquals(-112, new Pipe().run("literal a|gate blah"));
	}

	public void testGate() {
		Pipe pipe = new Pipe();
		assertEquals(0, pipe.run("(end ?) zzzgen 8 | l: locate /f/ | g: gate ? l: | g: | zzzcheck /a/b/c/d/e/"));
	}
}
