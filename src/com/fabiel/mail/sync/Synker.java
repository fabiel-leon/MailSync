/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fabiel.mail.sync;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;

/**
 *
 * @author miguel
 */
class Synker extends Thread {

    private final Store storein;
    private final Store storeout;
    private boolean cancel;

    public Synker(Store storein, Store storeout) {
        this.storein = storein;
        this.storeout = storeout;
    }

    @Override
    public void run() {
        try {
            storein.connect();
            storeout.connect();
            sync(storein.getDefaultFolder());
//            storein.addFolderListener(new FolderAdapterImpl(currentout));
        } catch (MessagingException ex) {
            Logger.getLogger(Synker.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                storein.close();
                storeout.close();
            } catch (MessagingException ex) {
                Logger.getLogger(Synker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void cancel() throws InterruptedException {
        cancel = true;
        this.join();

    }

//    private static class FolderAdapterImpl extends FolderAdapter {
//
//        public FolderAdapterImpl(Folder currentout) {
//            super(currentout);
//        }
//
//        @Override
//        public void folderCreated(FolderEvent e) {
//            super.folderCreated(e); //To change body of generated methods, choose Tools | Templates.
//        }
//
//        @Override
//        public void folderRenamed(FolderEvent e) {
//            super.folderRenamed(e); //To change body of generated methods, choose Tools | Templates.
//        }
//    }
    private void sync(Folder folder) {
        try {

            if ((folder.getType() & Folder.HOLDS_MESSAGES) != 0) {
                System.out.println("folder = " + folder.getFullName() + " puede contener mensajes");
//fetch profile                
                FetchProfile fp = new FetchProfile();
//                fp.add(FetchProfile.Item.ENVELOPE);
                fp.add("Message-ID");

//open in folder and fetch messages id
                folder.open(Folder.READ_ONLY);
                Message[] msgs = folder.getMessages();
                System.out.println("msgs = " + msgs.length);
//                folder.fetch(msgs, fp);
//
////create and open out folder and fetch messages id
                Folder folderout = storeout.getFolder(folder.getFullName());
                folderout.create(folder.getType());
                folderout.open(Folder.READ_WRITE);
//                Message[] msgsOut = folderout.getMessages();
//                folderout.fetch(msgsOut, fp);
//
////copiar mensajes que no se repiten               
//                Message[] copy = compare(msgs, msgsOut);
                folder.copyMessages(msgs, folderout);

//cerrar in folder       
                folder.close(false);

//cerrar out folder             
                folderout.close(false);
            }
            if ((folder.getType() & Folder.HOLDS_FOLDERS) != 0) {
                System.out.println("folder = " + folder.getFullName() + " puede contener carpetas");
                Folder[] list = folder.list();
                for (Folder nextFolder : list) {
                    System.out.println("nextFolder = " + nextFolder.getFullName());
                    sync(nextFolder);
                }
            }

        } catch (MessagingException ex) {
            Logger.getLogger(Synker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Message[] compare(Message[] msgs, Message[] msgsOut) {
        ArrayList arrayList = new ArrayList();
        boolean isIn = false;
        for (Message message : msgs) {
            for (Message messageOut : msgsOut) {
                try {
                    if (message.getHeader("Message-ID") != null && messageOut.getHeader("Message-ID") != null && message.getHeader("Message-ID")[0].equals(messageOut.getHeader("Message-ID")[0])) {
                        isIn = true;
                        break;
                    }
                } catch (MessagingException ex) {
                    Logger.getLogger(Synker.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (isIn) {
                isIn = false;
            } else {
                arrayList.add(message);
            }

        }
        System.out.println("copiando " + arrayList.size() + " mensajes");
        return (Message[]) arrayList.toArray(new Message[arrayList.size()]);
    }
}
