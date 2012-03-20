package com.hae.pipe;

import java.util.*;

import junit.framework.*;

public class TestCommandShort extends TestCase {
	public static final String COPYRIGHT = "Copyright 2007,2012. H.A. Enterprises Pty Ltd. All Rights Reserved";
	
	public void test() {
		Pipe.register("shorttest", ShortTest.class);
		Pipe pipe = new Pipe();
		assertEquals(0, pipe.run("literal 4|literal 3|literal 2|literal 1|shorttest 2|stem results"));
		ArrayList<?> results = (ArrayList<?>)pipe.getParameters().get("results");
		assertEquals(2, results.size());
		assertEquals("3", results.get(0));
		assertEquals("4", results.get(1));
	}
	
	public static class ShortTest extends Stage {
		public int execute(String args) throws PipeException {
			int count = Integer.parseInt(args.trim());
			for (int i = 0; i < count; i++)
				readto();
			shortStreams();
			return 0;
		}
	}

}
