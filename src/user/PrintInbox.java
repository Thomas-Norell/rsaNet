package user;
import client.Client;
import rsa.PrivateKeys;
import server.Account;

import java.util.ArrayList;

class PrintInbox implements Runnable {
    public Thread t;
    boolean suspended = false;
    Account myAccount;
    PrivateKeys k;
    Client client;
    int size = 0;

    PrintInbox(Account myAccount, PrivateKeys k, Client client) {
        this.myAccount = myAccount;
        this.k = k;
        this.client = client;
    }

    public void run() {
        try {
            while (true) {
                ArrayList<String> messages= Procedures.read(myAccount, k, client);
                if (messages.size() != size) {
                    System.out.println(messages.get(messages.size() -1));
                    size = messages.size();
                }
                synchronized(this) {
                    while(suspended) {
                        wait();
                    }
                }
            }
        } catch (InterruptedException e) {
        }
    }

    public void start() {
        if (t == null) {
            t = new Thread (this);
            t.start();
        }
    }

    void suspend() {
        suspended = true;
    }

    synchronized void resume() {
        suspended = false;
        notify();
    }
}