package com.hae.pipe;

import junit.framework.*;

public class TestSyntax extends TestCase {
	public static final String COPYRIGHT = "Copyright 2007,2012. H.A. Enterprises Pty Ltd. All Rights Reserved";
	
	public void testPipeUtil() {
		assertEquals('|', PipeUtil.makeChar("|"));
		assertEquals('|', PipeUtil.makeChar("7C"));
		assertEquals(' ', PipeUtil.makeChar("20"));
// TODO number format		assertEquals(' ', PipeUtil.makeChar("XX"));
	}
	public void testPipeArgs() throws PipeException {
		PipeArgs args;
		
		// words
		args = new PipeArgs("");
		assertEquals("", args.nextWord());
		assertEquals("", args.nextWord());
		assertEquals("", args.getRemainder());

		args = new PipeArgs("hello");
		assertEquals("hello", args.nextWord());
		assertEquals("", args.nextWord());
		assertEquals("", args.getRemainder());

		args = new PipeArgs("  hello");
		assertEquals("hello", args.nextWord());
		assertEquals("", args.nextWord());
		assertEquals("", args.getRemainder());

		args = new PipeArgs("  hello ");
		assertEquals("hello", args.nextWord());
		assertEquals("", args.nextWord());
		assertEquals("", args.getRemainder());

		args = new PipeArgs("  hello  ");
		assertEquals("hello", args.nextWord());
		assertEquals("", args.nextWord());
		assertEquals(" ", args.getRemainder());

		args = new PipeArgs("  hello there, how are you  ");
		assertEquals("hello", args.nextWord());
		assertEquals("there,", args.nextWord());
		assertEquals("how", args.nextWord());
		assertEquals("are you  ", args.getRemainder());

		// delim string
		args = new PipeArgs("");
		assertEquals("", args.nextDelimString(false));
		assertEquals("", args.nextDelimString(false));
		assertEquals("", args.getRemainder());

		args = new PipeArgs("/hello/");
		assertEquals("hello", args.nextDelimString(false));
		assertEquals("", args.nextDelimString(false));
		assertEquals("", args.getRemainder());

		args = new PipeArgs("  /hello/");
		assertEquals("hello", args.nextDelimString(false));
		assertEquals("", args.nextDelimString(false));
		assertEquals("", args.getRemainder());

		args = new PipeArgs("  /hello/ ");
		assertEquals("hello", args.nextDelimString(false));
		assertEquals("", args.nextDelimString(false));
		assertEquals(" ", args.getRemainder());

		args = new PipeArgs("  ,hello,  ");
		assertEquals("hello", args.nextDelimString(false));
		assertEquals("", args.nextDelimString(false));
		assertEquals("  ", args.getRemainder());

		args = new PipeArgs("  ,hello, /there,/ /how/ /are/ /you/  ");
		assertEquals("hello", args.nextDelimString(false));
		assertEquals("there,", args.nextDelimString(false));
		assertEquals("how", args.nextDelimString(false));
		assertEquals(" /are/ /you/  ", args.getRemainder());

		args = new PipeArgs("/hello");
		try {
			args.nextDelimString(true);
		}
		catch(PipeException e) {
			assertEquals(-60, e.getMessageNo());
		}

		args = new PipeArgs("b00111000");
		assertEquals("8", args.nextDelimString(false));

		args = new PipeArgs("b11000010");
		assertEquals("\u00C2", args.nextDelimString(false));

		args = new PipeArgs("  b00111000  ");
		assertEquals("8", args.nextDelimString(false));
		assertEquals("  ", args.getRemainder());

		args = new PipeArgs("  b00111000 b00111000 /a/  ");
		assertEquals("8", args.nextDelimString(false));
		assertEquals("8", args.nextDelimString(false));
		assertEquals("a", args.nextDelimString(false));
		assertEquals("  ", args.getRemainder());

		args = new PipeArgs("b001110000011100100111010");
		assertEquals("89:", args.nextDelimString(false));

		args = new PipeArgs("b");
		try {
			args.nextDelimString(false);
		}
		catch(PipeException e) {
			assertEquals(-337, e.getMessageNo());
		}

		args = new PipeArgs(" b ");
		try {
			args.nextDelimString(false);
		}
		catch(PipeException e) {
			assertEquals(-337, e.getMessageNo());
		}

		args = new PipeArgs("b1111");
		try {
			args.nextDelimString(false);
		}
		catch(PipeException e) {
			assertEquals(-336, e.getMessageNo());
		}

		args = new PipeArgs("bxxxxxxxx");
		try {
			args.nextDelimString(false);
		}
		catch(PipeException e) {
			assertEquals(-338, e.getMessageNo());
		}

		args = new PipeArgs("x20");
		assertEquals(" ", args.nextDelimString(false));

		args = new PipeArgs("x4D");
		assertEquals("M", args.nextDelimString(false));

		args = new PipeArgs("x4D204D204D");
		assertEquals("M M M", args.nextDelimString(false));
		
		args = new PipeArgs("x");
		try {
			args.nextDelimString(false);
		}
		catch(PipeException e) {
			assertEquals(-64, e.getMessageNo());
		}

		args = new PipeArgs(" x ");
		try {
			args.nextDelimString(false);
		}
		catch(PipeException e) {
			assertEquals(-64, e.getMessageNo());
		}

		args = new PipeArgs("xA");
		try {
			args.nextDelimString(false);
		}
		catch(PipeException e) {
			assertEquals(-335, e.getMessageNo());
		}

		args = new PipeArgs("xxxxxxxxx");
		try {
			args.nextDelimString(false);
		}
		catch(PipeException e) {
			assertEquals(-65, e.getMessageNo());
		}

		args = new PipeArgs("hello there, /how/ are you  ");
		assertEquals("hello", args.nextWord());
		assertEquals("there,", args.nextWord());
		assertEquals("/how/", args.nextWord());
		args.undo();
		assertEquals("/how/", args.nextWord());
		args.undo();
		assertEquals("how", args.nextDelimString(false));

		args = new PipeArgs("hello there");
		assertEquals("", args.nextExpression());
		assertEquals("hello", args.nextWord());
		assertEquals("there", args.nextWord());
		args = new PipeArgs("hello (hi there) there");
		assertEquals("", args.nextExpression());
		assertEquals("hello", args.nextWord());
		assertEquals("(hi", args.nextWord());
		assertEquals("there)", args.nextWord());
		assertEquals("there", args.nextWord());
		args = new PipeArgs("(hi there) there");
		assertEquals("hi there", args.nextExpression());
		assertEquals("there", args.nextWord());
		try {
			args = new PipeArgs("(hi");
			args.nextExpression();
		}
		catch(PipeException e) {
			assertEquals(-200, e.getMessageNo());
		}
	}
	
