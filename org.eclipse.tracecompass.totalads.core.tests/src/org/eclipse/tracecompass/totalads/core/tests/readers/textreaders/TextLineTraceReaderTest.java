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

package org.eclipse.tracecompass.totalads.core.tests.readers.textreaders;

import static org.junit.Assert.*;

import org.eclipse.tracecompass.totalads.exceptions.TotalADSReaderException;
import org.eclipse.tracecompass.totalads.readers.ITraceTypeReader;
import org.eclipse.tracecompass.totalads.readers.TraceTypeFactory;
import org.junit.Test;

/**
 * Test cases for {@link TextLineTraceReaderTest} class
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class TextLineTraceReaderTest {

    /**
     * Tests the constructor
     */
    @Test
    public void testTextLineTraceReader() {

        ITraceTypeReader textReader = TraceTypeFactory.getInstance().getCTFKernelReaderOrSimpleTextReader(false);
        assertTrue(textReader.createInstance() != null);// Create instance calls
                                                        // the constructor
                                                        // Constructor cannot be
                                                        // called from outside
                                                        // of the totalads.core
                                                        // package

    }

    /**
     * Tests the createInstance
     */
    @Test
    public void testCreateInstance() {
        ITraceTypeReader textReader = TraceTypeFactory.getInstance().getCTFKernelReaderOrSimpleTextReader(false);
        assertTrue(textReader.createInstance() != null);

    }

    /**
     * Tests the getName function
     */
    @Test
    public void testGetName() {
        ITraceTypeReader textReader = TraceTypeFactory.getInstance().getCTFKernelReaderOrSimpleTextReader(false);
        assertTrue(textReader.getName().length() > 0);
    }

    /**
     * Tests the getAcronym function
     */
    @Test
    public void testGetAcronym() {
        ITraceTypeReader textReader = TraceTypeFactory.getInstance().getCTFKernelReaderOrSimpleTextReader(false);
        assertTrue(textReader.getAcronym().length() > 0);
    }

    /**
     * Tests the getTraceIterator function
     *
     * @throws TotalADSReaderException
     *             I/O Exception
     */
    @Test(expected = TotalADSReaderException.class)
    public void testGetTraceIterator() throws TotalADSReaderException {
        ITraceTypeReader textReader = TraceTypeFactory.getInstance().getCTFKernelReaderOrSimpleTextReader(false);
        textReader.getTraceIterator(null);// getTraceIterator has already been
                                          // tested in the
                                          // traceIterator class

    }

}
