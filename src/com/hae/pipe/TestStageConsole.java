package com.hae.pipe;

import junit.framework.*;

public class TestStageConsole extends TestCase {
	public static final String COPYRIGHT = "Copyright 2007,2012. H.A. Enterprises Pty Ltd. All Rights Reserved";
	
	public void testSyntax() {
		assertEquals(0, new Pipe().run("literal a | console"));
		assertEquals(-112, new Pipe().run("literal a | console xxx"));
		assertEquals(-112, new Pipe().run("console xxx"));
		assertEquals(-11, new Pipe().run("console eof"));
	}

	public void testConsole() {
		Pipe pipe = new Pipe();
		//assertEquals(0, pipe.run("console | zzzcheck /a/b/c/"));  // type a <cr> b <cr> c
		//assertEquals(0, pipe.run("console eof /d/ | zzzcheck /a/b/c/"));  // type a <cr> b <cr> c <cr> d
		// TODO check that eof propogates back faster for the next one
		//assertEquals(0, pipe.run("console noeof | take 3 | zzzcheck /a/b/c/"));  // type a <cr> b <cr> c <cr> d <cr>
		assertEquals(0, pipe.run("zzzgen 3| console | zzzcheck /a/b/c/"));
	}
}
