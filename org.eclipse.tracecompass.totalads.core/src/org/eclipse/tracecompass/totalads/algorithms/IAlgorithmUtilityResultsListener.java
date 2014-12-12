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
package org.eclipse.tracecompass.totalads.algorithms;

import java.util.HashMap;

import org.eclipse.tracecompass.totalads.algorithms.AlgorithmUtility;
import org.eclipse.tracecompass.totalads.algorithms.Results;

/**
 * An interface to listen to results of test traces generated using testing.
 *
 * @author Syed Shariyar Murtaza
 *
 */
public interface IAlgorithmUtilityResultsListener {
    /**
     * Listener for test results about a recent trace and execution. It is
     * called by {@link AlgorithmUtility} during testing to notify about results
     * in real time
     *
     * @param traceName
     *            Trace name
     * @param modelsAndResults
     *            A map containing models and their results about a trace in the
     *            test folder
     * @param modelsAndAnomalyCount
     *            A map containing models and total anomalous traces found so
     *            far during testing from the beginning of execution
     */
    public void listenTestResults(String traceName, HashMap<String, Results> modelsAndResults,
            HashMap<String, Double> modelsAndAnomalyCount);
}
