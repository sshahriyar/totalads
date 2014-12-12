/*******************************************************************************
 * Copyright (c) 2014-2015 Software Behaviour Lab, Concordia University and Ericsson
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Alexandre Montplaisir - Initial API and implementation
 *
 *******************************************************************************/

package org.eclipse.tracecompass.totalads.traces;


import org.eclipse.tracecompass.ctf.core.trace.CTFReaderException;
import org.eclipse.tracecompass.ctf.core.trace.CTFTrace;

/**
 *
 * Here is the list of the available test traces for the CTF parser.
 *
 * Make sure you run the traces/get-traces.xml Ant script to download them
 * first! (This class is modified by Syed Shariyar Murtaza for TotalADS)
 *
 * @author Alexandre Montplaisir
 * @author Syed Shariyar Murtaza
 */
public enum TestTraces {



    /**
     * LTTng kernel trace
     *
     * <pre>
     * Trace Size: 14 MB
     * Tracer: lttng-modules 2.0.0
     * Event count: 595 641
     * Kernel version: 3.2.0-18-generic
     * Trace length: 11s
     * </pre>
     */

    LTTNG_TRACE_DIR("../org.eclipse.tracecompass.totalads.core.tests/sampletraces/trace2"),
    /**
     * Trace directory containing text traces
     */
    TXT_TRACE_DIR("../org.eclipse.tracecompass.totalads.core.tests/sampletraces/txt"),
    /**
     * Main folder containing all the traces
     */
    MAIN_TRACE_DIR("../org.eclipse.tracecompass.totalads.core.tests/sampletraces/");

    private final String fPath;
    private CTFTrace fTrace = null;


    private TestTraces(String path) {
        fPath = path;
    }

    /** @return The path to the test trace */
    public String getPath() {
        return fPath;
    }

    /**
     * Get a CTFTrace instance of a test trace. Make sure
     * {@link #exists()} before calling this!
     *
     * @return The CTFTrace object
     * @throws CTFReaderException
     *             If the trace cannot be found.
     */
    public CTFTrace getTrace() throws CTFReaderException {
        if (fTrace == null) {
            fTrace = new CTFTrace(fPath);
        }
        return fTrace;
    }

    /**
     * Check if this test trace actually exists on disk.
     *
     * @return If the trace exists
     */
    public boolean exists() {
        try {
            getTrace();
        } catch (CTFReaderException e) {
            return false;
        }
        return true;
    }
}
