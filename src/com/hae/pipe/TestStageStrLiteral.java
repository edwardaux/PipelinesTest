package com.hae.pipe;

import junit.framework.*;

public class TestStageStrLiteral extends TestCase {
	public static final String COPYRIGHT = "Copyright 2007,2012. H.A. Enterprises Pty Ltd. All Rights Reserved";
	
	public void testSyntax() {
		assertEquals(-60, new Pipe().run("strliteral foobar"));
		assertEquals(-60, new Pipe().run("strliteral ifempty conditional"));
	}
	
	public void testStrLiteral() {
		assertEquals(0, new Pipe().run("strliteral /a/| zzzcheck /a/"));
		assertEquals(0, new Pipe().run("strliteral / a/| zzzcheck / a/"));
		assertEquals(0, new Pipe().run("strliteral /a/|literal b| zzzcheck /b/a/"));
		assertEquals(0, new Pipe().run("strliteral /a /|literal b | zzzcheck /b /a /"));
		assertEquals(0, new Pipe().run("strliteral | zzzcheck //"));
		
		assertEquals(0, new Pipe().run("strliteral /a/| strliteral /b/ | zzzcheck /b/a/"));
		assertEquals(0, new Pipe().run("strliteral /a/| strliteral /b/ | strliteral /c/ | zzzcheck /c/b/a/"));
		assertEquals(0, new Pipe().run("strliteral preface /a/| strliteral /b/ | strliteral preface /c/ | zzzcheck /c/b/a/"));
		
		assertEquals(0, new Pipe().run("strliteral append /a/ | zzzcheck /a/"));
		assertEquals(0, new Pipe().run("strliteral append /a/ | strliteral append /b/ | strliteral append /c/ | zzzcheck /a/b/c/"));
		assertEquals(0, new Pipe().run("strliteral preface /a/| strliteral append /b/ | strliteral preface /c/ | strliteral append /d/ | zzzcheck /c/a/b/d/"));
		assertEquals(0, new Pipe().run("literal abcd| strliteral preface /<start>/| strliteral append /<end>/ | zzzcheck /<start>/abcd/<end>/"));

		assertEquals(0, new Pipe().run("literal b| strliteral preface conditional /a/| literal c| zzzcheck /c/a/b/"));
		assertEquals(0, new Pipe().run("strliteral preface conditional /a/| literal c| zzzcheck /c/"));
		assertEquals(0, new Pipe().run("literal b| strliteral append conditional /a/| literal c| zzzcheck /c/b/a/"));
		assertEquals(0, new Pipe().run("strliteral append conditional /a/| literal c| zzzcheck /c/"));

		assertEquals(0, new Pipe().run("literal b| strliteral ifempty /a/| literal c| zzzcheck /c/b/"));
		assertEquals(0, new Pipe().run("strliteral ifempty /a/| literal c| zzzcheck /c/a/"));
	}
}
