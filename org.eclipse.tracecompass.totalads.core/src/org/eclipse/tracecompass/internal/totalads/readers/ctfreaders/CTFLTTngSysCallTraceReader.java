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
package org.eclipse.tracecompass.internal.totalads.readers.ctfreaders;

import java.io.File;

import org.eclipse.tracecompass.tmf.core.exceptions.TmfTraceException;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSGeneralException;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSReaderException;
import org.eclipse.tracecompass.totalads.readers.ITraceIterator;
import org.eclipse.tracecompass.totalads.readers.ITraceTypeReader;
import org.eclipse.tracecompass.totalads.readers.TraceTypeFactory;

/**
 * The class to read system calls from LTTng traces by using CtfTmfTrace class
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 */
public class CTFLTTngSysCallTraceReader implements ITraceTypeReader {
    /**
     * Constructor: Instantiate a new trace reader
     *
     */
    public CTFLTTngSysCallTraceReader() {
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.tracecompass.totalads.readers.ITraceTypeReader#createInstance
     * ()
     */
    @Override
    public ITraceTypeReader createInstance() {
        return new CTFLTTngSysCallTraceReader();
    }

    /**
     * Registers itself with the TraceTypeFactory
     *
     * @throws TotalADSGeneralException
     *             Exception for invalid reader
     */
    public static void registerTraceTypeReader() throws TotalADSGeneralException {
        TraceTypeFactory trcTypFactory = TraceTypeFactory.getInstance();
        CTFLTTngSysCallTraceReader kernelTraceReader = new CTFLTTngSysCallTraceReader();
        trcTypFactory.registerTraceReaderWithFactory(kernelTraceReader.getName(), kernelTraceReader);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.tracecompass.totalads.readers.ITraceTypeReader#getName()
     */
    @Override
    public String getName() {
        return "LTTng System Call"; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.tracecompass.totalads.readers.ITraceTypeReader#getAcronym()
     */
    @Override
    public String getAcronym() {

        return "SYS"; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.tracecompass.totalads.readers.ITraceTypeReader#getTraceIterator
     * (java.io.File)
     */
    @Override
    public ITraceIterator getTraceIterator(File file) throws TotalADSReaderException {

        if (file == null) {
            throw new TotalADSReaderException(Messages.CTFLTTngSysCallTraceReader_NullArgument);
        }

        String filePath = file.getPath();
        // CtfTmfTrace fTrace =null;

        try {
            return new CTFSystemCallIterator(filePath);

        } catch (TmfTraceException e) {

            /* Should not happen if tracesExist() passed */
            throw new TotalADSReaderException(e.getMessage() + "\n File: " + file.getName()); //$NON-NLS-1$
        }

    }

}
