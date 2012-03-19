package com.hae.pipe;

import junit.framework.*;

public class TestStageHole extends TestCase {
	public void testHole() {
		assertEquals(0, new Pipe().run("literal a|literal b| hole | literal c| zzzcheck /c/"));
	}
}
