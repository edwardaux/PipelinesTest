package com.hae.pipe;

import junit.framework.*;

public class TestStageAddrdw extends TestCase {
	public void testSyntax() {
		assertEquals(-113, new Pipe().run("literal a | addrdw "));
		assertEquals(-112, new Pipe().run("literal a | addrdw xxx"));
		assertEquals(0, new Pipe().run("literal a | addrdw variable"));
		assertEquals(0, new Pipe().run("literal a | addrdw v"));
		assertEquals(-112, new Pipe().run("literal a | addrdw v xxx"));
	}

	public void testAddrdw() {
		// TODO convert to hex and compare
		assertEquals(0, new Pipe().run("literal c|literal bb|literal AAA| addrdw v    | hole")); //zzzcheck /\u0000\u0007\u0000\u0000AAA/\u0000\u0006\u0000\u0000bb/\u0000\u0005\u0000\u0000c/"));
		assertEquals(0, new Pipe().run("literal c|literal bb|literal AAA| addrdw cms  | hole")); //zzzcheck /\u0000\u0003AAA/\u0000\u0002bb/\u0000\u0001c/"));
		assertEquals(0, new Pipe().run("literal c|literal bb|literal AAA| addrdw sf   | hole")); //zzzcheck /\u0000\u0005AAA/\u0000\u0004bb/\u0000\u0003c/"));
		assertEquals(0, new Pipe().run("literal c|literal bb|literal AAA| addrdw cms4 | hole")); //zzzcheck /\u0000\u0003AAA/\u0000\u0002bb/\u0000\u0001c/"));
		assertEquals(0, new Pipe().run("literal c|literal bb|literal AAA| addrdw sf4  | hole")); //zzzcheck /\u0000\u0000\u0000\u0007AAA/\u0000\u0000\u0000\u0006bb/\u0000\u0000\u0000\u0005c/"));
	}
}
