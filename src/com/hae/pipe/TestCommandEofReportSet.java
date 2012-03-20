package com.hae.pipe;

import junit.framework.*;

public class TestCommandEofReportSet extends TestCase {
	public static final String COPYRIGHT = "Copyright 2007,2012. H.A. Enterprises Pty Ltd. All Rights Reserved";
	
	public void test() {
		Pipe.register("single", EOFReportTestSingle.class);
		assertEquals(0, new Pipe().run("single"));
		Pipe.register("multiple", EofReportTestMultiple.class);
		assertEquals(0, new Pipe().run("literal a|multiple|literal x"));
	}
	
	public static class EOFReportTestSingle extends Stage {
		public int execute(String args) throws PipeException {
			assertEquals(8, eofReport(ALL));          // only stage, so nothing connected
			assertEquals(8, eofReport(ANY));          // only stage, so nothing connected
			return 0;
		}
	}
	public static class EofReportTestMultiple extends Stage {
		public int execute(String args) throws PipeException {
			assertEquals(-111, eofReport(100));           // invalid stream type
			assertEquals(0, eofReport(CURRENT));
			sever(INPUT);
			assertEquals(0, eofReport(CURRENT));          // should still be ok coz 1 output is connected
			sever(OUTPUT);
			assertEquals(8, eofReport(CURRENT));          // now neither are connected
			return 0;
		}
	}

}