	public void testConversions() throws PipeException {
		assertEquals("01100001", Conversion.create("C2B").convert("a", 1));
		assertEquals("0110000101100010", Conversion.create("C2B").convert("ab", 1));
		assertEquals("a", Conversion.create("B2C").convert("01100001", 1));
		assertEquals("ab", Conversion.create("B2C").convert("0110000101100010", 1));
		try { assertNull(Conversion.create("B2C").convert("01010", 1)); } catch(PipeException e) { assertEquals(e.getMessageNo(), -392); }
		try { assertNull(Conversion.create("B2C").convert("sssfddfd", 1)); } catch(PipeException e) { assertEquals(e.getMessageNo(), -392); }
		
		assertEquals("61", Conversion.create("C2X").convert("a", 1));
		assertEquals("6162", Conversion.create("C2X").convert("ab", 1));
		assertEquals("a", Conversion.create("X2C").convert("61", 1));
		assertEquals("ab", Conversion.create("X2C").convert("6162", 1));
		assertEquals("ab", Conversion.create("X2C").convert(" 61 62 ", 1));
		try { assertNull(Conversion.create("X2C").convert("1", 1)); } catch(PipeException e) { assertEquals(e.getMessageNo(), -392); }
		try { assertNull(Conversion.create("X2C").convert("sssfddfd", 1)); } catch(PipeException e) { assertEquals(e.getMessageNo(), -392); }
		
		assertEquals("\u0000\u0000\u0000\u007B", Conversion.create("D2C").convert("123", 1));
		assertEquals("\u00ff\u00ff\u00ff\u0085", Conversion.create("D2C").convert("-123", 1));
		try { assertNull(Conversion.create("D2C").convert("sssfdg", 1)); } catch(PipeException e) { assertEquals(e.getMessageNo(), -392); }
		try { assertNull(Conversion.create("D2C").convert("123456789023345", 1)); } catch(PipeException e) { assertEquals(e.getMessageNo(), -392); }
		assertEquals("        123", Conversion.create("C2D").convert("\u0000\u0000\u0000\u007B", 1));
		assertEquals("       -123", Conversion.create("C2D").convert("\u00ff\u00ff\u00ff\u0085", 1));
		try { assertNull(Conversion.create("C2D").convert("010", 1)); } catch(PipeException e) { assertEquals(e.getMessageNo(), -392); }
		
		assertEquals("\u0040\u005e\u00dc\u00cc\u00cc\u00cc\u00cc\u00cd", Conversion.create("F2C").convert("123.45", 1));
		assertEquals("\u00c0\u005e\u00dc\u00cc\u00cc\u00cc\u00cc\u00cd", Conversion.create("F2C").convert("-123.45", 1));
		try { assertNull(Conversion.create("F2C").convert("sssfdg", 1)); } catch(PipeException e) { assertEquals(e.getMessageNo(), -392); }
		try { assertNull(Conversion.create("F2C").convert("99999"+Double.MAX_VALUE+"1", 1)); } catch(PipeException e) { assertEquals(e.getMessageNo(), -392); }
		assertEquals("123.45", Conversion.create("C2F").convert("\u0040\u005e\u00dc\u00cc\u00cc\u00cc\u00cc\u00cd", 1));
		assertEquals("-123.45", Conversion.create("C2F").convert("\u00c0\u005e\u00dc\u00cc\u00cc\u00cc\u00cc\u00cd", 1));
		try { assertNull(Conversion.create("C2F").convert("010", 1)); } catch(PipeException e) { assertEquals(e.getMessageNo(), -392); }
		
		assertEquals("\u0000\u0007abcdefg", Conversion.create("V2C").convert("abcdefg", 1));
		assertEquals("\u0000\u0000", Conversion.create("V2C").convert("", 1));
		assertEquals("abcdefg", Conversion.create("C2V").convert("\u0000\u0007abcdefg", 1));
		assertEquals("", Conversion.create("C2V").convert("\u0000\u0000", 1));
		try { assertNull(Conversion.create("C2V").convert("\u0000\u0003", 1)); } catch(PipeException e) { assertEquals(e.getMessageNo(), -392); }
		try { assertNull(Conversion.create("C2V").convert("\u0000\u0003abcdefg", 1)); } catch(PipeException e) { assertEquals(e.getMessageNo(), -392); }
		
		assertEquals("\u0000\u0000\u0001\u0017\u000c\u003d\u0000\u0080", Conversion.create("I2C").convert("071225", 1));
		assertEquals("20071225000000", Conversion.create("C2I").convert("\u0000\u0000\u0001\u0017\u000c\u003d\u0000\u0080", 1));
		try { assertNull(Conversion.create("C2I").convert("\u0000\u0003", 1)); } catch(PipeException e) { assertEquals(e.getMessageNo(), -352); }
	}
	
