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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.tracecompass.totalads.algorithms.AlgorithmUtility;
import org.eclipse.tracecompass.totalads.algorithms.IDetectionAlgorithm;
import org.eclipse.tracecompass.totalads.dbms.DBMSFactory;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSDBMSException;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSGeneralException;
import org.eclipse.tracecompass.totalads.ui.models.create.AlgorithmSelectionPage;
import org.eclipse.tracecompass.totalads.ui.models.create.AlgorithmSettingsPage;
import org.eclipse.tracecompass.totalads.ui.models.create.Messages;
import org.eclipse.tracecompass.totalads.ui.models.create.ModelNamePage;
import org.eclipse.tracecompass.totalads.ui.models.settings.TestSettingsHandler;
import org.eclipse.ui.PlatformUI;

/**
 * This class generates a wizard dialog box for the creation of a new model
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class CreateModelWizard extends Wizard {
    private AlgorithmSelectionPage fPageAlgoSelection;
    private AlgorithmSettingsPage fPageAlgoSettings;
    private ModelNamePage fModelPage;

    /**
     * Constructor
     */
    public CreateModelWizard() {
        super();
        setNeedsProgressMonitor(true);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.jface.wizard.Wizard#getWindowTitle()
     */
    @Override
    public String getWindowTitle() {
        return Messages.CreateModelWizard_NewModel;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages() {
        fPageAlgoSelection = new AlgorithmSelectionPage();
        fPageAlgoSettings = new AlgorithmSettingsPage();
        fModelPage = new ModelNamePage();
        addPage(fPageAlgoSelection);
        addPage(fModelPage);
        addPage(fPageAlgoSettings);

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.jface.wizard.Wizard#getNextPage(org.eclipse.jface.wizard.
     * IWizardPage)
     */
    @Override
    public IWizardPage getNextPage(IWizardPage page) {
        if (page.equals(fPageAlgoSelection)) {

            IDetectionAlgorithm alg = fPageAlgoSelection.getSelectedAlgorithm();
            String[] settings = alg.getTrainingSettings();
            fPageAlgoSettings.setSettings(settings);

        }

        return super.getNextPage(page);
    }

    /*
     * Creates a model in the database when the Finish button is clicked
     * (non-Javadoc)
     *
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        IDetectionAlgorithm alg = fPageAlgoSelection.getSelectedAlgorithm();
        String modelName = fModelPage.gettheModel();
        String[] settings = fPageAlgoSettings.getSettingsSelectedByTheUser();
        String msgTitle="TotalADS"; //$NON-NLS-1$
        String exception = ""; //$NON-NLS-1$
        if (settings == null)
        {
            return false;
        }
        try {
            AlgorithmUtility.createModel(modelName, alg, settings);

        } catch (TotalADSDBMSException e) {
            exception = e.getMessage();
        } catch (TotalADSGeneralException e) {
            exception = e.getMessage();
        } catch (Exception ex) {
            exception = ex.getMessage();
            Logger.getLogger(TestSettingsHandler.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            // Check if connection still exists and all the views are notified
            // of the presence and absence of connection
            DBMSFactory.INSTANCE.verifyConnection();
        }

        if (exception != null && !exception.isEmpty()) {
            MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                    msgTitle, exception);
            return false;
        }
        return true;

    }

}
