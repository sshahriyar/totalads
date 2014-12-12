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

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.tracecompass.internal.totalads.readers.tmfreaders.Messages;
import org.eclipse.tracecompass.tmf.core.event.ITmfEvent;
import org.eclipse.tracecompass.tmf.core.exceptions.TmfTraceException;
import org.eclipse.tracecompass.tmf.core.parsers.custom.CustomTxtEvent;
import org.eclipse.tracecompass.tmf.core.parsers.custom.CustomXmlTrace;
import org.eclipse.tracecompass.tmf.core.parsers.custom.CustomXmlTraceDefinition;
import org.eclipse.tracecompass.tmf.core.trace.ITmfContext;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSReaderException;
import org.eclipse.tracecompass.totalads.readers.ITraceIterator;

/**
 * This class provides an iterator for a trace based on the custom TMF-XML
 * parser
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class CustomTmfXmlIterator implements ITraceIterator {

    private CustomXmlTrace fXmlTraceParser;
    private ITmfContext fCtx;
    private ITmfEvent fEvent;
    private String fTrainingField;

    /**
     * @param custTraceDef
     *            Trace definition
     * @param path
     *            Path to the trace
     * @throws TmfTraceException
     *             Trace exception
     */
    public CustomTmfXmlIterator(CustomXmlTraceDefinition custTraceDef, String path) throws TmfTraceException {
        fXmlTraceParser = new CustomXmlTrace(custTraceDef);

        fXmlTraceParser.initTrace(null, path, CustomTxtEvent.class);
        fCtx = fXmlTraceParser.seekEvent(0);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.tracecompass.totalads.readers.ITraceIterator#advance()
     */
    @Override
    public boolean advance() throws TotalADSReaderException {
        if ((fEvent = fXmlTraceParser.getNext(fCtx)) != null) {

            // Check only once for the training field
            if (fTrainingField == null) {
                Collection<String> fieldName = fEvent.getContent().getFieldNames();
                Iterator<String> it = fieldName.iterator();
                while (it.hasNext()) {
                    String field = it.next();
                    if (field.contains("*")) { //$NON-NLS-1$
                        fTrainingField = field;
                        return true;
                    }
                }
                // if training field is still null then there is no training
                // field return false
                if (fTrainingField == null) {
                    throw new TotalADSReaderException(Messages.CustomTmfXmlIterator_NoTrainingField);
                }
            }// End of check for training field

            return true;
        }
        return false;
    }

    @Override
    public String getCurrentEvent() {

        // fEvent.getContent().getField(fTrainingField).getName();
        return (String) fEvent.getContent().getField(fTrainingField).getValue();

    }

    @Override
    public void close() throws TotalADSReaderException {
        fXmlTraceParser.dispose();

    }

}
