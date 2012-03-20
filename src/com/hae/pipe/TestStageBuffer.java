package com.hae.pipe;

import junit.framework.*;

public class TestStageBuffer extends TestCase {
	public void testSyntax() {
		assertEquals(-60, new Pipe().run("buffer 1 /"));
		assertEquals(-58, new Pipe().run("buffer x"));
		assertEquals(-66, new Pipe().run("buffer 0"));
		assertEquals(-58, new Pipe().run("buffer -1"));
	}

	public void testBuffer() {
		Pipe pipe = new Pipe();
		assertEquals(0, pipe.run("zzzgen 3 | buffer       | zzzcheck /a/b/c/"));
		assertEquals(0, pipe.run("zzzgen 3 | buffer 1     | zzzcheck /a/b/c/"));
		assertEquals(0, pipe.run("zzzgen 3 | buffer 1 /-/ | zzzcheck /a/b/c/"));
		assertEquals(0, pipe.run("zzzgen 3 | buffer 2     | zzzcheck /a/b/c//a/b/c/"));
		assertEquals(0, pipe.run("zzzgen 3 | buffer 2 /-/ | zzzcheck /a/b/c/-/a/b/c/"));
		assertEquals(0, pipe.run("zzzgen 3 | buffer 3 /-/ | zzzcheck /a/b/c/-/a/b/c/-/a/b/c/"));
	}
}
