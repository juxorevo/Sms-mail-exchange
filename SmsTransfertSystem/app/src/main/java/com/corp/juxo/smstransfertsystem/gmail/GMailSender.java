package com.corp.juxo.smstransfertsystem.gmail;

import com.corp.juxo.smstransfertsystem.gmail.secureGmail.ByteArrayDataSource;
import com.corp.juxo.smstransfertsystem.gmail.secureGmail.JSSEProvider;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class GMailSender extends javax.mail.Authenticator{

    private String mailHost = "smtp.gmail.com";
    private String user;
    private String password;
    private Session session;

    private String subject;
    private String body;
    private String sender;
    private String recipients;
    private Properties props;

    private Multipart _multipart = new MimeMultipart();


    private static List<GMailSender> mailAEnvoyer = new ArrayList<>();

    static {
        Security.addProvider(new JSSEProvider());
    }


    public GMailSender(String user, String password, String s, String b, String se, String re) {
        this.user = user;
        this.password = password;
        subject = s;
        body = b;
        sender = se;
        recipients = re;
        configMail();
        mailAEnvoyer.add(this);
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

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }

    public synchronized void sendMail(){
        try {
            MimeMessage message = new MimeMessage(session);
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
            messageBodyPart.setText(body);
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