	public void testDates() throws PipeException {
		assertEquals(PipeUtil.makeDateFromISO("070727").toString(), "Fri Jul 27 00:00:00 EST 2007");
		assertEquals(PipeUtil.makeDateFromISO("20070727").toString(), "Fri Jul 27 00:00:00 EST 2007");
		assertEquals(PipeUtil.makeDateFromISO("2007072716").toString(), "Fri Jul 27 16:00:00 EST 2007");
		assertEquals(PipeUtil.makeDateFromISO("200707271623").toString(), "Fri Jul 27 16:23:00 EST 2007");
		assertEquals(PipeUtil.makeDateFromISO("20070727162356").toString(), "Fri Jul 27 16:23:56 EST 2007");
		try { assertNull(PipeUtil.makeDateFromISO("444444").toString(), "Fri Jul 27 16:23:56 EST 2007"); } catch(PipeException e) { assertEquals(e.getMessageNo(), -1183); }
		try { assertNull(PipeUtil.makeDateFromISO("abcdef").toString(), "Fri Jul 27 16:23:56 EST 2007"); } catch(PipeException e) { assertEquals(e.getMessageNo(), -1183); }
		try { assertNull(PipeUtil.makeDateFromISO("4444").toString(), "Fri Jul 27 16:23:56 EST 2007"); } catch(PipeException e) { assertEquals(e.getMessageNo(), -1183); }

		assertEquals(PipeUtil.makeTimestampFromISO("2007-07-27").toString(), "Fri Jul 27 00:00:00 EST 2007");
		assertEquals(PipeUtil.makeTimestampFromISO("2007-07-27").toString(), "Fri Jul 27 00:00:00 EST 2007");
		assertEquals(PipeUtil.makeTimestampFromISO("2007-07-27 16").toString(), "Fri Jul 27 16:00:00 EST 2007");
		assertEquals(PipeUtil.makeTimestampFromISO("2007-07-27 16:23").toString(), "Fri Jul 27 16:23:00 EST 2007");
		assertEquals(PipeUtil.makeTimestampFromISO("2007-07-27 16:23:56").toString(), "Fri Jul 27 16:23:56 EST 2007");
		assertEquals(PipeUtil.makeTimestampFromISO("2007-07-27 16:23:56.123").toString(), "Fri Jul 27 16:23:56 EST 2007");
		try { assertNull(PipeUtil.makeTimestampFromISO("444444").toString(), "Fri Jul 27 16:23:56 EST 2007"); } catch(PipeException e) { assertEquals(e.getMessageNo(), -1183); }
		try { assertNull(PipeUtil.makeTimestampFromISO("abcdef").toString(), "Fri Jul 27 16:23:56 EST 2007"); } catch(PipeException e) { assertEquals(e.getMessageNo(), -1183); }
		try { assertNull(PipeUtil.makeTimestampFromISO("4444").toString(), "Fri Jul 27 16:23:56 EST 2007"); } catch(PipeException e) { assertEquals(e.getMessageNo(), -1183); }

		assertEquals(PipeUtil.makeDateFromJUL("07015S").toString(), "Mon Jan 15 00:00:00 EST 2007");
		assertEquals(PipeUtil.makeDateFromJUL("0107015S").toString(), "Mon Jan 15 00:00:00 EST 2007");
		assertEquals(PipeUtil.makeDateFromJUL("0107015S16").toString(), "Mon Jan 15 16:00:00 EST 2007");
		assertEquals(PipeUtil.makeDateFromJUL("0107015S1623").toString(), "Mon Jan 15 16:23:00 EST 2007");
		assertEquals(PipeUtil.makeDateFromJUL("0107015S162356").toString(), "Mon Jan 15 16:23:56 EST 2007");
		try { assertNull(PipeUtil.makeDateFromJUL("444444").toString(), "Fri Jul 27 16:23:56 EST 2007"); } catch(PipeException e) { assertEquals(e.getMessageNo(), -1183); }
		try { assertNull(PipeUtil.makeDateFromJUL("abcdef").toString(), "Fri Jul 27 16:23:56 EST 2007"); } catch(PipeException e) { assertEquals(e.getMessageNo(), -1183); }
		try { assertNull(PipeUtil.makeDateFromJUL("4444").toString(), "Fri Jul 27 16:23:56 EST 2007"); } catch(PipeException e) { assertEquals(e.getMessageNo(), -1183); }
	}
	
