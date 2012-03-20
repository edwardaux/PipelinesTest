package com.hae.pipe;

import junit.framework.*;

public class TestCommandStreamState extends TestCase {
	public static final String COPYRIGHT = "Copyright 2007,2012. H.A. Enterprises Pty Ltd. All Rights Reserved";
	
	public void test() {
		Pipe.register("streamtest", StreamStateTest.class);
		assertEquals(0, new Pipe().run("literal a | streamtest | literal b"));
	}
	
	public static class StreamStateTest extends Stage {
		public int execute(String args) throws PipeException {
			assertEquals(0, streamState(INPUT));
			assertEquals(0, streamState(OUTPUT));
			assertEquals(0, streamState(SUMMARY));
			assertEquals(-164, streamState(100));
			assertEquals("0:0", streamStateAll());

			assertEquals(0, addStream(OUTPUT));
			assertEquals(0, streamState(OUTPUT, 0));   // should still be connected
			assertEquals(12, streamState(OUTPUT, 1));  // should be defined but not connected
			assertEquals(-4, streamState(OUTPUT, 2));  // shouldn't be defined
			assertEquals("0:0 -4:12", streamStateAll());
			assertEquals(0, select(OUTPUT, 0));
			assertEquals(0, sever(OUTPUT));
			assertEquals(12, streamState(OUTPUT, 0));  // shouldn't be connected now
			assertEquals(12, streamState(OUTPUT, 1));  // should be defined but not connected
			assertEquals(-4, streamState(OUTPUT, 2));  // shouldn't be defined
			assertEquals(-4, streamState(SUMMARY));
			assertEquals("0:12 -4:12", streamStateAll());

			return 0;
		}
	}
}
