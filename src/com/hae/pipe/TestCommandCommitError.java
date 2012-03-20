package com.hae.pipe;

import junit.framework.*;

public class TestCommandCommitError extends TestCase {
	public static final String COPYRIGHT = "Copyright 2007,2012. H.A. Enterprises Pty Ltd. All Rights Reserved";
	
	public void test() {
		Pipe.register("commit1", CommitBailTest.class);
		Pipe.register("commit2", CommitBailTest2.class);
		
		// CommitError1 bails out with RC=args
		// CommitError2 tries to commit to 0, and checks RC=args
		assertEquals(0, new Pipe().run("commit1 0| commit2 0"));
		assertEquals(2, new Pipe().run("commit1 2| commit2 2"));
	}
	
	public static class CommitBailTest extends Stage {
		public int execute(String args) {
			return Integer.parseInt(args);
		}
	}

	public static class CommitBailTest2 extends Stage {
		public int execute(String args) throws PipeException {
			try { Thread.sleep(500); } catch(InterruptedException e){}
			int expectedRC = Integer.parseInt(args);
			commit(0);
			assertEquals(expectedRC, RC);
			return 0;
		}
	}
	
}
