package com.hae.pipe;

import junit.framework.*;

public class TestCommandResolve extends TestCase {
	public static final String COPYRIGHT = "Copyright 2007,2012. H.A. Enterprises Pty Ltd. All Rights Reserved";
	
	public void test() {
		Pipe.register("resolvetest", ResolveTest.class);
		assertEquals(0, new Pipe().run("resolvetest"));
	}
	
	public static class ResolveTest extends Stage {
		public int execute(String args) throws PipeException {
			assertEquals(-42, resolve(null));
			assertEquals(-42, resolve(""));
			assertEquals(1, resolve("console"));
			assertEquals(1, resolve("consol"));
			assertEquals(1, resolve("conso"));
			assertEquals(1, resolve("cons"));
			assertEquals(0, resolve("con"));
			assertEquals(1, resolve("CONSOLE"));
			assertEquals(1, resolve("CONS"));
			assertEquals(0, resolve("ffdgdgd"));
			assertEquals(-112, resolve("two words"));
			return 0;
		}
	}

}
