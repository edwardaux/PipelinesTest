package com.hae.pipe;

import junit.framework.*;

public class TestStageTake extends TestCase {
	public void testSyntax() {
		assertEquals(0, new Pipe().run("literal a|take 2 | console"));
		assertEquals(0, new Pipe().run("literal a|take 3 bytes | console"));
		assertEquals(0, new Pipe().run("literal a|take last 3 bytes | console"));
		assertEquals(-287, new Pipe().run("literal a|take -50 | console"));
		assertEquals(-112, new Pipe().run("literal a|take blah 3 bytes | console"));
		assertEquals(-102, new Pipe().run("take 2"));
		assertEquals(-112, new Pipe().run("take rubbish"));
	}

	public void testTake() {
		Pipe pipe = new Pipe();
		assertEquals(0, pipe.run("(trace)zzzgen 5 | take 3 | literal x| zzzcheck /x/a/b/c/"));
		assertEquals(0, pipe.run("zzzgen 5 | take 0 | literal x| zzzcheck /x/"));
		assertEquals(0, pipe.run("zzzgen 5 | take 5 | zzzcheck /a/b/c/d/e/"));
		assertEquals(0, pipe.run("zzzgen 5 | take 8 | zzzcheck /a/b/c/d/e/"));

		assertEquals(0, pipe.run("zzzgen 5 | take 3 bytes | zzzcheck /a/b/c/"));
		assertEquals(0, pipe.run("literal abcdefgh | take 3 bytes | zzzcheck /abc/"));
		assertEquals(0, pipe.run("zzzgen 5 | literal abcdefgh| take 10 bytes | zzzcheck /abcdefgh/a/b/"));
		assertEquals(0, pipe.run("literal abcdefgh| take 20 bytes | zzzcheck /abcdefgh/"));
		assertEquals(0, pipe.run("zzzgen 5 | literal abcdefgh| take 20 bytes | zzzcheck /abcdefgh/a/b/c/d/e/"));
		
		assertEquals(0, pipe.run("(end ?) zzzgen 5 | t: take 2 | zzzcheck /a/b/"));
		assertEquals(0, pipe.run("(end ?) zzzgen 5 | t: take 2 | zzzcheck /a/b/ ? t: | zzzcheck /c/d/e/"));
		assertEquals(0, pipe.run("(end ?) literal abcdefgh| t: take 2 bytes | zzzcheck /ab/ ? t: | zzzcheck /cdefgh/"));
		
		// no output stream is connected, so take terminate immediately, which shuts zzzgen down too
		assertEquals(0, pipe.run("(end ?) zzzgen 5 | t: take 2 ? t: | literal x| zzzcheck /x/"));
		// however, now cons is in the picture so we can keep going.
		assertEquals(0, pipe.run("(end ?) zzzgen 5 | t: take 2 | console | zzzcheck /a/b/ ? t: | literal x| zzzcheck /x/c/d/e/"));
		
		// TODO take last ...
	}
}
