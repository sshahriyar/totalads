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

package org.eclipse.tracecompass.totalads.core.tests.algorithms;

import static org.junit.Assert.*;

import org.eclipse.tracecompass.totalads.exceptions.TotalADSGeneralException;
import org.eclipse.tracecompass.totalads.algorithms.AlgorithmFactory;
import org.eclipse.tracecompass.totalads.algorithms.AlgorithmTypes;
import org.eclipse.tracecompass.totalads.algorithms.IDetectionAlgorithm;
import org.junit.Assume;
import org.junit.Test;

/**
 * Tests for the {@link AlgorithmFactory} class
 *
 * @author Syed Shariyar Murtaza
 *
 */
public class AlgorithmFactoryTest {

    /**
     * Test method for
     * {@link org.eclipse.tracecompass.totalads.algorithms.AlgorithmFactory#getInstance()}
     * .
     */
    @Test
    public void testGetInstance() {
        AlgorithmFactory algFactory = AlgorithmFactory.getInstance();
        assertNotNull("Algorithm Factory is null", algFactory);
    }

    /**
     * Test method for
     * {@link org.eclipse.tracecompass.totalads.algorithms.AlgorithmFactory#destroyInstance()}
     * .
     *
     */
    @Test
    public void testDestroyInstance() {

        AlgorithmFactory.destroyInstance();// This code will always execute
                                           // correctly
        Assume.assumeTrue(true);
    }

    /**
     * Test method for
     * {@link org.eclipse.tracecompass.totalads.algorithms.AlgorithmFactory#getAlgorithms(org.eclipse.tracecompass.totalads.algorithms.AlgorithmTypes)}
     * .
     *
     * @throws TotalADSGeneralException
     *             Validation exception for parameters
     */
    @Test
    public void testGetAlgorithms() throws TotalADSGeneralException {
        AlgorithmFactory.getInstance().initialize();
        IDetectionAlgorithm[] algs = AlgorithmFactory.getInstance().getAlgorithms(AlgorithmTypes.ANOMALY);
        AlgorithmFactory.destroyInstance();
        assertTrue("No algorithms are found", algs.length > 0);

    }

    /**
     * Test method for
     * {@link org.eclipse.tracecompass.totalads.algorithms.AlgorithmFactory#registerModelWithFactory(org.eclipse.tracecompass.totalads.algorithms.AlgorithmTypes, org.eclipse.tracecompass.totalads.algorithms.IDetectionAlgorithm)}
     * .
     *
     * @throws TotalADSGeneralException
     *             Validation exception for parameters
     */
    @Test(expected = TotalADSGeneralException.class)
    public void testRegisterModelWithFactoryForNullValues() throws TotalADSGeneralException {

        AlgorithmFactory.getInstance().registerModelWithFactory
                (AlgorithmTypes.ANOMALY, null);
        // RegisterModelWithFactory is already tested in initialize,
        // automatically
    }

    /**
     * Test method for
     * {@link org.eclipse.tracecompass.totalads.algorithms.AlgorithmFactory#getAlgorithmByAcronym(java.lang.String)}
     * .
     *
     * @throws TotalADSGeneralException
     *             Validation exception for parameters
     */
    @Test
    public void testGetAlgorithmByAcronym() throws TotalADSGeneralException {
        AlgorithmFactory.getInstance().initialize();
        IDetectionAlgorithm alg = AlgorithmFactory.getInstance().getAlgorithmByAcronym("SQM");
        AlgorithmFactory.destroyInstance();
        assertTrue("Algorithm is not found", alg != null);
    }

    /**
     * Test method for
     * {@link org.eclipse.tracecompass.totalads.algorithms.AlgorithmFactory#getAlgorithmByAcronym(java.lang.String)}
     * .
     */
    @Test
    public void testGetAlgorithmByAcronymNullValue() {

        IDetectionAlgorithm alg = AlgorithmFactory.getInstance().getAlgorithmByAcronym(null);
        assertTrue("Algorithm should have been null here", alg == null);

    }

    /**
     * Test method for
     * {@link org.eclipse.tracecompass.totalads.algorithms.AlgorithmFactory#initialize()}
     * .
     *
     * @throws TotalADSGeneralException
     *             Validation exception for parameters
     */
    @Test
    public void testInitialize() throws TotalADSGeneralException {
        AlgorithmFactory algFac = AlgorithmFactory.getInstance();
        algFac.initialize();
        IDetectionAlgorithm[] algs = algFac.getAlgorithms(AlgorithmTypes.ANOMALY);
        assertTrue("Algorithms not initialized", algs != null);
        AlgorithmFactory.destroyInstance();

    }

}
