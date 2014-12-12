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

import org.eclipse.tracecompass.totalads.algorithms.AlgorithmFactory;

/**
 * This class provides choices of Algorithm Types that must be used to register an algorithm with
 *  {@link AlgorithmFactory} or to retrieve a list of algorithms from {@link AlgorithmFactory}. Every
 *  new algorithm must be one of the types listed here.
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public enum AlgorithmTypes {
    /**
     * Represents Anomaly Detection Algorithms
     */
    ANOMALY,
    /**
     * Represents Classification Algorithms
     */
    CLASSIFCATION,
    /**
     * Represents Clustering Algorithms
     */
    CLUSTERING;

}
