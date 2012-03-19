package com.hae.pipe;

import java.util.*;

import junit.framework.*;

public class TestStageStem extends TestCase {
	public void testSyntax() {
		assertEquals(0, new Pipe().run("stem blah"));
		assertEquals(-112, new Pipe().run("stem blah foo"));
		assertEquals(0, new Pipe().run("stem blah from 1"));
		assertEquals(-58, new Pipe().run("stem blah from xxx"));
		assertEquals(-14, new Pipe().run("stem blah append from 5"));
		assertEquals(-231, new Pipe().run("stem"));
	}
	
	public void testStem2Stem() {
		ArrayList input = new ArrayList();
		input.add("a"); input.add("b"); input.add("c");
		
		Pipe pipe = new Pipe();
		pipe.addParam("input", input);
		
		assertEquals(0, pipe.run("stem input | stem output"));

		ArrayList output = (ArrayList)pipe.getParameters().get("output");
		assertEquals("a", (String)output.get(0));
		assertEquals("b", (String)output.get(1));
		assertEquals("c", (String)output.get(2));
	}

	public void testLiteral2Stem() {
		Pipe pipe = new Pipe();
		assertEquals(0, pipe.run("literal c|literal b|literal a| stem output"));

		ArrayList output = (ArrayList)pipe.getParameters().get("output");
		assertEquals("a", (String)output.get(0));
		assertEquals("b", (String)output.get(1));
		assertEquals("c", (String)output.get(2));
	}

	public void testStem2Stem2Stem() {
		ArrayList input = new ArrayList();
		input.add("a"); input.add("b"); input.add("c");
		
		Pipe pipe = new Pipe();
		pipe.addParam("input", input);
		
		assertEquals(0, pipe.run("stem input | stem output | stem output2"));

		ArrayList output = (ArrayList)pipe.getParameters().get("output");
		assertEquals("a", (String)output.get(0));
		assertEquals("b", (String)output.get(1));
		assertEquals("c", (String)output.get(2));
		ArrayList output2 = (ArrayList)pipe.getParameters().get("output2");
		assertEquals("a", (String)output2.get(0));
		assertEquals("b", (String)output2.get(1));
		assertEquals("c", (String)output2.get(2));
	}

	public void testStemMissingInput() {
		Pipe pipe = new Pipe();
		
		assertEquals(0, pipe.run("stem missing | stem output"));

		ArrayList output = (ArrayList)pipe.getParameters().get("output");
		assertEquals(0, output.size());
	}

	public void testOverwrite1() {
		ArrayList input = new ArrayList();
		input.add("a"); input.add("b"); input.add("c");
		ArrayList output = new ArrayList();
		output.add("d"); output.add("e"); 
		
		Pipe pipe = new Pipe();
		pipe.addParam("input", input);
		pipe.addParam("output", output);
		
		assertEquals(0, pipe.run("stem input | stem output"));

		output = (ArrayList)pipe.getParameters().get("output");
		assertEquals("a", (String)output.get(0));
		assertEquals("b", (String)output.get(1));
		assertEquals("c", (String)output.get(2));
	}

	public void testOverwrite2() {
		ArrayList input = new ArrayList();
		input.add("a"); input.add("b"); input.add("c");
		ArrayList output = new ArrayList();
		output.add("d"); output.add("e"); output.add("f"); output.add("g"); output.add("h"); 
		
		Pipe pipe = new Pipe();
		pipe.addParam("input", input);
		pipe.addParam("output", output);
		
		assertEquals(0, pipe.run("stem input | stem output"));

		output = (ArrayList)pipe.getParameters().get("output");
		assertEquals(5, output.size());
		assertEquals("a", (String)output.get(0));
		assertEquals("b", (String)output.get(1));
		assertEquals("c", (String)output.get(2));
		assertEquals("g", (String)output.get(3));
		assertEquals("h", (String)output.get(4));
	}

	public void testFrom1() {
		ArrayList input = new ArrayList();
		input.add("a"); input.add("b"); input.add("c");
		
		Pipe pipe = new Pipe();
		pipe.addParam("input", input);
		
		assertEquals(0, pipe.run("stem input from 2 | stem output"));

		ArrayList output = (ArrayList)pipe.getParameters().get("output");
		assertEquals(2, output.size());
		assertEquals("b", (String)output.get(0));
		assertEquals("c", (String)output.get(1));
	}

	public void testFrom2() {
		ArrayList input = new ArrayList();
		input.add("a"); input.add("b"); input.add("c");
		
		Pipe pipe = new Pipe();
		pipe.addParam("input", input);
		
		assertEquals(0, pipe.run("stem input from 5 | stem output"));

		ArrayList output = (ArrayList)pipe.getParameters().get("output");
		assertEquals(0, output.size());
	}

	public void testFrom3() {
		ArrayList input = new ArrayList();
		input.add("a"); input.add("b"); input.add("c");
		
		Pipe pipe = new Pipe();
		pipe.addParam("input", input);
		
		assertEquals(0, pipe.run("stem input from 2 | stem output from 3"));

		ArrayList output = (ArrayList)pipe.getParameters().get("output");
		assertEquals(4, output.size());
		assertEquals(null, (String)output.get(0));
		assertEquals(null, (String)output.get(1));
		assertEquals("b", (String)output.get(2));
		assertEquals("c", (String)output.get(3));
	}

	public void testTypes1() {
		ArrayList input = new ArrayList();
		input.add("a"); input.add(null); input.add("c");
		
		Pipe pipe = new Pipe();
		pipe.addParam("input", input);
		
		assertEquals(0, pipe.run("stem input | stem output "));

		ArrayList output = (ArrayList)pipe.getParameters().get("output");
		assertEquals(3, output.size());
		assertEquals("a", (String)output.get(0));
		assertEquals("", (String)output.get(1));
		assertEquals("c", (String)output.get(2));
	}

	public void testTypes2() {
		ArrayList input = new ArrayList();
		input.add(new Integer(123)); input.add(null); input.add(new Character('x'));
		
		Pipe pipe = new Pipe();
		pipe.addParam("input", input);
		
		assertEquals(0, pipe.run("stem input | stem output "));

		ArrayList output = (ArrayList)pipe.getParameters().get("output");
		assertEquals(3, output.size());
		assertEquals("123", (String)output.get(0));
		assertEquals("", (String)output.get(1));
		assertEquals("x", (String)output.get(2));
	}

	public void testArray() {
		Pipe pipe = new Pipe();
		pipe.addParam("input", new String[] { "a", null, "c" });
		
		assertEquals(0, pipe.run("stem input | stem output "));

		ArrayList output = (ArrayList)pipe.getParameters().get("output");
		assertEquals(3, output.size());
		assertEquals("a", (String)output.get(0));
		assertEquals("", (String)output.get(1));
		assertEquals("c", (String)output.get(2));
	}
}
