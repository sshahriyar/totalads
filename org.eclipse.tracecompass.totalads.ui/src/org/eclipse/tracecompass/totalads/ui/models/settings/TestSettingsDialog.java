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

package org.eclipse.tracecompass.totalads.ui.models.settings;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.tracecompass.totalads.algorithms.IDetectionAlgorithm;
import org.eclipse.tracecompass.totalads.dbms.DBMSFactory;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSDBMSException;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSGeneralException;
import org.eclipse.tracecompass.totalads.ui.models.SettingsForm;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tracecompass.totalads.ui.models.settings.Messages;

/**
 * This class implements the settings dialog box which is shown when test
 * settings is selected
 *
 * @author <p>
 *         Syed Shariyar Murtaza
 *         </p>
 *
 */
public class TestSettingsDialog extends TitleAreaDialog {
    // Variables
    private String[] fSettingsForAlgorithm;
    private SettingsForm fSettingsForm;
    //private MessageBox fMsgErr;
    private IDetectionAlgorithm fAlgorithm;
    private String fModelName;

    /**
     * Constructor
     *
     * @param parentShell
     *            Parent shell
     * @param algorithm
     *            Algorithm
     * @param modelName
     *            Model name
     * @param settings
     *            Settings of the model
     */
    public TestSettingsDialog(Shell parentShell, IDetectionAlgorithm algorithm, String modelName, String[] settings) {
        super(parentShell);
        this.fSettingsForAlgorithm = settings;
        this.fAlgorithm = algorithm;
        this.fModelName = modelName;
      //  fMsgErr = new MessageBox(parentShell, SWT.ERROR);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.jface.dialogs.Dialog#create()
     */
    @Override
    public void create() {
        super.create();
        setTitle(Messages.TestSettingsDialog_SettingsTitle);

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.jface.dialogs.TitleAreaDialog#createDialogArea(org.eclipse
     * .swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite compParent) {
        String msgTitle="TotalADS"; //$NON-NLS-1$
        Composite compSuper = (Composite) super.createDialogArea(compParent);
        Composite compSettingsDialog = new Composite(compSuper, SWT.NONE);
        compSettingsDialog.setLayoutData(new GridData(GridData.FILL_BOTH));
        compSettingsDialog.setLayout(new GridLayout(1, false));
        try {
            fSettingsForm = new SettingsForm(fSettingsForAlgorithm, compSettingsDialog);
        } catch (TotalADSGeneralException e) {
            MessageDialog.openError(org.eclipse.ui.PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                    msgTitle , e.getMessage());

            cancelPressed();
        }

        return compSuper;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.jface.dialogs.Dialog#isResizable()
     */
    @Override
    protected boolean isResizable() {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed() {
        try {

            fSettingsForAlgorithm = fSettingsForm.getSettings();
            fAlgorithm.saveTestSettings(fSettingsForAlgorithm, fModelName, DBMSFactory.INSTANCE.getDataAccessObject());

        } catch (TotalADSGeneralException ex) {
            setErrorMessage(ex.getMessage());
            return;
        } catch (TotalADSDBMSException ex) {
            setErrorMessage(ex.getMessage());
            return;
        }

        super.okPressed();
    }

}
