package server;

import client.Message;
import rsa.PublicKeys;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Account implements Serializable{
    private String userName;
    private PublicKeys publicKeys;
    volatile List inbox;

    public Account(String userName, PublicKeys publicKeys) {
        this.userName = userName;
        this.publicKeys = publicKeys;
        inbox = new ArrayList<Message>();
    }
    public String getUserName() {
        return userName;
    }

    public PublicKeys getPublicKeys() {
        return publicKeys;
    }

    public List<Message> getInbox() {
        return inbox;

    }
    public void addInbox(Message m) {
        this.inbox.add(m);

    }
}
