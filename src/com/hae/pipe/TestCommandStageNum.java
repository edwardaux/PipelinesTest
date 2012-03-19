package com.hae.pipe;

import junit.framework.*;

public class TestCommandStageNum extends TestCase {
	public void test() {
		Pipe.register("stagenum", StageNumManual.class);
		assertEquals(0,   new Pipe().run("literal a| stagenum"));
		assertEquals(127, new Pipe().run("stagenum"));
		
		Pipe.register("stagenum", StageNum.class);
		assertEquals(0, new Pipe().run("stagenum 1 | stagenum 2 | stagenum 3 | literal a | stagenum 5"));
	}
	
	public static class StageNum extends Stage {
		public int execute(String args) throws PipeException {
			int expected = Integer.parseInt(args.trim());
			assertEquals(expected, stageNum());
			return 0;
		}
	}

	public static class StageNumManual extends Stage {
		public int execute(String args) throws PipeException {
			stageNum();
			if (RC == 1) {
				issueMessage(127, "TPOSXX");
				return RC;
			}
			
			return 0;
		}
	}


}
