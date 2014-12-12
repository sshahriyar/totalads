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
import org.eclipse.tracecompass.tmf.core.parsers.custom.CustomTxtTrace;
import org.eclipse.tracecompass.tmf.core.parsers.custom.CustomTxtTraceDefinition;
import org.eclipse.tracecompass.tmf.core.trace.ITmfContext;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSReaderException;
import org.eclipse.tracecompass.totalads.readers.ITraceIterator;

/**
 * This class provides an iterator for a trace based on the custom TMF text parser
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class CustomTmfTextIterator implements ITraceIterator {

    private CustomTxtTrace fTxtTraceParser;
    private ITmfContext fCtx;
    private ITmfEvent fEvent;
    private String fTrainingField;

    /**
     * @param custTraceDef
     *            The context definition of the trace
     * @param path
     *            Path
     * @throws TmfTraceException
     *             Trace exception thrown by TMF
     */
    public CustomTmfTextIterator(CustomTxtTraceDefinition custTraceDef, String path) throws TmfTraceException {
        fTxtTraceParser = new CustomTxtTrace(custTraceDef);
        fTxtTraceParser.initTrace(null, path, CustomTxtEvent.class);
        fCtx = fTxtTraceParser.seekEvent(0);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.tracecompass.totalads.readers.ITraceIterator#advance()
     */
    @Override
    public boolean advance() throws TotalADSReaderException {
        if ((fEvent = fTxtTraceParser.getNext(fCtx)) != null) {

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
                    throw new TotalADSReaderException(Messages.CustomTmfTextIterator_NoTrainingField);
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
        fTxtTraceParser.dispose();

    }

}
