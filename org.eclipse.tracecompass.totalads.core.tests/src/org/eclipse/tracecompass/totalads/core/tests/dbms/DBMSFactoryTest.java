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

package org.eclipse.tracecompass.totalads.core.tests.dbms;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.UnknownHostException;

import org.eclipse.tracecompass.totalads.exceptions.TotalADSDBMSException;
import org.eclipse.tracecompass.totalads.dbms.DBMSFactory;
import org.eclipse.tracecompass.totalads.dbms.IDataAccessObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

/**
 * Test cases for {@link DBMSFactoryTest} class
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class DBMSFactoryTest {
    private static MongodExecutable mongodExe;
    private static MongodProcess mongod;

    /**
     * Initial setup before testing
     *
     * @throws UnknownHostException
     *             unknown host exception
     * @throws IOException
     *             IO exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws UnknownHostException, IOException {

        MongodStarter runtime = MongodStarter.getDefaultInstance();

        mongodExe = runtime.prepare(new MongodConfigBuilder()
                .version(Version.Main.V2_4)
                .net(new Net(12345, Network.localhostIsIPv6()))

                .build());

        mongod = mongodExe.start();

    }

    /**
     * Destroying the DBMS instances after the execution of tests
     */
    @AfterClass
    public static void tearDownAfterClass() {
        if (mongod != null) {

            mongod.stop();
            mongodExe.stop();
        }
    }

    /**
     * Tests the getDataAccessObject function
     */
    @Test
    public void testGetDataAccessObject() {
        DBMSFactory.INSTANCE.openConnection("localhost", 12345);
        IDataAccessObject dao = DBMSFactory.INSTANCE.getDataAccessObject();
        DBMSFactory.INSTANCE.closeConnection();
        assertTrue(dao != null);

    }

    /**
     * Tests the testCloseConnection function
     */
    @Test
    public void testCloseConnection() {
        DBMSFactory.INSTANCE.openConnection("localhost", 12345);
        DBMSFactory.INSTANCE.closeConnection();
        assertTrue(true);// if it reaches here then then test passes

    }

    /**
     * Tests the openConnection function
     */
    @Test
    public void testOpenConnection() {
        String err = DBMSFactory.INSTANCE.openConnection("localhost", 12345);
        DBMSFactory.INSTANCE.closeConnection();
        assertTrue(err.isEmpty());

    }

    /**
     * Tests the openConnectionWthUserNameAndPassword
     */
    @Test
    public void testOpenConnectionWithUserNameAndPassword() {

        String err = DBMSFactory.INSTANCE.openConnection("localhost", 12345, "", "", "local");
        DBMSFactory.INSTANCE.closeConnection();
        assertTrue(!err.isEmpty());

    }

    /**
     * Tests the deleteDatabase function
     *
     * @throws TotalADSDBMSException
     *             DBMS exception
     */
    @Test
    public void testDeleteDatabase() throws TotalADSDBMSException {
        DBMSFactory.INSTANCE.openConnection("localhost", 12345);
        IDataAccessObject dao = DBMSFactory.INSTANCE.getDataAccessObject();
        String[] collections = { "collection1", "collection2" };
        dao.createDatabase("temp", collections);
        String msg = DBMSFactory.INSTANCE.deleteDatabase("temp");
        DBMSFactory.INSTANCE.closeConnection();
        assertTrue(msg.isEmpty());
    }

    /**
     * Tests the verifyConnection function
     */
    @Test
    public void testVerifyConnection() {

        String msg = DBMSFactory.INSTANCE.verifyConnection();
        assertTrue(msg.isEmpty());

    }

}
