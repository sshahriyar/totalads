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
package org.eclipse.tracecompass.totalads.ui.modeling;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.tracecompass.totalads.ui.modeling.Messages;
import org.eclipse.tracecompass.totalads.ui.modeling.Modeling;
import org.eclipse.tracecompass.totalads.ui.models.DataModelsView;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

/**
 * Creates a modeling view based on the Eclipse ViewPart class
 *
 * @author <p>
 *         Efraim Lopez
 *         </p>
 *         <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class ModelingView extends ViewPart {
    /**
     * View ID
     */
    public static final String VIEW_ID = "org.eclipse.tracecompass.totalads.ui.ModelingView"; //$NON-NLS-1$
    // Variables
    private Modeling fModeling;
    private SelectionListener fSelectionListener;

    // ///////////////////////////////////////
    // /Inner class implementing a listener for another view
    // //////////////////////////////////////
    private class SelectionListener implements ISelectionListener {
        @Override
        public void selectionChanged(IWorkbenchPart part, ISelection selection) {

            if (part instanceof DataModelsView) {
                Object obj = ((org.eclipse.jface.viewers.StructuredSelection) selection).getFirstElement();
                @SuppressWarnings("unchecked")
                HashSet<String> modelList = (HashSet<String>) obj;
                fModeling.updateonModelSelection(modelList);
            }
        }
    }

    /**
     * Constructor
     */
    public ModelingView() {
        fModeling = new Modeling();
        fSelectionListener = new SelectionListener();
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
        fModeling.createControls(compParent);
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
            getSite().getPage().addSelectionListener(DataModelsView.VIEW_ID, fSelectionListener);

        } catch (PartInitException e) {
            String msgTitle="TotalADS"; //$NON-NLS-1$
            if (e.getMessage() != null) {
                MessageDialog.openError( getSite().getShell(), msgTitle, e.getMessage());
            } else {
                MessageDialog.openError( getSite().getShell(), msgTitle,Messages.ModelingView_UnableToLaunch);
            }

            Logger.getLogger(Modeling.class.getName()).log(Level.SEVERE, null, e);
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
        getSite().getPage().removeSelectionListener(DataModelsView.VIEW_ID, fSelectionListener);
    }

}
