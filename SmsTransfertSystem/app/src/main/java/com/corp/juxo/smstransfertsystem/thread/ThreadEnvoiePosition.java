package com.corp.juxo.smstransfertsystem.thread;

/**
 * Created by Juxo on 30/10/2015.
 */
public class ThreadEnvoiePosition extends Thread {

    private String commands;
    private String numero;

    public ThreadEnvoiePosition(String cmd, String num){
        commands = cmd;
        numero = num;
    }
    //TODO A FAIRE
    public void run() {
        //Commands.executeCommande(commands, numero);
    }
}
