package server;

import client.Message;
import rsa.PublicKeys;

import java.io.Serializable;
import java.util.ArrayList;

public class Account implements Serializable{
    private String userName;
    private PublicKeys publicKeys;
    private ArrayList<Message> inbox;

    public Account(String userName, PublicKeys publicKeys) {
        this.userName = userName;
        this.publicKeys = publicKeys;
        this.inbox = new ArrayList();
    }
    public String getUserName() {
        return userName;
    }

    public PublicKeys getPublicKeys() {
        return publicKeys;
    }

    public ArrayList<Message> getInbox() {
        return inbox;
    }
    public void addInbox(Message m) {
        this.inbox.add(m);
    }
}