	public void testSyntax() throws PipeException {
		assertTrue(Syntax.isString(""));
		assertTrue(Syntax.isString("   "));
		assertTrue(Syntax.isString("hello there how are you"));

		assertFalse(Syntax.isHexString(""));
		assertTrue(Syntax.isHexString("x123456"));
		assertTrue(Syntax.isHexString("xF1F2F3"));
		assertTrue(Syntax.isHexString("X123456"));
		assertTrue(Syntax.isHexString("XF1F2F3"));
		assertFalse(Syntax.isHexString("x12345"));
		assertFalse(Syntax.isHexString("123456"));

		assertFalse(Syntax.isBinString(""));
		assertTrue(Syntax.isBinString("B00000000"));
		assertTrue(Syntax.isBinString("B01010101"));
		assertFalse(Syntax.isBinString("B02020202"));
		assertFalse(Syntax.isBinString("B0000000"));
		assertFalse(Syntax.isBinString("X0000000"));
		assertFalse(Syntax.isBinString("1100101"));

		assertFalse(Syntax.isDelimitedString(""));
		assertTrue(Syntax.isDelimitedString("X1234"));
		assertTrue(Syntax.isDelimitedString("B01010101"));
		assertTrue(Syntax.isDelimitedString("/abc/"));
		assertTrue(Syntax.isDelimitedString("/hello there/"));
		assertTrue(Syntax.isDelimitedString(",,"));
		assertFalse(Syntax.isDelimitedString(","));
		assertFalse(Syntax.isDelimitedString("1234"));
		assertTrue(Syntax.isDelimitedString("0000"));         // leading and trailing 0's

		assertFalse(Syntax.isNumber(""));
		assertTrue(Syntax.isNumber("1"));
		assertTrue(Syntax.isNumber("0012"));
		assertTrue(Syntax.isNumber("00"));
		assertTrue(Syntax.isNumber("123"));
		assertFalse(Syntax.isNumber("-1"));
		assertFalse(Syntax.isNumber("a"));
		assertFalse(Syntax.isNumber(""));

		assertFalse(Syntax.isSignedNumber(""));
		assertTrue(Syntax.isSignedNumber("1"));
		assertTrue(Syntax.isSignedNumber("123"));
		assertTrue(Syntax.isSignedNumber("-1"));
		assertFalse(Syntax.isSignedNumber("a"));
		assertFalse(Syntax.isSignedNumber(""));

		assertTrue(Syntax.isDecimalNumber("0"));
		assertTrue(Syntax.isDecimalNumber("0.0"));
		assertTrue(Syntax.isDecimalNumber("1.0"));
		assertTrue(Syntax.isDecimalNumber("1.1"));
		assertTrue(Syntax.isDecimalNumber("1234.56"));
		assertFalse(Syntax.isDecimalNumber(".02"));
		assertTrue(Syntax.isDecimalNumber("1.00"));
		assertTrue(Syntax.isDecimalNumber("1.02"));
		assertFalse(Syntax.isDecimalNumber("1.02.0"));

		assertFalse(Syntax.isNumberOrStar("", false));
		assertTrue(Syntax.isNumberOrStar("1", false));
		assertTrue(Syntax.isNumberOrStar("123", false));
		assertFalse(Syntax.isNumberOrStar("-1", false));
		assertTrue(Syntax.isNumberOrStar("-1", true));
		assertFalse(Syntax.isNumberOrStar("a", false));
		assertTrue(Syntax.isNumberOrStar("*", false));
		assertFalse(Syntax.isNumberOrStar("**", false));

		assertFalse(Syntax.isRange(""));
		assertTrue(Syntax.isRange("8"));
		assertTrue(Syntax.isRange("1.5"));
		assertTrue(Syntax.isRange("10-12"));
		assertTrue(Syntax.isRange("9-*"));
		assertTrue(Syntax.isRange("*.8"));
		assertTrue(Syntax.isRange("9.3"));
		assertTrue(Syntax.isRange("*-*"));
		assertFalse(Syntax.isRange("1-"));
		assertFalse(Syntax.isRange("1."));
		assertFalse(Syntax.isRange("*-*-*"));

		assertFalse(Syntax.isRanges(""));
		assertTrue(Syntax.isRanges("8"));
		assertTrue(Syntax.isRanges("1.5"));
		assertTrue(Syntax.isRanges("10-12"));
		assertTrue(Syntax.isRanges("9-*"));
		assertTrue(Syntax.isRanges("*.8"));
		assertTrue(Syntax.isRanges("9.3"));
		assertTrue(Syntax.isRanges("*-*"));
		assertFalse(Syntax.isRanges("1-"));
		assertFalse(Syntax.isRanges("1."));
		assertFalse(Syntax.isRanges("*-*-*"));
		assertFalse(Syntax.isRanges("()"));
		assertFalse(Syntax.isRanges("("));
		assertFalse(Syntax.isRanges(")"));
		assertTrue(Syntax.isRanges("(8)"));
		assertTrue(Syntax.isRanges("(8 1.5)"));
		assertTrue(Syntax.isRanges("(8         1.5)"));
		assertTrue(Syntax.isRanges("(8 1.5 10-12 *.*)"));

		assertFalse(Syntax.isXorC(""));
		assertTrue(Syntax.isXorC("8"));
		assertFalse(Syntax.isXorC(" "));
		assertTrue(Syntax.isXorC("40"));
		assertTrue(Syntax.isXorC("ff"));
		assertTrue(Syntax.isXorC("BLANK"));
		assertTrue(Syntax.isXorC("blank"));
		assertTrue(Syntax.isXorC("SPACE"));
		assertTrue(Syntax.isXorC("space"));
		assertTrue(Syntax.isXorC("TAB"));
		assertTrue(Syntax.isXorC("TAB"));

		assertFalse(Syntax.isXrange(""));
		assertTrue(Syntax.isXrange("X"));
		assertTrue(Syntax.isXrange("X-Z"));
		assertTrue(Syntax.isXrange("00-7f"));
		assertTrue(Syntax.isXrange("00.256"));
		assertTrue(Syntax.isXrange("BLANK"));
		assertTrue(Syntax.isXrange("40-7f"));
		assertTrue(Syntax.isXrange("00-blank"));

		assertFalse(Syntax.isStreamId(""));
		assertFalse(Syntax.isStreamId("17"));
		assertFalse(Syntax.isStreamId("0"));
		assertTrue(Syntax.isStreamId("abcd"));
		assertFalse(Syntax.isStreamId("abcde"));
		assertFalse(Syntax.isStreamId("a de"));

		assertFalse(Syntax.isStream(""));
		assertTrue(Syntax.isStream("17"));
		assertTrue(Syntax.isStream("0"));
		assertTrue(Syntax.isStream("abcd"));
		assertFalse(Syntax.isStream("abcde"));
		assertFalse(Syntax.isStream("a de"));

		assertFalse(Syntax.isIPAddress(""));
		assertFalse(Syntax.isIPAddress("123"));
		assertFalse(Syntax.isIPAddress("123.123"));
		assertFalse(Syntax.isIPAddress("123.123.123"));
		assertTrue(Syntax.isIPAddress("123.123.123.123"));
		assertFalse(Syntax.isIPAddress("123.123.123.123."));
		assertFalse(Syntax.isIPAddress("123+123+123+123"));
		assertTrue(Syntax.isIPAddress("1.1.1.1"));
		assertFalse(Syntax.isIPAddress("a.a.a.a"));
		assertFalse(Syntax.isIPAddress("256.256.256.256"));

		assertTrue(Syntax.isRanges("(8 1.5 10-12 *.*)"));

		assertTrue(Syntax.abbrev("HELLO", "HELLO", 0, false));
		assertTrue(Syntax.abbrev("HELLO", "hello", 0, true));
		assertTrue(Syntax.abbrev("HELLO", "HELLO", 5, false));
		assertTrue(Syntax.abbrev("HELLO", "hello", 5, true));
		assertFalse(Syntax.abbrev("HELLX", "HELLO", 5, false));
		assertFalse(Syntax.abbrev("HELLX", "hello", 5, true));
		assertTrue(Syntax.abbrev("HELLOTHERE", "HELLO", 5, false));
		assertTrue(Syntax.abbrev("HELLOTHERE", "hello", 5, true));
		assertTrue(Syntax.abbrev("HELLO THERE", "HELLO", 5, false));
		assertTrue(Syntax.abbrev("HELLO THERE", "hello", 5, true));
		assertFalse(Syntax.abbrev("HELLO", "HE", 3, false));
		assertFalse(Syntax.abbrev("hello", "HE", 3, true));
		assertTrue(Syntax.abbrev("HELLO", "HEL", 3, false));
		assertTrue(Syntax.abbrev("hello", "HEL", 3, true));
		assertTrue(Syntax.abbrev("HELLO", "HELL", 3, false));
		assertTrue(Syntax.abbrev("hello", "HELL", 3, true));
		// TODO
//		assertEquals(scanRange("5-3"));       // no good
//		assertEquals(scanRange("-1;-4"));     // no good
//		assertEquals(scanRange("-4;-1"));     // ok
//		assertEquals(scanRange("4;-3"));      // don't know until extractRange
		
		assertEquals(new String[] { "", "" }, PipeUtil.split("", 1));
		assertEquals(new String[] { "", "" }, PipeUtil.split("", -1));
		assertEquals(new String[] { "a", "bcde" }, PipeUtil.split("abcde", 1));
		assertEquals(new String[] { "ab", "cde" }, PipeUtil.split("abcde", 2));
		assertEquals(new String[] { "abc", "de" }, PipeUtil.split("abcde", 3));
		assertEquals(new String[] { "abcd", "e" }, PipeUtil.split("abcde", 4));
		assertEquals(new String[] { "abcde", "" }, PipeUtil.split("abcde", 5));
		assertEquals(new String[] { "abcde", "" }, PipeUtil.split("abcde", 6));
		assertEquals(new String[] { "abcd", "e" }, PipeUtil.split("abcde", -1));
		assertEquals(new String[] { "abc", "de" }, PipeUtil.split("abcde", -2));
		assertEquals(new String[] { "ab", "cde" }, PipeUtil.split("abcde", -3));
		assertEquals(new String[] { "a", "bcde" }, PipeUtil.split("abcde", -4));
		assertEquals(new String[] { "", "abcde" }, PipeUtil.split("abcde", -5));
		assertEquals(new String[] { "", "abcde" }, PipeUtil.split("abcde", -6));
		assertEquals(new String[] { "", "abcde" }, PipeUtil.split("abcde", 0));
		
		assertEquals("abcdexxxxx", PipeUtil.align("abcde", 10, PipeConstants.LEFT, 'x'));
		assertEquals("xxxxxabcde", PipeUtil.align("abcde", 10, PipeConstants.RIGHT, 'x'));
		assertEquals("xxabcdexxx", PipeUtil.align("abcde", 10, PipeConstants.CENTER, 'x'));
		assertEquals("abc", PipeUtil.align("abcde", 3, PipeConstants.LEFT, 'x'));
		assertEquals("cde", PipeUtil.align("abcde", 3, PipeConstants.RIGHT, 'x'));
		assertEquals("bcd", PipeUtil.align("abcde", 3, PipeConstants.CENTRE, 'x'));
		assertEquals("ab", PipeUtil.align("abcde", 2, PipeConstants.LEFT, 'x'));
		assertEquals("de", PipeUtil.align("abcde", 2, PipeConstants.RIGHT, 'x'));
		assertEquals("bc", PipeUtil.align("abcde", 2, PipeConstants.CENTRE, 'x'));
		
		assertEquals("a12de", PipeUtil.insert("abcde", "12", 2, 2, PipeConstants.LEFT, '_'));
		assertEquals("a12_e", PipeUtil.insert("abcde", "12", 2, 3, PipeConstants.LEFT, '_'));
		assertEquals("a_12e", PipeUtil.insert("abcde", "12", 2, 3, PipeConstants.RIGHT, '_'));
		assertEquals("abcde____12_", PipeUtil.insert("abcde", "12", 10, 3, PipeConstants.LEFT, '_'));
		assertEquals("         1wxyz", PipeUtil.insert("mnopqrstuvwxyz", "         1", 1, 10, PipeConstants.LEFT, ' '));
	}

