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

import static org.junit.Assert.fail;

import java.util.List;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.eclipse.tracecompass.totalads.ui.TotalAdsPerspectiveFactory;
import org.eclipse.tracecompass.tmf.ui.swtbot.tests.conditions.ConditionHelpers;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.results.BoolResult;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

/**
 * Makes sure that TotalADS's perspective initializes correctly and
 * provides common methods for testing TotalADS's UI.
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
@RunWith(SWTBotJunit4ClassRunner.class)
public abstract class AbstractUiTest {
    /** An instance of SWTWorkbenchBot **/
    protected static SWTWorkbenchBot fBot;
    /** The Log4j logger instance. */
    protected static final Logger fLogger = Logger.getRootLogger();

    /**
     *
     * Initial setup before starting SWTBot based tests on TotalADS's
     * perspective
     */
    @BeforeClass
    public static void beforeClass() {

        fBot = new SWTWorkbenchBot();
        fLogger.addAppender(new ConsoleAppender(new SimpleLayout(), ConsoleAppender.SYSTEM_OUT));

        final List<SWTBotView> openViews = fBot.views();
        for (SWTBotView view : openViews) {
            if (view.getTitle().equals("Welcome")) {
                view.close();
                fBot.waitUntil(ConditionHelpers.ViewIsClosed(view));
            }
        }

        openTotalADSPerspective();
        /* set up for swtbot */
        SWTBotPreferences.PLAYBACK_DELAY = 100; /* 300 second timeout */

    }

    /**
     * Clicks on the OK button of the message box shown by TotalADS
     */
    protected static void clickMessageBoxWithOK() {
        fBot.shell("TotalADS").bot().button("OK").click();
    }

    /**
     * Opens the perspective of TotalADS
     */
    protected static void openTotalADSPerspective() {
        final Exception retE[] = new Exception[1];
        if (!UIThreadRunnable.syncExec(new BoolResult() {
            @Override
            public Boolean run() {
                try {
                    PlatformUI.getWorkbench().showPerspective(TotalAdsPerspectiveFactory.ID,
                            PlatformUI.getWorkbench().getActiveWorkbenchWindow());
                } catch (WorkbenchException e) {
                    retE[0] = e;
                    return false;
                }
                return true;
            }
        })) {
            fail(retE[0].getMessage());
        }

    }

    /**
     * Returns a reference to a view in a perspective
     *
     * @param viewTile
     *            The name of the view
     * @return A reference to the view
     */
    protected static IViewReference getViewPartRef(final String viewTile) {
        final IViewReference[] vrs = new IViewReference[1];
        UIThreadRunnable.syncExec(new VoidResult() {
            @Override
            public void run() {
                IViewReference[] viewRefs = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences();
                for (IViewReference viewRef : viewRefs) {
                    IViewPart vp = viewRef.getView(true);
                    if (vp.getTitle().equals(viewTile)) {
                        vrs[0] = viewRef;
                        return;
                    }
                }
            }
        });

        return vrs[0];
    }

}
