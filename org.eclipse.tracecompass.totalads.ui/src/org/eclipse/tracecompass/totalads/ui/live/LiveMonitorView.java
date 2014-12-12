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

package org.eclipse.tracecompass.totalads.ui.live;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.tracecompass.totalads.ui.live.ILiveObserver;
import org.eclipse.tracecompass.totalads.ui.live.LiveMonitor;
import org.eclipse.tracecompass.totalads.ui.live.LivePartListener;
import org.eclipse.tracecompass.totalads.ui.live.LiveResultsView;
import org.eclipse.tracecompass.totalads.ui.live.Messages;
import org.eclipse.tracecompass.totalads.ui.models.DataModelsView;
import org.eclipse.tracecompass.totalads.ui.results.ResultsAndFeedback;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

/**
 * This class creates the LiveMonitor View
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class LiveMonitorView extends ViewPart implements ISelectionListener, ILiveObserver {
    /**
     * VIEW ID
     */
    public static final String VIEW_ID = "org.eclipse.tracecompass.totalads.ui.LiveMonitorView"; //$NON-NLS-1$
    // Variables declaration
    private LiveMonitor fLiveMonitor;
    private LivePartListener fPartListener;

    /**
     * Constructor
     */
    public LiveMonitorView() {
        fLiveMonitor = new LiveMonitor();
        fPartListener = new LivePartListener();
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
        fLiveMonitor.createControls(compParent);

        // / Registers a listener to Eclipse to get the list of models selected
        // (checked) by the user
        getSite().getPage().addSelectionListener(DataModelsView.VIEW_ID, this);

        // Registers a part listener to Eclipse
        getSite().getPage().addPartListener(fPartListener);
        try {

            IViewPart viewRes = getSite().getWorkbenchWindow().getActivePage().showView(LiveResultsView.VIEW_ID);
            LiveResultsView liveView = (LiveResultsView) viewRes;

            fLiveMonitor.setLiveChart(liveView.getLiveChart());
            fLiveMonitor.setResultsAndFeedback(liveView.getResults());

            // Trying to clear the already selected instances in the models view
            // when this view is opened in the middle of execution
            // If the view is opened in the middle, already selected models are
            // not available using the event handler
            IViewPart dataModelsView = getSite().getWorkbenchWindow().getActivePage().showView(DataModelsView.VIEW_ID);
            if (dataModelsView instanceof DataModelsView) {
                ((DataModelsView) dataModelsView).refresh();
            }

        } catch (PartInitException e) {
            String msgTitle="TotalADS"; //$NON-NLS-1$
            if (e.getMessage() != null) {

                MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                         msgTitle,e.getMessage());
            } else {
                MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                        msgTitle,Messages.LiveMonitorView_UnableToLaunch);
            }

            Logger.getLogger(LiveMonitor.class.getName()).log(Level.SEVERE, null, e);

        }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    @Override
    public void setFocus() {

    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.ui.part.WorkbenchPart#dispose()
     */
    @Override
    public void dispose() {
        getSite().getPage().removeSelectionListener(DataModelsView.VIEW_ID, this);
        fPartListener.removeObserver(this);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.tracecompass.totalads.ui.live.ILiveObserver#update(org.
     * eclipse.tracecompass.totalads.ui.results.ResultsAndFeedback)
     */
    @Override
    public void update(ResultsAndFeedback results) {
        fLiveMonitor.setResultsAndFeedback(results);

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
            Object obj = ((org.eclipse.jface.viewers.StructuredSelection) selection).getFirstElement();
            @SuppressWarnings("unchecked")
            HashSet<String> modelList = (HashSet<String>) obj;
            fLiveMonitor.updateOnModelSelction(modelList);
        }
    }

}
