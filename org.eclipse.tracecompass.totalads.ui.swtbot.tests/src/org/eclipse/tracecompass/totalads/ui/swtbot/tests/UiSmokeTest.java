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

package org.eclipse.tracecompass.totalads.ui.swtbot.tests;

import static org.junit.Assert.*;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.keyboard.Keystrokes;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Provides smoke tests for TotalADS by testing the basic user interface
 * @author <p>
 *         Syed Shariyar Murtaza jusstshary@hotmail.com
 *         </p>
 *
 */
@RunWith(SWTBotJunit4ClassRunner.class)
public class UiSmokeTest extends AbstractUiTest {

    /**
     * Tests the Models DB view in TotalADS
     */
    @Test
    public void testDBModelView() {

        SWTBotView botView = new SWTBotView(getViewPartRef("Models DB"), fBot);
        assertNotNull(botView);
        botView.setFocus();
        SWTBotTable table = botView.bot().table();
        assertNotNull(table);
        table.getTableItem(0).select();

        botView.toolbarButton("Creates a new model").click();
        clickMessageBoxWithOK();

        botView.toolbarButton("Adjusts settings for testing").click();
        clickMessageBoxWithOK();

        botView.toolbarButton("Connects to a database").click();
        fBot.button("Finish").click();
        clickMessageBoxWithOK();
        fBot.button("Cancel").click();

        botView.toolbarButton("Deletes a model").click();
        clickMessageBoxWithOK();

        botView = null;
        assertTrue(true);
    }

    /**
     * Tests the diagnosis view in TotalADS
     */
    @Test
    public void testDiagnosis() {
        SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
        SWTBotView botView = new SWTBotView(getViewPartRef("Diagnosis"), fBot);
        assertNotNull(botView);
        botView.setFocus();
        SWTBot bot = botView.bot();

        bot.comboBox().setSelection("LTTng System Call");
        bot.comboBox().setSelection("Simple Text File");

        bot.text().setText("/tmp");
        bot.button("Browse...").click();
        bot.button("Browse...").pressShortcut(Keystrokes.ESC);

        bot.radio("Select the Kernel Trace in Project Explorer").click();
        bot.radio("Select the Folder Containing Test Traces").click();

        bot.button("    Start Evaluation").click();
        clickMessageBoxWithOK();
        bot = null;
        botView = null;
        assertTrue(true);
    }

    /**
     * Tests the modeling view in TotalADS
     */
    @Test
    public void testModelingView() {
        SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
        SWTBotView botView = new SWTBotView(getViewPartRef("Modeling"), fBot);
        assertNotNull(botView);
        botView.setFocus();
        SWTBot bot = botView.bot();

        bot.comboBox().setSelection("LTTng System Call");
        bot.comboBox().setSelection("Simple Text File");

        bot.textWithLabel("Select the Folder Containing Training Traces").setText("/tmp");
        bot.textWithLabel("Select the Folder Containing Validation Traces").setText("/tmp");
        bot.button("Start Modeling").click();
        clickMessageBoxWithOK();
        bot.button("Browse...").click();
        bot.button("Browse...").pressShortcut(Keystrokes.ESC);
        bot = null;
        botView = null;
        assertTrue(true);

    }

    /**
     * Tests the live monitor view in TotalADS
     */
    @Test
    public void testLiveMonitor() {
        SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
        SWTBotView botView = new SWTBotView(getViewPartRef("Live Monitor"), fBot);
        assertNotNull(botView);
        botView.setFocus();

        SWTBot bot = botView.bot();
        bot.comboBox(0).setSelection("15");
        bot.comboBox(1).setSelection("7");
        bot.text(0).setText("temp@temphost");
        bot.text(1).setText("abcd");
        bot.text(2).setText("222");
        bot.radio("Training and Testing").click();
        bot.radio("Testing").click();
        bot.text(3).setText("/tmp");
        bot.button("Browse...").click();
        bot.button("Browse...").pressShortcut(Keystrokes.ESC);
        bot.button("Start").click();
        clickMessageBoxWithOK();
        bot = null;
        botView = null;
        assertTrue(true);

    }

    /**
     * Tests the live results view in TotalADS
     */
    @Test
    public void testLiveResults() {

        SWTBotView botView = new SWTBotView(getViewPartRef("Live Results"), fBot);
        assertNotNull(botView);
        botView.setFocus();

        SWTBot bot = botView.bot();
        bot.tabItem("Timeline").activate();
        bot.tabItem("Details").activate();

        assertFalse(bot.text(0).isActive());
        assertFalse(bot.text(1).isActive());
        assertFalse(bot.text(2).isActive());
        assertFalse(bot.text(3).isActive());
        assertTrue(bot.comboBox(0).isEnabled());
        assertTrue(bot.tree().isEnabled());

    }

    /**
     * Tests the results view TotalADS
     */
    @Test
    public void testResults() {

        SWTBotView botView = new SWTBotView(getViewPartRef("Results"), fBot);
        assertNotNull(botView);
        botView.setFocus();

        SWTBot bot = botView.bot();

        assertFalse(bot.text(0).isActive());
        assertFalse(bot.text(1).isActive());
        assertFalse(bot.text(2).isActive());
        assertFalse(bot.text(3).isActive());
        assertTrue(bot.comboBox(0).isEnabled());
        assertTrue(bot.tree().isEnabled());

    }

    /**
     * Tests the properties view in TotalADS
     */
    @Test
    public void testProperties() {

        SWTBotView botView = new SWTBotView(getViewPartRef("Properties"), fBot);
        assertNotNull(botView);
        botView.setFocus();

        SWTBot bot = botView.bot();

        assertFalse(bot.table().isActive());
        assertTrue(bot.table().isEnabled());

    }

}