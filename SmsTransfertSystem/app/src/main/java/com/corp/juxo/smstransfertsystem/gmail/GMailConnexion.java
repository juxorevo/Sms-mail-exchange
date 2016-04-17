package com.corp.juxo.smstransfertsystem.gmail;

import android.content.Context;

import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

/**
 * Created by Juxo on 17/04/2016.
 */
public class GMailConnexion extends javax.mail.Authenticator{

    public static GMailConnexion myConnexion;

    public static final String ENVOIESECURE = "ContenuSpecialTxt -- ";
    protected static final String mailHost = "smtp.gmail.com";

    protected Session session = null;

    protected String userName;
    protected String password;
    protected Context mContext;

    private Properties props;

    public GMailConnexion(Context c, String u, String p){

        if(myConnexion==null){
            userName = u;
            password = p;
            configMail();
            myConnexion = this;
        }
        mContext = c ;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
    }

    private void configMail() {
        props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", mailHost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");
        session = Session.getDefaultInstance(props, this);
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
