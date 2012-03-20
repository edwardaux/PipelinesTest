package com.hae.pipe;

import junit.framework.*;

public class TestStageFanout extends TestCase {
	public static final String COPYRIGHT = "Copyright 2007,2012. H.A. Enterprises Pty Ltd. All Rights Reserved";
	
	public void testSyntax() {
		assertEquals(0, new Pipe().run("literal a| fanout"));
		assertEquals(-112, new Pipe().run("literal a| fanout blah"));
		assertEquals(-58, new Pipe().run("literal a| fanout stop blah"));
		assertEquals(-112, new Pipe().run("literal a| fanout stop anyeof blah"));
	}
	
	public void testFanout() {
		assertEquals(0, new Pipe().run("literal a| fanout | zzzcheck /a/"));
		assertEquals(0, new Pipe().run("(end ?) literal blah| f: fanout | spec /1/ 1 1-* nw | zzzcheck /1 blah/ ? f: | spec /2/ 1 1-* nw | zzzcheck /2 blah/ ? f: | spec /3/ 1 1-* nw | zzzcheck /3 blah/ ? f: | spec /4/ 1 1-* nw | zzzcheck /4 blah/"));
		assertEquals(0, new Pipe().run("(end ?) literal d|literal c|literal b|literal a| f: fanout STOP ALLEOF| spec /1/ 1 1-* nw | zzzcheck /1 a/1 b/1 c/1 d/ ? f: | spec /2/ 1 1-* nw | zzzcheck /2 a/2 b/2 c/2 d/ ? f: | spec /3/ 1 1-* nw | zzzcheck /3 a/3 b/3 c/3 d/ ? f: | spec /4/ 1 1-* nw | zzzcheck /4 a/4 b/4 c/4 d/"));
		assertEquals(0, new Pipe().run("(end ?) literal d|literal c|literal b|literal a| f: fanout STOP ALLEOF| spec /1/ 1 1-* nw | zzzcheck /1 a/1 b/1 c/1 d/ ? f: | take 2 | spec /2/ 1 1-* nw | zzzcheck /2 a/2 b/ ? f: | spec /3/ 1 1-* nw | zzzcheck /3 a/3 b/3 c/3 d/ ? f: | spec /4/ 1 1-* nw | zzzcheck /4 a/4 b/4 c/4 d/"));
		assertEquals(0, new Pipe().run("(end ?) literal d|literal c|literal b|literal a| f: fanout STOP ANYEOF| spec /1/ 1 1-* nw | zzzcheck /1 a/1 b/1 c/ ? f: | take 2 | spec /2/ 1 1-* nw | zzzcheck /2 a/2 b/ ? f: | spec /3/ 1 1-* nw | zzzcheck /3 a/3 b/3 c/ ? f: | spec /4/ 1 1-* nw | zzzcheck /4 a/4 b/4 c/"));
		assertEquals(0, new Pipe().run("(end ?) literal d|literal c|literal b|literal a| f: fanout STOP 1| spec /1/ 1 1-* nw | zzzcheck /1 a/1 b/1 c/ ? f: | take 2 | spec /2/ 1 1-* nw | zzzcheck /2 a/2 b/ ? f: | spec /3/ 1 1-* nw | zzzcheck /3 a/3 b/3 c/ ? f: | spec /4/ 1 1-* nw | zzzcheck /4 a/4 b/4 c/"));
		assertEquals(0, new Pipe().run("(end ?) literal d|literal c|literal b|literal a| f: fanout STOP 2| spec /1/ 1 1-* nw | zzzcheck /1 a/1 b/1 c/1 d/ ? f: | take 2 | spec /2/ 1 1-* nw | zzzcheck /2 a/2 b/ ? f: | take 3 | spec /3/ 1 1-* nw | zzzcheck /3 a/3 b/3 c/ ? f: | spec /4/ 1 1-* nw | zzzcheck /4 a/4 b/4 c/4 d/"));
		assertEquals(0, new Pipe().run("(end ?) literal d|literal c|literal b|literal a| f: fanout STOP 10| spec /1/ 1 1-* nw | zzzcheck /1 a/1 b/1 c/1 d/ ? f: | take 2 | spec /2/ 1 1-* nw | zzzcheck /2 a/2 b/ ? f: | take 3 | spec /3/ 1 1-* nw | zzzcheck /3 a/3 b/3 c/ ? f: | spec /4/ 1 1-* nw | zzzcheck /4 a/4 b/4 c/4 d/"));
	}
	
}
