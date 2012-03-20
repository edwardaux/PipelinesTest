package com.hae.pipe;

import junit.framework.*;

public class TestStageLocate extends TestCase {
	public void testSyntax() {
		assertEquals(0, new Pipe().run("literal a | locate /a/"));
		assertEquals(0, new Pipe().run("literal a | locate anycase /a/"));
		assertEquals(0, new Pipe().run("literal a | locate anycase mixed /a/"));
		assertEquals(0, new Pipe().run("literal a | locate anycase ones /a/"));
		assertEquals(-60, new Pipe().run("literal a | locate anycase mixed ones /a/"));
		assertEquals(0, new Pipe().run("literal a | locate mixed /a/"));
		assertEquals(0, new Pipe().run("literal a | locate 1.* /a/"));
		assertEquals(0, new Pipe().run("literal a | locate 3-10 /a/"));
		assertEquals(0, new Pipe().run("literal a | locate (1-5 3-10) /a/"));
		assertEquals(0, new Pipe().run("literal a | locate caseignore (1-5 3-10) /a/"));
		assertEquals(0, new Pipe().run("literal a | locate caseignore (1-5 3-10) anyof /a/"));
		assertEquals(0, new Pipe().run("literal a | locate caseignore (1-5 3-10) anyof"));
		assertEquals(0, new Pipe().run("literal a | locate caseany mixed (1-5 3-10 10-*) anyof x323232"));
		assertEquals(0, new Pipe().run("literal a | locate b11110000"));
	}

	public void testLocate() {
		Pipe pipe = new Pipe();
		assertEquals(0, pipe.run("zzzgen 3 | locate /a/ | zzzcheck /a/"));
		assertEquals(0, pipe.run("zzzgen 3 | locate /A/ | literal x| zzzcheck /x/"));
		assertEquals(0, pipe.run("zzzgen 3 | locate anycase /A/ | zzzcheck /a/"));
		assertEquals(0, pipe.run("literal 4444|literal 333|literal 22|literal 1| locate 2| zzzcheck /22/333/4444/"));
		assertEquals(0, pipe.run("literal 4444|literal 333|literal 22|literal 1| locate w1| zzzcheck /1/22/333/4444/"));
		assertEquals(0, pipe.run("literal 444 4|literal 333|literal 22|literal 1| locate w2| zzzcheck /444 4/"));
		assertEquals(0, pipe.run("literal a|literal|literal b| locate | zzzcheck /b/a/"));

		assertEquals(0, pipe.run("literal hello there|literal I am here| locate /here/ | zzzcheck /I am here/hello there/"));
		assertEquals(0, pipe.run("literal hello there|literal I am here| locate anycase /HE/ | zzzcheck /I am here/hello there/"));
		assertEquals(0, pipe.run("literal hello there|literal I am here| locate anycase 2-5 /HE/ | literal x|zzzcheck /x/"));
		assertEquals(0, pipe.run("literal hello there|literal I am here| locate (1.2 2-5) /he/ | literal x|zzzcheck /x/hello there/"));
		assertEquals(0, pipe.run("literal hello there|literal I am here| locate anycase (1.2 2-5 6-7) /HE/ | literal x|zzzcheck /x/I am here/hello there/"));
		assertEquals(0, pipe.run("literal hello there|literal I am here| locate anycase (1.2 2-5 7-8) /HE/ | literal x|zzzcheck /x/hello there/"));

		assertEquals(0, pipe.run("literal a-b-c|literal d-e-f| locate wordsep - w3 /c/ |zzzcheck /a-b-c/"));
		assertEquals(0, pipe.run("literal a?b?|literal e??f| locate fieldsep ? f2-3 /f/ |zzzcheck /e??f/"));
		assertEquals(0, pipe.run("literal ?ab?c??a|literal ab?c?a| locate (ws ? w1 w3) /a/ |zzzcheck /ab?c?a/?ab?c??a/"));
		assertEquals(0, pipe.run("literal afbc|literal adef|literal ghfi|literal fjkl| locate -2;-1 /f/ |zzzcheck /ghfi/adef/"));
		
		pipe.addParam("colours", new String[] { "red apples", "white flag", "roses are red", "grass is green", "barry white", "white christmas", "blue bayou", "helen reddy" });
		assertEquals(0, pipe.run("stem colours | locate /red/ |zzzcheck /red apples/roses are red/helen reddy/"));
		assertEquals(0, pipe.run("(end ?) stem colours | l: locate /red/ | f: faninany | zzzcheck /red apples/white flag/roses are red/barry white/white christmas/helen reddy/ ? l: | locate /white/ | f:")); 
		
		assertEquals(0, pipe.run("literal aaa|literal bbb|literal ccc|literal ddd| locate anyof /a/ |zzzcheck /aaa/"));
		assertEquals(0, pipe.run("literal aaa|literal bbb|literal ccc|literal ddd| locate anyof /ac/ |zzzcheck /ccc/aaa/"));
		assertEquals(0, pipe.run("literal aaa|literal bbb|literal ccc|literal ddd| locate anyof /abc/ |zzzcheck /ccc/bbb/aaa/"));
		assertEquals(0, pipe.run("literal aaa|literal bbb|literal ccc|literal ddd| locate anyof /zyxabc/ |zzzcheck /ccc/bbb/aaa/"));
		assertEquals(0, pipe.run("literal aaa|literal bbb|literal ccc|literal ddd| locate anyof |zzzcheck /ddd/ccc/bbb/aaa/"));
		
		assertEquals(0, pipe.run("literal one|literal two|literal three|literal four|literal five|locate 4|nlocate 5|zzzcheck /five/four/"));
	}
}
