package com.hae.pipe;

import java.util.*;

import junit.framework.*;

public class TestCommandCommitSequencing extends TestCase {
	public void test() {
		System.out.print("Running commit tests... wait 2 secs....");

		Pipe.register("commitsleeper", CommitSleeperTest.class);
		new Pipe().run("commitsleeper 500| commitsleeper 2000");

		System.out.println("done!");
		
		long[] times = new long[CommitSleeperTest.results.size()];
		int[] stageNos = new int[CommitSleeperTest.results.size()];
		String[] msgs = new String[CommitSleeperTest.results.size()];
		for (int i = 0; i < CommitSleeperTest.results.size(); i++) {
			String[] words = PipeUtil.split((String)CommitSleeperTest.results.get(i));
			times[i] = Long.parseLong(words[0]);
			stageNos[i] = Integer.parseInt(words[1]);
			msgs[i] = words[2];
		}
		assertEquals(msgs[0], "precommit-10");
		assertEquals(msgs[1], "precommit-10");
	}
	
	public static class CommitSleeperTest extends Stage {
		public static ArrayList<String> results = new ArrayList<String>();
		public int execute(String args) throws PipeException {
			int time = Integer.parseInt(args);
			msg("precommit-10");
			commit(-10);
			msg("postcommit-10");
			try { Thread.sleep(time); } catch(InterruptedException e){}
			msg("wokeup-10");

			msg("precommit-5");
			commit(-5);
			msg("postcommit-5");
			try { Thread.sleep(time); } catch(InterruptedException e){}
			msg("wokeup-5");

			commit(0);
			msg("exiting");
			return 0;
		}
		private void msg(String s) {
			results.add(System.currentTimeMillis()+" "+getStageNumber()+" "+s);
		}
	}

}
