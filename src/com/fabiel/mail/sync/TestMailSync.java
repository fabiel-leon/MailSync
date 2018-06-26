/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fabiel.mail.sync;

import java.security.Security;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;

/**
 *
 * @author Fabiel <fabiel.leon at gmail.com>
 */
public class TestMailSync {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {

            String user = "admin@emsume.sld.cu";
//IMAP IN
            Properties propIn = new Properties();
            propIn.put("mail.store.protocol", "imap");
            propIn.put("mail.imap.host", "192.168.1.2");
            propIn.put("mail.imap.starttls.enable", "true");
            propIn.put("mail.imap.auth", "true");
//            propIn.put("mail.imap.password", "Imbadbad123");

//IMAP OUT
            Properties propOut = new Properties();
            propOut.put("mail.store.protocol", "imap");
            propOut.put("mail.host", "192.168.1.155");
            propOut.put("mail.imap.starttls.enable", "true");
            propOut.put("mail.imap.auth", "true");
//            propOut.put("mail.imap.password", "cme2016*");
            //        propIn.put("mail.imap.socketFactory.fallback", "false");
            //        properties.put("mail.imap.poNewInterfacert", "995");
            //        properties.put("mail.imap.socketFactory.port", "995");
            //Para SSL
            Security.setProperty("ssl.SocketFactory.provider", "com.fabiel.security.SSLSocketFactorySimple");
            Session sIn = Session.getInstance(propIn, new MailAuth(user, "Cme2015*"));
//            sIn.setDebug(true);
            Session sOut = Session.getInstance(propOut, new MailAuth(user, "cme2015*"));
//            sOut.setDebug(true);
            Synker synker = new Synker(
                    sIn.getStore(),
                    sOut.getStore()
            );

            synker.start();
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(TestMailSync.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
