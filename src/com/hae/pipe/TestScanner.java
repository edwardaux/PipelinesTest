package com.hae.pipe;

import com.hae.pipe.stages.*;

import junit.framework.*;

public class TestScanner extends TestCase {
	public static final String COPYRIGHT = "Copyright 2007,2012. H.A. Enterprises Pty Ltd. All Rights Reserved";
	
	private class ScannerResult {
		public Scanner scanner;
		public int rc;
	}
	private ScannerResult scan(int type, String pipelineSpec) {
		ScannerResult result = new ScannerResult();
		try {
			Pipe pipe = new Pipe();
			result.scanner = new Scanner(pipe, new Literal(), type, pipelineSpec);
			result.rc = 0;
		}
		catch(PipeException e) {
			result.rc = e.getMessageNo();
		}
		return result;
	}
	public void testScannerEmptySpec() {
		ScannerResult result = scan(Pipe.PIPE_TYPE_PIPE, "");
		assertEquals(256, result.rc);
	}
	
	public void testScannerOptionsNoClosingParen() {
		assertEquals(636, scan(Pipe.PIPE_TYPE_PIPE, "( blah").rc);
	}
	
	public void testScannerOptionsEmptyParen1() throws PipeException {
		ScannerResult result = scan(Pipe.PIPE_TYPE_PIPE, "() literal blah");
		assertEquals(0, result.rc);
		assertEquals('\u0000', result.scanner.getPipeline().getOptions().endChar);
		assertEquals(false, result.scanner.getPipeline().getOptions().listCmd);
		assertEquals(false, result.scanner.getPipeline().getOptions().listErr);
		assertEquals(false, result.scanner.getPipeline().getOptions().listRC);
		assertEquals(7, result.scanner.getPipeline().getOptions().msgLevel);
		assertEquals(null, result.scanner.getPipeline().getOptions().name);
		assertEquals('|', result.scanner.getPipeline().getOptions().stageSep);
		assertEquals(false, result.scanner.getPipeline().getOptions().trace);
	}
	
	public void testScannerOptionsEmptyParen2() {
		ScannerResult result = scan(Pipe.PIPE_TYPE_PIPE, "()");
		assertEquals(256, result.rc);
	}
	
	public void testScannerOptionsEverything() {
		ScannerResult result = scan(Pipe.PIPE_TYPE_PIPE, "(END ? LISTCMD LISTERR LISTRC MSGLEVEL 7 NAME foo SEP ! TRACE) literal blah");
		assertEquals(0, result.rc);
		assertEquals('?', result.scanner.getPipeline().getOptions().endChar);
		assertEquals(true, result.scanner.getPipeline().getOptions().listCmd);
		assertEquals(true, result.scanner.getPipeline().getOptions().listErr);
		assertEquals(true, result.scanner.getPipeline().getOptions().listRC);
		assertEquals(7, result.scanner.getPipeline().getOptions().msgLevel);
		assertEquals("foo", result.scanner.getPipeline().getOptions().name);
		assertEquals('!', result.scanner.getPipeline().getOptions().stageSep);
		assertEquals(true, result.scanner.getPipeline().getOptions().trace);
	}
	
	public void testScannerOptionsNoMode1() {
		ScannerResult result = scan(Pipe.PIPE_TYPE_PIPE, "(END ? NOLISTCMD NOLISTERR NOLISTRC NOMSGLEVEL 7 NAME foo SEP ! NOTRACE) literal blah");
		assertEquals('?', result.scanner.getPipeline().getOptions().endChar);
		assertEquals(false, result.scanner.getPipeline().getOptions().listCmd);
		assertEquals(false, result.scanner.getPipeline().getOptions().listErr);
		assertEquals(false, result.scanner.getPipeline().getOptions().listRC);
		assertEquals(0, result.scanner.getPipeline().getOptions().msgLevel);
		assertEquals("foo", result.scanner.getPipeline().getOptions().name);
		assertEquals('!', result.scanner.getPipeline().getOptions().stageSep);
		assertEquals(false, result.scanner.getPipeline().getOptions().trace);
	}
	
	public void testScannerOptionsNoMode2() {
		ScannerResult result = scan(Pipe.PIPE_TYPE_PIPE, "(END ? NO LISTCMD NO LISTERR NO LISTRC NO MSGLEVEL 7 NAME foo SEP ! NO TRACE) literal blah");
		assertEquals('?', result.scanner.getPipeline().getOptions().endChar);
		assertEquals(false, result.scanner.getPipeline().getOptions().listCmd);
		assertEquals(false, result.scanner.getPipeline().getOptions().listErr);
		assertEquals(false, result.scanner.getPipeline().getOptions().listRC);
		assertEquals(0, result.scanner.getPipeline().getOptions().msgLevel);
		assertEquals("foo", result.scanner.getPipeline().getOptions().name);
		assertEquals('!', result.scanner.getPipeline().getOptions().stageSep);
		assertEquals(false, result.scanner.getPipeline().getOptions().trace);
	}
	
