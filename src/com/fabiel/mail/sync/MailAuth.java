/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fabiel.mail.sync;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 *
 * @author Fabiel <fabiel.leon at gmail.com>
 */
public class MailAuth extends Authenticator {
    private final String user;
    private final String pass;

    public MailAuth(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(user,pass
            );
        }
    }

