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

package org.eclipse.tracecompass.totalads.ui.results;

import java.util.HashMap;

import org.eclipse.tracecompass.totalads.algorithms.Results;
import org.eclipse.tracecompass.totalads.ui.results.Messages;
import org.eclipse.swt.SWT;
//import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * This class creates GUI widgets for the results and feedback part of the GUI.
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class ResultsAndFeedback {
    private Tree fTreeTraceResults;
    private Text fTxtAnomalyType;
    private Text fTxtAnomalySummary;
    private Text fTxtAnalysisDetails;
    private Label fLblAnalysisCurrentAnomaly;
    private Group fGrpAnalysisResults;
    private Label fLblAnomalySummary;
    private Label fLblDetails;
    private Group fGrpResults;
    private Composite fCompTraceList;
    private Composite fCompSummary;
    private Label fLblTreeTraceResult;
    private Composite fCompResAndFeedback;
    private Label fLblTraceSummary;
    private Label fLblSelectModel;
    private Combo fCmbModels;
    private Text fTxtTraceSummary;
    private Integer fMaxAllowableTraces;
    private HashMap<String, Double> fModelAndAnomalyCount;
    private Display fDisplay;
    private Shell fDialogShel;
    private String fTraceToDelete;

    /**
     * Constructor
     *
     * @param parent
     *            Composite object
     * @param isDiagnosis
     *            false if model combo box is to be made visible
     */
    public ResultsAndFeedback(Composite parent, Boolean isDiagnosis) {
        detailsAndFeedBack(parent, isDiagnosis);
        fMaxAllowableTraces = 5000;
    }

    /**
     * Constructor to create results object as a separate dialog form
     */
    public ResultsAndFeedback() {
        fDisplay = Display.getDefault();
        fDialogShel = new Shell(fDisplay, SWT.BORDER | SWT.CLOSE | SWT.V_SCROLL);
        fDialogShel.setLayout(new GridLayout(4, false));

        detailsAndFeedBack(fDialogShel, false);
        fMaxAllowableTraces = 5000;
        // adding event to avoid disposing it off
        fDialogShel.addListener(SWT.Close, new Listener() {
            @Override
            public void handleEvent(Event event) {
                event.doit = false;
                fDialogShel.setVisible(false);
            }
        });
    }

    /**
     * Shows the modal form
     */
    public void showForm() {
        // open the form as a modal only if it has been created like this
        if (fDialogShel != null) {
            fDialogShel.open();
            while (!fDialogShel.isDisposed()) {
                if (!fDisplay.readAndDispatch()) {
                    fDisplay.sleep();
                }
            }

        }

    }// end function ShowForm

    /**
     * Close the shell dialog
     */
    public void destroy() {
        if (fDialogShel != null) {
            fDialogShel.dispose();
        }
    }

    /**
     * Creates widgets for details and results
     *
     * @param compParent
     *            Composite object
     * @param isDiagnosis
     *            It is false if it is from live monitor and true if it s from
     *            diagnosis
     */
    private void detailsAndFeedBack(Composite compParent, Boolean isDiagnosis) {
        // Group "Feedback: Is it anomaly?"
        fGrpResults = new Group(compParent, SWT.NONE);
        fGrpResults.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 4));
        // grpAnalysisIdentify.setText("Results and Feedback");
        fGrpResults.setText(Messages.ResultsAndFeedback_Results);
        fGrpResults.setLayout(new GridLayout(2, false));

        // ///////////////////////////////////
        // Widgets for summary
        // ////////////////////////////////////
        fCompSummary = new Composite(fGrpResults, SWT.None);
        fCompSummary.setLayout(new GridLayout(4, false));
        fCompSummary.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));

        fLblTraceSummary = new Label(fCompSummary, SWT.NONE);
        fLblTraceSummary.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 2, 1));
        fLblTraceSummary.setText(Messages.ResultsAndFeedback_TotTraces);

        fTxtTraceSummary = new Text(fCompSummary, SWT.BORDER);
        fTxtTraceSummary.setEditable(false);
        fTxtTraceSummary.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 2, 1));

        fLblSelectModel = new Label(fCompSummary, SWT.NONE);
        fLblSelectModel.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1));
        fLblSelectModel.setText(Messages.ResultsAndFeedback_SelModels);

        fCmbModels = new Combo(fCompSummary, SWT.BORDER | SWT.READ_ONLY);
        fCmbModels.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1));
        if (isDiagnosis == true) {
            fCmbModels.setVisible(false);
            fLblSelectModel.setVisible(false);
        }
        fLblAnomalySummary = new Label(fCompSummary, SWT.NONE);
        fLblAnomalySummary.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false, 1, 1));
        fLblAnomalySummary.setText(Messages.ResultsAndFeedback_TotAnomalies);

        fTxtAnomalySummary = new Text(fCompSummary, SWT.BORDER);
        fTxtAnomalySummary.setEditable(false);
        fTxtAnomalySummary.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1));

        // /////////////////////////////////
        // / Widgets for Trace list
        // ///////////////////////////////
        fCompTraceList = new Composite(fGrpResults, SWT.None);
        fCompTraceList.setLayout(new GridLayout(1, false));
        fCompTraceList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));

        fLblTreeTraceResult = new Label(fCompTraceList, SWT.NONE);
        fLblTreeTraceResult.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        fLblTreeTraceResult.setText(Messages.ResultsAndFeedback_TraceList);

        fTreeTraceResults = new Tree(fCompTraceList, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
        fTreeTraceResults.setLinesVisible(true);
        fTreeTraceResults.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 5));

        fCompResAndFeedback = new Composite(fGrpResults, SWT.NONE);
        fCompResAndFeedback.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        fCompResAndFeedback.setLayout(new GridLayout(1, false));

        results(fCompResAndFeedback);

        addHandlers();

        // *** End Trace list

        // *** Group Feedback: To be included in the next version.

    }

    /**
     * Creates widgets or GUI elements for the results
     *
     * @param compParent
     *            Composite object
     */
    private void results(Composite compParent) {
        /**
         *
         * Result
         *
         */
        fGrpAnalysisResults = new Group(compParent, SWT.NONE);
        GridData gridDataResult = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridDataResult.horizontalSpan = 1;
        fGrpAnalysisResults.setLayoutData(gridDataResult);
        fGrpAnalysisResults.setLayout(new GridLayout(2, false));

        fLblAnalysisCurrentAnomaly = new Label(fGrpAnalysisResults, SWT.NONE);
        fLblAnalysisCurrentAnomaly.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));// gridDataResultLabels);
        fLblAnalysisCurrentAnomaly.setText(Messages.ResultsAndFeedback_Anomaly);

        fTxtAnomalyType = new Text(fGrpAnalysisResults, SWT.BORDER);
        fTxtAnomalyType.setEditable(false);
        fTxtAnomalyType.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));// gridDataResultText);

        fLblDetails = new Label(fGrpAnalysisResults, SWT.NONE);
        fLblDetails.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 2, 1));
        fLblDetails.setText(Messages.ResultsAndFeedback_Details);

        fTxtAnalysisDetails = new Text(fGrpAnalysisResults, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
        fTxtAnalysisDetails.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 2));

        /**
         * End result group
         *
         */
    }

    /**
     * This function adds the handlers for the different events called on
     * widgets
     */
    private void addHandlers() {
        //
        // Event handler for the tree
        //
        fTreeTraceResults.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {

                String modelName = fCmbModels.getItem(fCmbModels.getSelectionIndex());
                if (modelName == null || modelName.isEmpty()) {
                    return;
                }

                TreeItem item = (TreeItem) e.item;
                @SuppressWarnings("unchecked")
                HashMap<String, Results> modelResults = (HashMap<String, Results>) item.getData();

                Results results = modelResults.get(modelName);
                if (results != null) {
                    // currentTraceResults=results;
                    if (results.getAnomaly() != null && results.getAnomaly() && (results.getAnomalyType() != null && !results.getAnomalyType().isEmpty())) {
                        fTxtAnomalyType.setText(results.getAnomalyType());
                    } else {
                        fTxtAnomalyType.setText(booleanAnomalyToString(results.getAnomaly()));
                    }

                    fTxtAnalysisDetails.setText(results.getDetails().toString());
                } else {
                    fTxtAnomalyType.setText(Messages.ResultsAndFeedback_Evaluating);
                    fTxtAnalysisDetails.setText(Messages.ResultsAndFeedback_Evaluating);
                }
            }
        });

        /**
         * Add Combo handlers
         */
        fCmbModels.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                String item = fCmbModels.getItem(fCmbModels.getSelectionIndex());
                if (item != null && !item.isEmpty()) {
                    if (fModelAndAnomalyCount != null) {
                        Double anomalies = fModelAndAnomalyCount.get(item);
                        if (anomalies != null) {
                            fTxtAnomalySummary.setText(anomalies.toString());
                            fTxtAnalysisDetails.setText(""); //$NON-NLS-1$
                            fTxtAnomalyType.setText(""); //$NON-NLS-1$

                        }
                    }
                }

            }
        });
    }

    /**
     * Add all model names
     *
     * @param modelNames
     *            Array of all the models whose results will appear in the
     *            results section
     */
    public void registerAllModelNames(final String[] modelNames) {
        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {
                for (int j = 0; j < modelNames.length; j++) {
                    fCmbModels.add(modelNames[j]);
                }
                fCmbModels.select(0);
            }

        });

    }

    /**
     * This function sets the maximum traces that are allowed to be displayed in
     * the results sections
     *
     * @param maxTraces
     *            Maximum traces
     */
    public void setMaxAllowableTrace(Integer maxTraces) {
        fMaxAllowableTraces = maxTraces;
    }

    /**
     * Assigns a trace and its results to appropriate widgets for viewing in
     * Results Section. If the number of traces increase beyond maximum
     * allowable traces then the first trace is removed and its name is
     * returned. First call registerAllModels function before calling this
     * function.
     *
     * @param traceName
     *            Name of the trace
     * @param modelResults
     *            Results of all the models as a HashMap
     * @return Name of the trace removed or empty if none is removed
     */
    public String addTraceResult(final String traceName, final HashMap<String, Results> modelResults) {

        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {

                fTraceToDelete = ""; //$NON-NLS-1$

                if (!traceName.isEmpty() && modelResults != null) {
                    // Check if the same name already exists
                    TreeItem[] allItems = fTreeTraceResults.getItems();
                    for (int j = 0; j < allItems.length; j++)
                    {
                        if (allItems[j].getText().equalsIgnoreCase(traceName))
                        {
                            return; // if it exists do not add it again
                        }
                    }

                    // Add if no such name is there
                    TreeItem item = new TreeItem(fTreeTraceResults, SWT.NONE);
                    item.setText(traceName);
                    item.setData(modelResults);

                    if (fTreeTraceResults.getItemCount() > fMaxAllowableTraces) {
                        fTraceToDelete = fTreeTraceResults.getItem(0).getText();
                        fTreeTraceResults.getItem(0).dispose();
                    }

                }
            }
        });

        return fTraceToDelete;
    }

    /**
     * Converts a boolean to displayable string
     *
     * @param anomaly
     * @return A String value to be displayed
     */
    private static String booleanAnomalyToString(Boolean anomaly) {
        if (anomaly == null) {
            return Messages.ResultsAndFeedback_AnomYes;
        } else if (anomaly) {
            return Messages.ResultsAndFeedback_AnomYes;
        } else {
            return Messages.ResultsAndFeedback_AnomNo;
        }
    }

    /**
     *
     * Clears the tree
     */
    public void clearData() {
        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {

                fTreeTraceResults.removeAll();
                fTxtAnomalyType.setText(""); //$NON-NLS-1$
                fTxtAnalysisDetails.setText(""); //$NON-NLS-1$
                fTxtAnomalySummary.setText(""); //$NON-NLS-1$
                fTxtTraceSummary.setText(""); //$NON-NLS-1$
                fCmbModels.removeAll();

            }
        });
    }

    /**
     * Sets the summary of results
     *
     * @param modelAnomCount
     *            Model and anomaly count
     */
    public void setTotalAnomalyCount(final HashMap<String, Double> modelAnomCount) {
        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {

                fModelAndAnomalyCount = modelAnomCount;
                if (fCmbModels.getItemCount() > 0) {
                    int index = fCmbModels.getSelectionIndex();
                    if (index == -1) {
                        index = 0;
                    }
                    Double anomCount = fModelAndAnomalyCount.get(fCmbModels.getItem(index));
                    if (anomCount != null) {
                        fTxtAnomalySummary.setText(anomCount.toString());
                    }

                }
            }
        });
    }

    /**
     * Sets the total trace count
     *
     * @param traceCount
     *            Total trace count
     */
    public void setTotalTraceCount(final String traceCount) {
        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {
                fTxtTraceSummary.setText(traceCount);

            }
        });

    }
}
