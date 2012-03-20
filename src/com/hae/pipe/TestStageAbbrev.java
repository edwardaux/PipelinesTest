package com.hae.pipe;

import junit.framework.*;

public class TestStageAbbrev extends TestCase {
	public static final String COPYRIGHT = "Copyright 2007,2012. H.A. Enterprises Pty Ltd. All Rights Reserved";
	
	public void testSyntax() {
		assertEquals(0, new Pipe().run("literal a|abbrev hello 2 | hole"));
	}

	public void testAbbrev() {
		Pipe pipe = new Pipe();
		assertEquals(0, pipe.run("literal he| abbrev hello 3 | literal x| zzzcheck /x/"));
		assertEquals(0, pipe.run("literal he| abbrev hello 2 | literal x| zzzcheck /x/he/"));
		assertEquals(0, pipe.run(
				"(end ?) literal hel"+
				"| literal he"+
				"| literal hello"+
				"| a: abbrev hello 3"+
				"| zzzcheck /hello/hel/ "+
				"? "+
				"  a:"+
				"| zzzcheck /he/"));
	}
}
