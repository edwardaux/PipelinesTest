package com.hae.pipe;

import junit.framework.*;

public class TestStageChop extends TestCase {
	public static final String COPYRIGHT = "Copyright 2007,2012. H.A. Enterprises Pty Ltd. All Rights Reserved";
	
	public void testSyntax() {
		assertEquals(0, new Pipe().run("literal a | chop 3 | hole"));
		assertEquals(-54, new Pipe().run("literal a | chop anycase anycase"));
		assertEquals(-54, new Pipe().run("literal abcde| chop -4 | hole"));
		assertEquals(-54, new Pipe().run("literal abcde| chop -4 /a/| hole"));
	}

	public void testChop() {
		Pipe pipe = new Pipe();
		String s = "12345678901234567890123456789012345678901234567890123456789012345678901234567890";
		assertEquals(0, pipe.run("literal abcde| chop 3 | zzzcheck /abc/"));
		assertEquals(0, pipe.run("literal abcde| literal mnopq| literal yz| chop 3 | zzzcheck /yz/mno/abc/"));
		assertEquals(0, pipe.run("literal abcde| chop 0 | zzzcheck //"));
		assertEquals(0, pipe.run("literal "+s+"xxxx| chop | zzzcheck /"+s+"/"));

		assertEquals(0, pipe.run("literal abcdefghi| chop d-d | zzzcheck /abc/"));
		assertEquals(0, pipe.run("literal abcdefghi| chop D-D | zzzcheck /abcdefghi/"));
		assertEquals(0, pipe.run("literal abcdefghi| chop anycase D-D | zzzcheck /abc/"));
		assertEquals(0, pipe.run("literal abcdefghi| chop blank | zzzcheck /abcdefghi/"));
		assertEquals(0, pipe.run("literal abc efghi| chop blank | zzzcheck /abc/"));
		assertEquals(0, pipe.run("literal abcdefghi| chop 64-64 | zzzcheck /abc/"));
		assertEquals(0, pipe.run("literal abcdefghijkl| chop 2 before d-f | zzzcheck /a/"));
		assertEquals(0, pipe.run("literal abcdefghijkl| chop 2 after d-f | zzzcheck /abcdefgh/"));
		assertEquals(0, pipe.run("literal abcdefghijkl| chop -2 before d-f | zzzcheck /abcde/"));
		assertEquals(0, pipe.run("literal abcdefghijkl| chop -2 after d-f | zzzcheck /abcd/"));
		assertEquals(0, pipe.run("literal abcdefghi| chop not a-c | zzzcheck /abc/"));

		assertEquals(0, pipe.run("literal ab.cd | chop . | zzzcheck /ab/"));
		assertEquals(0, pipe.run("literal ab.cd | chop b | zzzcheck /a/"));
		assertEquals(0, pipe.run("literal ab-cd | chop - | zzzcheck /ab/"));

		assertEquals(0, pipe.run("literal abcdefghi| chop string /d/ | zzzcheck /abc/"));
		assertEquals(0, pipe.run("literal abcdefghi| chop string /x/ | zzzcheck /abcdefghi/"));
		assertEquals(0, pipe.run("literal abcdefghi| chop 2 before string /e/ | zzzcheck /ab/"));
		assertEquals(0, pipe.run("literal abcdefghi| chop 2 after string /e/ | zzzcheck /abcdefg/"));
		assertEquals(0, pipe.run("literal abcdefghi| chop 2 after string /E/ | zzzcheck /abcdefghi/"));
		assertEquals(0, pipe.run("literal abcdefghi| chop caseignore 2 after string /E/ | zzzcheck /abcdefg/"));
		assertEquals(0, pipe.run("literal abcdefghi| chop 10 before string /c/ | zzzcheck //"));
		assertEquals(0, pipe.run("literal abcdefghi| chop 10 after string /g/ | zzzcheck /abcdefghi/"));
		assertEquals(0, pipe.run("literal abcdefghijkl| chop 2 before string /def/ | zzzcheck /a/"));
		assertEquals(0, pipe.run("literal abcdefghijkl| chop 2 after string /def/ | zzzcheck /abcdefgh/"));
		assertEquals(0, pipe.run("literal abcdefghijkl| chop -2 before string /def/ | zzzcheck /abcde/"));
		assertEquals(0, pipe.run("literal abcdefghijkl| chop -2 after string /def/ | zzzcheck /abcd/"));

		assertEquals(0, pipe.run("literal abcdefghi| chop not string /def/ | zzzcheck //"));
		assertEquals(0, pipe.run("literal abcdefghi| chop not string /abcdef/ | zzzcheck /abcdef/"));
		assertEquals(0, pipe.run("literal zzzzzabcdefg| chop not string /z/ | zzzcheck /zzzzz/"));
		assertEquals(0, pipe.run("literal abababzzzzabab| chop not string /ab/ | zzzcheck /ababab/"));
		assertEquals(0, pipe.run("literal abababzzzzabab| chop not string /lksdfgjlkdsfgdsfd/ | zzzcheck //"));

		assertEquals(0, pipe.run("literal abcdefghi| chop anyof /d/ | zzzcheck /abc/"));
		assertEquals(0, pipe.run("literal abcdefghi| chop anyof /xpdl/ | zzzcheck /abc/"));
		assertEquals(0, pipe.run("literal abcdefghi| chop anyof /xpdal/ | zzzcheck //"));
		assertEquals(0, pipe.run("literal abcdefghijkl| chop 2 before anyof /def/ | zzzcheck /a/"));
		assertEquals(0, pipe.run("literal abcdefghijkl| chop 2 after anyof /def/ | zzzcheck /abcdefgh/"));
		assertEquals(0, pipe.run("literal abcdefghijkl| chop -2 before anyof /def/ | zzzcheck /abcde/"));
		assertEquals(0, pipe.run("literal abcdefghijkl| chop -2 after anyof /def/ | zzzcheck /abcd/"));
		assertEquals(0, pipe.run("literal abcdefghijkl| chop not anyof /abcd/ | zzzcheck /abcd/"));

		assertEquals(0, pipe.run("(end ?) literal abcde| literal mnopq| literal yz| c: chop 3 | zzzcheck /yz/mno/abc/ ? c: | zzzcheck //pq/de/"));
	}
}
