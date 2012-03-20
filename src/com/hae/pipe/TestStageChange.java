package com.hae.pipe;

import junit.framework.*;

public class TestStageChange extends TestCase {
	public void testSyntax() {
		assertEquals(0, new Pipe().run("literal a | change /a/ /b/ | hole"));
		assertEquals(-200, new Pipe().run("literal a | change ("));
		assertEquals(-55, new Pipe().run("literal a | change () /a/ /b/"));
		assertEquals(-113, new Pipe().run("literal a | change"));
		assertEquals(-113, new Pipe().run("literal a | change /a/"));
		assertEquals(-66, new Pipe().run("literal a | change /a/ /b/ XXX"));
		assertEquals(-66, new Pipe().run("literal a | change /a/ /b/ -1"));
		assertEquals(-198, new Pipe().run("literal a | change // /b/ 2"));
	}

	public void testChange() {
		Pipe pipe = new Pipe();
		assertEquals(0, pipe.run("zzzgen 6| change /a/ /b/ | zzzcheck /b/b/c/d/e/f/"));
		assertEquals(0, pipe.run("literal abc|change // /msg / | zzzcheck /msg abc/"));
		assertEquals(0, pipe.run("literal xabab|literal abab| change /ab/ /cd/ | zzzcheck /cdcd/xcdcd/"));
		assertEquals(0, pipe.run("literal xabab|literal abab| change (1.2 3-*) /ab/ /cd/ | zzzcheck /cdcd/xabcd/"));
		assertEquals(0, pipe.run("literal pipe | change anycase /pipe/ /line/ | zzzcheck /line /"));
		assertEquals(0, pipe.run("literal Pipe | change anycase /pipe/ /line/ | zzzcheck /Line /"));
		assertEquals(0, pipe.run("literal PiPe | change anycase /pipe/ /line/ | zzzcheck /Line /"));
		assertEquals(0, pipe.run("literal PIpe | change anycase /pipe/ /line/ | zzzcheck /LINE /"));
		assertEquals(0, pipe.run("literal PiPe | change anycase /Pipe/ /line/ | zzzcheck /line /"));
		assertEquals(0, pipe.run("literal 1234 | change /2/ b11000010 | zzzcheck /1\u00C234 /"));
		assertEquals(0, pipe.run("literal 1234 | change x32 /second/ | zzzcheck /1second34 /"));
		assertEquals(0, pipe.run("literal 1234 | change x32 x62 | zzzcheck /1b34 /"));
		assertEquals(0, pipe.run("literal abcdefghi ghi| literal hx ghi qh| change w2 /h/ /*/ | zzzcheck /hx g*i qh/abcdefghi g*i/"));
		
		assertEquals(0, pipe.run("(end ?) zzzgen 6| c: change /a/ /b/ | zzzcheck /b/ ? c: | zzzcheck /b/c/d/e/f/"));
		assertEquals(0, pipe.run("(end ?) zzzgen 6| c: change /a/ /b/ | d: change /b/ /x/ | zzzcheck /x/ ? c: | zzzcheck /b/c/d/e/f/ ? d: | literal z| zzzcheck /z/"));
	}
}
