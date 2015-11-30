package com.corp.juxo.smstransfertsystem.thread;

import com.corp.juxo.smstransfertsystem.tools.Commands;

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

    public void run() {
        Commands.executeCommande(commands, numero);
    }
}
