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
import org.eclipse.tracecompass.totalads.exceptions.TotalADSGeneralException;
import org.eclipse.tracecompass.totalads.ui.models.SettingsForm;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.tracecompass.totalads.ui.models.create.Messages;
import org.eclipse.ui.PlatformUI;

/**
 * This class creates a page on the new model wizard. The page contains the
 * settings of an algorithm that can be adjusted be a user.
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class AlgorithmSettingsPage extends WizardPage {
    private String[] fSettings;
    private Composite fCompSettings;
    private SettingsForm fSettingForm;

    /**
     * Constructor
     */
    public AlgorithmSettingsPage() {
        super(Messages.AlgorithmSettingsPage_AlgorithmSettings);
        setTitle(Messages.AlgorithmSettingsPage_AdjustSettings);

    }

    @Override
    public void createControl(Composite compParent) {
        fCompSettings = new Composite(compParent, SWT.NONE);
        fCompSettings.setLayout(new GridLayout(1, false));
        setControl(fCompSettings);
        setPageComplete(false);

    }

    /**
     * Sets the settings of an algorithm to the fields on the page
     *
     * @param settings
     *            Algorithm settings
     */
    public void setSettings(String[] settings) {
        this.fSettings = settings;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean isVisible) {
        if (isVisible) {
            try {
                if (fSettings != null) {
                    Control[] widgets = fCompSettings.getChildren();
                    for (int i = 0; i < widgets.length; i++) {
                        widgets[i].dispose();
                    }
                    fCompSettings.layout();

                    fSettingForm = new SettingsForm(fSettings, fCompSettings);

                    fCompSettings.layout();
                    setPageComplete(true);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        super.setVisible(isVisible);

    }

    /**
     * Returns the selected settings from a user
     *
     * @return An array of fSettings
     */
    public String[] getSettingsSelectedByTheUser() {
        String[] settingFromUser = null;

        try {
            settingFromUser = fSettingForm.getSettings();
        } catch (TotalADSGeneralException e) {
           String msgTitle="TotalADS";  //$NON-NLS-1$
           MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                   msgTitle,e.getMessage());


        }
        return settingFromUser;

    }

    @Override
    public boolean canFlipToNextPage() {
        return false;
    }

}
