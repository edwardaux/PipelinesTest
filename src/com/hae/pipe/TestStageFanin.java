package com.hae.pipe;

import com.hae.pipe.stages.*;

import junit.framework.*;

public class TestStageFanin extends TestCase {
	public void testSyntax() {
		assertEquals(0, new Pipe().run("literal a | fanin | stem blah"));
		assertEquals(-102, new Pipe().run("fanin"));
	}
	
	public void testStreams() {
		assertEquals(-264, new Pipe().run("(end ?) literal a | a: fanin | stem blah ? a: | stem foo"));
	}
	
	public void testData() {
		assertEquals(0, new Pipe().run("(end ?) zzzgen 3 | a: fanin | zzzcheck /a/b/c/d/e/ ? literal e|literal d| a:"));
		assertEquals(0, new Pipe().run("(end ?) zzzgen 3 | a: fanin | zzzcheck /a/b/c/d/e/f/g/h/ ? literal e|literal d| a: ? literal f| a: ? literal h| literal g| a:"));
		assertEquals(0, new Pipe().run("(end ?) zzzgen 3 | a: fanin 0 1 2 3 | zzzcheck /a/b/c/d/e/f/g/h/ ? literal e|literal d| a: ? literal f| a: ? literal h| literal g| a:"));
		assertEquals(0, new Pipe().run("(end ?) zzzgen 3 | a: fanin 0 1 2 | zzzcheck /a/b/c/d/e/f/ ? literal e|literal d| a: ? literal f| a: ? literal h| literal g| a:"));
		assertEquals(-178, new Pipe().run("(end ?) zzzgen 3 | a: fanin 6 | console ? literal e|literal d| a: ? literal f| a: ? literal h| literal g| a:"));
	}
	

}
