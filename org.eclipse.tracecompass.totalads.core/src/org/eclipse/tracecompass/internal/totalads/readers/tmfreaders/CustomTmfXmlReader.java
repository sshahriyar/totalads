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

import org.eclipse.tracecompass.internal.totalads.readers.tmfreaders.CustomTmfXmlIterator;
import org.eclipse.tracecompass.internal.totalads.readers.tmfreaders.CustomTmfXmlReader;
import org.eclipse.tracecompass.tmf.core.exceptions.TmfTraceException;
import org.eclipse.tracecompass.tmf.core.parsers.custom.CustomXmlTraceDefinition;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSGeneralException;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSReaderException;
import org.eclipse.tracecompass.totalads.readers.ITraceIterator;
import org.eclipse.tracecompass.totalads.readers.ITraceTypeReader;
import org.eclipse.tracecompass.totalads.readers.TraceTypeFactory;

/**
 * This class provides a trace reader based on the Custom TMF-XML parser
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class CustomTmfXmlReader implements ITraceTypeReader {
    private String fReaderName;
    private CustomXmlTraceDefinition fCustReader;

    /**
     * @param custReader
     *            Trace definition
     */
    public CustomTmfXmlReader(CustomXmlTraceDefinition custReader) {
        fReaderName = "CustXML-" + custReader.definitionName; //$NON-NLS-1$
        fCustReader = custReader;
    }

    @Override
    public ITraceTypeReader createInstance() {

        return this;
    }

    /**
     * Registers itself with the TraceTypeFactory
     *
     * @param customXmlReader
     *            Custom XML reader
     *
     * @throws TotalADSGeneralException
     *             Exception for invalid reader
     */
    public static void registerTraceTypeReader(CustomTmfXmlReader customXmlReader) throws TotalADSGeneralException {
        TraceTypeFactory trcTypFactory = TraceTypeFactory.getInstance();
        trcTypFactory.registerTraceReaderWithFactory(customXmlReader.getName(), customXmlReader);
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
        return "CX" + fReaderName.substring(0, endIdx); //$NON-NLS-1$
    }

    @Override
    public ITraceIterator getTraceIterator(File file) throws TotalADSReaderException {
        try {
            return new CustomTmfXmlIterator(fCustReader, file.getPath());
        } catch (TmfTraceException e) {

            throw new TotalADSReaderException(e.getMessage() + "\n File: " + file.getName()); //$NON-NLS-1$
        }

    }

}
