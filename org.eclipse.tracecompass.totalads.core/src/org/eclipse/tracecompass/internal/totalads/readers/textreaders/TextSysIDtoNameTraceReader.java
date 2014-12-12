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
package org.eclipse.tracecompass.internal.totalads.readers.textreaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.tracecompass.totalads.exceptions.TotalADSGeneralException;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSReaderException;
import org.eclipse.tracecompass.totalads.readers.ITraceIterator;
import org.eclipse.tracecompass.totalads.readers.ITraceTypeReader;
import org.eclipse.tracecompass.totalads.readers.TraceTypeFactory;

/**
 * This class is only used for lab experiments and it is used to map ids to
 * names.
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class TextSysIDtoNameTraceReader implements ITraceTypeReader {
    // -----------------------------------------------------------------
    // Inner class: Implements an iterator
    // -------------------------------------------------------------
    private class TextLineIterator implements ITraceIterator {
        private BufferedReader bufferedReader;
        private String event = ""; //$NON-NLS-1$
        private Boolean isClose = false;

        public TextLineIterator(BufferedReader bReader) {
            bufferedReader = bReader;
        }

        /**
         * Advances the iterator
         */
        @Override
        public boolean advance() throws TotalADSReaderException {
            boolean isAdvance = false;
            try {
                String syscall = ""; //$NON-NLS-1$
                do {
                    event = bufferedReader.readLine();
                    if (event == null) {
                        bufferedReader.close();
                        isClose = true;
                        isAdvance = false;
                    }
                    else {
                        isAdvance = true;
                        int id;
                        try {
                            id = Integer.parseInt(event);
                            syscall = MapSysCallIDToName.getSysCallName(id);
                            event = syscall;
                        } catch (NumberFormatException ex) {
                            syscall = null;
                        }

                    }
                } while (syscall == null);

            } catch (IOException e) {

                throw new TotalADSReaderException(e.getMessage());
            }
            return isAdvance;
        }

        /**
         * Gets current event
         */
        @Override
        public String getCurrentEvent() {

            return event;
        }

        /**
         * Closes the iterator
         */
        @Override
        public void close() throws TotalADSReaderException {
            try {
                if (!isClose) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                throw new TotalADSReaderException(e.getMessage());
            }

        }

    }

    // -------------------------------------------------------------------------------
    // Inner class ends
    // -------------------------------------------------------------------------------
    /**
     * Constructor
     */
    public TextSysIDtoNameTraceReader() {

    }

    /**
     * Creates an instance by implementing the ITraceTypeReader interface
     */
    @Override
    public ITraceTypeReader createInstance() {
        return new TextSysIDtoNameTraceReader();
    }

    /**
     * Returns the name
     */
    @Override
    public String getName() {
        // This is for lab experiments only
        return "Text-Syscall VIEW_ID to Name (lab)"; //$NON-NLS-1$
    }

    /**
     * Returns the acronym of the text reader
     */
    @Override
    public String getAcronym() {

        return "TXT"; //$NON-NLS-1$
    }

    /**
     * Returns the trace iterator
     */
    @Override
    public ITraceIterator getTraceIterator(File file) throws TotalADSReaderException {

        BufferedReader bufferReader;
        try {
            bufferReader = new BufferedReader(new FileReader(file));
            TextLineIterator textLineIterator = new TextLineIterator(bufferReader);
            return textLineIterator;
        } catch (FileNotFoundException e) {
            throw new TotalADSReaderException(e.getMessage());
        }
    }

    /**
     * Static function to register a trace type reader with the factory
     *
     * @throws TotalADSGeneralException
     *             A validation exception
     */
    public static void registerTraceTypeReader() throws TotalADSGeneralException {
        TraceTypeFactory trcTypFactory = TraceTypeFactory.getInstance();
        TextSysIDtoNameTraceReader textFileReader = new TextSysIDtoNameTraceReader();
        trcTypFactory.registerTraceReaderWithFactory(textFileReader.getName(), textFileReader);
    }
}
