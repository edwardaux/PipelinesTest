package com.hae.pipe;

import junit.framework.*;

public class TestStageLiteral extends TestCase {
	public void testSyntax() {
		assertEquals(0, new Pipe().run("literal a| zzzcheck /a/"));
		assertEquals(0, new Pipe().run("literal  a| zzzcheck / a/"));
		assertEquals(0, new Pipe().run("literal a|literal b| zzzcheck /b/a/"));
		assertEquals(0, new Pipe().run("literal a |literal b | zzzcheck /b /a /"));
		assertEquals(0, new Pipe().run("literal| zzzcheck //"));
	}
}
