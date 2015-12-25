// IRemoteCheckMail.aidl
package com.corp.juxo.smstransfertsystem.services;

// Declare any non-default types here with import statements

interface IRemoteCheckMail {

    void stopSystem();
    void startSystem();
    void setEnabled(boolean b);
    void setUsername(String u);
    void setPassword(String p);
    boolean isOnline();
}
