package com.hae.pipe;

import junit.framework.*;

public class TestStageSpecs extends TestCase {
	public void testSyntax() {
		Pipe pipe = new Pipe();
		assertEquals(-11, pipe.run("literal a| spec | hole"));
		assertEquals(-60, pipe.run("literal a | spec reno | hole"));
		assertEquals(-63, pipe.run("literal a | spec recno nb | hole"));

		assertEquals(0, pipe.run("literal blah| spec 1-* c2x 1 | spec 1-* x2c 1 | zzzcheck /blah/"));
		assertEquals(0, pipe.run("literal 123| spec 1-* d2c 1 | spec 1-* c2d 1 | zzzcheck /        123/"));
		assertEquals(0, pipe.run("literal blah| spec 1-* c2b 1 | spec 1-* b2c 1 | zzzcheck /blah/"));
		assertEquals(0, pipe.run("literal 123.45| spec 1-* f2c 1 | spec 1-* c2f 1 | zzzcheck /123.45/"));
		assertEquals(0, pipe.run("literal 20070727| spec 1-* i2c 1 | spec 1-* c2i 1 | zzzcheck /20070727000000/"));
		assertEquals(0, pipe.run("literal blah| spec 1-* v2c 1 | spec 1-* c2v 1 | zzzcheck /blah/"));
	}

