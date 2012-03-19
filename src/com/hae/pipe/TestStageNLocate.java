package com.hae.pipe;

import junit.framework.*;

public class TestStageNLocate extends TestCase {
	public void testSyntax() {
		assertEquals(0, new Pipe().run("literal a | nlocate /a/"));
		assertEquals(0, new Pipe().run("literal a | notlocate /a/"));
		assertEquals(0, new Pipe().run("literal a | nlocate anycase /a/"));
		assertEquals(0, new Pipe().run("literal a | nlocate anycase mixed /a/"));
		assertEquals(0, new Pipe().run("literal a | nlocate anycase ones /a/"));
		assertEquals(-60, new Pipe().run("literal a | nlocate anycase mixed ones /a/"));
		assertEquals(0, new Pipe().run("literal a | nlocate mixed /a/"));
		assertEquals(0, new Pipe().run("literal a | nlocate 1.* /a/"));
		assertEquals(0, new Pipe().run("literal a | nlocate 3-10 /a/"));
		assertEquals(0, new Pipe().run("literal a | nlocate (1-5 3-10) /a/"));
		assertEquals(0, new Pipe().run("literal a | nlocate caseignore (1-5 3-10) /a/"));
		assertEquals(0, new Pipe().run("literal a | nlocate caseignore (1-5 3-10) anyof /a/"));
		assertEquals(0, new Pipe().run("literal a | nlocate caseignore (1-5 3-10) anyof"));
		assertEquals(0, new Pipe().run("literal a | nlocate caseany mixed (1-5 3-10 10-*) anyof x323232"));
		assertEquals(0, new Pipe().run("literal a | nlocate b11110000"));
	}

	public void testNLocate() {
		Pipe pipe = new Pipe();
		assertEquals(0, pipe.run("zzzgen 3 | nlocate /a/ | zzzcheck /b/c/"));
		assertEquals(0, pipe.run("zzzgen 3 | nlocate /A/ | literal x| zzzcheck /x/a/b/c/"));
		assertEquals(0, pipe.run("zzzgen 3 | nlocate anycase /A/ | zzzcheck /b/c/"));
		assertEquals(0, pipe.run("literal 4444|literal 333|literal 22|literal 1| nlocate 2| zzzcheck /1/"));
		assertEquals(0, pipe.run("literal 4444|literal 333|literal 22|literal 1| nlocate w1| literal x|zzzcheck /x/"));
		assertEquals(0, pipe.run("literal 444 4|literal 333|literal 22|literal 1| nlocate w2| zzzcheck /1/22/333/"));
		assertEquals(0, pipe.run("literal a|literal|literal b| nlocate | zzzcheck //"));

		assertEquals(0, pipe.run("literal hello there|literal I am here| nlocate /here/ | literal x| zzzcheck /x/"));
		assertEquals(0, pipe.run("literal hello there|literal I am here| nlocate anycase /HE/ | literal x|zzzcheck /x/"));
		assertEquals(0, pipe.run("literal hello there|literal I am here| nlocate anycase 2-5 /HE/ | literal x|zzzcheck /x/I am here/hello there/"));
		assertEquals(0, pipe.run("literal hello there|literal I am here| nlocate (1.2 2-5) /he/ | literal x|zzzcheck /x/I am here/"));
		assertEquals(0, pipe.run("literal hello there|literal I am here| nlocate anycase (1.2 2-5 6-7) /HE/ | literal x|zzzcheck /x/"));
		assertEquals(0, pipe.run("literal hello there|literal I am here| nlocate anycase (1.2 2-5 7-8) /HE/ | literal x|zzzcheck /x/I am here/"));

		assertEquals(0, pipe.run("literal a-b-c|literal d-e-f| nlocate wordsep - w3 /c/ |zzzcheck /d-e-f/"));
		assertEquals(0, pipe.run("literal a?b?|literal e??f| nlocate fieldsep ? f2-3 /f/ |zzzcheck /a?b?/"));
		assertEquals(0, pipe.run("literal ?ab?c??a|literal ab?c?a| nlocate (ws ? w1 w3) /a/ |literal x|zzzcheck /x/"));
		assertEquals(0, pipe.run("literal afbc|literal adef|literal ghfi|literal fjkl| nlocate -2;-1 /f/ |zzzcheck /fjkl/afbc/"));
		
		pipe.addParam("colours", new String[] { "red apples", "white flag", "roses are red", "grass is green", "barry white", "white christmas", "blue bayou", "helen reddy" });
		assertEquals(0, pipe.run("stem colours | nlocate /red/ |zzzcheck /white flag/grass is green/barry white/white christmas/blue bayou/"));
		assertEquals(0, pipe.run("(end ?) stem colours | l: nlocate /red/ | nlocate /white/ | zzzcheck /grass is green/blue bayou/")); 
		
		assertEquals(0, pipe.run("literal aaa|literal bbb|literal ccc|literal ddd| nlocate anyof /a/ |zzzcheck /ddd/ccc/bbb/"));
		assertEquals(0, pipe.run("literal aaa|literal bbb|literal ccc|literal ddd| nlocate anyof /ac/ |zzzcheck /ddd/bbb/"));
		assertEquals(0, pipe.run("literal aaa|literal bbb|literal ccc|literal ddd| nlocate anyof /abc/ |zzzcheck /ddd/"));
		assertEquals(0, pipe.run("literal aaa|literal bbb|literal ccc|literal ddd| nlocate anyof /zyxabc/ |zzzcheck /ddd/"));
		assertEquals(0, pipe.run("literal aaa|literal bbb|literal ccc|literal ddd| nlocate anyof |literal x|zzzcheck /x/"));
		
		assertEquals(0, pipe.run("literal one|literal two|literal three|literal four|literal five|locate 4|nlocate 5|zzzcheck /five/four/"));
	}
}
