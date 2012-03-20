package com.hae.pipe;

import junit.framework.*;

public class TestStageAggrc extends TestCase {
	public void testAggrc() {
		Pipe pipe = new Pipe();
		assertEquals(0, pipe.run("literal 0 | literal 99 | literal 3   | literal 6  | aggrc | zzzcheck /99/"));
		assertEquals(0, pipe.run("literal 0 | literal 99 | literal -3  | literal 6  | aggrc | zzzcheck /-3/"));
		assertEquals(0, pipe.run("literal 0 | literal +99 | literal -3 | literal +6 | aggrc | zzzcheck /-3/"));
		assertEquals(6, pipe.run("(listerr) literal 6 | aggrc"));
		assertEquals(0, pipe.run("aggrc | literal x| zzzcheck /x/"));
	}
}
