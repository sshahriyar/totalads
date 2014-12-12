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

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tracecompass.totalads.ui.models.create.Messages;
import org.eclipse.ui.PlatformUI;

/**
 * This class creates the model name page
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class ModelNamePage extends WizardPage {
    private Label lblModelName;
    private StyledText txtDescription;
    private Text txtModelName;
    private Composite compModel;
    private Boolean isModelOK;

    /**
     * Constructor
     */
    public ModelNamePage() {
        super(Messages.ModelNamePage_ModelTitle);
        setTitle(Messages.ModelNamePage_EnterModelName);
        isModelOK = false;

    }

    //
    // Creates GUI widgets
    //
    /*
     *
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
     * .Composite)
     */
    @Override
    public void createControl(Composite compParent) {
        compModel = new Composite(compParent, SWT.NONE);
        compModel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
        compModel.setLayout(new GridLayout(3, false));

        lblModelName = new Label(compModel, SWT.NONE);
        lblModelName.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false));
        lblModelName.setText(Messages.ModelNamePage_ModelLabel);

        txtModelName = new Text(compModel, SWT.BORDER);
        txtModelName.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        txtModelName.setTextLimit(20);

        // Empty labels used for styling
        Label lbl1 = new Label(compModel, SWT.NONE);
        lbl1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        Label lbl2 = new Label(compModel, SWT.NONE);
        lbl2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 2));

        txtDescription = new StyledText(compModel, SWT.NONE | SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
        txtDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 8));
        txtDescription.setJustify(true);
        txtDescription.setText(Messages.ModelNamePage_Description);

        setControl(compModel);
        setPageComplete(false);

        // Event handler
        txtModelName.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {

                if (txtModelName.getText().matches(".*[@;:,!~`()*&^%_<>\"{}|_=+\\\\?\\/\\[\\]].*")) { //$NON-NLS-1$
                    String msgTitle = "TotalADS"; //$NON-NLS-1$
                    MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                            msgTitle, Messages.ModelNamePage_NoSpecialCharacters);
                    txtModelName.setText(txtModelName.getText().substring(0, txtModelName.getText().length() - 1));
                    txtModelName.setSelection(txtModelName.getText().length());
                    isModelOK = true;
                }

                if (txtModelName.getText().isEmpty()) {
                    isModelOK = false;
                } else {
                    isModelOK = true;
                }

                setPageComplete(true);
            }
        });
    }

    //
    // This function enables next button
    //
    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
     */
    @Override
    public boolean canFlipToNextPage() {
        if (isModelOK) {
            return true;
        }
        return false;
    }

    /**
     * Returns the name of the model typed by the user
     *
     * @return Model name
     */
    public String gettheModel() {
        return txtModelName.getText();
    }
}
