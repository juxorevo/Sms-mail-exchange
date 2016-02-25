package com.corp.juxo.smstransfertsystem.tools;

/**
 * Created by Juxo on 25/02/2016.
 */
public class MmsInformation {
    public String phoneNumber;
    public String image;
    public String contactName;
    public String message;

    public MmsInformation(String phoneNumber, String image, String contactName,String message) {
        this.phoneNumber = phoneNumber;
        this.image = image;
        this.contactName = contactName;
        this.message = message;
    }
}
