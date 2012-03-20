package com.hae.pipe;

import junit.framework.*;

public class TestStageHostid extends TestCase {
	public static final String COPYRIGHT = "Copyright 2007,2012. H.A. Enterprises Pty Ltd. All Rights Reserved";
	
	public void testSyntax() {
		assertEquals(0, new Pipe().run("hostid"));
		assertEquals(-112, new Pipe().run("hostid xx"));
		assertEquals(-87, new Pipe().run("literal a | hostid"));
	}

	public void testHostId() {
		Pipe pipe = new Pipe();
		assertEquals(0, pipe.run("hostid | console"));
	}
}
