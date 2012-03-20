package com.hae.pipe;

import junit.framework.*;

public class TestStageFaninany extends TestCase {
	public static final String COPYRIGHT = "Copyright 2007,2012. H.A. Enterprises Pty Ltd. All Rights Reserved";
	
	public void testSyntax() {
		assertEquals(0, new Pipe().run("literal a | faninany | stem blah"));
		assertEquals(-102, new Pipe().run("faninany"));
	}
	
	public void testStreams() {
		assertEquals(-264, new Pipe().run("(end ?) literal a | a: faninany | stem blah ? a: | stem foo"));
	}
	

}
