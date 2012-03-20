package com.hae.pipe;


// TODO - Throw a null pointer somewhere in dispatcher.  It should shut the pipe down.
// TODO - callpipe/addpipe
// TODO - stall (kill all threads)
// TODO - Investigate "literal a|f: fanin|f:" not connecting primary output of fanin to secondary input
// TODO - test case for fanin with a stream id, instead of a number

import junit.framework.*;

public class AutomatedTests extends TestSuite {
	public static final String COPYRIGHT = "Copyright 2007,2012. H.A. Enterprises Pty Ltd. All Rights Reserved";
	
	public static TestSuite suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(TestSyntax.class);
		suite.addTestSuite(TestScanner.class);

		suite.addTestSuite(TestCommandAddStream.class);
		suite.addTestSuite(TestCommandCallPipe.class);
		suite.addTestSuite(TestCommandCommitError.class);
		suite.addTestSuite(TestCommandCommitSequencing.class);
		suite.addTestSuite(TestCommandEofReportSet.class);
		suite.addTestSuite(TestCommandEofReportUse.class);
		suite.addTestSuite(TestCommandMaxStream.class);
		suite.addTestSuite(TestCommandResolve.class);
		suite.addTestSuite(TestCommandSetRC.class);
		suite.addTestSuite(TestCommandShort.class);
		suite.addTestSuite(TestCommandStageNum.class);
		suite.addTestSuite(TestCommandStreamState.class);

		suite.addTestSuite(TestStageAbbrev.class);
		suite.addTestSuite(TestStageAddrdw.class);
		suite.addTestSuite(TestStageAggrc.class);
		suite.addTestSuite(TestStageBeat.class);
		suite.addTestSuite(TestStageBetween.class);
		suite.addTestSuite(TestStageChange.class);
		suite.addTestSuite(TestStageChop.class);
		suite.addTestSuite(TestStageCommand.class);
		suite.addTestSuite(TestStageConsole.class);
		suite.addTestSuite(TestStageCount.class);
		suite.addTestSuite(TestStageBuffer.class);
		suite.addTestSuite(TestStageDiskr.class);
		suite.addTestSuite(TestStageDiskw.class);
		suite.addTestSuite(TestStageDuplicate.class);
		suite.addTestSuite(TestStageFanin.class);
		suite.addTestSuite(TestStageFaninany.class);
		suite.addTestSuite(TestStageFanout.class);
		suite.addTestSuite(TestStageGate.class);
		suite.addTestSuite(TestStageHole.class);
		suite.addTestSuite(TestStageHostid.class);
		suite.addTestSuite(TestStageHostname.class);
		suite.addTestSuite(TestStageLiteral.class);
		suite.addTestSuite(TestStageLocate.class);
		suite.addTestSuite(TestStageNLocate.class);
		suite.addTestSuite(TestStageNot.class);
		suite.addTestSuite(TestStageQuery.class);
		suite.addTestSuite(TestStageReverse.class);
		suite.addTestSuite(TestStageSpecs.class);
		suite.addTestSuite(TestStageSplit.class);
		suite.addTestSuite(TestStageStrLiteral.class);
		suite.addTestSuite(TestStageStem.class);
		suite.addTestSuite(TestStageTake.class);
		return suite;
	}
}