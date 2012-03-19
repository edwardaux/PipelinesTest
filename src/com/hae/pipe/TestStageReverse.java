package com.hae.pipe;

import junit.framework.*;

public class TestStageReverse extends TestCase {
	public void testSyntax() {
		assertEquals(0, new Pipe().run("literal a | reverse"));
		assertEquals(-112, new Pipe().run("literal a | reverse xx"));
	}

	public void testReverse() {
		Pipe pipe = new Pipe();
		assertEquals(0, pipe.run("literal | reverse | zzzcheck //"));
		assertEquals(0, pipe.run("literal a| reverse | zzzcheck /a/"));
		assertEquals(0, pipe.run("literal abcde| reverse | zzzcheck /edcba/"));
		assertEquals(0, pipe.run("literal hello there| reverse | zzzcheck /ereht olleh/"));
	}
}
