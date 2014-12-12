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

package org.eclipse.tracecompass.totalads.ui.models.create;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.tracecompass.totalads.algorithms.AlgorithmTypes;
import org.eclipse.tracecompass.totalads.algorithms.IDetectionAlgorithm;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tracecompass.totalads.ui.models.create.AlgorithmTreeContentProvider;
import org.eclipse.tracecompass.totalads.ui.models.create.AlgorithmTreeLabelProvider;
import org.eclipse.tracecompass.totalads.ui.models.create.Messages;

/**
 * This class creates the algorithm selection page
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class AlgorithmSelectionPage extends WizardPage {
    private CheckboxTreeViewer fTreeViewer;
    private StyledText fTxtDescription;
    private Boolean fIsItemSelected;
    private IDetectionAlgorithm fAlgorithmSelected;

    /**
     * Constructor
     */
    public AlgorithmSelectionPage() {
        super(Messages.AlgorithmSelectionPage_AlgSelTitle);
        setTitle(Messages.AlgorithmSelectionPage_SelAlgorithm);
        fIsItemSelected = false;

    }

    //
    // Creates GUI widgets
    //
    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
     * .Composite)
     */
    @Override
    public void createControl(Composite compParent) {
        Composite compAlgorithms = new Composite(compParent, SWT.NONE);
        compAlgorithms.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
        compAlgorithms.setLayout(new GridLayout(2, false));

        fTreeViewer = new CheckboxTreeViewer(compAlgorithms);
        fTreeViewer.getTree().setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, true));
        fTreeViewer.setContentProvider(new AlgorithmTreeContentProvider());
        fTreeViewer.setLabelProvider(new AlgorithmTreeLabelProvider());
        fTreeViewer.setInput(AlgorithmTypes.ANOMALY);

        fTxtDescription = new StyledText(compAlgorithms, SWT.MULTI | SWT.READ_ONLY | SWT.NONE | SWT.WRAP | SWT.V_SCROLL);
        fTxtDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        fTxtDescription.setText(Messages.AlgorithmSelectionPage_SelectAlgForDescription);

        // Event handler for the tree
        fTreeViewer.addCheckStateListener(new ICheckStateListener() {

            @Override
            public void checkStateChanged(CheckStateChangedEvent event) {

                IDetectionAlgorithm algorithm = ((IDetectionAlgorithm) event.getElement());
                String algorithmName = algorithm.getName();
                for (int i = 0; i < fTreeViewer.getTree().getItemCount(); i++) {
                    if (!fTreeViewer.getTree().getItem(i).getText().equalsIgnoreCase(algorithmName)) {
                        fTreeViewer.getTree().getItem(i).setChecked(false);// Make
                                                                           // all
                                                                           // unchecked
                    } else {
                        fIsItemSelected = fTreeViewer.getTree().getItem(i).getChecked();
                    }
                }
                if (fIsItemSelected) {
                    fAlgorithmSelected = algorithm;
                    String desc = algorithm.getDescription();
                    if (desc != null) {
                        fTxtDescription.setText(desc);
                    } else {
                        fTxtDescription.setText(Messages.AlgorithmSelectionPage_NoDescription);
                    }

                }
                else {
                    fAlgorithmSelected = null;
                    fTxtDescription.setText(Messages.AlgorithmSelectionPage_SelectAlgForDescription);
                }

                // getWizard().getContainer().updateButtons();
                setPageComplete(true);

            }
        });

        setControl(compAlgorithms);
        setPageComplete(false);
    }

    //
    // This function enables next button
    //
    @Override
    public boolean canFlipToNextPage() {
        if (fIsItemSelected) {
            return true;
        }
        return false;
    }

    /**
     * Gets the currently selected algorithm
     *
     * @return Algorithm object
     */
    public IDetectionAlgorithm getSelectedAlgorithm() {
        return fAlgorithmSelected;
    }
}
