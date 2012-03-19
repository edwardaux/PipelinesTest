package com.hae.pipe;

import junit.framework.*;

public class TestStageBeat extends TestCase {
	public void testSyntax() {
		assertEquals(0, new Pipe().run("literal a | beat 1 | hole"));   
		assertEquals(0, new Pipe().run("literal a | beat 1 /xx/ | hole"));   
		assertEquals(0, new Pipe().run("literal a | beat once 1 /xx/ | hole"));   
		assertEquals(0, new Pipe().run("literal a | beat ONCE 1 /xx/ | hole"));   
		assertEquals(-102, new Pipe().run("literal a | beat ONCE 1 /xx/"));   
		assertEquals(-58, new Pipe().run("literal a | beat XXX 1 /xx/"));   
		assertEquals(-58, new Pipe().run("literal a | beat 1.2.2 /xx/"));   
	}

	public void testBeat() {
		Pipe.register("testBeatDelay", TestBeatDelay.class);
		Pipe.register("testBeatProduce", TestBeatProduce.class);
		Pipe pipe = new Pipe();
		assertEquals(0, pipe.run("(end ?)literal 500 | literal 1200 | literal 300 | testBeatDelay | b: beat 1 /xx/ | zzzcheck /300/1200/500/ ? b: | zzzcheck /xx/")); 
		assertEquals(0, pipe.run("(end ?)literal 150 | literal 150  | literal 150 | testBeatDelay | b: beat 0.1 /xx/ | zzzcheck /150/150/150/ ? b: | zzzcheck /xx/xx/xx/")); 
//		assertEquals(0, pipe.run("(end ?)literal 250 | literal 250  | literal 250 | testBeatDelay | b: beat 0.1 /xx/ | zzzcheck /250/250/250/ ? b: | zzzcheck /xx/xx/xx/xx/xx/xx/")); 
//		assertEquals(0, pipe.run("(end ?)literal 250 | literal 250  | literal 250 | testBeatDelay | b: beat once 0.1 /xx/ | zzzcheck /250/250/250/ ? b: | zzzcheck /xx/xx/xx/")); 
//		assertEquals(0, pipe.run("(end ?)literal 350 | testBeatDelay | b: beat once 0.1 /xx/ | zzzcheck /350/ ? b: | zzzcheck /xx/xx/xx/"));

		// keep going until no record is produced in 1 second
//		assertEquals(0, pipe.run("(end ?)testBeatProduce | b: beat 1 /xx/ | zzzcheck /100/200/300/ ? b:"));   
	}
	
	public static class TestBeatDelay extends Stage {
		public int execute(String args) throws PipeException {
			signalOnError();
			try {
				while (true) {
					// delay (in time) a record by the passed time (in ms) 
					String s = peekto();
					try { Thread.sleep(Integer.parseInt(s.trim())); } catch(Throwable t) {}
					output(s.trim());
					readto();
				}
			}
			catch(EOFException e) {
				return 0;
			}
		}
	}

	public static class TestBeatProduce extends Stage {
		public int execute(String args) throws PipeException {
			signalOnError();
			try {
				sleepAndOutput(100);
				sleepAndOutput(200);
				sleepAndOutput(300);
				sleepAndOutput(1200);
				sleepAndOutput(400);
			}
			catch(EOFException e) {
			}
			return 0;
		}
		private void sleepAndOutput(int time) throws PipeException {
			try { Thread.sleep(time); } catch(Throwable t) {}
			output(""+time);
		}
	}
}
