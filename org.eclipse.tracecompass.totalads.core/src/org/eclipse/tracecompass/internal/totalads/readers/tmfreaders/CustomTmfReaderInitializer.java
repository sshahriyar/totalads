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

import org.eclipse.tracecompass.internal.totalads.readers.tmfreaders.CustomTmfTextReader;
import org.eclipse.tracecompass.internal.totalads.readers.tmfreaders.CustomTmfXmlReader;
import org.eclipse.tracecompass.tmf.core.parsers.custom.CustomTxtTraceDefinition;
import org.eclipse.tracecompass.tmf.core.parsers.custom.CustomXmlTraceDefinition;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSGeneralException;
import org.eclipse.tracecompass.totalads.readers.TraceTypeFactory;

/**
 * This class loads all the custom text and XML readers created by users into
 * TotalADS.
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class CustomTmfReaderInitializer {
    /**
     * This function loads all the custom text readers created in TMF and
     * registers them with the {@link TraceTypeFactory}
     *
     * @throws TotalADSGeneralException
     *             An exception for invalid readers
     */
    public static void registerAllCustomTmfTextTReaders() throws TotalADSGeneralException {

        CustomTxtTraceDefinition[] cust = CustomTxtTraceDefinition.loadAll();

        if (cust != null) {

            for (int j = 0; j < cust.length; j++) {

                CustomTmfTextReader tmfTextReaders = new CustomTmfTextReader(cust[j]);
                CustomTmfTextReader.registerTraceTypeReader(tmfTextReaders);
            }

        }

    }

    /**
     * This function loads all the custom XML readers created in TMF and
     * registers them with the {@link TraceTypeFactory}
     *
     * @throws TotalADSGeneralException
     *             An exception for invalid readers
     */
    public static void registerAllCustomTmfXmlReaders() throws TotalADSGeneralException {
        CustomXmlTraceDefinition[] cust = CustomXmlTraceDefinition.loadAll();

        if (cust != null) {

            for (int j = 0; j < cust.length; j++) {

                CustomTmfXmlReader tmfXmlReaders = new CustomTmfXmlReader(cust[j]);
                CustomTmfXmlReader.registerTraceTypeReader(tmfXmlReaders);
            }

        }

    }

}
