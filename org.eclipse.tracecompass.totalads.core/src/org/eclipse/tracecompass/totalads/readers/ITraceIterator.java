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

import org.eclipse.tracecompass.totalads.exceptions.TotalADSReaderException;

/**
 * An Iterator to move through the trace and read events one by one without
 * loading the whole trace in memory
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 **/
public interface ITraceIterator extends AutoCloseable {
    /**
     * Moves Iterator to the next event, and returns true if the iterator can
     * advance or false if the iterator cannot advance
     *
     * @return true or false
     * @throws TotalADSReaderException
     *             An exception during the read operation
     **/
    public boolean advance() throws TotalADSReaderException;

    /**
     * Returns an event based on the current location of the iterator
     *
     * @return Current event
     **/
    public String getCurrentEvent();

    /**
     * Closes the iterator stream
     *
     * @throws TotalADSReaderException
     *             An exception during the read operation
     *
     **/
    @Override
    public void close() throws TotalADSReaderException;

}
