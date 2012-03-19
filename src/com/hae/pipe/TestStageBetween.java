package com.hae.pipe;

import junit.framework.*;

public class TestStageBetween extends TestCase {
	public void testSyntax() {
		assertEquals(0, new Pipe().run("literal a | between /hello/ /there/ | hole"));
		assertEquals(-11, new Pipe().run("literal a | between "));
		assertEquals(-211, new Pipe().run("between /hello/"));
		assertEquals(-66, new Pipe().run("between /hello/ 1"));
		assertEquals(-211, new Pipe().run("between /a/ /d"));
		assertEquals(-60, new Pipe().run("between /a"));
	}

	public void testBetween() {
		Pipe pipe = new Pipe();
		assertEquals(0, pipe.run("zzzgen 6| between /b/ /e/ | zzzcheck /b/c/d/e/"));
		assertEquals(0, pipe.run("zzzgen 6| between /b/ /b/ | zzzcheck /b/c/d/e/f/"));
		assertEquals(0, pipe.run("zzzgen 6| between /b/ /z/ | zzzcheck /b/c/d/e/f/"));
		assertEquals(0, pipe.run("zzzgen 6| between /m/ /p/ | literal z| zzzcheck /z/"));
		assertEquals(0, pipe.run("zzzgen 6| between /b/ 2 | zzzcheck /b/c/"));
		assertEquals(0, pipe.run("zzzgen 6| between /b/ 4 | zzzcheck /b/c/d/e/"));
		assertEquals(0, pipe.run("zzzgen 6| between /b/ 14 | zzzcheck /b/c/d/e/f/"));

		assertEquals(0, pipe.run("(end ?)zzzgen 6| b: between /b/ 3 | zzzcheck /b/c/d/ ? b: | zzzcheck /a/e/f/"));
		assertEquals(0, pipe.run("(end ?)zzzgen 6| b: between /z/ 3 | literal g| zzzcheck /g/ ? b: | zzzcheck /a/b/c/d/e/f/"));
		assertEquals(0, pipe.run("(end ?)literal e|literal d|literal c|literal a|literal b|literal a| b: between /a/ /c/ | zzzcheck /a/b/a/c/ ? b: | zzzcheck /d/e/"));
		assertEquals(0, pipe.run("(end ?)literal e|literal d|literal c|literal a|literal b|literal a| b: between /a/ 4   | zzzcheck /a/b/a/c/ ? b: | zzzcheck /d/e/"));

		assertEquals(0, pipe.run("(end ?)zzzgen 6| between anycase /b/ /c/ | zzzcheck /b/c/"));
		assertEquals(0, pipe.run("(end ?)zzzgen 6| between anycase /B/ /C/ | zzzcheck /b/c/"));
		assertEquals(0, pipe.run("(end ?)zzzgen 6| between /a/ ,d, | zzzcheck /a/b/c/d/"));
		assertEquals(0, pipe.run("(end ?)zzzgen 6| between anycase // 2 | zzzcheck /a/b/c/d/e/f/")); // TODO change scanString
	}
}
