/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fabiel.mail.sync;

import java.util.Properties;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

/**
 *
 * @author miguel
 */
public class MailSync {

    private Store storein;
    private Store storeout;
    private Synker synker;
//    private static final ThreadGroup GROUP = new ThreadGroup("MailSync");

    public MailSync(Store storein, Store storeout) {
        this.storein = storein;
        this.storeout = storeout;
    }

    public MailSync(Properties storein, Properties storeout) throws NoSuchProviderException {
        this(Session.getInstance(storein), Session.getInstance(storeout));
    }

    public MailSync(Session storein, Session storeout) throws NoSuchProviderException {
        this(storein.getStore(), storeout.getStore());
    }

    public void sync() {
        if (!synker.isAlive()) {
            synker = new Synker(storein, storeout);
            synker.start();
        } else {
            throw new IllegalStateException("Sync Started Already");
        }
    }

    public void stopSync() throws InterruptedException {
        synker.cancel();
    }
//    public static void stopSyncs() {
//GROUP.
//    }

}
