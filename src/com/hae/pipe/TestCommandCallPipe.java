package com.hae.pipe;

import junit.framework.*;

public class TestCommandCallPipe extends TestCase {
	public void testSyntax() {
		Pipe.register("callpipesyntax", SyntaxTest.class);
		assertEquals(0, new Pipe().run("callpipesyntax"));
	}

	public void testCallPipe() {
		Pipe.register("simple", Simple.class);
		Pipe.register("take2", Take2.class);
		Pipe.register("mygen", MyGen.class);
		Pipe.register("twostreams", TwoStreams.class);
		Pipe.register("twostreamsrev", TwoStreamsReversed.class);
		
		assertEquals(0, new Pipe().run("zzzgen 1 | simple | zzzcheck /before/change=a/after/"));
		assertEquals(0, new Pipe().run("zzzgen 2 | simple | literal x| zzzcheck /x/before/change=a/change=b/after/"));
		assertEquals(0, new Pipe().run("zzzgen 3 | take2  | literal x| zzzcheck /x/before/c/"));
		assertEquals(0, new Pipe().run("literal 2|literal 3|literal 1| mygen | zzzcheck /a/a/b/c/a/b/"));

		assertEquals(0, new Pipe().run("(end ?) zzzgen 5 | t: twostreams | zzzcheck /a/ ? t: | zzzcheck /b/c/d/e/"));
		assertEquals(0, new Pipe().run("(end ?) zzzgen 5 | t: twostreamsrev | zzzcheck /b/c/d/e/ ? t: | zzzcheck /a/"));
	}

	public static class SyntaxTest extends Stage {
		public int execute(String args) throws PipeException {
			assertEquals(-193, callpipe("*.input.1"));
			assertEquals(-195, callpipe("*.input.1:"));
			assertEquals(-98, callpipe("*.input.1: literal a | cons"));
			assertEquals(-99, callpipe("literal a | *.input.1: | cons"));
			assertEquals(-191, callpipe("*input.1:"));
			assertEquals(-103, callpipe("*.xxx.1: | cons"));   // strictly speaking, this should probably return -100
			return 0;
		}
	}
	
	public static class Simple extends Stage {
		public int execute(String args) throws PipeException {
			signalOnError();
			output("before");
			callpipe("*: | change // /change=/ | *:");
			output("after");
			return 0;
		}
	}
	
	public static class Take2 extends Stage {
		public int execute(String args) throws PipeException {
			signalOnError();
			try {
				output("before");
				callpipe("*: | take 2 | hole");
				while (true) {
					String s = peekto();
					output(s);
					readto();
				}
			}
			catch(EOFException e) {
			}
			return 0;
		}
	}

	public static class MyGen extends Stage {
		public int execute(String args) throws PipeException {
			signalOnError();
			try {
				while (true) {
					String s = peekto();
					callpipe("zzzgen "+s+" | *:");
					readto();
				}
			}
			catch(EOFException e) {
			}
			return 0;
		}
	}
	
	public static class TwoStreams extends Stage {
		public int execute(String args) throws PipeException {
			callpipe("(end ?) *: | l: locate /a/ | *.OUTPUT.0: ? l: | *.OUTPUT.1:");
			return 0;
		}
	}

	public static class TwoStreamsReversed extends Stage {
		public int execute(String args) throws PipeException {
			callpipe("(end ?) *: | l: locate /a/ | *.OUTPUT.1: ? l: | *.OUTPUT.0:");
			return 0;
		}
	}
}
