package client;

import server.Account;

import java.io.Serializable;

public abstract class Request implements Serializable{
    protected Account sender;
    protected Account recipient;

    public final Account getSender() {
        return sender;
    }
    public final Account getRecipient() {
        return recipient;
    }
}