	public void testRangeTest() throws PipeException {
		Pipe.register("rangetest", RangeTest.class);
		assertEquals(0, new Pipe().run("rangetest"));
	}
	
	public static class RangeTest extends Stage {
		public int execute(String input) throws PipeException {
			PipeArgs args;
			
			assertEquals("1-1", scanRange(new PipeArgs("1;1"), true).toString());
			assertEquals("W 1-1", scanRange(new PipeArgs("WORDS 1;1"), true).toString());
			assertEquals("F 1-1", scanRange(new PipeArgs("FIELDS 1;1"), true).toString());
			assertEquals("WS a W 1-1", scanRange(new PipeArgs("WORDSEP a WORDS 1;1"), true).toString());
			assertEquals("2-4", scanRange(new PipeArgs("2.3"), true).toString());
			assertEquals("W 4-8", scanRange(new PipeArgs("WORDS 4-8"), true).toString());
			assertEquals("F 1-1", scanRange(new PipeArgs("FIELDS 1.1"), true).toString());
			assertEquals("FS b F 1-1", scanRange(new PipeArgs("FIELDSEP b FIELDS 1;1"), true).toString());
			assertEquals("WS a 1-1", scanRange(new PipeArgs("WORDSEP a 1;1"), true).toString());
			assertEquals("FS b 1-1", scanRange(new PipeArgs("FIELDSEP b 1;1"), true).toString());
			assertEquals("WS a FS b 1-1", scanRange(new PipeArgs("WORDSEP a FIELDSEP b 1;1"), true).toString());       // missing WORDS
			assertEquals("-1;-1", scanRange(new PipeArgs("-1"), true).toString());
			assertEquals("SUBSTR 2-5 OF 1-10", scanRange(new PipeArgs("SUBSTR 2-5 OF 1-10"), true).toString());
			assertEquals("SUBSTR 2-5 OF SUBSTR 3-4 OF 1-10", scanRange(new PipeArgs("SUBSTR 2-5 OF SUBSTR 3.2 OF 1-10 "), true).toString());
			
			args = new PipeArgs("");
			assertEquals(null, scanRange(args, false));
			args = new PipeArgs("something else");
			assertEquals(null, scanRange(args, false));
			assertEquals("something else", args.getRemainder());
			
			try {
				args = new PipeArgs("something else");
				assertEquals("1-*", scanRange(args, true).toString());   // RC != 0
			}
			catch(PipeException e) {
				assertEquals(-54, e.getMessageNo());
				assertEquals("something else", args.getRemainder());
			}
			args = new PipeArgs("SUBSTR 2-5 OF 1-10");
			assertEquals("SUBSTR 2-5 OF 1-10", scanRange(args, false).toString());
			args = new PipeArgs("SUBSTR 2-5 OF SUBSTR 3.2 OF 1-10 ");
			assertEquals("SUBSTR 2-5 OF SUBSTR 3-4 OF 1-10", scanRange(args, false).toString());
			args = new PipeArgs("1-5 hello there");
			assertEquals("1-5", scanRange(args, false).toString());
			assertEquals("hello there", args.getRemainder());
			args = new PipeArgs("1-5 hello there");
			assertEquals("1-5", scanRange(args, true).toString());
			assertEquals("hello there", args.getRemainder());

			assertEquals("", scanRange(new PipeArgs("1-8"), true).extractRange(""));
			assertEquals("", scanRange(new PipeArgs("*.8"), true).extractRange(""));
			assertEquals("hel", scanRange(new PipeArgs("1-3"), true).extractRange("hello"));
			assertEquals("hel", scanRange(new PipeArgs("1.3"), true).extractRange("hello"));
			assertEquals("llo", scanRange(new PipeArgs("3-5"), true).extractRange("hello"));
			assertEquals("llo", scanRange(new PipeArgs("3.3"), true).extractRange("hello"));
			assertEquals("hello", scanRange(new PipeArgs("1-*"), true).extractRange("hello"));
			assertEquals("hello", scanRange(new PipeArgs("1.*"), true).extractRange("hello"));
			assertEquals("hello", scanRange(new PipeArgs("*-*"), true).extractRange("hello"));
			assertEquals("hello", scanRange(new PipeArgs("*.*"), true).extractRange("hello"));
			assertEquals("", scanRange(new PipeArgs("8-18"), true).extractRange("hello"));
			assertEquals("", scanRange(new PipeArgs("8.18"), true).extractRange("hello"));
			assertEquals("llo", scanRange(new PipeArgs("3-18"), true).extractRange("hello"));
			assertEquals("llo", scanRange(new PipeArgs("3.18"), true).extractRange("hello"));
			assertEquals("l", scanRange(new PipeArgs("4"), true).extractRange("hello"));
			assertEquals("", scanRange(new PipeArgs("8"), true).extractRange("hello"));

			String s = "abcdefghijklmnopqrstuvwxyz";
			assertEquals("abcdefghijklm", scanRange(new PipeArgs("1-13 "), true).extractRange(s));
			assertEquals("cdefghij", scanRange(new PipeArgs("SUBSTR 3.8 OF 1-13 "), true).extractRange(s));
			assertEquals("defg", scanRange(new PipeArgs("SUBSTR 2-5 OF SUBSTR 3.8 OF 1-13 "), true).extractRange(s));
			assertEquals("z", scanRange(new PipeArgs("-1"), true).extractRange(s));
			assertEquals("yz", scanRange(new PipeArgs("-2;-1"), true).extractRange(s));
			assertEquals("yz", scanRange(new PipeArgs("25-26"), true).extractRange(s));
			assertEquals("yz", scanRange(new PipeArgs("25;-1"), true).extractRange(s));
			try {
				scanRange(new PipeArgs("-1;-2"), true).extractRange(s);
			}
			catch(PipeException e) {
				assertEquals(-54, e.getMessageNo());
			}
			

			s = "  hello there   how   are   you today? I am well thanks  ";
			assertEquals("hello", scanRange(new PipeArgs("words 1"), true).extractRange(s));
			assertEquals("how", scanRange(new PipeArgs("words 3"), true).extractRange(s));
			assertEquals("hello there   how", scanRange(new PipeArgs("words 1-3"), true).extractRange(s));
			assertEquals("hello there   how   are   you today?", scanRange(new PipeArgs("words 1-6"), true).extractRange(s));
			assertEquals("hello there   how   are   you today? I am well thanks", scanRange(new PipeArgs("words 1-*"), true).extractRange(s));
			assertEquals("hello there   how   are   you today? I am well thanks", scanRange(new PipeArgs("words 1-10"), true).extractRange(s));
			assertEquals("thanks", scanRange(new PipeArgs("words -1"), true).extractRange(s));
			assertEquals("well thanks", scanRange(new PipeArgs("words -2;-1"), true).extractRange(s));
			assertEquals("well thanks", scanRange(new PipeArgs("words 9-10"), true).extractRange(s));
			assertEquals("well thanks", scanRange(new PipeArgs("words 9;-1"), true).extractRange(s));
			assertEquals("I", scanRange(new PipeArgs("words 7.1"), true).extractRange(s));
			assertEquals("hello", scanRange(new PipeArgs("words 1"), true).extractRange("hello"));
			assertEquals("", scanRange(new PipeArgs("words 2"), true).extractRange("hello"));
			assertEquals("", scanRange(new PipeArgs("words 2"), true).extractRange(""));
			assertEquals("hello", scanRange(new PipeArgs("words -2;-1"), true).extractRange("hello"));

			s = ",b,,d,eeee,fff,ggg,";
			assertEquals("", scanRange(new PipeArgs("fieldsep , fields 1"), true).extractRange(s));
			assertEquals("b", scanRange(new PipeArgs("fieldsep , fields 2"), true).extractRange(s));
			assertEquals("d,eeee,fff", scanRange(new PipeArgs("fieldsep , fields 4-6"), true).extractRange(s));
			assertEquals(",b,,d,eeee,fff,ggg", scanRange(new PipeArgs("fieldsep , fields 1-7"), true).extractRange(s));
			assertEquals(",b,,d,eeee,fff,ggg,", scanRange(new PipeArgs("fieldsep , fields 1-*"), true).extractRange(s));
			assertEquals(",b,,d,eeee,fff,ggg,", scanRange(new PipeArgs("fieldsep , fields 1-8"), true).extractRange(s));
			assertEquals("", scanRange(new PipeArgs("fieldsep , fields -1"), true).extractRange(s));
			assertEquals("ggg,", scanRange(new PipeArgs("fieldsep , fields -2;-1"), true).extractRange(s));
			assertEquals("ggg,", scanRange(new PipeArgs("fieldsep , fields 7-8"), true).extractRange(s));
			assertEquals("ggg,", scanRange(new PipeArgs("fieldsep , fields 7;-1"), true).extractRange(s));

			args = new PipeArgs("words 1-2 words 3-4");
			assertEquals("W 1-2", scanRange(args, true).toString());
			assertEquals("words 3-4", args.getRemainder());
			args = new PipeArgs("SUBSTR 2-5 OF SUBSTR 3.2 OF 1-10 words 3-4");
			assertEquals("SUBSTR 2-5 OF SUBSTR 3-4 OF 1-10", scanRange(args, true).toString());
			assertEquals("words 3-4", args.getRemainder());
			args = new PipeArgs("SUBSTR 2-5 OF SUBSTR 3.2 OF 1-10 SUBSTR 2-5 OF SUBSTR 3.2 OF 1-10");
			assertEquals("SUBSTR 2-5 OF SUBSTR 3-4 OF 1-10", scanRange(args, true).toString());
			assertEquals("SUBSTR 2-5 OF SUBSTR 3.2 OF 1-10", args.getRemainder());
	
			args = new PipeArgs("word1 word2");
			assertEquals("word1", args.nextWord());
			assertEquals(null, scanRange(args, false));
			assertEquals("word2", args.nextWord());
	
			args = new PipeArgs("w1");
			assertEquals("W 1-1", scanRange(args, true).toString());
			args = new PipeArgs("FS 1 F2.5");
			assertEquals("FS 1 F 2-6", scanRange(args, true).toString());
			
			args = new PipeArgs("()");
			try {
				scanRange(args, true);
			}
			catch(PipeException e) {
				assertEquals(-55, e.getMessageNo());
			}
			args = new PipeArgs("(1.1)");
			assertEquals("(1-1)", scanRange(args, true).toString());
			args = new PipeArgs("(1.1 2-5)");
			assertEquals("(1-1 2-5)", scanRange(args, true).toString());
			args = new PipeArgs("(x)");
			try {
				scanRange(args, true);
			}
			catch(PipeException e) {
				assertEquals(-54, e.getMessageNo());
			}
			args = new PipeArgs("(x");
			try {
				scanRange(args, true);
			}
			catch(PipeException e) {
				assertEquals(-200, e.getMessageNo());
			}

			args = new PipeArgs("(1.1 3-5 9.4 -2;-1)");
			assertEquals("acdeijklyz", scanRange(args, true).extractRange("abcdefghijklmnopqrstuvwxyz"));

			return 0;
		}
	}

