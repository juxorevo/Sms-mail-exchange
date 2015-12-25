// IRemoteCheckMail.aidl
package com.corp.juxo.smstransfertsystem.services;

// Declare any non-default types here with import statements

interface IRemoteCheckMail {

    boolean getEnabled();
    void reload();
    void setEnabled(boolean b);
    void setUsername(String u);
    void setPassword(String p);
}
