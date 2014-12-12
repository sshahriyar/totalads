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

package org.eclipse.tracecompass.totalads.ui.io;


import org.eclipse.tracecompass.totalads.ui.io.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.KeyEvent;

/**
 * This class takes care of directory browsing on a hard disk from the GUI
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class DirectoryBrowser {
    private Button fBtnTraceBrowser;
    private Composite fParent;
    private Text fTxtPath;

    // private Text txtTraceID;//,txtTraceSource,txtTraceCounter;

    /**
     * Constructor
     *
     * @param parent
     *            Composite object
     * @param txtBox
     *            Text box
     * @param gridData
     *            Grid layout
     */
    public DirectoryBrowser(Composite parent, Text txtBox, GridData gridData) {
        this.fTxtPath = txtBox;
        this.fParent = parent;
        fBtnTraceBrowser = new Button(parent, SWT.NONE);
        fBtnTraceBrowser.setLayoutData(gridData);
        fBtnTraceBrowser.setText(Messages.DirectoryBrowser_Browse);
        fBtnTraceBrowser.addMouseListener(new MouseUpEvent());
        fBtnTraceBrowser.addKeyListener(new KeyPressEvent());

    }

    /**
     * Constructor
     *
     * @param parent
     *            Composite
     * @param gridData
     *            Grid Layout
     */
    public DirectoryBrowser(Composite parent, GridData gridData) {

        this.fParent = parent;
        fBtnTraceBrowser = new Button(parent, SWT.NONE);
        fBtnTraceBrowser.setLayoutData(gridData);
        fBtnTraceBrowser.setText(Messages.DirectoryBrowser_BrowseTraces);
        fBtnTraceBrowser.addMouseListener(new MouseUpEvent());
        fBtnTraceBrowser.addKeyListener(new KeyPressEvent());

    }

    /**
     * Sets the trace path text box to a local variable, which is updated when a
     * user clicks browse
     *
     * @param text
     *            Text box
     */
    public void setTextBox(Text text) {
        this.fTxtPath = text;
    }

    /**
     * Disables the browse button
     */
    public void disableBrowsing() {
        this.fBtnTraceBrowser.setEnabled(false);
    }

    /**
     * Enables the browse button
     */
    public void enableBrowsing() {
        this.fBtnTraceBrowser.setEnabled(true);
    }

    /**
     * Method to open directory dialog box
     */
    private void directoryDialog() {

        DirectoryDialog dD = new DirectoryDialog(fParent.getShell());
        dD.setText(Messages.DirectoryBrowser_Open);
        if (!fTxtPath.getText().isEmpty()) {
            dD.setFilterPath(fTxtPath.getText());
        }

        String path = dD.open();

        if (path != null) {
            this.fTxtPath.setText(path);
        }

    }

    // ///////////////////////////////////////////////////////////////////////////////////
    //
    // Inner classes (event handlers) for listeners of GUI elements (widgets)
    //
    // /////////////////////////////////////////////////////////////////////////////////
    /** Inner class for mouse up event on the button */
    private class MouseUpEvent extends MouseAdapter {
        @Override
        public void mouseUp(MouseEvent e) {
            directoryDialog();
        }
    }

    /** Inner class for name press event on the button */
    private class KeyPressEvent extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            directoryDialog();
        }
    }

}
