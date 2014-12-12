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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tracecompass.totalads.ui.models.dbconnect.Messages;

/**
 * This class implements basic configurations to connect to a database
 * management system
 *
 * @author <p>
 *         Syed Shariyar Murtaza
 *         </p>
 *
 */
public class DBConfigurationPage extends WizardPage {
    // Variables
    private Label fLblHost;
    private Text fTxtHost;
    private Label fLblPort;
    private Text fTxtPort;
    private boolean fIsEmpty;
    private final String HOST = "localhost"; //$NON-NLS-1$
    private final String PORT = "27017"; //$NON-NLS-1$

    /**
     * Constructor
     *
     */
    public DBConfigurationPage() {
        super(Messages.DBConfigurationPage_DBConfigureTitle);
        setTitle(Messages.DBConfigurationPage_BasicDBConfigTitle);
        setDescription(Messages.DBConfigurationPage_PressFinishOrNext);
        fIsEmpty = false;
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

        fLblHost = new Label(compConfigure, SWT.NONE);
        fLblHost.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
        fLblHost.setText(Messages.DBConfigurationPage_3);

        fTxtHost = new Text(compConfigure, SWT.BORDER);
        fTxtHost.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
        fTxtHost.setText(HOST);

        fLblPort = new Label(compConfigure, SWT.NONE);
        fLblPort.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
        fLblPort.setText(Messages.DBConfigurationPage_4);

        fTxtPort = new Text(compConfigure, SWT.BORDER);
        fTxtPort.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
        fTxtPort.setText(PORT);

        setControl(compConfigure);
        setPageComplete(true);
        // Event handlers
        fTxtHost.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                if (fTxtHost.getText().isEmpty()) {
                    fIsEmpty = true;
                    setPageComplete(false);
                } else {
                    fIsEmpty = false;
                    setPageComplete(true);
                }
            }
        });

        fTxtPort.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                if (fTxtPort.getText().isEmpty()) {
                    fIsEmpty = true;
                    setPageComplete(false);
                } else {
                    fIsEmpty = false;
                    setPageComplete(true);
                }
            }
        });

    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
     */
    @Override
    public boolean canFlipToNextPage() {
        if (!fIsEmpty) {
            return true;
        }
        return false;

    }

    /**
     * Returns the name of the host
     *
     * @return host name
     */
    public String getHost() {
        return fTxtHost.getText();
    }

    /**
     * Returns the name of the port
     *
     * @return port number
     */
    public String getPort() {
        return fTxtPort.getText();
    }

}
