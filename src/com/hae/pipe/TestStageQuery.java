package com.hae.pipe;

import junit.framework.*;

public class TestStageQuery extends TestCase {
	public void testSyntax() {
		assertEquals(0, new Pipe().run("query"));
		assertEquals(0, new Pipe().run("query VERSION"));
		assertEquals(-111, new Pipe().run("query xx"));
		assertEquals(-87, new Pipe().run("literal a | query"));
	}

	public void testQuery() {
		Pipe pipe = new Pipe();
		assertEquals(0, pipe.run("query version | hole"));
		assertEquals(0, pipe.run("query msglevel | zzzcheck /\u0000\u0000\u0000\u0007/"));
		assertEquals(0, pipe.run("(msglevel 14) query msglevel | zzzcheck /\u0000\u0000\u0000\u000e/"));
		assertEquals(0, pipe.run("query msglist | zzzcheck //"));
		assertEquals(0, pipe.run("query level | zzzcheck /\u0032\u0000\u0000\u0000/"));
	}
}
