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
package org.eclipse.tracecompass.internal.totalads.readers.tmfreaders;

import java.io.File;

import org.eclipse.tracecompass.internal.totalads.readers.tmfreaders.CustomTmfTextIterator;
import org.eclipse.tracecompass.internal.totalads.readers.tmfreaders.CustomTmfTextReader;
import org.eclipse.tracecompass.tmf.core.exceptions.TmfTraceException;
import org.eclipse.tracecompass.tmf.core.parsers.custom.CustomTxtTraceDefinition;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSGeneralException;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSReaderException;
import org.eclipse.tracecompass.totalads.readers.ITraceIterator;
import org.eclipse.tracecompass.totalads.readers.ITraceTypeReader;
import org.eclipse.tracecompass.totalads.readers.TraceTypeFactory;

/**
 * This class implements the trace reader for the custom TMF text parser.
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class CustomTmfTextReader implements ITraceTypeReader {
    private String fReaderName;
    private CustomTxtTraceDefinition fCustReader;

    /**
     * @param custReader
     *            Context of the reader
     */
    public CustomTmfTextReader(CustomTxtTraceDefinition custReader) {
        fReaderName = "CustTxt-" + custReader.definitionName; //$NON-NLS-1$
        fCustReader = custReader;
    }

    @Override
    public ITraceTypeReader createInstance() {

        return this;
    }

    /**
     * Registers itself with the TraceTypeFactory
     *
     * @param customTextReader
     *            Custom text reader
     *
     * @throws TotalADSGeneralException
     *             Exception for invalid reader
     */
    public static void registerTraceTypeReader(CustomTmfTextReader customTextReader) throws TotalADSGeneralException {
        TraceTypeFactory trcTypFactory = TraceTypeFactory.getInstance();
        trcTypFactory.registerTraceReaderWithFactory(customTextReader.getName(), customTextReader);
    }

    @Override
    public String getName() {
        return fReaderName;
    }

    @Override
    public String getAcronym() {
        int endIdx = fReaderName.length();
        if (fReaderName.length() > 3) {
            endIdx = 3;
        }
        return "CT" + fReaderName.substring(0, endIdx); //$NON-NLS-1$
    }

    @Override
    public ITraceIterator getTraceIterator(File file) throws TotalADSReaderException {
        try {
            return new CustomTmfTextIterator(fCustReader, file.getPath());
        } catch (TmfTraceException e) {

            throw new TotalADSReaderException(e.getMessage() + "\n File: " + file.getName()); //$NON-NLS-1$
        }

    }

}
