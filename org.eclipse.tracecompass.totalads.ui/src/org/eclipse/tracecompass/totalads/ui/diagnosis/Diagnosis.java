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
package org.eclipse.tracecompass.totalads.ui.diagnosis;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.tracecompass.totalads.readers.ITraceTypeReader;
import org.eclipse.tracecompass.totalads.readers.TraceTypeFactory;
import org.eclipse.tracecompass.totalads.ui.diagnosis.BackgroundTesting;
import org.eclipse.tracecompass.totalads.ui.diagnosis.DiagnosisView;
import org.eclipse.tracecompass.totalads.ui.diagnosis.Messages;
import org.eclipse.tracecompass.totalads.ui.io.DirectoryBrowser;
import org.eclipse.tracecompass.totalads.ui.io.TracingTypeSelector;
import org.eclipse.tracecompass.totalads.ui.results.ResultsAndFeedback;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

/**
 * This class creates the GUI elements/widgets for the diagnosis view. It
 * creates a diagnosis tab and then creates other GUI widgets by instantiating
 * SWT widgets.
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class Diagnosis {
    // Initializes variables
    private TracingTypeSelector fTraceTypeSelector;
    private Text fTxtTMFTraceID;
    private Text fTxtTestTraceDir;
    private DirectoryBrowser fTraceBrowser;
    private StringBuilder fTmfTracePath;
    private StringBuilder fCurrentlySelectedTracesPath;
    private ResultsAndFeedback fResultsAndFeedback;
    private Button fBtnSelTestTraces;
    private Button fBtnSelTMFTrace;
    private HashSet<String> fModelsList;
    private Button fBtnEvaluateModels;
    private Button fBtnStop;
    private ExecutorService fExecutor;

    /**
     * Constructor
     */
    public Diagnosis() {

        fModelsList = new HashSet<>();

    }

    /**
     * Creates GUI widgets
     *
     * @param compParent
     *            Composite
     */
    public void createControl(Composite compParent) {
        fTmfTracePath = new StringBuilder();
        fCurrentlySelectedTracesPath = new StringBuilder();

        // Making scrollable composite

        ScrolledComposite scrolCompDiag = new ScrolledComposite(compParent, SWT.H_SCROLL | SWT.V_SCROLL);
        Composite compDiagnosis = new Composite(scrolCompDiag, SWT.NONE);

        // tbItmDiagnosis.setControl(scrolCompAnom);

        // Designing the Layout of the GUI Items for the LiveMonitor Tab Item
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridData.horizontalSpan = 1;
        compDiagnosis.setLayoutData(gridData);
        compDiagnosis.setLayout(new GridLayout(1, false));

        // /////////////////////////////////////////////////////////////////////////
        // Creating GUI widgets for selection of a trace type and a selection of
        // the model
        // /////////////////////////////////////////////////////////////////

        selectTraceTypeAndTraces(compDiagnosis);
        addEvaluateButton(compDiagnosis);
        // Create GUI elements for a selection of a trace

        // Adjust settings for scrollable tab Item
        scrolCompDiag.setContent(compDiagnosis);
        // Set the minimum size
        scrolCompDiag.setMinSize(200, 200);
        // Expand both horizontally and vertically
        scrolCompDiag.setExpandHorizontal(true);
        scrolCompDiag.setExpandVertical(true);
    }

    /**
     * Creates GUI widgets for a selection of traces and trace types
     *
     * @param compDiagnosis
     *            Composite of LiveMonitor
     */
    private void selectTraceTypeAndTraces(Composite compDiagnosis) {
        /**
         * Group trace selection
         */
        Group grpTraceSelection = new Group(compDiagnosis, SWT.NONE);
        grpTraceSelection.setText(Messages.Diagnosis_SelTraces);

        grpTraceSelection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        grpTraceSelection.setLayout(new GridLayout(4, false));

        // Creating widgets for the selection of a trace type
        Composite compTraceType = new Composite(grpTraceSelection, SWT.NONE);
        compTraceType.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 4, 1));
        compTraceType.setLayout(new GridLayout(2, false));

        Label lblTraceType = new Label(compTraceType, SWT.NONE);
        lblTraceType.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1));
        lblTraceType.setText(Messages.Diagnosis_SelTraceType);

        fTraceTypeSelector = new TracingTypeSelector(compTraceType, new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));


        fBtnSelTestTraces = new Button(grpTraceSelection, SWT.RADIO);
        fBtnSelTestTraces.setText(Messages.Diagnosis_SelTestFolder);
        fBtnSelTestTraces.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 2, 1));
        fBtnSelTestTraces.setSelection(true);

        fBtnSelTMFTrace = new Button(grpTraceSelection, SWT.RADIO);
        fBtnSelTMFTrace.setText(Messages.Diagnosis_SelectTMFTrace);
        fBtnSelTMFTrace.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 2, 1));

        fTxtTestTraceDir = new Text(grpTraceSelection, SWT.BORDER);
        fTxtTestTraceDir.setEnabled(true);
        fTxtTestTraceDir.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

        fTraceBrowser = new DirectoryBrowser(grpTraceSelection, fTxtTestTraceDir, new GridData(SWT.RIGHT, SWT.TOP, false, false));

        fTxtTMFTraceID = new Text(grpTraceSelection, SWT.BORDER);
        fTxtTMFTraceID.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
        fTxtTMFTraceID.setEditable(false);
        fTxtTMFTraceID.setEnabled(false);

        // Adding an event handler for the radio button fBtnSelTestTraces
        fBtnSelTestTraces.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {

                fTxtTestTraceDir.setEnabled(true);
                fTxtTMFTraceID.setEnabled(false);
                fTraceBrowser.enableBrowsing();
                fTxtTMFTraceID.setText(""); //$NON-NLS-1$
                fTxtTestTraceDir.setText(""); //$NON-NLS-1$
                fCurrentlySelectedTracesPath.delete(0, fCurrentlySelectedTracesPath.length());// first
                                                                                            // clearing
                                                                                            // current
                                                                                            // path

            }
        });

        // Adding an event handler for the radio button fBtnSelTMFTrace
        fBtnSelTMFTrace.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {

                fTxtTestTraceDir.setEnabled(false);
                fTxtTMFTraceID.setEnabled(true);
                fTraceBrowser.disableBrowsing();

                setTMFTraceToCurrentTracePath();

            }

        });

        //
        // Adding an event handler for the text box of fTxtTestTraceDir to update
        // the path
        fTxtTestTraceDir.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {

                fCurrentlySelectedTracesPath.delete(0, fCurrentlySelectedTracesPath.length());// first
                fCurrentlySelectedTracesPath.append(fTxtTestTraceDir.getText());
            }
        });
        /**
         * End group trace selection
         */
    }

    /**
     * Adds the evaluation button
     *
     * @param compParent
     *            Composite
     */
    private void addEvaluateButton(Composite compParent) {
        // Creating widgets for the selection of a trace type
        Composite compButtons = new Composite(compParent, SWT.NONE);
        compButtons.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 4, 1));
        compButtons.setLayout(new GridLayout(2, false));

        fBtnEvaluateModels = new Button(compButtons, SWT.NONE);
        fBtnEvaluateModels.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false));
        fBtnEvaluateModels.setText(Messages.Diagnosis_StartEval);

        fBtnStop = new Button(compButtons, SWT.NONE);
        fBtnStop.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
        fBtnStop.setText(Messages.Diagnosis_StopEval);
        fBtnStop.setEnabled(false);
        /**
         * Event handler for the evaluate button
         */
        fBtnEvaluateModels.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseUp(MouseEvent e) {
                String msgTitle="TotalADS"; //$NON-NLS-1$
                if (fModelsList.size() <= 0) {
                    MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                            msgTitle,Messages.Diagnosis_SelModel);
                     return;
                }
                if (fCurrentlySelectedTracesPath.length() <= 0) {
                    MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                            msgTitle,Messages.Diagnosis_SelTrace);
                    return;
                }

                Iterator<String> it = fModelsList.iterator();

                ITraceTypeReader traceReader = fTraceTypeSelector.getSelectedType();
                String[] modelNames = new String[fModelsList.size()];

                int counter = 0;
                while (it.hasNext()) {
                    modelNames[counter] = it.next();
                    counter++;
                }
                fResultsAndFeedback.clearData();

                fBtnEvaluateModels.setEnabled(false);
                fBtnStop.setEnabled(true);

                BackgroundTesting testTheModel = new BackgroundTesting(fCurrentlySelectedTracesPath.toString(), traceReader,
                        modelNames, fBtnEvaluateModels, fBtnStop, fResultsAndFeedback);
                fExecutor = Executors.newSingleThreadExecutor();
                fExecutor.execute(testTheModel);

            }
        });

        //
        // Event handler for the stop button
        //
        fBtnStop.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseUp(MouseEvent e) {
                fBtnStop.setEnabled(false);
                fExecutor.shutdownNow();
            }
        });

    }

    /**
     * This function gets updated when a user selects a trace in TMF Views
     *
     * @param traceLocation
     *            Trace location
     * @param traceTypeName
     *            Trace type
     */
    public void updateOnTraceSelection(String traceLocation, String traceTypeName) {

        fTmfTracePath.delete(0, fTmfTracePath.length());
        fTmfTracePath.append(traceLocation);

        if (fBtnSelTMFTrace.getSelection()) {
            setTMFTraceToCurrentTracePath();
        }

    }

    /**
     * Sets the ResultsAndFeddback object to a local variable
     *
     * @param resultsAndFeedback
     *            An object to display results
     */
    public void setResultsAndFeedbackInstance(ResultsAndFeedback resultsAndFeedback) {

        this.fResultsAndFeedback = resultsAndFeedback;
    }

    /**
     * This function gets called from {@link DiagnosisView} to get updated for
     * the currently selected models
     *
     * @param listOfModels
     *            List of models
     */
    public void updateonModelSelection(HashSet<String> listOfModels) {
        this.fModelsList = listOfModels;
    }

    /**
     * Sets the current trace path to the path of a TMF trace and also adjusts
     * the text boxes accordingly
     */
    private void setTMFTraceToCurrentTracePath() {
        fTxtTestTraceDir.setText(""); //$NON-NLS-1$

        File file = new File(fTmfTracePath.toString());
        String traceName = file.getName();

        fTxtTMFTraceID.setText(""); //$NON-NLS-1$

        // clear it and copy the path, don't make a copy of the reference of the
        // object because it is a different object
        fCurrentlySelectedTracesPath.delete(0, fCurrentlySelectedTracesPath.length());

        if (!traceName.isEmpty()) {
            fTxtTMFTraceID.setText(NLS.bind(Messages.Diagnosis_TraceInTMF, traceName));
            fCurrentlySelectedTracesPath.append(fTmfTracePath.toString());
            // Make sure that only LTTng system call trace reader is selected
            // for TMF traces
            ITraceTypeReader lttngReader = TraceTypeFactory.getInstance().getCTFKernelReaderOrSimpleTextReader(true);

            fTraceTypeSelector.selectTraceType(lttngReader.getName());
        }

    }
}
