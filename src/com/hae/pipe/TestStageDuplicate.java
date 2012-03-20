package com.hae.pipe;

import junit.framework.*;

public class TestStageDuplicate extends TestCase {
	public void testSyntax() {
		assertEquals(0, new Pipe().run("literal a | dup"));
		assertEquals(0, new Pipe().run("literal a | dup 1"));
		assertEquals(0, new Pipe().run("literal a | dup 123"));
		assertEquals(0, new Pipe().run("literal a | dup -1"));
		assertEquals(0, new Pipe().run("literal a | dup *"));
		assertEquals(-66, new Pipe().run("literal a | dup -5"));
		assertEquals(-112, new Pipe().run("literal a | dup xxx"));
		assertEquals(-112, new Pipe().run("literal a | dup 1 xxx"));
	}

	public void testDuplicate() {
		Pipe pipe = new Pipe();
		assertEquals(0, pipe.run("literal a| dup | zzzcheck /a/a/"));
		assertEquals(0, pipe.run("literal a| dup 1 | zzzcheck /a/a/"));
		assertEquals(0, pipe.run("literal b|literal a| dup 2 | zzzcheck /a/a/a/b/b/b/"));
		assertEquals(0, pipe.run("literal a| dup 5 | zzzcheck /a/a/a/a/a/a/"));
		assertEquals(0, pipe.run("literal a| dup -1 | literal b| zzzcheck /b/"));
		assertEquals(0, pipe.run("literal a| dup * | take 10 | zzzcheck /a/a/a/a/a/a/a/a/a/a/"));
	}
}
