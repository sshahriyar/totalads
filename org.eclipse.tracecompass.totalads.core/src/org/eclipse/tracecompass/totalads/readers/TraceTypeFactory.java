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

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.tracecompass.internal.totalads.readers.ctfreaders.CTFLTTngSysCallTraceReader;
import org.eclipse.tracecompass.internal.totalads.readers.textreaders.TextLineTraceReader;
import org.eclipse.tracecompass.internal.totalads.readers.tmfreaders.CustomTmfReaderInitializer;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSGeneralException;
import org.eclipse.tracecompass.totalads.readers.ITraceTypeReader;
import org.eclipse.tracecompass.totalads.readers.Messages;
import org.eclipse.tracecompass.totalads.readers.TraceTypeFactory;

/**
 * This class registers all the trace readers with itself by following a factory
 * pattern. It is a singleton and whenever an instance of any type of trace
 * reader is needed, this class should be called.
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class TraceTypeFactory {

    // Data variables
    private static TraceTypeFactory traceTypes = null;
    private static LinkedHashMap<String, ITraceTypeReader> traceTypeReadersList = null;
    private static Boolean init =false;
    /**
     * Constructor
     */
    private TraceTypeFactory() {
        traceTypeReadersList = new LinkedHashMap<>();
    }

    /**
     * Creates the instance of TraceTypeFactory
     *
     * @return TraceTypeFactory
     */
    public static TraceTypeFactory getInstance() {
        if (traceTypes == null) {
            traceTypes = new TraceTypeFactory();
        }
        return traceTypes;
    }

    /**
     * Destroys the instance of factory if already exists This code is necessary
     * because when Eclipse is running and TotalADS window is closed and
     * reopened, the static object is not recreated on the creation of a new
     * object of TotalADS
     */
    public static void destroyInstance() {
        if (traceTypes != null) {
            init =false;
            traceTypes = null;
            traceTypeReadersList=null;
        }
    }

    /**
     * Returns a trace type reader based on the name
     *
     * @param key
     *            The name of the reader
     * @return A reader of a selected type
     */
    public ITraceTypeReader getTraceReader(String key) {
        ITraceTypeReader reader = traceTypeReadersList.get(key);
        return reader.createInstance();

    }

    /**
     * Get all trace type readers
     *
     * @return An array of Trace type readers
     */
    public ITraceTypeReader[] getAllTraceReaders() {
        ITraceTypeReader[] traceReaders = new ITraceTypeReader[traceTypeReadersList.size()];
        int indx = 0;

        for (Map.Entry<String, ITraceTypeReader> list : traceTypeReadersList.entrySet()) {
            traceReaders[indx] = list.getValue();
            indx++;
        }
        return traceReaders;
    }

    /**
     * Get all trace type reader keys
     *
     * @return All the keys of trace type readers
     */
    public String[] getAllTraceTypeReaderKeys() {
        String[] keys = new String[traceTypeReadersList.size()];
        keys = traceTypeReadersList.keySet().toArray(keys);
        return keys;

    }

    /**
     * Registers a trace reader with the factory
     *
     * @param key
     *            Key/Name for the trace reader
     * @param traceReader
     *            Instance of the trace reader
     * @throws TotalADSGeneralException
     *             Exception
     */
    public void registerTraceReaderWithFactory(String key, ITraceTypeReader traceReader) throws TotalADSGeneralException {
    	if (key==null || traceReader==null) {
            throw new TotalADSGeneralException(Messages.TraceTypeFactory_NoNull);
        }


    	if (!key.isEmpty()) {
            ITraceTypeReader reader = traceTypeReadersList.get(key);
            if (reader == null) {
                traceTypeReadersList.put(key, traceReader);
            } else {
                throw new TotalADSGeneralException(Messages.TraceTypeFactory_DuplicateKey);
            }
        } else {
            throw new TotalADSGeneralException(Messages.TraceTypeFactory_EmptyKey);
        }

    }

    /**
     * Gets the Kernel or Text reader. Written to be used in the
     * initialization of a trace in the startup of the environment or during testing.
     * CustomTMFReaders do no initialize during testing and throw errors in the initialize
     * function
     *
     * @param isKernel
     *            True to get the LTTng kernel reader and false to get the text reader
     * @return The reader
     */
    public ITraceTypeReader getCTFKernelReaderOrSimpleTextReader(Boolean isKernel) {
        if (isKernel) {
            return new CTFLTTngSysCallTraceReader();
        }
        return new TextLineTraceReader();



    }

    /**
     * This function initializes the Trace readers. Currently Trace readers are
     * manually registered in this function but in future reflection will be
     * used to automatically register them
     *
     * @throws TotalADSGeneralException
     *             An exception for invalid readers
     */
    public void initialize() throws TotalADSGeneralException {

        // Reflections reflections = new
        // Reflections("org.eclipse.tracecompass.totalads.ui");
        // java.util.Set<Class<? extends IDetectionAlgorithm>> modules =
        // reflections.getSubTypesOf
        // (org.eclipse.tracecompass.totalads.ui.IDetectionModels.class);
        // The following code needs to be replaced with reflection in future
        // versions
       if (init ==false){
            CTFLTTngSysCallTraceReader.registerTraceTypeReader();
            TextLineTraceReader.registerTraceTypeReader();
            //TextSysIDtoNameTraceReader.registerTraceTypeReader();
            CustomTmfReaderInitializer.registerAllCustomTmfTextTReaders();
            CustomTmfReaderInitializer.registerAllCustomTmfXmlReaders();
            init =true;
       }
    }

}
