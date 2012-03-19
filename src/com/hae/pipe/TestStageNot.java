package com.hae.pipe;

import junit.framework.*;

public class TestStageNot extends TestCase {
	public void testSyntax() {
		assertEquals(0, new Pipe().run("literal| not chop"));
		assertEquals(-17, new Pipe().run("literal| not"));
	}
	
	public void testNot() {
		assertEquals(0, new Pipe().run("(end ?) zzzgen 5 | n:     locate /a/ | zzzcheck /a/       ? n: | zzzcheck /b/c/d/e/"));
		assertEquals(0, new Pipe().run("(end ?) zzzgen 5 | n: not locate /a/ | zzzcheck /b/c/d/e/ ? n: | zzzcheck /a/"));
		assertEquals(0, new Pipe().run("(end ?) zzzgen 5 | not locate /a/ | zzzcheck /b/c/d/e/"));
		assertEquals(0, new Pipe().run("(end ?)literal a.sds|literal bb.dsjs|literal ccc.lsdfksj| chop . | zzzcheck /ccc/bb/a/"));
		assertEquals(0, new Pipe().run("(end ?)literal a.x|literal bb.y|literal ccc.z| not chop . | zzzcheck /.z/.y/.x/"));
	}
}
