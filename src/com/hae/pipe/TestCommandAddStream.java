package com.hae.pipe;

import junit.framework.*;

public class TestCommandAddStream extends TestCase {
	public void testAddStream() {
		Pipe.register("addstreamtest", AddStreamTest.class);
		assertEquals(0, new Pipe().run("addstreamtest"));
	}

	public static class AddStreamTest extends Stage {
		public int execute(String args) throws PipeException {
			assertEquals(0, addStream(BOTH));
			assertEquals(0, addStream());
			assertEquals(0, addStream(INPUT, "blah"));
			assertEquals(0, addStream(BOTH));
			assertEquals(-165, addStream(BOTH, "BOTH BOTH BOTH"));  // too many parms
			assertEquals(0, addStream(BOTH, "BOTH"));               // stream should be called BOTH
			assertEquals(-165, addStream(BOTH, "broken"));          // invalid stream name
			assertEquals(-164, addStream(100));                     // invalid direction
			assertEquals(0, addStream(BOTH, "aa"));
			assertEquals(-174, addStream(INPUT, "aa"));             // already exists
			assertEquals(-174, addStream(OUTPUT, "aa"));            // already exists
			return 0;
		}
	}
}
