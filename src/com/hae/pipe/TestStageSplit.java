package com.hae.pipe;

import junit.framework.*;

public class TestStageSplit extends TestCase {
	public static final String COPYRIGHT = "Copyright 2007,2012. H.A. Enterprises Pty Ltd. All Rights Reserved";
	
	public void testSyntax() {
	}

	public void testSplit() {
		Pipe pipe = new Pipe();
		
		// blanks
		assertEquals(0, pipe.run("literal a bb   ccc dddd eeeee| split | zzzcheck /a/bb/ccc/dddd/eeeee/"));
		assertEquals(0, pipe.run("literal a bb   ccc dddd eeeee | split | zzzcheck /a/bb/ccc/dddd/eeeee/"));
		assertEquals(0, pipe.run("literal abbcccddddeeeee| split | zzzcheck /abbcccddddeeeee/"));
		assertEquals(0, pipe.run("literal abbcccddddeeeee | split | zzzcheck /abbcccddddeeeee/"));
		assertEquals(0, pipe.run("literal a bb   ccc dddd eeeee| split blank | zzzcheck /a/bb/ccc/dddd/eeeee/"));
		assertEquals(0, pipe.run("literal a bb   ccc dddd eeeee| split at | zzzcheck /a/bb/ccc/dddd/eeeee/"));
		assertEquals(0, pipe.run("literal a bb   ccc dddd eeeee| split at blank | zzzcheck /a/bb/ccc/dddd/eeeee/"));

		// min
		assertEquals(0, pipe.run("literal a bb   ccc dddd eeeee| split min 5 | zzzcheck /a bb / ccc dddd/eeeee/"));
		assertEquals(0, pipe.run("literal a bb    ccc dddd eeeee| split min 5 | zzzcheck /a bb /  ccc/dddd eeeee/"));
		assertEquals(0, pipe.run("literal a bb   ccc dddd eeeee| split min 0 | zzzcheck /a/bb/ccc/dddd/eeeee/"));
		assertEquals(0, pipe.run("literal a bb   ccc dddd eeeee| split min 1 | zzzcheck /a/bb/ /ccc/dddd/eeeee/"));
		assertEquals(0, pipe.run("literal a bb   ccc dddd eeeee| split min 20 | zzzcheck /a bb   ccc dddd eeeee/"));
		assertEquals(0, pipe.run("literal a bb   ccc dddd eeeee| split min 21 | zzzcheck /a bb   ccc dddd eeeee/"));
		assertEquals(0, pipe.run("literal a bb   ccc dddd eeeee| split min 22 | zzzcheck /a bb   ccc dddd eeeee/"));

		// string
		assertEquals(0, pipe.run("literal axbbxcccxddddxeeeee| split string /x/ | zzzcheck /a/bb/ccc/dddd/eeeee/"));
		assertEquals(0, pipe.run("literal axbbxcccxddddxeeeee| split string /X/ | zzzcheck /axbbxcccxddddxeeeee/"));
		assertEquals(0, pipe.run("literal axbbxcccxddddxeeeee| split anycase string /x/ | zzzcheck /a/bb/ccc/dddd/eeeee/"));
		assertEquals(0, pipe.run("literal axbbxcccxddddxeeeee| split anycase string /X/ | zzzcheck /a/bb/ccc/dddd/eeeee/"));
		assertEquals(0, pipe.run("literal axbbxxxcccxddddxeeeee| split string /x/ | zzzcheck /a/bb/ccc/dddd/eeeee/"));
		assertEquals(0, pipe.run("literal axbbxxxcccxddddxeeeee| split string /X/ | zzzcheck /axbbxxxcccxddddxeeeee/"));
		assertEquals(0, pipe.run("literal axbbxxxcccxddddxeeeee| split anycase string /X/ | zzzcheck /a/bb/ccc/dddd/eeeee/"));
		assertEquals(0, pipe.run("literal a_bb__ccc_dddd__eeeee| split string /__/ | zzzcheck /a_bb/ccc_dddd/eeeee/"));
		assertEquals(0, pipe.run("literal a_bb___ccc_dddd___eeeee| split string /__/ | zzzcheck /a_bb/_ccc_dddd/_eeeee/"));
		assertEquals(0, pipe.run("literal abc| split string /abc/ | literal xxx| zzzcheck /xxx/"));

		// anyof
		assertEquals(0, pipe.run("literal abcdefghi| split anyof /d/ | zzzcheck /abc/efghi/"));
		assertEquals(0, pipe.run("literal abcdefghi| split anyof /fxpdl/ | zzzcheck /abc/e/ghi/"));
		assertEquals(0, pipe.run("literal abcdefghi| split anyof /axyz/ | zzzcheck /bcdefghi/"));
		assertEquals(0, pipe.run("literal abcdefghi| split anyof /xyz/ | zzzcheck /abcdefghi/"));

		// xrange
		assertEquals(0, pipe.run("literal abcdefghi| split d-d | zzzcheck /abc/efghi/"));
		assertEquals(0, pipe.run("literal abcdefghi| split D-D | zzzcheck /abcdefghi/"));
		assertEquals(0, pipe.run("literal abcdefghi| split anycase D-D | zzzcheck /abc/efghi/"));
		assertEquals(0, pipe.run("literal abcdefghi| split 64-64 | zzzcheck /abc/efghi/"));
		assertEquals(0, pipe.run("literal abcdefghi| split a-c | zzzcheck /defghi/"));
		assertEquals(0, pipe.run("literal xaybzc| split a-c | zzzcheck /x/y/z/"));

		// single xrange
		assertEquals(0, pipe.run("literal ab.cd| split . | zzzcheck /ab/cd/"));
		assertEquals(0, pipe.run("literal ab.cd| split b | zzzcheck /a/.cd/"));
		assertEquals(0, pipe.run("literal ab-cd| split - | zzzcheck /ab/cd/"));

		// max count
		assertEquals(0, pipe.run("literal a_b_c_d_e| split string /_/   | zzzcheck /a/b/c/d/e/"));
		assertEquals(0, pipe.run("literal a_b_c_d_e| split string /_/ 0 | zzzcheck /a_b_c_d_e/"));
		assertEquals(0, pipe.run("literal a_b_c_d_e| split string /_/ 1 | zzzcheck /a/b_c_d_e/"));
		assertEquals(0, pipe.run("literal a_b_c_d_e| split string /_/ 2 | zzzcheck /a/b/c_d_e/"));
		assertEquals(0, pipe.run("literal a_b_c_d_e| split string /_/ 3 | zzzcheck /a/b/c/d_e/"));
		assertEquals(0, pipe.run("literal a_b_c_d_e| split string /_/ 4 | zzzcheck /a/b/c/d/e/"));
		assertEquals(0, pipe.run("literal a_b_c_d_e| split string /_/ 5 | zzzcheck /a/b/c/d/e/"));

		// before and after
		assertEquals(0, pipe.run("literal 5432154321|literal 1234512345| split at string /4/ | zzzcheck /123/5123/5/5/3215/321/"));
		assertEquals(0, pipe.run("literal 5432154321|literal 1234512345| split before string /4/ | zzzcheck /123/45123/45/5/43215/4321/"));
		assertEquals(0, pipe.run("literal 5432154321|literal 1234512345| split after string /4/ | zzzcheck /1234/51234/5/54/32154/321/"));
		assertEquals(0, pipe.run("literal 5432154321|literal 1234512345| split 1 before string /4/ | zzzcheck /12/34512/345/54321/54321/"));
		assertEquals(0, pipe.run("literal 5432154321|literal 1234512345| split 2 after string /12/ | zzzcheck /1234/51234/5/5432154321/"));
		assertEquals(0, pipe.run("literal 5432154321|literal 1234512345| split -1 before string /54/ | zzzcheck /1234512345/5/43215/4321/"));
		assertEquals(0, pipe.run("literal 5432154321|literal 1234512345| split -1 after string /123/ | zzzcheck /12/34512/345/5432154321/"));

		// not
		assertEquals(0, pipe.run("literal 1234512345| split not string /4/ | zzzcheck /4/4/"));
		assertEquals(0, pipe.run("literal 1a2b3c4d5e1f2g3h4i5j| split anyof /12345/ | zzzcheck /a/b/c/d/e/f/g/h/i/j/"));
		assertEquals(0, pipe.run("literal 1a2b3c4d5e1f2g3h4i5j| split not anyof /12345/ | zzzcheck /1/2/3/4/5/1/2/3/4/5/"));
	}
}
