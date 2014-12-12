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


import org.eclipse.tracecompass.tmf.core.event.ITmfEvent;
import org.eclipse.tracecompass.tmf.core.exceptions.TmfTraceException;
import org.eclipse.tracecompass.tmf.core.trace.ITmfContext;
import org.eclipse.tracecompass.tmf.ctf.core.event.CtfTmfEvent;
import org.eclipse.tracecompass.tmf.ctf.core.trace.CtfTmfTrace;
import org.eclipse.tracecompass.totalads.readers.ITraceIterator;

/**
 * A trace iterator to read events in an LTTng trace
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
class CTFSystemCallIterator implements ITraceIterator {
    // private CtfIterator fTraceIterator = null;
    private Boolean fIsDispose = false;
    private String fSyscall;
    private CtfTmfTrace fTrace;
    private ITmfContext fCtxt;

    /**
     * Constructor to initialize the trace
     *
     * @param filePath
     *            file Name
     * @throws TmfTraceException
     *             An exception during trace reading
     */
    public CTFSystemCallIterator(String filePath) throws TmfTraceException {
        fTrace = new CtfTmfTrace();
        fTrace.initTrace(null, filePath, CtfTmfEvent.class);
        fCtxt = fTrace.seekEvent(0);
        // fTraceIterator = fTrace.createIterator();
    }

    /**
     * Moves Iterator to the next event, and returns true if the iterator can
     * advance or false if the iterator cannot advance
     **/
    @Override
    public boolean advance() {
        boolean isAdvance = true;
        fSyscall = ""; //$NON-NLS-1$
        ITmfEvent event;
        do {
            if ((event = fTrace.getNext(fCtxt)) != null) {
                fSyscall = handleSysEntryEvent(event);// returns the sys call
                                                      // (entry) event

            } else {
                isAdvance = false;
            }
        } while (fSyscall.isEmpty() && isAdvance == true);

        if (!isAdvance) {
            close();
        }

        return isAdvance;

    }

    /** Returns the event for the location of the iterator **/
    @Override
    public String getCurrentEvent() {
        return fSyscall;
    }

    /** Closes the iterator stream **/
    @Override
    public void close() {
        if (!fIsDispose) {
            fTrace.dispose();
        }
    }

    /**
     * Returns System Call
     *
     * @param event
     *            Event object of type CtfTmfEvent
     * @return Event as a String
     */
    private static String handleSysEntryEvent(ITmfEvent event) {

        String eventName = event.getType().getName();

        String systemCall = ""; //$NON-NLS-1$

        if (eventName.startsWith("syscall_entry_"))//$NON-NLS-1$
        // Reference: org.eclipse.tracecompass.internal.lttng2.kernel.core.trace.layout.
        //                        Lttng26EventLayout.getInstance().eventSyscallEntryPrefix()
        {
            systemCall = eventName.replaceAll("syscall_entry_", "sys_"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        else if (eventName.startsWith("compat_syscall_entry_"))//$NON-NLS-1$
        // Reference: Lttng26EventLayout.getInstance().eventCompatSyscallEntryPrefix()
        {
            systemCall = eventName.replaceAll("compat_syscall_entry_", "compat_sys_"); //$NON-NLS-1$ //$NON-NLS-2$
        } else if (eventName.startsWith("sys_") || eventName.startsWith("compat_sys_"))//$NON-NLS-1$ //$NON-NLS-2$
        // Reference: LttngEventLayout.getInstance().eventSyscallEntryPrefix()
        // Reference: LttngEventLayout.getInstance().eventCompatSyscallEntryPrefix()
        {
            systemCall = eventName.trim();
        }
        return systemCall;

    }

}
