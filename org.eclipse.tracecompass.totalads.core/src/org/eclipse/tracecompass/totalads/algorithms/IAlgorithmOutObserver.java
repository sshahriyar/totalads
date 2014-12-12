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

import org.eclipse.tracecompass.totalads.algorithms.AlgorithmOutStream;

/**
 * An interface that is required to be implemented by the class that displays
 * the output of the algorithms
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public interface IAlgorithmOutObserver {
    /**
     * This method gets called from the {@link AlgorithmOutStream}
     *
     * @param message
     *            The message to be displayed
     */
    public void updateOutput(String message);
}
