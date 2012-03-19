package com.hae.pipe;

import junit.framework.*;

public class TestStageCount extends TestCase {
	public void testSyntax() {
		assertEquals(0, new Pipe().run("literal a|count words"));
		assertEquals(0, new Pipe().run("literal a|count words lines"));
		assertEquals(0, new Pipe().run("literal a|count words lines lines"));
		assertEquals(-111, new Pipe().run("literal a|count xxx"));
		assertEquals(-11, new Pipe().run("literal a|count "));
	}

	public void testCount() {
		Pipe pipe = new Pipe();
		assertEquals(0, pipe.run("literal abc|literal def|literal hello there how are you|literal | count chars words lines min max | zzzcheck /29 7 4 0 23/"));
		assertEquals(0, pipe.run("literal abc|literal def|literal hello there how are you|literal | count max min lines words chars | zzzcheck /29 7 4 0 23/"));
		
		assertEquals(0, pipe.run("(end ?)literal abc|literal def|literal hello there how are you|literal | c: count chars words lines min max | zzzcheck //hello there how are you/def/abc/ ? c: | zzzcheck /29 7 4 0 23/"));
	}
}
