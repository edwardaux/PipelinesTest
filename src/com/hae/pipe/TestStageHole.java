package com.hae.pipe;

import junit.framework.*;

public class TestStageHole extends TestCase {
	public static final String COPYRIGHT = "Copyright 2007,2012. H.A. Enterprises Pty Ltd. All Rights Reserved";
	
	public void testHole() {
		assertEquals(0, new Pipe().run("literal a|literal b| hole | literal c| zzzcheck /c/"));
	}
}