	public void testRangeReplace() throws PipeException {
		Pipe.register("rangereplace", RangeReplaceTest.class);
		assertEquals(0, new Pipe().run("rangereplace"));
	}
	
	public static class RangeReplaceTest extends Stage {
		public int execute(String input) throws PipeException {
			String alpha = "abcdefghijklmnopqrstuvwxyz";

			assertResult("xxxxxfghijklmnopqrstuvwxyz", 1, scanRange(new PipeArgs("1-10"), true).replace(alpha, "abcde", "xxxxx", 1, false));
			assertResult("abcdefghijklmnopqrstuvwxyz", 0, scanRange(new PipeArgs("3-10"), true).replace(alpha, "abcde", "xxxxx", 1, false));

			assertResult("b aa a", 1, scanRange(new PipeArgs("1-*"), true).replace("a aa a", "a", "b", 1, false));
			assertResult("b ba a", 2, scanRange(new PipeArgs("1-*"), true).replace("a aa a", "a", "b", 2, false));
			assertResult("b bb a", 3, scanRange(new PipeArgs("1-*"), true).replace("a aa a", "a", "b", 3, false));
			assertResult("b bb b", 4, scanRange(new PipeArgs("1-*"), true).replace("a aa a", "a", "b", 4, false));
			assertResult("b bb b", 4, scanRange(new PipeArgs("1-*"), true).replace("a aa a", "a", "b", 5, false));
			assertResult("b bb b", 4, scanRange(new PipeArgs("1-*"), true).replace("a aa a", "a", "b", -1, false));
			assertResult("a ba a", 1, scanRange(new PipeArgs("3-4"), true).replace("a aa a", "a", "b", 1, false));
			assertResult("a bb a", 2, scanRange(new PipeArgs("3-4"), true).replace("a aa a", "a", "b", 2, false));
			assertResult("a bb a", 2, scanRange(new PipeArgs("3-4"), true).replace("a aa a", "a", "b", 3, false));
			assertResult("a bb a", 2, scanRange(new PipeArgs("3-4"), true).replace("a aa a", "a", "b", 4, false));
			assertResult("a bb a", 2, scanRange(new PipeArgs("3-4"), true).replace("a aa a", "a", "b", 5, false));
			assertResult("a bb a", 2, scanRange(new PipeArgs("3-4"), true).replace("a aa a", "a", "b", -1, false));
			
			assertResult("b aa a", 1, scanRange(new PipeArgs("1-*"), true).replace("a aa a", "A", "b", 1, true));
			assertResult("b ba a", 2, scanRange(new PipeArgs("1-*"), true).replace("a aa a", "A", "b", 2, true));
			assertResult("b bb a", 3, scanRange(new PipeArgs("1-*"), true).replace("a aa a", "A", "b", 3, true));
			assertResult("b bb b", 4, scanRange(new PipeArgs("1-*"), true).replace("a aa a", "A", "b", 4, true));
			assertResult("b bb b", 4, scanRange(new PipeArgs("1-*"), true).replace("a aa a", "A", "b", 5, true));
			assertResult("b bb b", 4, scanRange(new PipeArgs("1-*"), true).replace("a aa a", "A", "b", -1, true));
			assertResult("a ba a", 1, scanRange(new PipeArgs("3-4"), true).replace("a aa a", "A", "b", 1, true));
			assertResult("a bb a", 2, scanRange(new PipeArgs("3-4"), true).replace("a aa a", "A", "b", 2, true));
			assertResult("a bb a", 2, scanRange(new PipeArgs("3-4"), true).replace("a aa a", "A", "b", 3, true));
			assertResult("a bb a", 2, scanRange(new PipeArgs("3-4"), true).replace("a aa a", "A", "b", 4, true));
			assertResult("a bb a", 2, scanRange(new PipeArgs("3-4"), true).replace("a aa a", "A", "b", 5, true));
			assertResult("a bb a", 2, scanRange(new PipeArgs("3-4"), true).replace("a aa a", "A", "b", -1, true));
			
			assertResult("Mr. Peter Edwards", 1, scanRange(new PipeArgs("1-*"), true).replace("Mr. Craig Edwards", "Craig", "Peter", -1, false));
			assertResult("Mr. Peter Edwards", 1, scanRange(new PipeArgs("1-*"), true).replace("Mr. Craig Edwards", "craig", "Peter", -1, true));
			assertResult("Mr. Peter Edwards", 1, scanRange(new PipeArgs("1-*"), true).replace("Mr. Craig Edwards", "craig", "peter", -1, true));
			assertResult("Mr. craig Edwards", 0, scanRange(new PipeArgs("1-*"), true).replace("Mr. craig Edwards", "Craig", "Peter", -1, false));
			assertResult("Mr. Peter Edwards", 1, scanRange(new PipeArgs("1-*"), true).replace("Mr. craig Edwards", "craig", "Peter", -1, true));
			assertResult("Mr. peter Edwards", 1, scanRange(new PipeArgs("1-*"), true).replace("Mr. craig Edwards", "craig", "peter", -1, true));
			
			assertResult("I watch the NFL.", 0, scanRange(new PipeArgs("1-*"), true).replace("I watch the NFL.", "nfl", "soccer", -1, false));
			assertResult("I watch the SOCCER.", 1, scanRange(new PipeArgs("1-*"), true).replace("I watch the NFL.", "nfl", "soccer", -1, true));

			assertResult("aaaaaaaaaa", 10, scanRange(new PipeArgs("1-*"), true).replace("xxxxxxxxxx", "x", "a", -1, false));
			assertResult("aaaaaxxxxx", 5,  scanRange(new PipeArgs("1-*"), true).replace("xxxxxxxxxx", "x", "a", 5, false));
			assertResult("xxaaaaaxxx", 5,  scanRange(new PipeArgs("3-*"), true).replace("xxxxxxxxxx", "x", "a", 5, false));
			assertResult("abababababababababab", 10,  scanRange(new PipeArgs("1-*"), true).replace("xxxxxxxxxx", "x", "ab", -1, false));
			assertResult("xxxxxxxxxxxxxxxxxxxx", 10,  scanRange(new PipeArgs("1-*"), true).replace("xxxxxxxxxx", "x", "xx", -1, false));
			
			assertResult("xxabcde", 1,  scanRange(new PipeArgs("1-*"), true).replace("abcde", "", "xx", -1, false));
			assertResult("abcde", 0,  scanRange(new PipeArgs("1-*"), true).replace("abcde", "a", "a", -1, false));
			assertResult("abde", 1,  scanRange(new PipeArgs("1-*"), true).replace("abcde", "c", "", -1, false));
			assertResult("abde", 2,  scanRange(new PipeArgs("1-*"), true).replace("abccde", "c", "", -1, false));
			assertResult("abcde", 1,  scanRange(new PipeArgs("1-*"), true).replace("", "", "abcde", -1, false));
			assertResult("abxxcde", 1,  scanRange(new PipeArgs("3-*"), true).replace("abcde", "", "xx", -1, false));
			assertResult("abcdexx", 1,  scanRange(new PipeArgs("10-*"), true).replace("abcde", "", "xx", -1, false));
			
			assertResult("xxaaxxaaaa", 4,  scanRange(new PipeArgs("(1.2 5.2)"), true).replace("aaaaaaaaaa", "a", "x", -1, false));
			assertResult("xxaaxaaaaa", 3,  scanRange(new PipeArgs("(1.2 5.2)"), true).replace("aaaaaaaaaa", "a", "x", 3, false));

			return 0;
		}
		private void assertResult(String expectedString, int expectedCount, Range.ReplaceResult result) {
			assertEquals(expectedString, result.changed);
			assertEquals(expectedCount, result.numberOfChanges);
		}
	}

	private void assertEquals(String[] expected, String[] actual) {
		assertEquals(expected.length, actual.length);
		for (int i = 0; i < expected.length; i++) 
			assertEquals(expected[i], actual[i]);
	}
}
