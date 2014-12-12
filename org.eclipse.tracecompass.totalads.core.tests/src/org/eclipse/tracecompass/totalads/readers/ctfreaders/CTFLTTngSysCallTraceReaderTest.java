/*********************************************************************************************
 * Copyright (c) 2014-2015  Software Behaviour Analysis Lab, Concordia University, Montreal, Canada
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of Eclipse Public License v1.0 License which
 * accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Syed Shariyar Murtaza -- Initial design and implementation
 **********************************************************************************************/

package org.eclipse.tracecompass.totalads.readers.ctfreaders;

import static org.junit.Assert.*;

import org.eclipse.tracecompass.totalads.exceptions.TotalADSReaderException;
import org.eclipse.tracecompass.totalads.readers.ITraceTypeReader;
import org.eclipse.tracecompass.totalads.readers.TraceTypeFactory;
import org.junit.Test;

/**
 * Test cases for {@link CTFLTTngSysCallTraceReaderTest} class
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class CTFLTTngSysCallTraceReaderTest {

    /**
     * Tests the constructor
     */
    @Test
    public void testCTFLTTngSysCallTraceReader() {
        ITraceTypeReader ctfReader = TraceTypeFactory.getInstance().getCTFKernelReaderOrSimpleTextReader(true);
        assertTrue(ctfReader.createInstance() != null);// create instructor
                                                       // calls the constructor
                                                       // Constructor cannot be
                                                       // called from outside of
                                                       // the totalads.core
                                                       // package

    }

    /**
     * Tests the createInstance function
     */
    @Test
    public void testCreateInstance() {
        ITraceTypeReader ctfReader = TraceTypeFactory.getInstance().getCTFKernelReaderOrSimpleTextReader(true);
        assertTrue(ctfReader.createInstance() != null);

    }

    /**
     * Tests the getName function
     */
    @Test
    public void testGetName() {
        ITraceTypeReader ctfReader = TraceTypeFactory.getInstance().getCTFKernelReaderOrSimpleTextReader(true);
        assertTrue(ctfReader.getName().length() > 0);
    }

    /**
     * Tests the getAcronym function
     */
    @Test
    public void testGetAcronym() {
        ITraceTypeReader ctfReader = TraceTypeFactory.getInstance().getCTFKernelReaderOrSimpleTextReader(true);
        assertTrue(ctfReader.getAcronym().length() > 0);
    }

    /**
     * Tests the getTraceIterator function
     *
     * @throws TotalADSReaderException
     *             General exception
     */
    @Test(expected = TotalADSReaderException.class)
    public void testGetTraceIterator() throws TotalADSReaderException {
        ITraceTypeReader ctfReader = TraceTypeFactory.getInstance().getCTFKernelReaderOrSimpleTextReader(true);
        ctfReader.getTraceIterator(null);// getTraceIterator has already been
                                         // tested in the
                                         // traceIterator class

    }

}
