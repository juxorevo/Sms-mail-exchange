package com.corp.juxo.smstransfertsystem.gmail;

import android.content.Context;
import android.content.Intent;

import com.corp.juxo.smstransfertsystem.sms.Sms;

import java.io.IOException;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

/**
 * Created by Juxo on 30/10/2015.
 */
public class GMailReceiver {

    private static final java.lang.String SEPARATEUR = "--";
    private static final String RETURNCHAR = "(\\r|\\n)";
    private static final String BLANKCHAR = "";
    public static final String ENVOIESECURE = "ContenuSpecialTxt -- ";
    private static final String ENVOIERETOUR = "Re";
    private static final String identificateurEnvoie = "txtmsg";

    private Properties properties = null;
    private Session session = null;
    private Store store = null;
    private Folder inbox = null;
    private String userName;// provide user name
    private String password;// provide password

    private Intent intentSent;
    public static boolean ENCOURS;

    private Context mContext;

    public GMailReceiver(Context c, Intent i, String user, String pass) {
        mContext = c;
        intentSent = i;
        properties = new Properties();
        properties.setProperty("mail.host", "imap.gmail.com");
        properties.setProperty("mail.port", "995");
        properties.setProperty("mail.transport.protocol", "imaps");
        userName = user;
        password = pass;
    }

    public void readMails() {
        ENCOURS = true;
        session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(userName, password);
                    }
                });
        try {
            store = session.getStore("imaps");
            store.connect();
            inbox = store.getFolder("inbox");
            inbox.open(Folder.READ_WRITE);
            Message messages[] = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            getExtractionContent(messages);
            inbox.close(true);
            store.close();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e1) {
            e1.printStackTrace();
        }
        ENCOURS = false;
    }

    public void getExtractionContent(Message[] messages) throws MessagingException {
        String msg = "";
        for (Message message : messages) {
            if (message.getSubject().contains(identificateurEnvoie) && message.getFrom()[0].toString().contains(userName)) {
                String identificateur = message.getSubject().split(SEPARATEUR)[0];
                String phoneNumber = message.getSubject().split(SEPARATEUR)[1];
                msg = processMessageBody(message).replaceAll(RETURNCHAR, BLANKCHAR);
                if (identificateur.contains(ENVOIERETOUR) && !msg.contains(ENVOIESECURE)) {
                    new Sms(mContext, msg, phoneNumber);
                    message.setFlag(Flags.Flag.DELETED, true);
                }else{
                    message.setFlag(Flags.Flag.SEEN, false);
                }

            }else{
                message.setFlag(Flags.Flag.SEEN, false);
            }
        }

    }

    public String processMessageBody(Message message) {
        try {
            Object content = message.getContent();
            // check for string
            // then check for multipart
            if (content instanceof Multipart) {
                Multipart multiPart = (Multipart) content;
                return procesMultiPart(multiPart);
            } else if (content instanceof String) {
                return (String) content;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MessagingException e1) {
            e1.printStackTrace();
        }
        return "problème pour envoyer le message";
    }

    public String procesMultiPart(Multipart content) {

        try {
            int multiPartCount = content.getCount();
            for (int i = 0; i < multiPartCount; i++) {
                BodyPart bodyPart = content.getBodyPart(i);
                Object o;

                o = bodyPart.getContent();
                if (o instanceof String) {
                    return (String) o;
                } else if (o instanceof Multipart) {
                    procesMultiPart((Multipart) o);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MessagingException e1) {
            e1.printStackTrace();
        }
        return "probleme pour envoyer le message";
    }

}
