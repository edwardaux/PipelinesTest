package com.hae.pipe;

import java.io.*;

import junit.framework.*;

public class TestStageDiskw extends TestCase {
	public void testSyntax() {
		assertEquals(0, new Pipe().run("literal | > blah"));
		assertEquals(-127, new Pipe().run("> blah"));
		assertEquals(-113, new Pipe().run("literal | >"));
		assertEquals(-112, new Pipe().run("literal | > blah blah"));
	}

	public void testDiskw() {
		Pipe pipe = new Pipe();
		assertEquals(0, pipe.run("zzzgen 8 | > blah"));
		assertEquals(0, pipe.run("diskr blah | zzzcheck /a/b/c/d/e/f/g/h/"));
		assertEquals(0, pipe.run("zzzgen 8 | > blah"));     // check overwrite
		assertEquals(0, pipe.run("diskr blah | zzzcheck /a/b/c/d/e/f/g/h/"));
		assertEquals(0, pipe.run("zzzgen 8 | diskw blah | take 5 | zzzcheck /a/b/c/d/e/"));  // check that we write them all
		assertEquals(0, pipe.run("diskr blah | zzzcheck /a/b/c/d/e/f/g/h/"));

		assertEquals(0, pipe.run("zzzgen 5 | > blah"));
		assertEquals(0, pipe.run("zzzgen 3 | >> blah"));
		assertEquals(0, pipe.run("diskr blah | zzzcheck /a/b/c/d/e/a/b/c/"));
		new File("blah").delete();
		assertEquals(0, pipe.run("zzzgen 4 | >> blah"));     // check new
		assertEquals(0, pipe.run("diskr blah | zzzcheck /a/b/c/d/"));
	}

	protected void tearDown() throws Exception {
		new File("blah").delete();
		super.tearDown();
	}
}
