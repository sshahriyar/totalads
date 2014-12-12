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

package org.eclipse.tracecompass.totalads.ui.models.dbconnect;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tracecompass.totalads.ui.models.dbconnect.Messages;

/**
 * This class implements advance configurations to connect to a database
 * management system
 *
 * @author <p>
 *         Syed Shariyar Murtaza
 *         </p>
 *
 */
public class AdvanceDBConfigurationPage extends WizardPage {
    // Variables
    private Label fLblUser;
    private Text fTxtUser;
    private Label fLblPassword;
    private Text fTxtPassword;
    private Label fLblDatabase;
    private Text fTxtDatabase;

    /**
     * Constructor
     */
    public AdvanceDBConfigurationPage() {
        super(Messages.AdvanceDBConfigurationPage_AdvanceConfigureTitle);
        setTitle(Messages.AdvanceDBConfigurationPage_AdvanDBConfigTitle);

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
     * .Composite)
     */
    @Override
    public void createControl(Composite compParent) {

        Composite compConfigure = new Composite(compParent, SWT.NONE);
        compConfigure.setLayoutData(new GridData(GridData.FILL_BOTH));
        compConfigure.setLayout(new GridLayout(2, false));

        fLblUser = new Label(compConfigure, SWT.NONE);
        fLblUser.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
        fLblUser.setText(Messages.AdvanceDBConfigurationPage_UserName);

        fTxtUser = new Text(compConfigure, SWT.BORDER);
        fTxtUser.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
        fTxtUser.setText(""); //$NON-NLS-1$

        fLblPassword = new Label(compConfigure, SWT.NONE);
        fLblPassword.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
        fLblPassword.setText(Messages.AdvanceDBConfigurationPage_Password);

        fTxtPassword = new Text(compConfigure, SWT.BORDER | SWT.PASSWORD);
        fTxtPassword.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
        fTxtPassword.setText(""); //$NON-NLS-1$

        fLblDatabase = new Label(compConfigure, SWT.NONE);
        fLblDatabase.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
        fLblDatabase.setText(Messages.AdvanceDBConfigurationPage_Database);

        fTxtDatabase = new Text(compConfigure, SWT.BORDER);
        fTxtDatabase.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
        fTxtDatabase.setText(""); //$NON-NLS-1$

        setControl(compConfigure);
        setPageComplete(true);

    }

    /**
     * Returns the user name
     *
     * @return User name
     */
    public String getUserName() {
        return fTxtUser.getText();
    }

    /**
     * Returns the password
     *
     * @return Password
     */
    public String getPassword() {
        return fTxtPassword.getText();
    }

    /**
     * Returns the database
     *
     * @return Database
     */
    public String getDatabase() {
        return fTxtDatabase.getText();
    }

}