	public void testScannerOptionsAbbrev() {
		ScannerResult result = scan(Pipe.PIPE_TYPE_PIPE, "(ENDCH ? NO LISTCMD NO LISTERR NO LISTRC NO MSG 7 NAME foo SEP ! NO TRACE) literal blah");
		assertEquals(0, result.rc);
	}
	
	public void testScannerOptionsMsgLevel() {
		ScannerResult result;
		result = scan(Pipe.PIPE_TYPE_PIPE, "(MSGLEVEL 7 NOMSGLEVEL 1) literal blah");
		assertEquals(6, result.scanner.getPipeline().getOptions().msgLevel);
		result = scan(Pipe.PIPE_TYPE_PIPE, "(MSGLEVEL 7 NOMSGLEVEL 6) literal blah");
		assertEquals(1, result.scanner.getPipeline().getOptions().msgLevel);
		result = scan(Pipe.PIPE_TYPE_PIPE, "(MSGLEVEL 7 NOMSGLEVEL 15) literal blah");
		assertEquals(0, result.scanner.getPipeline().getOptions().msgLevel);
	}
	
	public void testScannerOptionsStage() {
		ScannerResult result = scan(Pipe.PIPE_TYPE_PIPE, "(MSGLEVEL 7) literal blah | (NOMSGLEVEL 6) console");
		assertEquals(0, result.rc);
		Pipeline pipeline = result.scanner.getPipeline();
		assertEquals(7, pipeline.getOptions().msgLevel);
		Stage console = (Stage)pipeline.getStages().get(1);
		assertEquals(1, console.getOptions().msgLevel);
	}
	
	public void testScannerNullStage() {
		ScannerResult result = scan(Pipe.PIPE_TYPE_PIPE, "console | | console");
		assertEquals(17, result.rc);
	}
	
	public void testScannerNonExistantStage() {
		ScannerResult result;
		result = scan(Pipe.PIPE_TYPE_PIPE, "kfhdkjfhd");
		assertEquals(27, result.rc);
	}
	
	public void testScannerConnectors() throws PipeException {
		ScannerResult result;
		result = scan(Pipe.PIPE_TYPE_CALLPIPE, "*: | console");
		assertEquals(0, result.rc);
		result = scan(Pipe.PIPE_TYPE_CALLPIPE, "*.INPUT: | console");
		assertEquals(0, result.rc);
		result = scan(Pipe.PIPE_TYPE_CALLPIPE, "*.OUTPUT: | console");
		assertEquals(0, result.rc);
		result = scan(Pipe.PIPE_TYPE_CALLPIPE, "*.0: | console");
		assertEquals(0, result.rc);
		result = scan(Pipe.PIPE_TYPE_CALLPIPE, "*.INPUT.0: | console");
		assertEquals(0, result.rc);
		result = scan(Pipe.PIPE_TYPE_CALLPIPE, "*.OUTPUT.0: | console");
		assertEquals(0, result.rc);
		result = scan(Pipe.PIPE_TYPE_CALLPIPE, "*..0: | console");
		assertEquals(0, result.rc);
		result = scan(Pipe.PIPE_TYPE_CALLPIPE, "*.INPUT.*: | console");
		assertEquals(0, result.rc);
		result = scan(Pipe.PIPE_TYPE_CALLPIPE, "*..*: | console");
		assertEquals(0, result.rc);
		result = scan(Pipe.PIPE_TYPE_CALLPIPE, "*..: | console");
		assertEquals(0, result.rc);
		result = scan(Pipe.PIPE_TYPE_CALLPIPE, "*.x.x.0: | console");
		assertEquals(-103, result.rc);
		result = scan(Pipe.PIPE_TYPE_CALLPIPE, "*.IN.abc: | console");   // valid, but no stream exists
		assertEquals(-103, result.rc);
		result = scan(Pipe.PIPE_TYPE_CALLPIPE, "*.IN.abcde: | console"); // invalid
		assertEquals(-165, result.rc);
	}

	public void testScannerLabels() {
		ScannerResult result = scan(Pipe.PIPE_TYPE_PIPE, "(end ?) literal a f: fanout | cons ? f: | cons");
		assertEquals(-46, result.rc);
	}
	

}
