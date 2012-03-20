package com.hae.pipe;

import junit.framework.*;

public class TestCommandMaxStream extends TestCase {
	public static final String COPYRIGHT = "Copyright 2007,2012. H.A. Enterprises Pty Ltd. All Rights Reserved";
	
	public void test() {
		Pipe.register("streamtest", MaxStreamTest.class);
		assertEquals(0, new Pipe().run("streamtest"));

		Pipe.register("tmaxstr", MaxStreamTestManual.class);
		assertEquals(222, new Pipe().run("tmaxstr"));
		assertEquals(0,   new Pipe().run("(end ?) m: tmaxstr ? m:"));
		assertEquals(264, new Pipe().run("(end ?) m: tmaxstr ? m: ? m:"));
	}
	
	public static class MaxStreamTest extends Stage {
		public int execute(String args) throws PipeException {
			assertEquals(0, maxStream(INPUT));
			assertEquals(0, maxStream(OUTPUT));
			assertEquals(-164, maxStream(BOTH));
			assertEquals(0, addStream(BOTH));
			assertEquals(1, maxStream(INPUT));
			assertEquals(1, maxStream(OUTPUT));
			assertEquals(0, addStream(INPUT));
			assertEquals(2, maxStream(INPUT));
			assertEquals(1, maxStream(OUTPUT));
			return 0;
		}
	}

	public static class MaxStreamTestManual extends Stage {
		public int execute(String args) throws PipeException {
			/*
			'maxstream output'
			select
			    when RC=0
			       Then 'issuemsg 222 TMAXST'
			    when RC>1
			       Then 'issuemsg 264 TMAXST'
			    otherwise
			       nop
			end
			If RC/=1
			    Then exit RC
	    */
			maxStream(OUTPUT);
			if (RC == 0)
				issueMessage(222, "TMAXST");
			else if (RC > 1)
				issueMessage(264, "TMAXST");
			
			if (RC != 1)
				return RC;
			else
				return 0;
		}
	}
}
