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

/**
 * An interface to display the outputs of algorithms
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public interface IAlgorithmOutStream {
    /**
     * Adds new event to the output stream
     *
     * @param event
     *            Information to display on the output stream
     */

    public void addOutputEvent(String event);

    /**
     * Adds new line to the output Stream
     */
    public void addNewLine();
}
