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

import java.util.ArrayList;

import org.eclipse.tracecompass.totalads.algorithms.IAlgorithmOutObserver;
import org.eclipse.tracecompass.totalads.algorithms.IAlgorithmOutStream;

/**
 * This class provides concrete functions to output messages to a class
 * implementing {@link IAlgorithmOutObserver} interface. This makes it
 * independent of the display mechanism, which could be a simple console, GUI, a
 * web page, a log file or any thing else.
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class AlgorithmOutStream implements IAlgorithmOutStream {

    private ArrayList<IAlgorithmOutObserver> fObservers;

    /**
     * Constructor
     */
    public AlgorithmOutStream() {
        fObservers = new ArrayList<>();
    }

    /**
     * Adds new event to the output stream
     *
     * @param event
     *            Information to display on the output stream
     */
    @Override
    public void addOutputEvent(String event) {
        notifyObservers(event);

    }

    /**
     * Adds new line to the output Stream
     */
    @Override
    public void addNewLine() {
        notifyObservers("\n"); //$NON-NLS-1$
    }

    /**
     * Adds an observer of type {@link IAlgorithmOutObserver}
     *
     * @param observer
     *            Observer
     */
    public void addObserver(IAlgorithmOutObserver observer) {
        fObservers.add(observer);

    }

    /**
     * Removes an observer of type {@link IAlgorithmOutObserver}
     *
     * @param observer
     *            Observer
     */

    public void removeObserver(IAlgorithmOutObserver observer) {
        fObservers.remove(observer);

    }

    /**
     *
     * Notifies all observers of type
     *
     * @param event
     *            Message to observers
     */
    private void notifyObservers(String event) {
        for (IAlgorithmOutObserver ob : fObservers) {
            ob.updateOutput(event);
        }
    }
}
