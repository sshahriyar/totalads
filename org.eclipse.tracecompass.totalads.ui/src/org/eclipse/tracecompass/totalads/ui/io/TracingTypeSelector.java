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

package org.eclipse.tracecompass.totalads.ui.io;

import org.eclipse.tracecompass.totalads.exceptions.TotalADSGeneralException;
import org.eclipse.tracecompass.totalads.readers.ITraceTypeReader;
import org.eclipse.tracecompass.totalads.readers.TraceTypeFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.layout.GridData;
//import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

/**
 * Creates a combo box and populates it with all the trace types registered with
 * the {@link TraceTypeFactory}
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class TracingTypeSelector {
    private Combo fCmbTraceTypes;
    private TraceTypeFactory fTraceFac;

    /**
     * Constructor
     *
     * @param compParent
     *            Composite
     * @param gridDataComboBox
     *            Style of combo box
     */
    public TracingTypeSelector(Composite compParent, GridData gridDataComboBox) {
        /*
         * Trace Type Selection
         */
        fTraceFac = TraceTypeFactory.getInstance();
        fCmbTraceTypes = new Combo(compParent, SWT.READ_ONLY);
        fCmbTraceTypes.setLayoutData(gridDataComboBox);

        // Unfortunately, this is a workaround to know if we have
        // all the custom parsers loaded or not. Custom parsers in TMF do
        // not provide any event at the time of creation of new parsers
        fCmbTraceTypes.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                try {
                    TraceTypeFactory.destroyInstance();
                    fTraceFac = TraceTypeFactory.getInstance();
                    fTraceFac.initialize();
                    fCmbTraceTypes.removeAll();
                    populateCombo(fCmbTraceTypes);

                } catch (TotalADSGeneralException e1) {

                    e1.printStackTrace();
                }

            }
        });

        populateCombo(fCmbTraceTypes);

    }

    /**
     * Populates the combo box with the trace readers
     *
     * @param combTraceTypes
     *            Combo box which need to be populated
     */
    private void populateCombo(Combo combTraceTypes) {

        // populating anomaly detection models
        String[] traceReaders = fTraceFac.getAllTraceTypeReaderKeys();

        if (traceReaders != null) {
            for (int i = 0; i < traceReaders.length; i++) {

                combTraceTypes.add(traceReaders[i]);
            }
        }

        combTraceTypes.select(0);
    }

    /**
     * Returns the ITraceTypeReader for the selected trace type
     *
     * @return A trace reader
     */
    public ITraceTypeReader getSelectedType() {

        String key = fCmbTraceTypes.getItem(fCmbTraceTypes.getSelectionIndex());
        return fTraceFac.getTraceReader(key);
    }

    /**
     * Selects a trace type reader in the combo box
     *
     * @param traceTypeName
     *            Type of the trace
     */
    public void selectTraceType(String traceTypeName) {
        for (int i = 0; i < fCmbTraceTypes.getItemCount(); i++) {
            if (fCmbTraceTypes.getItem(i).equalsIgnoreCase(traceTypeName)) {
                fCmbTraceTypes.select(i);
                break;
            }
        }
    }

}
