package com.hae.pipe;

import java.io.*;
import java.util.*;

import junit.framework.*;

public class TestStageDiskr extends TestCase {
	public void testSyntax() {
		writeFile("blah", new String[] { "a", "b", "c", "d", "e" });
		assertEquals(0, new Pipe().run("< blah"));
		assertEquals(0, new Pipe().run("diskr blah"));
		assertEquals(-112, new Pipe().run("diskr blah foo"));
		assertEquals(-113, new Pipe().run("diskr"));
		assertEquals(-87, new Pipe().run("literal a | diskr blah"));
	}

	public void testUpperCase() {
		writeFile("BLAH", new String[] { "a", "b", "c", "d", "e" });

		Pipe pipe = new Pipe();
		assertEquals(0, pipe.run("diskr BLAH | zzzcheck /a/b/c/d/e/"));

		assertEquals(0, pipe.run("diskr blah | zzzcheck /a/b/c/d/e/"));

		writeFile("Foo", new String[] { "a", "b", "c", "d", "e" });
		if (isCaseInsensitiveOS())
			assertEquals(0, pipe.run("diskr foo | stem output"));
		else
			assertEquals(-146, pipe.run("diskr foo | stem output"));
	}

	public void testFile2Stem() {
		writeFile("blah", new String[] { "a", "b", "", "d", "e" });
		
		Pipe pipe = new Pipe();
		assertEquals(0, pipe.run("diskr blah | stem output"));

		ArrayList output = (ArrayList)pipe.getParameters().get("output");
		assertEquals(5, output.size());
		assertEquals("a", (String)output.get(0));
		assertEquals("b", (String)output.get(1));
		assertEquals("",  (String)output.get(2));
		assertEquals("d", (String)output.get(3));
		assertEquals("e", (String)output.get(4));
	}

	private void writeFile(String file, String[] contents) {
		try {
			FileWriter writer = new FileWriter(file);
			for (int i = 0; i < contents.length; i++) {
				writer.write(contents[i]+"\n");
			}
			writer.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean isCaseInsensitiveOS() {
		String os = System.getProperty("os.name").toUpperCase();
		return os.contains("WINDOWS") || os.contains("MAC OS X");
	}

	protected void tearDown() throws Exception {
		new File("foo").delete();
		new File("blah").delete();
		super.tearDown();
	}
}