	public void testSpecs() {
		Pipe pipe = new Pipe();
		assertEquals(0, pipe.run("zzzgen 2 | literal abcdefgh| literal mnopqrstuvwxyz| spec recno 1 1-* n /blah/ nw | zzzcheck /         1mnopqrstuvwxyz blah/         2abcdefgh blah/         3a blah/         4b blah/"));
		assertEquals(0, pipe.run("literal | spec number 1 | zzzcheck /         1/"));
		assertEquals(0, pipe.run("literal | spec recno 1 | zzzcheck /         1/"));
		assertEquals(0, pipe.run("literal | spec recno 1 l /a/ n | zzzcheck /1         a/"));
		assertEquals(0, pipe.run("literal | spec recno 1 r /a/ n | zzzcheck /         1a/"));
		assertEquals(0, pipe.run("literal | spec recno 1 c /a/ n | zzzcheck /    1     a/"));
		assertEquals(0, pipe.run("literal | spec recno 1 recno n | zzzcheck /         1         1/"));
		assertEquals(0, pipe.run("literal | spec recno 1 l recno n | zzzcheck /1                  1/"));
		
		assertEquals(0, pipe.run("literal | spec /abc/ 1 | zzzcheck /abc/"));
		assertEquals(0, pipe.run("literal | spec /abc/ 1 l /a/ n | zzzcheck /abca/"));
		assertEquals(0, pipe.run("literal | spec /abc/ 1 r /a/ n | zzzcheck /abca/"));
		assertEquals(0, pipe.run("literal | spec /abc/ 1 c /a/ n | zzzcheck /abca/"));
		assertEquals(0, pipe.run("literal | spec /abc/ 1 l /abc/ n | zzzcheck /abcabc/"));
		
		assertEquals(0, pipe.run("literal abcdefgh| spec 2-5 1 | zzzcheck /bcde/"));
		assertEquals(0, pipe.run("literal abcdefgh| spec 2-5 1 l /a/ n | zzzcheck /bcdea/"));
		assertEquals(0, pipe.run("literal abcdefgh| spec 2-5 1 r /a/ n | zzzcheck /bcdea/"));
		assertEquals(0, pipe.run("literal abcdefgh| spec 2-5 1 c /a/ n | zzzcheck /bcdea/"));
		assertEquals(0, pipe.run("literal abcdefgh| spec 2-5 1 l 2-5 n | zzzcheck /bcdebcde/"));
		
		assertEquals(0, pipe.run("literal abcdefgh| spec 2-5 3.4 | zzzcheck /  bcde/"));
		assertEquals(0, pipe.run("literal abcdefgh| spec 2-3 3.4 | zzzcheck /  bc  /"));
		assertEquals(0, pipe.run("literal abcdefgh| spec 2-5 3.2 | zzzcheck /  bc/"));
		assertEquals(0, pipe.run("literal abcdefgh| spec 2-5 3.4 left | zzzcheck /  bcde/"));
		assertEquals(0, pipe.run("literal abcdefgh| spec 2-3 3.4 left | zzzcheck /  bc  /"));
		assertEquals(0, pipe.run("literal abcdefgh| spec 2-5 3.2 left | zzzcheck /  bc/"));
		assertEquals(0, pipe.run("literal abcdefgh| spec 2-5 3.4 right | zzzcheck /  bcde/"));
		assertEquals(0, pipe.run("literal abcdefgh| spec 2-3 3.4 right | zzzcheck /    bc/"));
		assertEquals(0, pipe.run("literal abcdefgh| spec 2-5 3.2 right | zzzcheck /  de/"));
		assertEquals(0, pipe.run("literal abcdefgh| spec 2-5 3.4 center | zzzcheck /  bcde/"));
		assertEquals(0, pipe.run("literal abcdefgh| spec 2-3 3.4 center | zzzcheck /   bc /"));
		assertEquals(0, pipe.run("literal abcdefgh| spec 2-5 3.2 center | zzzcheck /  cd/"));

		assertEquals(0, pipe.run("literal abcdefgh| spec 1.3 n 4.3 n 1.3 n | zzzcheck /abcdefabc/"));
		assertEquals(0, pipe.run("literal abcdefgh| spec 1.3 nw 4.3 nw 1.3 nw | zzzcheck /abc def abc/"));
		assertEquals(0, pipe.run("literal abcdefgh| spec 1.3 nf 4.3 nf 1.3 nf | zzzcheck /abc\tdef\tabc/"));

		assertEquals(0, pipe.run("literal abcdefgh| spec 1.3 n.3  4.3 n.3  1.3 n.3 | zzzcheck /abcdefabc/"));
		assertEquals(0, pipe.run("literal abcdefgh| spec 1.3 nw.3 4.3 nw.3 1.3 nw.3 | zzzcheck /abc def abc/"));
		assertEquals(0, pipe.run("literal abcdefgh| spec 1.3 nf.3 4.3 nf.3 1.3 nf.3 | zzzcheck /abc\tdef\tabc/"));
		assertEquals(0, pipe.run("literal abcdefgh| spec 1.3 n.5  4.3 n.5  1.3 n.5 | zzzcheck /abc  def  abc  /"));
		assertEquals(0, pipe.run("literal abcdefgh| spec 1.3 nw.5 4.3 nw.5 1.3 nw.5 | zzzcheck /abc   def   abc  /"));
		assertEquals(0, pipe.run("literal abcdefgh| spec 1.3 nf.5 4.3 nf.5 1.3 nf.5 | zzzcheck /abc  \tdef  \tabc  /"));
		assertEquals(0, pipe.run("literal abcdefgh| spec 1.3 n.2  4.3 n.2  1.3 n.2 | zzzcheck /abdeab/"));
		assertEquals(0, pipe.run("literal abcdefgh| spec 1.3 nw.2 4.3 nw.2 1.3 nw.2 | zzzcheck /ab de ab/"));
		assertEquals(0, pipe.run("literal abcdefgh| spec 1.3 nf.2 4.3 nf.2 1.3 nf.2 | zzzcheck /ab\tde\tab/"));

		assertEquals(0, pipe.run("literal abcdefgh| spec 1.3 nw.5 right 4.3 nw.5 c 1.3 nw.5 ri | zzzcheck /  abc  def    abc/"));

		assertEquals(0, pipe.run("literal   abc  | spec 1-* 1 | zzzcheck /  abc  /"));
		assertEquals(0, pipe.run("literal   abc  | spec 1-* strip 1 | zzzcheck /abc/"));
		assertEquals(0, pipe.run("literal   abc  def| spec 1-7 strip 1 8.3 n | zzzcheck /abcdef/"));

		assertEquals(0, pipe.run("literal a b c d e f | spec w1 1 | zzzcheck /a/"));
		assertEquals(0, pipe.run("literal a b c d e f | spec w1 n w2 n w3 n w4-6 nw | zzzcheck /abc d e f/"));

		assertEquals(0, pipe.run("literal a | spec pad _ 1.1 1.5 | zzzcheck /a____/"));
		assertEquals(0, pipe.run("literal a b | spec pad _ w1 1.5 pad + w2 n.4 right | zzzcheck /a____+++b/"));

		assertEquals(0, pipe.run("literal blah| spec 1-* c2x 1 | spec 1-* x2c 1 | zzzcheck /blah/"));
		assertEquals(0, pipe.run("literal 123| spec 1-* d2c 1 | spec 1-* c2d 1 | zzzcheck /        123/"));
		assertEquals(0, pipe.run("literal blah| spec 1-* c2b 1 | spec 1-* b2c 1 | zzzcheck /blah/"));
		assertEquals(0, pipe.run("literal 123.45| spec 1-* f2c 1 | spec 1-* c2f 1 | zzzcheck /123.45/"));
		assertEquals(0, pipe.run("literal 20070727| spec 1-* i2c 1 | spec 1-* c2i 1 | zzzcheck /20070727000000/"));
		assertEquals(0, pipe.run("literal blah| spec 1-* v2c 1 | spec 1-* c2v 1 | zzzcheck /blah/"));
	}
}
