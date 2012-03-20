package com.hae.pipe;

import junit.framework.*;

public class TestCommandSetRC extends TestCase {
	public void test() {
		Pipe.register("producer", Producer.class);
		Pipe.register("consumer", Consumer.class);
		assertEquals(0, new Pipe().run("producer | consumer"));
	}
	
	public static class Consumer extends Stage {
		public int execute(String args) throws PipeException {
			assertEquals("hello", peekto());
			assertEquals("hello", readto());
			assertEquals(0, RC);
			assertEquals("there", peekto());
			setRC(123);
			assertEquals("there", readto());
			assertEquals(0, RC);
			assertEquals("how are you?", peekto());
			assertEquals("how are you?", readto());
			assertEquals(0, RC);
			assertEquals(null, peekto());
			assertEquals(12, RC);
			return 0;
		}
	}
	public static class Producer extends Stage {
		public int execute(String args) throws PipeException {
			assertEquals(0, output("hello"));
			assertEquals(123, output("there"));
			assertEquals(0, output("how are you?"));
			return 0;
		}
	}


}
