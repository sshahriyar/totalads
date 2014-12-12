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

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.tracecompass.tmf.core.signal.TmfSignalHandler;
import org.eclipse.tracecompass.tmf.core.signal.TmfTraceSelectedSignal;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.eclipse.tracecompass.totalads.readers.ITraceTypeReader;
import org.eclipse.tracecompass.totalads.readers.TraceTypeFactory;
import org.eclipse.tracecompass.totalads.ui.diagnosis.Diagnosis;
import org.eclipse.tracecompass.totalads.ui.diagnosis.DiagnosisPartListener;
import org.eclipse.tracecompass.totalads.ui.diagnosis.DiagnosisView;
import org.eclipse.tracecompass.totalads.ui.diagnosis.IDiagnosisObserver;
import org.eclipse.tracecompass.totalads.ui.diagnosis.Messages;
import org.eclipse.tracecompass.totalads.ui.models.DataModelsView;
import org.eclipse.tracecompass.totalads.ui.results.ResultsView;
import org.eclipse.tracecompass.totalads.ui.results.ResultsAndFeedback;
import org.eclipse.tracecompass.tmf.ui.views.TmfView;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * This class creates the diagnosis view
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class DiagnosisView extends TmfView implements IDiagnosisObserver, ISelectionListener {

    /**
     * View id
     */
    public static final String VIEW_ID = "org.eclipse.tracecompass.totalads.ui.diagnosis.DiagnosisView"; //$NON-NLS-1$

    private Diagnosis fDiagnosis;
    private DiagnosisPartListener fPartListener;

    /**
     * Constructor
     */
    public DiagnosisView() {
        super(VIEW_ID);
        fDiagnosis = new Diagnosis();
        fPartListener = new DiagnosisPartListener();
        fPartListener.addObserver(this);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
     * .Composite)
     */
    @Override
    public void createPartControl(Composite compParent) {

        fDiagnosis.createControl(compParent);

        ITmfTrace trace = getActiveTrace();
        if (trace != null) {
            traceSelected(new TmfTraceSelectedSignal(this, trace));
        }

        try {

            // Trying to clear the already selected instances in the models view
            // when this view is opened in the middle of execution
            // If the view is opened in the middle, already selected models are
            // not available using the event handler
            IViewPart dataModelsView = getSite().getWorkbenchWindow().getActivePage().showView(DataModelsView.VIEW_ID);
            if (dataModelsView instanceof DataModelsView) {
                ((DataModelsView) dataModelsView).refresh();
            }

            // / Registers a listener to Eclipse to get the list of models
            // selected (checked) by the user
            getSite().getPage().addSelectionListener(DataModelsView.VIEW_ID, this);

            IViewPart viewRes = getSite().getWorkbenchWindow().getActivePage().showView(ResultsView.VIEW_ID);
            ResultsView resView = (ResultsView) viewRes;
            fDiagnosis.setResultsAndFeedbackInstance(resView.getResultsAndFeddbackInstance());

            // Registers a part listener
            getSite().getPage().addPartListener(fPartListener);

        } catch (PartInitException e) {

            String msgTitle="TotalADS"; //$NON-NLS-1$
            if (e.getMessage() != null) {
                MessageDialog.openError(getSite().getShell(),
                        msgTitle,e.getMessage());
            } else {
                MessageDialog.openError(getSite().getShell(),
                        msgTitle,Messages.DiagnosisView_UnableToLaunch);
            }

            Logger.getLogger(DiagnosisView.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    /**
     * Sets the focus
     */
    @Override
    public void setFocus() {

    }

    /**
     * Gets called from TMF when a trace is selected
     *
     * @param signal TMF signal
     */
    @TmfSignalHandler
    public void traceSelected(final TmfTraceSelectedSignal signal) {

        ITmfTrace currentTrace = signal.getTrace();

        // ITmfTrace trace = signal.getTrace();
        // Right now we are not sure how to determine whether a trace is a user
        // space trace or kernel space trace
        // so we are only considering kernel space traces
        Boolean isKernelSpace = true;
        ITraceTypeReader traceReader = TraceTypeFactory.getInstance().getCTFKernelReaderOrSimpleTextReader(isKernelSpace);

        fDiagnosis.updateOnTraceSelection(currentTrace.getPath(), traceReader.getName());

    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.tracecompass.ui.views.TmfView#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();
        getSite().getPage().removeSelectionListener(DataModelsView.VIEW_ID, this);
        fPartListener.removeObserver(this);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.tracecompass.totalads.ui.diagnosis.IDiagnosisObserver#update
     * (org.eclipse.tracecompass.totalads.ui.results.ResultsAndFeedback)
     */
    @Override
    public void update(ResultsAndFeedback results) {
        fDiagnosis.setResultsAndFeedbackInstance(results);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.
     * IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
     */
    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {

        if (part instanceof DataModelsView) {
            Object obj = ((StructuredSelection) selection).getFirstElement();
            @SuppressWarnings("unchecked")
            HashSet<String> modelsList = (HashSet<String>) obj;
            fDiagnosis.updateonModelSelection(modelsList);
        }
    }

}
