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

import java.io.File;

import org.eclipse.tracecompass.totalads.exceptions.TotalADSReaderException;
import org.eclipse.tracecompass.totalads.readers.ITraceIterator;
import org.eclipse.tracecompass.totalads.readers.ITraceTypeReader;

/**
 * Each trace type parser should implement the functions of this interface and,
 * in addition, a static function registerTraceTypeReader
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 */
public interface ITraceTypeReader {
    /**
     * Returns a self created instance of the model
     *
     * @return An instance of the trace reader
     **/
    public ITraceTypeReader createInstance();

    /**
     * Gets the name of the model
     *
     * @return The name
     **/
    public String getName();

    /**
     * Returns the acronym of the trace reader; should only be three characters long
     *
     * @return The acronym
     */
    public String getAcronym();

    /**
     * Creates a trace iterator for a file
     *
     * @param file
     *            An object representing a file
     * @return Iterator for a trace
     * @throws TotalADSReaderException
     *             An exception about reading of a trace
     */
    public ITraceIterator getTraceIterator(File file) throws TotalADSReaderException;
    // /////////////////////////////////////////////////////////////////////////////////
    // A reader must register itself with the {@link TraceTypeFactory}
    // Each derived class must implement the following static method
    // public static void registerTraceTypeReader() throws
    // TotalADSGeneralException
    // /////////////////////////////////////////////////////////////////////////////////
}
