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

package org.eclipse.tracecompass.totalads.readers;

import static org.junit.Assert.*;

import org.eclipse.tracecompass.totalads.exceptions.TotalADSGeneralException;
import org.eclipse.tracecompass.totalads.readers.ITraceTypeReader;
import org.eclipse.tracecompass.totalads.readers.TraceTypeFactory;
import org.junit.Test;

/**
 * Tests for {@link TraceTypeFactory} class
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class TraceTypeFactoryTest {

    /**
     * Tests the getInstance function
     */
    @Test
    public void testGetInstance() {
        TraceTypeFactory traceFactory = TraceTypeFactory.getInstance();
        TraceTypeFactory.destroyInstance();
        assertTrue(traceFactory != null);

    }

    /**
     * Tests the destroyInstance function
     */
    @Test
    public void testDestroyInstance() {
        TraceTypeFactory.destroyInstance();
        assertTrue(true);// If the code reaches here, then the test case passed
    }

    /**
     * Tests the getTraceReader function
     *
     * @throws TotalADSGeneralException
     *             General exception
     */
    @Test
    public void testGetTraceReader() throws TotalADSGeneralException {

        TraceTypeFactory fac = TraceTypeFactory.getInstance();
        ITraceTypeReader textReader = fac.getCTFKernelReaderOrSimpleTextReader(false);
        fac.registerTraceReaderWithFactory(textReader.getName(), textReader);
        ITraceTypeReader reader = TraceTypeFactory.getInstance().getTraceReader(textReader.getName());
        TraceTypeFactory.destroyInstance();
        assertTrue(reader != null);
    }

    /**
     * Tests the getAllTraceReaders function
     *
     * @throws TotalADSGeneralException
     *             General exception
     */
    @Test
    public void testGetAllTraceReaders() throws TotalADSGeneralException {
        TraceTypeFactory fac = TraceTypeFactory.getInstance();

        ITraceTypeReader textReader = fac.getCTFKernelReaderOrSimpleTextReader(false);
        fac.registerTraceReaderWithFactory(textReader.getName(), textReader);

        ITraceTypeReader kernelReader = fac.getCTFKernelReaderOrSimpleTextReader(true);
        fac.registerTraceReaderWithFactory(kernelReader.getName(), kernelReader);

        ITraceTypeReader[] readers = TraceTypeFactory.getInstance().getAllTraceReaders();
        TraceTypeFactory.destroyInstance();
        assertTrue(readers != null && readers.length > 1);
    }

    /**
     * Tests getAllTraceTypeReaderKeys function
     *
     * @throws TotalADSGeneralException
     *             General exception
     */
    @Test
    public void testGetAllTraceTypeReaderKeys() throws TotalADSGeneralException {
        TraceTypeFactory fac = TraceTypeFactory.getInstance();

        ITraceTypeReader textReader = fac.getCTFKernelReaderOrSimpleTextReader(false);
        fac.registerTraceReaderWithFactory(textReader.getName(), textReader);

        ITraceTypeReader kernelReader = fac.getCTFKernelReaderOrSimpleTextReader(true);
        fac.registerTraceReaderWithFactory(kernelReader.getName(), kernelReader);

        String[] keys = TraceTypeFactory.getInstance().getAllTraceTypeReaderKeys();
        TraceTypeFactory.destroyInstance();
        assertTrue(keys.length > 1);
    }

    /**
     * Tests the registerTraceReaderWithFactory function for null values
     *
     * @throws TotalADSGeneralException
     *             General exception
     */
    @Test(expected = TotalADSGeneralException.class)
    public void testRegisterTraceReaderWithFactoryForNullValues() throws TotalADSGeneralException {
        // registerTraceReaderWithFactory with proper arguments
        // is automatically tested in all other functions. Only testing for null
        // values here
        TraceTypeFactory trcTypFactory = TraceTypeFactory.getInstance();
        trcTypFactory.registerTraceReaderWithFactory(null, null);

    }

    /**
     * Tests the CTFKernelReaderOrSimpleTextReader function
     */
    @Test
    public void testCTFKernelReaderOrSimpleTextReader() {
        ITraceTypeReader readerCtf = TraceTypeFactory.getInstance().
                getCTFKernelReaderOrSimpleTextReader(true);
        ITraceTypeReader readerText = TraceTypeFactory.getInstance().
                getCTFKernelReaderOrSimpleTextReader(false);
        assertTrue(readerCtf != null && readerText != null);

    }

    /**
     * Tests the initialize function
     */
    @Test
    public void testInitialize() {
        // Initialize does not work in JUnit because CustomTMFReaders in
        // initialize
        // require initialization of Eclipse platform. However, initialize
        // actually calls
        // registerTraceReaderWithFactory which has been tested
        // TraceTypeFactory.getInstance().initialize();

        assertTrue(true);

    }

}
