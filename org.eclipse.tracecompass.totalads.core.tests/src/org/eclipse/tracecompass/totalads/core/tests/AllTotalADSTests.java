/*******************************************************************************
 * Copyright (c) 2014-2015 SBA Lab, Concordia University
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Syed Shariyar Murtaza
 *******************************************************************************/

package org.eclipse.tracecompass.totalads.core.tests;

import org.eclipse.tracecompass.totalads.core.tests.algorithms.AlgorithmFactoryTest;
import org.eclipse.tracecompass.totalads.core.tests.algorithms.AlgorithmUtilityTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Master test suite for TotalADS core plugin
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        AlgorithmFactoryTest.class,
        AlgorithmUtilityTest.class,
        org.eclipse.tracecompass.totalads.core.tests.algorithms.hiddenmarkovmodel.HiddenMarkovModelTest.class,
        org.eclipse.tracecompass.totalads.core.tests.algorithms.ksm.KernelStateModelingTest.class,
        org.eclipse.tracecompass.totalads.core.tests.algorithms.slidingwindow.SlidingWindowTest.class,
        org.eclipse.tracecompass.totalads.core.tests.dbms.DBMSFactoryTest.class,
        org.eclipse.tracecompass.totalads.core.tests.readers.TraceTypeFactoryTest.class,
        org.eclipse.tracecompass.totalads.core.tests.readers.ctfreaders.CTFLTTngSysCallTraceReaderTest.class,
        org.eclipse.tracecompass.totalads.core.tests.readers.textreaders.TextLineTraceReaderTest.class

})
public class AllTotalADSTests {

}
