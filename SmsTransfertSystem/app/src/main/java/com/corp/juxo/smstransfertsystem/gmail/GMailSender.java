package com.corp.juxo.smstransfertsystem.gmail;

import com.corp.juxo.smstransfertsystem.gmail.secureGmail.ByteArrayDataSource;
import com.corp.juxo.smstransfertsystem.gmail.secureGmail.JSSEProvider;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class GMailSender extends javax.mail.Authenticator{

    private String subject;
    private String body;
    private String sender;
    private String recipients;

    private Multipart _multipart = new MimeMultipart();
    private static List<GMailSender> mailAEnvoyer = new ArrayList<>();

    private GMailConnexion gConnexion;

    static {
        Security.addProvider(new JSSEProvider());
    }


    public GMailSender(GMailConnexion con, String s, String b, String re) {
        gConnexion=con;
        subject = s;
        body = b;
        sender = "System@juxoCorp.com";
        recipients = re;
        mailAEnvoyer.add(this);
    }

    public GMailSender(GMailConnexion con, String s, String b, String re, String attachement) throws Exception {
        gConnexion=con;
        subject = s;
        body = b;
        sender = "System@juxoCorp.com";
        recipients = re;
        mailAEnvoyer.add(this);
        addAttachment(attachement);
    }





    public synchronized void sendMail(){
        try {
            MimeMessage message = new MimeMessage(gConnexion.getSession());
            DataHandler handler;
            if(body!=null){
                handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
            }else{
                handler = new DataHandler(new ByteArrayDataSource(" ".getBytes(), "text/plain"));
            }

            message.setSender(new InternetAddress(sender));
            message.setSubject(subject);
            message.setDataHandler(handler);
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(GMailConnexion.ENVOIESECURE + " " + body);
            _multipart.addBodyPart(messageBodyPart);

            // Put parts in message
            message.setContent(_multipart);
            if (recipients.indexOf(',') > 0) {
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
            }else {
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
            }
            Transport.send(message);
        } catch (Exception e) {
            System.out.println("Envoie mail erreur " + e);
        }

    }

    public void addAttachment(String filename) throws Exception {
        BodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName("download image");
        _multipart.addBodyPart(messageBodyPart);
    }

    public synchronized static List<GMailSender> getMailAEnvoyer() {
        return mailAEnvoyer;
    }

    public static void setMailAEnvoyer(List<GMailSender> m) {
        mailAEnvoyer = m;
    }
}